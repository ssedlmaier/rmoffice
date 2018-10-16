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
import static org.junit.Assert.assertNotNull;
import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.Skillcost;
import net.sf.rmoffice.meta.UTSkill;
import net.sf.rmoffice.meta.UTSkillCategory;
import net.sf.rmoffice.meta.talentflaw.CostPart;

import org.junit.Before;
import org.junit.Test;

public class CostParserTest {
	MetaData metaData;

	@Before
	public void prepareMetaData() {
		metaData = new MetaData() {
			@Override
			public ISkill getSkill(Integer id) {
				return new UTSkill(id.intValue());
			}
			@Override
			public SkillCategory getSkillCategory(Integer id) {
				return new UTSkillCategory(id.intValue());
			}
		};
	}
	
	@Test
	public void testParse() {
		assertSkill(" COST=S48=1/1/1 ", 48, true, 1, 1, 1);
		assertSkill(" COST=S53=1/2/3 ", 53, true, 1, 2, 3);
		assertSkill(" COST=S1 = 3/2/13 ", 1, true, 3, 2, 13);
		assertSkill(" COST=S53=1/2 ", 53, true, 1, 2);
		assertSkill(" COST=S53=1 ", 53, true, 1);
		
		assertSkill(" COST=C48=1/1/1 ", 48, false, 1, 1, 1);
		assertSkill(" COST=C53=1/2/3 ", 53, false, 1, 2, 3);
		assertSkill(" COST=C1 = 3/2/13 ", 1, false, 3, 2, 13);
		assertSkill(" COST=C53=1/2 ", 53, false, 1, 2);
		assertSkill(" COST=C53=1 ", 53, false, 1);
	}


	private void assertSkill(String parseableString, int id, boolean isSkill, int... expectedSkill) {
		CostParser parser = new CostParser(metaData);
		CostPart part = parser.parse(parseableString);
		TalentFlaw tf = new TalentFlaw();
		part.addToTalentFlaw(null, tf);
		Skillcost cost;
		if (isSkill) {
			cost = tf.getSkillCostReplacement(new UTSkill(id));
		} else {
			cost = tf.getSkillCategoryCostReplacement(new UTSkillCategory(id));
		}
		assertNotNull(cost);
		assertEquals(expectedSkill.length, cost.size());
		for (int i=0; i<expectedSkill.length; i++) {
			assertEquals(expectedSkill[i], cost.getCost(i));
		}
	}
}
