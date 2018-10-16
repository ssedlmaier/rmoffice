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
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

import net.sf.rmoffice.core.items.MagicalFeature;

/**
 * 
 */
public class NumberSpinnerTableCellEditor extends AbstractCellEditor implements TableCellEditor, ChangeListener {
	private static final long serialVersionUID = 1L;
	private final JSpinner editor;
	private int row;
	private final boolean editable;

	public NumberSpinnerTableCellEditor(double stepSize) {
		SpinnerNumberModel model = new SpinnerNumberModel(0d, 0d, 9999d, stepSize);
		editor = new JSpinner(model);
		model.addChangeListener(this);
		editor.setBorder(null);
		editable = false;
		((DefaultEditor) editor.getEditor()).getTextField().setEditable(false);
	}
	
	public NumberSpinnerTableCellEditor(int stepSize, boolean editable, int minimum) {
		SpinnerNumberModel model = new SpinnerNumberModel(0, minimum, 9999, stepSize);
		editor = new JSpinner(model);
		model.addChangeListener(this);
		editor.setBorder(null);
		this.editable = editable;
		if (! editable) {
			((DefaultEditor) editor.getEditor()).getTextField().setEditable(false);
		}
	}

	@Override
	public Object getCellEditorValue() {
		return editor.getValue();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.row = row;
		if (isSelected) {
			editor.setBackground(table.getSelectionBackground());
		}
		if (!editable) {
			((DefaultEditor) editor.getEditor()).getTextField().setBackground(table.getSelectionBackground());
		}
		/* MagicalFeature#bonus */
		if (value instanceof MagicalFeature) {
			MagicalFeature feature = (MagicalFeature)value;
			if (feature.getType().isBonusAvailable()) {
				value = feature.getBonus();
			}
		}
		if (value == null) {
			editor.setValue(Double.valueOf(0));
		} else {
			editor.setValue(value);
		}
		return editor;
	}
	
    /**
     * Returns true if <code>anEvent</code> is <b>not</b> a
     * <code>MouseEvent</code>.  Otherwise, it returns true
     * if the necessary number of clicks have occurred, and
     * returns false otherwise.
     *
     * @param   anEvent         the event
     * @return  true  if cell is ready for editing, false otherwise
     * @see #shouldSelectCell
     */
     @Override
     public boolean isCellEditable(EventObject anEvent) {
    	 if (anEvent instanceof MouseEvent) { 
    		 return ((MouseEvent)anEvent).getClickCount() >= 1;
    	 }
    	 return true;
     }

	/** {@inheritDoc} */
	@Override
	public void stateChanged(ChangeEvent e) {
		/* we stop after every click */
		stopCellEditing();
	}
	
	public int getRow() {
		return row;
	}
}