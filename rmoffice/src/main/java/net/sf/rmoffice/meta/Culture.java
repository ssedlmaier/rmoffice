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
package net.sf.rmoffice.meta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.rmoffice.core.ToDo;
import net.sf.rmoffice.meta.enums.SkillType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 */
public class Culture {
	private final static Logger log = LoggerFactory.getLogger(Culture.class);
	
	private Integer id;
	private String name;
	private String rune;
	private Map<SkillCategory, Integer> youthGroupRanks = new HashMap<SkillCategory, Integer>();
	private Map<ISkill, Integer> youthSkillRanks = new HashMap<ISkill, Integer>();
	private Map<Integer, SkillType> skillTypes = new HashMap<Integer, SkillType>();
	private Map<SkillCategory, SkillType> skillgroupTypes = new HashMap<SkillCategory, SkillType>();
	private List<Race> races = new ArrayList<Race>();
	private WeightHeight weightHeight;
	private int languageRanks;
	private int hobbyRanks;
	private int openSpellRanks;

	private List<String> todos = new ArrayList<String>();
	
	public Integer getId() {
		return id;
	}
	
	/* package private*/ void setId(Integer id) {
		this.id = id;
	}

	/* package private*/ void addYouthRank(SkillCategory group, int youthrank) {
		if (youthGroupRanks.containsKey(group)) {
			if (log.isWarnEnabled()) log.warn("Race "+getId()+": youth rank skillgroup "+group.getId()+" was added, yet. Ignoring second value.");
		} else {
			youthGroupRanks.put(group, Integer.valueOf(youthrank));
		}
	}
	
	/* package private*/ void addYouthRank(ISkill skill, int youthrank) {
		if (youthSkillRanks.containsKey(skill)) {
			if (log.isWarnEnabled()) log.warn("Race "+getId()+": youth rank skill "+skill.getId()+" was added, yet. Ignoring second value.");
		} else {
			youthSkillRanks.put(skill, Integer.valueOf(youthrank));
		}
	}
	
	public int getYouthRank(SkillCategory group) {
		if (youthGroupRanks.containsKey(group)) {
			return youthGroupRanks.get(group).intValue();
		} else {
			return 0;
		}
	}
	
	public int getYouthRank(ISkill group) {
		if (youthSkillRanks.containsKey(group)) {
			return youthSkillRanks.get(group).intValue();
		} else {
			return 0;
		}
	}
	/**
	 * 
	 * @return an unmodifiable collection of {@link SkillCategory}s that have a youth rank
	 */
	public Collection<SkillCategory> getYouthSkillgroups() {
		return Collections.unmodifiableCollection(youthGroupRanks.keySet());
	}
	
	/**
	 * 
	 * @return an unmodifiable collection of {@link Skill}s that have a youth rank
	 */
	public Collection<ISkill> getYouthSkills() {
		return Collections.unmodifiableCollection(youthSkillRanks.keySet());
	}
	
	/* package */ void addValidRace(Race race) {
		races.add(race);
	}
	
	public List<Race> getValidRaces() {
		return Collections.unmodifiableList(races);
	}

	/**
	 * Sets the name of the culture.
	 * @param name the new name
	 */
	/* package */ void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of the culture.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * The rune of the culture.
	 * 
	 * @return the rune base name
	 */
	public String getRune() {
		return rune;
	}

	/**
	 * 
	 * @param rune the bae rune name
	 */
	/* package */ void setRune(String rune) {
		this.rune = rune;
	}

	/**
	 * Returns the list of message to be added as {@link ToDo} if this culture was selected.
	 * 
	 * @return the list of todo messages
	 */
	public List<String> getTodos() {
		return Collections.unmodifiableList(todos);
	}
	
	/**
	 * 
	 * @param string
	 */
	/* package */ void addTodo(String string) {
		todos.add(string);
	}
	
	/**
	 * 
	 * @param skill not {@code null}
	 * @param skillType not {@code null}
	 */
	/* package*/ void addSkillType(ISkill skill, SkillType skillType) {
		skillTypes.put(skill.getId(), skillType);
	}
	
	/**
	 * Returns the type of the skill.
	 * 
	 * @param skillId the skill id
	 * @return type of the skill or {@code null} if not available
	 */
	public SkillType getSkillType(int skillId) {
		if (skillTypes.containsKey(Integer.valueOf(skillId))) {
			return skillTypes.get(Integer.valueOf(skillId));
		}
		return null;		
	}
	
	/**
	 * 
	 * @param skillgroup
	 * @param skillType
	 */
	/* package*/ void addSkillgroupType(SkillCategory skillgroup, SkillType skillType) {
		skillgroupTypes.put(skillgroup, skillType);
	}

	/**
	 * Returns the type of the skills in the given skill group.
	 * 
	 * @param skillgroup the skill group
	 * @return type of all skill or {@code null} if not available
	 */
	public SkillType getSkillgroupType(SkillCategory skillgroup) {
		if (skillgroupTypes.containsKey(skillgroup)) {
			return skillgroupTypes.get(skillgroup);
		}
		return null;		
	}
	
	
	
	/**
	 * 
	 * @return the weight and height metrics for this culture.
	 */
	public WeightHeight getWeightHeight() {
		return weightHeight;
	}

	
	/* package */ void setWeightHeight(WeightHeight weightHeight) {
		this.weightHeight = weightHeight;
	}
	
	public int getLanguageRanks() {
		return languageRanks;
	}
	
	/* package */  void setLanguageRanks(int languageRanks) {
		this.languageRanks = languageRanks;
	}
	
	public int getHobbyRanks() {
		return hobbyRanks;
	}
	
	/* package */  void setHobbyRanks(int hobbyRanks) {
		this.hobbyRanks = hobbyRanks;
	}
	
	public int getOpenSpellRanks() {
		return openSpellRanks;
	}
	
	/* package */  void setOpenSpellRanks(int openSpellRanks) {
		this.openSpellRanks = openSpellRanks;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getName();
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
		Culture other = (Culture) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}


}
