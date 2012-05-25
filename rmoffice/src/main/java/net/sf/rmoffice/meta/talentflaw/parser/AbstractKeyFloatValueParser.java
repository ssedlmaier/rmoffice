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

import net.sf.rmoffice.meta.talentflaw.ITalentFlawPart;

/**
 * Key-Value parser where value is a float.
 *
 * @param <T> the sub-class of {@link ITalentFlawPart}
 */
public abstract class AbstractKeyFloatValueParser<T extends ITalentFlawPart> extends AbstractPatternParser<T> {
	private static final String PATTERN = "=[0-9\\.-]+";
	
	/**
	 * 
	 * @param key the key part, not {@code null}
	 */
	public AbstractKeyFloatValueParser(String key) {
		super(key + PATTERN);
	}
	
	@Override
	protected T createPart(String key, String[] valueParts) {
		processKey(key);
		float factor = Float.parseFloat(valueParts[0]);
		return createPart(factor);
	}

	protected abstract T createPart(float value);
	
	/**
	 * Overwrite to process the key part in sub-classes.
	 * 
	 * @param key the key string, not {@code null} and trimmed
	 */
	protected void processKey(String key) {}
}
