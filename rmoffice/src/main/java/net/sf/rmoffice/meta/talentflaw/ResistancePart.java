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
package net.sf.rmoffice.meta.talentflaw;


import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.enums.ResistanceEnum;

public class ResistancePart extends AbstractTalentFlawPart {
	public static final String ID = TalentFlawFactory.registerID("resistance");
	private final String descr;
	private final ResistanceEnum res;
	private final int bonus;
	
	public ResistancePart(String descr) {
		this.descr = descr;
		this.res = null;
		this.bonus = 0;
	}
	public ResistancePart(ResistanceEnum res, int bonus) {
		this.res = res;
		this.bonus = bonus;
		this.descr = null;
	}
	
	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void addToTalentFlaw(TalentFlawContext context, TalentFlaw talentFlaw) {
		if (descr != null) {
			talentFlaw.addAdditionalResistanceLine(descr);
		}
		if (res != null) {
			talentFlaw.setResistanceBonus(res, Integer.valueOf(this.bonus));
		}
	}

	@Override
	public String asText() {
		if (descr != null) {
			return descr;
		}
		StringBuilder sb = new StringBuilder();
		appendBonusLine(sb, res.toString(), Integer.valueOf(bonus));
		return sb.toString();
	}
}
