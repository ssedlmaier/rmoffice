/*
 * Copyright 2018 Daniel Nettesheim
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
package net.sf.rmoffice.ui.components;

import javax.swing.JCheckBox;

class CheckboxItem<T> extends JCheckBox{
	private static final long serialVersionUID = 1L;
	
	private T object;
	
	public CheckboxItem (T object){
		this.object = object;
		this.setText(object.toString());
	}
	
	public T getObject() {
		return object;
	}
}
