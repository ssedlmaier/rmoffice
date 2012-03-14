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
package net.sf.rmoffice.ui.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.sf.rmoffice.core.AbstractPropertyChangeSupport;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.meta.Progression;

import com.jgoodies.binding.beans.BeanAdapter;


/**
 * 
 */
public class ProgressionValueModelAdapter extends AbstractPropertyChangeSupport implements PropertyChangeListener {
	private final BeanAdapter<RMSheet> rmSheetAdapter;
	private Progression progression;

	public ProgressionValueModelAdapter(BeanAdapter<RMSheet> rmSheetAdapter, String property) {
		this.rmSheetAdapter = rmSheetAdapter;
		this.rmSheetAdapter.addBeanPropertyChangeListener(property, this);
	}
	
	public String getDigit0() {
		return getDigit(0);
	}

	public String getDigit1() {
		return getDigit(1);
	}
	
	public String getDigit2() {
		return getDigit(2);
	}
	
	public String getDigit3() {
		return getDigit(3);
	}
	
	public String getDigit4() {
		return getDigit(4);
	}
	
	private String getDigit(int digit) {
		String retVal = "";
		if (progression != null) {
			retVal = ""+progression.getDigit(digit);
		}
		return retVal;
	}

	/** {@inheritDoc} */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		progression = ((Progression) evt.getNewValue());
		for (int i=0; i < 5; i++) {
			firePropertyChange("digit"+i, null, getDigit(i));
		}
	}

}
