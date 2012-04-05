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
package net.sf.rmoffice.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.meta.enums.TalentFlawLevel;
import net.sf.rmoffice.meta.enums.TalentFlawTransform;

/**
 * Meta data: One parameter value of a {@link TalentFlawPreset} from configuration
 * file.
 */
public class TalentFlawPresetValue {
	private TalentFlawLevel level;
	private int costs;
	private Integer initiativeBonus;
	private List<String> descriptions;
	private Map<SkillCategory, Integer> skillCatBonus;
	private Map<SkillCategory, SkillType> skillCatType;
	private Map<ISkill, Integer> skillBonus;
	private Map<ISkill, SkillType> skillType;
	private Map<TalentFlawTransform, TalentFlawTransform> transformations;

	public TalentFlawLevel getLevel() {
		return level;
	}

	/* package */ void setLevel(TalentFlawLevel level) {
		this.level = level;
	}

	public int getCosts() {
		return costs;
	}

	/* package */ void setCosts(int costs) {
		this.costs = costs;
	}

	public Integer getInitiativeBonus() {
		return initiativeBonus;
	}

	/* package */ void setInitiativeBonus(Integer initiativeBonus) {
		this.initiativeBonus = initiativeBonus;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	/* package */ void addDescriptions(String description) {
		if (this.descriptions == null) {
			this.descriptions = new ArrayList<String>();
		}
		this.descriptions.add(description);
	}

	public Map<SkillCategory, Integer> getSkillCatBonus() {
		return skillCatBonus;
	}

	/* package */ void putSkillCatBonus(SkillCategory skillCat, Integer skillCatBonus) {
		if (this.skillCatBonus == null) {
			this.skillCatBonus = new HashMap<SkillCategory, Integer>();
		}
		this.skillCatBonus.put(skillCat, skillCatBonus);
	}

	public Map<SkillCategory, SkillType> getSkillCatType() {
		return skillCatType;
	}

	/* package */ void putSkillCatType(SkillCategory skillCat, SkillType skillCatType) {
		if (this.skillCatType == null) {
			this.skillCatType = new HashMap<SkillCategory, SkillType>();
		}
		this.skillCatType.put(skillCat, skillCatType);
	}

	public Map<ISkill, Integer> getSkillBonus() {
		return skillBonus;
	}

	/* package */ void putSkillBonus(ISkill skill, Integer skillBonus) {
		if (this.skillBonus == null) {
			this.skillBonus = new HashMap<ISkill, Integer>();
		}
		this.skillBonus.put(skill, skillBonus);
	}

	public Map<ISkill, SkillType> getSkillType() {
		return skillType;
	}

	/* package */ void putSkillType(ISkill skill, SkillType skillType) {
		if (this.skillType == null) {
			this.skillType = new HashMap<ISkill, SkillType>();
		}
		this.skillType.put(skill, skillType);
	}

	public Map<TalentFlawTransform, TalentFlawTransform> getTransformations() {
		return transformations;
	}

	/* package */ void putTransformations(
			TalentFlawTransform from, TalentFlawTransform to) {
		if (this.transformations == null) {
			this.transformations = new HashMap<TalentFlawTransform, TalentFlawTransform>();
		}
		this.transformations.put(from, to);
	}

}
