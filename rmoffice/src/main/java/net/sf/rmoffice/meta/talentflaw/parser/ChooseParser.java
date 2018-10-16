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
package net.sf.rmoffice.meta.talentflaw.parser;

import java.util.ArrayList;
import java.util.List;

import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.MetaDataLoader;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.meta.talentflaw.ChoosePart;

import org.apache.commons.lang.StringUtils;

public class ChooseParser extends AbstractPatternParser<ChoosePart> {

	private static final String ALLSKILL = "ALLSKILL";
	private static final String ALLSPELLLIST = "ALLSPELLLIST";
	private static final String PATTERN = "CHOOSE[0-9]+=(([C|S]{1}[;0-9]+)*|"+ALLSKILL+"|"+ALLSPELLLIST+")=([0-9-]+|EVERYMAN|OCCUPATIONAL|RESTRICTED|RESTRICTED_IF_NOT_CHANNELING)";
	private final MetaData metaData;

	public ChooseParser(MetaData metaData) {
		super(PATTERN);
		this.metaData = metaData;
	}
	
	@Override
	protected ChoosePart createPart(String key, String[] valParts) {
		int amount = Integer.parseInt(key.substring(6));
		int bonus = 0;
		SkillType type = null;
		try {
			bonus = Integer.parseInt(valParts[1]);
		} catch (NumberFormatException e) {
			type = SkillType.valueOf(valParts[1]);
		}
		// values
		List<SkillCategory> cats = new ArrayList<SkillCategory>();
		List<ISkill> skills = new ArrayList<ISkill>();
		String[] valueParts = StringUtils.splitPreserveAllTokens(valParts[0], ";");
		boolean showAllSkills = false;
		boolean showAllSpelllists = false;
		for (String val : valueParts) {
			if (MetaDataLoader.CATEGORY_CHAR.equals(val.substring(0, 1))) {
				Integer catID = Integer.valueOf(val.substring(1));
				SkillCategory skillCat = metaData.getSkillCategory(catID);
				cats.add(skillCat);
			} else if (MetaDataLoader.SKILL_CHAR.equals(val.substring(0,1))) {
				Integer skillID = Integer.valueOf(val.substring(1));
				ISkill skill = metaData.getSkill(skillID);
				skills.add(skill);
			} else if (ALLSKILL.equals(val)) {
				showAllSkills = true;
			} else if (ALLSPELLLIST.equals(val)) {
				showAllSpelllists = true;
			}
		}
		if (type != null) {
			return createChooseBonusPart(amount, type, cats, skills, showAllSkills, showAllSpelllists);
		} else {
			return createChooseBonusPart(amount, bonus, cats, skills, showAllSkills, showAllSpelllists);
		}
	}

	/* for test */ ChoosePart createChooseBonusPart(int amount, int bonus, List<SkillCategory> cats, List<ISkill> skills, boolean showAllSkills, boolean showAllSpelllists) {
		if (cats.size() > 0) {
			return new ChoosePart(bonus, amount, false, false, false, cats.toArray());
		} else {
			return new ChoosePart(bonus, amount, true, showAllSkills, showAllSpelllists, skills.toArray());
		}
	}
	
	/* for test */ ChoosePart createChooseBonusPart(int amount, SkillType type, List<SkillCategory> cats, List<ISkill> skills, boolean showAllSkills, boolean showAllSpelllists) {
		if (cats.size() > 0) {
			return new ChoosePart(type, amount, false, false, false, cats.toArray());
		} else {
			return new ChoosePart(type, amount, true, showAllSkills, showAllSpelllists, skills.toArray());
		}
	}
	
}
