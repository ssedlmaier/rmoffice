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

import static org.junit.Assert.*;

import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.meta.talentflaw.StatBonusPart;

import org.junit.Test;

public class StatBonusParserTest {

	@Test
	public void testParseable() {
		StatBonusParser parser = new StatBonusParser();
		for (StatEnum stat : StatEnum.values()) {
			parser.isParseable(stat.name() + "=5");
			parser.isParseable(stat.name() + "=-5");
		}
	}
	
	@Test
	public void testParse() {
		StatBonusParser parser = new StatBonusParser();
		int i=-4;
		for (StatEnum stat : StatEnum.values()) {
			String s = "   " + stat.name() + "=" + i + "  ";
			StatBonusPart part = parser.parse(s);
			TalentFlaw talentFlaw = new TalentFlaw();
			part.addToTalentFlaw(null, talentFlaw);
			assertNotNull(talentFlaw.getStatBonus(stat));
			assertEquals(i, talentFlaw.getStatBonus(stat).intValue());
			i++;
		}
	}

}
