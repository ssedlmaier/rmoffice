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

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;

import net.sf.rmoffice.ui.UIConstants;


/**
 * 
 */
public class TextAreaTableCellRenderer extends DefaultTableCellRenderer  {
	private static final long serialVersionUID = 1L;
	private JTextArea textAreaComponent;
	private JTextArea textAreaComponentSelected;
	
	/**
	 * 
	 */
	public TextAreaTableCellRenderer() {
		super();
	}
	
	private JTextArea createTF() {
		JTextArea ta = new JTextArea();
		ta.setWrapStyleWord(true);
		ta.setLineWrap(true);
		ta.setForeground(Color.black);
		return ta;
	}

	/** {@inheritDoc} */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JTextArea renderer;
		// due to performance issues we will hold 2 different renderer components
		if (isSelected) {
			if (textAreaComponentSelected == null) {
				JLabel lbl = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				textAreaComponentSelected = createTF();
				textAreaComponentSelected.setBorder(lbl.getBorder());
				textAreaComponentSelected.setBackground(lbl.getBackground());
			}
			renderer = textAreaComponentSelected;
		} else {
			if (textAreaComponent == null) {
				JLabel lbl = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				textAreaComponent = createTF();
				textAreaComponent.setBorder(lbl.getBorder());
				textAreaComponent.setBackground(lbl.getBackground());
			}
			renderer = textAreaComponent;
		}
		if (value != null) {
			renderer.setText(value.toString());
		} else{
			renderer.setText("");
		}
		renderer.setSize(table.getSize().width, table.getRowHeight());
		int rowHeight = renderer.getPreferredSize().height;
		if (rowHeight < UIConstants.TABLE_ROW_HEIGHT) {
			rowHeight = UIConstants.TABLE_ROW_HEIGHT;
		}
		if (table.getRowHeight(row) != rowHeight) {
			table.setRowHeight(row, rowHeight );
		}
		return renderer;
	}
}
