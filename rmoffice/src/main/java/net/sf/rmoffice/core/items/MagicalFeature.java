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
package net.sf.rmoffice.core.items;

import com.jgoodies.binding.beans.BeanAdapter;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.meta.enums.MagicalItemFeatureType;
import net.sf.rmoffice.meta.enums.ResistanceEnum;
import net.sf.rmoffice.meta.enums.StatEnum;


/**
 * The basic magical feature with a type, description, bonus and optional ID and stat.
 * For bonus calculation only the types {@link MagicalItemFeatureType#SKILL}, {@link MagicalItemFeatureType#RESISTANCE}
 * and {@link MagicalItemFeatureType#STAT} are used.
 */
public class MagicalFeature {
	private transient BeanAdapter<RMSheet> rmSheetAdapter; /* do not persist */
	private MagicalItemFeatureType type = MagicalItemFeatureType.DESCRIPTION;
	private int bonus;
	private String description;
	private Integer id; 
	private StatEnum stat;
	private ResistanceEnum resistance;
	
	/* for Java 7 xStream deserializing behavior */
	/* package private */ MagicalFeature() {
	}
	
	/**
	 * @param rmSheetAdapter the sheet adapter to inform about bonus relevant changes
	 */
	public MagicalFeature(BeanAdapter<RMSheet> rmSheetAdapter) {
		this.rmSheetAdapter = rmSheetAdapter;
	}
	
	public void init(BeanAdapter<RMSheet> rmSheetAdapter) {
		this.rmSheetAdapter = rmSheetAdapter;
	}

	/**
	 * 
	 * 
	 * @return the bonus, not {@code null}
	 */
	public Integer getBonus() {
		return Integer.valueOf(bonus);
	}
	
	public void setBonus(Integer bonus) {
		if (bonus == null) {
			this.bonus = 0;
		} else {
			this.bonus = bonus.intValue();
		}
		rmSheetAdapter.getBean().notifyItemBonusChanged();
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	
	public MagicalItemFeatureType getType() {
		return type;
	}

	
	public void setType(MagicalItemFeatureType type) {
		MagicalItemFeatureType oldValue = this.type;
		if (type == null) {
			this.type = MagicalItemFeatureType.DESCRIPTION;
		} else {
			this.type = type;
		}
		if (! this.type.equals(oldValue)) {
			setDescription("");
			setId(null);
		}
		rmSheetAdapter.getBean().notifyItemBonusChanged();
	}

	
	public Integer getId() {
		return id;
	}

	
	public void setId(Integer id) {
		this.id = id;
		rmSheetAdapter.getBean().notifyItemBonusChanged();
	}
	
	public void setInternalId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return the {@link StatEnum} or {@code null}
	 */
	public StatEnum getStat() {
		return stat;
	}

	
	public void setStat(StatEnum stat) {
		this.stat = stat;
		rmSheetAdapter.getBean().notifyItemBonusChanged();
	}

	/**
	 * 
	 * @return the {@link ResistanceEnum} or {@code null}
	 */
	public ResistanceEnum getResistance() {
		return resistance;
	}
	
	
	public void setResistance(ResistanceEnum resistance) {
		this.resistance = resistance;
		rmSheetAdapter.getBean().notifyItemBonusChanged();
	}
}
