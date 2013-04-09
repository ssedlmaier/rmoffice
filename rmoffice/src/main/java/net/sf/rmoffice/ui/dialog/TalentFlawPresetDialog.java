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
package net.sf.rmoffice.ui.dialog;

import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.TalentFlawPreset;
import net.sf.rmoffice.meta.TalentFlawPresetLevel;
import net.sf.rmoffice.meta.talentflaw.ITalentFlawPart;
import net.sf.rmoffice.meta.talentflaw.TalentFlawContext;
import net.sf.rmoffice.ui.UIConstants;
import net.sf.rmoffice.ui.models.TalentFlawPresetTableAdapter;
import net.sf.rmoffice.ui.models.TalentFlawPresetValueTableAdapter;
import net.sf.rmoffice.ui.models.TalentFlawPresetValueTableAdapter.SelectionHolder;
import net.sf.rmoffice.ui.renderer.ColoredBooleanRenderer;
import net.sf.rmoffice.ui.renderer.MultiLineTableCellRenderer;
import net.sf.rmoffice.util.DialogUtils;

import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.TableSearchable;

/**
 * Shows all talents and flaws from the configuration with a description.
 */
public class TalentFlawPresetDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	private final MetaData metaData;
	private ValueHolder selectionHolder = new ValueHolder();
	private final BeanAdapter<RMSheet> beanAdapter;
	private JComponent panel;
	private SelectionInList<TalentFlawPreset> listModel;
	private final Frame owner;
	
	public TalentFlawPresetDialog(Frame owner, MetaData metaData, BeanAdapter<RMSheet> beanAdapter) {
		super(owner, RESOURCE.getString("ui.talentflaw.dialog.title"), true);
		this.owner = owner;
		this.metaData = metaData;
		this.beanAdapter = beanAdapter;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		panel = createPanel();
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			// clear selection (for the second time)
			listModel.getSelectionHolder().setValue(null);
			JOptionPane optionPane = createContentPane();
			setContentPane( optionPane );
			pack();
		}
		super.setVisible(visible);
	}

	private JOptionPane createContentPane() {
		final String okText = RESOURCE.getString("ui.talentflaw.dialog.addselected");
		final String cancelText = RESOURCE.getString("ui.talentflaw.dialog.cancel");
		Object[] options = new Object[] { okText, cancelText };
		final JOptionPane optionPane = new JOptionPane(
                panel,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION, null, options );
		final JButton okButton = DialogUtils.getButton(optionPane, okText);
		okButton.setEnabled(false);
		final PropertyChangeListener enabledChangeListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				okButton.setEnabled(evt.getNewValue() != null);
			}
		};
		selectionHolder.addValueChangeListener(enabledChangeListener);
        
		optionPane.addPropertyChangeListener(
			    new PropertyChangeListener() {
			        @Override
					public void propertyChange(PropertyChangeEvent e) {
			            String prop = e.getPropertyName();

			            if (isVisible() 
			             && (e.getSource() == optionPane)
			             && (JOptionPane.VALUE_PROPERTY.equals(prop))) {
			                /* If you were going to check something
			                 * before closing the window, you'd do
			                 * it here. */
			                setVisible(false);
			                if (okText.equals(optionPane.getValue())) {
			                	if (selectionHolder.getValue() != null) {
			                		SelectionHolder selection = (SelectionHolder) selectionHolder.getValue();
			                		// convert to TalentFlaw (incl. ask for options)
			                		TalentFlaw tf = createTalentFlaw(selection);
			                		List<TalentFlaw> talentsFlaws = new ArrayList<TalentFlaw>();
			                		talentsFlaws.addAll(beanAdapter.getBean().getTalentsFlaws());
			                		talentsFlaws.add(tf);
			                		beanAdapter.getBean().setTalentsFlaws(talentsFlaws);
			                	}
			                }
			                selectionHolder.removeValueChangeListener(enabledChangeListener);
			            }
			        }
			    });
		return optionPane;
	}

	private JComponent createPanel() {
		FormLayout layout = new FormLayout("550dlu", "fill:100dlu, 5dlu, fill:150dlu");

		JPanel panel = new JPanel(layout);
		List<TalentFlawPreset> talentFlawsMeta = metaData.getTalentFlaws();
		List<TalentFlawPreset> talentFlaws = new ArrayList<TalentFlawPreset>();
		talentFlaws.addAll(talentFlawsMeta);
		Comparator<? super TalentFlawPreset> tfComparator = new Comparator<TalentFlawPreset>() {
			@Override
			public int compare(TalentFlawPreset tf1, TalentFlawPreset tf2) {
				return tf1.getName().compareTo(tf2.getName());
			}
		};
		Collections.sort(talentFlaws, tfComparator);
		listModel = new SelectionInList<TalentFlawPreset>(talentFlaws);
		TalentFlawPresetTableAdapter adapter = new TalentFlawPresetTableAdapter(listModel);
		JTable table = new JTable(adapter);
		table.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
		table.setSelectionForeground(UIConstants.COLOR_SELECTION_FG);
		table.setSelectionBackground(UIConstants.COLOR_SELECTION_BG);
		table.getTableHeader().setReorderingAllowed(false);
		SingleListSelectionAdapter selectionModel = new SingleListSelectionAdapter(listModel.getSelectionIndexHolder());
		table.setSelectionModel(selectionModel);
		panel.add(new JScrollPane(table), CC.xy(1, 1));
		// Searchable support for the table
		new TableSearchable(table);
		
		TalentFlawPresetValueTableAdapter detailAdapter = new TalentFlawPresetValueTableAdapter(listModel.getSelectionHolder(), selectionHolder);
		final TableCellRenderer multiLineRenderer = new MultiLineTableCellRenderer();
		final ColoredBooleanRenderer boolRend = new ColoredBooleanRenderer();
		final DefaultCellEditor boolEditor = new DefaultCellEditor(new JCheckBox());
		((JCheckBox)boolEditor.getComponent()).setHorizontalAlignment(JCheckBox.CENTER);
		JTable detailTable = new JTable(detailAdapter) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				if (row == 0 && column > 0) {
					return true;
				}
				return false;
			}
			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				if (row == 0 && column > 0){
					return boolRend;
				}
				if (column > 0) {
					return multiLineRenderer;
				}
				return super.getCellRenderer(row, column);
			}
			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				if (row == 0 && column > 0){
					return boolEditor;
				}
				return super.getCellEditor(row, column);
			}
		};
		detailTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		detailTable.putClientProperty("autoStartsEdit", Boolean.TRUE);
		detailTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
		detailTable.setSelectionForeground(UIConstants.COLOR_SELECTION_FG);
		detailTable.setSelectionBackground(UIConstants.COLOR_SELECTION_BG);
		detailTable.getTableHeader().setReorderingAllowed(false);
		panel.add(new JScrollPane(detailTable), CC.xy(1, 3) );
		
		return panel;
	}

	/* called in EDT, will ask the user about options */
	private TalentFlaw createTalentFlaw(SelectionHolder selection) {
		TalentFlaw talFlaw = new TalentFlaw();
		TalentFlawPreset tfPreset = selection.getTalentFlaw();
		TalentFlawPresetLevel tfpLevel = selection.getTalenFlawLevel();
		// copy basics values
		talFlaw.setId(tfPreset.getId());
		talFlaw.setType(tfPreset.getType());
		talFlaw.setSource(talFlaw.getSource());
		talFlaw.setName(tfPreset.getName());
		talFlaw.setLevel(tfpLevel.getLevel());
		talFlaw.setCosts(tfpLevel.getCosts());
		// add the dynamic parts
		TalentFlawContext context = new TalentFlawContext(owner, beanAdapter.getBean());
		for (ITalentFlawPart part : tfpLevel.getTalentFlawParts() ) {
			part.addToTalentFlaw(context, talFlaw);
		}
		return talFlaw;
	}
}
