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

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.ui.dialog.TalentFlawPresetDialog;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;

public class TalentFlawPresentationModel extends PresentationModel<RMSheet> {
	private static final long serialVersionUID = 1L;
	
	private final Action addTalentFlawAction;
	private final Action delTalentFlawAction;

	private final ValueModel enabledModel;

	private TalentFlawPresetDialog presetDialog;
	
	public TalentFlawPresentationModel(BeanAdapter<RMSheet> beanAdapter, ValueModel enabledModel, Frame owner, MetaData metaData) {
		super(beanAdapter.getBeanChannel());
		this.enabledModel = enabledModel;
		addTalentFlawAction = new AddAction();
		delTalentFlawAction = new DelAction();
		enabledModel.addValueChangeListener(new EnabledListener());
		presetDialog = new TalentFlawPresetDialog(owner, metaData, beanAdapter);
	}

	public Action getAddTalentFlawAction() {
		return addTalentFlawAction;
	}

	public Action getDelTalentFlawAction() {
		return delTalentFlawAction;
	}
	
	public ValueModel getEnabledHolder() {
		return enabledModel;
	}
	
	private class EnabledListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			boolean enabled = Boolean.TRUE.equals(evt.getNewValue());
			addTalentFlawAction.setEnabled(enabled);
			delTalentFlawAction.setEnabled(enabled);
		}
	}

	private class AddAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public AddAction() {
			super("Add (TODO)");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			presetDialog.setVisible(true);
		}
		
	}
	private class DelAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public DelAction() {
			super("TODO (DEL)");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
		
	}
}
