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
package net.sf.rmoffice.meta.enums;



/**
 * The definition of the spelllist parts.
 */
public enum SpelllistPart {
    RANK0_5(1),
	RANK6_10(2),
	RANK11_15(3),
	RANK16_20(4),
	RANK21_(5);
    
    
	private final int noneSpelluserFactor;

	private SpelllistPart(int noneSpelluserFactor) {
		this.noneSpelluserFactor = noneSpelluserFactor;
	}
	
	public int getNoneSpelluserFactor() {
		return noneSpelluserFactor;
	}
	
	/**
	 * Returns the part for the given rank. Rank 5 returns Part 1-5.
	 * 
	 * @param rank the rank
	 * @return the part
	 */
	public static SpelllistPart getPartForRank(int rank) {
		if (rank <= 5) {
			return RANK0_5;
		} else if (rank <= 10) {
			return RANK6_10;
		} else if (rank <= 15) {
			return RANK11_15;
		} else if (rank <= 20) {
			return RANK16_20;
		} else {
			return RANK21_;
		}
	}
}
