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

import net.sf.rmoffice.meta.talentflaw.ProgressionPart;

import org.apache.commons.lang.StringUtils;

public class ProgressionParser extends AbstractPatternParser<ProgressionPart> {
	private static final String VALUE_PATTERN = "([0-9-]+);([0-9-]+);([0-9-]+);([0-9-]+);([0-9-]+)";
	private static final String BODYDEV = "BODYDEV";
	private static final String POWERDEV = "POWERDEV";
	private static final String PARSEABLE_PATTERN = "("+BODYDEV+"|"+POWERDEV+")=" + VALUE_PATTERN;
	
	public ProgressionParser() {
		super(PARSEABLE_PATTERN);
	}

	@Override
	protected ProgressionPart createPart(String key, String[] valueParts) {
		String[] enumeration = StringUtils.split(valueParts[0], ';');
		int[] progMods = new int[5];
		for (int i=0; i<5; i++) {
			progMods[i] = Integer.parseInt(enumeration[i]);
		}
		return new ProgressionPart(progMods, key.startsWith(BODYDEV));
	}

}
