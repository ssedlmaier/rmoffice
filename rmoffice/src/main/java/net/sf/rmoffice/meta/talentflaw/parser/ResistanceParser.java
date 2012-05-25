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

import java.util.ResourceBundle;

import net.sf.rmoffice.meta.talentflaw.ResistancePart;

public class ResistanceParser extends AbstractPatternParser<ResistancePart> {
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private static final String PATTERN = "RR=.+";
	
	public ResistanceParser() {
		super(PATTERN);
	}

	@Override
	protected ResistancePart createPart(String key, String[] valueParts) {
		return new ResistancePart(RESOURCE.getString(valueParts[0]));
	}

}
