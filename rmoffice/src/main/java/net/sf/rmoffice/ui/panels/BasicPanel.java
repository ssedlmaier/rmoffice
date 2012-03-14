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
package net.sf.rmoffice.ui.panels;

import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.meta.Culture;
import net.sf.rmoffice.meta.Profession;
import net.sf.rmoffice.meta.Race;
import net.sf.rmoffice.meta.Shield;
import net.sf.rmoffice.meta.enums.CharImagePos;
import net.sf.rmoffice.meta.enums.LengthUnit;
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.meta.enums.WeightUnit;
import net.sf.rmoffice.ui.UIConstants;
import net.sf.rmoffice.ui.converters.IProgressionConverter;
import net.sf.rmoffice.ui.converters.LongToStringConverter;
import net.sf.rmoffice.ui.editor.NumberDocument;
import net.sf.rmoffice.ui.models.BasicPresentationModel;
import net.sf.rmoffice.ui.renderer.ArmorListCellRenderer;
import net.sf.rmoffice.ui.renderer.EnumListCellRenderer;
import net.sf.rmoffice.ui.renderer.RaceCellRenderer;

import org.apache.commons.lang.StringUtils;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 */
public class BasicPanel extends AbstractPanel<RMSheet> {
	private static final long serialVersionUID = 1L;
	private final static ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	/**
	 * 
	 */
	public BasicPanel(BasicPresentationModel basicModel, ValueHolder enabledValueHolder) {
		
		FormLayout layout = new FormLayout(
				COL_DEFINITION_2COLGROUPS,
				StringUtils.repeat(ROW_WITH_GAP, 8) + HEADLINE_SEPARATOR + 
				StringUtils.repeat(ROW_WITH_GAP, 3));
		layout.setColumnGroups(new int[][]{{1, 7}});

		// PanelBuilder just for the border
		PanelBuilder builder = new PanelBuilder(layout, this);
		builder.setDefaultDialogBorder();

		int row = 1;
		
		/* Race */
		AbstractValueModel raceVM = basicModel.getModel(RMSheet.PROPERTY_RACE);
		SelectionInList<Race> selModelRaces = new SelectionInList<Race>(basicModel.getAvailableRaces(), raceVM);
		JComboBox cbRace = BasicComponentFactory.createComboBox(selModelRaces, new RaceCellRenderer());
		builder.addLabel(RESOURCE.getString(getResKeyPrefix() + RMSheet.PROPERTY_RACE), CC.xy(COL_LBL_L, row));
		Bindings.bind(cbRace, "enabled", basicModel.getRaceProfEnabledValueHolder());
		builder.add(cbRace, CC.xyw(COL_COMP_L, row, 3));
		
		/* Culture */
		AbstractValueModel culturesVM = basicModel.getModel(RMSheet.PROPERTY_CULTURE);
		ValueModel cultures = basicModel.getAvailableCultures();
		SelectionInList<Culture> selModelCultures = new SelectionInList<Culture>(cultures, culturesVM);
		JComboBox cbCulture = BasicComponentFactory.createComboBox(selModelCultures);
		builder.addLabel(RESOURCE.getString(getResKeyPrefix() + RMSheet.PROPERTY_CULTURE), CC.xy(COL_LBL_R, row));
		Bindings.bind(cbCulture, "enabled", basicModel.getCultureEnabledValueHolder());
		builder.add(cbCulture, CC.xyw(COL_COMP_R, row, 3));
		row += 2;
		
		/* Profession */		
		AbstractValueModel profVM = basicModel.getModel(RMSheet.PROPERTY_PROFESSION);
		SelectionInList<Profession> selModelProfs = new SelectionInList<Profession>(basicModel.getAvailableProfession(), profVM);
		JComboBox cbProf = BasicComponentFactory.createComboBox(selModelProfs);
		builder.addLabel(RESOURCE.getString(getResKeyPrefix() + RMSheet.PROPERTY_PROFESSION), CC.xy(COL_LBL_L, row));
		Bindings.bind(cbProf, "enabled", basicModel.getRaceProfEnabledValueHolder());
		builder.add(cbProf, CC.xyw(COL_COMP_L, row, 3));

		/* magic realm */
		builder.addLabel(RESOURCE.getString("rolemaster.realm"), CC.xy(COL_LBL_R, row));
		FormLayout magRealmLayout = new FormLayout("40dlu, 2dlu, 40dlu, 2dlu, 40dlu", ROW);
		PanelBuilder mrPB = new PanelBuilder(magRealmLayout);
		JCheckBox cbIn = BasicComponentFactory.createCheckBox(basicModel.getMagicRealmIn(), RESOURCE.getString("MagicRealm."+StatEnum.INTUITION.name()+".short"));
		Bindings.bind(cbIn, "enabled", basicModel.getModel(RMSheet.PROPERTY_MAGICREALM_EDITABLE));
		mrPB.add(cbIn, CC.xy(1, 1));
		JCheckBox cbEm = BasicComponentFactory.createCheckBox(basicModel.getMagicRealmEm(), RESOURCE.getString("MagicRealm."+StatEnum.EMPATHY.name()+".short"));
		Bindings.bind(cbEm, "enabled", basicModel.getModel(RMSheet.PROPERTY_MAGICREALM_EDITABLE));
		mrPB.add(cbEm, CC.xy(3, 1));
		JCheckBox cbPr = BasicComponentFactory.createCheckBox(basicModel.getMagicRealmPr(), RESOURCE.getString("MagicRealm."+StatEnum.PRESENCE.name()+".short"));
		Bindings.bind(cbPr, "enabled", basicModel.getModel(RMSheet.PROPERTY_MAGICREALM_EDITABLE));
		mrPB.add(cbPr, CC.xy(5, 1));
		builder.add(mrPB.getPanel(), CC.xyw(COL_COMP_R, row, 3));
		row += 2;

		/* progression body & magic */
		ValueHolder alwaysFalse = new ValueHolder(false);
		addTextFieldL(RMSheet.PROPERTY_PROGRESSION_BODY, row, basicModel, alwaysFalse, new IProgressionConverter(basicModel.getModel(RMSheet.PROPERTY_PROGRESSION_BODY)), builder);
		addTextFieldR(RMSheet.PROPERTY_PROGRESSION_POWER, row, basicModel, alwaysFalse, new IProgressionConverter(basicModel.getModel(RMSheet.PROPERTY_PROGRESSION_POWER)), builder);
		row += 2;
		
		/* player and character name */
		addTextFieldL(RMSheet.PROPERTY_PLAYERNAME, row, basicModel, enabledValueHolder, null, builder);
		addTextFieldR(RMSheet.PROPERTY_CHARACTERNAME, row, basicModel, enabledValueHolder, null, builder);
		row += 2;
		
		/* exp / level */
		LongToStringConverter epConverter = new LongToStringConverter(basicModel.getModel(RMSheet.PROPERTY_EXPPOINTS));
		addTextFieldL(RMSheet.PROPERTY_EXPPOINTS, row, basicModel, enabledValueHolder, epConverter, builder).setDocument(new NumberDocument());
		LongToStringConverter lvlConverter = new LongToStringConverter(basicModel.getModel(RMSheet.PROPERTY_LEVEL));
		addTextFieldR(RMSheet.PROPERTY_LEVEL, row, basicModel, enabledValueHolder, lvlConverter, builder).setDocument(new NumberDocument());
		row += 2;

		/* Training packs */
		builder.addLabel(RESOURCE.getString("rolemaster.trainingpacks"), CC.xy(COL_LBL_L, row));
		JTextField tfApprenticeShip = BasicComponentFactory.createTextField(basicModel.getModel(RMSheet.PROPERTY_APPRENTICESHIP));
		Bindings.bind(tfApprenticeShip, "enabled", enabledValueHolder);
		builder.add(tfApprenticeShip, CC.xy(COL_COMP_L, row));
		JButton btAddTrainPack = new JButton(basicModel.getAddTrainPackAction());
		Bindings.bind(btAddTrainPack, "enabled", enabledValueHolder);
		builder.add(btAddTrainPack, CC.xy(COL_COMP_L + 2, row));

		/* level-up modus */
		JToggleButton btLevelUp = new JToggleButton();
		btLevelUp.setBackground(UIConstants.COLOR_SELECTION_BG);
		btLevelUp.setForeground(UIConstants.COLOR_SELECTION_FG);
		Bindings.bind(btLevelUp, "text", basicModel.getLevelUpButtonLabel());
		Bindings.bind(btLevelUp, basicModel.getModel(RMSheet.PROPERTY_LEVELUP_MODE));
		Bindings.bind(btLevelUp, "enabled", enabledValueHolder);
		builder.add(btLevelUp, CC.xyw(COL_COMP_R, row, 3));
		
		row += 2;
		
		/* Armor */
		builder.addLabel(RESOURCE.getString("rolemaster.armor"), CC.xy(COL_LBL_L, row));
		SelectionInList<Integer> selArmor = new SelectionInList<Integer>(basicModel.getAvailableArmors(), basicModel.getModel(RMSheet.PROPERTY_ARMOR));
		JComboBox cbArmor = BasicComponentFactory.createComboBox(selArmor,new ArmorListCellRenderer());
		Bindings.bind(cbArmor, "enabled", enabledValueHolder);
		builder.add(cbArmor, CC.xyw(COL_COMP_L, row, 3));
		
		/* Shield */
		builder.addLabel(RESOURCE.getString("rolemaster.shield"), CC.xy(COL_LBL_R, row));
		SelectionInList<Shield> selShield = new SelectionInList<Shield>(basicModel.getAvailableShields(), basicModel.getModel(RMSheet.PROPERTY_SHIELD));
		JComboBox cbShield = BasicComponentFactory.createComboBox(selShield);
		Bindings.bind(cbShield, "enabled", enabledValueHolder);
		builder.add(cbShield, CC.xyw(COL_COMP_R, row, 3));
		row += 2;

		/* armor penalties */
		builder.addLabel(RESOURCE.getString("rolemaster.penalty.ui.label"), CC.xy(COL_LBL_L, row));
		JLabel lblArmor = BasicComponentFactory.createLabel(basicModel.getArmorModis());
		builder.add(lblArmor, CC.xyw(COL_COMP_L, row, 3));
		row += 2;
		
		/* settings  */
		builder.addSeparator(RESOURCE.getString("ui.basic.settings"), CC.xyw(1, row, 9));
		row++;
		
		/* Length Unit */
		builder.addLabel(RESOURCE.getString("ui.basic.lengthunit"), CC.xy(COL_LBL_L, row));
		SelectionInList<LengthUnit> selLengUnits = new SelectionInList<LengthUnit>(basicModel.getAvailableLengthUnits(), basicModel.getModel(RMSheet.PROPERTY_LENGTH_UNIT));
		JComboBox cbLength = BasicComponentFactory.createComboBox(selLengUnits,new EnumListCellRenderer());
		Bindings.bind(cbLength, "enabled", enabledValueHolder);
		builder.add(cbLength, CC.xyw(COL_COMP_L, row, 3));
		
		/* Weight Unit */
		builder.addLabel(RESOURCE.getString("ui.basic.weightunit"), CC.xy(COL_LBL_R, row));
		SelectionInList<WeightUnit> selWeightUnits = new SelectionInList<WeightUnit>(basicModel.getAvailableWeightUnits(), basicModel.getModel(RMSheet.PROPERTY_WEIGHT_UNIT));
		JComboBox cbWeight = BasicComponentFactory.createComboBox(selWeightUnits, new EnumListCellRenderer());
		Bindings.bind(cbWeight, "enabled", enabledValueHolder);
		builder.add(cbWeight, CC.xyw(COL_COMP_R, row, 3));
		
		row += 2;
		
		/* print outline image */
		builder.addLabel(RESOURCE.getString("ui.basic.printoutline"), CC.xy(COL_LBL_L, row));
		JCheckBox cbShowOutline = BasicComponentFactory.createCheckBox(basicModel.getModel(RMSheet.PROPERTY_PRINT_OUTLINE_IMG), "");
		Bindings.bind(cbShowOutline, "enabled", enabledValueHolder);
		builder.add(cbShowOutline, CC.xyw(COL_COMP_L, row, 3));
		
		/* image position */
		builder.addLabel(RESOURCE.getString("ui.basic.imagepos"), CC.xy(COL_LBL_R, row));
		SelectionInList<CharImagePos> selCharImgList = new SelectionInList<CharImagePos>(basicModel.getAvailableCharImgPos(), basicModel.getModel(RMSheet.PROPERTY_IMG_POS));
		JComboBox cbCharImg = BasicComponentFactory.createComboBox(selCharImgList, new EnumListCellRenderer());
		Bindings.bind(cbCharImg, "enabled", enabledValueHolder);
		builder.add(cbCharImg, CC.xyw(COL_COMP_R, row, 3));
	}
	
	@Override
	public String getResKeyPrefix() {
		return "ui.basic.";
	}
	
}
