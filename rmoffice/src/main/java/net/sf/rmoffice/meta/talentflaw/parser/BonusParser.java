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

import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.MetaDataLoader;
import net.sf.rmoffice.meta.talentflaw.BonusPart;

public class BonusParser extends AbstractPatternParser<BonusPart> {
	private final static String BONUS_FORMAT = "[0-9]+=[-]{0,1}[0-9]+";
	private final MetaData metaData;

	public BonusParser(MetaData metaData) {
		super(MetaDataLoader.SKILL_CHAR+BONUS_FORMAT, MetaDataLoader.CATEGORY_CHAR+BONUS_FORMAT);
		this.metaData = metaData;
	}
	
	@Override
	protected BonusPart createPart(String key, String[] valueParts) {
		Integer id = Integer.valueOf(key.substring(1));
		int bonus = Integer.parseInt(valueParts[0]);
		if (key.startsWith(MetaDataLoader.SKILL_CHAR)) {
			return new BonusPart(metaData.getSkill(id), bonus);
		} else {
			return new BonusPart(metaData.getSkillCategory(id), bonus);
		}
	}

}
