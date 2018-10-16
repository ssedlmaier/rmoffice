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
package net.sf.rmoffice.meta;

import java.util.ArrayList;
import java.util.List;

import net.sf.rmoffice.meta.enums.SkillType;

/**
 * Stores the possible values to choose and additional information about the effect.
 */
public class TalentFlawChoice {

	private int amount;
	private Integer bonus;
	private SkillType skillType; 
	private List<Object> values = new ArrayList<Object>();

	/* package */ void setAmount(int amount) {
		this.amount = amount;
	}
	
	/**
	 * Returns the amount of possible choices.
	 * 
	 * @return the amount of choices
	 */
    public int getAmount() {
		return amount;
	}

	/* package */ void addValue(Object value) {
		values.add(value);
	}
	
	/**
	 * Returns all possible values.
	 * @return list of possible values, not {@code null}
	 */
	public List<Object> getValues() {
		return values;
	}

	/**
	 * Returns whether this choice results in a {@link SkillType} change or not.
	 * 
	 * @return whether this choice results in a {@link SkillType} change or not.
	 */
	public boolean isSkillTypeChoice() {
		return skillType != null;
	}
	
	/* package */ void setSkillType(SkillType skillType) {
		this.skillType = skillType;
	}

	/**
	 * Returns the {@link SkillType} if {@link #isSkillTypeChoice()} is true otherwise it returns {@code null}.
	 * 
	 * @return the {@link SkillType} or {@code null}
	 */
	public SkillType getSkillType() {
		return skillType;
	}

	/**
	 * Returns whether this choice results in a bonus or not.
	 * 
	 * @return whether this choice result in a bonus or not
	 */
	public boolean isBonusChoice() {
		return bonus != null;
	}
	
	/* package */ void setBonus(Integer bonus) {
		this.bonus = bonus;
	}

	/**
	 * Returns the bonus if {@link #isBonusChoice()} is true or {@code null}.
	 * 
	 * @return the bonus or {@code null}
	 */
	public Integer getBonus() {
		return bonus;
	}
}
