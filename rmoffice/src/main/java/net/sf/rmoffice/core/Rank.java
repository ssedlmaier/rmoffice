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
package net.sf.rmoffice.core;

import java.math.BigDecimal;


/**
 * The association between a sheet and the skills/skill groups.
 */
public class Rank {
	private Integer id;
	private BigDecimal rank;	
	private Integer specialBonus;
	private Boolean favorite;
	
	/**
	 * 
	 */
	/* package private */ Rank(Integer id) {
		this.id = id;
		rank = BigDecimal.valueOf(0);
	}
	
	/**
	 * 
	 * @return the id of the skill or skill category
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * 
	 * 
	 * @param id the id of the skill or skill category
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * Returns the current rank.
	 * @return rank, not {@code null}
	 */
	public BigDecimal getRank() {
		if (rank == null) {
			return BigDecimal.valueOf(0);
		} else {
			return rank;
		}
	}
	
	/* package private */ void setRank(BigDecimal rank) {
		this.rank = rank;
	}

	/**
	 * 
	 * @return whether the underlying skill is marked as favorite or not, not {@code null}
	 */
	public Boolean getFavorite() {
		if (favorite == null) return Boolean.FALSE;
		return favorite;
	}
	
	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
	}
	
	/**
	 * 
	 * 
	 * @return the skills special bonus or {@code null} if none was set
	 */
	public Integer getSpecialBonus() {
		return specialBonus;
	}

	/* package private */ void setSpecialBonus(Integer specialBonus) {
		this.specialBonus = specialBonus;
	}
}
