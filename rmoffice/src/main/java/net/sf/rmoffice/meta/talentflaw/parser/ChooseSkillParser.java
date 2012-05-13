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
package net.sf.rmoffice.meta.talentflaw.parser;

import java.util.ArrayList;
import java.util.List;

import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.MetaDataLoader;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.meta.talentflaw.ChooseSkillPart;

import org.apache.commons.lang.StringUtils;

public class ChooseSkillParser extends AbstractPatternParser<ChooseSkillPart> {

	private static final String PATTERN = "CHOOSESKILL[0-9]+=([C|S]{1}([0-9;]+))+=([0-9-]+|EVERYMAN|OCCUPATIONAL|RESTRICTED|RESTRICTED_IF_NOT_CHANNELING).*";
	private final MetaData metaData;
	
	public ChooseSkillParser(MetaData metaData) {
		super(PATTERN);
		this.metaData = metaData;
	}
	
	@Override
	public ChooseSkillPart parse(String parseableString) {
		String trimmed = StringUtils.trim(parseableString);
		String[] parts = StringUtils.splitPreserveAllTokens(trimmed.substring(11), "=");

		int amount = Integer.parseInt(parts[0]);
		int bonus = 0;
		SkillType type = null;
		try {
			bonus = Integer.parseInt(parts[2]);
		} catch (NumberFormatException e) {
			type = SkillType.valueOf(parts[2]);
		}
		// values
		List<SkillCategory> cats = new ArrayList<SkillCategory>();
		List<ISkill> skills = new ArrayList<ISkill>();
		String[] valueParts = StringUtils.splitPreserveAllTokens(parts[1], ";");
		for (String val : valueParts) {
			if (MetaDataLoader.CATEGORY_CHAR.equals(val.substring(0, 1))) {
				Integer catID = Integer.valueOf(val.substring(1));
				SkillCategory skillCat = metaData.getSkillCategory(catID);
				cats.add(skillCat);
			} else if (MetaDataLoader.SKILL_CHAR.equals(val.substring(0,1))) {
				Integer skillID = Integer.valueOf(val.substring(1));
				ISkill skill = metaData.getSkill(skillID);
				skills.add(skill);
			}
		}
		// follow-up action
		String followUpAction = "";
		if (parts.length > 3) {
			followUpAction = parts[3];
		}
		int followUpBonus = 0;
		SkillType followUpType = null;
		if (parts.length > 4) {
			try {
				followUpBonus = Integer.parseInt(parts[4]);
			} catch (NumberFormatException e) {
				followUpType = SkillType.valueOf(parts[4]);
			}
		}
		// creation
		if (type != null) {
			return createChooseSkillBonusPart(amount, type, cats, skills, followUpAction, followUpBonus, followUpType);
		} else {
			return createChooseBonusPart(amount, bonus, cats, skills, followUpAction, followUpBonus, followUpType);
		}
	}

	/* for test */ ChooseSkillPart createChooseBonusPart(int amount, int bonus, List<SkillCategory> cats, List<ISkill> skills, String followUpAction, int followUpBonus, SkillType followUpType) {
//		TODO collect skills		
		ChooseSkillPart part = new ChooseSkillPart(bonus, amount, skills, cats);
		part.setFollowup(followUpAction, followUpBonus, followUpType);
		return part;
	}
	
	/* for test */ ChooseSkillPart createChooseSkillBonusPart(int amount, SkillType type, List<SkillCategory> cats, List<ISkill> skills, String followUpAction, int followUpBonus, SkillType followUpType) {
//		TODO collect skills		
		ChooseSkillPart part = new ChooseSkillPart(type, amount, skills, cats);
		part.setFollowup(followUpAction, followUpBonus, followUpType);
		return part;
	}
	
}
