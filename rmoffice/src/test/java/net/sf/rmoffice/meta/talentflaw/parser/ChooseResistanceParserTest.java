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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.enums.ResistanceEnum;
import net.sf.rmoffice.meta.talentflaw.ChooseResistancePart;
import net.sf.rmoffice.meta.talentflaw.TalentFlawContext;

import org.junit.Test;

public class ChooseResistanceParserTest {

	@Test
	public void testParse() {
		
		assertPart(" CHOOSERR1= "+ResistanceEnum.CHANNELING.name()+" ; "+ResistanceEnum.FEAR.name() + " =30", 30, 0, ResistanceEnum.CHANNELING);
		assertPart(" CHOOSERR2= "+ResistanceEnum.CHANNELING.name()+" ; "+ResistanceEnum.FEAR.name() + " =30", 30, 0, ResistanceEnum.CHANNELING, ResistanceEnum.FEAR);
		assertPart(" CHOOSERR1= "+ResistanceEnum.ESSENCE.name() + ";"+ResistanceEnum.MENTALISM.name()+" =2", 2, 0, ResistanceEnum.ESSENCE);
		assertPart(" CHOOSERR1= "+ResistanceEnum.ESSENCE.name() + " =2=25", 2, 25, ResistanceEnum.ESSENCE);
		assertPart(" CHOOSERR1= "+ResistanceEnum.CHANNELING.name()+" ; "+ResistanceEnum.FEAR.name() + " =-30", -30, 0, ResistanceEnum.CHANNELING);
		assertPart(" CHOOSERR2= "+ResistanceEnum.CHANNELING.name()+" ; "+ResistanceEnum.FEAR.name() + " =-30", -30, 0, ResistanceEnum.CHANNELING, ResistanceEnum.FEAR);
		assertPart(" CHOOSERR1= "+ResistanceEnum.ESSENCE.name() + ";"+ResistanceEnum.MENTALISM.name()+" =-2", -2, 0, ResistanceEnum.ESSENCE);
		assertPart(" CHOOSERR1= "+ResistanceEnum.ESSENCE.name() + " =-2=-25", -2, -25, ResistanceEnum.ESSENCE);
	}


	private void assertPart(String parseableString, int expectedBonus, int expectedSpellBonus, ResistanceEnum... expectedEnums) {
		ChooseResistanceParser parser = new ChooseResistanceParser() {
			@Override
			protected ChooseResistancePart createPart(int amount, int bonus, int spellBonus, ResistanceEnum... selectables) {
				UTChooseResistancePart part =  new UTChooseResistancePart(amount, bonus, spellBonus, selectables);
				part.selectables = new ArrayList<ResistanceEnum>();
				for (int i=0; i<amount; i++) {
					part.selectables.add(selectables[i]);
				}
				return part;
			}
		};
		assertTrue(parser.isParseable(parseableString));
		ChooseResistancePart part = parser.parse(parseableString);
		TalentFlaw tf =new TalentFlaw();
		part.addToTalentFlaw(null, tf);
		List<ResistanceEnum> expectedEnumList = Arrays.asList(expectedEnums);
		for (ResistanceEnum res : ResistanceEnum.values()) {
			if (expectedEnumList.contains(res)) {
				assertNotNull(tf.getResistanceBonus(res));
				assertEquals(expectedBonus, tf.getResistanceBonus(res).intValue());
				if (expectedSpellBonus != 0) {
					assertEquals(expectedSpellBonus, tf.getSpellRealmBonus(res.getStat()).intValue());
				} else {
					assertNull(tf.getSpellRealmBonus(res.getStat()));
				}
			} else {
				assertNull(tf.getResistanceBonus(res));
				assertNull(tf.getSpellRealmBonus(res.getStat()));
			}
		}
	}

	
	private static class UTChooseResistancePart extends ChooseResistancePart {
		List<ResistanceEnum> selectables;

		public UTChooseResistancePart(int amount, int bonus, int spellBonus, ResistanceEnum[] selectables) {
			super(amount, bonus, spellBonus, selectables);
		}
		
		@Override
		protected List<ResistanceEnum> showSelectionDialog(TalentFlawContext context) {
			return selectables;
		}
	}
}
