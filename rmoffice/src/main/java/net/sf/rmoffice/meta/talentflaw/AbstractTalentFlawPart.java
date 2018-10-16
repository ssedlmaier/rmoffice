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
package net.sf.rmoffice.meta.talentflaw;

import java.text.NumberFormat;
import java.util.ResourceBundle;

import net.sf.rmoffice.meta.enums.SkillType;

public abstract class AbstractTalentFlawPart implements ITalentFlawPart {

	protected static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
	protected StringBuilder appendBreak(StringBuilder sb) {
		if (sb.length() > 0) {
			sb.append("\n");
		}
		return sb;
	}
	
	protected void appendBonusLine(StringBuilder sb, String name, Integer value) {
		appendBreak(sb).append(name).append(" ").append(format(value.floatValue()));
	}

	protected void appendTypeLine(StringBuilder sb, String name, SkillType value) {
		appendBreak(sb).append(name).append(": ").append(RESOURCE.getString("SkillType."+value.name()));
	}
	
	public static String format(float number) {
		int f100 = Math.round( 100f * number );
		if (f100 > 0) {
			return "+" + NumberFormat.getNumberInstance().format(number);
		} else if (f100 == 0) {
			return "+0";
		}
		return NumberFormat.getNumberInstance().format(number);
	}
}
