/*
 * Copyright 2019 Daniel Golesny
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
package net.sf.rmoffice.meta;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class MetaDataLoaderTest {
	MetaDataLoader md;
	
	@Before
	public void setup() {
		md = new MetaDataLoader() {
			@Override
			String getResKey(String resKey, boolean isUserFile) {
				switch (resKey) {
				case "test.xyz.123":
					return "test-xyz-123";
				case "skillgroup.17":
					return "Natur-Tiere";
				}
				return resKey;
			}
		};
	}
	
	@Test
	public void test_that_replaceResToken_returns_token_unmodified() {
		assertEquals("abc", new MetaDataLoader().replaceResToken("abc"));
	}
	
	@Test
	public void test_the_skillcategory_are_replaced_with_blanks_at_start() {
		
		assertEquals("abc Natur-Tiere", md.replaceResToken("abc {G17}"));
	}
	
	@Test
	public void test_the_skillcategory_are_replaced_with_blanks_at_end() {
		assertEquals("Natur-Tiere abc", md.replaceResToken("{G17} abc"));
	}
	
	@Test
	public void test_the_skillcategory_are_replaced_without_blanks() {
		assertEquals("abc_Natur-Tiere_xyz", md.replaceResToken("abc_{G17}_xyz"));
	}

	@Test
	public void test_the_skillcategory_are_replaced_with_blanks() {
		assertEquals("abc Natur-Tiere xyz", md.replaceResToken("abc {G17} xyz"));
	}
	
	@Test
	public void test_the_reskeys_are_replaced() {
		assertEquals("abc test-xyz-123 xyz", md.replaceResToken("abc {=test.xyz.123} xyz"));
	}
	
	@Test
	public void test_the_na_reskeys_shortcut_are_not_replaced() {
		assertEquals("abc {test.xyz.123} xyz", md.replaceResToken("abc {test.xyz.123} xyz"));
	}
	
	@Test
	public void test_the_wrong_reskeys_shortcut_are_not_replaced() {
		assertEquals("abc {-test.xyz.123} xyz", md.replaceResToken("abc {-test.xyz.123} xyz"));
	}
}
