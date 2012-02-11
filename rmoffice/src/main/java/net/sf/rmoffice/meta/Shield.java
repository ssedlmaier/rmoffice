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


/**
 * 
 */
public class Shield {
	private int id;
	private String name;
	private int closeBonus;
	private int rangeBonus;
	private String displayName;
	
	/* package private */ Shield() {
	}

	public int getId() {
		return id;
	}

	/* package private */ void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	
	/* package private */ void setName(String name) {
		this.name = name;
	}

	
	public int getCloseBonus() {
		return closeBonus;
	}

	
	/* package private */ void setCloseBonus(int closeBonus) {
		this.closeBonus = closeBonus;
	}

	
	public int getRangeBonus() {
		return rangeBonus;
	}

	
	/* package private */ void setRangeBonus(int rangeBonus) {
		this.rangeBonus = rangeBonus;
	}	
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		if (displayName == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(name).append(" (");
			sb.append("+"+closeBonus).append("/+"+rangeBonus);
			sb.append(")");
			displayName = sb.toString();
		}
		return displayName;
	}
	
}
