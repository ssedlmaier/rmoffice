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
import net.sf.rmoffice.meta.Progression;
import net.sf.rmoffice.pdf.PDFCreator;

public class ProgressionPart extends AbstractTalentFlawPart {
	public static final String BODY_ID = TalentFlawFactory.registerID("bodydev");
	public static final String POWER_ID = TalentFlawFactory.registerID("bodydev");

	private final int[] progModifier;
	private final boolean bodyOrPower;
	
	public ProgressionPart(int[] progModifier, boolean bodyOrPower) {
		this.progModifier = progModifier;
		this.bodyOrPower = bodyOrPower;
	}
	
	@Override
	public String getId() {
		return bodyOrPower ? BODY_ID : POWER_ID;
	}

	@Override
	public void addToTalentFlaw(TalentFlawContext context, TalentFlaw talentFlaw) {
		if (bodyOrPower) {
			talentFlaw.setProgressionBody(new Progression(progModifier[0], progModifier[1], progModifier[2], progModifier[3], progModifier[4]));
		} else {
			talentFlaw.setProgressionPower(new Progression(progModifier[0], progModifier[1], progModifier[2], progModifier[3], progModifier[4]));
		}
	}

	@Override
	public String asText() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < progModifier.length; i++) {
			if (i > 0) {
				sb.append(" / ");
			}
			sb.append(PDFCreator.format(progModifier[i], false));
		}
		return sb.toString();
	}

}
