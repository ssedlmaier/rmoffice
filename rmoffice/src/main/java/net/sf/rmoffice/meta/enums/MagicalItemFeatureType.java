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

import java.util.ResourceBundle;

import net.sf.rmoffice.core.items.MagicalFeature;


/**
 * immutable type of any {@link MagicalFeature}.
 */
public enum MagicalItemFeatureType {
	SKILL(true),
	RESISTANCE(true),
	STAT(true),
	DESCRIPTION(false)
	/* TODO DB(Shield) */
	;
	
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	private final boolean bonusAvailable;
	private String name;

	private MagicalItemFeatureType(boolean bonusAvailable) {
		this.bonusAvailable = bonusAvailable;
	}
	
	/**
	 * 
	 * @return whether the bonus is for this type available and used.
	 */
	public boolean isBonusAvailable() {
		return bonusAvailable;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		if (name == null) {
			name = RESOURCE.getString("MagicalItemFeatureType."+name());
		}
		return name;
	}
}
