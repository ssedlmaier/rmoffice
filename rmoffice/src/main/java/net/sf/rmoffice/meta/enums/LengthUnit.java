/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.rmoffice.meta.enums;

import java.text.DecimalFormat;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * All available length units.
 */
public enum LengthUnit {
    /** Base unit */
	CM(1),
	/** 1 inch = 2,54 cm */
	INCH(2.54f);
	
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private static final DecimalFormat WITH_SCALE_0x = new DecimalFormat("0.##");
	private static final DecimalFormat WITH_SCALE_00 = new DecimalFormat("0.00");
	private static final DecimalFormat WITHOUT_SCALE = new DecimalFormat("0");
	private final float factor;

	private LengthUnit(float factor) {
		this.factor = factor;
	}
	
	public int asBaseUnit(int value) {
		return Math.round(value * factor);
	}
	
	/**
	 * 
	 * @return the factor, not {@code null}
	 */
	public float getFactor() {
		return factor;
	}
	
	/**
	 * Converts the given value to the given length unit. Returns the given value
     * if toUnit is {@code null}.
	 * 
	 * @param value the length value
	 * @param toUnit the destination unit, may be {@code null}
	 * @return the converted value
	 */
	public int convertTo(int value, LengthUnit toUnit) {
		if (toUnit == null || toUnit.equals(this)) {
			return value;
		}
		return Math.round(value * getFactor() / toUnit.getFactor());
	}

	public String getFormattedString(int value) {
		if (CM.equals(this) && value > 100) {
			StringBuilder sb = new StringBuilder();
			if ( value % 100 == 0) {
				sb.append(WITH_SCALE_0x.format(value/100f));
			} else {
				sb.append(WITH_SCALE_00.format(value/100f));
			}
			sb.append(" ");
			sb.append(RESOURCE.getString("LengthUnit.METER"));
			return sb.toString();
		}
		if (INCH.equals(this)) {
			/* 1 foot = 12 inch */
			int inches = value % 12;
			int feet = (value - inches) / 12;
			/* f" i' */
			return feet + "\" " + inches + "'";
		}
		return WITHOUT_SCALE.format(value) + " " + RESOURCE.getString("LengthUnit."+name());
	}

	/** 
	 * Parses and returns the value string to integer.
	 * 
	 * @throws NumberFormatException on parse errors 
	 */
	public int parseInt(String valueStr) {
		int retVal = 0;
		if (valueStr != null) {
			if (CM.equals(this)) {
				int cmIdx = valueStr.toLowerCase().indexOf("cm");
				int mIdx = valueStr.toLowerCase().indexOf("m");
				int decCharIdx = Math.max(valueStr.toLowerCase().indexOf(","), valueStr.toLowerCase().indexOf("."));
				if (cmIdx > -1) {
					retVal = Integer.parseInt(StringUtils.trimToEmpty(valueStr.substring(0, cmIdx)));
				} else if (mIdx > -1 || decCharIdx > -1) {
					String s = mIdx > -1 ? valueStr.substring(0, mIdx) : valueStr;
					s = StringUtils.trimToEmpty(s);
					s = StringUtils.replace(s, ",", ".");
					retVal = Math.round(100 * Float.parseFloat(s));
				} else {
					retVal = Integer.parseInt(StringUtils.trimToEmpty(valueStr));
				}
			} else {
				int idx = valueStr.indexOf('"');
				String[] parts = StringUtils.split(valueStr, "\"'");
				if (parts.length == 1) {
					retVal = NumberUtils.toInt(parts[0]);
					if (idx > -1) {
						// we have only feet
						retVal *= 12;
					} 
				} else if (parts.length > 1) {
					retVal = 12 * NumberUtils.toInt(StringUtils.trimToEmpty(parts[0]));
					retVal += NumberUtils.toInt(StringUtils.trimToEmpty(parts[1]));
				}
			}
		}
		return retVal;
	}
}
