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
	
	/**
	 * 
	 */
	public TextAreaTableCellRenderer() {
		super();
		textAreaComponent = new JTextArea();
		textAreaComponent.setWrapStyleWord(true);
		textAreaComponent.setLineWrap(true);
		textAreaComponent.setForeground(Color.black);
	}
	
	/** {@inheritDoc} */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel lbl = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		textAreaComponent.setBorder(lbl.getBorder());
		textAreaComponent.setBackground(lbl.getBackground());
		textAreaComponent.setText(lbl.getText());
		textAreaComponent.setSize(table.getSize().width, table.getRowHeight());
		int rowHeight = textAreaComponent.getPreferredSize().height;
		if (rowHeight < UIConstants.TABLE_ROW_HEIGHT) {
			rowHeight = UIConstants.TABLE_ROW_HEIGHT;
		}
		table.setRowHeight(row, rowHeight );
		return textAreaComponent;
	}
}
