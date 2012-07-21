/*
 * Copyright 2012 Daniel Golesny
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.rmoffice.ui.models;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.sf.rmoffice.RMPreferences;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.meta.Culture;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.Profession;
import net.sf.rmoffice.meta.Race;
import net.sf.rmoffice.meta.Shield;
import net.sf.rmoffice.meta.enums.CharImagePos;
import net.sf.rmoffice.meta.enums.LengthUnit;
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.meta.enums.WeightUnit;
import net.sf.rmoffice.pdf.AbstractPDFCreator;
import net.sf.rmoffice.ui.dialog.TrainingPackDialog;
import net.sf.rmoffice.ui.panels.BasicPanel;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * Presentation model for the {@link BasicPanel}.
 */
public class BasicPresentationModel extends PresentationModel<RMSheet> implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	private final static ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	private final BeanAdapter<RMSheet> adapter;
	private final MetaData data;
	private final Frame owner;
	private ValueHolder raceProfEnabledValueHolder = new ValueHolder(true);
	private ValueHolder cultureEnabledValueHolder = new ValueHolder(false);
	private ValueHolder magicRealmEnabledValueHolder = new ValueHolder(false);
	private AbstractAction actionTrainPack;
	private ValueHolder magicRealmIn = new ValueHolder(false);
	private ValueHolder magicRealmEm = new ValueHolder(false);
	private ValueHolder magicRealmPr = new ValueHolder(false);
	private ValueHolder levelUpValueModel = new ValueHolder(RESOURCE.getString("ui.basic.levelup.start"));
	private ValueHolder availableCultures = new ValueHolder(new ArrayList<Culture>(), true);
	private ValueHolder armorModisModel = new ValueHolder("");
	private final ValueHolder enabledValueHolder;

	public BasicPresentationModel(Frame owner, final BeanAdapter<RMSheet> adapter, MetaData data, ValueHolder enabledValueHolder) {
		super(adapter.getBeanChannel());
		this.owner = owner;
		this.adapter = adapter;
		this.data = data;
		this.enabledValueHolder = enabledValueHolder;
		adapter.addBeanPropertyChangeListener(this);
		enabledValueHolder.addPropertyChangeListener("value", new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (Boolean.TRUE.equals(evt.getNewValue())) {
					magicRealmEnabledValueHolder.setValue(adapter.getBean().isMagicRealmEditable());
				} else {
					magicRealmEnabledValueHolder.setValue(false);
				}
			}
		});
		magicRealmIn.addPropertyChangeListener(new MagicRealmPropertyChangeListener(StatEnum.INTUITION, magicRealmEm, magicRealmPr));
		magicRealmEm.addPropertyChangeListener(new MagicRealmPropertyChangeListener(StatEnum.EMPATHY, magicRealmIn, magicRealmPr));
		magicRealmPr.addPropertyChangeListener(new MagicRealmPropertyChangeListener(StatEnum.PRESENCE, magicRealmIn, magicRealmEm));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (RMSheet.PROPERTY_STATE.equals(evt.getPropertyName())) {
			raceProfEnabledValueHolder.setValue(RMSheet.State.RACE_PROF_SELECTION.equals(evt.getNewValue()));
			if (RMSheet.State.NORMAL.equals(evt.getNewValue())) {
				cultureEnabledValueHolder.setValue(false);
			}
		} else if (RMSheet.PROPERTY_MAGICREALM_CHANGED.equals(evt.getPropertyName())) {
			@SuppressWarnings("unchecked")
			Set<StatEnum> stats = (Set<StatEnum>)evt.getNewValue();
			magicRealmIn.setValue(stats.contains(StatEnum.INTUITION));
			magicRealmEm.setValue(stats.contains(StatEnum.EMPATHY));
			magicRealmPr.setValue(stats.contains(StatEnum.PRESENCE));
		} else if (RMSheet.PROPERTY_ARMOR.equals(evt.getPropertyName())
				|| RMSheet.PROPERTY_ARMOR_SKILL.equals(evt.getPropertyName())) {
			armorModisModel.setValue(getArmorModisString());
		} else if (RMSheet.PROPERTY_RACE.equals(evt.getPropertyName())) {
			List<Culture> list = getCultureList();
            availableCultures.setValue(list);
			cultureEnabledValueHolder.setValue(list.size() > 1);
		} else if (RMSheet.PROPERTY_LEVELUP_MODE.equals(evt.getPropertyName())) {
			if (Boolean.TRUE.equals(evt.getNewValue())) {
				levelUpValueModel.setValue(RESOURCE.getString("ui.basic.levelup.finish"));
			} else {
				levelUpValueModel.setValue(RESOURCE.getString("ui.basic.levelup.start"));
			}
		} else if (RMSheet.PROPERTY_MAGICREALM_EDITABLE.equals(evt.getPropertyName())) {
			if (Boolean.TRUE.equals(evt.getNewValue())) {
				magicRealmEnabledValueHolder.setValue(enabledValueHolder.booleanValue());
			} else {
				magicRealmEnabledValueHolder.setValue(false);
			}
		}
	}

	private List<Culture> getCultureList() {
		return data.getCultureForRace(adapter.getBean().getRace());
	}

	/**
	 * Returns the included races.
	 * @return list of races, not {@code null}
	 */
	public List<Race> getAvailableRaces() {
		List<Race> races = new Vector<Race>();
		races.add(null);
		for (Race race : data.getRaces()) {
			if (!RMPreferences.getInstance().isExcluded(race.getScope().name()) &&
					!RMPreferences.getInstance().isExcluded(race.getSource())) {
				races.add(race);
			}
		}
		return races;
	}

	public ValueHolder getRaceProfEnabledValueHolder() {
		return raceProfEnabledValueHolder;
	}
	
	public ValueModel getAvailableCultures() {
		return availableCultures;
	}
	
	public ValueModel getCultureEnabledValueHolder() {
		return cultureEnabledValueHolder;
	}
	
	public ValueHolder getMagicRealmEnabledValueHolder() {
		return magicRealmEnabledValueHolder;
	}
	
	public List<Profession> getAvailableProfession() {
		List<Profession> profs = new ArrayList<Profession>();
		for (Profession p : data.getProfessions()) {
			if (!RMPreferences.getInstance().isExcluded(p.getSource())) {
				profs.add(p);
			}
		}
		return profs;
	}
	
	public ValueModel getMagicRealmIn() {
		return magicRealmIn;
	}
	
	public ValueModel getMagicRealmEm() {
		return magicRealmEm;
	}
	
	public ValueModel getMagicRealmPr() {
		return magicRealmPr;
	}
	
	public List<Integer> getAvailableArmors() {
		List<Integer> armors = new ArrayList<Integer>();
		for (int i=1; i <= 20; i++) {
			armors.add(Integer.valueOf(i));
		}
		return armors;
	}
	
	public List<Shield> getAvailableShields() {
		return data.getShields();
	}
	
	public Action getAddTrainPackAction() {
		if (actionTrainPack == null) {
			actionTrainPack = new AbstractAction("+") {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					TrainingPackDialog dialog = new TrainingPackDialog(owner, adapter.getBean(), data);
					dialog.setVisible(true);
				}
			};
		}
		return actionTrainPack;
	}
	
	public ValueModel getLevelUpButtonLabel() {
		return levelUpValueModel;
	}

	public List<WeightUnit> getAvailableWeightUnits() {
		return Arrays.asList(WeightUnit.values());
	}

	public List<LengthUnit> getAvailableLengthUnits() {
		return Arrays.asList(LengthUnit.values());
	}
	
	public ValueModel getArmorModis() {
		return armorModisModel;
	}
	private String getArmorModisString() {
		StringBuilder sb = new StringBuilder();
		sb.append(AbstractPDFCreator.format(adapter.getBean().getArmorManeuverModi(), false)).append(" / ");
		sb.append(AbstractPDFCreator.format(adapter.getBean().getArmorReactionModi(), false)).append(" / ");
		sb.append(""+adapter.getBean().getArmorRangeModi());
		return sb.toString();
	}

	public List<CharImagePos> getAvailableCharImgPos() {
		return Arrays.asList(CharImagePos.values());
	}
	
	/* ********************************************************
	 * delegate the checkbox changes to to sheet
	 ******************************************************** */
	private class MagicRealmPropertyChangeListener implements PropertyChangeListener {

		private final ValueHolder other1;
		private final ValueHolder other2;
		private final StatEnum stat;

		private MagicRealmPropertyChangeListener(StatEnum stat, ValueHolder other1, ValueHolder other2) {
			this.stat = stat;
			this.other1 = other1;
			this.other2 = other2;
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if ("value".equals(evt.getPropertyName())) {
				if (adapter.getBean().isMagicRealmEditable()) {
					if (Boolean.TRUE.equals(evt.getNewValue())) {
						other1.setValue(false);
						other2.setValue(false);
						Set<StatEnum> magicRealm = getBean().getMagicRealm();
						if (magicRealm == null || magicRealm.size() != 1 || ! magicRealm.contains(stat)) {
							getBean().setMagicRealm(stat);
						}
					}
				}
			}
		}
		
	}
}
