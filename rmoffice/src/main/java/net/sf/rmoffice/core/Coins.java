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

import java.util.ArrayList;
import java.util.List;


/**
 * The model for coins and jewelry.
 */
public class Coins extends AbstractPropertyChangeSupport {

	public final static String PROP_JUWELRY = "juwelry";
	public final static String PROP_MITHRIL = "mithril";
	public final static String PROP_PLATINUM = "platinum";
	public final static String PROP_GOLD = "gold";
	public final static String PROP_SILVER = "silver";
	public final static String PROP_BRONZE = "bronze";
	public final static String PROP_COPPER = "copper";
	public final static String PROP_TIN = "tin";
	public final static String PROP_IRON = "iron";
	
	private List<String> juwelry;
	private String mithril;
	private String platinum;
	private String gold;
	private String silver;
	private String bronze;
	private String copper;
	private String tin;
	private String iron;
	
	public List<String> getJuwelry() {
		checkJewelry();
		return juwelry;
	}

	/**
	 * 
	 */
	private void checkJewelry() {
		if (juwelry == null) {
			juwelry = new ArrayList<String>();
		}
		for (int i=juwelry.size() - 1; i<7;i++) {
			juwelry.add("");
		}
		while (juwelry.size() > 7) {
			juwelry.remove(juwelry.size() - 1);
		}
	}
	
	public void setJuwelry(List<String> juwelry) {
		List<String> oldVal = this.juwelry;
		this.juwelry = juwelry;		
		checkJewelry();
		firePropertyChange(PROP_JUWELRY, oldVal, this.juwelry);
	}
	
	public String getMithril() {
		return mithril;
	}
	
	public void setMithril(String mithril) {
		String oldVal = this.mithril;
		this.mithril = mithril;
		firePropertyChange(PROP_MITHRIL, oldVal, this.mithril);
	}
	
	public String getPlatinum() {
		return platinum;
	}
	
	public void setPlatinum(String platinum) {
		String oldVal = this.platinum;
		this.platinum = platinum;
		firePropertyChange(PROP_PLATINUM, oldVal, this.platinum);
	}
	
	public String getGold() {
		return gold;
	}
	
	public void setGold(String gold) {
		String oldVal = this.gold;
		this.gold = gold;
		firePropertyChange(PROP_GOLD, oldVal, this.gold);
	}
	
	public String getSilver() {
		return silver;
	}
	
	public void setSilver(String silver) {
		String oldVal = this.silver;
		this.silver = silver;
		firePropertyChange(PROP_SILVER, oldVal, this.silver);
	}
	
	public String getBronze() {
		return bronze;
	}
	
	public void setBronze(String bronze) {
		String oldVal = this.bronze;
		this.bronze = bronze;
		firePropertyChange(PROP_BRONZE, oldVal, this.bronze);
	}
	
	public String getCopper() {
		return copper;
	}
	
	public void setCopper(String copper) {
		String oldVal = this.copper;
		this.copper = copper;
		firePropertyChange(PROP_COPPER, oldVal, this.copper);
	}
	
	public String getTin() {
		return tin;
	}
	
	public void setTin(String tin) {
		String oldVal = this.tin;
		this.tin = tin;
		firePropertyChange(PROP_TIN, oldVal, this.tin);
	}
	
	public String getIron() {
		return iron;
	}
	
	public void setIron(String iron) {
		String oldVal = this.iron;
		this.iron = iron;
		firePropertyChange(PROP_IRON, oldVal, this.iron);
	} 
 
	
}
