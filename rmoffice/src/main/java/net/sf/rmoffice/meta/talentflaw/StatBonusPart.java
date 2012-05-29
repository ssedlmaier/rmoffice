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
import net.sf.rmoffice.meta.enums.StatEnum;

public class StatBonusPart extends KeyValuePart {
	public static final String ID = TalentFlawFactory.registerID("stat");
	private final StatEnum stat;
	
	public StatBonusPart(StatEnum stat, float value) {
		super(value);
		this.stat = stat;
	}


	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void addToTalentFlaw(TalentFlawContext context, TalentFlaw talentFlaw) {
		talentFlaw.addStatBonus(stat, Integer.valueOf(Math.round(value)));
	}

	@Override
	public String asText() {
		return RESOURCE.getString("StatEnum."+stat.name()+".long") + "=" + format(value);
	}

}
