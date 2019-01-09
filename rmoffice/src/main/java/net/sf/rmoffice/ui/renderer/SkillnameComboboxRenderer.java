/*
 * Copyright 2013 Daniel Nettesheim
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
package net.sf.rmoffice.ui.renderer;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import com.itextpdf.text.Font;

import net.sf.rmoffice.meta.ISkill;

/**
 * Spells are italic.
 * Custom skills are bold.
 */
public class SkillnameComboboxRenderer  extends BasicComboBoxRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value != null && value instanceof ISkill) {
			ISkill skill = (ISkill)value;
			if (skill.getClass().getName().endsWith("CustomSkill")) {
				if (skill.isSpelllist()) {
					comp.setFont(comp.getFont().deriveFont(Font.BOLDITALIC));
				} else {
					comp.setFont(comp.getFont().deriveFont(Font.BOLD));
				}
			} else if (skill.isSpelllist()) {
				comp.setFont(comp.getFont().deriveFont(Font.ITALIC));
			}
		}
		return comp;
	}
}
