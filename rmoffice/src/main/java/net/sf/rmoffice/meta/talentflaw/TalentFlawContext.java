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

import java.awt.Frame;

import net.sf.rmoffice.core.RMSheet;

public class TalentFlawContext {
	
	private final Frame owner;
	private final RMSheet sheet;

	public TalentFlawContext(Frame owner, RMSheet sheet) {
		this.owner = owner;
		this.sheet = sheet;
	}
	
	/**
	 * Returns the UI owner for UI interactions.
	 * 
	 * @return owner for UI interactions, not {@code null}
	 */
	public Frame getOwner() {
		return owner;
	}
	
	/**
	 * Returns the {@link RMSheet}.
	 * 
	 * @return the sheet, not {@code null}
	 */
	public RMSheet getSheet() {
		return sheet;
	}
}
