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

import net.sf.rmoffice.meta.enums.TalentFlawSkillCategoryType;
import net.sf.rmoffice.meta.talentflaw.RankPart;

import org.apache.commons.lang.StringUtils;

public class RankParser extends AbstractPatternParser<RankPart> {
	private static final String PATTERN = "^RANK[0-9]+=(BASELIST|OPENLIST|CLOSEDLIST|OPENARCANELIST|CLOSEDARCANELIST|LANGUAGE|;)+$";
	
	public RankParser() {
		super(PATTERN);
	}

	@Override
	protected RankPart createPart(String key, String[] valueParts) {
		int amount = Integer.parseInt(key.substring(4));
		List<TalentFlawSkillCategoryType> types = new ArrayList<TalentFlawSkillCategoryType>();
		String[] catTypeStrings = StringUtils.split(valueParts[0], ';');
		for (String cts : catTypeStrings) {
			types.add(TalentFlawSkillCategoryType.valueOf(cts));
		}
		return createRankPart(amount, types);
	}

	protected RankPart createRankPart(int amount, List<TalentFlawSkillCategoryType> types) {
		return new RankPart(amount, types);
	}

}
