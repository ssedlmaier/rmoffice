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
package net.sf.rmoffice.meta.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 */
public class SpelllistType {	
	public final static int OPEN = 1;
	public final static int CLOSED = 2;
	public final static int PROFESSION = 4;
	public final static int BASE = 8;
	public final static int TRAINING = 16;
	
	private final int type;
	private final boolean evil;
	private final List<Integer> professionIds;
	private final Integer trainingPackageSpelllistId;
	
	/*package*/SpelllistType(int type, boolean evil, List<Integer> professionIds, Integer trainingPackageSpelllistId) {
		super();
		switch (type) {
			case OPEN: this.type = OPEN;break;
			case CLOSED: this.type = CLOSED;break;
			case PROFESSION: this.type = PROFESSION;break;
			case TRAINING: this.type = TRAINING;break;
			default:
				throw new IllegalArgumentException("Not supported type");
		}
		this.evil = evil;
		this.professionIds = professionIds;
		this.trainingPackageSpelllistId = trainingPackageSpelllistId;
	}
	
	public boolean isOpen() {
		return type == OPEN;
	}
	
	public boolean isClosed() {
		return type == CLOSED;
	}
	
	public boolean isProfession() {
		return type == PROFESSION;
	}
	
	public boolean isTrainingPackage() {
		return type == TRAINING;
	}
	
	public boolean isEvil() {
		return evil;
	}
	
	public int getTypeOrdinal() {
		return type;
	}
	
	/**
	 * 
	 * 
	 * @return a list with profession Ids; not {@code null}
	 */
	public List<Integer> getProfessionIds() {
		return professionIds;
	}
	
	/**
	 * 
	 * @return the id of the trainingpackage spell list that this list contains to
	 */
	public Integer getTrainingPackageSpelllistId() {
		return trainingPackageSpelllistId;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((professionIds == null) ? 0 : professionIds.hashCode());
		result = prime * result + ((trainingPackageSpelllistId == null) ? 0 : trainingPackageSpelllistId.hashCode());
		result = prime * result + type;
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		SpelllistType other = (SpelllistType) obj;
		if (type != other.type) return false;
		if (professionIds == null) {
			if (other.professionIds != null) return false;
		} else {
			if (other.professionIds == null) return false;
			if (professionIds.size() != other.professionIds.size()) return false;
			if (! professionIds.containsAll(other.professionIds)) {
				return false;
			}
		}
		if (type == TRAINING) {
			if (other.trainingPackageSpelllistId != null) {
				if (! other.trainingPackageSpelllistId.equals(trainingPackageSpelllistId)) {
					return false;
				} 
			} else {
				// no id is always different
				return false;
			}
		}
		return true;
	}
	
	public static class Builder {
		private static Map<Integer, SpelllistType> cache = new HashMap<Integer, SpelllistType>();
		private int type = 0;
		private boolean evil;
		private List<StatEnum> attributes = new ArrayList<StatEnum>();
		private List<Integer> professionIds  = new ArrayList<Integer>();
		private Integer trainingPackageSpelllistId;
		
		public Builder() {
		}
		
		public Builder setOpen() {
			if (type != 0 && this.type != OPEN) throw new IllegalArgumentException("You can only set one as type: OPEN, CLOSED or PROFESSION");
			this.type = OPEN;
			return this;
		}
		public Builder setClosed() {
			if (type != 0 && this.type != CLOSED) throw new IllegalArgumentException("You can only set one as type: OPEN, CLOSED or PROFESSION");
			this.type = CLOSED;
			return this;
		}
		public Builder setProf() {
			if (type != 0 && this.type != PROFESSION) throw new IllegalArgumentException("You can only set one as type: OPEN, CLOSED or PROFESSION");
			this.type = PROFESSION;
			return this;
		}
		
		public void setTrainingpack() {
			if (type != 0 && this.type != TRAINING) throw new IllegalArgumentException("You can only set one as type: OPEN, CLOSED or PROFESSION");
			this.type = TRAINING;
		}
		
		public Builder setEvil(boolean evil) {
			this.evil = evil;
			return this;
		}
		public Builder addAttribute(StatEnum attr) {
			if (attributes == null) {
				attributes = new ArrayList<StatEnum>();
			}
			attributes.add(attr);
			return this;
		}
		
		public Builder addProfessionId(Integer id) {
			if (professionIds == null) {
				professionIds = new ArrayList<Integer>();
			}
			professionIds.add(id);
			return this;
		}
		
		public Builder setTraingpackageSpelllist(Integer id) {
			trainingPackageSpelllistId = id;
			return this;
		}
		
		public SpelllistType build() {
			SpelllistType tempType = new SpelllistType(type, evil, Collections.unmodifiableList(professionIds), trainingPackageSpelllistId);
			synchronized (cache) {
				if (cache.containsKey(Integer.valueOf(tempType.hashCode()))) {
					SpelllistType spelllistType = cache.get(Integer.valueOf(tempType.hashCode()));
					return spelllistType;
				} else {
					cache.put(Integer.valueOf(tempType.hashCode()), tempType);
					return tempType;
				}
			}
		}

	}
}
