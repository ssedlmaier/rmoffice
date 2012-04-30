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

import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.IProgression;
import net.sf.rmoffice.meta.talentflaw.ProgressionPart;

import org.junit.Test;

public class ProgressionParserTest {

	@Test
	public void testIsParseable() {
		ProgressionParser parser = new ProgressionParser();
		
		assertFalse(parser.isParseable(null));
		assertFalse(parser.isParseable(""));
		assertFalse(parser.isParseable(" "));
		assertFalse(parser.isParseable("         "));
		assertFalse(parser.isParseable("BODYDEV=5;4;3;;"));
		assertFalse(parser.isParseable("POWERDEV=5;4;3;;"));
		
		assertTrue(parser.isParseable("BODYDEV=5;4;3;2;1"));
		assertTrue(parser.isParseable("  BODYDEV=5;4;3;2;1"));
		assertTrue(parser.isParseable("BODYDEV=5;4;3;2;-1  "));
		assertTrue(parser.isParseable("  BODYDEV=5;4;3;2;1  "));
		assertTrue(parser.isParseable("POWERDEV=5;4;3;2;1"));
		assertTrue(parser.isParseable("  POWERDEV=5;4;3;2;1"));
		assertTrue(parser.isParseable("POWERDEV=5;4;3;2;1  "));
		assertTrue(parser.isParseable("  POWERDEV=5;4;3;2;-1  "));
	}

	@Test
	public void testParse() {
		assertProgression("BODYDEV=5;4;3;2;1", true, 5, 4, 3, 2, 1);
		assertProgression("  BODYDEV=0;1;21;2;-1", true, 0, 1, 21, 2, -1);
		assertProgression("POWERDEV=0;1;21;2;-1 ", false, 0, 1, 21, 2, -1);
		assertProgression("  POWERDEV=1;1;1;1;1 ", false, 1, 1, 1, 1, 1);
		
	}

	private void assertProgression(String parseableString, boolean bodyOrPower, int... exp) {
		ProgressionParser parser = new ProgressionParser();
		ProgressionPart part = parser.parse(parseableString);
		TalentFlaw tf = new TalentFlaw();
		part.addToTalentFlaw(null, tf);
		IProgression progression = null;
		if (bodyOrPower) {
			progression = tf.getProgressionBody();
		} else {
			progression = tf.getProgressionPower();
		}
		assertNotNull(progression);
		for (int i=0; i<5; i++) {
			assertEquals(exp[i], progression.getDigit(i), 0.001f);
		}
	}
}
