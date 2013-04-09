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
package net.sf.rmoffice.ui.models;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.ui.UIConstants;
import net.sf.rmoffice.ui.converters.SelectionIndexToEnabledListener;
import net.sf.rmoffice.ui.dialog.TalentFlawCreateDialog;
import net.sf.rmoffice.ui.dialog.TalentFlawPresetDialog;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * Presentation model for the character tab "talent and flaws".
 */
public class TalentFlawPresentationModel extends PresentationModel<RMSheet> {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	private final Action addTalentFlawAction;
	private final Action delTalentFlawAction;
	private final Action createTalentFlawAction;
	private final ValueModel enabledModel;
	private ValueHolder selectionEnabledHolder;

	private TalentFlawPresetDialog presetDialog;
	private TalentFlawCreateDialog createDialog;
	private SelectionInList<TalentFlaw> listModel;
	
	public TalentFlawPresentationModel(BeanAdapter<RMSheet> beanAdapter, ValueModel enabledModel, Frame owner, MetaData metaData) {
		super(beanAdapter.getBeanChannel());
		this.enabledModel = enabledModel;
		// 
		presetDialog = new TalentFlawPresetDialog(owner, metaData, beanAdapter);
		createDialog = new TalentFlawCreateDialog(owner, metaData, beanAdapter);
		listModel = new SelectionInList<TalentFlaw>(getModel(RMSheet.TALENTSFLAWS_PROP));
		// add buttons
		addTalentFlawAction = new AddAction();
		enabledModel.addValueChangeListener(new ActionEnabledListener(addTalentFlawAction));
		// delete button
		delTalentFlawAction = new DelAction();
		selectionEnabledHolder = new ValueHolder(false);
		ValueModel selectionIndexHolder = listModel.getSelectionIndexHolder();
		selectionIndexHolder.addValueChangeListener(new SelectionIndexToEnabledListener(selectionEnabledHolder));
		selectionEnabledHolder.addValueChangeListener(new ActionEnabledListener(delTalentFlawAction));
		// create Talent/Flaw
		createTalentFlawAction = new CreateAction();
		enabledModel.addValueChangeListener(new ActionEnabledListener(createTalentFlawAction));
	}

	public Action getAddTalentFlawAction() {
		return addTalentFlawAction;
	}

	public Action getDelTalentFlawAction() {
		return delTalentFlawAction;
	}

	public Action getCreateTalentFlawAction() {
		return createTalentFlawAction;
	}
	
	public ValueModel getEnabledHolder() {
		return enabledModel;
	}
	
	public SelectionInList<TalentFlaw> getListModel() {
		return listModel;
	}
	
	public ValueHolder getSelectionEnabledHolder() {
		return selectionEnabledHolder;
	}
	
	/* 
	 * Listener for enabled changes.
	 */
	private class ActionEnabledListener implements PropertyChangeListener {

		private final Action action;

		public ActionEnabledListener(Action action) {
			this.action = action;
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			boolean enabled = Boolean.TRUE.equals(evt.getNewValue());
			action.setEnabled(enabled);
		}
	}

	/* 
	 * add action.
	 */
	private class AddAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public AddAction() {
			super(RESOURCE.getString("ui.talentflaw.add"), UIConstants.ICON_NEWLINE);
			setEnabled(false);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			presetDialog.setVisible(true);
		}
		
	}
	
	/*
	 * delete action.
	 */
	private class DelAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public DelAction() {
			super(RESOURCE.getString("ui.talentflaw.delete"), UIConstants.ICON_DELETE);
			setEnabled(false);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (listModel.getSelection() != null) {
				List<TalentFlaw> talentsFlaws = new ArrayList<TalentFlaw>();
				talentsFlaws.addAll(getBean().getTalentsFlaws());
				talentsFlaws.remove(listModel.getSelection());
				getBean().setTalentsFlaws(talentsFlaws);
			}
		}
		
	}
	
	private class CreateAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public CreateAction() {
			super(RESOURCE.getString("ui.talentflaw.add"), UIConstants.ICON_WAND);
			setEnabled(false);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			createDialog.setVisible(true);
		}
	}
}
