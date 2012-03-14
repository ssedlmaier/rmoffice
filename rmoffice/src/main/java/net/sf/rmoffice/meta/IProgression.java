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
package net.sf.rmoffice.meta;

/**
 * 
 */
public interface IProgression extends Comparable<IProgression>{

	public int getBonus(int rangCount);

	/**
	 * Returns the digit of the progression.
	 * 
	 * @param step 0 to 4
	 * @return the digit of the progression
	 */
	public float getDigit(int step);
	
	/**
	 * Returns the progression as human readable string.
	 * @return progression string, not {@code null}
	 */
	public String getFormattedString();

}