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
package net.sf.rmoffice.core;

import org.apache.commons.lang.StringUtils;



/**
 * 
 */
public class Characteristics extends AbstractPropertyChangeSupport {
	
	public final static String PROP_CLOTH_SIZE = "clothSize";
	public static final String PROP_CHILDREN = "children";
	public static final String PROP_SPOUSE = "spouse";
	public static final String PROP_SIBLINGS = "siblings";
	public static final String PROP_PARENT = "parent";
	public static final String PROP_LORD = "lord";
	public static final String PROP_DEITY = "deity";
	public static final String PROP_HOME_TOWN = "homeTown";
	public static final String PROP_NATIONALITY = "nationality";
	public static final String PROP_ALIGNMENT = "alignment";
	public static final String PROP_MOTIVATION = "motivation";
	public static final String PROP_PERSONALITY = "personality";
	public static final String PROP_EYE = "eyeColor";
	public static final String PROP_HAIR = "hairColor";
	public static final String PROP_WEIGHT = "weight";
	public static final String PROP_HEIGHT_CM = "height";
	public static final String PROP_SKIN = "skin";
	public static final String PROP_AGE = "age";
	public static final String PROP_AGE_APPEAR = "apparentlyAge";
	public static final String PROP_DEMEANOR = "demeanor";
	public static final String PROP_APPEARANCE = "appearance";
	public static final String PROP_IS_FEMALE = "female";
	public static final String PROP_HAT_SIZE = "hatSize";
	public static final String PROP_SHOE_SIZE = "shoeSize";
	public static final String PROP_MISC1 = "misc1";
	public static final String PROP_MISC2 = "misc2";
	public static final String PROP_MISC3 = "misc3";
	public static final String PROP_MISC4 = "misc4";
	public static final String PROP_CHARIMAGE = "charImage";

	
	private int appearance;
	private String demeanor;
	private String apparentlyAge;
	private int age;
	private boolean isFemale;
	private String taint; /* for compatibility named as taint (should be skin) */
	private int heightCm; /* for compatibility named as heightCm (should be height) */
	/* the field weight is deprecated since 4.1.0 */
	String weight;
	private int weightInt;
	private String hairColor;
	private String eyeColor;
	private String personality;
	private String motivation;
	private String attitude;
	
	private String nationality;
	private String homeTown;
	private String god;
	private String lord;
	private String parent;
	private String spouse;
	private String siblings;
	private String children;
	/* this field is for version 4.0.1 to 4.0.3 format and will be converted to misc1-misc4*/
	private String misc;
	private String misc1;
	private String misc2;
	private String misc3;
	private String misc4;
	
	private String clothSize;
	private String hatSize;
	private String shoeSize;
	private byte[] charImage;
	
	/**
	 * Called after a new RMSHeet was deserialized.
	 * This updates fields if needed.
	 */
	public void initialize() {
		/* convert msic -> since 4.0.4 we have misc1-4*/
		if (StringUtils.trimToNull(misc) != null) {
			String[] part = StringUtils.split(misc, "\n");
		    if (part != null && part.length > 0) {
		    	setMisc1(part[0]);
		    }
		    if (part != null && part.length > 1) {
		    	setMisc2(part[1]);
		    }
		    if (part != null && part.length > 2) {
		    	setMisc3(part[2]);
		    }
		    if (part != null && part.length > 3) {
		    	setMisc4(part[3]);
		    }
		}
	}
	
	public int getAppearance() {
		return appearance;
	}
	
	public void setAppearance(int appearance) {
		Object oldValue = Integer.valueOf(this.appearance);
		this.appearance = appearance;
		firePropertyChange(PROP_APPEARANCE, oldValue, Integer.valueOf(appearance));
	}
	
	public String getDemeanor() {
		return demeanor;
	}
	
	public void setDemeanor(String behavior) {
		Object oldValue = this.demeanor; 
		this.demeanor = behavior;
		firePropertyChange(PROP_DEMEANOR, oldValue, behavior);
	}
	
	public String getApparentlyAge() {
		return apparentlyAge;
	}
	
	public void setApparentlyAge(String apparentlyAge) {
		Object oldValue = this.apparentlyAge;
		this.apparentlyAge = apparentlyAge;
		firePropertyChange(PROP_AGE_APPEAR, oldValue, apparentlyAge);
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		Object oldValue = Integer.valueOf(this.age);
		this.age = age;
		firePropertyChange(PROP_AGE, oldValue, Integer.valueOf(age));
	}
	
	public boolean isFemale() {
		return isFemale;
	}
	
	public void setFemale(boolean isFemale) {
		Object oldValue = Boolean.valueOf(this.isFemale);
		this.isFemale = isFemale;
		firePropertyChange(PROP_IS_FEMALE, oldValue, Boolean.valueOf(isFemale));
	}
	
	public String getSkin() {
		return taint;
	}
	
	public void setSkin(String skin) {
		Object oldValue = this.taint;
		this.taint = skin;
		firePropertyChange(PROP_SKIN, oldValue, skin);
	}
	
	/**
	 * Returns the value in the adjusted unit.
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return heightCm;
	}
	
	/**
	 * 
	 * @param height the height
	 */
	public void setHeight(int height) {
		Object oldValue = Integer.valueOf(this.heightCm);
		this.heightCm = height;
		firePropertyChange(PROP_HEIGHT_CM, oldValue, Integer.valueOf(height));
	}
	
	public int getWeight() {
		return weightInt;
	}

	public void setWeight(int weight) {
		Object oldValue = Integer.valueOf(this.weightInt);
		this.weightInt = weight;
		firePropertyChange(PROP_WEIGHT, oldValue, Integer.valueOf(weight));
	}
	
	public String getHairColor() {
		return hairColor;
	}
	
	public void setHairColor(String hairColor) {
		Object oldValue = this.hairColor;
		this.hairColor = hairColor;
		firePropertyChange(PROP_HAIR, oldValue, hairColor);
	}
	
	public String getEyeColor() {
		return eyeColor;
	}
	
	public void setEyeColor(String eyeColor) {
		Object oldValue = this.eyeColor;
		this.eyeColor = eyeColor;
		firePropertyChange(PROP_EYE, oldValue, eyeColor);
	}
	
	public String getPersonality() {
		return personality;
	}
	
	public void setPersonality(String personality) {
		Object oldValue = this.personality;
		this.personality = personality;
		firePropertyChange(PROP_PERSONALITY, oldValue, personality);
	}
	
	public String getMotivation() {
		return motivation;
	}
	
	public void setMotivation(String motivation) {
		Object oldValue = this.motivation;
		this.motivation = motivation;
		firePropertyChange(PROP_MOTIVATION, oldValue, motivation);
	}
	
	public String getAlignment() {
		return attitude;
	}
	
	public void setAlignment(String alignment) {
		Object oldValue = this.attitude;
		this.attitude = alignment;
		firePropertyChange(PROP_ALIGNMENT, oldValue, alignment);
	}
	
	public String getNationality() {
		return nationality;
	}
	
	public void setNationality(String nationality) {
		Object oldValue = this.nationality;
		this.nationality = nationality;
		firePropertyChange(PROP_NATIONALITY, oldValue, nationality);
	}
	
	public String getHomeTown() {
		return homeTown;
	}

	
	public void setHomeTown(String homeTown) {
		Object oldValue = this.homeTown;
		this.homeTown = homeTown;
		firePropertyChange(PROP_HOME_TOWN, oldValue, homeTown);
	}

	
	public String getDeity() {
		return god;
	}

	
	public void setDeity(String deity) {
		Object oldValue = this.god;
		this.god = deity;
		firePropertyChange(PROP_DEITY, oldValue, deity);
	}

	
	public String getLord() {
		return lord;
	}

	
	public void setLord(String lord) {
		Object oldValue = this.lord;
		this.lord = lord;
		firePropertyChange(PROP_LORD, oldValue, lord);
	}

	
	public String getParent() {
		return parent;
	}

	
	public void setParent(String parent) {
		Object oldValue = this.parent;
		this.parent = parent;
		firePropertyChange(PROP_PARENT, oldValue, parent);
	}

	public String getSpouse() {
		return spouse;
	}

	
	public void setSpouse(String spouse) {
		Object oldValue = this.spouse;
		this.spouse = spouse;
		firePropertyChange(PROP_SPOUSE, oldValue, spouse);
	}
	
	public String getSiblings() {
		return siblings;
	}

	
	public void setSiblings(String siblings) {
		Object oldValue = this.siblings;
		this.siblings = siblings;
		firePropertyChange(PROP_SIBLINGS, oldValue, siblings);
	}

	
	public String getChildren() {
		return children;
	}

	
	public void setChildren(String children) {
		String oldValue = this.children;
		this.children = children;
		firePropertyChange(PROP_CHILDREN, oldValue, children);
	}

	public String getClothSize() {
		return clothSize;
	}
	
	public void setClothSize(String clothSize) {
		String oldValue = this.clothSize;
		this.clothSize = clothSize;
		firePropertyChange(PROP_CLOTH_SIZE, oldValue, clothSize);
	}
	
	public String getHatSize() {
		return hatSize;
	}

	
	public void setHatSize(String hatSize) {
		Object oldValue = this.hatSize;
		this.hatSize = hatSize;
		firePropertyChange(PROP_HAT_SIZE, oldValue, hatSize);
	}

	
	public String getShoeSize() {
		return shoeSize;
	}

	
	public void setShoeSize(String shoeSize) {
		Object oldValue = this.shoeSize;
		this.shoeSize = shoeSize;
		firePropertyChange(PROP_SHOE_SIZE, oldValue, shoeSize);
	}

	public String getMisc1() {
		return misc1;
	}
	
	public void setMisc1(String misc) {
		Object oldValue = this.misc1;
		this.misc1 = misc;
		firePropertyChange(PROP_MISC1, oldValue, misc);
	}

	public String getMisc2() {
		return misc2;
	}
	
	public void setMisc2(String misc) {
		Object oldValue = this.misc2;
		this.misc2 = misc;
		firePropertyChange(PROP_MISC2, oldValue, misc);
	}
	
	public String getMisc3() {
		return misc3;
	}
	
	public void setMisc3(String misc) {
		Object oldValue = this.misc3;
		this.misc3 = misc;
		firePropertyChange(PROP_MISC3, oldValue, misc);
	}
	
	public String getMisc4() {
		return misc4;
	}
	
	public void setMisc4(String misc) {
		Object oldValue = this.misc4;
		this.misc4 = misc;
		firePropertyChange(PROP_MISC4, oldValue, misc);
	}

	
	public byte[] getCharImage() {
		return charImage;
	}

	
	public void setCharImage(byte[] charImage) {
		this.charImage = charImage;
		firePropertyChange(PROP_CHARIMAGE, null, charImage);
	}
	
	
}
