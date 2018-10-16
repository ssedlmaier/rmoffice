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
package net.sf.rmoffice.ui.dialog;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

import net.sf.rmoffice.ui.components.CheckBoxList;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Shows elements to select one or many in form of checkbox items.
 */
public class SelectionDialog<T> extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private CheckBoxList<T> checkBoxlist;
	private List<T> checkedItems;
	

	public SelectionDialog(Frame owner, int amount, Object... selectables) {
		super(owner, RESOURCE.getString("ui.talentflaw.dialog.title"), true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setContentPane( createContentPane(amount, selectables) );
		pack();
	}
	
	/**
	 * Returns the checked items.
	 * 
	 * @return the checked items, not {@code null}
	 */
	public List<T> getCheckedItems() {
		return checkedItems;
	}


	private Container createContentPane(final int amount, final Object... selectables) {
		FormLayout layout = new FormLayout("150dlu", "15dlu,3dlu,fill:150dlu,5dlu,15dlu");
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		String msg = MessageFormat.format(RESOURCE.getString("ui.talentflaw.dialog.select"), ""+amount);
		builder.addLabel(msg , CC.xy(1, 1));
		checkBoxlist = new CheckBoxList<T>();
		//
		builder.add(new JScrollPane(checkBoxlist), CC.xy(1, 3));
		// buttons
		final OKAction okAction = new OKAction();
		builder.add(new JButton(okAction), CC.xy(1, 5));
		
		/* enabled/disable the action */
		checkBoxlist.addPropertyChangeListener(CheckBoxList.ITEMS_CHECKED_PROP, new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				boolean enabled = false;
				Integer checkedItems = (Integer) evt.getNewValue();
				if (checkedItems != null) {
					enabled = (amount == checkedItems.intValue());
				}
				okAction.setEnabled(enabled);
			}
		});
		checkBoxlist.setSelectableValues(selectables);
		return builder.getPanel();
	}
	
	private class OKAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public OKAction() {
			super("OK");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			checkedItems = checkBoxlist.getCheckedItems();
		}
		
	}
}
