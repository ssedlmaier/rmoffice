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

public class WeightUnitTest {

	@Test
	public void testConvertTo() {
		assertEquals(2.204f, WeightUnit.KILOGRAM.convertTo(1, WeightUnit.LBS), 0.01f);
		assertEquals(0.453592370f , WeightUnit.LBS.convertTo(1, WeightUnit.KILOGRAM), 0.01f);
		assertEquals(11.02f, WeightUnit.KILOGRAM.convertTo(5, WeightUnit.LBS), 0.01f);
		assertEquals(5f , WeightUnit.LBS.convertTo(11.02f, WeightUnit.KILOGRAM), 0.01f);
	}
}
