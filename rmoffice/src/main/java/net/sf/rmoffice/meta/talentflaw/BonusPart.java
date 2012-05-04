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
package net.sf.rmoffice.meta.talentflaw;

import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.SkillCategory;

/**
 * Provides a bonus for {@link ISkill} and {@link SkillCategory}.
 */
public class BonusPart extends AbstractTalentFlawPart {
	public static final String SKILLBONUS_ID = TalentFlawFactory.registerID("skillbonus");
	public static final String CATBONUS_ID = TalentFlawFactory.registerID("categorybonus");
	
	private final ISkill skill;
	private final SkillCategory skillCategory;
	private final Integer bonus;

	public BonusPart(ISkill skill, int bonus) {
		this.skill = skill;
		this.bonus = Integer.valueOf(bonus);
		this.skillCategory = null;
	}
	
	public BonusPart(SkillCategory skillCategory, int bonus) {
		this.skillCategory = skillCategory;
		this.bonus = Integer.valueOf(bonus);
		this.skill = null;
	}
	
	@Override
	public String getId() {
		return skillCategory == null ? SKILLBONUS_ID : CATBONUS_ID;
	}
	
	@Override
	public void addToTalentFlaw(TalentFlawContext context, TalentFlaw talentFlaw) {
		if (skillCategory != null) {
			talentFlaw.addSkillCatBonus(skillCategory, bonus);
		} else if (skill != null) {
			talentFlaw.addSkillBonus(skill, bonus);
		}
	}

	@Override
	public String asText() {
		StringBuilder sb = new StringBuilder();
		String name = skill != null ? skill.getName() : skillCategory.getName();
		appendBonusLine(sb, name, bonus);
		return sb.toString();
	}
}
