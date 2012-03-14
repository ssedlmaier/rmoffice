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
 * Holder for male/female avg values for weight and height.
 */
public class WeightHeight {
	private int MALE = 0;
	private int FEMALE = 1;
	private int[] weight = new int[2];
	private int[] height = new int[2];
	
	
	public int getWeightAvg(boolean isFemale) {
		return weight[isFemale ? FEMALE : MALE];
	}
	
	public int getHeightAvg(boolean isFemale) {
		return height[isFemale ? FEMALE : MALE];
	}
	
	/* package*/ void setWeightAvg(int kgMale, int kgFemale) {
		weight[MALE] = kgMale;
		weight[FEMALE] = kgFemale;
	}
	
	/* package*/ void setHeightAvg(int cmMale, int cmFemale) {
		height[MALE] = cmMale;
		height[FEMALE] = cmFemale;
	}
}
