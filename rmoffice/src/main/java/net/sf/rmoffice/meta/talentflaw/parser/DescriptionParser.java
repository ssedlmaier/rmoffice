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

import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import net.sf.rmoffice.meta.talentflaw.DescriptionPart;

public class DescriptionParser implements ITalentFlawPartParser<DescriptionPart> {
	private static final String DESCR = "DESCR";
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	static final String COPYRIGHT_DESCR_KEY = "talent.descr.copyright";
	
	@Override
	public boolean isParseable(String toParse) {
		if (StringUtils.isEmpty(toParse)) {
			return false;
		}
		return StringUtils.trim(toParse).startsWith(DESCR);
	}

	@Override
	public DescriptionPart parse(String parseableString) {
		String trimmed = StringUtils.trim(parseableString);
		if (DESCR.equals(trimmed)) {
			return new DescriptionPart(RESOURCE.getString(COPYRIGHT_DESCR_KEY));
		}
		return new DescriptionPart(RESOURCE.getString(trimmed.substring(DESCR.length() + 1)));
	}
}
