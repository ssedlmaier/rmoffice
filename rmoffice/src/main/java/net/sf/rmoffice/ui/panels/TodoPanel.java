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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.core.ToDo;
import net.sf.rmoffice.ui.UIConstants;
import net.sf.rmoffice.ui.renderer.TextAreaTableCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 */
public class TodoPanel extends JPanel implements PropertyChangeListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private final static Logger log = LoggerFactory.getLogger(TodoPanel.class);
	private static final Icon OK = new ImageIcon(LevelUpStatusBar.class.getResource("/images/icons/ok.png"));
	
	private JButton btDone;
	private JTable todosTable;
	private RMSheet sheet;

	public TodoPanel() {
		super(new BorderLayout());
		Dimension dim = getPreferredSize();
		dim.height = 72;
		setPreferredSize(dim);
	}
	
	public void init() {
		btDone = new JButton(RESOURCE.getString("ui.todo.done"), OK);
		btDone.addActionListener(this);
		todosTable = new JTable(0, 1) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
		    }
			@Override
			public java.lang.Class<?> getColumnClass(int column) {
				return ToDo.class;
			}
		};
		todosTable.setBackground(Color.yellow);
		todosTable.setSelectionBackground(Color.orange);
		todosTable.setTableHeader(null);
		todosTable.setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
		todosTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TextAreaTableCellRenderer renderer = new TextAreaTableCellRenderer();
		todosTable.setDefaultRenderer(ToDo.class, renderer);
		add(btDone, BorderLayout.EAST);
		JScrollPane pane = new JScrollPane(todosTable);
		add(pane, BorderLayout.CENTER);
	}
	
	public void setSheet(RMSheet sheet) {
		if (this.sheet != null) {
			this.sheet.removePropertyChangeListener(RMSheet.PROPERTY_TODO_CHANGED, this);
		}
		this.sheet = sheet;
		this.sheet.addPropertyChangeListener(RMSheet.PROPERTY_TODO_CHANGED, this);
	}

	/** {@inheritDoc} */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (log.isDebugEnabled()) log.debug("received todo change event");
		for (int i = todosTable.getModel().getRowCount() - 1; i >= 0; i--) {
			((DefaultTableModel)todosTable.getModel()).removeRow(0);
		}
		List<ToDo> toDos = sheet.getToDos();
		if (toDos.size() > 0) {
			for (ToDo td : toDos) {
				((DefaultTableModel)todosTable.getModel()).addRow(new Object[]{td});
			}
			if (todosTable.getRowCount() == 1) {
				todosTable.getSelectionModel().setSelectionInterval(0, 0);
			} else {
				todosTable.getSelectionModel().setAnchorSelectionIndex(-1);
			}
			setVisible(true);
		} else {
			setVisible(false);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(ActionEvent e) {
		int selectedRow = todosTable.getSelectedRow();
		if (selectedRow > -1) {
			ToDo td = (ToDo) todosTable.getValueAt(selectedRow, 0);
			sheet.finishTodo( td );
		}
	}
}
