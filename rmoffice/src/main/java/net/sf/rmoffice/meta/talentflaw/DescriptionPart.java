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

import java.awt.Frame;

import net.sf.rmoffice.core.TalentFlaw;

/**
 * Stores one description to be added to the TalentFlaw.
 */
public class DescriptionPart implements ITalentFlawPart {
	public static final String ID = TalentFlawFactory.registerID("descr");
	
    private final String description;
	
    public DescriptionPart(String description) {
		this.description = description;
	}
    
    @Override
    public String getId() {
    	return ID;
    }
    
	@Override
	public void addToTalentFlaw(Frame owner, TalentFlaw talentFlaw) {
		talentFlaw.addDescription(description);
	}

	@Override
	public String asText() {
		return description;
	}
}
