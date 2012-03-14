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
package net.sf.rmoffice.meta;




/**
 * 
 */
public class Skillcost implements Comparable<Skillcost>{
	private final int[] costs;
	private final String name;
	
	public Skillcost(int... costs) {
		this.costs = costs;
		StringBuilder sb = new StringBuilder();
		for (int c : costs) {
			if (sb.length() > 0) {
				sb.append("/");
			}
			sb.append(""+c);			
		}
		this.name = sb.toString();
	}
	
	/**
	 * Returns the costs for the given index beginning with 0.
	 * 
	 * @param idx less than {@link #size()}
	 * @return the costs
	 */
	public int getCost(int idx) {
		return costs[idx];
	}
	
	public int size() {
		if (costs == null) return 0;
		return costs.length;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int compareTo(Skillcost other) {
		int maxSize = Math.max(size(), other.size());
		for (int i = 0; i < maxSize; i++) {
			if (size() > i) {
				if (other.size() > i) {
					if (costs[i] != other.costs[i]) {
						return Integer.valueOf(costs[i]).compareTo(Integer.valueOf(other.costs[i]));
					}
				} else {
					return 1;
				}
			} else {
				return -1;
			}
		}
		return 0;
	}
}
