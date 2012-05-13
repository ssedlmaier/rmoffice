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
package net.sf.rmoffice.meta.talentflaw.parser;

import java.util.regex.Matcher;

import net.sf.rmoffice.meta.talentflaw.ProgressionPart;

import org.apache.commons.lang.StringUtils;

public class ProgressionParser extends AbstractPatternParser<ProgressionPart> {
	private static final String BODYDEV = "BODYDEV";
	private static final String POWERDEV = "POWERDEV";
	private static final String PARSEABLE_PATTERN = "("+BODYDEV+"|"+POWERDEV+")=([0-9-]+);([0-9-]+);([0-9-]+);([0-9-]+);([0-9-]+)";
	
	public ProgressionParser() {
		super(PARSEABLE_PATTERN);
	}

	@Override
	public ProgressionPart parse(String parseableString) {
		Matcher matcher = patternList[0].matcher(StringUtils.trimToEmpty(parseableString));
		if (! matcher.matches()) {
			throw new IllegalArgumentException("parseableString must be parseable: "+parseableString);
		}
		boolean bodyOrPower = BODYDEV.equals(matcher.group(1));
		int[] progMods = new int[5];
		for (int i=0; i<5; i++) {
			progMods[i] = Integer.parseInt(matcher.group(i+2));
		}
		return new ProgressionPart(progMods, bodyOrPower);
	}

}
