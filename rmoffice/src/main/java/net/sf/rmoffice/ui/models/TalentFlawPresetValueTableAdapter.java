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
package net.sf.rmoffice.ui.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import net.sf.rmoffice.meta.TalentFlawPreset;
import net.sf.rmoffice.meta.TalentFlawPresetLevel;
import net.sf.rmoffice.meta.talentflaw.ITalentFlawPart;
import net.sf.rmoffice.meta.talentflaw.TalentFlawFactory;

import com.jgoodies.binding.value.ValueModel;

/**
 * Table shows the details, values and levels ({@link TalentFlawPresetLevel}s) of
 * a selected {@link TalentFlawPreset}.
 */
public class TalentFlawPresetValueTableAdapter extends DefaultTableModel {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	private static final String COSTS = "costs";
	private static final String BGO = "bgo";
	
	private final ValueModel beanChannel;
	private final ValueModel selectionHolder;

	public TalentFlawPresetValueTableAdapter(ValueModel beanChannel, ValueModel selectionHolder) {
		this.beanChannel = beanChannel;
		this.selectionHolder = selectionHolder;
		updateData();
		this.beanChannel.addValueChangeListener(new BeanChangeListener());
	}
	
	@Override
	public void setValueAt(Object aValue, int row, int column) {
		// switch others off
		if (row == 0 && column > 0) {
			if (Boolean.TRUE.equals(aValue)) {
				TalentFlawPreset tf = (TalentFlawPreset)beanChannel.getValue();
				TalentFlawPresetLevel talentFlawPresetValue = tf.getValues().get(column - 1);
				selectionHolder.setValue(new SelectionHolder(tf, talentFlawPresetValue));
				for (int i=1; i<getColumnCount(); i++) {
					if (i != column) {
						super.setValueAt(Boolean.FALSE, row, i);
					}
				}
			} else {
				selectionHolder.setValue(null);
			}
		}
		super.setValueAt(aValue, row, column);
	}
	
	private void updateData() {
		selectionHolder.setValue(null);
		TalentFlawPreset tf = (TalentFlawPreset)beanChannel.getValue();
		if (tf == null) {
			setDataVector(new String[0][0], new Object[0]);
		} else {
			Vector<Object> colsName = new Vector<Object>();
			Vector<Vector<Object>> data = new Vector<Vector<Object>>();
			colsName.add(""); // empty 1,1 col
			List<TalentFlawPresetLevel> values = tf.getValues();
			if (values != null) {
				for (int columnIdx=0; columnIdx < values.size(); columnIdx++) {
					colsName.add(RESOURCE.getString("TalentFlawLevel."+values.get(columnIdx).getLevel().name()));
				}
			}
			// create the selection row with the checkbox
			Vector<Object> row = new Vector<Object>();
			data.add(row);
			// create the first line
			row.add("");
			if (values != null) {
				for (int columnIdx=1; columnIdx <= values.size(); columnIdx++) {
					row.add(Boolean.FALSE);
				}
				// create for data
				createDataLine(data, values, COSTS);
				createDataLine(data, values, BGO);
				// dynamic data
				for (String partID : TalentFlawFactory.getPartIDs()) {
					createDataLine(data, values, partID);
				}
			}
			setDataVector(data, colsName);
		}
	}

	private void createDataLine(Vector<Vector<Object>> data, List<TalentFlawPresetLevel> levels, String prop) {
		// check if there is a value
		boolean found = false;
		for (int columnIdx=0; columnIdx < levels.size(); columnIdx++) {
			TalentFlawPresetLevel val = levels.get(columnIdx);
			if (getValue(val, prop) != null) {
				found = true;
			}
		}
		// we have at least one value, so we print the line
		if (found) {
			Vector<Object> row = new Vector<Object>();
			data.add(row);
			// create the line
			row.add(RESOURCE.getString("ui.talentflaw.value."+prop));
			for (int columnIdx=1; columnIdx <= levels.size(); columnIdx++) {
				TalentFlawPresetLevel val = levels.get(columnIdx-1);
				row.add(getValue(val, prop));
			}
		}
	}

	private Object getValue(TalentFlawPresetLevel val, String id) {
		if (COSTS.equals(id)) {
			return ""+val.getCosts();
		} else if (BGO.equals(id)) {
			return ""+val.getLevel().getBGCost();
		}
		// search through all the parts
		List<String> values = new ArrayList<String>();
		for (ITalentFlawPart part : val.getTalentFlawParts()) {
			if (part.getId().equals(id)) {
				values.add(part.asText());
			}
		}
		if (values.size() > 0) {
			return values;
		}
		return null;
	}


	/**
	 * 
	 */
	public static class SelectionHolder {
		private TalentFlawPreset talentFlaw;
		private TalentFlawPresetLevel talenFlawValue;
		
		public SelectionHolder(TalentFlawPreset talentFlaw, TalentFlawPresetLevel talenFlawValue) {
			super();
			this.talentFlaw = talentFlaw;
			this.talenFlawValue = talenFlawValue;
		}
		public TalentFlawPreset getTalentFlaw() {
			return talentFlaw;
		}
		public TalentFlawPresetLevel getTalenFlawLevel() {
			return talenFlawValue;
		}
		
	}
	
	/* **************************************************** 
	 * Notified about bean changes (selection change events)
	 **************************************************** */
	private class BeanChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			updateData();
		}
	}
}
