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
package net.sf.rmoffice.meta.talentflaw;

import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.enums.SkillType;

/**
 * Provides a bonus for {@link ISkill} and {@link SkillCategory}.
 */
public class SkillTypePart extends AbstractTalentFlawPart {
	public static final String SKILLTYPE_ID = TalentFlawFactory.registerID("skilltype");
	public static final String CATTYPE_ID = TalentFlawFactory.registerID("categorytype");

	private final ISkill skill;
	private final SkillCategory skillCategory;
	private final SkillType skillType;

	public SkillTypePart(ISkill skill, SkillType bonus) {
		this.skill = skill;
		this.skillType = bonus;
		this.skillCategory = null;
	}
	
	public SkillTypePart(SkillCategory skillCategory, SkillType bonus) {
		this.skillCategory = skillCategory;
		this.skillType = bonus;
		this.skill = null;
	}
	
	@Override
	public String getId() {
		return skillCategory != null ? CATTYPE_ID : SKILLTYPE_ID;
	}
	
	@Override
	public void addToTalentFlaw(TalentFlawContext context, TalentFlaw talentFlaw) {
		if (skillCategory != null) {
			talentFlaw.addSkillCatType(skillCategory, skillType);
		} else if (skill != null) {
			talentFlaw.addSkillType(skill, skillType);
		}
	}

	@Override
	public String asText() {
		StringBuilder sb = new StringBuilder();
		String name = skill != null ? skill.getName() : skillCategory.getName();
		appendTypeLine(sb, name , skillType);
		return sb.toString();
	}	
}
