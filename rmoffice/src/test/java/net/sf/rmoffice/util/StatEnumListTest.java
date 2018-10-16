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
package net.sf.rmoffice.util;

import static org.junit.Assert.*;

import net.sf.rmoffice.meta.enums.StatEnum;

import org.junit.Test;


/**
 * 
 */
public class StatEnumListTest {

	@Test
	public void testIsArcane() {
		assertIsArcane(false, StatEnum.AGILITY);
		assertIsArcane(false, StatEnum.CONSTITUTION, StatEnum.PRESENCE);
		assertIsArcane(false, StatEnum.CONSTITUTION, StatEnum.PRESENCE, StatEnum.INTUITION);
		assertIsArcane(true, StatEnum.CONSTITUTION, StatEnum.PRESENCE, StatEnum.INTUITION, StatEnum.EMPATHY);
		assertIsArcane(true, StatEnum.PRESENCE, StatEnum.INTUITION, StatEnum.EMPATHY);
		assertIsArcane(true, StatEnum.CONSTITUTION, StatEnum.PRESENCE, StatEnum.INTUITION, StatEnum.EMPATHY, StatEnum.MEMORY);
	}

	private void assertIsArcane(boolean expectedResult, StatEnum... stats ) {
		StatEnumList enumList = new StatEnumList();
		for (StatEnum statEnum : stats) {
			enumList.add(statEnum);
		}
		assertEquals(Boolean.valueOf(expectedResult), Boolean.valueOf(enumList.isArcane()));
	}
}
