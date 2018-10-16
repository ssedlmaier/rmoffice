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

import java.util.ArrayList;
import java.util.List;

import net.sf.rmoffice.core.AbstractPropertyChangeSupport;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.enums.MagicalItemFeatureType;
import net.sf.rmoffice.meta.enums.ResistanceEnum;
import net.sf.rmoffice.pdf.AbstractPDFCreator;

import com.jgoodies.binding.beans.BeanAdapter;


/**
 * An item for internal use in {@link RMSheet}. It represents a magical item with
 * several magical features like +10 Swordsword or +1 Agility. 
 */
public class MagicalItem extends AbstractPropertyChangeSupport {
	
	public static final String PROP_NAME = "name";
	public static final String PROP_FAVORITE = "favorite";
	public static final String PROP_FEATURES = "features";
	private String name;
	private List<MagicalFeature> features = new ArrayList<MagicalFeature>();
	private Boolean favorite;
	private transient BeanAdapter<RMSheet> rmSheetAdapter; 
	
	/* for Java 7 xStream deserializing behavior */
	/* package private */ MagicalItem() {
	}
	
	/**
	 * @param rmSheetAdapter 
	 * 
	 */
	public MagicalItem(BeanAdapter<RMSheet> rmSheetAdapter) {
		this.rmSheetAdapter = rmSheetAdapter;
		this.favorite = Boolean.TRUE;
	}
	
	public void init(BeanAdapter<RMSheet> adapter) {
		rmSheetAdapter = adapter;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name or short description of the item.
	 * 
	 * @param name the name or short description
	 */
	public void setName(String name) {
		String oldValue = this.name;
		this.name = name;
		firePropertyChange(PROP_NAME, oldValue, this.name);
	}
	
	public Boolean getFavorite() {
		return favorite;
	}

	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
		rmSheetAdapter.getBean().notifyItemBonusChanged();
	}

	/**
	 * Sets a new list if {@link MagicalFeature}s.
	 * 
	 * @param features
	 */
	public void setFeatures(List<MagicalFeature> features) {
		List<MagicalFeature> oldValue = this.features;
		this.features = features;
		firePropertyChange(PROP_FEATURES, oldValue, features);
	}
	
	/**
	 * Returns anlist of {@link MagicalFeature}s that are added to this magical item.
	 * 
	 * @return list of {@link MagicalFeature}s
	 */
	public List<MagicalFeature> getFeatures() {
		if (features == null) {
			features = new ArrayList<MagicalFeature>();
		}
		return features;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getName();
	}

	/**
	 * 
	 * @param skill
	 * @return whether one of the features is skill modifying for given skill 
	 */
	public boolean hasSkillModifier(ISkill skill) {
		for (MagicalFeature feat : getFeatures()) {
			if (MagicalItemFeatureType.SKILL.equals(feat.getType()) && skill.getId().equals(feat.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param res
	 * @return whether one of the features is resistance modifying for given resistance
	 */
	public boolean hasResistanceModifier(ResistanceEnum res) {
		for (MagicalFeature feat : getFeatures()) {
			if (MagicalItemFeatureType.RESISTANCE.equals(feat.getType()) && res.equals(feat.getResistance())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param res
	 * @return whether one of the features is DB modifying
	 */
	public boolean hasDBModifier() {
		for (MagicalFeature feat : getFeatures()) {
			if (MagicalItemFeatureType.DB.equals(feat.getType())) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Returns the item with all features as one string line.
	 * @param index the index of the item
	 * 
	 * @return item as string
	 */
	public String asOneLine(int index) {
		StringBuilder sb = new StringBuilder();
		sb.append(""+index).append(")");
		sb.append(getName()).append(": ");
		for (int j=0; j < features.size(); j++) {
			MagicalFeature feat = features.get(j);
			if (j > 0) {
				sb.append(", ");
			}
			sb.append(feat.getDescription()).append(" ");
			if (feat.getType().isBonusAvailable() && feat.getBonus() != null) {
				sb.append(AbstractPDFCreator.format(feat.getBonus().intValue(), false));
			}
		}
		return sb.toString();
	}
}
