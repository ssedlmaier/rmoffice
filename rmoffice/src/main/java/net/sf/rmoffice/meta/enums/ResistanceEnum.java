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

import java.util.ResourceBundle;



/**
 * All available resistance types.
 */
public enum ResistanceEnum {
	CHANNELING(StatEnum.INTUITION),
	ESSENCE(StatEnum.EMPATHY),
	MENTALISM(StatEnum.PRESENCE),
	POISON(StatEnum.CONSTITUTION),
	DISEASE(StatEnum.CONSTITUTION),
	FEAR(StatEnum.SELFDISCIPLINE);
	
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	private final StatEnum stat;

	private ResistanceEnum(StatEnum stat) {
		this.stat = stat;
	}
	
	public StatEnum getStat() {
		return stat;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return RESOURCE.getString("ResistanceEnum."+name());
	}
}
