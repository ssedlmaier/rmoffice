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
import static net.sf.rmoffice.meta.enums.TalentFlawSkillCategoryType.*;

import java.util.List;

import net.sf.rmoffice.meta.enums.TalentFlawSkillCategoryType;
import net.sf.rmoffice.meta.talentflaw.RankPart;

import org.junit.Test;

public class RankParserTest {

	private int amount;
	private List<TalentFlawSkillCategoryType> types;
	
	@Test
	public void testIsParseable() {
		RankParser parser = new RankParser();
		
		assertFalse(parser.isParseable(null));
		assertFalse(parser.isParseable(""));
		assertFalse(parser.isParseable("   "));
		assertFalse(parser.isParseable(" CHOOSE1=XY"));
		assertFalse(parser.isParseable(" RANK=1"));
		assertFalse(parser.isParseable(" RANK1=XY"));
		assertFalse(parser.isParseable("RANK3=BASELIST;OPENLIST;CLOSEDLIST;INVALID"));
		
		assertTrue(parser.isParseable("RANK3=BASELIST;OPENLIST;CLOSEDLIST"));
		assertTrue(parser.isParseable("   RANK4=BASELIST;OPENLIST;CLOSEDLIST"));
		assertTrue(parser.isParseable("RANK10=BASELIST;OPENLIST;CLOSEDLIST   "));
		assertTrue(parser.isParseable("   RANK3=BASELIST;OPENLIST;CLOSEDLIST   "));
	}

	@Test
	public void testParse() {
		RankParser parser = new RankParser(){
			@Override
			protected RankPart createRankPart(int amount, List<TalentFlawSkillCategoryType> types) {
				RankParserTest.this.amount = amount;
				RankParserTest.this.types = types;
				return super.createRankPart(amount, types);
			}
		};
		
		assertRankPart(parser, "RANK3=BASELIST;OPENLIST;CLOSEDLIST", 3, BASELIST, OPENLIST, CLOSEDLIST);
		assertRankPart(parser, "RANK4=BASELIST;OPENLIST", 4, BASELIST, OPENLIST);
		assertRankPart(parser, "RANK6=BASELIST;OPENARCANELIST;OPENLIST", 6, BASELIST, OPENLIST, OPENARCANELIST);
		assertRankPart(parser, "RANK10=LANGUAGE", 10, LANGUAGE);
		assertRankPart(parser, "RANK20=LANGUAGE", 20, LANGUAGE);
	}

	private void assertRankPart(RankParser parser, String parseableString, int expectedAmount, TalentFlawSkillCategoryType... expectedTypes) {
		assertNotNull(parser.parse(parseableString));
		assertEquals(expectedAmount, amount);
		assertEquals(expectedTypes.length, types.size());
		for (TalentFlawSkillCategoryType exType : expectedTypes) {
			assertTrue(types.contains(exType));
		}
	}
	
	
}
