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
package net.sf.rmoffice.meta.internal;

import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.enums.SpellUserType;
import net.sf.rmoffice.meta.enums.SpelllistPart;


/**
 * Compound object used as key in a map for spelllist costs.
 */
public class SkillcategorySpelllistPartKey {
	
	private final SpelllistPart part;
	private final SkillCategory category;
	private final SpellUserType type;

	/**
	 * @param category the skill category
	 * @param part the spell list part
	 * @param type the spell user type
	 * 
	 */
	public SkillcategorySpelllistPartKey(SkillCategory category, SpelllistPart part, SpellUserType type) {
		this.category = category;
		this.part = part;
		this.type = type;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((part == null) ? 0 : part.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		SkillcategorySpelllistPartKey other = (SkillcategorySpelllistPartKey) obj;
		if (category == null) {
			if (other.category != null) return false;
		} else if (!category.equals(other.category)) return false;
		if (part != other.part) return false;
		if (type != other.type) return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SpelllistPartKey[").append(category).append("/").append(part).append("/").append(type).append("]");
		return sb.toString();
	}
	
	
}
