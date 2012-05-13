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

import java.util.regex.Pattern;

import net.sf.rmoffice.meta.talentflaw.ITalentFlawPart;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractPatternParser<T extends ITalentFlawPart> implements ITalentFlawPartParser<T> {
	protected final Pattern[] patternList;

	/**
	 * Alternative patterns. {@link #isParseable(String)} returns true if one matches.
	 * 
	 * @param pattern the patterns
	 */
	public AbstractPatternParser(final String... pattern) {
		patternList = new Pattern[pattern.length];
		for (int i=0; i<pattern.length; i++) {
			patternList[i] = Pattern.compile(pattern[i]);
		}

	}
	
	@Override
	public boolean isParseable(String toParse) {
		if (StringUtils.isEmpty(toParse)) {
			return false;
		}
		String trimmed = StringUtils.trim(toParse);
		for (Pattern p : patternList) {
			if (p.matcher(trimmed).matches()) {
				return true;
			}
		}
		return false;
	}

}