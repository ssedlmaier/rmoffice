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
package net.sf.rmoffice.ui.renderer;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellRenderer;

import net.sf.rmoffice.core.items.MagicalFeature;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.ui.UIConstants;

import com.jidesoft.swing.DefaultOverlayable;


/**
 * Editor for {@link SkillCategory}, {@link ISkill}s and {@link MagicalFeature#getBonus()}
 */
public class NumberSpinnerTableRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	private final OverlayableSpinner[] spinner;
	private final DefaultOverlayable[] overlayable;
	private final boolean alwaysShow;
	
	/**
	 * Creates a new cell editor instance where the spinner component is shown only
	 * if the underlying object need this. Example: an non-editable skill category does not
	 * need a jspinner.
	 */
	public NumberSpinnerTableRenderer() {
		this(false);
	}
	
	/**
	 * Creates new cell editor instance.
	 * 
	 * @param alwaysShow whether the spinner component should shown always or not
	 */
    public NumberSpinnerTableRenderer(boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
        spinner = new OverlayableSpinner[4];
        overlayable = new DefaultOverlayable[4];
        for (int i=0; i<spinner.length; i++) {
        	spinner[i]  = new OverlayableSpinner(new SpinnerNumberModel(0d, 0d, 100d, 0.5d));
        	spinner[i].setBorder(null);
        	overlayable[i] = new DefaultOverlayable(spinner[i]);
        	if (i > 0) {
        		JLabel icon = new JLabel(new ImageIcon(NumberSpinnerTableRenderer.class.getResource("/images/icons/overlay_"+i+".png")));   		
        		overlayable[i].addOverlayComponent(icon, DefaultOverlayable.WEST);
        		overlayable[i].setOverlayVisible(true);
        	}
        }
    }

    @Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    	/* ranks */
		Object val = table.getValueAt(row, 0);
		if (val instanceof SkillCategory) {
			SkillCategory gr = (SkillCategory)val;
			/* not editable && not special2 (=!alwaysShow) = return Label*/
			if ( ! gr.getRankType().isGroupRankEditable() && !alwaysShow ) {
				return getEmptyLabel(table, isSelected);
			}
		}
		/* MagicalFeature#bonus */
		if (value instanceof MagicalFeature) {
			MagicalFeature feature = (MagicalFeature)value;
			if (feature.getType().isBonusAvailable()) {
				value = feature.getBonus();
			} else {
				return getEmptyLabel(table, isSelected);
			}
		}
		// determine the renderer index
		int rendererIdx = 0;
		if (table.getModel() instanceof IOverlaySupportable) {
			IOverlaySupportable overlaySupport = (IOverlaySupportable) table.getModel();
			int actualRow = table.convertRowIndexToModel(row);
			int actualCol = table.convertColumnIndexToModel(column);
			rendererIdx = overlaySupport.getLevelUpSteps(actualRow, actualCol);
		}
		 // modify renderer
		if (isSelected) {
			spinner[rendererIdx].setBackground(table.getSelectionBackground());
			((NumberEditor)spinner[rendererIdx].getEditor()).getTextField().setBackground(table.getSelectionBackground());
			setOpaque(true);
		} else {
			spinner[rendererIdx].setBackground(table.getBackground());
			((NumberEditor)spinner[rendererIdx].getEditor()).getTextField().setBackground(UIConstants.COLOR_EDITABLE_BG);
			setOpaque(false);
		}		
        if(value == null) {
            spinner[rendererIdx].setValue(Double.valueOf(0));
        } else {
            spinner[rendererIdx].setValue(value);
        }
       
        return overlayable[rendererIdx];
    }

	/**
	 * 
	 * @param table
	 * @param isSelected
	 * @return
	 */
	private Component getEmptyLabel(JTable table, boolean isSelected) {
		if (isSelected) {
			setBackground(table.getSelectionBackground());
			setOpaque(true);
		} else {
			setBackground(table.getBackground());
			setOpaque(false);	
		}
		return this;
	}
}
