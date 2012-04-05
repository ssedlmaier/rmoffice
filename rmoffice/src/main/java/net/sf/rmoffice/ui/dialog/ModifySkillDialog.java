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
package net.sf.rmoffice.ui.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.ui.renderer.ComboboxSkillTypeRenderer;


/**
 * 
 */
public class ModifySkillDialog extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private JTextField tfName;
	private JComboBox cbSkillType;


	/**
	 * @param skill the skill
	 * @param rankType the skills rank type or {@code null} for new skills
	 * 
	 */
	public ModifySkillDialog(ISkill skill, SkillType rankType) {
		initComponents(skill, rankType);
	}

	/**
	 * Returns the name of the skill.
	 * @return the name
	 */
	public String getSkillName() {
		return tfName.getText();
	}
	
	/**
	 * Returns the {@link SkillType} for the new skill.
	 * @return the skill type
	 */
	public SkillType getSkillType() {
		return (SkillType) cbSkillType.getSelectedItem();
	}

	/**
	 * 
	 */
	private void initComponents(ISkill skill, SkillType rankType) {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;c.gridy = 0;c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 3, 2, 3);
		
		/* ----- name ---------- */
		add (new JLabel(RESOURCE.getString("message.skills.modifyname")+":"), c);
		c.gridx++;
		tfName = new JTextField(skill.getName());
		add(tfName, c);
		/* -------- skill type ---------- */
		c.gridy++;c.gridx=0;
		add (new JLabel(RESOURCE.getString("message.skills.skilltype")+":"), c);
		c.gridx++;
		SkillType[] choices = new SkillType[SkillType.values().length];
		choices[0] = null;
		choices[1] = SkillType.DEFAULT;
		choices[2] = SkillType.EVERYMAN;
		choices[3] = SkillType.OCCUPATIONAL;
		choices[4] = SkillType.RESTRICTED;
		DefaultComboBoxModel model = new DefaultComboBoxModel(choices);
		cbSkillType = new JComboBox(model);
		cbSkillType.setEditable(false);
		cbSkillType.setRenderer(new ComboboxSkillTypeRenderer() );
		if (rankType != null) {
			/* select current value */
			model.setSelectedItem(rankType);
		}
		
		add(cbSkillType, c);
	}

}
