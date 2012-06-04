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

public enum TalentFlawLevel {
	FREE(0),
	LESSER(2),
	MINOR(3),
	MAJOR(4),
	GREATER(5);

	private final int bgCosts;

	private TalentFlawLevel(int bgCosts) {
		this.bgCosts = bgCosts;
		
	}
	
	/**
	 * Default background option costs.
	 *  
	 * @return background option costs
	 */
	public int getBGCost() {
		return bgCosts;
	}
}
