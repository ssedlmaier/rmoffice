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
package net.sf.rmoffice.ui.converters;

import net.sf.rmoffice.core.RMSheet;

import org.apache.commons.lang.StringUtils;

import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;

/**
 * Converter for different formats (foot/inch format or cm) in the ui.
 **/
public class LengthFormatToIntegerConverter extends AbstractConverter {

	private final BeanAdapter<RMSheet> sheetAdapter;

	public LengthFormatToIntegerConverter(ValueModel subject, BeanAdapter<RMSheet> sheetAdapter) {
		super(subject);
		this.sheetAdapter = sheetAdapter;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void setValue(Object newValue) {
		/* value from UI */
		if (newValue != null && StringUtils.trimToNull((String)newValue) != null) {
			try {
				String s = newValue.toString();
				int parsedValue = sheetAdapter.getBean().getLengthUnit().parseInt(s);
				subject.setValue(Integer.valueOf(parsedValue));
			} catch (Exception e) {
				subject.setValue(Integer.valueOf(0));
			}
		} else {
			subject.setValue(Integer.valueOf(0));
		}
	}

	@Override
	public Object convertFromSubject(Object subjectValue) {
		/* value from model */
		if (subjectValue == null) return "";
		return sheetAdapter.getBean().getLengthUnit().getFormattedString( ((Number)subjectValue).intValue() );
	}

}
