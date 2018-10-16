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
package net.sf.rmoffice.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

/**
 * 
 */
public class AbstractPropertyChangeSupport {
    
	protected transient PropertyChangeSupport propertyChangeSupport;
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		initPropertyChangeSupport();
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		initPropertyChangeSupport();
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		initPropertyChangeSupport();
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		initPropertyChangeSupport();
		propertyChangeSupport.removePropertyChangeListener(propertyName,
				listener);
	}

	protected void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
		initPropertyChangeSupport();
		try {
			if (SwingUtilities.isEventDispatchThread()) {
				propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
			} else {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
					}
				});
			}
		} catch (InterruptedException e) {
		} catch (InvocationTargetException e) {
		}
	}
	
	private void initPropertyChangeSupport() {
		if (propertyChangeSupport == null) propertyChangeSupport = new PropertyChangeSupport(this);
	}

}