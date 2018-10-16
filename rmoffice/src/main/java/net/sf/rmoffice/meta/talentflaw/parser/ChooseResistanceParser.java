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
package net.sf.rmoffice.meta.talentflaw.parser;

import net.sf.rmoffice.meta.enums.ResistanceEnum;
import net.sf.rmoffice.meta.talentflaw.ChooseResistancePart;

import org.apache.commons.lang.StringUtils;

public class ChooseResistanceParser extends AbstractPatternParser<ChooseResistancePart> {
	private static final String KEY = "CHOOSERR";
	private static final String PATTERN = KEY + "([0-9]+)=.+=([0-9-]+)=*([0-9-]*)";

	public ChooseResistanceParser() {
		super(PATTERN);
	}
	
	@Override
	protected ChooseResistancePart createPart(String key, String[] valueParts) {
		int amount = Integer.parseInt(key.substring(KEY.length()));
		int bonus = Integer.parseInt(valueParts[1]);
		int spellBonus = 0;
		if (valueParts.length > 2) {
			spellBonus = Integer.parseInt(valueParts[2]);
		}
		String[] selectableStrings = StringUtils.splitByWholeSeparator(valueParts[0], ";");
		ResistanceEnum[] selectables = new ResistanceEnum[selectableStrings.length];
		for (int i=0; i<selectableStrings.length; i++) {
			selectables[i] = ResistanceEnum.valueOf(StringUtils.trimToEmpty(selectableStrings[i]));
		}
		return createPart(amount, bonus, spellBonus, selectables);
	}

	protected ChooseResistancePart createPart(int amount, int bonus, int spellBonus, ResistanceEnum... selectables) {
		return new ChooseResistancePart(amount, bonus, spellBonus, selectables);
	}

}
