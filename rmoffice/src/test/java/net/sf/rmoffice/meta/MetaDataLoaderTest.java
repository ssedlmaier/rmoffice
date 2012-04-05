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
package net.sf.rmoffice.meta;

import static org.junit.Assert.*;

import net.sf.rmoffice.meta.enums.SkillType;

import org.junit.Test;

public class MetaDataLoaderTest {

	@Test
	public void testParseTalentFlawValue() throws Exception {
		final Skill s1 = new Skill();
		final SkillCategory c1 = new SkillCategory();
		MetaData meta = new MetaData() {
			@Override
			public ISkill getSkill(Integer id) {
				return s1; 
			}
			@Override
			public SkillCategory getSkillCategory(Integer id) {
				return c1;	
			}
		};
		MetaDataLoader loader = new MetaDataLoader();
		TalentFlawPresetValue talFlawVal = new TalentFlawPresetValue();
		loader.parseTalentFlawValue(talFlawVal, "C1=5", meta);
		assertNotNull(talFlawVal.getSkillCatBonus());
		assertEquals(1, talFlawVal.getSkillCatBonus().size());
		assertTrue(talFlawVal.getSkillCatBonus().containsKey(c1));

		talFlawVal = new TalentFlawPresetValue();
		loader.parseTalentFlawValue(talFlawVal, "C1=RESTRICTED", meta);
		assertNotNull(talFlawVal.getSkillCatType());
		assertEquals(1, talFlawVal.getSkillCatType().size());
		assertEquals(SkillType.RESTRICTED, talFlawVal.getSkillCatType().get(c1));
		
		talFlawVal = new TalentFlawPresetValue();
		loader.parseTalentFlawValue(talFlawVal, "S1=15", meta);
		assertNotNull(talFlawVal.getSkillBonus());
		assertEquals(1, talFlawVal.getSkillBonus().size());
		assertTrue(talFlawVal.getSkillBonus().containsKey(s1));

		talFlawVal = new TalentFlawPresetValue();
		loader.parseTalentFlawValue(talFlawVal, "S1=EVERYMAN", meta);
		assertNotNull(talFlawVal.getSkillType());
		assertEquals(1, talFlawVal.getSkillType().size());
		assertEquals(SkillType.EVERYMAN, talFlawVal.getSkillType().get(s1));
		
		// INI
		talFlawVal = new TalentFlawPresetValue();
		loader.parseTalentFlawValue(talFlawVal, "INI=3", meta);
		assertNotNull(talFlawVal.getInitiativeBonus());
		assertEquals(Integer.valueOf(3), talFlawVal.getInitiativeBonus());
		
		// DESCR
		talFlawVal = new TalentFlawPresetValue();
		loader.parseTalentFlawValue(talFlawVal, "DESCR=talent.snap.5", meta);
		assertNotNull(talFlawVal.getDescriptions());
		assertEquals(1, talFlawVal.getDescriptions().size());
		
	}

}
