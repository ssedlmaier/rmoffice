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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.UTSkill;
import net.sf.rmoffice.meta.UTSkillCategory;
import net.sf.rmoffice.meta.enums.SkillType;

import org.junit.Test;

public class TalentFlawFactoryTest {

	@Test
	public void testParseTalentFlawValue() throws Exception {
	    // prepare test data
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
		assertTrue(talFlawVal.getSkillCatBonus().containsKey(Integer.valueOf(1)));

		talFlawVal = callFactory(factory, "C1=RESTRICTED");
		assertNotNull(talFlawVal.getSkillCatType());
		assertEquals(1, talFlawVal.getSkillCatType().size());
		assertEquals(SkillType.RESTRICTED, talFlawVal.getSkillCatType().get(Integer.valueOf(1)));
		
		talFlawVal = callFactory(factory, "S2=15");
		assertNotNull(talFlawVal.getSkillBonus());
		assertEquals(1, talFlawVal.getSkillBonus().size());
		assertTrue(talFlawVal.getSkillBonus().containsKey(Integer.valueOf(2)));

		talFlawVal = callFactory(factory, "S3=EVERYMAN");
		assertNotNull(talFlawVal.getSkillType());
		assertEquals(1, talFlawVal.getSkillType().size());
		assertEquals(SkillType.EVERYMAN, talFlawVal.getSkillType().get(Integer.valueOf(3)));
		
		// INI
		talFlawVal = callFactory(factory, "INI=3");
		assertNotNull(talFlawVal.getInitiativeBonus());
		assertEquals(Integer.valueOf(3), talFlawVal.getInitiativeBonus());
		
		// DESCR
		talFlawVal = callFactory(factory, "DESCR=rolemaster.version");
		assertNotNull(talFlawVal.getDescription());
	}

	private TalentFlaw callFactory(TalentFlawFactory factory, String partAsString) {
		TalentFlaw talFlawVal = new TalentFlaw();
		ITalentFlawPart part = factory.parseTalentFlawPart(partAsString);
		part.addToTalentFlaw(null, talFlawVal);
		return talFlawVal;
	}


}
