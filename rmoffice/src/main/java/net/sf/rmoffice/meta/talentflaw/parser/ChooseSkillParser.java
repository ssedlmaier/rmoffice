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
	protected ChooseSkillPart createPart(String key, String[] valueParts) {
		int amount = Integer.parseInt(key.substring(11));
		int bonus = 0;
		SkillType type = null;
		try {
			bonus = Integer.parseInt(valueParts[1]);
		} catch (NumberFormatException e) {
			type = SkillType.valueOf(valueParts[1]);
		}
		// values
		List<SkillCategory> cats = new ArrayList<SkillCategory>();
		List<ISkill> skills = new ArrayList<ISkill>();
		String[] singleValueParts = StringUtils.splitPreserveAllTokens(valueParts[0], ";");
		for (String val : singleValueParts) {
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
		if (valueParts.length > 2) {
			followUpAction = valueParts[2];
		}
		int followUpBonus = 0;
		SkillType followUpType = null;
		if (valueParts.length > 3) {
			try {
				followUpBonus = Integer.parseInt(valueParts[3]);
			} catch (NumberFormatException e) {
				followUpType = SkillType.valueOf(valueParts[3]);
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
