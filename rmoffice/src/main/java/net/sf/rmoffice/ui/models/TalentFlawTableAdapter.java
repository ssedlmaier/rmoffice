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

import net.sf.rmoffice.core.TalentFlaw;

import com.jgoodies.binding.adapter.AbstractTableAdapter;

/**
 * Adapter for the table of characters talent and flaws.
 */
public class TalentFlawTableAdapter extends AbstractTableAdapter<TalentFlaw> {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$

	public TalentFlawTableAdapter(ListModel model) {
		super(model, RESOURCE.getString("ui.talentflaw.tablecol.name"),
				RESOURCE.getString("ui.talentflaw.tablecol.type"),
				RESOURCE.getString("ui.talentflaw.tablecol.level")
				);
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		TalentFlaw row = getRow(rowIndex);
		switch (columnIndex) {
		case 0:
			return row.getName();
		case 1:
			return RESOURCE.getString("TalentFlawType."+row.getType().name());
		case 2:
			return RESOURCE.getString("TalentFlawLevel."+row.getLevel().name());
		}
		return null;
	}
}
