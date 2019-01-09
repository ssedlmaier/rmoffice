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
package net.sf.rmoffice.ui.editor;

import java.awt.Component;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.core.items.MagicalFeature;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.enums.MagicalItemFeatureType;
import net.sf.rmoffice.meta.enums.ResistanceEnum;
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.ui.components.StrictAutoCompletionComboBox;
import net.sf.rmoffice.ui.renderer.SkillnameComboboxRenderer;

import com.jgoodies.binding.beans.BeanAdapter;
import com.jidesoft.swing.AutoCompletionComboBox;


/**
 * 
 */
public class MagicalFeatureTableCellEditor extends AbstractComboBoxTableCellEditor {

	private static final long serialVersionUID = 1L;
	private final JTextField textfield = new JTextField();
	private final AutoCompletionComboBox cbStat;
	private AutoCompletionComboBox cbSkill;
	private final JComboBox<?> cbResistance;
	private MagicalItemFeatureType currentType;
	private final BeanAdapter<RMSheet> rmSheetAdapter;
	/**
	 * @param rmSheetAdapter the bean adapter to get custom skills, too
	 */
	public MagicalFeatureTableCellEditor(BeanAdapter<RMSheet> rmSheetAdapter) {
		super();
		this.rmSheetAdapter = rmSheetAdapter;
		cbStat = new StrictAutoCompletionComboBox(StatEnum.values());
		cbStat.addPopupMenuListener(this);
		cbResistance = new StrictAutoCompletionComboBox(ResistanceEnum.values());
		cbResistance.addPopupMenuListener(this);
		cbSkill = new StrictAutoCompletionComboBox(new Vector<ISkill>());
		cbSkill.setRenderer(new SkillnameComboboxRenderer());
		cbSkill.addPopupMenuListener(this);
	}

	/** {@inheritDoc} */
	@Override
	public Object getCellEditorValue() {
		switch (currentType) {
			case STAT:
				return cbStat.getSelectedItem();
			case SKILL:
				return cbSkill.getSelectedItem();
			case RESISTANCE:
				return cbResistance.getSelectedItem();
			case DESCRIPTION:
				return textfield.getText();
			case DB:
				// cell is not editable
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (value instanceof MagicalFeature) {
			MagicalFeature mf = (MagicalFeature) value;
			currentType = mf.getType();
			switch (currentType) {
				case STAT:
					cbStat.setSelectedItem(mf.getStat());
					return cbStat;
				case SKILL:
					/* always renew the skills. to have all new custom skills */
					Vector<ISkill> skills = new Vector<ISkill>();
					skills.addAll(rmSheetAdapter.getBean().getSkills());
					cbSkill.setModel(new DefaultComboBoxModel(skills));
					cbSkill.setSelectedItem(rmSheetAdapter.getBean().getSkill(mf.getId()));
					return cbSkill;
				case RESISTANCE:
					cbResistance.setSelectedItem(mf.getResistance());
					return cbResistance;
				case DESCRIPTION:
					textfield.setText(mf.getDescription());
					return textfield;
				case DB:
					// cell is not editable
			}
		}
		return textfield;
	}
}
