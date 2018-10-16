/*
 * Copyright 2013 Daniel Nettesheim
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

/**
 * Absolute soul depature value.
 */
public class SouldeparturePart implements ITalentFlawPart {
	public static final String ID = TalentFlawFactory.registerID("souldeparture");
	private final Integer souldeparture;

	public SouldeparturePart(int souldeparture) {
		this.souldeparture = Integer.valueOf(souldeparture);
	}
	
	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void addToTalentFlaw(TalentFlawContext context, TalentFlaw talentFlaw) {
		talentFlaw.setSouldeparture(souldeparture);
	}

	@Override
	public String asText() {
		return Integer.toString(souldeparture.intValue());
	}

}
