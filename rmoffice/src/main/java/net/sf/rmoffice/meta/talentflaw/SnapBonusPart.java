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
import net.sf.rmoffice.pdf.PDFCreator;

/**
 * Stores the snap bonus.
 */
public class SnapBonusPart implements ITalentFlawPart {
	public static final String ID = TalentFlawFactory.registerID("snapbonus");
	private final Integer snapBonus;

	public SnapBonusPart(int snapBonus) {
		this.snapBonus = Integer.valueOf(snapBonus);
	}
	
	@Override
	public String getId() {
		return ID;
	}
	
	@Override
	public void addToTalentFlaw(TalentFlawContext context, TalentFlaw talentFlaw) {
		talentFlaw.setSnapBonus(snapBonus);
	}

	@Override
	public String asText() {
		return PDFCreator.format(snapBonus.intValue(), false);
	}
}
