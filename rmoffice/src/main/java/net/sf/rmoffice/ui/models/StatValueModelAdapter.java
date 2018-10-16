/*
 * Copyright 2012 Daniel Nettesheim
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

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import net.sf.rmoffice.core.AbstractPropertyChangeSupport;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.generator.StatGainGenerator;
import net.sf.rmoffice.meta.Race;
import net.sf.rmoffice.meta.enums.StatEnum;

import com.jgoodies.binding.beans.BeanAdapter;


/**
 * 
 */
public class StatValueModelAdapter extends AbstractPropertyChangeSupport implements PropertyChangeListener {
	private final static ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private final BeanAdapter<RMSheet> rmSheetAdapter;

	public StatValueModelAdapter(BeanAdapter<RMSheet> rmSheetAdapter) {
		this.rmSheetAdapter = rmSheetAdapter;
		this.rmSheetAdapter.addBeanPropertyChangeListener(this);
	}

	/* ============== temp stats =============== */
	private String getTemp(StatEnum stat) {
		if (rmSheetAdapter.getBean() == null) return "0";
		return Integer.toString(rmSheetAdapter.getBean().getStatTemp(stat));
	}
	
	public String getAGILITYTemp() {
		return getTemp(StatEnum.AGILITY);
	}
	public String getCONSTITUTIONTemp() {
		return getTemp(StatEnum.CONSTITUTION);
	}
	public String getMEMORYTemp() {
		return getTemp(StatEnum.MEMORY);
	}
	public String getREASONINGTemp() {
		return getTemp(StatEnum.REASONING);
	}
	public String getSELFDISCIPLINETemp() {
		return getTemp(StatEnum.SELFDISCIPLINE);
	}
	public String getEMPATHYTemp() {
		return getTemp(StatEnum.EMPATHY);
	}
	public String getINTUITIONTemp() {
		return getTemp(StatEnum.INTUITION);
	}
	public String getPRESENCETemp() {
		return getTemp(StatEnum.PRESENCE);
	}
	public String getQUICKNESSTemp() {
		return getTemp(StatEnum.QUICKNESS);
	}
	public String getSTRENGTHTemp() {
		return getTemp(StatEnum.STRENGTH);
	}
	/**/
	private void setTemp(StatEnum stat, String value) {
		rmSheetAdapter.getBean().setStatTemp(stat, convert(value), false);
		firePropertyChange(stat.name()+"StatPotDice", null, getStatPotDice(stat));
	}
	public void setAGILITYTemp(String value) {
		setTemp(StatEnum.AGILITY, value);
	}

	public void setCONSTITUTIONTemp(String value) {
		setTemp(StatEnum.CONSTITUTION, value);
	}
	public void setMEMORYTemp(String value) {
		setTemp(StatEnum.MEMORY, value);
	}
	public void setREASONINGTemp(String value) {
		setTemp(StatEnum.REASONING, value);
	}
	public void setSELFDISCIPLINETemp(String value) {
		setTemp(StatEnum.SELFDISCIPLINE, value);
	}
	public void setEMPATHYTemp(String value) {
		setTemp(StatEnum.EMPATHY, value);
	}
	public void setINTUITIONTemp(String value) {
		setTemp(StatEnum.INTUITION, value);
	}
	public void setPRESENCETemp(String value) {
		setTemp(StatEnum.PRESENCE, value);
	}
	public void setQUICKNESSTemp(String value) {
		setTemp(StatEnum.QUICKNESS, value);
	}
	public void setSTRENGTHTemp(String value) {
		setTemp(StatEnum.STRENGTH, value);
	}
	/* ============== pot stats =============== */
	private String getPot(StatEnum stat) {
		if (rmSheetAdapter.getBean() == null) return "0";
		return Integer.toString(rmSheetAdapter.getBean().getStatPot(stat));
	}
	
	public String getAGILITYPot() {
		return getPot(StatEnum.AGILITY);
	}
	public String getCONSTITUTIONPot() {
		return getPot(StatEnum.CONSTITUTION);
	}
	public String getMEMORYPot() {
		return getPot(StatEnum.MEMORY);
	}
	public String getREASONINGPot() {
		return getPot(StatEnum.REASONING);
	}
	public String getSELFDISCIPLINEPot() {
		return getPot(StatEnum.SELFDISCIPLINE);
	}
	public String getEMPATHYPot() {
		return getPot(StatEnum.EMPATHY);
	}
	public String getINTUITIONPot() {
		return getPot(StatEnum.INTUITION);
	}
	public String getPRESENCEPot() {
		return getPot(StatEnum.PRESENCE);
	}
	public String getQUICKNESSPot() {
		return getPot(StatEnum.QUICKNESS);
	}
	public String getSTRENGTHPot() {
		return getPot(StatEnum.STRENGTH);
	}
	/**/
	public void setPot(StatEnum stat, String value) {
		rmSheetAdapter.getBean().setStatPot(stat, convert(value), false);
		firePropertyChange("statPotButtonsVisible", null, Boolean.valueOf(getStatPotButtonsVisible()));
		firePropertyChange("statGainButtonsVisible", null, Boolean.valueOf(getStatGainButtonsVisible()));
	}
	public void setAGILITYPot(String value) {
		setPot(StatEnum.AGILITY, value);
	}
	public void setCONSTITUTIONPot(String value) {
		setPot(StatEnum.CONSTITUTION, value);
	}
	public void setMEMORYPot(String value) {
		setPot(StatEnum.MEMORY, value);
	}
	public void setREASONINGPot(String value) {
		setPot(StatEnum.REASONING, value);
	}
	public void setSELFDISCIPLINEPot(String value) {
		setPot(StatEnum.SELFDISCIPLINE, value);
	}
	public void setEMPATHYPot(String value) {
		setPot(StatEnum.EMPATHY, value);
	}
	public void setINTUITIONPot(String value) {
		setPot(StatEnum.INTUITION, value);
	}
	public void setPRESENCEPot(String value) {
		setPot(StatEnum.PRESENCE, value);
	}
	public void setQUICKNESSPot(String value) {
		setPot(StatEnum.QUICKNESS, value);
	}
	public void setSTRENGTHPot(String value) {
		setPot(StatEnum.STRENGTH, value);
	}
	
	/* ============== misc stats =============== */
	private String getMisc(StatEnum stat) {
		if (rmSheetAdapter.getBean() == null) return "0";
		return Integer.toString(rmSheetAdapter.getBean().getStatMiscBonus(stat));
	}
	
	public String getAGILITYMisc() {
		return getMisc(StatEnum.AGILITY);
	}
	public String getCONSTITUTIONMisc() {
		return getMisc(StatEnum.CONSTITUTION);
	}
	public String getMEMORYMisc() {
		return getMisc(StatEnum.MEMORY);
	}
	public String getREASONINGMisc() {
		return getMisc(StatEnum.REASONING);
	}
	public String getSELFDISCIPLINEMisc() {
		return getMisc(StatEnum.SELFDISCIPLINE);
	}
	public String getEMPATHYMisc() {
		return getMisc(StatEnum.EMPATHY);
	}
	public String getINTUITIONMisc() {
		return getMisc(StatEnum.INTUITION);
	}
	public String getPRESENCEMisc() {
		return getMisc(StatEnum.PRESENCE);
	}
	public String getQUICKNESSMisc() {
		return getMisc(StatEnum.QUICKNESS);
	}
	public String getSTRENGTHMisc() {
		return getMisc(StatEnum.STRENGTH);
	}
	/**/
	private void setMisc(StatEnum stat, String value) {
		rmSheetAdapter.getBean().setStatMiscBonus(stat, convert(value), false);
	}
	
	public void setAGILITYMisc(String value) {
		setMisc(StatEnum.AGILITY, value);
	}
	public void setCONSTITUTIONMisc(String value) {
		setMisc(StatEnum.CONSTITUTION, value);
	}
	public void setMEMORYMisc(String value) {
		setMisc(StatEnum.MEMORY, value);
	}
	public void setREASONINGMisc(String value) {
		setMisc(StatEnum.REASONING, value);
	}
	public void setSELFDISCIPLINEMisc(String value) {
		setMisc(StatEnum.SELFDISCIPLINE, value);
	}
	public void setEMPATHYMisc(String value) {
		setMisc(StatEnum.EMPATHY, value);
	}
	public void setINTUITIONMisc(String value) {
		setMisc(StatEnum.INTUITION, value);
	}
	public void setPRESENCEMisc(String value) {
		setMisc(StatEnum.PRESENCE, value);
	}
	public void setQUICKNESSMisc(String value) {
		setMisc(StatEnum.QUICKNESS, value);
	}
	public void setSTRENGTHMisc(String value) {
		setMisc(StatEnum.STRENGTH, value);
	}
	/* ============== misc stats =============== */
	private String getMisc2(StatEnum stat) {
		if (rmSheetAdapter.getBean() == null) return "0";
		return Integer.toString(rmSheetAdapter.getBean().getStatMisc2Bonus(stat));
	}
	
	public String getAGILITYMisc2() {
		return getMisc2(StatEnum.AGILITY);
	}
	public String getCONSTITUTIONMisc2() {
		return getMisc2(StatEnum.CONSTITUTION);
	}
	public String getMEMORYMisc2() {
		return getMisc2(StatEnum.MEMORY);
	}
	public String getREASONINGMisc2() {
		return getMisc2(StatEnum.REASONING);
	}
	public String getSELFDISCIPLINEMisc2() {
		return getMisc2(StatEnum.SELFDISCIPLINE);
	}
	public String getEMPATHYMisc2() {
		return getMisc2(StatEnum.EMPATHY);
	}
	public String getINTUITIONMisc2() {
		return getMisc2(StatEnum.INTUITION);
	}
	public String getPRESENCEMisc2() {
		return getMisc2(StatEnum.PRESENCE);
	}
	public String getQUICKNESSMisc2() {
		return getMisc2(StatEnum.QUICKNESS);
	}
	public String getSTRENGTHMisc2() {
		return getMisc2(StatEnum.STRENGTH);
	}
	
	/* ======================race bonus =========================== */
	private String getStatBonus(StatEnum stat) {
		if (rmSheetAdapter.getBean() == null) return "0";
		return Integer.toString(rmSheetAdapter.getBean().getStatBonus(stat));
	}
	
	public String getAGILITYStatBonus() {
		return getStatBonus(StatEnum.AGILITY);
	}
	public String getCONSTITUTIONStatBonus() {
		return getStatBonus(StatEnum.CONSTITUTION);
	}
	public String getMEMORYStatBonus() {
		return getStatBonus(StatEnum.MEMORY);
	}
	public String getREASONINGStatBonus() {
		return getStatBonus(StatEnum.REASONING);
	}
	public String getSELFDISCIPLINEStatBonus() {
		return getStatBonus(StatEnum.SELFDISCIPLINE);
	}
	public String getEMPATHYStatBonus() {
		return getStatBonus(StatEnum.EMPATHY);
	}
	public String getINTUITIONStatBonus() {
		return getStatBonus(StatEnum.INTUITION);
	}
	public String getPRESENCEStatBonus() {
		return getStatBonus(StatEnum.PRESENCE);
	}
	public String getQUICKNESSStatBonus() {
		return getStatBonus(StatEnum.QUICKNESS);
	}
	public String getSTRENGTHStatBonus() {
		return getStatBonus(StatEnum.STRENGTH);
	}
	/* ======================race bonus =========================== */
	private String getRaceBonus(StatEnum stat) {
		if (rmSheetAdapter.getBean() == null) return "0";
		Race race = rmSheetAdapter.getBean().getRace();
		if (race == null) return "0";
		return Integer.toString(race.getStatBonus(stat));
	}
	
	public String getAGILITYRace() {
		return getRaceBonus(StatEnum.AGILITY);
	}

	public String getCONSTITUTIONRace() {
		return getRaceBonus(StatEnum.CONSTITUTION);
	}
	public String getMEMORYRace() {
		return getRaceBonus(StatEnum.MEMORY);
	}
	public String getREASONINGRace() {
		return getRaceBonus(StatEnum.REASONING);
	}
	public String getSELFDISCIPLINERace() {
		return getRaceBonus(StatEnum.SELFDISCIPLINE);
	}
	public String getEMPATHYRace() {
		return getRaceBonus(StatEnum.EMPATHY);
	}
	public String getINTUITIONRace() {
		return getRaceBonus(StatEnum.INTUITION);
	}
	public String getPRESENCERace() {
		return getRaceBonus(StatEnum.PRESENCE);
	}
	public String getQUICKNESSRace() {
		return getRaceBonus(StatEnum.QUICKNESS);
	}
	public String getSTRENGTHRace() {
		return getRaceBonus(StatEnum.STRENGTH);
	}
	/* ================= total stat bonus ================== */
	private String getTotalBonus(StatEnum stat) {
		if (rmSheetAdapter.getBean() == null) return "0";
		return Integer.toString(rmSheetAdapter.getBean().getStatBonusTotal(stat));
	}
	public String getAGILITYTotal() {
		return getTotalBonus(StatEnum.AGILITY);
	}
	public String getCONSTITUTIONTotal() {
		return getTotalBonus(StatEnum.CONSTITUTION);
	}
	public String getMEMORYTotal() {
		return getTotalBonus(StatEnum.MEMORY);
	}
	public String getREASONINGTotal() {
		return getTotalBonus(StatEnum.REASONING);
	}
	public String getSELFDISCIPLINETotal() {
		return getTotalBonus(StatEnum.SELFDISCIPLINE);
	}
	public String getEMPATHYTotal() {
		return getTotalBonus(StatEnum.EMPATHY);
	}
	public String getINTUITIONTotal() {
		return getTotalBonus(StatEnum.INTUITION);
	}
	public String getPRESENCETotal() {
		return getTotalBonus(StatEnum.PRESENCE);
	}
	public String getQUICKNESSTotal() {
		return getTotalBonus(StatEnum.QUICKNESS);
	}
	public String getSTRENGTHTotal() {
		return getTotalBonus(StatEnum.STRENGTH);
	}
	/* ========= color ============ */
	private Color getForeground(StatEnum stat) {
		if (rmSheetAdapter.getBean() == null || rmSheetAdapter.getBean().getProfession() == null) return Color.BLACK;
		if (rmSheetAdapter.getBean().getProfession().getStats().contains(stat)) {
			return Color.BLUE;
		} else {
			return Color.BLACK;
		}
	}
	public Color getAGILITYForeground() {
		return getForeground(StatEnum.AGILITY);
	}
	public Color getCONSTITUTIONForeground() {
		return getForeground(StatEnum.CONSTITUTION);
	}
	public Color getMEMORYForeground() {
		return getForeground(StatEnum.MEMORY);
	}
	public Color getREASONINGForeground() {
		return getForeground(StatEnum.REASONING);
	}
	public Color getSELFDISCIPLINEForeground() {
		return getForeground(StatEnum.SELFDISCIPLINE);
	}
	public Color getEMPATHYForeground() {
		return getForeground(StatEnum.EMPATHY);
	}
	public Color getINTUITIONForeground() {
		return getForeground(StatEnum.INTUITION);
	}
	public Color getPRESENCEForeground() {
		return getForeground(StatEnum.PRESENCE);
	}
	public Color getQUICKNESSForeground() {
		return getForeground(StatEnum.QUICKNESS);
	}
	public Color getSTRENGTHForeground() {
		return getForeground(StatEnum.STRENGTH);
	}
	
	/* ============= dice string ============= */
	
	public String getStatPotDice(StatEnum stat) {
		StringBuffer sb = new StringBuffer();
		sb.append(RESOURCE.getString("ui.stats.action.statPotDice"));
		sb.append(": ");
		if (rmSheetAdapter.getBean() != null) {
			sb.append(StatGainGenerator.getStatPotDiceString(rmSheetAdapter.getBean().getStatTemp(stat)));
		}
		return sb.toString();
	}
	
	public String getAGILITYStatPotDice() {
		return getStatPotDice(StatEnum.AGILITY);
	}
	public String getCONSTITUTIONStatPotDice() {
		return getStatPotDice(StatEnum.CONSTITUTION);
	}
	public String getMEMORYStatPotDice() {
		return getStatPotDice(StatEnum.MEMORY);
	}
	public String getREASONINGStatPotDice() {
		return getStatPotDice(StatEnum.REASONING);
	}
	public String getSELFDISCIPLINEStatPotDice() {
		return getStatPotDice(StatEnum.SELFDISCIPLINE);
	}
	public String getEMPATHYStatPotDice() {
		return getStatPotDice(StatEnum.EMPATHY);
	}
	public String getINTUITIONStatPotDice() {
		return getStatPotDice(StatEnum.INTUITION);
	}
	public String getPRESENCEStatPotDice() {
		return getStatPotDice(StatEnum.PRESENCE);
	}
	public String getQUICKNESSStatPotDice() {
		return getStatPotDice(StatEnum.QUICKNESS);
	}
	public String getSTRENGTHStatPotDice() {
		return getStatPotDice(StatEnum.STRENGTH);
	}
	
	
	/* */
	
	/**
	 * Returns true if any potential value is 0.
	 * 
	 * @return whether the potential (fix and dice) buttons are visible
	 */
	public boolean getStatPotButtonsVisible() {
		if (rmSheetAdapter.getBean() == null) return true;
		for (StatEnum stat : StatEnum.values()) {
			if (rmSheetAdapter.getBean().getStatPot(stat) == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return whether the stat gain (fix and dice) buttons are visible
	 */
	public boolean getStatGainButtonsVisible() {
		return ! getStatPotButtonsVisible();
	}
	
	/* */
	
	public String getStatCostSum() {
		return ""+rmSheetAdapter.getBean().getStatTempSum();
	}
	
	
	public String getDevPoints() {
		return ""+rmSheetAdapter.getBean().getDevPoints();
	}
	
	/* */
	
	private int convert(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private void redirect(String statStr, String suffix, Object newValue) {
		
		if (newValue != null) {
			if (newValue instanceof Integer) {
				newValue = ((Integer)newValue).toString();
			}
		} else {
			newValue = "0";
		}
		firePropertyChange(statStr+suffix, null, newValue);
	}
	
	/** {@inheritDoc} */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() != null) {
			if (evt.getPropertyName().startsWith(RMSheet.PROPERTY_STAT_BONUS_PREFIX)) {
				String statStr = evt.getPropertyName().substring(RMSheet.PROPERTY_STAT_BONUS_PREFIX.length());
				redirect(statStr, "StatBonus", getStatBonus(StatEnum.valueOf(statStr)));
				redirect(statStr, "Total", getTotalBonus(StatEnum.valueOf(statStr)));
			} else if (evt.getPropertyName().startsWith(RMSheet.PROPERTY_STAT_TEMP_PREFIX)) {
				String statStr = evt.getPropertyName().substring(RMSheet.PROPERTY_STAT_TEMP_PREFIX.length());
				redirect(statStr, "Temp", evt.getNewValue());
				redirect(statStr, "StatPotDice", getStatPotDice(StatEnum.valueOf(statStr)));
			} else if (evt.getPropertyName().startsWith(RMSheet.PROPERTY_STAT_POT_PREFIX)) {
				String statStr = evt.getPropertyName().substring(RMSheet.PROPERTY_STAT_POT_PREFIX.length());
				redirect(statStr, "Pot", evt.getNewValue());
				firePropertyChange("statPotButtonsVisible", null, Boolean.valueOf(getStatPotButtonsVisible()));
				firePropertyChange("statGainButtonsVisible", null, Boolean.valueOf(getStatGainButtonsVisible()));
			} else if (evt.getPropertyName().startsWith(RMSheet.PROPERTY_STAT_MISCBONUS_PREFIX)) {
				String statStr = evt.getPropertyName().substring(RMSheet.PROPERTY_STAT_MISCBONUS_PREFIX.length());
				redirect(statStr, "Misc", evt.getNewValue());
			} else if (evt.getPropertyName().startsWith(RMSheet.PROPERTY_STAT_MISC2BONUS_PREFIX)) {
				String statStr = evt.getPropertyName().substring(RMSheet.PROPERTY_STAT_MISC2BONUS_PREFIX.length());
				redirect(statStr, "Misc2", evt.getNewValue());
				redirect(statStr, "Total", getTotalBonus(StatEnum.valueOf(statStr)));
			} else if (evt.getPropertyName().equals(RMSheet.PROPERTY_RACE)) {
				for (StatEnum stat : StatEnum.values()) {
					firePropertyChange(stat.name()+"Race", null, getRaceBonus(stat));
				}
			} else if (evt.getPropertyName().equals(RMSheet.PROPERTY_STAT_COST_SUM)) {
				firePropertyChange(RMSheet.PROPERTY_STAT_COST_SUM, null, evt.getNewValue().toString());
			} else if (evt.getPropertyName().equals(RMSheet.PROPERTY_DEVPPOINTS)) {
				firePropertyChange(RMSheet.PROPERTY_DEVPPOINTS, null, evt.getNewValue().toString());
			} else if (evt.getPropertyName().equals(RMSheet.PROPERTY_PROFESSION)) {
				for (StatEnum stat : StatEnum.values()) {
					firePropertyChange(stat.name()+"Foreground", null, getForeground(stat));
				}
			}
		}
	}
}
