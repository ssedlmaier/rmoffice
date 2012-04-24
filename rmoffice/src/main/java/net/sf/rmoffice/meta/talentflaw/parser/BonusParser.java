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

import org.apache.commons.lang.StringUtils;

import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.MetaDataLoader;
import net.sf.rmoffice.meta.talentflaw.BonusPart;

public class BonusParser implements ITalentFlawPartParser<BonusPart> {
	private final static String BONUS_FORMAT = "[0-9]+=[-]{0,1}[0-9]+";
	private final MetaData metaData;
	private final Pattern patternSkill;
	private final Pattern patternSkillCat;

	public BonusParser(MetaData metaData) {
		this.metaData = metaData;
		patternSkill = Pattern.compile(MetaDataLoader.SKILL_CHAR+BONUS_FORMAT);
		patternSkillCat = Pattern.compile(MetaDataLoader.CATEGORY_CHAR+BONUS_FORMAT);
	}
	
	@Override
	public boolean isParseable(String toParse) {
		String trimmed = StringUtils.trimToEmpty(toParse);
		return patternSkill.matcher(trimmed).matches() || patternSkillCat.matcher(trimmed).matches();
	}

	@Override
	public BonusPart parse(String parseableString) {
		String trimmed = StringUtils.trimToEmpty(parseableString);
		String[] parts = StringUtils.splitPreserveAllTokens(trimmed.substring(1), "=");
		Integer id = Integer.valueOf(StringUtils.trim(parts[0]));
		int bonus = Integer.parseInt(StringUtils.trim(parts[1]));
		if (patternSkill.matcher(trimmed).matches()) {
			return new BonusPart(metaData.getSkill(id), bonus);
		} else {
			return new BonusPart(metaData.getSkillCategory(id), bonus);
		}
	}

}
