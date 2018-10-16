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

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import net.sf.rmoffice.meta.Race;


/**
 * 
 */
public class RaceCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	/** {@inheritDoc} */
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Race r = null;
		if (value instanceof Race) {
			r = (Race)value;
			String s = RESOURCE.getString("RaceScope."+r.getScope().name());
			if (s.length() > 0) {
				s = "[" + s + "] ";  
			}
			s += r.getName();
			value = s;
		}
		Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (r != null) {
			comp.setBackground(r.getScope().getBackground());
			comp.setForeground(r.getScope().getForeground());
		}
		return comp;
	}
}
