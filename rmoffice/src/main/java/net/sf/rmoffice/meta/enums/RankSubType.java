/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.rmoffice.meta.enums;




/**
 * Sub type of the {@link RankType#Z}, {@link RankType#A} or {@link RankType#Y}.
 */
public enum RankSubType {
	
	O(SpelllistType.OPEN),
	C(SpelllistType.CLOSED),
	P(SpelllistType.PROFESSION),
	B(SpelllistType.BASE);
	
	private final int type;
	
	
	private RankSubType(int type) {
		this.type = type;
	}
	
	public boolean isOpen() {
		return type == SpelllistType.OPEN;
	}
	
	public boolean isClosed() {
		return type == SpelllistType.CLOSED;
	}
	
	public boolean isProfession() {
		return type == SpelllistType.PROFESSION;
	}
	
	public boolean isBase() {
		return type == SpelllistType.BASE;
	}
	
	public int getTypeOrdinal() {
		return type;
	}
}
