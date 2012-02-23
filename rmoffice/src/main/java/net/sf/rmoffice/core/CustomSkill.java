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
package net.sf.rmoffice.core;

import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.enums.LengthUnit;
import net.sf.rmoffice.meta.enums.RaceScope;
import net.sf.rmoffice.meta.enums.SkillType;


/**
 * A skill that has a modified name.
 */
/* package private */ class CustomSkill implements ISkill {

	
	private final Integer inheritSkillId;
	private final boolean isSpellList;
	private final Integer id;
	private String name;
	private SkillType type;

	/* for Java 7 xStream deserializing behavior */
	/* package private */ CustomSkill() {
		inheritSkillId = Integer.valueOf(0);
		isSpellList = false;
		id = Integer.valueOf(-9999);
	}
	
	/**
	 * 
	 * @param inheritSkill
	 * @param id
	 * @param newName
	 * @param type or {@code null} to inherit from inheritSkill
	 */
	public CustomSkill(ISkill inheritSkill, Integer id, String newName, SkillType type) {
		this.type = type;
		this.isSpellList = inheritSkill.isSpelllist();
		this.inheritSkillId = inheritSkill.getId();
		this.id = id;
		name = newName;
	}
	
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * To modify the name.
	 * 
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription(LengthUnit lengthUnit) {
		return "";
	}

	@Override
	public boolean isSpelllist() {
		return isSpellList;
	}
	
	public Integer getActualSkillId() {
		return inheritSkillId;
	}

	@Override
	public RaceScope getScope() {
		/* must be null, because this custom skill should be handled everywhere */
		return null;
	}

	/**
	 * 
	 * @return the overwritten {@link SkillType} or {@code null}
	 */
	public SkillType getType() {
		return type;
	}
	
	/**
	 * To modify the skill type.
	 * 
	 * @param type the new skill type, not {@code null}
	 */
	public void setType(SkillType type) {
		this.type = type;
	}
	
	@Override
	public String getSource() {
		return "";
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return name;
	}
}
