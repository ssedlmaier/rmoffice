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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.rmoffice.generator.Name.Style;
import net.sf.rmoffice.meta.enums.RaceScope;
import net.sf.rmoffice.meta.enums.ResistanceEnum;
import net.sf.rmoffice.meta.enums.StatEnum;



/**
 * The race meta data loaded from configuration.
 */
public class Race {
	private Integer id;
	private RaceScope scope;
	private String name;
	private String source;
	private Style nameStyle;
	private String outline;
	private int backgroundOptions;
	private Map<StatEnum, Integer> attributes = new HashMap<StatEnum, Integer>();
	private Map<ResistanceEnum, Integer> resistances = new HashMap<ResistanceEnum, Integer>();
	private IProgression progKoerperentw;
	private Map<StatEnum, Progression> progMagie = new HashMap<StatEnum, Progression>();
	private int soulDeparture;
	private float recoveryMultiplier;
    private List<String> additionalResistanceLines = new ArrayList<String>();
	private int exhaustionPoints;
	
	/* package */ Race() {
	}
	
	public Integer getId() {
		return id;
	}

	/* package */ void setId(Integer id) {
		this.id = id;
	}
	
	
	/* package */ void setScope(RaceScope scope) {
		this.scope = scope;
	}

	public RaceScope getScope() {
		return scope;
	}

	public String getName() {
		return name;
	}
	
	/* package */ void setName(String name) {
		this.name = name;
	}

	public int getStatBonus(StatEnum attribute) {
		if (attributes.containsKey(attribute)) {
			return attributes.get(attribute).intValue();
		}
		return 0;
	}
	
	/* package */ void setAttributeBonus(StatEnum attribute, Integer value) {
		attributes.put(attribute, value);
	}
	
	public int getResistanceBonus(ResistanceEnum resistance) {
		if (resistances.containsKey(resistance)) {
			return resistances.get(resistance).intValue();
		}
		return 0;
	}
	
	/* package */ void setResistanceBonus(ResistanceEnum resistance, Integer value) {
		resistances.put(resistance, value);
	}
	
	public IProgression getProgKoerperentw() {
		return progKoerperentw;
	}

	/* package */ void setProgKoerperentw(IProgression progKoerperentw) {
		this.progKoerperentw = progKoerperentw;
	}

	public Progression getProgMagic(StatEnum at) {
		if ( ! at.isForMagic()) throw new IllegalArgumentException("Attribute "+at+" is not valid for magical progression");
		return progMagie.get(at);
	}

	/* package */ void setProgMagic(StatEnum at, Progression prog) {
		if ( ! at.isForMagic()) throw new IllegalArgumentException("Attribute "+at+" is not valid for magical progression");
		progMagie.put(at, prog);
	}
	
	public int getSoulDeparture() {
		return soulDeparture;
	}
	
	/* package */ void setSoulDeparture(int soulDeparture) {
		this.soulDeparture = soulDeparture;
	}
	
	public float getRecoveryMultiplier() {
		return recoveryMultiplier;
	}
	
	/* package */ void setRecoveryMultiplier(float recoveryMultiplier) {
		this.recoveryMultiplier = recoveryMultiplier;
	}
	
	/**
	 * Returns the style of the character name. The is used as default
	 * for character name generation.
	 * 
	 * @return the style of the races names
	 */
	public Style getNameStyle() {
		return nameStyle;
	}
	
	/**
	 * Sets the default for character name generation. 
	 * 
	 * @param nameStyle
	 */
	/* package */ void setNameStyle(Style nameStyle) {
		this.nameStyle = nameStyle;
	}
	
	
	/**
	 * Returns an unmodifiable list of additional resistance lines.
	 * 
	 * @return list of resistance lines, not {@code null}
	 */
	public List<String> getAdditionalResistanceLines() {
		return Collections.unmodifiableList(additionalResistanceLines);
	}
	
	/* package private */  void addAdditionalResistanceLine(String resLine) {
		this.additionalResistanceLines.add(resLine);
	}
	
	
	public int getBackgroundOptions() {
		return backgroundOptions;
	}

	
	/* package private */ void setBackgroundOptions(int backgroundOptions) {
		this.backgroundOptions = backgroundOptions;
	}

	/* package */ void setExhaustionPoints(int exhaustionPoints) {
		this.exhaustionPoints = exhaustionPoints;
	}

	public int getExhaustionPoints() {
		return exhaustionPoints;
	}
	
	
	/* package */ void setSource(String source) {
		this.source = source;
	}
	/**
	 * 
	 * @return the source book string or {@code null}
	 */
	public String getSource() {
		return source;
	}
	

	/**
	 * 
	 * @return the outline base name
	 */
	public String getOutline() {
		return outline;
	}

	
	/* package */ void setOutline(String outline) {
		this.outline = outline;
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}


	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Race other = (Race) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}
}
