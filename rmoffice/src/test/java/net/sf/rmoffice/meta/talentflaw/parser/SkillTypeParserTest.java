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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.UTSkill;
import net.sf.rmoffice.meta.UTSkillCategory;
import net.sf.rmoffice.meta.enums.SkillType;

import org.junit.Test;

public class SkillTypeParserTest {

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
		SkillTypeParser parser = new SkillTypeParser(meta);
		
		assertFalse(parser.isParseable(null));
		assertFalse(parser.isParseable(""));
		assertFalse(parser.isParseable(" "));
		assertFalse(parser.isParseable("   "));
		assertFalse(parser.isParseable(" sdfsdf  "));
		assertFalse(parser.isParseable(" Cfdgdfg  "));
		assertFalse(parser.isParseable("C89"));
		assertFalse(parser.isParseable("C89=aS"));
		assertFalse(parser.isParseable("C89="));
		assertFalse(parser.isParseable("S89"));
		assertFalse(parser.isParseable("S89=as"));
		assertFalse(parser.isParseable("S89="));
		
		assertTrue(parser.isParseable("C12=EVERYMAN"));
		assertTrue(parser.isParseable("S12=RESTRICTED"));
		assertTrue(parser.isParseable(" C12=EVERYMAN"));
		assertTrue(parser.isParseable("S12=OCCUPATIONAL  "));
		assertTrue(parser.isParseable("  S12=RESTRICTED "));
	}

	@Test
	public void testParse() {
		assertSkillTypeCat(SkillType.EVERYMAN, 12, "C12=EVERYMAN");
		assertSkillTypeCat(SkillType.RESTRICTED, 2, "  C2=RESTRICTED  ");
		assertSkillTypeSkill(SkillType.RESTRICTED, 2, " S2=RESTRICTED ");
		assertSkillTypeSkill(SkillType.OCCUPATIONAL, 102, " S102=OCCUPATIONAL ");
	}

	private void assertSkillTypeCat(SkillType type, int id, String parseableText) {
		TalentFlaw talentFlaw = doTest(parseableText);
		assertEquals(1, talentFlaw.getSkillCatType().size());
		assertTrue(talentFlaw.getSkillCatType().containsKey(Integer.valueOf(id)));
		assertEquals(type, talentFlaw.getSkillCatType().get(Integer.valueOf(id)));
	}
	
	private void assertSkillTypeSkill(SkillType type, int id, String parseableText) {
		TalentFlaw talentFlaw = doTest(parseableText);
		assertEquals(1, talentFlaw.getSkillType().size());
		assertTrue(talentFlaw.getSkillType().containsKey(Integer.valueOf(id)));
		assertEquals(type, talentFlaw.getSkillType().get(Integer.valueOf(id)));
	}

	private TalentFlaw doTest(String parseableText) {
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
		SkillTypeParser parser = new SkillTypeParser(meta);
		TalentFlaw talentFlaw = new TalentFlaw();
		// test
		parser.parse(parseableText).addToTalentFlaw(null, talentFlaw);
		return talentFlaw;
	}
}
