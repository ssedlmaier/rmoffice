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

import java.util.regex.Pattern;

import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.MetaDataLoader;
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.meta.talentflaw.SkillTypePart;

import org.apache.commons.lang.StringUtils;

public class SkillTypeParser implements ITalentFlawPartParser<SkillTypePart> {
	private final static String BONUS_FORMAT = "[0-9]+=[A-Z_]+";
	private final MetaData metaData;
	private final Pattern patternSkill;
	private final Pattern patternSKillCat;

	public SkillTypeParser(MetaData metaData) {
		this.metaData = metaData;
		patternSkill = Pattern.compile(MetaDataLoader.SKILL_CHAR+BONUS_FORMAT);
		patternSKillCat = Pattern.compile(MetaDataLoader.CATEGORY_CHAR+BONUS_FORMAT);
	}
	
	@Override
	public boolean isParseable(String toParse) {
		String trimmed = StringUtils.trimToEmpty(toParse);
		return patternSkill.matcher(trimmed).matches() || patternSKillCat.matcher(trimmed).matches();
	}

	@Override
	public SkillTypePart parse(String parseableString) {
		String trimmed = StringUtils.trimToEmpty(parseableString);
		String[] parts = StringUtils.splitPreserveAllTokens(trimmed.substring(1), "=");
		Integer id = Integer.valueOf(StringUtils.trim(parts[0]));
		SkillType skillType = SkillType.valueOf(StringUtils.trim(parts[1]));
		if (patternSkill.matcher(trimmed).matches()) {
			return new SkillTypePart(metaData.getSkill(id), skillType);
		} else {
			return new SkillTypePart(metaData.getSkillCategory(id), skillType);
		}
	}

}
