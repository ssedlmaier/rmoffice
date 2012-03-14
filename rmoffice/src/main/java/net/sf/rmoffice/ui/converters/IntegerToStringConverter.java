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
package net.sf.rmoffice.ui.converters;

import org.apache.commons.lang.StringUtils;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;


/**
 * 
 */
public class IntegerToStringConverter extends AbstractConverter {

	private static final long serialVersionUID = 1L;

	/**
	 * @param subject
	 */
	public IntegerToStringConverter(ValueModel subject) {
		super(subject);
	}

	/** {@inheritDoc} */
	@Override
	public void setValue(Object newValue) {
		/* value from UI */
		if (newValue != null && StringUtils.trimToNull((String)newValue) != null) {
			try {
				subject.setValue(Integer.valueOf((String)newValue));
			} catch (Exception e) {
				subject.setValue(subject.getValue());
			}
		} else {
			subject.setValue(Integer.valueOf(0));
		}
	}

	/** {@inheritDoc} */
	@Override
	public Object convertFromSubject(Object subjectValue) {
		/* value from model */
		if (subjectValue == null) return "";
		return subjectValue.toString();
	}

}
