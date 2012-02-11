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
import net.sf.rmoffice.meta.enums.SpellUserType;
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.util.StatEnumList;


/**
 * 
 */
public class Profession {
	private Integer id;
	private String name;
	private String source;
	private String rune;
	private SpellUserType spellUserType = SpellUserType.NONE;
	private StatEnumList stats = new StatEnumList();
	private Map<Integer, Integer> skillgroupBonus = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> skillBonus = new HashMap<Integer, Integer>();
	private Map<Integer, SkillType> skillgroupType = new HashMap<Integer, SkillType>();
	private Map<Integer, SkillType> skillType = new HashMap<Integer, SkillType>();
	private List<String> additionInfo = new ArrayList<String>();
	
	/* package */ Profession() {
	}
	
	public Integer getId() {
		return id;
	}

	/* package */ void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	
	/* package */ void setName(String name) {
		this.name = name;
	}
	
	
	public String getSource() {
		return source;
	}

	
	/* package */ void setSource(String source) {
		this.source = source;
	}

	/**
	 * 
	 * @return a list of stat, not {@code null}
	 */
	public StatEnumList getStats() {
		return stats;
	}

	/* package */ void setStats(List<StatEnum> stats) {
		this.stats.clear();
		stats.addAll(stats);
	}
	
	public String getRune() {
		return rune;
	}

	/* package */ void setRune(String rune) {
		this.rune = rune;
	}
	
	/* package */ void addSkillgroupBonus(int id, int bonus) {
		skillgroupBonus.put(Integer.valueOf(id), Integer.valueOf(bonus));
	}
	
	public int getSkillgroupBonus(int id) {
		if (skillgroupBonus.containsKey(Integer.valueOf(id))) {
			return skillgroupBonus.get(Integer.valueOf(id)).intValue();
		}
		return 0;
	}
	
	/* package */ void addSkillBonus(int id, int bonus) {
		skillBonus.put(Integer.valueOf(id), Integer.valueOf(bonus));
	}
	
	public int getSkillBonus(int id) {
		if (skillBonus.containsKey(Integer.valueOf(id))) {
			return skillBonus.get(Integer.valueOf(id)).intValue();
		}
		return 0;
	}
	
	/* package */ void addSkillType(int id, SkillType type) {
		skillType.put(Integer.valueOf(id), type);
	}
	
	/**
	 * Returns the skill type from the profession. 
	 * 
	 * @param id the skill id
	 * @return the skill type or {@code null} if not available
	 */
	public SkillType getSkillType(int id) {
		if (skillType.containsKey(Integer.valueOf(id))) {
			return skillType.get(Integer.valueOf(id));
		}
		return null;
	}
	
	/* package */ void addSkillGroupType(int id, SkillType type) {
		skillgroupType.put(Integer.valueOf(id), type);
	}
	
	/**
	 * Returns the skill type of the skill group from the profession. 
	 * 
	 * @param id the skill id
	 * @return the skill type or {@code null} if not available
	 */
	public SkillType getSkillGroupType(int id) {
		if (skillgroupType.containsKey(Integer.valueOf(id))) {
			return skillgroupType.get(Integer.valueOf(id));
		}
		return null;
	}

	/**
	 * 
	 * @param addInfo localized string
	 */
	/* package private */ void addAdditionInfo(String addInfo) {
		additionInfo.add(addInfo);
	}
	
	public List<String> getAdditionInfo() {
		return additionInfo;
	}
	
	/**
	 * 
	 * @return the spell user type, not {@code null}
	 */
	public SpellUserType getSpellUserType() {
		return spellUserType;
	}

	/**
	 * 
	 * @param spellUserType the type, not {@code null}
	 */
	/* package private */ void setSpellUserType(SpellUserType spellUserType) {
		if (spellUserType == null) {
			throw new IllegalArgumentException("spellUserType must not be null");
		}
		this.spellUserType = spellUserType;
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
		Profession other = (Profession) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}
}
