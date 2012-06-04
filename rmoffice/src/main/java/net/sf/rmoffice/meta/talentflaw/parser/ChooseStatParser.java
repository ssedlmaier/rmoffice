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
package net.sf.rmoffice.meta.talentflaw.parser;

import net.sf.rmoffice.meta.talentflaw.ChooseStatPart;

public class ChooseStatParser extends AbstractPatternParser<ChooseStatPart> {
	private static final String STAT = "STAT";
	private static final String PATTERN = STAT + "([0-9]+)=([0-9]+)";

	public ChooseStatParser() {
		super(PATTERN);
	}
	
	@Override
	protected ChooseStatPart createPart(String key, String[] valueParts) {
		int amount = Integer.parseInt(key.substring(STAT.length()));
		int bonus = Integer.parseInt(valueParts[0]);
		return createPart(amount, bonus);
	}

	protected ChooseStatPart createPart(int amount, int bonus) {
		return new ChooseStatPart(amount, bonus);
	}

}
