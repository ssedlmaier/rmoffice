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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import net.sf.rmoffice.LevelUpVetoException;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.core.Rank;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.Skillcost;
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.ui.renderer.IOverlaySupportable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 */
public class SkillsTableModel extends DefaultTableModel implements IOverlaySupportable {
	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(SkillsTableModel.class);
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$

	public static final int COL_SKILL = 0;
	public static final int COL_SKILLGROUP = 1;
	public static final int COL_FAVORITE = 2;
	public static final int COL_RANK = 3;
	public static final int COL_RANK_TYPE = 4;
	public static final int COL_COST = 5;
	/** talents & flaws bonus */
	public static final int COL_SPECIAL2_BONUS = 6; 
	/** user defined */
	public static final int COL_SPECIAL_BONUS = 7; 
	public static final int COL_TOTAL_BONUS = 8;
	public static final int COL_ITEM_BONUS = 9;

	private String[] columNames;
	private RMSheet sheet;

	/**
	 * 
	 */
	public SkillsTableModel() {
		columNames = new String[] { RESOURCE.getString("common.skill"),
				RESOURCE.getString("ui.skills.table.skillgroup"),
				RESOURCE.getString("common.favorite"),
				RESOURCE.getString("common.ranks"),
				RESOURCE.getString("ui.skills.table.skilltype"),
				RESOURCE.getString("rolemaster.dpcosts"),
				RESOURCE.getString("pdf.page3.bonus.special"),
				RESOURCE.getString("pdf.page3.bonus.special"),
				RESOURCE.getString("common.bonus"), 
				RESOURCE.getString("pdf.page3.bonus.item") 
				};
	}

	/** {@inheritDoc} */
	@Override
	public int getColumnCount() {
		return columNames.length;
	}

	/** {@inheritDoc} */
	@Override
	public String getColumnName(int column) {
		return columNames[column];
	}

	/** {@inheritDoc} */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case COL_COST:
			return Skillcost.class;
		case COL_FAVORITE:
			return Boolean.class;
		case COL_RANK:
			return Double.class;
		case COL_SPECIAL_BONUS:
		case COL_SPECIAL2_BONUS:
		case COL_TOTAL_BONUS:
			return Integer.class;
		default:
			return String.class;
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean isCellEditable(int row, int column) {
		return column == COL_FAVORITE || column == COL_RANK || column == COL_SPECIAL_BONUS;
	}

	public void setSheet(RMSheet sheet) {
		this.sheet = sheet;
		for (int i = getRowCount() - 1; i >= 0; i--) {
			super.removeRow(i);
		}
	}
	
	public ISkill getSkillAtRow(int row) {
		return (ISkill)super.getValueAt(row, COL_SKILL);
	}
	
	/** {@inheritDoc} */
	@Override
	public Object getValueAt(int row, int column) {
		if (column == COL_SKILL) {
			ISkill skill = (ISkill)super.getValueAt(row, column);
			if (skill != null) {
				return skill.getName();
			} else {
				return "";
			}
		}
		return super.getValueAt(row, column);
	}
	
	/** {@inheritDoc} */
	@Override
	public void setValueAt(Object aValue, int row, int column) {
		ISkill skill = (ISkill) super.getValueAt(row, COL_SKILL);			
		if (column == COL_RANK) {
			try {
				if (aValue instanceof Double) {
					sheet.setSkillRank(skill, BigDecimal.valueOf(((Double)aValue).doubleValue()));
				} else {
					sheet.setSkillRank(skill, BigDecimal.valueOf(0));
				}
				super.setValueAt(Integer.valueOf(sheet.getSkillTotalBonus(skill)), row, COL_TOTAL_BONUS);
			} catch (LevelUpVetoException e) {
				if (log.isDebugEnabled()) log.debug(e.getMessage());
			}
		} else if (column == COL_FAVORITE) {
			Rank skillRank = sheet.getSkillRank(skill);
			Boolean favVal = Boolean.valueOf(aValue instanceof Boolean && ((Boolean) aValue).booleanValue());
			skillRank.setFavorite(favVal);
			super.setValueAt(aValue, row, column);
		} else if (column == COL_SPECIAL_BONUS) {
			if (aValue instanceof Integer) {
				sheet.setSkillSpecialBonus(skill, ((Integer) aValue).intValue());
			} else {
				sheet.setSkillSpecialBonus(skill, 0);
			}
			super.setValueAt(aValue, row, column);
		} else {
			super.setValueAt(aValue, row, column);
		}
		super.setValueAt(Integer.valueOf(sheet.getSkillTotalBonus(skill)), row, COL_TOTAL_BONUS);
	}

	/**
	 * Adds a custom skill with a new name. 
	 * @param skill the actual skill
	 * @param newName the modified name
	 * @param type the skill type or {@code null} to use the type of the given skill
	 */
	public void addSkill(ISkill skill, String newName, SkillType type) {
		ISkill customSkill = sheet.registerCustomSkill(skill, newName, type);
		addSkill(customSkill);
	}
	
	@SuppressWarnings("unchecked")
	public void addSkill(ISkill skill) {
		/* add to sheet, if new */
		if (! sheet.hasSkillRank(skill)) {
			try {
				sheet.setSkillRank(skill, BigDecimal.ZERO);
			} catch (LevelUpVetoException e) {
				log.error(e.getMessage(), e);
			}
		}
		/* add to model */
		Object[] rowData = new Object[columNames.length];
		rowData[COL_SKILL] = skill;
		rowData[COL_SKILLGROUP] = sheet.getSkillcategory(skill).getName();
		rowData[COL_FAVORITE] = sheet.getSkillRank(skill).getFavorite();
		BigDecimal rank = sheet.getSkillRank(skill).getRank();
		if (rank != null) {
			rowData[COL_RANK] = Double.valueOf(sheet.getSkillRank(skill).getRank().doubleValue());
		} else {
			rowData[COL_RANK] = Double.valueOf(0);
		}
		rowData[COL_RANK_TYPE] = RESOURCE.getString("SkillType."+sheet.getSkillType(skill).name());
		if (sheet.getProfession() != null) {
			rowData[COL_COST] = sheet.getSkillcost(skill);
		} else {
			rowData[COL_COST] = "n/v";
		}
		Integer specialBonus = sheet.getSkillRank(skill).getSpecialBonus();
		if (specialBonus == null) {
			specialBonus = Integer.valueOf(0);
		}
		rowData[COL_SPECIAL_BONUS] = specialBonus;
		/* talents, flaw special bonus */
		rowData[COL_SPECIAL2_BONUS] = Integer.valueOf(0);
		rowData[COL_TOTAL_BONUS] = Integer.valueOf(sheet.getSkillTotalBonus(skill));
		rowData[COL_ITEM_BONUS] = sheet.getSkillItemBonus(skill);
		addRow(rowData);
		sortAndUpdate(getDataVector());
	}

	@SuppressWarnings("unchecked")
	public void updateTable() {
		/* remove all */
		for (int row = getRowCount() - 1; row >= 0; row--) {
			super.removeRow(row);
		}
		/* add all added skills */
		for (Rank rank : sheet.getSkillRanks()) {
			ISkill skill = sheet.getSkill(rank.getId());
			if (skill != null) {
				addSkill(skill);
			} else {
				if (log.isWarnEnabled()) log.warn("Could not load skill with ID "+rank.getId());
			}
		}
		/* */
		sortAndUpdate(getDataVector());
	}
	
	/** {@inheritDoc} */
	@Override
	public void removeRow(int row) {
		ISkill skill = (ISkill)super.getValueAt(row, 0);			
		sheet.removeSkillRank(skill);
		super.removeRow(row);
	}
	
	@SuppressWarnings("unchecked")
    private void sortAndUpdate(Vector<Object> v) {
        Collections.sort(v, new Comparator<Object>() {
            @Override
			public int compare(Object o1, Object o2) {
 
                Vector<Object> v1 = (Vector<Object>) o1;
                Vector<Object> v2 = (Vector<Object>) o2;
 
                ISkill sk1 = (ISkill) v1.get(0);
                ISkill sk2 = (ISkill) v2.get(0);
                if (! sk1.isSpelllist() && sk2.isSpelllist()) {
                	return -1;
                }
                if (sk1.isSpelllist() && ! sk2.isSpelllist()) {
                	return 1;
                }
                return sk1.getName().compareTo(sk2.getName());
            }
        });
    }

	public void skillValuesChanged() {
		for (int row=0; row < getRowCount(); row++) {
			ISkill skill = (ISkill) super.getValueAt(row, COL_SKILL);
			super.setValueAt(sheet.getSkillRank(skill).getFavorite(), row, COL_FAVORITE);
			BigDecimal rank = sheet.getSkillRank(skill).getRank();
			if (rank == null) {
				rank = BigDecimal.ZERO;
			}
			super.setValueAt(Double.valueOf(rank.doubleValue()), row, COL_RANK);
			if (sheet.getProfession() != null) {
				super.setValueAt(sheet.getSkillcost(skill), row, COL_COST);
			} else {
				super.setValueAt(RESOURCE.getString("pdf.rank.notavailable.short"), row, COL_COST);
			}
			super.setValueAt("", row, COL_SPECIAL2_BONUS);
			super.setValueAt(Integer.valueOf(sheet.getSkillTotalBonus(skill)), row, COL_TOTAL_BONUS);
			super.setValueAt(sheet.getSkillItemBonus(skill), row, COL_ITEM_BONUS);
		}
	}

	/** {@inheritDoc} */
	@Override
	public int getLevelUpSteps(int row, int column) {
		if (column == COL_RANK && sheet.getLevelUp().getLvlUpActive()) {
			int coststep = sheet.getLevelUp().getLevelUpSteps( getSkillAtRow(row) );
			return coststep;
		}
		return 0;
	}
}
