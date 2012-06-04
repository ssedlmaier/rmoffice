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

import java.util.ResourceBundle;

import net.sf.rmoffice.core.TalentFlaw;

import org.junit.Test;

public class DescriptionParserTest {

	protected static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	@Test
	public void testIsParseable() {
		DescriptionParser parser = new DescriptionParser();
		assertFalse(parser.isParseable(null));
		assertFalse(parser.isParseable(""));
		assertFalse(parser.isParseable(" "));
		assertFalse(parser.isParseable("  dfdf   "));
		assertFalse(parser.isParseable("67575  "));
		
		assertTrue(parser.isParseable("DESCR=rolemaster.version"));
		assertTrue(parser.isParseable("  DESCR=rolemaster.version"));
		assertTrue(parser.isParseable("DESCR=rolemaster.version   "));
		assertTrue(parser.isParseable("  DESCR=rolemaster.version  "));
		assertTrue(parser.isParseable("  DESCR  "));
		assertTrue(parser.isParseable("DESCR"));
	}

	@Test
	public void testParse() {
		assertDescr("DESCR=rolemaster.version");
		assertDescr("  DESCR=rolemaster.version");
		assertDescr("  DESCR=rolemaster.version ");
		assertDescr(" DESCR=rolemaster.version  ");
		assertDescr("DESCR=rolemaster.version  ");
		assertDescr("DESCR", DescriptionParser.COPYRIGHT_DESCR_KEY);
		assertDescr("DESCR ", DescriptionParser.COPYRIGHT_DESCR_KEY);
		assertDescr("  DESCR   ", DescriptionParser.COPYRIGHT_DESCR_KEY);
	}

	private void assertDescr(String parseableText) {
		assertDescr(parseableText, "rolemaster.version");
	}
	
	private void assertDescr(String parseableText, String expectedKey) {
		DescriptionParser parser = new DescriptionParser();
		TalentFlaw talentFlaw = new TalentFlaw();
		parser.parse(parseableText).addToTalentFlaw(null, talentFlaw);
		assertEquals(1, talentFlaw.getDescriptions().size());
		assertEquals(RESOURCE.getString(expectedKey), talentFlaw.getDescriptions().get(0));
	}
}
