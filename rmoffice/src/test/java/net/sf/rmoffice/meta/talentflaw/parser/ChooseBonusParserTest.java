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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.UTSkill;
import net.sf.rmoffice.meta.UTSkillCategory;
import net.sf.rmoffice.meta.talentflaw.ChooseBonusPart;

import org.junit.Test;

public class ChooseBonusParserTest {

	private int amount = 0;
	private int bonus = 0;
	private List<SkillCategory> cats = null;
	private List<ISkill> skills = null;
	
	@Test
	public void testIsParseable() {
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
		ChooseBonusParser parser = new ChooseBonusParser(meta);

		assertFalse(parser.isParseable(null));
		assertFalse(parser.isParseable(""));
		assertFalse(parser.isParseable(" "));
		assertFalse(parser.isParseable(" C48=5"));
		assertFalse(parser.isParseable(" CHOOSE=S48="));
		assertFalse(parser.isParseable(" CHOOSE1=S48="));
		
		assertTrue(parser.isParseable(" CHOOSE1=S48;S56=5"));
		assertTrue(parser.isParseable(" CHOOSE2=S48;S56=10 "));
		assertTrue(parser.isParseable("CHOOSE4=C48;C45=-4"));
		assertTrue(parser.isParseable("  CHOOSE10=C48;C45=24  "));
	}
	
	@Test
	public void test() {
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
		ChooseBonusParser parser = new ChooseBonusParser(meta) {
			@Override
			ChooseBonusPart createChooseBonusPart(int amount, int bonus, List<SkillCategory> cats, List<ISkill> skills) {
				ChooseBonusParserTest.this.amount = amount;
				ChooseBonusParserTest.this.bonus = bonus;
				ChooseBonusParserTest.this.cats = cats;
				ChooseBonusParserTest.this.skills = skills;
				return super.createChooseBonusPart(amount, bonus, cats, skills);
			}
		};
		
		assertPartValues(parser, " CHOOSE1=S48;S5=5", 1, 5, ISkill.class, 48, 5);
		assertPartValues(parser, " CHOOSE2=S48;S156=10 ", 2, 10, ISkill.class, 48, 156);
		assertPartValues(parser, "CHOOSE4=C48;C45=-4", 4, -4, SkillCategory.class, 48, 45);
		assertPartValues(parser, "  CHOOSE10=C48;C45=23  ", 10, 23, SkillCategory.class, 48, 45);
		assertPartValues(parser, "  CHOOSE2=C28;C29;C30;C31;C32;C33=10  ", 2, 10, SkillCategory.class, 28,29,30,31,32,33);
	}

	private void assertPartValues(ChooseBonusParser parser, String pStr, int amount, int bonus, Class<?> toCheckType, int... ids) {
		cleanResults();
		parser.parse(pStr);
		assertEquals(amount, this.amount);
		assertEquals(bonus, this.bonus);
		if (toCheckType.equals(ISkill.class)) {
			assertNotNull(skills);
			assertEquals(ids.length, skills.size());
			for (int i=0; i<ids.length; i++) {
				assertEquals(ids[i], skills.get(i).getId().intValue());
			}
			assertEquals(0, cats.size());
		} else if (toCheckType.equals(SkillCategory.class)){
			assertNotNull(cats);
			assertEquals(ids.length, cats.size());
			for (int i=0; i<ids.length; i++) {
				assertEquals(ids[i], cats.get(i).getId().intValue());
			}
			assertEquals(0, skills.size());
		}
	}
	
	private void cleanResults() {
		amount = 0;
		bonus = 0;
		cats = null;
		skills = null;
	}
}
