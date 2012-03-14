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


/**
 * 
 */
public class Equipment {
	private String description;
	private String place;
	/* to be converted to weightFloat since 4.2.4 */ Integer weight;
	private float weightFloat;
	private boolean favorite;
	private boolean carried;
	
	
	/**
	 * Default constructor. 
	 */
	public Equipment() {
	}
	
	/**
	 * creates a new {@link Equipment}.
	 * @param description
	 * @param place
	 * @param weight 
	 * @param favorite 
	 * @param carried
	 */
	public Equipment(String description, String place, float weight, boolean favorite, boolean carried) {
		super();
		this.description = description;
		this.place = place;
		this.weightFloat = weight;
		this.favorite = favorite;
		this.carried = carried;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPlace() {
		return place;
	}
	
	public void setPlace(String place) {
		this.place = place;
	}
	
	/**
	 * Returns the weight in grams.
	 * 
	 * @return the weights
	 */
	public float getWeight() {
		return weightFloat;
	}
	
	/**
	 * Sets the weight in gram.
	 * 
	 * @param weight the weight in gram
	 */
	public void setWeight(float weight) {
		this.weightFloat = weight;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	
	public boolean isCarried() {
		return carried;
	}
	
	public void setCarried(boolean carried) {
		this.carried = carried;
	} 
}
