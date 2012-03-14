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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.core.items.MagicalFeature;
import net.sf.rmoffice.core.items.MagicalItem;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.enums.MagicalItemFeatureType;
import net.sf.rmoffice.meta.enums.ResistanceEnum;
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.ui.UIConstants;
import net.sf.rmoffice.ui.converters.SelectionIndexToEnabledListener;
import net.sf.rmoffice.ui.editor.MagicalFeatureTableCellEditor;
import net.sf.rmoffice.ui.editor.EnumTableCellEditor;
import net.sf.rmoffice.ui.editor.NumberSpinnerTableCellEditor;
import net.sf.rmoffice.ui.renderer.MagicalFeatureTableCellRenderer;
import net.sf.rmoffice.ui.renderer.MagicalItemFeatureTypeTableCellRenderer;
import net.sf.rmoffice.ui.renderer.NumberSpinnerTableRenderer;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;


/**
 * Tab panel for magical items.
 */
public class MagicalItemPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	private static final String CMD_DEL_ITEM = "delItem";
	private static final String CMD_NEW_ITEM = "newItem";
	private static final String CMD_DEL_FEAT = "delFeat";
	private static final String CMD_NEW_FEAT = "newFeat";
	private static final int COL_TYPE = 0;
	private static final int COL_DESCR = 1;
	private static final int COL_BONUS = 2;
	
	private SelectionInList<MagicalItem> listModel;
	private SelectionInList<MagicalFeature> featureListModel;
	private ValueHolder enabledHolderItemSelected = new ValueHolder(false);

	private final ValueHolder enabledHolderRMFrame;
	private final BeanAdapter<RMSheet> rmSheetAdapter;

	/**
	 * 
	 * @param rmSheetAdapter the sheet adapter, not {@code null}
	 * @param enabledHolderRMFrame the main enabled holder
	 */
	public MagicalItemPanel(BeanAdapter<RMSheet> rmSheetAdapter, ValueHolder enabledHolderRMFrame) {
		super(new GridBagLayout());
		this.rmSheetAdapter = rmSheetAdapter;
		this.enabledHolderRMFrame = enabledHolderRMFrame;
		
		/* prepare models */
		BeanAdapter<RMSheet>.SimplePropertyAdapter magicItemModel = rmSheetAdapter.getValueModel(RMSheet.PROPERTY_MAGICALITEMS);
		listModel = new SelectionInList<MagicalItem>(magicItemModel);
		/* bind enabled holder with the model to disable components that works with selection */
		listModel.getSelectionIndexHolder().addValueChangeListener(new SelectionIndexToEnabledListener(enabledHolderItemSelected));
		
		GridBagConstraints gc = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3,3,3,3), 1, 1);
		createButtonsItem(gc);
		
		/* left col overview over all items */
		JList itemList = new JList();
		Bindings.bind(itemList, "enabled", enabledHolderRMFrame);
		itemList.setFixedCellHeight(UIConstants.TABLE_ROW_HEIGHT);
		Bindings.bind(itemList, listModel);
		JScrollPane scroller = new JScrollPane(itemList);
		gc.gridx = 0; gc.gridy++; gc.gridheight = 7; gc.gridwidth = 2; gc.fill = GridBagConstraints.BOTH;
		add(scroller, gc);
		
		/* right column */
		gc.gridheight = 1; gc.gridwidth = 3; gc.gridx = 2; gc.gridy = 1;
		JLabel lbName = new JLabel(RESOURCE.getString("ui.items.name"));
		Bindings.bind(lbName, "enabled", enabledHolderItemSelected);
		add(lbName, gc);
		gc.gridy++;
		
		final BeanAdapter<MagicalItem> beanAdapter = new BeanAdapter<MagicalItem>(listModel);
		beanAdapter.addBeanPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (MagicalItem.PROP_NAME.equals(evt.getPropertyName()) && listModel.getSelectionIndex() > -1) {
					listModel.fireSelectedContentsChanged();
				}
			}
		});
		
		/* name binding */
		BeanAdapter<MagicalItem>.SimplePropertyAdapter nameValueModel = beanAdapter.getValueModel(MagicalItem.PROP_NAME);
		JTextField tfName = BasicComponentFactory.createTextField(nameValueModel, false);
		Bindings.bind(tfName, "enabled", enabledHolderItemSelected);
		add(tfName, gc);
		gc.gridy++;
		
		/* favorite */
		BeanAdapter<MagicalItem>.SimplePropertyAdapter favoriteValueModel = beanAdapter.getValueModel(MagicalItem.PROP_FAVORITE);
		JCheckBox cbFavorite = BasicComponentFactory.createCheckBox(favoriteValueModel, RESOURCE.getString("ui.items.favorite"));
		Bindings.bind(cbFavorite, "enabled", enabledHolderItemSelected);
		add(cbFavorite, gc);
		
		/* */
		createButtonsFeatures(gc);

		featureListModel = createFeatureTable(beanAdapter, gc, rmSheetAdapter);
	}

	private SelectionInList<MagicalFeature> createFeatureTable(BeanAdapter<MagicalItem> beanAdapter, GridBagConstraints gc, BeanAdapter<RMSheet> rmSheetAdapter) {
		gc.gridheight = 3; gc.gridwidth = 3; gc.gridx = 2; gc.gridy++;
		BeanAdapter<MagicalItem>.SimplePropertyAdapter featureModel = beanAdapter.getValueModel(MagicalItem.PROP_FEATURES);
		SelectionInList<MagicalFeature> model = new SelectionInList<MagicalFeature>(featureModel);
		
		final MagicalItemTableAdapter featureTableAdapter = new MagicalItemTableAdapter(model);
		/* define renderer and editors */
		final MagicalItemFeatureTypeTableCellRenderer cbRenderer = new MagicalItemFeatureTypeTableCellRenderer();
		final EnumTableCellEditor cbEditor = new EnumTableCellEditor(MagicalItemFeatureType.values());
		final NumberSpinnerTableRenderer bonusRenderer = new NumberSpinnerTableRenderer();
		final NumberSpinnerTableCellEditor bonusEditor = new NumberSpinnerTableCellEditor(1, false);
		final MagicalFeatureTableCellRenderer descrRenderer = new MagicalFeatureTableCellRenderer();
		final MagicalFeatureTableCellEditor descrEditor = new MagicalFeatureTableCellEditor(rmSheetAdapter);
		
		JTable featureTable = new JTable(featureTableAdapter) {
			private static final long serialVersionUID = 1L;

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				switch (column) {
					case COL_TYPE: return cbRenderer;
					case COL_DESCR: return descrRenderer;
					case COL_BONUS: return bonusRenderer;					
				}
				return super.getCellRenderer(row, column);
			}
			
			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				switch (column) {
					case COL_TYPE: return cbEditor;
					case COL_DESCR: return descrEditor;
					case COL_BONUS: return bonusEditor;
				}
				return super.getCellEditor(row, column);
			}
		};
		featureTable.setBackground(UIConstants.COLOR_EDITABLE_BG);
		featureTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		featureTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
		featureTable.getTableHeader().setReorderingAllowed(false);
		featureTable.setAutoCreateRowSorter(true);
		Bindings.bind(featureTable, "enabled", enabledHolderItemSelected);
		Bindings.bind(featureTable.getTableHeader(), "visible", enabledHolderItemSelected);

		/* bind selection to selectionInList model */
		featureTable.setSelectionModel(new SingleListSelectionAdapter(model.getSelectionIndexHolder()));
		Bindings.addComponentPropertyHandler(featureTable, model.getSelectionIndexHolder());
		
		
		/* add table with scroll bar*/
		JScrollPane tblScroll = new JScrollPane(featureTable);
		tblScroll.setPreferredSize(new Dimension(tblScroll.getPreferredSize().width, 300));
		add(tblScroll, gc);
		
		return model;
	}

	private void createButtonsItem(GridBagConstraints gc) {
		JButton btNewItem = new JButton(RESOURCE.getString("ui.items.newItem"), UIConstants.ICON_NEWITEM);
		btNewItem.setActionCommand(CMD_NEW_ITEM);
		btNewItem.addActionListener(this);
		Bindings.bind(btNewItem, "enabled", enabledHolderRMFrame);
		add(btNewItem, gc);
		
		JButton btDelItem = new JButton(RESOURCE.getString("ui.items.delItem"), UIConstants.ICON_DELETE);
		btDelItem.setActionCommand(CMD_DEL_ITEM);
		btDelItem.addActionListener(this);
		Bindings.bind(btDelItem, "enabled", enabledHolderItemSelected);
		gc.gridx++;
		add(btDelItem, gc);
	}
	
	private void createButtonsFeatures(GridBagConstraints gc) {
		gc.gridwidth = 1;
		gc.gridy++;
		JButton btNewSkill = new JButton(RESOURCE.getString("ui.items.newFeature"), UIConstants.ICON_NEWLINE);
		btNewSkill.addActionListener(this);
		btNewSkill.setActionCommand(CMD_NEW_FEAT);
		Bindings.bind(btNewSkill, "enabled", enabledHolderItemSelected);
		add(btNewSkill, gc);
		gc.gridx = 4;
		JButton btDelSkill = new JButton(RESOURCE.getString("ui.items.delFeature"), UIConstants.ICON_DELETE);
		btDelSkill.addActionListener(this);
		btDelSkill.setActionCommand(CMD_DEL_FEAT);
		Bindings.bind(btDelSkill, "enabled", enabledHolderItemSelected);
		add(btDelSkill, gc);
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (CMD_NEW_ITEM.equals( e.getActionCommand() )) {
			List<MagicalItem> newList = new ArrayList<MagicalItem>();
			newList.addAll(listModel.getList());
			MagicalItem item = new MagicalItem(rmSheetAdapter);
			newList.add(item);
			item.setName(RESOURCE.getString("ui.items.newItem.name"));
			listModel.setList(newList);
			listModel.setSelection(item);
		} else if (CMD_DEL_ITEM.equals( e.getActionCommand() )) {
			List<MagicalItem> newList = new ArrayList<MagicalItem>();
			newList.addAll(listModel.getList());
			newList.remove(listModel.getSelection());
			listModel.setList(newList);
			listModel.clearSelection();
			rmSheetAdapter.getBean().notifyItemBonusChanged();
		} else if (CMD_NEW_FEAT.equals( e.getActionCommand() )) {
			MagicalFeature feat = new MagicalFeature(rmSheetAdapter);
			feat.setDescription("");
			int size = featureListModel.getList().size();
			featureListModel.getList().add(feat);
			featureListModel.setSelection(feat);
			featureListModel.fireIntervalAdded(size - 1, size - 1);
		} else if (CMD_DEL_FEAT.equals( e.getActionCommand() )) {
			int idx = featureListModel.getSelectionIndex();
			if (idx > -1) {
				featureListModel.getList().remove(featureListModel.getSelection());
				featureListModel.fireIntervalRemoved(idx, idx);
				featureListModel.clearSelection();
			}
			rmSheetAdapter.getBean().notifyItemBonusChanged();
		}
	}
	
	/* ***************************************
	 * Adapter to bind table and RMSheet
	 * ************************************** */
	private static class MagicalItemTableAdapter extends AbstractTableAdapter<MagicalFeature> {
		private static final long serialVersionUID = 1L;
		private static final String[] COLUMN_NAMES = new String[] {RESOURCE.getString("ui.items.col.type"),
			     RESOURCE.getString("ui.items.col.descr"), RESOURCE.getString("ui.items.col.bonus")};
		public MagicalItemTableAdapter(SelectionInList<MagicalFeature> listModel) {
            super(listModel, COLUMN_NAMES);
        }
		
		/** {@inheritDoc} */
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			MagicalFeature feature = getRow(rowIndex);
			switch (columnIndex) {
				case COL_TYPE: return feature.getType();
				case COL_DESCR: return feature;
				case COL_BONUS: return feature;
				default:
					return "";
			}
		}
		
		/** {@inheritDoc} */
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			MagicalFeature feature = getRow(rowIndex);
			switch (columnIndex) {
				case COL_TYPE:
				  feature.setType((MagicalItemFeatureType)aValue);
				  fireTableRowsUpdated(rowIndex, rowIndex);
				break;
				case COL_DESCR:
					switch (feature.getType()) {
						case STAT:
							StatEnum stat = (StatEnum)aValue;
							feature.setStat(stat);
							if (stat != null) {
								feature.setDescription(RESOURCE.getString("StatEnum."+stat.name()+".short"));
							} else {
								feature.setDescription("");
							}
							break;
						case SKILL:
							
							ISkill skill = (ISkill)aValue;
							if (skill != null) {
								feature.setId( skill.getId() );
								feature.setDescription(skill.getName());
							} else {
								feature.setId(null);
								feature.setDescription("");
							}
							break;
						case RESISTANCE:
							ResistanceEnum res = (ResistanceEnum)aValue;
							if (res != null) {
								feature.setResistance( res );
								feature.setDescription(RESOURCE.getString("ResistanceEnum."+res.name()));
							} else {
								feature.setId( null );
								feature.setDescription("");
							}
							
							break;
						case DESCRIPTION:
							feature.setDescription((String)aValue);
					}
					break;
				case COL_BONUS:
					feature.setBonus((Integer)aValue);
					break;
			}
		}
		
		/** {@inheritDoc} */
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			MagicalFeature feature = getRow(rowIndex);
			if (columnIndex == COL_BONUS) {
				return feature.getType().isBonusAvailable();
			}
			return true;
		}
	}
}
