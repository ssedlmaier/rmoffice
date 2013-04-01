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

import java.util.ResourceBundle;

import javax.swing.ListModel;

import net.sf.rmoffice.meta.TalentFlawPreset;

import com.jgoodies.binding.adapter.AbstractTableAdapter;

/**
 * Table shows the name and basic information of a {@link TalentFlawPreset}. This table
 * is a read-only table to select a talent or flaw. The details, values and levels will
 * be shown in the {@link TalentFlawPresetValueTableAdapter}.
 */
public class TalentFlawPresetTableAdapter extends AbstractTableAdapter<TalentFlawPreset> {
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	private static final long serialVersionUID = 1L;

	public TalentFlawPresetTableAdapter(ListModel listModel) {
		super(listModel, RESOURCE.getString("ui.talentflaw.tablecol.name"),
				         RESOURCE.getString("ui.talentflaw.tablecol.type"),
				         RESOURCE.getString("ui.talentflaw.tablecol.source"));
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		TalentFlawPreset tfp = getRow(rowIndex);
		switch (columnIndex) {
		case 0: return tfp.getName();
		case 1: 
			int costs = tfp.getValues().get(0).getCosts();
			String cat = RESOURCE.getString("ui.talentflaw."+ (costs >= 0 ? "talent" : "flaw"));
			String type = cat + " - " + RESOURCE.getString("TalentFlawType."+tfp.getType().name());
			return type;
		case 2: return RESOURCE.getString("rolemaster.source." + tfp.getSource());
		}
		return null;
	}

}
