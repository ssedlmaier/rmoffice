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
import net.sf.rmoffice.core.TalentFlaw;

import org.junit.Test;

public class WeightPenaltyParserTest {

	@Test
	public void testIsParseable() {
		WeightPenaltyParser parser = new WeightPenaltyParser();
		
		assertFalse(parser.isParseable(null));
		assertFalse(parser.isParseable(""));
		assertFalse(parser.isParseable(" "));
		assertFalse(parser.isParseable("   "));
		assertFalse(parser.isParseable(" WEIGHTPENALTY=  "));
		assertFalse(parser.isParseable(" WEIGHTPENALTY  "));
		assertFalse(parser.isParseable("WEIGHTPENALTY=as"));
		assertFalse(parser.isParseable("WEIGHTPENALTY=10a"));
		
		assertTrue(parser.isParseable("WEIGHTPENALTY=1"));
		assertTrue(parser.isParseable("  WEIGHTPENALTY=0.5  "));
		assertTrue(parser.isParseable("  WEIGHTPENALTY=1  "));
		assertTrue(parser.isParseable("  WEIGHTPENALTY=1"));
		assertTrue(parser.isParseable("WEIGHTPENALTY=1  "));
		assertTrue(parser.isParseable("  WEIGHTPENALTY=-0.5  "));
	}

	@Test
	public void testParse() {
		WeightPenaltyParser parser = new WeightPenaltyParser();
		
		assertFloat(parser, "WEIGHTPENALTY=1", 1);
		assertFloat(parser, "WEIGHTPENALTY=0.5", 0.5f);
		assertFloat(parser, "WEIGHTPENALTY=0.25", 0.25f);
		assertFloat(parser, "WEIGHTPENALTY=1.25", 1.25f);
		
	}

	private void assertFloat(WeightPenaltyParser parser, String parseableString, float expected) {
		TalentFlaw tf = new TalentFlaw();
		parser.parse(parseableString).addToTalentFlaw(null, tf);
		assertNotNull(tf.getWeightPenalty());
		assertEquals(expected, tf.getWeightPenalty().floatValue(), 0.001f);
	}
	
}
