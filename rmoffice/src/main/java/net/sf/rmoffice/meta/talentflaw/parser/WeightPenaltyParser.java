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

import net.sf.rmoffice.meta.talentflaw.WeightPenaltyPart;

import org.apache.commons.lang.StringUtils;

public class WeightPenaltyParser extends AbstractPatternParser<WeightPenaltyPart> {
	private static final String PATTERN = "WEIGHTPENALTY=[0-9\\.-]+";
	
	public WeightPenaltyParser() {
		super(PATTERN);
	}

	@Override
	public WeightPenaltyPart parse(String parseableString) {
		String trimmed = StringUtils.trimToEmpty(parseableString);
		String[] parts = StringUtils.splitPreserveAllTokens(trimmed.substring(1), "=");
		float factor = Float.parseFloat(StringUtils.trim(parts[1]));
		return new WeightPenaltyPart(factor);
	}

}
