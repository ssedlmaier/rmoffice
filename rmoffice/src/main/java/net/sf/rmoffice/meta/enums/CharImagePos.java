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

public enum CharImagePos {
	NO_IMAGE(false, false),
	PAGE1(true, false),
	PAGE_EQUIPMENT(false, true),
	PAGE1_AND_EQUIPMENT(true, true);
	
	
	private final boolean page1;
	private final boolean pageEquipment;
	
	private CharImagePos(boolean page1, boolean pageEquipment) {
		this.page1 = page1;
		this.pageEquipment = pageEquipment;
	}

	public boolean isPage1() {
		return page1;
	}

	public boolean isPageEquipment() {
		return pageEquipment;
	}
}
