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
package net.sf.rmoffice.meta.enums;

import java.text.DecimalFormat;
import java.util.ResourceBundle;

/**
 * All available units for weight.
 */
public enum WeightUnit {
	/** 1 pound = 0,453 592 370 kg (exact, per definitionem) */
	LBS(0.45359237f), 
	/** 1 kg = 1000g */
	KILOGRAM(1f);

	public static final WeightUnit BASE_UNIT = KILOGRAM;
	
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private static final DecimalFormat WITH_SCALE = new DecimalFormat("0.###");
	private static final DecimalFormat WITHOUT_SCALE = new DecimalFormat("0");
	private final float factor;

	private WeightUnit(float factor) {
		this.factor = factor;
	}
	
	/**
	 * Returns the weight value as {@link WeightUnit#KILOGRAM}.
	 * 
	 * @param value the weight value
	 * @return the weight
	 */
	public float asBaseUnit(float value) {
		return value * factor;
	}
	
	/**
	 * Returns the factor to the base unit.
	 * 
	 * @return factor, not <code>null</code>
	 */
	public float getFactor() {
		return this.factor;
	}

	/**
	 * Returns the weight as human readable formatted string.
	 * 
	 * @return weight string
	 */
	public String getFormattedString(float weight, boolean ext) {
		if (weight <= 0) {
			return "";
		}
		if ( KILOGRAM.equals(this) ) {
			if (weight < 1) {
				String s = WITHOUT_SCALE.format( weight * 1000 );
				if (ext) {
					s += " "+ RESOURCE.getString("WeightUnit.GRAM");
				}
				return s;
			}
		}
		String s = WITH_SCALE.format(weight);
		if (ext) {
			s += " "+RESOURCE.getString("WeightUnit."+name());
		}
		return s;		
	}
	/**
	 * Returns the weight as human readable formatted string.
	 * 
	 * @return weight string
	 */
	public String getFormattedString(float weight) {
		return getFormattedString(weight, true);
	}

	/**
	 * Converts the given value to the given weight unit. Returns the given value
	 * if toUnit is {@code null}.
	 * 
	 * @param value the weight value
	 * @param toUnit the destination unit, may be {@code null}
	 * @return the converted value
	 */
	public float convertTo(float value, WeightUnit toUnit) {
		if (toUnit == null || toUnit.equals(this)) {
			return value;
		}
		return value * getFactor() / toUnit.getFactor();
	}
	
}
