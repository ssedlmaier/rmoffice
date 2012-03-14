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

public enum RankType {
	/** Standard*/
	S(false, false, true, false, true, false, false, false, false, false),
	/** Kombiniert */
	C(false, true, false, false, false, false, false, false, false, false),
	/** Limitiert */
	L(true, false, false, false, false, false, false, false, false, false),
	/** Volks progressionMagie */
	M(false, false, false, false, false, false, true, false, true, false),
	/** Volks KörperEntwicklung */
	K(false, false, false, false, false, true, false, false, false, false),
	/** Spell (own realm) */
	Z(true, false, false, false, false, false, false, false, true, true),
	/** Spell (not-own realm) */
	Y(true, false, false, false, false, false, false, false, true, false),
	/** Arcane */
	A(true, false, false, false, false, false, false, false, true, false),
	/** Weapon */
	W(false, false, true, true, true, false, false, false, false, false),
	/** Weapon (switchable costs) */
	X(false, false, true, true, true, false, false, true, false, false)
	;
	
	private final boolean limited;
	private final boolean groupRankEditable;
	private final boolean weapon;
	private final boolean defaultProgression;
	private final boolean combined;
	private final boolean progressionBody;
	private final boolean progressionMagic;
	private final boolean costSwitchable;
	private final boolean magical;
	private final boolean ownRealm;
	
	
	private RankType(boolean limited, boolean combined, boolean groupRankEditable, boolean weapon,
			boolean defaultProgression, boolean progressionBody, boolean progressionMagic,
			boolean costSwitchable, boolean magical, boolean ownRealm) {
		this.limited = limited;
		this.combined = combined;
		this.groupRankEditable = groupRankEditable;
		this.weapon = weapon;
		this.defaultProgression = defaultProgression;
		this.progressionBody = progressionBody;
		this.progressionMagic = progressionMagic;
		this.costSwitchable = costSwitchable;
		this.magical = magical;
		this.ownRealm = ownRealm;
	}
	
	
	public boolean isLimited() {
		return limited;
	}
	
	public boolean isCombined() {
		return combined;
	}
	
	public boolean isGroupRankEditable() {
		return groupRankEditable;
	}
	
	public boolean isWeapon() {
		return weapon;
	}
	
	public boolean isDefaultProgression() {
		return defaultProgression;
	}
	
	public boolean isProgressionBody() {
		return progressionBody;
	}
	
	public boolean isProgressionMagic() {
		return progressionMagic;
	}
	
	public boolean isCostSwitchable() {
		return costSwitchable;
	}
	
	/**
	 * Marks the {@link RankType} as an own-realm type.
	 * 
	 * @return wthether it is an own realm spell category or not
	 */
	public boolean isOwnRealm() {
		return ownRealm;
	}
	
	/**
	 * Means average attribute bonus, special attribute resolution 
	 * 
	 * @return whether the rank is a magical or not
	 */
	public boolean isMagical() {
		return magical;
	}
}