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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.meta.talentflaw.ChooseStatPart;
import net.sf.rmoffice.meta.talentflaw.TalentFlawContext;

import org.junit.Test;

public class ChooseStatParserTest {

	@Test
	public void testIsParseable() {
		ChooseStatParser parser = new ChooseStatParser();
		assertFalse(parser.isParseable(null));
		assertFalse(parser.isParseable(""));
		assertFalse(parser.isParseable(" "));
		assertFalse(parser.isParseable("  shdgfdhs"));

		assertTrue(parser.isParseable("STAT1=5"));
		assertTrue(parser.isParseable(" STAT2=10"));
		assertTrue(parser.isParseable(" STAT10=55 "));
		assertTrue(parser.isParseable("STAT1=-5"));
		assertTrue(parser.isParseable(" STAT2=-10"));
		assertTrue(parser.isParseable(" STAT10=-55 "));
	}
	
	@Test
	public void testParse() {
		assertStatBonus("STAT1=5", 5, StatEnum.STRENGTH);
		assertStatBonus("STAT2=10", 10, StatEnum.STRENGTH, StatEnum.QUICKNESS);
		assertStatBonus("  STAT1=5  ", 5, StatEnum.STRENGTH);
		assertStatBonus("  STAT1=5", 5, StatEnum.STRENGTH);
		assertStatBonus("  STAT1=  5", 5, StatEnum.STRENGTH);
		assertStatBonus("STAT1=-5", -5, StatEnum.STRENGTH);
		assertStatBonus("STAT2=-10", -10, StatEnum.STRENGTH, StatEnum.QUICKNESS);
		assertStatBonus("  STAT1=-5  ", -5, StatEnum.STRENGTH);
		assertStatBonus("  STAT1=-5", -5, StatEnum.STRENGTH);
		assertStatBonus("  STAT1=  -5", -5, StatEnum.STRENGTH);
	}

	private void assertStatBonus(String parseableString, final int expectedBonus, final StatEnum... expectedStats) {
		ChooseStatParser parser = new ChooseStatParser() {
			@Override
			protected ChooseStatPart createPart(int amount, int bonus) {
				UTChooseStatPart part = new UTChooseStatPart(amount, bonus);
				part.returnValue.clear();
				for (StatEnum stat : expectedStats) {
					part.returnValue.add(stat);
				}
				return part;
			}
		};

		ChooseStatPart part = parser.parse(parseableString);
		TalentFlaw tf = new TalentFlaw();
		part.addToTalentFlaw(null, tf );
		for (StatEnum stat : expectedStats) {
			assertNotNull( tf.getStatBonus(stat) );
			assertEquals(expectedBonus, tf.getStatBonus(stat).intValue());
		}
	}

	private static final class UTChooseStatPart extends ChooseStatPart{
		private final List<StatEnum> returnValue = new ArrayList<StatEnum>();
		
		public UTChooseStatPart(int amount, int bonus) {
			super(amount, bonus);
		}
		
		@Override
		protected List<StatEnum> showStatsSelectionDialog(TalentFlawContext context) {
			/* for testing no dialog */
			return returnValue;
		}
	}
}
