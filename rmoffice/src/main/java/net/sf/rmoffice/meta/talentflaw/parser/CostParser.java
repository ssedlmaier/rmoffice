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

import org.apache.commons.lang.StringUtils;

import net.sf.rmoffice.meta.INamed;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.MetaDataLoader;
import net.sf.rmoffice.meta.Skillcost;
import net.sf.rmoffice.meta.talentflaw.CostPart;

public class CostParser extends AbstractPatternParser<CostPart> {
    private static final String PATTERN = "COST=([C|S]{1}[0-9]+)*=[0-9]+/[0-9]+/[0-9]+";
	private final MetaData meta;
	
	public CostParser(MetaData meta) {
		super(PATTERN);
		this.meta = meta;
	}
	
	@Override
	protected CostPart createPart(String key, String[] valueParts) {
		boolean isSkill = valueParts[0].startsWith(MetaDataLoader.SKILL_CHAR);
		Integer id = Integer.valueOf(valueParts[0].substring(1));
		String[] costTokens = StringUtils.splitPreserveAllTokens(valueParts[1], "/");
		int[] cost = new int[costTokens.length];
		for (int i=0; i<costTokens.length; i++) {
			cost[i] = Integer.parseInt(StringUtils.trim(costTokens[i]));
		}
		INamed named = null;
		if (isSkill) {
			named = meta.getSkill(id);
			if (named == null) {
				throw new IllegalArgumentException("skill id "+id+" is not available");
			}
		} else {
			named = meta.getSkillCategory(id);
			if (named == null) {
				throw new IllegalArgumentException("skill category id "+id+" is not available");
			}
		}
		return new CostPart(named, isSkill, new Skillcost(cost));
	}

}
