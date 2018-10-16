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

import net.sf.rmoffice.meta.talentflaw.ITalentFlawPart;

/**
 * This interface provides meta data loading functions.
 */
public interface ITalentFlawPartParser<T extends ITalentFlawPart> {

	/**
	 * Returns whether the given string is parseable or not.
	 * 
	 * @param toParse the string to parse, may be {@code null}
	 * @return whether the string is parseable or not
	 */
	public boolean isParseable(String toParse);
	
	/**
	 * Returns a new instance of a {@link ITalentFlawPart}. This method
	 * is called only if {@link #isParseable(String)} returns <code>true</code>.
	 * 
	 * @param parseableString the parseable string
	 * @return a {@link ITalentFlawPart}, not {@code null}
	 */
	public T parse(String parseableString);
}
