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

import java.util.List;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.meta.enums.RankSubType;
import net.sf.rmoffice.meta.enums.RankType;
import net.sf.rmoffice.meta.enums.StatEnum;




/**
 * skill category (skill group is wrong translation).
 */
public class SkillCategory {
	private Integer id;
	private String name;
	private RankType rankType;
	private RankSubType rankSubType;
	private List<StatEnum> attributes;
	
	
	/* package */ SkillCategory() {
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
	
	/**
	 * Do not use this directly. Use {@link RMSheet#getSkillcategoryStats(SkillCategory)}
	 * 
	 * @return the meta data attributes
	 */
	public List<StatEnum> getAttributes() {
		return attributes;
	}
	
	/* package */ void setAttributes(List<StatEnum> attributes) {
		this.attributes = attributes;
	}
	
	public RankType getRankType() {
		return rankType;
	}
	
	/* package */ void setRankType(RankType rankType) {
		this.rankType = rankType;
	}
	
	public RankSubType getRankSubType() {
		return rankSubType;
	}
	
	/* package */  void setRankSubType(RankSubType rankSubType) {
		this.rankSubType = rankSubType;
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
		SkillCategory other = (SkillCategory) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}
	
	
}
