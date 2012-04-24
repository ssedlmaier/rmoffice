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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.Skill;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.UTSkill;
import net.sf.rmoffice.meta.UTSkillCategory;
import net.sf.rmoffice.meta.enums.SkillType;

import org.junit.Test;

public class TalentFlawFactoryTest {

	@Test
	public void testParseTalentFlawValue() throws Exception {
	    // prepare test data
		Skill s1 = new UTSkill(1);
		SkillCategory c1 = new UTSkillCategory(1);
		MetaData meta = new MetaData() {
			@Override
			public ISkill getSkill(Integer id) {
				return new UTSkill(id.intValue());
			}
			@Override
			public SkillCategory getSkillCategory(Integer id) {
				return new UTSkillCategory(id.intValue());
			}
		};
		TalentFlawFactory factory = new TalentFlawFactory(meta);
		
		TalentFlaw talFlawVal = callFactory(factory, "C1=5");
		assertNotNull(talFlawVal.getSkillCatBonus());
		assertEquals(1, talFlawVal.getSkillCatBonus().size());
		assertTrue(talFlawVal.getSkillCatBonus().containsKey(c1));

		talFlawVal = callFactory(factory, "C1=RESTRICTED");
		assertNotNull(talFlawVal.getSkillCatType());
		assertEquals(1, talFlawVal.getSkillCatType().size());
		assertEquals(SkillType.RESTRICTED, talFlawVal.getSkillCatType().get(c1));
		
		talFlawVal = callFactory(factory, "S1=15");
		assertNotNull(talFlawVal.getSkillBonus());
		assertEquals(1, talFlawVal.getSkillBonus().size());
		assertTrue(talFlawVal.getSkillBonus().containsKey(s1));

		talFlawVal = callFactory(factory, "S1=EVERYMAN");
		assertNotNull(talFlawVal.getSkillType());
		assertEquals(1, talFlawVal.getSkillType().size());
		assertEquals(SkillType.EVERYMAN, talFlawVal.getSkillType().get(s1));
		
		// INI
		talFlawVal = callFactory(factory, "INI=3");
		assertNotNull(talFlawVal.getInitiativeBonus());
		assertEquals(Integer.valueOf(3), talFlawVal.getInitiativeBonus());
		
		// DESCR
		talFlawVal = callFactory(factory, "DESCR=talent.snap.5");
		assertNotNull(talFlawVal.getDescriptions());
		assertEquals(1, talFlawVal.getDescriptions().size());
		
		// CHOOSE1
//		talFlawVal = new TalentFlaw();
//		loader.parseTalentFlawValue(talFlawVal, "CHOOSE1=C3;C29=EVERYMAN", meta);
//		assertNotNull(talFlawVal.getChoices());
//		assertEquals(1, talFlawVal.getChoices().size());
//		TalentFlawChoice choice1 = talFlawVal.getChoices().get(0);
//		assertEquals(1, choice1.getAmount());
//		assertEquals(2, choice1.getValues().size());
//		assertEquals(3, ((SkillCategory)choice1.getValues().get(0)).getId().intValue());
//		assertEquals(29, ((SkillCategory)choice1.getValues().get(1)).getId().intValue());
//		
//		// CHOOSE2
//		talFlawVal = new TalentFlawPresetLevel();
//		loader.parseTalentFlawValue(talFlawVal, "CHOOSE2=S1;S12;S123=EVERYMAN", meta);
//		assertNotNull(talFlawVal.getChoices());
//		assertEquals(1, talFlawVal.getChoices().size());
//		choice1 = talFlawVal.getChoices().get(0);
//		assertEquals(2, choice1.getAmount());
//		assertEquals(3, choice1.getValues().size());
//		assertEquals(1, ((ISkill)choice1.getValues().get(0)).getId().intValue());
//		assertEquals(12, ((ISkill)choice1.getValues().get(1)).getId().intValue());
//		assertEquals(123, ((ISkill)choice1.getValues().get(2)).getId().intValue());
		
		// TODO CHOOSESKILL1=C2=25
		
		// TODO CHOOSESKILL1=C13;C14=EVERYMAN
		
		// TODO BODYDEV=2;2;2;2;2
		
		// TODO CHOOSESKILL1=C27;C28;C29;C30;C31;C32;C33=OCCUPATIONAL=CAT=EVERYMAN
		
		// Belastungsabzüge halbieren
		
		// Sprachränge
		
		// Talentiert: +5 auf Gruppe oder +10 auf Fertigkeit
		
		// +25 auf Hinterhalt, aber nicht für Kritische Modifikation)
		
		// Waffen-Patzer um 2 (in der niedrigsten Kategorie) alle anderen um 1 reduzieren
	}

	private TalentFlaw callFactory(TalentFlawFactory factory, String partAsString) {
		TalentFlaw talFlawVal = new TalentFlaw();
		ITalentFlawPart part = factory.parseTalentFlawPart(partAsString);
		part.addToTalentFlaw(null, talFlawVal);
		return talFlawVal;
	}


}
