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

import javax.swing.JTable;

import net.sf.rmoffice.ui.components.StrictAutoCompletionComboBox;

import com.jidesoft.swing.AutoCompletionComboBox;


/**
 * Table cell editor for all enums.
 */
public class EnumTableCellEditor extends AbstractComboBoxTableCellEditor {
	private static final long serialVersionUID = 1L;
	private final AutoCompletionComboBox cb;
	
	/**
	 * Default constructor.
	 * 
	 * @param items the items array
	 */
	public EnumTableCellEditor(Object[] items) {
		cb = new StrictAutoCompletionComboBox(items);
		cb.addPopupMenuListener(this);
	}

	/** {@inheritDoc} */
	@Override
	public Object getCellEditorValue() {
		return cb.getSelectedItem();
	}

	/** {@inheritDoc} */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		cb.setSelectedItem(value);
		return cb;
	}
}
