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
import java.util.regex.Pattern;

import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.MetaDataLoader;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.meta.talentflaw.ChoosePart;

import org.apache.commons.lang.StringUtils;

public class ChooseParser implements ITalentFlawPartParser<ChoosePart> {

	private static final String PATTERN = "CHOOSE[0-9]+=[CS0-9;]+=([0-9-]+|EVERYMAN|OCCUPATIONAL|RESTRICTED|RESTRICTED_IF_NOT_CHANNELING)";
	private final MetaData metaData;
	private final Pattern pattern;

	public ChooseParser(MetaData metaData) {
		this.metaData = metaData;
		pattern = Pattern.compile(PATTERN);
	}
	
	@Override
	public boolean isParseable(String toParse) {
		if (StringUtils.isEmpty(toParse)) {
			return false;
		}
		String trimmed = StringUtils.trim(toParse);
		return pattern.matcher(trimmed).matches();
	}

	@Override
	public ChoosePart parse(String parseableString) {
		String trimmed = StringUtils.trim(parseableString);
		String[] parts = StringUtils.splitPreserveAllTokens(trimmed.substring(6), "=");

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
		if (type != null) {
			return createChooseBonusPart(amount, type, cats, skills);
		} else {
			return createChooseBonusPart(amount, bonus, cats, skills);
		}
	}

	/* for test */ ChoosePart createChooseBonusPart(int amount, int bonus, List<SkillCategory> cats, List<ISkill> skills) {
		if (cats.size() > 0) {
			return new ChoosePart(bonus, amount, false, cats.toArray());
		} else {
			return new ChoosePart(bonus, amount, true, skills.toArray());
		}
	}
	
	/* for test */ ChoosePart createChooseBonusPart(int amount, SkillType type, List<SkillCategory> cats, List<ISkill> skills) {
		if (cats.size() > 0) {
			return new ChoosePart(type, amount, false, cats.toArray());
		} else {
			return new ChoosePart(type, amount, true, skills.toArray());
		}
	}
	
}
