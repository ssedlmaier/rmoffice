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
package net.sf.rmoffice.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.meta.enums.StatEnum;


/**
 * The description of a development package.
 */
public class TrainPack {
	public static enum Type { L, V } 
	
	private Integer id;
	private String name;
	private int aquireMonth;
	private String source;
	private String startMoney;
	private Type type;
	private List<String> todos = new ArrayList<String>();
	private Map<Integer, Integer> skills = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> skillsGroups = new HashMap<Integer, Integer>();
	private Map<Integer, SkillType> skillTypes = new HashMap<Integer, SkillType>();
	private Map<Integer, SkillType> skillgroupTypes = new HashMap<Integer, SkillType>();
	private List<List<StatEnum>> statGains = new ArrayList<List<StatEnum>>();
	
	/**
	 * @param id the unique id of the development package
	 * @param name the name of development package
	 * @param aquireMonth the time to aquire in month
	 * @param source the source book
	 * @param startMoney the start money
	 * @param type the type of the package or {@code null}
	 */
	TrainPack(Integer id, String name, int aquireMonth, String source, String startMoney, Type type) {		
		super();
		this.id = id;
		this.name = name;
		this.aquireMonth = aquireMonth;
		this.source = source;
		this.startMoney = startMoney;
		this.type = type;
	}
	
	
	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getAquireMonth() {
		return aquireMonth;
	}
	
	public String getSource() {
		return source;
	}
	
	public String getStartMoney() {
		return startMoney;
	}
	
	public Type getType() {
		return type;
	}
	
	public List<String> getTodos() {
		return todos;
	}
	/**
	 * 
	 * @param todo the localized string
	 */
	/* package protected*/ void addTodo(String todo) {
		todos.add(todo);
	}
	
	
	public Map<Integer, Integer> getSkills() {
		return skills;
	}
	
	/* package private*/ void addSkill(int id, int ranks) {
		skills.put(Integer.valueOf(id), Integer.valueOf(ranks));
	}
	
	public Map<Integer, Integer> getSkillsGroups() {
		return skillsGroups;
	}
	
	/* package private */ void addSkillGroup(int id, int ranks) {
		skillsGroups.put(Integer.valueOf(id), Integer.valueOf(ranks));
	}
	
	public Map<Integer, SkillType> getSkillTypes() {
		return skillTypes;
	}
	
	/*
	 * To be added to the {@link RMSheet} if selected.
	 * 
	 * @param skillId the skillId
	 * @param type the skill type
	 */
	/* package private */ void addSkillType(int skillId, SkillType type) {
		skillTypes.put(Integer.valueOf(skillId), type);
	}
	
	public Map<Integer, SkillType> getSkillgroupTypes() {
		return skillgroupTypes;
	}
	
	/*
	 * To be added to the {@link RMSheet} if selected.
	 * 
	 * @param skillgroupId the skillId
	 * @param type the skill group type
	 */
	/* package private */ void addSkillgroupType(int skillId, SkillType type) {
		skillgroupTypes.put(Integer.valueOf(skillId), type);
	}
	
	public List<List<StatEnum>> getStatGains() {
		return statGains;
	}
	
	/* package private */ void addStatGain(List<StatEnum> choices) {
		statGains.add(choices);
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return name;
	}


	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		TrainPack other = (TrainPack) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}
	
	
}
