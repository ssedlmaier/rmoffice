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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.sf.rmoffice.core.InfoPage;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.ui.UIConstants;
import net.sf.rmoffice.ui.converters.SelectionIndexToEnabledListener;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;


/**
 *  Tab panel for info pages that are added at the end of the generated PDF.
 */
public class InfoPagePanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private static final String CMD_NEW = "new";
	private static final String CMD_DEL = "del";
	
	private SelectionInList<InfoPage> listModel;
	
	/**
	 * @param rmSheetAdapter the adapter of the {@link RMSheet}
	 */
	public InfoPagePanel(BeanAdapter<RMSheet> rmSheetAdapter) {
		super(new BorderLayout());
		/* prepare models */
		BeanAdapter<RMSheet>.SimplePropertyAdapter infoModel = rmSheetAdapter.getValueModel(RMSheet.PROPERTY_INFO_PAGES);
		listModel = new SelectionInList<InfoPage>(infoModel);
		ValueHolder enabledHolder = new ValueHolder(false);
		
		/* build UI */
		JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);		
		/* left pane */
		JPanel leftPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = createButtons(enabledHolder);
		leftPanel.add(buttonPanel, BorderLayout.NORTH);
		JList list = new JList();
		Bindings.bind(list, listModel);
		leftPanel.add(new JScrollPane(list), BorderLayout.CENTER);
		pane.setLeftComponent(leftPanel);
		
		/* text area pane & title */
		final BeanAdapter<InfoPage> beanAdapter = new BeanAdapter<InfoPage>(listModel);
		beanAdapter.addBeanPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (InfoPage.PROP_TITLE.equals(evt.getPropertyName()) && listModel.getSelectionIndex() > -1) {
					listModel.fireSelectedContentsChanged();
				}
			}
		});
		
		BeanAdapter<InfoPage>.SimplePropertyAdapter contentValueModel = beanAdapter.getValueModel("content");
		JTextArea textArea = BasicComponentFactory.createTextArea(contentValueModel, false);
		Bindings.bind(textArea, "enabled", enabledHolder);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		BeanAdapter<InfoPage>.SimplePropertyAdapter titleValueModel = beanAdapter.getValueModel("title");
		JTextField tfTitle = BasicComponentFactory.createTextField(titleValueModel, false);
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(tfTitle, BorderLayout.NORTH);
		Bindings.bind(tfTitle, "enabled", enabledHolder);
		rightPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
		pane.setRightComponent(rightPanel);
		add(pane, BorderLayout.CENTER);
		
		/* enabled handling */
		listModel.getSelectionIndexHolder().addValueChangeListener(new SelectionIndexToEnabledListener(enabledHolder));
	}

	private JPanel createButtons(ValueHolder enabledHolder) {
		JPanel panel = new JPanel();
		JButton btNew = new JButton(RESOURCE.getString("ui.tab.info.newpage"), UIConstants.ICON_NEW);
		btNew.setToolTipText(RESOURCE.getString("ui.tab.info.tooltip.new"));
		btNew.setActionCommand(CMD_NEW);
		btNew.addActionListener(this);
		panel.add(btNew);
		
		JButton btDelete = new JButton(RESOURCE.getString("ui.tab.info.delpage"), UIConstants.ICON_DELETE);
		btDelete.setToolTipText(RESOURCE.getString("ui.tab.info.tooltip.delete"));
		btDelete.setActionCommand(CMD_DEL);
		btDelete.addActionListener(this);
		Bindings.bind(btDelete, "enabled", enabledHolder);
		panel.add(btDelete);
		return panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (CMD_NEW.equals( e.getActionCommand() )) {
			List<InfoPage> newList = new ArrayList<InfoPage>();
			newList.addAll(listModel.getList());
			InfoPage page = new InfoPage();
			newList.add(page);
			page.setTitle(RESOURCE.getString("ui.tab.info.newpagetitle")+newList.size());
			listModel.setList(newList);
			listModel.setSelection(page);
		} else if (CMD_DEL.equals( e.getActionCommand() )) {
			InfoPage selection = listModel.getSelection();
			if (selection != null) {
				List<InfoPage> newList = new ArrayList<InfoPage>();
				newList.addAll(listModel.getList());
				newList.remove(selection);
				listModel.setList(newList);
			}
		}
	}

}
