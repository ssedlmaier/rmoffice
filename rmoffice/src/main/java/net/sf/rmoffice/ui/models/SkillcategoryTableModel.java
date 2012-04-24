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
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import net.sf.rmoffice.LevelUpVetoException;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.core.Rank;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.Skillcost;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.ui.RMFrame;
import net.sf.rmoffice.ui.renderer.IOverlaySupportable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 */
public class SkillcategoryTableModel extends DefaultTableModel implements IOverlaySupportable {
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private final static Logger log = LoggerFactory.getLogger(SkillcategoryTableModel.class);
	
	
	public static final int SKILLCAT_TABLE_COL_NAME = 0;
	public  static final int SKILLCAT_TABLE_COL_STAT = 1;
	public  static final int SKILLCAT_TABLE_COL_AP_COST = 2;
	public  static final int SKILLCAT_TABLE_COL_RANKS = 3;
	public  static final int SKILLCAT_TABLE_COL_RANK_BONUS = 4;
	public  static final int SKILLCAT_TABLE_COL_STAT_BONUS = 5;
	public  static final int SKILLCAT_TABLE_COL_PROF_BONUS = 6;
	public  static final int SKILLCAT_TABLE_COL_SPEC_BONUS = 7;
	/* user specific special bonus */
	public  static final int SKILLCAT_TABLE_COL_SPEC2_BONUS = 8;
	public  static final int SKILLCAT_TABLE_COL_TOTAL_BONUS = 9;

	private static Vector<String> skillgroupTableColums = new Vector<String>();

	
	static {
		skillgroupTableColums.add(RESOURCE.getString("ui.skillcategory.table.skillgroup"));
		skillgroupTableColums.add(RESOURCE.getString("ui.skillcategory.table.stat"));
		skillgroupTableColums.add(RESOURCE.getString("rolemaster.dpcosts"));
		skillgroupTableColums.add(RESOURCE.getString("common.ranks"));
		skillgroupTableColums.add(RESOURCE.getString("ui.skillcategory.table.bonus.rank"));
		skillgroupTableColums.add(RESOURCE.getString("ui.skillcategory.table.bonus.stat"));
		skillgroupTableColums.add(RESOURCE.getString("ui.skillcategory.table.bonus.prof"));
		skillgroupTableColums.add(RESOURCE.getString("ui.skillcategory.table.bonus.special"));
		skillgroupTableColums.add(RESOURCE.getString("ui.skillcategory.table.bonus.special2"));
		skillgroupTableColums.add(RESOURCE.getString("ui.skillcategory.table.bonus.total"));
	}
	
	private RMSheet sheet;
	private final MetaData meta;
	
	/**
	 * @param meta the meta data
	 * 
	 */
	public SkillcategoryTableModel(MetaData meta) {
		super(new Vector<Vector<Object>>(), skillgroupTableColums);
		this.meta = meta;
	}
	
	public void setSheet(RMSheet sheet) {
		this.sheet = sheet;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isCellEditable(int row, int column) {
		/* special2 bonus is always editable */
		if (column == SKILLCAT_TABLE_COL_SPEC2_BONUS) {
			return true;
		}
		/* */
		SkillCategory sg = (SkillCategory)getValueAt(row, SKILLCAT_TABLE_COL_NAME);
		if (sg.getRankType().isGroupRankEditable()) {
			return column == SKILLCAT_TABLE_COL_RANKS;
		} else {
			return false;
		}				
	}
	
	@Override
	public java.lang.Class<?> getColumnClass(int column) {
		if (column == SKILLCAT_TABLE_COL_RANKS || column == SKILLCAT_TABLE_COL_SPEC2_BONUS) {
			return Integer.class;
		} else {
			return String.class;
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void setValueAt(Object aValue, int row, int column) {
		if (column == SKILLCAT_TABLE_COL_RANKS) {
			SkillCategory gr = (SkillCategory) super.getValueAt(row, SKILLCAT_TABLE_COL_NAME);
			BigDecimal rank = BigDecimal.valueOf(0);
			if (aValue != null) {
				if ( gr.getRankType().isGroupRankEditable()) {
					rank = BigDecimal.valueOf( ((Integer) aValue).intValue() );
				}
			}
			try {
				sheet.setSkillcategoryRank(gr, rank);
				super.setValueAt(aValue, row, column);
			} catch (LevelUpVetoException vetoEx) {
				if (log.isDebugEnabled()) log.debug(vetoEx.getMessage());
			}
		} else if (column == SKILLCAT_TABLE_COL_SPEC2_BONUS) {
			SkillCategory gr = (SkillCategory) super.getValueAt(row, SKILLCAT_TABLE_COL_NAME);
			/* special bonus is integer */
			Integer bonus = (Integer) aValue;
			sheet.setSkillcategorySpecialBonus(gr, (bonus == null ? 0 : bonus.intValue()) );
			super.setValueAt(aValue, row, column);
		} else {
			super.setValueAt(aValue, row, column);
		}
	}

	/**
	 * 
	 */
	public void updateTable() {
		if (getRowCount() == 0) {
			createSkillcategoryData();
		} else {
			updateSkillcategoryData();
		}
	}
	
	private void createSkillcategoryData() {		
		for (SkillCategory cat : meta.getSkillCategories()) {
			Vector<Object> row = new Vector<Object>();
			/* SkillCategory */
			row.add(cat);
			/* Stat */
			StringBuffer sb = new StringBuffer();
			for (StatEnum stat : sheet.getSkillcategoryStats(cat)) {
				if (sb.length() > 0) {
					sb.append("/");
				}
				sb.append(RESOURCE.getString("StatEnum."+stat.name()+".short"));				
			}
			row.add(sb.toString());			
			/* AP-Kosten */
			Skillcost cost = sheet.getSkillcost(cat);
			row.add(cost.toString());
			if (cat.getRankType().isGroupRankEditable()) {
				/* Ranks */
				row.add( Integer.valueOf(0) );
				/* Rank-Bonus */
				row.add( "" );
			} else {
				row.add(null);
				row.add( "0" );
			}
			/* Attr-Bonus */
			row.add("");
			/* profession bonus */
			row.add("");
			/* Special */
			row.add(""+Integer.valueOf(sheet.getSkillcategorySpecial1Bonus(cat)));
			/* Special user defined */
			row.add(sheet.getSkillcategoryRank(cat).getSpecialBonus());
			/* Gesamt-Bonus */
			row.add("");
			addRow(row);
		}
		updateSkillcategoryData();
	}

	/**
	 * 
	 */
	private void updateSkillcategoryData() {
		/* skill group table */
		int rowCount = getRowCount();
		for(int row=0; row < rowCount; row++) {
			SkillCategory cat = (SkillCategory )super.getValueAt(row, SkillcategoryTableModel.SKILLCAT_TABLE_COL_NAME);
			/* stat */
			StringBuffer sb = new StringBuffer();
			for (StatEnum stat : sheet.getSkillcategoryStats(cat)) {
				if (sb.length() > 0) {
					sb.append("/");
				}
				sb.append(RESOURCE.getString("StatEnum."+stat.name()+".short"));				
			}
			super.setValueAt(sb.toString(), row, SkillcategoryTableModel.SKILLCAT_TABLE_COL_STAT);
			/* skill group bonus */
			int skillgroupAttrBonus = sheet.getSkillcategoryStatBonus(cat);
			super.setValueAt(RMFrame.format(skillgroupAttrBonus), row, SkillcategoryTableModel.SKILLCAT_TABLE_COL_STAT_BONUS);
			/* skill group prof bonus */			
			int skillgroupProfBonus = 0;
			if (sheet.getProfession() != null) {
				skillgroupProfBonus = sheet.getProfession().getSkillgroupBonus(cat.getId().intValue());
			}
			super.setValueAt(RMFrame.format(skillgroupProfBonus), row, SkillcategoryTableModel.SKILLCAT_TABLE_COL_PROF_BONUS);
			/* ap costs */
			Skillcost cost = sheet.getSkillcost(cat);
			super.setValueAt(cost, row, SkillcategoryTableModel.SKILLCAT_TABLE_COL_AP_COST);
			/* rank & rank bonus */
			Rank rank = sheet.getSkillcategoryRank(cat);
			Integer rankObj = null;
			if (cat.getRankType().isGroupRankEditable()) {
				rankObj =  Integer.valueOf( rank.getRank().intValue() );
			}
			super.setValueAt(rankObj, row, SkillcategoryTableModel.SKILLCAT_TABLE_COL_RANKS);
			super.setValueAt(RMFrame.format(sheet.getSkillcategoryRankBonus(cat)), row, SkillcategoryTableModel.SKILLCAT_TABLE_COL_RANK_BONUS);
			/* special bonus */
			super.setValueAt(RMFrame.format(sheet.getSkillcategorySpecial1Bonus(cat)), row, SkillcategoryTableModel.SKILLCAT_TABLE_COL_SPEC_BONUS);
			/* special (user defined) bonus */
			Integer spec2Bonus = rank.getSpecialBonus();
			if (spec2Bonus == null) {
				spec2Bonus = Integer.valueOf(0);
			}
			super.setValueAt(spec2Bonus, row, SkillcategoryTableModel.SKILLCAT_TABLE_COL_SPEC2_BONUS);
			/* total bonus */
			super.setValueAt(RMFrame.format(sheet.getSkillcategoryTotalBonus(cat)), row, SkillcategoryTableModel.SKILLCAT_TABLE_COL_TOTAL_BONUS);
		}
	}

	/** {@inheritDoc} */
	@Override
	public int getLevelUpSteps(int row, int column) {
		if (column == SKILLCAT_TABLE_COL_RANKS && sheet.getLevelUp().getLvlUpActive()) {
			SkillCategory category = (SkillCategory) super.getValueAt(row, SKILLCAT_TABLE_COL_NAME);
			return sheet.getLevelUp().getLevelUpSteps(category);
		}
		return 0;
	}
	
	
}
