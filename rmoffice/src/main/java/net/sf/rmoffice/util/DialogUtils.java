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
package net.sf.rmoffice.util;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComponent;

public class DialogUtils {
	/**
	 * Returns the OK button from the dialog.
	 */
	public static JButton getButton(JComponent comp, String text) {
	        if (comp == null) {
	            return null;     
	        }

	        for (Component c : comp.getComponents()) {
	            if (c instanceof JButton) {
	            	JButton bt = (JButton)c;
	            	if (text.equals(bt.getText())) {
	            		return bt;
	            	}
	            } else if (c instanceof JComponent) {
	                JButton bt = getButton((JComponent) c, text);
	                if (bt != null) {
	                	return bt;
	                }
	            }
	        }
	        return null;
	    }
}
