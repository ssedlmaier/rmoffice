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
package net.sf.rmoffice.meta.talentflaw;

import java.text.NumberFormat;

public abstract class KeyValuePart extends AbstractTalentFlawPart {

	protected final float value;

	public KeyValuePart(float value) {
		super();
		this.value = value;
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