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
package net.sf.rmoffice.generator;

import org.apache.commons.lang.math.RandomUtils;


/**
 * 
 */
public class DiceUtils {
	/**
	 * Rolls the given number of dices.
	 * 
	 * @param count the number of dices.
	 * @param maxVal the max value, e.g. for 1d10 is maxVal 10
	 * @return the rolled number
	 */
	public static int roll(int count, int maxVal) {
		int r = 0;
		for (int i=0; i< count; i++) {
			r += RandomUtils.nextInt(maxVal) + 1;
		}
		return r;
	}
}
