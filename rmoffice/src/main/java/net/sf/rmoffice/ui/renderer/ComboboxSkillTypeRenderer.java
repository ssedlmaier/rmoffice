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
package net.sf.rmoffice.ui.renderer;

import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import net.sf.rmoffice.meta.enums.SkillType;


/**
 * 
 */
public class ComboboxSkillTypeRenderer extends JLabel implements ListCellRenderer {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	public ComboboxSkillTypeRenderer() {
		setOpaque(true);
	}

	/*
	 * This method finds the image and text corresponding
	 * to the selected value and returns the label, set up
	 * to display the text and image.
	 */
	@Override
	public Component getListCellRendererComponent(
			JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus) {
		/* Get the selected index. (The index param isn't
		 always valid, so just use the value.) */

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		/* Set the icon and text.  If icon was null, say so. */
		if (value != null) {
			String text = RESOURCE.getString("SkillType."+((SkillType)value).name()+".combobox");
			setText(text);
		} else {
			setText(RESOURCE.getString("SkillType.inherit.combobox"));
		}

		return this;
	}
}