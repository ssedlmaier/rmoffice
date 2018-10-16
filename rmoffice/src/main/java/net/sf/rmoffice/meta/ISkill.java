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
package net.sf.rmoffice.meta;

import net.sf.rmoffice.meta.enums.LengthUnit;
import net.sf.rmoffice.meta.enums.RaceScope;


/**
 * 
 */
public interface ISkill extends INamed {

	/**
	 * Returns the description with the given length unit. Returns the general description
	 * if no description for length unit is available. 
	 * 
	 * @param lengthUnit the length unit, may be {@code null} 
	 * @return description
	 */
	public String getDescription(LengthUnit lengthUnit);
	
	/**
	 * Returns whether the skill is a spell list or not.
	 * 
	 * @return whether the skill is a spell list or not
	 */
	public boolean isSpelllist();
	
	public RaceScope getScope();
	
	/**
	 * 
	 * @return the source book string
	 */
	public String getSource();
	
	/**
	 * Returns a number for ordering/grouping the favorites.
	 * 
	 * @return order group number, not {@code null}
	 */
	public Integer getOrderGroup();
	
	/**
	 * Sets the new order number or {@code null} for default.
	 * 
	 * @param order number or {@code null}
	 */
	public void setOrderGroup(Integer orderGroup);
	
}