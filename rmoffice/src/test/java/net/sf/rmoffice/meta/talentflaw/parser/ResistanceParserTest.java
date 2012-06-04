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
import net.sf.rmoffice.meta.enums.ResistanceEnum;
import net.sf.rmoffice.meta.talentflaw.ResistancePart;

import org.junit.Test;

public class ResistanceParserTest {
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	@Test
	public void testParse() {
		assertAdditionalLine("  RR=  rolemaster.version ", "rolemaster.version");

		
		for (int i=0; i<ResistanceEnum.values().length; i++) {
			assertRR(" RR= "+ResistanceEnum.values()[i].name()+" = "+(i-2), ResistanceEnum.values()[i], i-2);
		}
	}

	private void assertRR(String parseableString, ResistanceEnum res, int expectedBonus) {
		ResistanceParser parser = new ResistanceParser();
		ResistancePart part = parser.parse(parseableString);
		TalentFlaw tf = new TalentFlaw();
		part.addToTalentFlaw(null, tf );
		assertNull(tf.getAdditionalResistanceLine());
		assertNotNull(tf.getResistanceBonus(res));
		assertEquals(expectedBonus, tf.getResistanceBonus(res).intValue());
	}
	
	private void assertAdditionalLine(String parseableString, String expectedLine) {
		ResistanceParser parser = new ResistanceParser();
		ResistancePart part = parser.parse(parseableString);
		TalentFlaw tf = new TalentFlaw();
		part.addToTalentFlaw(null, tf );
		assertNotNull( tf.getAdditionalResistanceLine() );
		assertEquals( 1, tf.getAdditionalResistanceLine().size() );
		assertEquals( RESOURCE.getString(expectedLine), tf.getAdditionalResistanceLine().get(0) );
	}

}
