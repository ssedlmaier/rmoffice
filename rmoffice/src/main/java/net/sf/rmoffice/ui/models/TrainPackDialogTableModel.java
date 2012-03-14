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

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import net.sf.rmoffice.RMPreferences;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.meta.TrainPack;


/**
 * table model for the train pack dialog table.
 */
public class TrainPackDialogTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private static final int COL_NAME = 0;
	private static final int COL_TYPE = 1;
	private static final int COL_DURATION = 2;
	private static final int COL_COSTS = 3;
	
	private static final String[] COLUMN_NAMES = {RESOURCE.getString("ui.trainpack.table.trainpack"),
		RESOURCE.getString("ui.trainpack.table.type"),
		RESOURCE.getString("ui.trainpack.table.duration"),
		RESOURCE.getString("ui.trainpack.table.costs")
	};
	private final RMSheet sheet;
	private final List<TrainPack> availableDevPacks;
	

	public TrainPackDialogTableModel(RMSheet sheet) {
		this.sheet = sheet;
		availableDevPacks = new ArrayList<TrainPack>();
		for (TrainPack tp : sheet.getAvailableDevPacks()) {
			if (!RMPreferences.getInstance().isExcluded(tp.getSource())) {
				availableDevPacks.add(tp);
			}
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public Object getValueAt(int row, int column) {
		TrainPack trainPack = availableDevPacks.get(row);
		switch (column) {
			case COL_NAME:
				return trainPack.getName();
			case COL_TYPE:
				String str = null;
				if (trainPack.getType() != null) {
					str = RESOURCE.getString("TrainPack.Type." + trainPack.getType().name() );
				} else {
					str = RESOURCE.getString("TrainPack.Type.na");
				}
				return str;
			case COL_DURATION:
				return "" + trainPack.getAquireMonth();
			case COL_COSTS:
				return "" + sheet.getDevPackCosts(trainPack);
		}
		return "";
	}
	
	/** {@inheritDoc} */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}
	
	/** {@inheritDoc} */
	@Override
	public int getRowCount() {
		return availableDevPacks.size();
	}
	
	/** {@inheritDoc} */
	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}
	
	/** {@inheritDoc} */
	@Override
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}

	/** {@inheritDoc} */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the {@link TrainPack} at given index.
	 * 
	 * @param index the row index
	 * @return {@link TrainPack}
	 */
	public TrainPack getDevPack(int index) {
		return availableDevPacks.get(index);
	}
}
