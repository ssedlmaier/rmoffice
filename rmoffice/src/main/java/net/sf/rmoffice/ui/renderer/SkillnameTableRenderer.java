/*
 * Copyright 2013 Daniel Golesny
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

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.ui.models.SkillsTableModel;

import com.itextpdf.text.Font;

public class SkillnameTableRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (value != null) {
			ISkill skill = ((SkillsTableModel)table.getModel()).getSkillAtRow(row);
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
