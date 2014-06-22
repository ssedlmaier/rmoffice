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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.meta.enums.RankType;
import net.sf.rmoffice.meta.enums.SpellUserType;
import net.sf.rmoffice.meta.enums.SpelllistPart;
import net.sf.rmoffice.meta.internal.SkillcategorySpelllistPartKey;



/**
 * 
 */
public class MetaData {
	private static int[] armorManeuverMin = new int[] {0,0,0,0,
		                                               0,0,-10,-15,
		                                               -5, -10, -15, -15,
		                                               -10, -15, -25, -25,
		                                               -15, -20, -35, -45};
	private static int[] armorManeuverMax = new int[] {0,0,0,0,
		                                               0,-20,-40,-50,
		                                               -50,-70,-90,-110,
		                                               -70,-90,-120,-130,
		                                               -90,-110,-150,-165};
	private static int[] armorRangeModi = new int[] {0,0,0,0,
		                                             0,-5,-15,-15,
		                                             0,-10,-20,-30,
		                                             0,-10,-20,-20,
		                                             0,-10,-30,-40};
	private static int[] armorReactionModi = new int[] {0,0,0,0,
		                                                0,0,-10,-15,
		                                                0,-5,-15,-15,
		                                                -5,-10,-20,-20,
		                                                -10,-20,-30,-40};
	/* maybe we can configure this later, too*/
	private IProgression defaultSkillgroupProg = new Progression(-15f, 2f, 1f, 0.5f, 0f);
	private IProgression emptySkillgroupProg = new Progression(0, 0, 0, 0, 0);
	private IProgression combinedSkillProg = new Progression(-30, 5, 3, 1.5f, 0.5f);
	private IProgression limitedSkillProg = new Progression(0, 1, 1, 0.5f, 0);
	private IProgression defaultSkillProg = new Progression(-15, 3, 2, 1, 0.5f);
	
	private List<Race> races;
	private Map<Integer, Race> raceById;
	private List<Culture> cultures;
	private Map<Integer, Culture> culturesById;
	private List<Profession> professions;
	private Map<Integer, Profession> professionsById;
	private List<SkillCategory> skillgroups;
	private Map<Integer, SkillCategory> skillgroupById;
	private Map<Profession, Map<SkillCategory, Skillcost>> skillcosts = new HashMap<Profession, Map<SkillCategory,Skillcost>>();
	private List<ISkill> skills;
	private Map<Integer, ISkill> skillsById;
	private List<Shield> shields = new ArrayList<Shield>();
	private Map<Integer, Shield> shieldsById = new HashMap<Integer, Shield>();
	private Map<Integer, ISkill> armorSkills = new HashMap<Integer, ISkill>();
	private Map<Integer, TrainPack> trainPacks = new HashMap<Integer, TrainPack>();
	private Map<TrainPackKey, Integer> trainPackCosts = new HashMap<TrainPackKey, Integer>();
	private Map<SkillcategorySpelllistPartKey, Skillcost> spellcostByLevel = new HashMap<SkillcategorySpelllistPartKey, Skillcost>();
    private List<TalentFlawPreset> talentFlaws = new ArrayList<TalentFlawPreset>();
	
	public List<Race> getRaces() {
		return races;
	}
	
	/**
	 * 
	 * @param races not {@code null}
	 */
	/*package */ void setRaces(List<Race> races) {
		raceById = new HashMap<Integer, Race>();
		for (Race r : races) {
			raceById.put(r.getId(), r);
		}
		this.races = races;
	}
	
	public Race getRace(Integer raceId) {
		return raceById.get(raceId);
	}
	
	public List<Profession> getProfessions() {
		return professions;
	}
	
	/**
	 * 
	 * @param professions not {@code null}
	 */
	/*package */ void setProfessions(List<Profession> professions) {
		professionsById = new HashMap<Integer, Profession>();
		for (Profession p : professions) {
			professionsById.put(p.getId(), p);
		}
		this.professions = professions;
	}
	
	public Profession getProfession(Integer id) {
		return professionsById.get(id);
	}

	public List<SkillCategory> getSkillCategories() {
		return skillgroups;
	}
	
	/**
	 * 
	 * @param skillgroups not {@code null}
	 */
	/*package */ void setSkillgroups(List<SkillCategory> skillgroups) {
		skillgroupById = new HashMap<Integer, SkillCategory>();
		for (SkillCategory sg : skillgroups) {
			skillgroupById.put(sg.getId(), sg);
		}
		this.skillgroups = skillgroups;
	}
	
	/**
	 * Returns the skill category or {@code null} if no category exists for the
	 * given id.
	 * 
	 * @param id
	 *            the id
	 * @return the skill category or {@code null}
	 */
	public SkillCategory getSkillCategory(Integer id) {
		return skillgroupById.get(id);
	}
	
	public void addSkillcost(Profession prof, SkillCategory sg, Skillcost cost) {
		if (! skillcosts.containsKey(prof)) {
			skillcosts.put(prof, new HashMap<SkillCategory, Skillcost>());
		}
		skillcosts.get(prof).put(sg, cost);
	}
	
	/**
	 * UI must use {@link RMSheet#getSkillcost(SkillCategory)}. This returns only the default meta data. 
	 * 
	 * @param prof the profession
	 * @param group the skill group
	 * @return the costs given by the meta data without modified switchable costs  
	 */
	public Skillcost getSkillcost(Profession prof, SkillCategory group) {
		if (! skillcosts.containsKey(prof)) {
			return new Skillcost();
		}
		if (skillcosts.get(prof).containsKey(group) ) {
			return skillcosts.get(prof).get(group);
		}
		return new Skillcost();
	}
	
	/**
	 * UI must use {@link RMSheet#getSkillcost(ISkill)}. This returns only the default meta data. 
	 * 
	 * @param profession the profession
	 * @param cat the skill category (could be a switched category)
	 * @param skill the skill
	 * @param rank the current rank of the skill
	 * @return the costs depending on the rank
	 */
	public Skillcost getSkillcost(Profession profession, SkillCategory cat, ISkill skill, BigDecimal rank) {
		if (skill.isSpelllist()) {
			/* spell lists have costs that depends on rank */
			/* 1. convert rank to spelllistPart */
			SpelllistPart spelllistPart = SpelllistPart.getPartForRank(rank.intValue() + 1);
			SpellUserType spellUserType = profession.getSpellUserType();
			Skillcost baseCosts = getSkillcost(profession, cat);
			if (cat.getRankSubType().isTraining()) {
				return baseCosts;				
			} else if (SpellUserType.NONE.equals(spellUserType)) {
				/* we have to calculate the costs from base */
				int[] newCosts = new int[baseCosts.size()];
				for (int i = 0; i < newCosts.length; i++) {
					newCosts[i] = baseCosts.getCost(i) * spelllistPart.getNoneSpelluserFactor();
				}
				return new Skillcost(newCosts);
			} else {
				/* all others has fixed costs */
				return spellcostByLevel.get(new SkillcategorySpelllistPartKey(cat, spelllistPart, spellUserType));
			}
		} else {
			return getSkillcost(profession, cat);
		}
	}

	public IProgression getSkillgroupProgression(RankType type) {
		if (type.isDefaultProgression()) {
			return defaultSkillgroupProg;
		}
		return emptySkillgroupProg;
	}
	
	/**
	 * Returns an unmodifiable list of skills including all spell lists.
	 * 
	 * @return unmodifiable list of skills, not {@code null}
	 */
	public List<ISkill> getSkills() {
		return Collections.unmodifiableList(skills);
	}

	/**
	 * Creates a new index for {@link #getSkill(Integer)}.
	 * 
	 * @param skills
	 */
	/*package */ void setSkills(List<ISkill> newSkills) {
		skills = new ArrayList<ISkill>();		
		skillsById = new HashMap<Integer, ISkill>();
		for (ISkill skill : newSkills) {
			skills.add(skill);
			skillsById.put(skill.getId(), skill);
		}
	}
	
	/**
	 * Only used by {@link RMSheet}. Use {@link RMSheet#getSkill(Integer)}
	 * 
	 * @param id
	 * @return the {@link ISkill} from meta data or {@code null}
	 */
	public ISkill getSkill(Integer id) {
		return skillsById.get(id);
	}

	
	public IProgression getCombinedSkillProg() {
		return combinedSkillProg;
	}

	
	public IProgression getLimitedSkillProg() {
		return limitedSkillProg;
	}

	
	public IProgression getDefaultSkillProg() {
		return defaultSkillProg;
	}

	public void addShield(Shield shield) {
		shields.add(shield);		
		shieldsById.put(Integer.valueOf(shield.getId()), shield);
	}
	
	/**
	 * 
	 * 
	 * @return unmodifiable {@link List} of {@link Shield}
	 */
	public List<Shield> getShields() {
		return Collections.unmodifiableList(shields);
	}
	
	/**
	 * 
	 * 
	 * @param shieldId the shield id
	 * @return the {@link Shield} or {@code null} if no shield is available for the id
	 */
	public Shield getShield(int shieldId) {
		return shieldsById.get(Integer.valueOf(shieldId));
	}
	
	/**
	 * Returns the minimal armor modification.
	 * 
	 * @param armor the armor class (1-20)
	 * @return the modification less or equals 0
	 * @throws ArrayIndexOutOfBoundsException if the given armor is less than 0 or greater than 20
	 */
	public int getArmorManeuverModiMin(int armor) {
		return armorManeuverMin[armor - 1];
	}
	
	/**
	 * Returns the maximum armor modification.
	 * 
	 * @param armor the armor class (1-20)
	 * @return the modification less or equals 0
	 * @throws ArrayIndexOutOfBoundsException if the given armor is less than 0 or greater than 20
	 */
	public int getArmorManeuverModiMax(int armor) {
		return armorManeuverMax[armor - 1];
	}
	
	/**
	 * Returns the range combat modification.
	 * 
	 * @param armor the armor class (1-20)
	 * @return the modification less or equals 0
	 * @throws ArrayIndexOutOfBoundsException if the given armor is less than 0 or greater than 20
	 */
	public int getArmorRangeCombatModi(int armor) {
		return armorRangeModi[armor - 1];
	}
	
	/**
	 * Returns the reaction bonus modification.
	 * 
	 * @param armor the armor class (1-20)
	 * @return the modification less or equals 0
	 * @throws ArrayIndexOutOfBoundsException if the given armor is less than 0 or greater than 20
	 */
	public int getArmorReactionModi(int armor) {
		return armorReactionModi[armor - 1];
	}
	
	public ISkill getArmorSkill(int armor) {
		return armorSkills.get(Integer.valueOf(armor));
	}
	
	/* package private */ void addArmourSkill(int armor, ISkill skill) {
		armorSkills.put(Integer.valueOf(armor), skill);
	}

	/**
	 * Adds a new training pack.
	 *  
	 * @param tp the new pack
	 */
	public void addTrainPack(TrainPack tp) {		
		trainPacks.put(tp.getId(), tp);
	}

	public Collection<TrainPack> getTrainPacks() {
		List<TrainPack> values = new ArrayList<TrainPack>();
		values.addAll(trainPacks.values());
		Comparator<TrainPack> comp = new Comparator<TrainPack>() {
			
			@Override
			public int compare(TrainPack o1, TrainPack o2) {
				if (o1 == null && o2 == null) return 0;
				if (o1 == null) return -1;
				if (o2 == null) return 1;
				return o1.getName().compareTo(o2.getName());
			}
		};
		Collections.sort(values, comp); 
		return Collections.unmodifiableCollection( values );
	}
	
	/**
	 * Returns the {@link TrainPack} for the given id or {@code null} if id does not exist. 
	 * 
	 * @param id the training pack id
	 * @return the {@link TrainPack} or {@code null}
	 */
	public TrainPack getDevPack(Integer id) {
		return trainPacks.get(id);
	}
	
	/* package private */ void addTrainPackCosts(TrainPack trainPack, Profession prof, int costs) {
		trainPackCosts.put(new TrainPackKey(trainPack, prof), Integer.valueOf(costs));
	}
	
	public int getTrainPackCosts(TrainPack trainpack, Profession prof) {
		if (trainPackCosts.containsKey(new TrainPackKey(trainpack, prof))) {
			return trainPackCosts.get(new TrainPackKey(trainpack, prof)).intValue();
		}
		return 0;
	}

	/**
	 * 
	 * @param cultures
	 */
	/*package */ void setCultures(List<Culture> cultures) {
		this.cultures = cultures;
		culturesById = new HashMap<Integer, Culture>();
		for (Culture culture : cultures) {
			culturesById.put(culture.getId(), culture);
		}
	}

	/**
	 * 
	 * @param cultureId the culture Id
	 * @return the culture or {@code null}
	 */
	public Culture getCulture(Integer cultureId) {
		return culturesById.get(cultureId);
	}
	
	/**
	 * Returns a list of valid {@link Culture}s for the race. Returns all {@link Culture}s if race is {@code null}.
	 * 
	 * @param race the race, may be {@code null}
	 * @return list of cultures
	 */
	public List<Culture> getCultureForRace(Race race) {
		List<Culture> culturesForRace = new ArrayList<Culture>();
		for (Culture culture : cultures) {
			if (culture.getValidRaces().contains(race)) {
				culturesForRace.add(culture);
			}
		}
		return culturesForRace;
	}

	/**
	 * 
	 * @param skillCatID the skill category id, not {@code null}
	 * @param part the spell list part, not {@code null}
	 * @param type the spell user type, not {@code null}
	 * @param sc the cost value
	 */
	/* package */ void addSpellcostByLevel(Integer skillCatID, SpelllistPart part, SpellUserType type, Skillcost sc) {
		SkillCategory category = getSkillCategory(skillCatID);
		spellcostByLevel.put(new SkillcategorySpelllistPartKey(category, part, type), sc);
	}
	
	/**
	 * Returns a list of all loaded talents and flaws with there parameter properties.
	 * @return an unmodifiable list of {@link TalentFlawPreset}s
	 */
	public List<TalentFlawPreset> getTalentFlaws() {
		return Collections.unmodifiableList(talentFlaws);
	}
	
	/* package */ void addTalentFlaw(TalentFlawPreset newEntry) {
		this.talentFlaws.add(newEntry);
	}
	
	/* **************************************************
	 * 
	 * ************************************************* */
	private static class TrainPackKey {
		private final TrainPack trainPack;
		private final Profession prof;

		public TrainPackKey(TrainPack trainPack, Profession prof) {
			this.trainPack = trainPack;
			this.prof = prof;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((prof == null) ? 0 : prof.hashCode());
			result = prime * result
					+ ((trainPack == null) ? 0 : trainPack.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TrainPackKey other = (TrainPackKey) obj;
			if (prof == null) {
				if (other.prof != null)
					return false;
			} else if (!prof.equals(other.prof))
				return false;
			if (trainPack == null) {
				if (other.trainPack != null)
					return false;
			} else if (!trainPack.equals(other.trainPack))
				return false;
			return true;
		}
		
	}
}
