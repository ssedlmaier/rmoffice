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
package net.sf.rmoffice.core;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.rmoffice.LevelUpVetoException;
import net.sf.rmoffice.RMPreferences;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.Skillcost;
import net.sf.rmoffice.meta.TrainPack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Is saved for each character and saves the level-up information.
 */
public class RMLevelUp {
	private final static Logger log = LoggerFactory.getLogger(RMLevelUp.class);
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	public static final String PROPERTY_LVLUP_ACTIVE = "LvlUpActive";
	public static final String PROPERTY_LVLUP_STATUSTEXT = "LvlUpStatusText";
	public static final String PROPERTY_LVLUP_DEVPOINTS = "LvlUpDevPoints";
	public static final String PROPERTY_LVLUP_SPELLLISTS = "LvlUpSpellLists";
	
	private boolean active;
	private int devPoints;
	/* deprecated since 4.2.5 */
	@SuppressWarnings("unused") @Deprecated
	private int spellRanks;
	private List<Integer> spellLists;
	private String devPointsText = "";
	private Map<Integer, Integer> skillRanks;
	private Map<Integer, Integer> skillgroupRanks;
	private transient String statusText = "";
	private transient RMSheet sheet;
	private transient MetaData meta;
	
	
	/**
	 * 
	 */
	public RMLevelUp() {
		active = false;
		statusText = "";
	}
	
	/* package private */ void init(RMSheet sheet, MetaData meta) {
		this.sheet = sheet;
		this.meta = meta;
		setLvlUpStatusText("");
		if (skillRanks == null) {
			skillRanks = new HashMap<Integer, Integer>();
		}
		if (skillgroupRanks == null) {
			skillgroupRanks = new HashMap<Integer, Integer>();
		}
		if (spellLists == null) {
			spellLists = new ArrayList<Integer>();
		}
		fireDevPointsUpdated();
	}
	
	public boolean getLvlUpActive() {
		return active;
	}
	
	public void setLvlUpActive(boolean active) {
		if (log.isDebugEnabled()) log.debug("set active to "+active);
		Boolean oldValue = Boolean.valueOf(active);
		this.active = active;
		sheet.firePropertyChange(PROPERTY_LVLUP_ACTIVE, oldValue, Boolean.valueOf(this.active));
		if (active) {
			devPoints = sheet.getDevPoints();
			skillRanks = new HashMap<Integer, Integer>();
			skillgroupRanks = new HashMap<Integer, Integer>();
			spellLists = new ArrayList<Integer>();
			fireDevPointsUpdated();
		} else {
			devPoints = 0;
			setLvlUpStatusText(null);
			fireDevPointsUpdated();
		}			
	}
	
	public String getLvlUpStatusText() {
		return statusText;
	}
	
	public void setLvlUpStatusText(String statusText) {
		String oldValue = this.statusText;
		this.statusText = statusText;
		sheet.firePropertyChange(PROPERTY_LVLUP_STATUSTEXT, oldValue, statusText);
	}
	
	private void setError(String message) {
		setLvlUpStatusText("WARNING"+message);
	}
	
	public String getLvlUpDevPoints() {
		return devPointsText;
	}
	
	/**
	 * If active it validates whether there are enough development points left or not.
	 * 
	 * @param pack the training package
	 * @throws LevelUpVetoException if train pack is not valid to add 
	 */
	/* package private */ void addTrainPack(TrainPack pack) throws LevelUpVetoException {
		if (active) {
			int costs = meta.getTrainPackCosts(pack, sheet.getProfession());
			if (costs > devPoints) {
				setError(RESOURCE.getString("ui.levelup.error.trainpack.expensive"));
				throw new LevelUpVetoException(RESOURCE.getString("ui.levelup.error.trainpack.expensive"));
			}
			devPoints -= costs;
			setLvlUpStatusText(MessageFormat.format(RESOURCE.getString("ui.levelup.msg.decrease"), Integer.valueOf(costs), pack.getName()));
			fireDevPointsUpdated();
		}
	}

	/**
	 * If active it validates whether the operation is within the valid costs and skill rank borders or not.
	 * 
	 * @param skill the skill
	 * @param rank the new rank
	 * @throws LevelUpVetoException if skill rank operation is not allowed
	 */
	/* package private */ boolean modifySkillRank(ISkill skill, BigDecimal rank) throws LevelUpVetoException {
		if (active) {
			/* current val */
			Rank rankObj = sheet.getSkillRank(skill);
			BigDecimal delta = rank.subtract(rankObj.getRank());
			/* get costs */
			Skillcost costs = sheet.getSkillcost(skill);
			/* */
			int costSteps = 0;
			if (skillRanks.containsKey(skill.getId())) {
				costSteps = skillRanks.get(skill.getId()).intValue();
			}
			if (delta.doubleValue() > 0) {
				/* increase rank*/
				if (costSteps >= costs.size()) {
					setError(RESOURCE.getString("ui.levelup.error.skill.maxreached"));
					throw new LevelUpVetoException(RESOURCE.getString("ui.levelup.error.skill.maxreached"));
				}
				/* check DPs */
				int costDP = costs.getCost(costSteps);
				if (skill.isSpelllist()) {
					costDP *= getSpellListsDPFactor(skill);
				}
				if (costDP <= devPoints) {
					skillRanks.put(skill.getId(), Integer.valueOf(costSteps + 1));
					devPoints -= costDP;
					if (log.isDebugEnabled()) log.debug("decrease DP by "+costDP);
					setLvlUpStatusText(MessageFormat.format(RESOURCE.getString("ui.levelup.msg.decrease"), Integer.valueOf(costDP), skill.getName()));
					fireDevPointsUpdated();
					sheet.firePropertyChange(PROPERTY_LVLUP_SPELLLISTS, null, getLvlUpSpellLists());
				} else {
					setError(RESOURCE.getString("ui.levelup.error.skill.notenoughDP"));
					throw new LevelUpVetoException(RESOURCE.getString("ui.levelup.error.skill.notenoughDP"));
				}
			} else if (delta.doubleValue() < 0) {
				/* decrease rank */
				if (costSteps <= 0) {
					setError(RESOURCE.getString("ui.levelup.error.skill.minreached"));
					throw new LevelUpVetoException(RESOURCE.getString("ui.levelup.error.skill.minreached"));
				}
				/* only decrease needs a second run */
				return true;
			}
		}
		return false;
	}
	
	/* */ void decreaseSkillRankSecondRun(ISkill skill) {
		Skillcost costs = sheet.getSkillcost(skill);
		int costSteps = 0;
		if (skillRanks.containsKey(skill.getId())) {
			costSteps = skillRanks.get(skill.getId()).intValue();
		}
		int costDP = costs.getCost(costSteps - 1);
		if (skill.isSpelllist()) {
			costDP *= getSpellListsDPFactor(skill);
			if (costSteps == 1) {
				/* move spell list because we reduce the last rank in spell list */
				int idx = spellLists.indexOf(skill.getId());
				moveSpelllists(idx);
			}
		}
		devPoints += costDP;
		skillRanks.put(skill.getId(), Integer.valueOf(costSteps - 1));
		if (log.isDebugEnabled()) log.debug("increase DP by "+costDP);
		setLvlUpStatusText(MessageFormat.format(RESOURCE.getString("ui.levelup.msg.increase"), Integer.valueOf(costDP), skill.getName()));
		fireDevPointsUpdated();
	}

	/*
	 * Moves the spell list from list. Changing costs will be calculated.
	 */
	private void moveSpelllists(final int idx) {
		for (int position = idx+1; position<spellLists.size(); position++) {
			Integer spellId = spellLists.get(position);
			int costSteps = 0;
			if (skillRanks.containsKey(spellId)) {
				costSteps = skillRanks.get(spellId).intValue();
			}
			int f1 = getSpellListsDPFactor(position);
			int f2 = getSpellListsDPFactor(position - 1);
			if (f1 != f2) {
				// new factor (f2) is smaller than the old factor (f1)
				int costDPPos = 0;
				Skillcost costPos = sheet.getSkillcost(meta.getSkill(spellId));
				for (int i=0; i< costSteps; i++) {
					costDPPos += costPos.getCost(costSteps - 1);
				}
				int dp = costDPPos * (f1 - f2);
				if (log.isDebugEnabled()) log.debug("increase DP by "+dp +" for moving spell list "+spellId+" from position "+position+" one down");
				devPoints += dp;
			}
			// put skill ID to next lower position
			spellLists.set(position - 1, spellId);
		}
		// remove last position
		spellLists.remove(spellLists.size() - 1);
		sheet.firePropertyChange(PROPERTY_LVLUP_SPELLLISTS, null, getLvlUpSpellLists());
	}

	/**
	 * 
	 * @param category
	 * @param rank
	 * @throws LevelUpVetoException if skill rank operation is not allowed
	 */
	/* package private */ void modifySkillcategoryRank(SkillCategory category, BigDecimal rank) throws LevelUpVetoException {
		if (active) {
			Rank rankObj = sheet.getSkillcategoryRank(category);
			BigDecimal delta = rank.subtract(rankObj.getRank());
			/* get costs */
			Skillcost costs = sheet.getSkillcost(category);
			/* */
			int costSteps = 0;
			if (skillgroupRanks.containsKey(category.getId())) {
				costSteps = skillgroupRanks.get(category.getId()).intValue();
			}
			if (delta.doubleValue() > 0) {
				/* increase rank*/
				if (costSteps >= costs.size()) {
					setError(RESOURCE.getString("ui.levelup.error.skillcat.maxreached"));
					throw new LevelUpVetoException(RESOURCE.getString("ui.levelup.error.skillcat.maxreached"));
				}
				/* check DPs */
				int costDP = costs.getCost(costSteps);
				if (costDP <= devPoints) {
					skillgroupRanks.put(category.getId(), Integer.valueOf(costSteps + 1));
					devPoints -= costDP;
					if (log.isDebugEnabled()) log.debug("decrease DP by "+costDP);
					setLvlUpStatusText(MessageFormat.format(RESOURCE.getString("ui.levelup.msg.decrease"), Integer.valueOf(costDP), category.getName()));
					fireDevPointsUpdated();
				} else {
					setError(RESOURCE.getString("ui.levelup.error.skillcat.notenoughDP"));
					throw new LevelUpVetoException(RESOURCE.getString("ui.levelup.error.skillcat.notenoughDP"));
				}
			} else if (delta.doubleValue() < 0) {
				/* decrease rank */
				if (costSteps <= 0) {
					setError(RESOURCE.getString("ui.levelup.error.skillcat.minreached"));
					throw new LevelUpVetoException(RESOURCE.getString("ui.levelup.error.skillcat.minreached"));
				}
				int costDP = costs.getCost(costSteps - 1);
				devPoints += costDP;
				skillgroupRanks.put(category.getId(), Integer.valueOf(costSteps - 1));
				setLvlUpStatusText(MessageFormat.format(RESOURCE.getString("ui.levelup.msg.increase"), Integer.valueOf(costDP), category.getName()));
				if (log.isDebugEnabled()) log.debug("increase DP by "+costDP);
				fireDevPointsUpdated();
			}
		}
	}
	
	private void fireDevPointsUpdated() {
		if (active) {
			devPointsText = MessageFormat.format(RESOURCE.getString("ui.levelup.dpLeft"), Integer.valueOf(devPoints));
		} else {
			devPointsText = "";
		}
		sheet.firePropertyChange(PROPERTY_LVLUP_DEVPOINTS, null, devPointsText);
	}
	
	/**
	 * 
	 * @param skill not {@code null}
	 * @return 0-3
	 */
	public int getLevelUpSteps(ISkill skill) {
		if (skillRanks != null) {
			if (skillRanks.containsKey(skill.getId())) {
				return skillRanks.get(skill.getId()).intValue();
			}
		}
		return 0;
	}
	
	/**
	 * 
	 * @param category category not {@code null}
	 * @return 0-3
	 */
	public int getLevelUpSteps(SkillCategory category) {
		if (skillgroupRanks != null) {
			if (skillgroupRanks.containsKey(category.getId())) {
				return skillgroupRanks.get(category.getId()).intValue();
			}
		}
		return 0;
	}

	/**
	 * 
	 * @param skill
	 */
	public void removeSkill(ISkill skill) {
		if (active) {
			if (skillRanks.containsKey(skill.getId())) {
				int costSteps = skillRanks.get(skill.getId()).intValue();
				if (costSteps > 0) {
					/* get costs */
					Skillcost costs = sheet.getSkillcost(skill);
					int dps = 0;
					for (int i=costSteps; i>0; i--) {
						int costDP = costs.getCost(i - 1);
						if (skill.isSpelllist()) {
							costDP *= getSpellListsDPFactor(skill);
						}
						devPoints += costDP;
						dps += costDP;
					}
					if (skill.isSpelllist()) {
						/* remove the spell list with costs */
						int idx = spellLists.indexOf(skill.getId());
						moveSpelllists(idx);
					}
					skillRanks.put(skill.getId(), Integer.valueOf(0));
					if (log.isDebugEnabled()) log.debug("increase DP by "+dps);
					setLvlUpStatusText(MessageFormat.format(RESOURCE.getString("ui.levelup.msg.increase"), Integer.valueOf(dps), skill.getName()));
					fireDevPointsUpdated();
				}
			}
		}
	}

	/**
	 * 
	 * @param oldSkill the renamed skill
	 * @param newSkill the new custom skill
	 */
	public void modifySkill(ISkill oldSkill, ISkill newSkill) {
		if (active) {
			if (skillRanks.containsKey(oldSkill.getId())) {
				Integer ranks = skillRanks.remove(oldSkill.getId());
				skillRanks.put(newSkill.getId(), ranks);
			}
		}
	}

	
	public int getSpellLists() {
		return spellLists.size();
	}
	
	public String getLvlUpSpellLists() {
		if (spellLists.size() > 1) {
			int dpFactor = getSpellListsDPFactor(spellLists.size());
			if (dpFactor > 1) {
				String costFactor = "" + dpFactor;
				return MessageFormat.format(RESOURCE.getString("ui.levelup.spellranks"), "" + getSpellLists(), costFactor );
			}
		}
		return "";
	}
	
	private int getSpellListsDPFactor(ISkill skill) {
		if (!spellLists.contains(skill.getId())) {
			spellLists.add(skill.getId());
		}
		int position = spellLists.indexOf(skill.getId());
		return getSpellListsDPFactor(position);
	}

	private int getSpellListsDPFactor(int position) {
		int f = RMPreferences.getInstance().getSpelllistDPIncrease();
		if (f > 0 && position > 0) {
			f = (1 + (position / f ));
		} else {
			f = 1;
		}
		log.debug("spelllist factor "+f+" at position="+position);
		return f;
	}
}
