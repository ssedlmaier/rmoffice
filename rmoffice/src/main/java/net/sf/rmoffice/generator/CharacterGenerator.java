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
package net.sf.rmoffice.generator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.rmoffice.LevelUpVetoException;
import net.sf.rmoffice.RMPreferences;
import net.sf.rmoffice.core.Characteristics;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.core.Rank;
import net.sf.rmoffice.core.ToDo;
import net.sf.rmoffice.generator.Name.Style;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.Profession;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.Skillcost;
import net.sf.rmoffice.meta.Spelllist;
import net.sf.rmoffice.meta.WeightHeight;
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.meta.enums.StatEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.binding.beans.BeanAdapter;


/**
 * Modifies the given {@link RMSheet} with random values.
 */
public class CharacterGenerator {
	private final static Logger log = LoggerFactory.getLogger(CharacterGenerator.class);
	/* special: Magic Development=202 */
	/* Swimming=5, Climbing=14, Body Dev=64, */
    private final static int[] BASE_SKILLS = {5, 14, 64, 98, 100, 110, 160, 168, 204 };
    /* Schleichen=91, Verstecken=93, Gassenwissen=104,  Entesslung=10, Überreden=33, Seilkunst=48,
     * Lautloser Angriff=79, Fallen entschärfen=81, Lock pick=87, Tarnung=88, Fingerfertigkeit=90, Taschendiebstahl=92, 
     * Verstecken=93, Runen lesen=101, Gassenwissen=104, Kräuter vorbereiten=123, Fallen entdecken=162,
     * Lügen entlarven=164, Spuren=165/166, Hinterhalt entdecken=343, Pflanzenkunde=180, Tierkunde=184, 185-189 kunden
     * Nahrungssuche=212, Sprinten=6,    */
    private final static int[] SECONDARY_SKILLS = {6, 10, 33, 48, 79, 81, 87, 88, 90, 91, 92, 93,
    	                             101, 104, 123, 162, 164, 165, 166, 180, 184, 185, 186, 187,
    	                             188, 189, 212, 343, };
    /* Individual random skill from cats: 10=Kunst-Aktiv 11=Kunst-Passiv 38=Wissen-Magisch, 39=Wissen-Obskur */
    private final static List<Integer> FLAVOR_SKILL_CATS = new ArrayList<Integer>();

    static {
    	FLAVOR_SKILL_CATS.add( Integer.valueOf(10) );
    	FLAVOR_SKILL_CATS.add( Integer.valueOf(11) );
    	FLAVOR_SKILL_CATS.add( Integer.valueOf(38) );
    	FLAVOR_SKILL_CATS.add( Integer.valueOf(39) );
    }
	private final MetaData data;
	private final BeanAdapter<Characteristics> characteristics;
	private final BeanAdapter<RMSheet> sheetAdapter;
	private List<ISkill> skills;

	/**
	 * 
	 * @param characteristics bean adapteer may be {@code null} if not characteristics methods are called
	 * @param sheetAdapter the rm sheet adapter not {@code null}
	 * @param data the meta data, may be {@code null}
	 */
	public CharacterGenerator(final BeanAdapter<Characteristics> characteristics, final BeanAdapter<RMSheet> sheetAdapter, final MetaData data) {
		this.characteristics = characteristics;
		this.sheetAdapter = sheetAdapter;
		this.data = data;
	}
	
	/**
	 * Generates a styled name according the the race and gender
	 */
	public void generateName() {
		boolean isMale = true;
		if (characteristics.getBean() != null && characteristics.getBean().isFemale()) {
			isMale = false;
		}
		Style style = null;
		if (sheetAdapter.getBean() != null && sheetAdapter.getBean().getRace() != null) {
			style = sheetAdapter.getBean().getRace().getNameStyle();
		}
		if (style == null) {
			style = Style.HUMAN;
		}
		sheetAdapter.getBean().setCharacterName(Name.getName(style, isMale));
	}

	/**
	 * Distributes the start points 600 + 10d10 to the given stats. Current temp values not equals 0 are
	 * used as minimum. Potential values not equals 0 are used as maximum, 
	 */
	public void distributeStats() {
		if (sheetAdapter.getBean() != null && sheetAdapter.getBean().getProfession() != null) {
			int pointsToDistribute = 600 + DiceUtils.roll(10, 10);
			
			Profession prof = sheetAdapter.getBean().getProfession();
			/* set all to 25 */
			for (StatEnum stat : StatEnum.values()) {
				if (sheetAdapter.getBean().getStatTemp(stat) == 0) {
					if ( prof.getStats().contains(stat) ) {
						/* prof stats */
						sheetAdapter.getBean().setStatTemp(stat, 90, true);
					} else {
						sheetAdapter.getBean().setStatTemp(stat, 25, true);
					}
				}
			}
			while (pointsToDistribute > sheetAdapter.getBean().getTempSum()) {
				StatEnum stat = StatEnum.values()[ DiceUtils.roll(1, 10) - 1 ];
				int currentStat = sheetAdapter.getBean().getStatTemp(stat);
				int maxStat = sheetAdapter.getBean().getStatPot(stat);
				if (maxStat == 0) {
					maxStat = 100;
				}
				int newVal = -1;
				if (currentStat < 70) {
					if (DiceUtils.roll(1, 100) < (60 + (stat.isForAPCalculation() ? 10 : 0))) {
						newVal = currentStat + DiceUtils.roll(1, 10);
						if (newVal % 10 > 1 && newVal % 10 < 5) {
							newVal = Math.round(newVal / 10 ) * 10 + 5;
							if (log.isDebugEnabled()) log.debug("round up to "+newVal);
						}
					}
				} else if (currentStat < 90) {
					if (DiceUtils.roll(1, 100) < 50) {
						newVal = currentStat + DiceUtils.roll(1, 5);
					}
				} else if (currentStat < 100) {
					if (DiceUtils.roll(1, 100) < 30) {
						newVal = currentStat + DiceUtils.roll(1, 3);
					}
				}
				/* check max value */
				if (newVal > -1) {
					if (newVal > maxStat) {
						newVal = maxStat;
					}
					sheetAdapter.getBean().setStatTemp(stat, newVal, true);
				}
			}
		}
	}
	
	/**
	 * Generates the potential stats for all potentials equals 0.
	 */
	public void generatePotStats() {
		for (StatEnum stat : StatEnum.values()) {
			if (sheetAdapter.getBean().getStatPot(stat) == 0) {
				int potStat = StatGainGenerator.getStatPotDice(sheetAdapter.getBean().getStatTemp(stat));
				sheetAdapter.getBean().setStatPot(stat, potStat, true);
			}
		}
	}
	
	/**
	 * Do the stat gains for each stat one time.
	 */
	public void doStatGains() {
		for (StatEnum stat : StatEnum.values()) {
			int tempStat = sheetAdapter.getBean().getStatTemp(stat);
			int potStat = sheetAdapter.getBean().getStatPot(stat);
			int dice1 = DiceUtils.roll(1, 10);
			int dice2 = DiceUtils.roll(1, 10);
			int newVal = StatGainGenerator.getStatGainDice(tempStat, potStat, dice1, dice2);
			sheetAdapter.getBean().setStatTemp(stat, newVal, true);
		}
	}

	/**
	 * Generates some values of the characteristics.
	 */
	public void generateCharacteristics() {
		/* appearance potential presence - 25 + 5W10 */
		int app = sheetAdapter.getBean().getStatPot(StatEnum.PRESENCE) - 25 + DiceUtils.roll(5, 10);
		if (app < 1) {
			app = 1;
		} else if (app > 100) {
			app = 100;
		}
		characteristics.getBean().setAppearance(app);
		/* age */
		characteristics.getBean().setAge(16 + DiceUtils.roll(3, 2 + (int)sheetAdapter.getBean().getLevel()));
		/* weight and height */
		if (sheetAdapter.getBean().getCulture() != null) {
			WeightHeight wh = sheetAdapter.getBean().getCulture().getWeightHeight();
			boolean isFemale = characteristics.getBean().isFemale();
			int avgH = wh.getHeightAvg(isFemale);
			/* height = 85% of avg height + 2 x 0-15% of avg height; range: 85% - 115% */
			int rnd = 85 + DiceUtils.roll(2, 15);
			int height = (int) ((rnd / 100f) * avgH);
			characteristics.getBean().setHeight(height);
			
			/* weight */
			int avgW = wh.getWeightAvg(isFemale);
			rnd = 100 + DiceUtils.roll(1, 15) /* 100-115% */  - (rnd - 100) /* the diff from height */;
			int weight = (int) ((rnd / 100f) * avgW);
			characteristics.getBean().setWeight(weight);
		}
	}
	
	public void levelUpPrepare() {
		RMSheet sheet = sheetAdapter.getBean();
		skills = new ArrayList<ISkill>();
		/* set magic realm */
		if (sheet.isMagicRealmEditable()) {
			int maxBonus = 0;
			StatEnum maxStat = null;
			for (StatEnum stat : StatEnum.values()) {
				if (stat.isForMagic()) {
					int statBonus = sheet.getStatBonusTotal(stat);
					if (maxBonus < statBonus) {
						maxStat = stat;
						maxBonus = statBonus;
					}
				}
			}
			if (maxStat != null) {
				sheet.setMagicRealm(maxStat);
			}
		} else {
			/* add power development for magic users */
			skills.add(data.getSkill(Integer.valueOf(202)));
		}
		/* get the favorite marked skills */
		List<ISkill> weaponsAll = new ArrayList<ISkill>();
		List<ISkill> weaponsFav = new ArrayList<ISkill>();
		for (Rank rank : sheet.getSkillRanks()) {
			ISkill skill = sheet.getSkill(rank.getId());
			if (skill != null) {
				SkillCategory category = sheet.getSkillcategory(skill);
				if (Boolean.TRUE.equals( rank.getFavorite() ) ) {				
					skills.add(skill);
					if (category.getRankType().isWeapon()) {
						weaponsFav.add(skill);
					}
				}
				/* all weapons (from youth and favorites) */
				if (category.getRankType().isWeapon() && category.getRankType().isCostSwitchable()){
					weaponsAll.add(skill);
				}
			} else {
				if (log.isWarnEnabled()) log.warn("Could not load skill with ID "+rank.getId());
			}
		}
		/* weapons handling */
		if (weaponsFav.size() == 0) {
			/* adjust weapon costs */
			List<ISkill> weaponsToAdd = levelUpAdjustWeaponCosts(sheet, weaponsAll);
			/* we have no favorite weapon, we use the youth weapons */
			skills.addAll(weaponsToAdd);
		}
		/* base skills */
		for (int skillId : BASE_SKILLS) {
			ISkill skill = data.getSkill(Integer.valueOf(skillId));
			if (skill != null) {
				if (!skills.contains(skill)) {
					skills.add(skill);
				}
			} else {
				if (log.isWarnEnabled()) log.warn("Skill ID "+skillId+" is not available");
			}
		}
		/* secondary skills */
		skills.addAll(collectSecondarySkills());
		/* individual flavored skills */
		skills.addAll(collectFlavorSkills(sheet));
		/* spell lists */
		int chanceToIgnore = 0;
		List<ISkill> openOwnRealmList = new ArrayList<ISkill>(); /* for youth open spell ranks */
		boolean isOpenInList = false;
		List<ISkill> skillsPool = new ArrayList<ISkill>();
		skillsPool.addAll( data.getSkills() );
		while (skillsPool.size() > 0) {
			ISkill skill = skillsPool.remove(DiceUtils.roll(1, skillsPool.size()) - 1);
			if (skill.isSpelllist() && !RMPreferences.getInstance().isExcluded(skill.getSource())) {
				SkillCategory cat = sheet.getSkillcategory(skill);
				if (cat.getRankSubType().isBase() && !((Spelllist)skill).getSpelllistType().isEvil()) {
					skills.add(skill);
				} else if ((cat.getRankSubType().isOpen() || cat.getRankSubType().isClosed() ) && cat.getRankType().isOwnRealm()) {
					if (cat.getRankSubType().isOpen() && cat.getRankType().isOwnRealm()) {
						openOwnRealmList.add(skill);
					}
					int chance = 5;
					for (StatEnum stat : sheet.getProfession().getStats()) {
						if (stat.isForMagic()) {
							chance = cat.getRankSubType().isOpen() ? 85 : 45;
						}
					}
					Skillcost cost = sheet.getSkillcost(skill);
					if (cost.size() > 0) {
						chance -= cost.getCost(0) * 2;
					}
					chance -= chanceToIgnore;
					int dice = DiceUtils.roll(1, 100);
					boolean addSpelllist = dice <= chance;
					if (log.isDebugEnabled()) log.debug(chance+"% to get spelllist "+skill.getName()+" result="+dice+" => add "+addSpelllist);
					if (addSpelllist) {
						skills.add(skill);
						if (cat.getRankSubType().isOpen() && cat.getRankType().isOwnRealm()) {
							isOpenInList = true;
						}
						/* For each added spell list (non-base) we reduce the chance to get another one */
						chanceToIgnore += 10;
					}
				}
			}
		}
		/* youth open spell ranks */
		if (!isOpenInList && openOwnRealmList.size() > 0) {
			ISkill rndSpelllist = openOwnRealmList.get(DiceUtils.roll(1, openOwnRealmList.size()) - 1);
			skills.add(rndSpelllist);
		}
		/* order by favorite, development costs */
		Collections.sort(skills, new SkillComparator(sheetAdapter.getBean()));
	}

	private List<ISkill> collectFlavorSkills(RMSheet sheet) {
		List<ISkill> flavored = new ArrayList<ISkill>();
		List<SkillCategory> foundCats = new ArrayList<SkillCategory>();
		for (ISkill skill : data.getSkills()) {
			if (!RMPreferences.getInstance().isExcluded(skill.getSource())) {
				SkillCategory cat = sheet.getSkillcategory(skill);
				if (FLAVOR_SKILL_CATS.contains(cat.getId()) && ! foundCats.contains(cat) && DiceUtils.roll(1, 100) < 20) {
					foundCats.add(cat);
					flavored.add(skill);
				}
			}
		}
		return flavored;
	}
	
	/**
	 * Returns a list of {@link ISkill}s which is a subset of the secondary skills.
	 * As lower the development costs are as higher is the chance to be part of the subset.
	 * If the skill is an everyman, vocational or restricted this will modify the chance to add.
	 */
	private List<ISkill> collectSecondarySkills() {
		List<ISkill> subset = new ArrayList<ISkill>();
		for (int id : SECONDARY_SKILLS) {
			int chance = 100;
			ISkill skill = data.getSkill(Integer.valueOf(id));
			Skillcost costs = sheetAdapter.getBean().getSkillcost(skill);
			/* Example base chance:
			 * Cost 1/3 --> 90%
			 * Cost 2/7 --> 80%
			 * Cost 6   --> 40%
			 * */
			chance -= costs.getCost(0) * 10;
			/* second value:
			 * 1/2 => base 90 - 4 = 86
			 * 1/5 => base 90 - 10 = 80
			 * 2/7 => base 80 - 14 = 66
			 * */
			if (costs.size() > 1) {
				chance -= costs.getCost(1) * 2;
			} else {
				chance -= 35;
			}
			/* check if everyman, restricted, vocational */
			SkillType skillType = sheetAdapter.getBean().getSkillType(skill);
			switch (skillType) {
			case DEFAULT:
				chance -= 10;
				break;
			case EVERYMAN:
				chance += 25;
				break;
			case OCCUPATIONAL:
				chance += 50;
				break;
			default:
				/* exclude restricted */
				chance -= 100;
				break;
			}
			if (DiceUtils.roll(1, 100) < chance) {
				subset.add(skill);
			}
		}
		return subset;
	}

	/**
	 * 
	 * @param sheet the sheet
	 * @param weaponsAll all weapons to lern
	 */
	private List<ISkill> levelUpAdjustWeaponCosts(RMSheet sheet, List<ISkill> weaponsAll) {
		List<SkillCategory> cats = new ArrayList<SkillCategory>();
		List<SkillCategory> otherCats = new ArrayList<SkillCategory>();
		List<ISkill> weaponsToAdd = new ArrayList<ISkill>();
		Map<SkillCategory, List<ISkill>> weaponsPerCat = new HashMap<SkillCategory, List<ISkill>>();
 		for (ISkill skill : weaponsAll) {
			SkillCategory cat = sheet.getSkillcategory(skill);
			cats.add(cat);
			if (!weaponsPerCat.containsKey(cat)) {
				weaponsPerCat.put(cat, new ArrayList<ISkill>());
			}
			weaponsPerCat.get(cat).add(skill);
		}
		for (SkillCategory cat: data.getSkillCategories()) {
			if (cat.getRankType().isWeapon() && cat.getRankType().isCostSwitchable() && ! cats.contains(cat)) {
				otherCats.add(cat);
			}
		}
		/* sort by costs */
		Collections.sort(otherCats, new SkillCategoryCostComparator(sheet));
		Collections.sort(cats, new SkillCategoryBonusComparator(sheet)); /* sort by stat bonus */
		/* */
		for (int i=0; i<cats.size(); i++) {
			SkillCategory cat = cats.get(i);
			/* check first the other cats if there is a lesser cost */
			for (int j=0; j<otherCats.size(); j++) {
				Skillcost myCosts = sheet.getSkillcost(cat);
				Skillcost otherCost = sheet.getSkillcost(otherCats.get(j));
				if (myCosts.compareTo(otherCost) > 0) {
					/* switch it */
					sheet.switchSkillCategoryCosts(cat, otherCats.get(j));
				}
			}
			/* check following cats */
			for (int j=i; j<cats.size(); j++) {
				Skillcost myCosts = sheet.getSkillcost(cat);
				Skillcost otherCost = sheet.getSkillcost(cats.get(j));
				if (myCosts.compareTo(otherCost) > 0) {
					/* switch it */
					sheet.switchSkillCategoryCosts(cat, cats.get(j));
				}
			}
		}
		/* now get the weapons (1 to 3 weapons, depending on the costs) */
		int weaponsCostDP = 0;
		int idx = 0;
		while (idx < cats.size()) {
			List<ISkill> ws = weaponsPerCat.get(cats.get(idx++));
			if (ws.size() > 0) {
				ISkill skill = ws.get(DiceUtils.roll(1, ws.size()) - 1);
				weaponsCostDP += sheet.getSkillcost(skill).getCost(0);
				if (weaponsCostDP < 6) {
					weaponsToAdd.add(skill);
				}
			}
		}
		return weaponsToAdd;
	}

	public void levelUpFinish() {
		RMSheet bean = sheetAdapter.getBean();
		for (ISkill skill : skills) {
			Rank rank = bean.getSkillRank(skill);
			SkillCategory cat = bean.getSkillcategory(skill);
			if (rank.getRank().intValue() > 0 && !cat.getRankType().isProgressionBody() && !cat.getRankType().isProgressionMagic()) {
				rank.setFavorite(Boolean.TRUE);
			}
		}
		/* remove todos */
		while (bean.getToDos().size() > 0) {
		   ToDo t = bean.getToDos().get(0);
			bean.finishTodo(t);
		}
	}
	
	public void levelUpSkillsAndCategories(long level) {
		RMSheet bean = sheetAdapter.getBean();
		/* */
		bean.setLvlUpActive(false);		
		int devPoints = bean.getDevPoints();
		devPoints = levelUpArmor(bean, devPoints);
		devPoints = levelUpskillCategories(bean, skills, devPoints);
		devPoints = levelUpSkills(bean, skills, devPoints, level);
		log.debug("finished levelUpSkillsAndCategories with dp="+devPoints);
	}
	

	public void levelUpHobbyAndLanguages() {
		/* TODO languages */
		/* hobby ranks */
		if (log.isDebugEnabled()) log.debug("distributing hobby ranks");
		RMSheet bean = sheetAdapter.getBean();
		int hobbyRanks = bean.getCulture().getHobbyRanks();
		for (int runs=5; hobbyRanks > 0 && runs > 0; runs--) {
			for (ISkill skill : skills) {
				BigDecimal currRank = bean.getSkillRank(skill).getRank();
				if (!skill.isSpelllist() && currRank.intValue() < 10) {
					if ( DiceUtils.roll(1, 100) < 80 ) {
						try {
							bean.setSkillRank(skill, currRank.add(BigDecimal.valueOf(1)));
							hobbyRanks--;
						} catch (LevelUpVetoException e) {
							/* ignore */
						}
					}
				}
			}
		}
		/* open spell list */
		if (log.isDebugEnabled()) log.debug("distributing open spell ranks");
		List<ISkill> openOwnRealmLists = new ArrayList<ISkill>();
		for (ISkill skill : skills) {
			if (skill.isSpelllist()) {
				SkillCategory cat = bean.getSkillcategory(skill);
				if (cat.getRankSubType().isOpen() && cat.getRankType().isOwnRealm()) {
					openOwnRealmLists.add(skill);
				}
			}
		}
		if (openOwnRealmLists.size() > 0) {
			for (int spellRanks = bean.getCulture().getOpenSpellRanks(); spellRanks > 0; spellRanks--) {
				ISkill spelllist = openOwnRealmLists.get(DiceUtils.roll(1, openOwnRealmLists.size())-1);
				BigDecimal rank = bean.getSkillRank(spelllist).getRank();
				try {
					bean.setSkillRank(spelllist, rank.add(BigDecimal.valueOf(1)));
				} catch (LevelUpVetoException e) {
					/* ignore, this should not occur, to prevent endless loop we do not distribute this point */
					log.error("Could not level up the spell "+spelllist.getName(), e);
				}
			}
		}
	}

	private int levelUpArmor(RMSheet bean, int devPoints) {
		if (log.isDebugEnabled()) log.debug("generate armor skill ranks. dp="+devPoints);
		ISkill arSkill = data.getArmorSkill(bean.getArmor());
		int modi = bean.getArmorManeuverModi() - data.getArmorManeuverModiMin(bean.getArmor());
		if (modi < 0) {
			/*raise armor */
			SkillCategory skillcategory = bean.getSkillcategory(arSkill);
			Skillcost costs = bean.getSkillcost(arSkill);
			for (int i=0; i<costs.size() && modi < 0; i++) {
				if (costs.getCost(i) <= devPoints) {
					Rank skillRank = bean.getSkillRank(arSkill);
					try {
						bean.setSkillRank(arSkill, skillRank.getRank().add(BigDecimal.valueOf(1)));
						modi = bean.getArmorManeuverModi() - data.getArmorManeuverModiMin(bean.getArmor());
						devPoints -= costs.getCost(i);
					} catch (LevelUpVetoException e) {
						/* ignore */
					}
				}
			}
			if (costs.getCost(0) <= devPoints && modi < 0) {
				/* level up category */
				Rank catRank = bean.getSkillcategoryRank(skillcategory);
				try {
					bean.setSkillgroupRank(skillcategory, catRank.getRank().add(BigDecimal.valueOf(1)));
					devPoints -= costs.getCost(0);
				} catch (LevelUpVetoException e) {
					/* ignore */
				}
			}
		}
		if (log.isDebugEnabled()) log.debug("generate armor skill ranks finished. dp="+devPoints);
		return devPoints;
	}

	private int levelUpskillCategories(RMSheet bean, List<ISkill> skills, int devPoints) {
		int devPointForSkillcats = Math.round(devPoints * 0.2f);
		if (log.isDebugEnabled()) log.debug("generate skill categories. dp="+devPoints+" we will distribute "+devPointForSkillcats);
		devPoints -= devPointForSkillcats;
		Set<SkillCategory> cats = new HashSet<SkillCategory>();
		for (ISkill skill : skills) {
			SkillCategory cat = bean.getSkillcategory(skill);
			if (!cats.contains(cat) && cat.getRankType().isGroupRankEditable()) {
				cats.add(cat);
				BigDecimal rank = bean.getSkillcategoryRank(cat).getRank();
				if (rank.intValue() < 29) {
					/* do not learn greater than 29, and only in */
					Skillcost cost = bean.getSkillcost(cat);
					if (cost.size() > 0 && devPointForSkillcats > cost.getCost(0)) {
						Rank skillRank = bean.getSkillRank(skill);
						boolean isFavorite =Boolean.TRUE.equals(skillRank.getFavorite());
						int chance = (100 - (cost.getCost(0) * 5) -  rank.intValue() * 4);
						if (isFavorite || DiceUtils.roll(1, 100) < chance) {
							/* only if 100 - (cost*5)% chance to learn */
							try {
								bean.setSkillgroupRank(cat, rank.add(BigDecimal.valueOf(1)) );
								devPointForSkillcats -= cost.getCost(0);
							} catch (LevelUpVetoException e) {
								/* ignore */
							}
						}
					}
				}
			}
		}
		devPoints += devPointForSkillcats;
		if (log.isDebugEnabled()) log.debug("generate skill categories finished. dp: " + devPoints+ " left ");
		return devPoints;
	}

	private int levelUpSkills(RMSheet bean, List<ISkill> skills, int devPoints, long level) {
		int spelllistRanks = 0;
		for (int round=0; round<3 && devPoints > 0; round++) {
			if (log.isDebugEnabled()) log.debug("generate round "+round+" skills. dp="+devPoints);
			for (ISkill skill : skills) {
				if (!skill.isSpelllist() || (skill.isSpelllist() && round == 0 && spelllistRanks < 5)) { 
					Skillcost costs = bean.getSkillcost(skill);
					if (costs.size() > round) {
						int cost = costs.getCost(round);
						if (devPoints >= cost) {
							/* everyman/restricted/... factor */
							float costsStepsFactor = 1f / bean.getSkillType(skill).getStep().floatValue();
							int chance = (int) (100f - (cost * 5f * costsStepsFactor));
							Rank rank = bean.getSkillRank(skill);		
							/* reduce the chance to increase very high skill ranks (e.g. 1/2 everyman skills will
							 * be very high on higher levels) */
							chance -= Math.max(0, (rank.getRank().longValue() - level) * 4 );
							boolean isFavorit = Boolean.TRUE.equals(rank.getFavorite());
							if ( (DiceUtils.roll(1, 100) < chance || isFavorit) && rank.getRank().longValue() < 9997) {
								try {
									bean.setSkillRank(skill, rank.getRank().add(BigDecimal.valueOf(1)));
									devPoints -= cost;
									if (skill.isSpelllist()) {
										spelllistRanks++;
									}
									/* check if the categorys rank is > 0 */
									SkillCategory cat = bean.getSkillcategory(skill);
									BigDecimal catRank = bean.getSkillcategoryRank(cat).getRank();
									if (catRank.intValue() <= 0) {
										/* we increase the rank */
										Skillcost catCost = bean.getSkillcost(cat);
										if (catCost.size() > 0 && devPoints > catCost.getCost(0)) {
											try {
												bean.setSkillgroupRank(cat, catRank.add(BigDecimal.valueOf(1)) );
												devPoints -= catCost.getCost(0);
											} catch (LevelUpVetoException e) {
												/* ignore */
											}
										}
									}
								} catch (LevelUpVetoException e) {
									/* ignore */
								}
							}
						}
					}
				}
			}
		}
		if (log.isDebugEnabled()) log.debug("generate skills. development points: " + devPoints+ " left ");
		return devPoints;
	}
	
	/* -------------------------------------------------------------------------
	 * 
	 * ------------------------------------------------------------------------- */
	private static final class SkillComparator implements Comparator<ISkill> {
		
		private final RMSheet bean;

		/**
		 * 
		 * @param bean the bean
		 */
		public SkillComparator(RMSheet bean) {
			this.bean = bean;
		}

		@Override
		public int compare(ISkill o1, ISkill o2) {
			Skillcost cost1 = bean.getSkillcost(o1);
			Skillcost cost2 = bean.getSkillcost(o2);
			Rank rank1 = bean.getSkillRank(o1);
			Rank rank2 = bean.getSkillRank(o2);
			/* favorite is top */
			boolean isFavorit1 = rank1.getFavorite() == null ? false : rank1.getFavorite().booleanValue();
			boolean isFavorit2 = rank2.getFavorite() == null ? false : rank2.getFavorite().booleanValue();
			if (isFavorit1 != isFavorit2) {
				return isFavorit1 ? -1 : 1;
			}
			/* check on everyman, restricted */
			BigDecimal steps1 = bean.getSkillType(o1).getStep();
			BigDecimal steps2 = bean.getSkillType(o2).getStep();
			if (!steps1.equals(steps2)) {
				/* higher is better */
				return steps2.compareTo(steps1);
			}
			/* cost compare */
			return cost1.compareTo(cost2);
		}
	}
	
	private static final class SkillCategoryCostComparator implements Comparator<SkillCategory> {
		private final RMSheet bean;

		/**
		 * 
		 * @param bean
		 */
		public SkillCategoryCostComparator(RMSheet bean) {
			this.bean = bean;
		}
		
		/** {@inheritDoc} */
		@Override
		public int compare(SkillCategory o1, SkillCategory o2) {
			Skillcost cost1 = bean.getSkillcost(o1);
			Skillcost cost2 = bean.getSkillcost(o2);
			return cost1.compareTo(cost2);
		}
	}
	
	private static final class SkillCategoryBonusComparator implements Comparator<SkillCategory> {
		private final RMSheet bean;

		/**
		 * 
		 * @param bean
		 */
		public SkillCategoryBonusComparator(RMSheet bean) {
			this.bean = bean;
		}
		
		/** {@inheritDoc} */
		@Override
		public int compare(SkillCategory o1, SkillCategory o2) {
			List<StatEnum> stats1 = bean.getSkillcategoryStats(o1);
			int pot1 = 0;
			for (StatEnum stat : stats1) {
				pot1 += getValue(bean, stat);
			}
			List<StatEnum> stats2 = bean.getSkillcategoryStats(o2);
			int pot2 = 0;
			for (StatEnum stat : stats2) {
				pot2 += getValue(bean, stat);
			}
			return Integer.valueOf(pot2).compareTo(Integer.valueOf(pot1));
		}
		
		public static int getValue(RMSheet sheet, StatEnum stat) {
			int statMaxBonus = sheet.getStatBonusTotal(stat);
			statMaxBonus -= sheet.getStatBonus(stat); /* reduce by temp stat bonus */
			statMaxBonus += sheet.getStatBonus(sheet.getStatPot(stat)); /* add stat pot bonus */
			return statMaxBonus;
		}
	}
}
