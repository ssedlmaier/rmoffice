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

import java.awt.Color;


/**
 * 
 */
public enum RaceScope {
	BASE(Color.black, Color.lightGray),
	SHADOWWORLD(Color.white, Color.darkGray),
	MIDDLEEARTH(Color.black, Color.yellow);

	private final Color fgColor;
	private final Color bgColor;
	
	private RaceScope(Color fgColor, Color bgColor) {
		this.fgColor = fgColor;
		this.bgColor = bgColor;
	}
	
	public Color getForeground() {
		return fgColor;
	}
	public Color getBackground() {
		return bgColor;
	}
}
