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

import static org.junit.Assert.*;

import net.sf.rmoffice.core.TalentFlaw;

import org.junit.Test;

public class InitiativeParserTest {

	@Test
	public void testIsParseable() {
		InitiativeParser parser = new InitiativeParser();
		
		assertFalse(parser.isParseable(null));
		assertFalse(parser.isParseable(""));
		assertFalse(parser.isParseable(" "));
		assertFalse(parser.isParseable("         "));
		
		assertTrue(parser.isParseable("INI=5"));
		assertTrue(parser.isParseable("INI=10"));
		assertTrue(parser.isParseable("  INI=10   "));
		assertTrue(parser.isParseable("  INI=10   "));
		assertTrue(parser.isParseable("INI=-5"));
		assertTrue(parser.isParseable("  INI=-5   "));
	}

	@Test
	public void testParse() {
		assertIni(5, "INI=5");
		assertIni(10, "INI=10");
		assertIni(10, "  INI=10   ");
		assertIni(10, "  INI=10   ");
		assertIni(-5, "INI=-5");
		assertIni(-5, "  INI=-5   ");
	}

	private void assertIni(int expectedIni, String parseableText) {
		InitiativeParser parser = new InitiativeParser();
		TalentFlaw tf = new TalentFlaw();
		parser.parse(parseableText).addToTalentFlaw(null, tf);
		assertEquals(Integer.valueOf(expectedIni), tf.getInitiativeBonus());
	}
}
