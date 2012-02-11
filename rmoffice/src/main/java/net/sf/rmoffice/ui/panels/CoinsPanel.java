/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.rmoffice.ui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import net.sf.rmoffice.core.Coins;
import net.sf.rmoffice.ui.UIConstants;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.list.SelectionInList;


/**
 * 
 */
public class CoinsPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final static ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$

	private final BeanAdapter<Coins> adapter;

	/**
	 * @param coinAdapter the coin adapter
	 */
	public CoinsPanel(BeanAdapter<Coins> coinAdapter) {
		super(new BorderLayout());
		this.adapter = coinAdapter;
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints col1 = new GridBagConstraints();
		col1.gridx = 0;
		col1.fill = GridBagConstraints.HORIZONTAL;
		col1.insets = new Insets(2, 10, 2, 10);
		col1.weightx = 0.1;
		col1.gridy = -1;
		GridBagConstraints col2 = new GridBagConstraints();
		col2.gridx = 1;
		col2.gridy = -1;
		col2.fill = GridBagConstraints.HORIZONTAL;
		col2.insets = new Insets(2, 2, 2, 10);
		col2.weightx = 0.35;
		
		create(panel, "rolemaster.money.mithril", Coins.PROP_MITHRIL, col1, col2);
		create(panel, "rolemaster.money.platinum", Coins.PROP_PLATINUM, col1, col2);
		create(panel, "rolemaster.money.gold", Coins.PROP_GOLD, col1, col2);
		create(panel, "rolemaster.money.silver", Coins.PROP_SILVER, col1, col2);
		create(panel, "rolemaster.money.bronze", Coins.PROP_BRONZE, col1, col2);
		create(panel, "rolemaster.money.copper", Coins.PROP_COPPER, col1, col2);
		create(panel, "rolemaster.money.tin", Coins.PROP_TIN, col1, col2);
		create(panel, "rolemaster.money.iron", Coins.PROP_IRON, col1, col2);
		
		/* Jewelry */
		col2.gridx++;		
		col2.gridy = 0;
		col2.weightx = 0.55;
		panel.add(new JLabel(RESOURCE.getString("rolemaster.money.jewelry")), col2);
		col2.gridy = 1;
		col2.gridheight = 7;
		col2.anchor = GridBagConstraints.NORTH;
		final BeanAdapter<Coins>.SimplePropertyAdapter model = adapter.getValueModel(Coins.PROP_JUWELRY);
		SelectionInList<String> listModel = new SelectionInList<String>(model);
		AbstractTableAdapter<String> jewAdapter = new AbstractTableAdapter<String>(listModel) {
			private static final long serialVersionUID = 1L;
			/** {@inheritDoc} */
			@Override
			public String getColumnName(int columnIndex) {
				return "";
			}
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				return getRow(rowIndex);
			}
			/** {@inheritDoc} */
			@Override
			public int getColumnCount() {
				return 1;
			}	
			/** {@inheritDoc} */
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return true;
			}
			/** {@inheritDoc} */
			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				List<String> lst = adapter.getBean().getJuwelry();
				lst.set(rowIndex, (String)aValue);
				adapter.getBean().setJuwelry(lst);
			}
		};
		final JTable jewelryTable = new JTable(jewAdapter);
		jewelryTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		jewelryTable.putClientProperty("autoStartsEdit", Boolean.TRUE);
		jewelryTable.setBorder(new JTextField().getBorder());
		jewelryTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
		jewelryTable.setBackground(UIConstants.COLOR_EDITABLE_BG);
		jewelryTable.setSelectionForeground(UIConstants.COLOR_SELECTION_FG);
		jewelryTable.setSelectionBackground(UIConstants.COLOR_SELECTION_BG);
		panel.add(jewelryTable, col2);
		jewelryTable.setPreferredSize(new Dimension(200, jewelryTable.getPreferredSize().height));
		final DefaultCellEditor cellEditor = (DefaultCellEditor)jewelryTable.getDefaultEditor(String.class);
		cellEditor.setClickCountToStart(1);
		add(panel, BorderLayout.WEST);
	}

	private void create(JPanel panel, String labelResKey, String prop, GridBagConstraints col1, GridBagConstraints col2) {
		col1.gridy++;
		panel.add(new JLabel(RESOURCE.getString(labelResKey)), col1);
		
		JTextField tf = new JTextField(10);
		Bindings.bind(tf, adapter.getValueModel(prop));
		col2.gridy++;
		panel.add(tf, col2);
	}
}
