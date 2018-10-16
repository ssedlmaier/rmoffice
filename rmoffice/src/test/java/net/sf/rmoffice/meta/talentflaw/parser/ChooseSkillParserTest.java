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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.UTSkill;
import net.sf.rmoffice.meta.UTSkillCategory;
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.meta.talentflaw.ChooseSkillPart;

import org.junit.Test;

public class ChooseSkillParserTest {
	private int amount;
	private int bonus;
	private SkillType skillType;
	private List<SkillCategory> cats;
	private List<ISkill> skills;
	private String followUpAction;
	private int followUpBonus;
	private SkillType followUpType;
	
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
		ChooseSkillParser parser = new ChooseSkillParser(meta);

		assertFalse(parser.isParseable(null));
		assertFalse(parser.isParseable(""));
		assertFalse(parser.isParseable(" "));
		assertFalse(parser.isParseable(" C48=5"));
		assertFalse(parser.isParseable(" CHOOSE=S48="));
		assertFalse(parser.isParseable(" CHOOSESKILL=S48="));
		assertFalse(parser.isParseable(" CHOOSE1=S48="));
		assertFalse(parser.isParseable(" CHOOSESKILL1=S48="));
		assertFalse(parser.isParseable("  CHOOSE10=C48;C45=EVERYMA  "));
		assertFalse(parser.isParseable("  CHOOSESKILL10=C48;C45=EVERYMA  "));
		assertFalse(parser.isParseable("  CHOOSE10=C48;C45=EEVERYMAN  "));
		assertFalse(parser.isParseable(" CHOOSESKILL1=S48"));
		assertFalse(parser.isParseable(" CHOOSESKILL1=C48"));
		assertFalse(parser.isParseable(" CHOOSESKILL1=CS48=1"));
		assertFalse(parser.isParseable(" CHOOSESKILL1=CS48=EVERYMAN"));
		assertFalse(parser.isParseable(" CHOOSESKILL1=CS48;S56=5"));
		
		assertTrue(parser.isParseable(" CHOOSESKILL1=S48=5"));
		assertTrue(parser.isParseable(" CHOOSESKILL2=C56=10 "));
		assertTrue(parser.isParseable(" CHOOSESKILL2=S48;C56=10 "));
		assertTrue(parser.isParseable("CHOOSESKILL4=C48;C45=-4"));
		assertTrue(parser.isParseable("  CHOOSESKILL10=C48;C45=24  "));
		assertTrue(parser.isParseable(" CHOOSESKILL2=C56=10=CAT=10 "));
		assertTrue(parser.isParseable(" CHOOSESKILL2=S48;C56=10=CAT=5 "));
		
		assertTrue(parser.isParseable("  CHOOSESKILL10=C48;C45=EVERYMAN  "));
		assertTrue(parser.isParseable("  CHOOSESKILL10=C48;C45=OCCUPATIONAL  "));
		assertTrue(parser.isParseable(" CHOOSESKILL2=C56=10=CAT=EVERYMAN "));
		assertTrue(parser.isParseable(" CHOOSESKILL2=S48;C56=10=CAT=OCCUPATIONAL "));
		assertTrue(parser.isParseable("CHOOSESKILL1=C27;C28;C29;C30;C31;C32;C33=OCCUPATIONAL=CAT=EVERYMAN"));
	}
	
	@Test
	public void testParse() {
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
		ChooseSkillParser parser = new ChooseSkillParser(meta) {

			@Override
			ChooseSkillPart createChooseBonusPart(int amount, int bonus, List<SkillCategory> cats, List<ISkill> skills, String followUpAction,
					int followUpBonus, SkillType followUpType) {
				ChooseSkillParserTest.this.amount = amount;
				ChooseSkillParserTest.this.bonus = bonus;
				ChooseSkillParserTest.this.cats = cats;
				ChooseSkillParserTest.this.skills = skills;
				ChooseSkillParserTest.this.followUpAction = followUpAction;
				ChooseSkillParserTest.this.followUpBonus = followUpBonus;
				ChooseSkillParserTest.this.followUpType = followUpType;
				ChooseSkillParserTest.this.skillType = null;
				return super.createChooseBonusPart(amount, bonus, cats, skills, followUpAction, followUpBonus, followUpType);
			}
			
			@Override
			ChooseSkillPart createChooseSkillBonusPart(int amount, SkillType type, List<SkillCategory> cats, List<ISkill> skills, String followUpAction,
					int followUpBonus, SkillType followUpType) {
				ChooseSkillParserTest.this.amount = amount;
				ChooseSkillParserTest.this.skillType = type;
				ChooseSkillParserTest.this.cats = cats;
				ChooseSkillParserTest.this.skills = skills;
				ChooseSkillParserTest.this.followUpAction = followUpAction;
				ChooseSkillParserTest.this.followUpBonus = followUpBonus;
				ChooseSkillParserTest.this.followUpType = followUpType;
				ChooseSkillParserTest.this.bonus = 0;
				return super.createChooseSkillBonusPart(amount, type, cats, skills, followUpAction, followUpBonus, followUpType);
			}
		};
		
		assertPartValues(parser, " CHOOSESKILL1=S48;S5=5", 1, 5, null, null, new int[] {48, 5});
		assertPartValues(parser, " CHOOSESKILL2=S48;S156=10 ", 2, 10, null, null, new int[] {48, 156});
		assertPartValues(parser, "CHOOSESKILL4=C48;C45=-4", 4, -4, null, new int[] {48, 45}, null);
		assertPartValues(parser, "  CHOOSESKILL10=C48;C45=23  ", 10, 23, null, new int[] {48, 45}, null);
		assertPartValues(parser, "  CHOOSESKILL2=C28;C29;C30;C31;C32;C33=10  ", 2, 10, null, new int[] {28,29,30,31,32,33}, null);
		
		assertPartValues(parser, "CHOOSESKILL4=C48;C45=OCCUPATIONAL", 4, 0, SkillType.OCCUPATIONAL, new int[] {48, 45}, null);
		assertPartValues(parser, "  CHOOSESKILL2=C28;C29;C30;C31;C32;C33=EVERYMAN  ", 2, 0, SkillType.EVERYMAN, new int[]{28,29,30,31,32,33}, null);
		
		// with follow-up action
		assertPartValues(parser, "CHOOSESKILL2=C48;C45=10=CAT=EVERYMAN", 2, 10, null, new int[] {48, 45}, null, "CAT", 0, SkillType.EVERYMAN);
		assertPartValues(parser, "CHOOSESKILL2=C48;C45=10=CAT=RESTRICTED", 2, 10, null, new int[] {48, 45}, null, "CAT", 0, SkillType.RESTRICTED);
		assertPartValues(parser, "CHOOSESKILL3=C48;C45;S66=5=CAT=30", 3, 5, null, new int[] {48, 45}, new int[] {66}, "CAT", 30, null);
		assertPartValues(parser, "CHOOSESKILL3=S66;S67=EVERYMAN=CAT=30", 3, 0, SkillType.EVERYMAN, null, new int[] {66,67}, "CAT", 30, null);
		assertPartValues(parser, "CHOOSESKILL1=C27;C28;C29;C30=OCCUPATIONAL=CAT=EVERYMAN", 1, 0, SkillType.OCCUPATIONAL, new int[] {27,28,29,30}, null,  "CAT", 0, SkillType.EVERYMAN);
	}
	
	private void assertPartValues(ChooseSkillParser parser, String pStr, int amount, int bonus, SkillType type, int[] catIds, int[] skillIds) {
		assertPartValues(parser, pStr, amount, bonus, type, catIds, skillIds, "", 0, null);
	}
	
	private void assertPartValues(ChooseSkillParser parser, String pStr, int amount, int bonus, SkillType type, int[] catIds, int[] skillIds, 
			 String followUpAction, int followUpBonus, SkillType followUpSkillType) {
		cleanResults();
		parser.parse(pStr);
		assertEquals(amount, this.amount);
		if (type != null) {
			assertEquals(skillType, this.skillType);
		} else {
			assertNull(skillType);
			assertEquals(bonus, this.bonus);
		}
		if (skillIds !=null) {
			assertEquals(skillIds.length, skills.size());
			for (int i=0; i<skillIds.length; i++) {
				assertEquals(skillIds[i], skills.get(i).getId().intValue());
			}
		} else {
			assertEquals(0, skills.size());
		}
		if (catIds != null) {
			assertEquals(catIds.length, cats.size());
			for (int i=0; i<catIds.length; i++) {
				assertEquals(catIds[i], cats.get(i).getId().intValue());
			}
		} else {
			assertEquals(0, cats.size());
		}
		// follow-up values
		assertEquals(this.followUpAction, followUpAction);
		assertEquals(this.followUpBonus, followUpBonus);
		assertEquals(this.followUpType, followUpSkillType);
	}

	private void cleanResults() {
		amount = 0;
		bonus = 0;
		skillType = null;
		cats = null;
		skills = null;
		skillType = null;
		followUpAction = null;
		followUpBonus = 0;
		followUpType = null;
	}
}
