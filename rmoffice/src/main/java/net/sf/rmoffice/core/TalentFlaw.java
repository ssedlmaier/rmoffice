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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.rmoffice.meta.IProgression;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.Skillcost;
import net.sf.rmoffice.meta.enums.LengthUnit;
import net.sf.rmoffice.meta.enums.ResistanceEnum;
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.meta.enums.TalentFlawLevel;
import net.sf.rmoffice.meta.enums.TalentFlawType;
import net.sf.rmoffice.meta.talentflaw.BaseMoveRatePart;
import net.sf.rmoffice.meta.talentflaw.BasemoverateMultiplierPart;
import net.sf.rmoffice.meta.talentflaw.DBPart;
import net.sf.rmoffice.meta.talentflaw.ExhaustionMultiplierPart;
import net.sf.rmoffice.meta.talentflaw.ExhaustionPart;
import net.sf.rmoffice.meta.talentflaw.InitiativePart;
import net.sf.rmoffice.meta.talentflaw.ProgressionPart;
import net.sf.rmoffice.meta.talentflaw.RecoveryPart;
import net.sf.rmoffice.meta.talentflaw.ResistancePart;
import net.sf.rmoffice.meta.talentflaw.ShieldDBPart;
import net.sf.rmoffice.meta.talentflaw.SnapBonusPart;
import net.sf.rmoffice.meta.talentflaw.SouldeparturePart;
import net.sf.rmoffice.meta.talentflaw.TolerancePart;
import net.sf.rmoffice.meta.talentflaw.WeightPenaltyPart;
import net.sf.rmoffice.pdf.PDFCreator;

import org.apache.commons.lang.StringUtils;

import com.jgoodies.binding.beans.Model;

/**
 * Sheet data: Talent or flaw with one level of the talent.
 */
public class TalentFlaw extends Model {
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private static final long serialVersionUID = 1L;
	private static final String ID_PROP = "id";
	private static final String NAME_PROP = "name";
	
	private Integer id;
	private String name;
	private String source;
	private TalentFlawType type;
	private TalentFlawLevel level;
	private int costs;
	private Integer initiativeBonus;
	private Integer snapBonus;
	private String description;
	private Map<Integer, Integer> skillCatBonus;
	private Map<Integer, SkillType> skillCatType;
	private Map<Integer, Integer> skillBonus;
	private Map<Integer, SkillType> skillType;
    private IProgression progressionBody;
    private IProgression progressionPower;
	private Float weightPenalty; 
	private Float baseMovement; 
	private List<String> resistanceLines;
    private Integer db;
    private Integer shieldDb;
	private Integer exhaustion;
	private Map<StatEnum, Integer> statBonus;
	private Float recoveryMultiplier;
	private Float exhaustionMultiplier;
	private Float basemoverateMultiplier;
	private Float tolerance;
	private Map<ResistanceEnum, Integer> resBonus;
	private Map<Integer, Skillcost> skillCostReplacement;
	private Map<Integer, Skillcost> skillCategoryCostReplacement;
	private HashMap<StatEnum, Integer> spellRealmBonus;
	private Integer souldeparture;
	
	public TalentFlaw() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		Object oldValue = this.id;
		this.id = id;
		firePropertyChange(ID_PROP, oldValue, this.id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		Object oldValue = this.name;
		this.name = name;
		firePropertyChange(NAME_PROP, oldValue, this.name);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public TalentFlawType getType() {
		return type;
	}

	public void setType(TalentFlawType type) {
		this.type = type;
	}

	public TalentFlawLevel getLevel() {
		return level;
	}

	public void setLevel(TalentFlawLevel level) {
		this.level = level;
	}

	public int getCosts() {
		return costs;
	}

	public void setCosts(int costs) {
		this.costs = costs;
	}

	public Integer getInitiativeBonus() {
		return initiativeBonus;
	}

	public void setInitiativeBonus(Integer initiativeBonus) {
		this.initiativeBonus = initiativeBonus;
	}
	
	public Integer getSnapBonus() {
		return snapBonus;
	}
	
	public void setSnapBonus(Integer snapBonus) {
		this.snapBonus = snapBonus;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void addDescription(String newDescription) {
		if (this.description == null) {
			this.description = newDescription;
		} else {
			this.description += "\n" + newDescription;
		}
	}

	public Map<Integer, Integer> getSkillCatBonus() {
		return skillCatBonus;
	}

	public void addSkillCatBonus(SkillCategory category, Integer value) {
		if (skillCatBonus == null) {
			skillCatBonus = new HashMap<Integer, Integer>();
		}
		if (category != null && value != null) {
			skillCatBonus.put(category.getId(), value);
		}
	}

	public Map<Integer, SkillType> getSkillCatType() {
		return skillCatType;
	}

	public void addSkillCatType(SkillCategory category, SkillType value) {
		if (skillCatType == null) {
			skillCatType = new HashMap<Integer, SkillType>();
		}
		if (category != null && value != null) {
			skillCatType.put(category.getId(), value);
		}
	}

	/**
	 * Returns the skill bonus map with all bonus.
	 * 
	 * @return the skill bonus map or {@code null}
	 */
	public Map<Integer, Integer> getSkillBonus() {
		return skillBonus;
	}

	public void addSkillBonus(ISkill skill, Integer rank) {
		if (skillBonus == null) {
			skillBonus = new HashMap<Integer, Integer>();
		}
		if (skill != null && rank != null) {
			skillBonus.put(skill.getId(), rank);
		}
	}
	
	/**
	 * Returns the spell realm bonus or {@code null} if not available.
	 * 
	 * @param stat the spell realm stat
	 * @return the bonus or {@code null}
	 */
	public Integer getSpellRealmBonus(StatEnum stat) {
		if (spellRealmBonus == null) {
			return null;
		}
		return spellRealmBonus.get(stat);
	}

	public void addSpellRealmBonus(StatEnum stat, Integer value) {
		if (spellRealmBonus == null) {
			spellRealmBonus = new HashMap<StatEnum, Integer>();
		}
		if (stat != null && value != null) {
			spellRealmBonus.put(stat, value);
		}
	}
	
	public Map<Integer, SkillType> getSkillType() {
		return skillType;
	}


	public void addSkillType(ISkill skill, SkillType value) {
		if (skillType == null) {
			skillType = new HashMap<Integer, SkillType>();
		}
		if (skill != null && value != null) {
			skillType.put(skill.getId(), value);
		}
	}

	public IProgression getProgressionBody() {
		return progressionBody;
	}

	public void setProgressionBody(IProgression progressionBody) {
		this.progressionBody = progressionBody;
	}

	public IProgression getProgressionPower() {
		return progressionPower;
	}

	public void setProgressionPower(IProgression progressionPower) {
		this.progressionPower = progressionPower;
	}

	public Float getWeightPenalty() {
		return weightPenalty;
	}

	public void setWeightPenalty(Float weightPenalty) {
		this.weightPenalty = weightPenalty;
	}

	/**
	 * Gets the base movement rate in {@link LengthUnit#CM}.
	 * 
	 * @return the base movement rate modifier or {@code null}
	 */
	public Float getBaseMovement() {
		return baseMovement;
	}

	/**
	 * Sets the base movement rate in {@link LengthUnit#CM}.
	 * 
	 * @param baseMovement the base movement rate modifier or {@code null} to reset
	 */
	public void setBaseMovement(Float baseMovement) {
		this.baseMovement = baseMovement;
	}

	/**
	 * Adds an additional resistance line on sheet.
	 * 
	 * @param descr localized rr line, not {@code null} 
	 */
	public void addAdditionalResistanceLine(String descr) {
		if (resistanceLines == null) {
			resistanceLines = new ArrayList<String>();
		}
		resistanceLines.add(descr);
	}

	/**
	 * Returns an unmodifiable list of resistance lines.
	 * 
	 * @return list of rr lines or {@code null}
	 */
	public List<String> getAdditionalResistanceLine() {
		if (resistanceLines == null) {
			return null;
		}
		return Collections.unmodifiableList(resistanceLines);
	}

	/**
	 * Returns the defensive bonus.
	 * 
	 * @return bonus or {@code null}
	 */
	public Integer getDb() {
		return db;
	}

	/**
	 * Sets the defensive bonus.
	 * 
	 * @param db bonus
	 */
	public void setDb(Integer db) {
		this.db = db;
	}

	/**
	 * Returns the shield db or {@code null}.
	 * 
	 * @return the shield db or {@code null}
	 */
	public Integer getShieldDb() {
		return shieldDb;
	}

	/**
	 * Sets the shield db
	 * 
	 * @param shieldDb the shield db, may be {@code null}
	 */
	public void setShieldDb(Integer shieldDb) {
		this.shieldDb = shieldDb;
	}

	public void setExhaustion(Integer exhaustion) {
		this.exhaustion = exhaustion;
	}
	
	public Integer getExhaustion() {
		return exhaustion;
	}

	public void addStatBonus(StatEnum stat, Integer bonus) {
		if (statBonus == null) {
			statBonus = new HashMap<StatEnum, Integer>();
		}
		statBonus.put(stat, bonus);
	}
	
	/**
	 * Returns the bonus or {@code null} if no bonus is available. 
	 * 
	 * @param stat the stat, may be {@code null}
	 * @return the bonus or {@code null}
	 */
	public Integer getStatBonus(StatEnum stat) {
		if (statBonus == null || stat == null) {
			return null;
		}
		return statBonus.get(stat);
	}

	public void setRecoveryMultiplier(Float recoveryMultiplier) {
		this.recoveryMultiplier = recoveryMultiplier;
	}
	
	public Float getRecoveryMultiplier() {
		return recoveryMultiplier;
	}
	
	public Float getExhaustionMultiplier() {
		return exhaustionMultiplier;
	}
	
	public void setExhaustionMultiplier(Float exhaustionMultiplier) {
		this.exhaustionMultiplier = exhaustionMultiplier;
	}
	
	public Float getBasemoverateMultiplier() {
		return basemoverateMultiplier;
	}
	
	public void setBasemoverateMultiplier(Float basemoverateMultiplier) {
		this.basemoverateMultiplier = basemoverateMultiplier;
	}

	/**
	 * Sets the tolerance factor.
	 * 
	 * @param tolerance the factor
	 */
	public void setTolerance(Float tolerance) {
		this.tolerance = tolerance;
	}
	
	/**
	 * Returns the tolerance factor or {@code null}.
	 * 
	 * @return the tolerance factor
	 */
	public Float getTolerance() {
		return tolerance;
	}
	
	/**
	 * Sets absolute value of soul departure.
	 * @param souldeparture
	 */
	public void setSouldeparture(Integer souldeparture) {
		this.souldeparture = souldeparture;
	}
	
	/**
	 * Returns the absolute value of soul departure.
	 * @return new value for soul departure or {@code null}
	 */
	public Integer getSouldeparture() {
		return souldeparture;
	}

	/**
	 * Adds a bonus for the given resistance.
	 * 
	 * @param res the resistance, not {@code null}
	 * @param bonus the bonus, not {@code null}
	 */
	public void setResistanceBonus(ResistanceEnum res, Integer bonus) {
		if (resBonus == null) {
			resBonus = new HashMap<ResistanceEnum, Integer>();
		}
		resBonus.put(res, bonus);
	}
	
	/**
	 * Returns the resistance bonus or {@code null} for the given resistance enum.
	 * 
	 * @param res the resistance or {@code null}
	 */
	public Integer getResistanceBonus(ResistanceEnum res) {
		if (resBonus == null) {
			return null;
		}
		return resBonus.get(res);
	}

	public void setSkillCostReplacement(Integer id, Skillcost costs) {
		if (skillCostReplacement == null) {
			skillCostReplacement = new HashMap<Integer, Skillcost>();
		}
		skillCostReplacement.put(id, costs);
	}

	public void setSkillCategoryCostReplacement(Integer id, Skillcost costs) {
		if (skillCategoryCostReplacement == null) {
			skillCategoryCostReplacement = new HashMap<Integer, Skillcost>();
		}
		skillCategoryCostReplacement.put(id, costs);
	}
	
	/**
	 * Returns a {@link Skillcost} replacement for the given {@link ISkill} or {@code null}.
	 * 
	 * @param skill the skill, not {@code null}
	 * @return the {@link Skillcost} or {@code null}
	 */
	public Skillcost getSkillCostReplacement(ISkill skill) {
		if (skillCostReplacement == null) {
			return null;
		}
		return skillCostReplacement.get(skill.getId());
	}
	
	/**
	 * Returns a {@link Skillcost} replacement for the given {@link SkillCategory} or {@code null}.
	 * 
	 * @param skillCat the skill category or {@code null}
	 * @return the {@link Skillcost} or {@code null}
	 */
	public Skillcost getSkillCategoryCostReplacement(SkillCategory skillCat) {
		if (skillCategoryCostReplacement == null) {
			return null;
		}
		return skillCategoryCostReplacement.get(skillCat.getId());
	}
	
	/**
	 * Returns this talent/flaw as text.
	 * 
	 * @return as text, not {@code null}
	 */
	public String asText(MetaData metaData, RMSheet sheet) {
		StringBuilder content = new StringBuilder();
		// Headline
		if (getName() != null) {
			content.append(getName());
		}
		String type = RESOURCE.getString("TalentFlawType."+getType().name());
		String level = RESOURCE.getString("TalentFlawLevel."+getLevel().name());
		content.append(" (").append(type).append(", ").append(level ).append(")");
		content.append("\n");
		// text
		if (!StringUtils.isBlank(getDescription())) {
			content.append(getDescription()).append("\n");
		}
		addLine(initiativeBonus, InitiativePart.ID, null, content);
		addLine(db, DBPart.ID, null, content);
		addLine(shieldDb, ShieldDBPart.ID, null, content);
		addLine(exhaustion, ExhaustionPart.ID, null, content);
		addLine(exhaustionMultiplier, ExhaustionMultiplierPart.ID, "x ", content);
		addLine(snapBonus, SnapBonusPart.ID, null, content);
		addLine(weightPenalty, WeightPenaltyPart.ID, "x ", content);
		if (baseMovement != null) {
			addLine(BaseMoveRatePart.formatLU(baseMovement.floatValue()), BaseMoveRatePart.ID, null, content);
		}
		addLine(basemoverateMultiplier, BasemoverateMultiplierPart.ID, "x ", content);
		addLine(recoveryMultiplier, RecoveryPart.ID, "x ", content);
		if (tolerance != null) {
			addLine(TolerancePart.formatValue(tolerance.floatValue()), TolerancePart.ID, "x ", content);
		}
		if (resistanceLines != null) {
			for (String line : resistanceLines) {
				addLine(line, ResistancePart.ID, null, content);
			}
		}
		addLine(souldeparture, SouldeparturePart.ID, null, content);
		if (progressionBody != null) {
			addLine(progressionBody.getFormattedString(), ProgressionPart.BODY_ID, null, content);
		}
		if (progressionPower != null) {
			addLine(progressionPower.getFormattedString(), ProgressionPart.POWER_ID, null, content);
		}
		
		if (statBonus != null) {
			for (StatEnum key : statBonus.keySet()) {
				content.append(RESOURCE.getString("StatEnum."+key.name()+".long")).append(" (");
				content.append(RESOURCE.getString("StatEnum."+key.name()+".short")).append(") ");
				Integer value = statBonus.get(key);
				content.append(PDFCreator.format(value.intValue(), false)).append(" ");
			}
			content.append("\n");
		}
		
		if (resBonus != null) {
			content.append(RESOURCE.getString("ui.talentflaw.value."+ResistancePart.ID)).append(" ");
			for (ResistanceEnum key : resBonus.keySet()) {
				content.append(RESOURCE.getString("ResistanceEnum."+key.name())).append(" ");
				Integer value = resBonus.get(key);
				content.append(PDFCreator.format(value.intValue(), false)).append(" ");
			}
			content.append("\n");
		}
		addMap(sheet, metaData, skillBonus, true, content);
		addMap(sheet, metaData, skillType, true, content);
		addMap(sheet, metaData, skillCatBonus, false, content);
		addMap(sheet, metaData, skillCatType, false, content);
		addMap(sheet, metaData, skillCostReplacement, true, content);
		addMap(sheet, metaData, skillCategoryCostReplacement, false, content);
		
		return content.toString();
	}

	private void addMap(RMSheet sheet, MetaData metaData, Map<Integer, ?> map, boolean isSkill, StringBuilder content) {
		if (map != null) {
			boolean first = true;
			for (Integer id : map.keySet()) {
				if (!first) {
					content.append(", ");
				}
				first = false;
				Object value = map.get(id);
				if (isSkill) {
					content.append( sheet.getSkill(id).getName() );
				} else {
					content.append( metaData.getSkillCategory(id).getName() );
				}
				content.append(" ");
				content.append( convertValue(value, null) );
			}
			content.append("\n");
		}
	}

	private void addLine(Object value, String prop, String prefix, StringBuilder content) {
		if (value != null) {
			content.append(RESOURCE.getString("ui.talentflaw.value."+prop))
			.append(": ");
			String formatted = convertValue(value, prefix);
			content.append(formatted);
			
			content.append("\n");
		}
	}

	private String convertValue(Object value, String prefix) {
		String formatted = "0";
		if (value instanceof Integer) {
			formatted = PDFCreator.format(((Integer)value).intValue(), false);
		} else if (value instanceof Float){
			formatted = prefix + NumberFormat.getNumberInstance().format(value);
		} else if (value instanceof SkillType) {
			formatted = RESOURCE.getString("SkillType."+((SkillType)value).name());
		} else if (value instanceof Skillcost) {
			formatted = ((Skillcost)value).toString();
		} else {
			formatted = value.toString();
		}
		return formatted;
	}
}
