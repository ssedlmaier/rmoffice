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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.rmoffice.meta.IProgression;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.enums.LengthUnit;
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.meta.enums.TalentFlawLevel;
import net.sf.rmoffice.meta.enums.TalentFlawTransform;
import net.sf.rmoffice.meta.enums.TalentFlawType;

import com.jgoodies.binding.beans.Model;

/**
 * Sheet data: Talent or flaw with one level of the talent.
 */
public class TalentFlaw extends Model {
	private static final long serialVersionUID = 1L;
	private static final String ID_PROP = "id";
	private static final String NAME_PROP = "name";
	
	private Integer id;
	private String name;
	private String source;
	private TalentFlawType type;
	private TalentFlawLevel level;
	private int costs;
	private Integer initiativeBonus;
	private List<String> descriptions;
	private Map<Integer, Integer> skillCatBonus;
	private Map<Integer, SkillType> skillCatType;
	private Map<Integer, Integer> skillBonus;
	private Map<Integer, SkillType> skillType;
	/* Example: open-arcane is now open-own-realm */
	private Map<Integer, TalentFlawTransform> transformedSkillCategories;
    private IProgression progressionBody;
    private IProgression progressionPower;
	private Float weightPenalty; 
	private Float baseMovement; 
	private Integer fumbleRange;
	private List<String> resistanceLines;
    private Integer db;
    private Integer shieldDb;
	private Integer exhaustion;
	private Map<StatEnum, Integer> statBonus;
	private Float recoveryMultiplier;
	private Float tolerance;
	
	public TalentFlaw() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		Object oldValue = this.id;
		this.id = id;
		firePropertyChange(ID_PROP, oldValue, this.id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		Object oldValue = this.name;
		this.name = name;
		firePropertyChange(NAME_PROP, oldValue, this.name);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public TalentFlawType getType() {
		return type;
	}

	public void setType(TalentFlawType type) {
		this.type = type;
	}

	public TalentFlawLevel getLevel() {
		return level;
	}

	public void setLevel(TalentFlawLevel level) {
		this.level = level;
	}

	public int getCosts() {
		return costs;
	}

	public void setCosts(int costs) {
		this.costs = costs;
	}

	public Integer getInitiativeBonus() {
		return initiativeBonus;
	}

	public void setInitiativeBonus(Integer initiativeBonus) {
		this.initiativeBonus = initiativeBonus;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}
	
	public void addDescription(String description) {
		if (descriptions == null) {
			descriptions = new ArrayList<String>();
		}
		descriptions.add(description);
	}

	public Map<Integer, Integer> getSkillCatBonus() {
		return skillCatBonus;
	}

	public void addSkillCatBonus(SkillCategory category, Integer value) {
		if (skillCatBonus == null) {
			skillCatBonus = new HashMap<Integer, Integer>();
		}
		if (category != null && value != null) {
			skillCatBonus.put(category.getId(), value);
		}
	}

	public Map<Integer, SkillType> getSkillCatType() {
		return skillCatType;
	}

	public void addSkillCatType(SkillCategory category, SkillType value) {
		if (skillCatType == null) {
			skillCatType = new HashMap<Integer, SkillType>();
		}
		if (category != null && value != null) {
			skillCatType.put(category.getId(), value);
		}
	}

	/**
	 * Returns the skill bonus map with all bonus.
	 * 
	 * @return the skill bonus map or {@code null}
	 */
	public Map<Integer, Integer> getSkillBonus() {
		return skillBonus;
	}

	public void addSkillBonus(ISkill skill, Integer rank) {
		if (skillBonus == null) {
			skillBonus = new HashMap<Integer, Integer>();
		}
		if (skill != null && rank != null) {
			skillBonus.put(skill.getId(), rank);
		}
	}

	public Map<Integer, SkillType> getSkillType() {
		return skillType;
	}


	public void addSkillType(ISkill skill, SkillType value) {
		if (skillType == null) {
			skillType = new HashMap<Integer, SkillType>();
		}
		if (skill != null && value != null) {
			skillType.put(skill.getId(), value);
		}
	}

	public Map<Integer, TalentFlawTransform> getTransformedSkillCategories() {
		return transformedSkillCategories;
	}

	public void setTransformedSkillCategories(
			Map<Integer, TalentFlawTransform> transformedSkillCategories) {
		this.transformedSkillCategories = transformedSkillCategories;
	}

	public IProgression getProgressionBody() {
		return progressionBody;
	}

	public void setProgressionBody(IProgression progressionBody) {
		this.progressionBody = progressionBody;
	}

	public IProgression getProgressionPower() {
		return progressionPower;
	}

	public void setProgressionPower(IProgression progressionPower) {
		this.progressionPower = progressionPower;
	}

	public Float getWeightPenalty() {
		return weightPenalty;
	}

	public void setWeightPenalty(Float weightPenalty) {
		this.weightPenalty = weightPenalty;
	}

	/**
	 * Gets the base movement rate in {@link LengthUnit#CM}.
	 * 
	 * @return the base movement rate modifier or {@code null}
	 */
	public Float getBaseMovement() {
		return baseMovement;
	}

	/**
	 * Sets the base movement rate in {@link LengthUnit#CM}.
	 * 
	 * @param baseMovement the base movement rate modifier or {@code null} to reset
	 */
	public void setBaseMovement(Float baseMovement) {
		this.baseMovement = baseMovement;
	}

	/**
	 * Returns the fumble range modifier or {@code null}.
	 * 
	 * @return funble range modifier.
	 */
	public Integer getFumbleRange() {
		return fumbleRange;
	}

	/**
	 * Sets the fumble range modifier.
	 * 
	 * @param fumbleRange the funble range
	 */
	public void setFumbleRange(Integer fumbleRange) {
		this.fumbleRange = fumbleRange;
	}

	/**
	 * Adds an additional resistance line on sheet.
	 * 
	 * @param descr localized rr line, not {@code null} 
	 */
	public void addAdditionalResistanceLine(String descr) {
		if (resistanceLines == null) {
			resistanceLines = new ArrayList<String>();
		}
		resistanceLines.add(descr);
	}

	/**
	 * Returns an unmodifiable list of resistance lines.
	 * 
	 * @return list of rr lines or {@code null}
	 */
	public List<String> getAdditionalResistanceLine() {
		if (resistanceLines == null) {
			return null;
		}
		return Collections.unmodifiableList(resistanceLines);
	}

	/**
	 * Returns the defensive bonus.
	 * 
	 * @return bonus or {@code null}
	 */
	public Integer getDb() {
		return db;
	}

	/**
	 * Sets the defensive bonus.
	 * 
	 * @param db bonus
	 */
	public void setDb(Integer db) {
		this.db = db;
	}

	/**
	 * Returns the shield db or {@code null}.
	 * 
	 * @return the shield db or {@code null}
	 */
	public Integer getShieldDb() {
		return shieldDb;
	}

	/**
	 * Sets the shield db
	 * 
	 * @param shieldDb the shield db, may be {@code null}
	 */
	public void setShieldDb(Integer shieldDb) {
		this.shieldDb = shieldDb;
	}

	public void setExhaustion(Integer exhaustion) {
		this.exhaustion = exhaustion;
	}
	
	public Integer getExhaustion() {
		return exhaustion;
	}

	public void addStatBonus(StatEnum stat, Integer bonus) {
		if (statBonus == null) {
			statBonus = new HashMap<StatEnum, Integer>();
		}
		statBonus.put(stat, bonus);
	}
	
	/**
	 * Returns the bonus or {@code null} if no bonus is available. 
	 * 
	 * @param stat the stat, may be {@code null}
	 * @return the bonus or {@code null}
	 */
	public Integer getStatBonus(StatEnum stat) {
		if (statBonus == null || stat == null) {
			return null;
		}
		return statBonus.get(stat);
	}

	public void setRecoveryMultiplier(Float recoveryMultiplier) {
		this.recoveryMultiplier = recoveryMultiplier;
	}
	
	public Float getRecoveryMultiplier() {
		return recoveryMultiplier;
	}

	/**
	 * Sets the tolerance factor.
	 * 
	 * @param tolerance the factor
	 */
	public void setTolerance(Float tolerance) {
		this.tolerance = tolerance;
	}
	
	/**
	 * Returns the tolerance factor or {@code null}.
	 * 
	 * @return the tolerance factor
	 */
	public Float getTolerance() {
		return tolerance;
	}
}
