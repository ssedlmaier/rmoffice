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

import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.meta.talentflaw.StatBonusPart;

/**
 * Not thread safe.
 */
public class StatBonusParser extends AbstractKeyFloatValueParser<StatBonusPart> {
	private static final String KEYPATTERN = "("+StatEnum.AGILITY.name() + "|" +
			StatEnum.CONSTITUTION.name() + "|" + 
			StatEnum.EMPATHY.name() + "|" + 
			StatEnum.INTUITION.name() + "|" + 
			StatEnum.MEMORY.name() + "|" + 
			StatEnum.PRESENCE.name() + "|" + 
			StatEnum.QUICKNESS.name() + "|" + 
			StatEnum.REASONING.name() + "|" + 
			StatEnum.SELFDISCIPLINE.name() + "|" + 
			StatEnum.STRENGTH.name()+")" 
			;
	private StatEnum lastKey;


	public StatBonusParser() {
		super(KEYPATTERN);
	}

	@Override
	protected StatBonusPart createPart(float value) {
		return new StatBonusPart(lastKey, value);
	}

	@Override
	protected void processKey(String key) {
		lastKey = StatEnum.valueOf(key);
	}
}
