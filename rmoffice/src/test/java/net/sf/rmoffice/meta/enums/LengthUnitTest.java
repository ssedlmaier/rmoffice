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
package net.sf.rmoffice.meta.enums;

import static org.junit.Assert.*;

import org.junit.Test;

public class LengthUnitTest {

	@Test
	public void testParse() {
		assertEquals(4, LengthUnit.INCH.parseInt("4"));
		assertEquals(48, LengthUnit.INCH.parseInt("4'"));
		assertEquals(75, LengthUnit.INCH.parseInt("75\""));
		assertEquals(49, LengthUnit.INCH.parseInt("4'1\""));
		assertEquals(59, LengthUnit.INCH.parseInt("4'11\""));
		assertEquals(59, LengthUnit.INCH.parseInt("4' 11\""));
		assertEquals(59, LengthUnit.INCH.parseInt("4' 11\" "));
		assertEquals(11, LengthUnit.INCH.parseInt("11\""));
		
		assertEquals(167, LengthUnit.CM.parseInt("167"));
		assertEquals(11, LengthUnit.CM.parseInt("11 cm"));
		assertEquals(11, LengthUnit.CM.parseInt("  11   cm"));
		assertEquals(145, LengthUnit.CM.parseInt("1,45 m"));
		assertEquals(145, LengthUnit.CM.parseInt("1,45"));
		assertEquals(145, LengthUnit.CM.parseInt(" 1,45   m   "));
		assertEquals(100, LengthUnit.CM.parseInt("1 m"));
		assertEquals(1, LengthUnit.CM.parseInt("1 cm"));
		
		try {
			LengthUnit.CM.parseInt(" sdf cm");
			fail("NFE expected");
		} catch (NumberFormatException e) {
		}
	}

	@Test
	public void testConvertTo() {
		assertEquals(180, LengthUnit.CM.convertTo(180, LengthUnit.CM));
		assertEquals(50, LengthUnit.INCH.convertTo(50, LengthUnit.INCH));
		assertEquals(100, LengthUnit.CM.convertTo(254, LengthUnit.INCH));
		assertEquals(254, LengthUnit.INCH.convertTo(100, LengthUnit.CM));
		assertEquals(71, LengthUnit.CM.convertTo(180, LengthUnit.INCH));
		assertEquals(213, LengthUnit.INCH.convertTo(84, LengthUnit.CM));
		assertEquals(0, LengthUnit.INCH.convertTo(0, LengthUnit.CM));
		assertEquals(0, LengthUnit.CM.convertTo(0, LengthUnit.INCH));
	}
	
	@Test
	public void testFormattedString() {
		assertEquals("6' 3\"", LengthUnit.INCH.getFormattedString(75)); // 6' 3" = 75"
	}
}
