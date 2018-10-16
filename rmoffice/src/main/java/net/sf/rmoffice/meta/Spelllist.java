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

import java.util.HashSet;
import java.util.Set;

import net.sf.rmoffice.meta.enums.SpelllistType;
import net.sf.rmoffice.meta.enums.StatEnum;



/**
 * 
 */
public class Spelllist extends Skill {
	private SpelllistType  type;
	private Set<StatEnum> attributes;
	
	/**
	 * Returns the type of the spelllist
	 * 
	 * @return the type
	 */
	public SpelllistType getSpelllistType() {
		return type;
	}
	
	/* package */ void setType(SpelllistType type) {
		this.type = type;
	}
	
	public Set<StatEnum> getAttributes() {
		return attributes;
	}
	
	/* package */  void addAttributes(StatEnum attribute) {
		if (attributes == null) {
			attributes = new HashSet<StatEnum>();
		}
		attributes.add(attribute);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isSpelllist() {
		return true;
	}
	
}
