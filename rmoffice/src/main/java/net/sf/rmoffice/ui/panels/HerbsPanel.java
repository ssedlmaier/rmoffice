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
package net.sf.rmoffice.ui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.table.TableCellRenderer;

import net.sf.rmoffice.core.Herb;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.ui.UIConstants;
import net.sf.rmoffice.ui.converters.SelectionIndexToEnabledListener;
import net.sf.rmoffice.ui.editor.NumberSpinnerTableCellEditor;
import net.sf.rmoffice.ui.renderer.NumberSpinnerTableRenderer;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.forms.builder.ButtonBarBuilder2;

public class HerbsPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private static final String NEW_LINE = "newLine";
	private static final String DEL_LINE = "delLine";
	
	private static final int COL_NAME = 0;
	private static final int COL_DESCR = 1;
	private static final int COL_AMOUNT = 2;

	private SelectionInList<Herb> listModel;

	public HerbsPanel(BeanAdapter<RMSheet> rmSheetAdapter) {
		super(new GridBagLayout());
		/* Buttons */
		JButton btNew = new JButton(RESOURCE.getString("ui.equipment.newline"), UIConstants.ICON_NEWLINE);
		btNew.setActionCommand(NEW_LINE);
		btNew.addActionListener(this);
		JButton btDel = new JButton(RESOURCE.getString("ui.equipment.delline"), UIConstants.ICON_DELETE);
		btDel.setActionCommand(DEL_LINE);
		btDel.addActionListener(this);
		
		ButtonBarBuilder2 builder = new ButtonBarBuilder2();
		builder.addButton(btNew, btDel);
		
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0; gc.gridy = 0;
		gc.insets = new Insets(7, 7, 7, 7);
		gc.anchor = GridBagConstraints.CENTER;
		add(builder.getPanel(), gc);
		/* */
		gc.insets = new Insets(0, 7, 7, 7);
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 1.0f;
		gc.weighty = 0.9f;
		gc.gridx = 0;
		gc.gridy = 1;
		
		/* Table */
		BeanAdapter<RMSheet>.SimplePropertyAdapter equipmentModel = rmSheetAdapter.getValueModel(RMSheet.PROPERTY_HERBS);
		listModel = new SelectionInList<Herb>(equipmentModel);
		
		HerbsTableAdapter equipmentAdapter = new HerbsTableAdapter(listModel, new String[] {RESOURCE.getString("common.herbs.name"), 
				                                          RESOURCE.getString("common.herbs.desc"), 
				                                          RESOURCE.getString("common.herb.amount") 
				                                          });
		JTable table = new JTable(equipmentAdapter) {
			private static final long serialVersionUID = 1L;

			/** {@inheritDoc} */
			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				TableCellRenderer cellRenderer = super.getCellRenderer(row, column);
				((JComponent)cellRenderer).setBackground(UIConstants.COLOR_EDITABLE_BG);
				return cellRenderer;
			}
		};
		table.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
		table.setSelectionModel(new SingleListSelectionAdapter(listModel.getSelectionIndexHolder()));
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setPreferredWidth(600);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		DefaultCellEditor cellEditor = (DefaultCellEditor)table.getDefaultEditor(String.class);
		cellEditor.setClickCountToStart(1);
		DefaultCellEditor cellEditorInt = (DefaultCellEditor)table.getDefaultEditor(Integer.class);
		cellEditorInt.setClickCountToStart(1);
		NumberSpinnerTableCellEditor numberRenderer = new NumberSpinnerTableCellEditor(1, true, 0);
		table.setDefaultEditor(Integer.class, numberRenderer);
		table.setDefaultRenderer(Integer.class, new NumberSpinnerTableRenderer(true));
		JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, gc);
        
        /* enabled handling */
        ValueHolder enabledHolder = new ValueHolder(false);
		listModel.getSelectionIndexHolder().addValueChangeListener(new SelectionIndexToEnabledListener(enabledHolder));
		Bindings.bind(btDel, "enabled", enabledHolder);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (NEW_LINE.equals(e.getActionCommand())) {
			List<Herb> list = listModel.getList();

			List<Herb> newList = new ArrayList<Herb>();
			if (list != null) {
				newList.addAll(list);
			}
			newList.add(new Herb());
			listModel.setList(newList);

		} else if (DEL_LINE.equals(e.getActionCommand())) {
			Herb selection = listModel.getSelection();
			if (selection != null) {
				List<Herb> list = listModel.getList();
				List<Herb> newList = new ArrayList<Herb>();
				for (Herb eq : list) {
					if (! eq.equals(selection)) {
						newList.add(eq);
					}
				}
				listModel.setList(newList);
			}
		}
	}
	
	private class HerbsTableAdapter extends AbstractTableAdapter<Herb> {
		private static final long serialVersionUID = 1L;

		public HerbsTableAdapter(ListModel listModel, String[] columnNames) {
            super(listModel, columnNames);
        }
        
        /** {@inheritDoc} */
        @Override
        public Class<?> getColumnClass(int columnIndex) {
        	if (columnIndex == COL_AMOUNT) {
        		return Integer.class;
        	} else {
        		return String.class;
        	}
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
        	Herb herb = getRow(rowIndex);
        	switch (columnIndex) {
        	case COL_NAME:
        		return herb.getName();
        	case COL_DESCR:
        		return herb.getDescription();
        	case COL_AMOUNT:
        		return Integer.valueOf(herb.getAmount());
        	}
        	return null;
        }
        
        /** {@inheritDoc} */
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
        	return true;
        }
        
        /** {@inheritDoc} */
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        	Herb equip = getRow(rowIndex);
        	switch (columnIndex) {
        	case COL_NAME:
        		equip.setName((String)aValue);
        		break;
        	case COL_DESCR:
        		equip.setDescription((String)aValue);
        		break;
        	case COL_AMOUNT:
        		equip.setAmount(((Integer)aValue).intValue());
        		break;
        	}
        }
    }
}
