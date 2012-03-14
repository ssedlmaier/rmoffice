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
package net.sf.rmoffice.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.rmoffice.meta.enums.StatEnum;


/**
 * 
 */
public class StatEnumList extends ArrayList<StatEnum> {
	private static final long serialVersionUID = 1L;
	private final static List<StatEnum> magicalStats;
	static {
		List<StatEnum> stats = new ArrayList<StatEnum>();
		for (StatEnum stat : StatEnum.values()) {
			if (stat.isForMagic()) {
				stats.add(stat);
			}
		}
		magicalStats = Collections.unmodifiableList(stats);
	}
	/**
	 * The list is arcane if there are at least all the magical stats are included. There may be more others.
	 * 
	 * @return whether the list contains all magical stats or not
	 */
	public boolean isArcane() {
		return containsAll(magicalStats);
	}
}
