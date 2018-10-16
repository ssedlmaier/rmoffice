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
package net.sf.rmoffice.meta.talentflaw;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

public class KeyValuePartTest {

	@Test
	public void testFormat() {
		Locale.setDefault(Locale.GERMAN);
		assertEquals("+0", KeyValuePart.format(0.0000005f));
		assertEquals("+1", KeyValuePart.format(0.999999f));
		assertEquals("+1,2", KeyValuePart.format(1.199999999f));
		assertEquals("+1,6", KeyValuePart.format(1.6f));
		assertEquals("-3", KeyValuePart.format(-3.000009f));
		assertEquals("-3,123", KeyValuePart.format(-3.12345f));
		Locale.setDefault(Locale.ENGLISH);
		assertEquals("+0", KeyValuePart.format(0.0000005f));
		assertEquals("+1", KeyValuePart.format(0.999999f));
		assertEquals("+1.2", KeyValuePart.format(1.199999999f));
		assertEquals("+1.6", KeyValuePart.format(1.6f));
		assertEquals("-3", KeyValuePart.format(-3.000009f));
		assertEquals("-3.123", KeyValuePart.format(-3.12345f));
	}

}
