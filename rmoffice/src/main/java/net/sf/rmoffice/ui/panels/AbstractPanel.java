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
package net.sf.rmoffice.ui.panels;

import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;

/**
 * Base for default panels.
 */
public abstract class AbstractPanel<T> extends JPanel {

	private static final long serialVersionUID = 1L;

	private final static ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	protected static final String ROW = "fill:15dlu";
	public static final String ROW_WITH_GAP = ROW + ", 2dlu, ";
	protected static final String COL_WITH_EXT = "pref, 3dlu, 90dlu, 3dlu, 35dlu";
	protected static final String COLGROUP_GAP = "9dlu, ";
	protected static final String HEADLINE_SEPARATOR = " 30dlu, ";
	protected static final String COL_DEFINITION_2COLGROUPS = COL_WITH_EXT + "," + COLGROUP_GAP + COL_WITH_EXT;
	
	protected final static int COL_LBL_L = 1;
	protected final static int COL_LBL_R = 7;
	protected final static int COL_COMP_L = 3;
	protected final static int COL_COMP_R = 9;
	
	protected JTextField addTextFieldL(String prop, int row, PresentationModel<T> model,
			ValueHolder enabledValueHolder, ValueModel converter, PanelBuilder builder) {
		return addTextField(prop, COL_LBL_L, COL_COMP_L, row, model, enabledValueHolder, converter, builder);
	}
	protected JTextField addTextFieldR(String prop, int row, PresentationModel<T> model,
			ValueHolder enabledValueHolder, ValueModel converter, PanelBuilder builder) {
		return addTextField(prop, COL_LBL_R, COL_COMP_R, row, model, enabledValueHolder, converter, builder);
	}
	
	private JTextField addTextField(String prop, int lblColIdx, int compColIdx,
			int row, PresentationModel<T> model, ValueHolder enabledValueHolder, ValueModel converter, PanelBuilder builder) {
		CellConstraints cc = new CellConstraints();
		String resKey = getResKeyPrefix() + prop;
		builder.addLabel(RESOURCE.getString(resKey), cc.xy(lblColIdx, row));
		JTextField tf = new JTextField();
		if (converter == null) {
			Bindings.bind(tf, model.getModel(prop));
		} else {
			Bindings.bind(tf, converter);
		}
		Bindings.bind(tf, "enabled", enabledValueHolder);
		builder.add(tf, cc.xyw(compColIdx, row, 3));
		return tf;
	}
	
	/**
	 * 
	 */
	public abstract String getResKeyPrefix();
}
