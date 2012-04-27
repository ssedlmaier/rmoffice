package net.sf.rmoffice.ui.renderer;
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

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

/**
 * Table cell renderer that displays the text in multiple lines
 * and modifies the row height.
 */
public class MultiLineTableCellRenderer extends JTextArea implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	private static Border border = new EmptyBorder(1, 2, 1, 2);

	public MultiLineTableCellRenderer() {
		setLineWrap(true);
		setWrapStyleWord(false);
		/* getPreferredSize() initializes something internally in the JTextArea, otherwise the first time rendering 
		 * will result in a very high table row height. */
		getPreferredSize();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (value != null && (value.getClass().isArray() || value instanceof List<?>)) {
			StringBuilder sb = new StringBuilder();
			Object[] arr = null;
			if (value instanceof List<?>) {
				arr = ((List<?>)value).toArray();
			} else {
				arr = (Object[]) value;
			}
			for (Object line : arr) {
				if (line != null) {
					if (sb.length() > 0) {
						sb.append("\n");
					}
					sb.append(line.toString());
				}
			}
			setText(sb.toString());
		} else {
			setText((String)value);
		}
		
		Color cellForeground;
		Color cellBackground;

		// Set the foreground and background colors
		// from the table if they are not set
		cellForeground = table.getForeground();
		cellBackground = table.getBackground();

		// Handle selection and focus colors
		if (isSelected == true) {
			cellForeground = table.getSelectionForeground();
			cellBackground = table.getSelectionBackground();
		}

		if (hasFocus == true) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		} else {
			setBorder(border);
		}

		setForeground(cellForeground);
		setBackground(cellBackground);
		
		// set row height
		int minHeight = table.getRowHeight(row);
		if (minHeight < getMinimumSize().height) { 
			table.setRowHeight(row, getMinimumSize().height);
		}
		
		return this;
	}
}