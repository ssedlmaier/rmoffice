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

import net.sf.rmoffice.meta.talentflaw.ExhaustionPart;

public class ExhaustionParser extends AbstractKeyFloatValueParser<ExhaustionPart> {
	private static final String KEY = "EXHAUSTION";
	
	public ExhaustionParser() {
		super(KEY);
	}

	@Override
	protected ExhaustionPart createPart(float value) {
		return new ExhaustionPart(value);
	}

}
