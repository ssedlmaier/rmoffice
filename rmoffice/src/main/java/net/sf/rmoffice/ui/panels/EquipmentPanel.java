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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.table.TableCellRenderer;

import net.sf.rmoffice.core.Equipment;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.pdf.AbstractPDFCreator;
import net.sf.rmoffice.ui.UIConstants;
import net.sf.rmoffice.ui.converters.EnumValueConverter;
import net.sf.rmoffice.ui.converters.SelectionIndexToEnabledListener;
import net.sf.rmoffice.ui.renderer.ColoredBooleanRenderer;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.forms.builder.ButtonBarBuilder2;


/**
 * Tab panel for equipment.
 */
public class EquipmentPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final static ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private static final String NEW_LINE = "newLine";
	private static final String DEL_LINE = "delLine";
	
	private static final int COL_NAME = 0;
	private static final int COL_PLACE = 1;
	private static final int COL_WEIGHT = 2;
	private static final int COL_FAV = 3;
	private static final int COL_AT_BODY = 4;
	
	private SelectionInList<Equipment> listModel;

	/**
	 * @param adapter the {@link RMSheet} adapter
	 * 
	 */
	public EquipmentPanel(BeanAdapter<RMSheet> adapter) {
		super(new GridBagLayout());
		/* Buttons */
		JButton btNew = new JButton(RESOURCE.getString("ui.equipment.newline"), UIConstants.ICON_NEWLINE);
		btNew.setActionCommand(NEW_LINE);
		btNew.addActionListener(this);
		JButton btDel = new JButton(RESOURCE.getString("ui.equipment.delline"), UIConstants.ICON_DELETE);
		btDel.setActionCommand(DEL_LINE);
		btDel.addActionListener(this);
		JLabel lblUnit = BasicComponentFactory.createLabel(new EnumValueConverter(adapter.getValueModel(RMSheet.PROPERTY_WEIGHT_UNIT)));
		ButtonBarBuilder2 builder = new ButtonBarBuilder2();
		builder.addButton(btNew, btDel).addRelatedGap();
		builder.addFixed(new JLabel(RESOURCE.getString("common.equipment.weight.unit") + ": ")).addGlue().addFixed(lblUnit);
		
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
		
		BeanAdapter<RMSheet>.SimplePropertyAdapter equipmentModel = adapter.getValueModel(RMSheet.PROPERTY_EQUIPMENTS);
		listModel = new SelectionInList<Equipment>(equipmentModel);
		
		EquipmentTableAdapter equipmentAdapter = new EquipmentTableAdapter(listModel, new String[] {RESOURCE.getString("common.equipment.itemdesc"), 
				                                          RESOURCE.getString("common.equipment.location"), 
				                                          RESOURCE.getString("common.equipment.weight"),
				                                          RESOURCE.getString("common.favorite"),
				                                          RESOURCE.getString("common.equipment.location.carried"), 
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
		table.getColumnModel().getColumn(0).setPreferredWidth(400);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(40);
		table.getColumnModel().getColumn(4).setPreferredWidth(40);
		table.setDefaultRenderer(Boolean.class, new ColoredBooleanRenderer());
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		DefaultCellEditor cellEditor = (DefaultCellEditor)table.getDefaultEditor(String.class);
		cellEditor.setClickCountToStart(1);
		DefaultCellEditor cellEditorInt = (DefaultCellEditor)table.getDefaultEditor(Integer.class);
		cellEditorInt.setClickCountToStart(1);
		JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, gc);
        
        /* enabled handling */
        ValueHolder enabledHolder = new ValueHolder(false);
		listModel.getSelectionIndexHolder().addValueChangeListener(new SelectionIndexToEnabledListener(enabledHolder));
		Bindings.bind(btDel, "enabled", enabledHolder);
	}	
	
    private class EquipmentTableAdapter extends AbstractTableAdapter<Equipment> {
		private static final long serialVersionUID = 1L;

		public EquipmentTableAdapter(ListModel listModel, String[] columnNames) {
            super(listModel, columnNames);
        }
        
        /** {@inheritDoc} */
        @Override
        public Class<?> getColumnClass(int columnIndex) {
        	if (columnIndex == COL_FAV || columnIndex == COL_AT_BODY) {
        		return Boolean.class;
        	} else if (columnIndex == COL_WEIGHT) {
        		return Float.class;
        	} else {
        		return String.class;
        	}
        }

        @Override
		public Object getValueAt(int rowIndex, int columnIndex) {
        	Equipment equip = getRow(rowIndex);
        	switch (columnIndex) {
        		case COL_NAME:
        			return equip.getDescription();
        		case COL_PLACE:
        			return equip.getPlace();
        		case COL_WEIGHT:
        			return Float.valueOf(equip.getWeight());
        		case COL_FAV:
        			return Boolean.valueOf(equip.isFavorite());
        		case COL_AT_BODY:
        			return Boolean.valueOf(equip.isCarried());
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
        	Equipment equip = getRow(rowIndex);
        	switch (columnIndex) {
        		case COL_NAME:
        			equip.setDescription((String)aValue);
        			break;
        		case COL_PLACE:
        			equip.setPlace((String)aValue);
        			break;
        		case COL_WEIGHT:
        			equip.setWeight(((Float)aValue).floatValue());
        			break;
        		case COL_FAV:
        			equip.setFavorite(((Boolean)aValue).booleanValue());
        			break;
        		case COL_AT_BODY:
        			equip.setCarried(((Boolean)aValue).booleanValue());
        			break;
        	}
        }
    }

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (NEW_LINE.equals(e.getActionCommand())) {
			List<Equipment> list = listModel.getList();
			if (list != null && list.size() >= AbstractPDFCreator.MAX_EQUIPMENT_LINES) {
				JOptionPane.showMessageDialog(this, "Maximale Anzahl Zeilen erreicht", "Warnung", JOptionPane.WARNING_MESSAGE);
			} else {
				List<Equipment> newList = new ArrayList<Equipment>();
				if (list != null) {
					newList.addAll(list);
				}
				newList.add(new Equipment());
				listModel.setList(newList);
			}
		} else if (DEL_LINE.equals(e.getActionCommand())) {
			Equipment selection = listModel.getSelection();
			if (selection != null) {
				List<Equipment> list = listModel.getList();
				List<Equipment> newList = new ArrayList<Equipment>();
				for (Equipment eq : list) {
					if (! eq.equals(selection)) {
						newList.add(eq);
					}
				}
				listModel.setList(newList);
			}
		}
	}
}
