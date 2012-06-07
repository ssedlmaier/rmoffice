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
package net.sf.rmoffice.meta.talentflaw;

import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.INamed;
import net.sf.rmoffice.meta.Skillcost;

public class CostPart extends AbstractTalentFlawPart {
	private static final String ID = TalentFlawFactory.registerID("cost");
	
	private final Skillcost costs;
	private final INamed namedObject;

	private final boolean isSkill;
	
	public CostPart(INamed id, boolean isSkill, Skillcost newCosts) {
		this.namedObject = id;
		this.isSkill = isSkill;
		this.costs = newCosts;
	}
	
	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void addToTalentFlaw(TalentFlawContext context, TalentFlaw talentFlaw) {
		if (isSkill) {
			talentFlaw.setSkillCostReplacement(namedObject.getId(), costs);
		} else {
			talentFlaw.setSkillCategoryCostReplacement(namedObject.getId(), costs);
		}
	}

	@Override
	public String asText() {
		StringBuilder sb = new StringBuilder();
		sb.append(namedObject.getName());
		sb.append(" ");
		sb.append(costs.toString());
		return sb.toString();
	}

}
