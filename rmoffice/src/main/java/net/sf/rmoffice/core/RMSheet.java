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
package net.sf.rmoffice.core;

import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import net.sf.rmoffice.LevelUpVetoException;
import net.sf.rmoffice.RMPreferences;
import net.sf.rmoffice.core.items.MagicalFeature;
import net.sf.rmoffice.core.items.MagicalItem;
import net.sf.rmoffice.meta.Culture;
import net.sf.rmoffice.meta.DivineStatus;
import net.sf.rmoffice.meta.INamed;
import net.sf.rmoffice.meta.IProgression;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.Profession;
import net.sf.rmoffice.meta.Progression;
import net.sf.rmoffice.meta.Race;
import net.sf.rmoffice.meta.Shield;
import net.sf.rmoffice.meta.Skill;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.Skillcost;
import net.sf.rmoffice.meta.Spelllist;
import net.sf.rmoffice.meta.TrainPack;
import net.sf.rmoffice.meta.enums.CharImagePos;
import net.sf.rmoffice.meta.enums.LengthUnit;
import net.sf.rmoffice.meta.enums.MagicalItemFeatureType;
import net.sf.rmoffice.meta.enums.RankType;
import net.sf.rmoffice.meta.enums.ResistanceEnum;
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.meta.enums.ToDoType;
import net.sf.rmoffice.meta.enums.WeightUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The root node of the RoleMaster(tm) sheet. Initialize members in
 * {@link #init(MetaData)}.
 */
public class RMSheet extends AbstractPropertyChangeSupport {
	private final static Logger log = LoggerFactory.getLogger(RMSheet.class);
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	public static enum State {
		RACE_PROF_SELECTION,
		NORMAL
	}
	/* send event to UI, which form elements must be disabled/enabled */
	public static final String PROPERTY_SHEET_ENABLE_ALL = "enableAll";
	public static final String PROPERTY_SHEET_ENABLE_RACE = "enableRace";
	public static final String PROPERTY_SHEET_ENABLE_PROF = "enableProf";
	public static final String PROPERTY_SHEET_ENABLE_CULTURE = "enableCulture";
	/* */
	public static final String PROPERTY_TODO_CHANGED = "todoChanged";
	/* value/data changes */
    public static final String PROPERTY_SKILLCATEGORY_CHANGED = "skillgroups";    
    /** on value change (any bonus, rank) NOT on structure change */
    public static final String PROPERTY_SKILLS_CHANGED = "skills";
    /** on structure change */
    public static final String PROPERTY_SKILL_STRUCTURE_CHANGED = "skillStructure";
    public static final String PROPERTY_SKILL_CATEGORIES = "skillscategories";
    public static final String PROPERTY_MAGICREALM_CHANGED = "magicRealm";
    public static final String PROPERTY_MAGICREALM_EDITABLE = "magicRealmEditable";
    public static final String PROPERTY_RACE = "race";
    public static final String PROPERTY_CULTURE = "culture";
    public static final String PROPERTY_PROGRESSION_POWER = "progressionPower";
    public static final String PROPERTY_PROGRESSION_BODY = "progressionBody";
    public static final String PROPERTY_STAT_TEMP_PREFIX = "stat.temp.";
    public static final String PROPERTY_STAT_BONUS_PREFIX = "stat.bonus.";
    public static final String PROPERTY_STAT_POT_PREFIX = "stat.pot.";
    public static final String PROPERTY_STAT_COST_SUM = "statCostSum";
    public static final String PROPERTY_STAT_MISCBONUS_PREFIX = "stat.misc.bonus";
    public static final String PROPERTY_STAT_MISC2BONUS_PREFIX = "stat.misc2.bonus";
    public static final String PROPERTY_PROFESSION = "profession";
    public static final String PROPERTY_PLAYERNAME = "playerName";
    public static final String PROPERTY_CHARACTERNAME = "characterName";
//    public static final String PROPERTY_CHARACTERISTICS = "characteristics";
    public static final String PROPERTY_SHIELD = "shield";
    public static final String PROPERTY_ARMOR = "armor";
    public static final String PROPERTY_ARMOR_SKILL = "armorSkill";
    public static final String PROPERTY_APPRENTICESHIP = "apprenticeShip";
    public static final String PROPERTY_EXPPOINTS = "ep";
    public static final String PROPERTY_LEVEL = "level";
    public static final String PROPERTY_DEVPPOINTS = "devPoints";
    public static final String PROPERTY_STATE = "state";
    public static final String PROPERTY_MAGICALITEMS = "magicalitems";
	public static final String PROPERTY_EQUIPMENTS = "equipments";
	public static final String PROPERTY_HERBS = "herbs";
	public static final String PROPERTY_INFO_PAGES = "infoPages";
	public static final String PROPERTY_WEIGHT_UNIT = "weightUnit";
	public static final String PROPERTY_LENGTH_UNIT = "lengthUnit";
	public static final String PROPERTY_LEVELUP_MODE = "lvlUpActive";
	public static final String PROPERTY_PRINT_OUTLINE_IMG = "printOutlineImage";
	public static final String IMG_POS_PROP = "imagePos";
	public static final String TALENTSFLAWS_PROP = "talentsFlaws";
	public static final String FATEPOINTS_PROP = "fatepoints";
	public static final String GRACEPOINTS_PROP = "gracepoints";
	public static final String DIVINESTATUS_PROP = "divinestatus";
	public static final String TALENTFLAW_OWN_PAGE_PROP = "talentFlawOwnPage";
	
	/* export to xml */
	private String playerName;
	private String characterName;
	private Long ep; 
	private Integer raceId;
	private Integer cultureId;
	private Integer professionId;
	private String apprenticeShip;
	private Integer shieldId;
	private int armor = 1;
	private Map<StatEnum, Integer> tempAttr;
	private Map<StatEnum, Integer> potAttr;
	private Map<StatEnum, Integer> miscAttrBonus;
	private Map<StatEnum, Integer> misc2StatBonus;
	private Map<Integer, Rank> skillgroupRanks;
	private Map<Integer, String> itemSkillBonus;
	private Map<Integer, Rank> skillRanks;
	private Map<Integer, SkillType> skillTypes; 
	private Set<StatEnum> magicrealm;
	private Characteristics characteristics;
	private Map<Integer, Integer> costsSwitchable;
	private Map<Integer, CustomSkill> customSkills;
	private int nextCustomSkillId;
	private State state;
	private List<ToDo> todos;
	private List<MagicalItem> magicalitems;
	private List<Equipment> equipments;
	private List<Herb> herbs;
	private Coins coins;
	private List<InfoPage> infoPages; 
	private RMLevelUp levelUp;
	private WeightUnit weightUnit;
	private LengthUnit lengthUnit;
	private Boolean printOutlineImage;
	private CharImagePos imagePos;
	private List<TalentFlaw> talentsFlaws;
	private Long fatepoints;
	private Long gracepoints;
	private Boolean talentFlawOwnPage;

	/* must not be exported to XML */
	private transient MetaData data;
	private transient IProgression progressionBody;
	private transient Progression progressionPower;
	private transient int tempAttrSum = 0;
	private transient boolean magicRealmEditable;

	public RMSheet() {
	}
	
	public void init() {
		convertOldFormats();
		firePropertyChange(PROPERTY_MAGICREALM_EDITABLE, null, Boolean.FALSE);
		if (talentsFlaws == null) {talentsFlaws = new ArrayList<TalentFlaw>();}
		if (todos == null) {todos = new ArrayList<ToDo>();}
		if (tempAttr == null) {tempAttr = new HashMap<StatEnum, Integer>();}
		if (potAttr == null) {potAttr = new HashMap<StatEnum, Integer>();}
		if (miscAttrBonus == null) {miscAttrBonus = new HashMap<StatEnum, Integer>();}
		if (misc2StatBonus == null) {misc2StatBonus = new HashMap<StatEnum, Integer>();}
		if (itemSkillBonus == null) {itemSkillBonus = new HashMap<Integer, String>(); }
		if (skillgroupRanks == null)  {skillgroupRanks = new HashMap<Integer, Rank>();}
		if (skillRanks == null) {skillRanks = new HashMap<Integer, Rank>();}
		if (magicrealm == null) {magicrealm = new HashSet<StatEnum>();}
		if (skillTypes == null) {skillTypes = new HashMap<Integer, SkillType>();}
		if (customSkills == null) {
			nextCustomSkillId = -1;
			customSkills = new HashMap<Integer, CustomSkill>();
		}
		if (magicalitems == null) {magicalitems = new ArrayList<MagicalItem>();}
		if (characteristics != null) {characteristics.initialize();}
		if (equipments == null) {equipments = new ArrayList<Equipment>();}
		if (herbs == null) {herbs = new ArrayList<Herb>();}
		if (coins == null) {coins = new Coins();}
		if (levelUp == null) {levelUp = new RMLevelUp();}
		levelUp.init(this, data);
		if (infoPages == null) {
			infoPages = new ArrayList<InfoPage>();
		}
		firePropertyChange(PROPERTY_EQUIPMENTS, null, equipments);
		firePropertyChange(PROPERTY_HERBS, null, herbs);
		firePropertyChange(PROPERTY_MAGICALITEMS, null, magicalitems);
		if (raceId == null || professionId == null || cultureId == null) {
			if (! State.RACE_PROF_SELECTION.equals(getState())) {
				setState(State.RACE_PROF_SELECTION);
				addToDo(new ToDo(RESOURCE.getString("todo.profrace"), ToDoType.RACE_PROF));
			}
			firePropertyChange(PROPERTY_SHEET_ENABLE_ALL, null, Boolean.FALSE);
			firePropertyChange(PROPERTY_SHEET_ENABLE_RACE, null, Boolean.TRUE);
			firePropertyChange(PROPERTY_SHEET_ENABLE_PROF, null, Boolean.TRUE);
		} else {
			setState(State.NORMAL);
			firePropertyChange(PROPERTY_SHEET_ENABLE_ALL, null, Boolean.TRUE);
			firePropertyChange(PROPERTY_SKILL_CATEGORIES, null, null);
			firePropertyChange(PROPERTY_SKILLCATEGORY_CHANGED, null, null);
		}
		if (raceId != null) {
			firePropertyChange(PROPERTY_RACE, null, data.getRace(raceId));
			if ( ! State.RACE_PROF_SELECTION.equals(getState())) {
				updateProgressionBody();
				firePropertyChange(PROPERTY_SHEET_ENABLE_RACE, null, Boolean.FALSE);
			}
		} else {
			firePropertyChange(PROPERTY_RACE, null, null);
		}
		if (cultureId != null) {
			firePropertyChange(PROPERTY_CULTURE, null, data.getCulture(cultureId));
			if ( ! State.RACE_PROF_SELECTION.equals(getState())) {
				firePropertyChange(PROPERTY_SHEET_ENABLE_CULTURE,null, Boolean.FALSE);
			}
		}
		if (professionId != null) {
			firePropertyChange(PROPERTY_PROFESSION, null, data.getProfession(professionId));
			if ( ! State.RACE_PROF_SELECTION.equals(getState())) {
				firePropertyChange(PROPERTY_SHEET_ENABLE_PROF, null, Boolean.FALSE);
			}
		} else {
			firePropertyChange(PROPERTY_PROFESSION, null, null);
		}
		firePropertyChange(PROPERTY_STATE, null, getState());
		if (costsSwitchable == null) {
			costsSwitchable = new HashMap<Integer, Integer>();
		}
		for (SkillCategory group : data.getSkillCategories()) {
			if (group.getRankType().isCostSwitchable()) {
				if (!costsSwitchable.containsKey(group.getId())) {				
					costsSwitchable.put(group.getId(), group.getId());
				}
			}
		}
		if (getState() == State.NORMAL) {
			updateMagicProgessionAndRealm();
			updateTempSum(false);
		}
		firePropertyChange(PROPERTY_MAGICREALM_CHANGED, null, this.magicrealm);
		for (StatEnum stat : StatEnum.values()) {
			firePropertyChange(PROPERTY_STAT_TEMP_PREFIX+stat.name(), null, Integer.valueOf(getStatTemp(stat)));
			firePropertyChange(PROPERTY_STAT_POT_PREFIX+stat.name(), null, Integer.valueOf(getStatPot(stat)));
			firePropertyChange(PROPERTY_STAT_MISCBONUS_PREFIX+stat.name(), null, Integer.valueOf(getStatMiscBonus(stat)));
			firePropertyChange(PROPERTY_STAT_BONUS_PREFIX+stat.name(), null, null);
		}
		if (armor < 1 || armor > 20) {
			armor = 1;
		}
		firePropertyChange(PROPERTY_ARMOR, null, Integer.valueOf(getArmor()));
		if (shieldId == null) {
			setShield(null);
		} else {
			firePropertyChange(PROPERTY_SHIELD, null, getShield());
		}
		
		/* weight/length unit */
		if (weightUnit == null) {
			setWeightUnit(getWeightUnit());
		} else {
			firePropertyChange(PROPERTY_WEIGHT_UNIT, null, this.weightUnit);
		}
		if (lengthUnit == null) {
			setLengthUnit(getLengthUnit());
		} else {
			firePropertyChange(PROPERTY_LENGTH_UNIT, null, this.lengthUnit);
		}
		if (printOutlineImage == null) {
			printOutlineImage = Boolean.valueOf(RMPreferences.getInstance().printOutlineImage());
		}
		if (fatepoints == null) {
			fatepoints = Long.valueOf(0);
		}
		if (gracepoints == null) {
			setGracepoints(Long.valueOf(0));
		} else {
			/* we need to update all possible events */
			Long stored = this.gracepoints;
			gracepoints = null;
			setGracepoints(stored);
		}
		firePropertyChange(PROPERTY_PRINT_OUTLINE_IMG, null, this.printOutlineImage);
		/* refresh todos */
		firePropertyChange(PROPERTY_TODO_CHANGED, null, null);
		firePropertyChange(PROPERTY_APPRENTICESHIP, null, this.apprenticeShip);
		firePropertyChange(PROPERTY_EXPPOINTS, null, this.ep);
		firePropertyChange(PROPERTY_LEVEL, null, Long.valueOf(getLevel()));
		firePropertyChange(PROPERTY_DEVPPOINTS, null, Integer.valueOf(getDevPoints()));
		firePropertyChange(FATEPOINTS_PROP, null, this.fatepoints);
		updateTempSum(true);
	}

	/**
	 * Converts old formats
	 */
	private void convertOldFormats() {
		if (cultureId == null && raceId != null) {
			/* we have an old format (4.0) that had no culture */
			if (raceId.intValue() < 25) {
				cultureId = raceId;
			}
			switch (raceId.intValue()) {
				case 26:
					cultureId = Integer.valueOf(31);
					break;
				case 27:
					cultureId = Integer.valueOf(27);
					raceId = Integer.valueOf(33);
					break;
				case 28:
					cultureId = Integer.valueOf(28);
					raceId = Integer.valueOf(33);
					break;
				case 29:
					cultureId = Integer.valueOf(29);
					raceId = Integer.valueOf(33);
					break;
				case 30:
					cultureId = Integer.valueOf(30);
					raceId = Integer.valueOf(33);
					break;
				case 31:
					cultureId = Integer.valueOf(31);
					raceId = Integer.valueOf(33);
					break;
				case 32:
					cultureId = Integer.valueOf(32);
					raceId = Integer.valueOf(33);
					break;
			}
		}
		/* weight convert */
		if (characteristics != null && characteristics.weight != null) {
			try {
				characteristics.setWeight(Integer.parseInt(characteristics.weight.replaceAll("[^0-9]", "")));
			} catch (Exception e) {
				if (log.isWarnEnabled()) log.warn("Could not convert deprecated weight field");
			}
			characteristics.weight = null; 
		}
		/* Bug: Skill 103 was duplicate --> must be 242 */
		/*      Skill 246 was duplicate --> must be 299 (Long bow) 
		 *      Skill 354/355 were duplicate --> must be 348/349 (Kynac)
		 *      */
		if (skillRanks != null) {
			int toReplace[]   = new int[] {103, 246, 354, 355};
			int replacement[] = new int[] {242, 299, 348, 349};
			for (int i=0; i<toReplace.length; i++) {
				Rank rankToReplace = skillRanks.remove(Integer.valueOf(toReplace[i]));
				Rank rankReplacement = skillRanks.get(Integer.valueOf(replacement[i]));
				if (rankReplacement == null && rankToReplace != null) {
					if (log.isInfoEnabled()) log.info("converting the skill "+toReplace[i]+" to "+replacement[i]);
					rankToReplace.setId(Integer.valueOf(replacement[i]));
					skillRanks.put(Integer.valueOf(replacement[i]), rankToReplace);
				}
			}
		}
		/* since 4.2.4 equipment weight is kg (cm was before) */
		if (equipments != null) {
			for (Equipment eq : equipments) {
				if (eq.weight != null) {
					eq.setWeight(eq.weight.intValue() / 1000f); /* g to kg*/
				}
				eq.weight = null;
			}
		}
	}

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		String oldValue = this.characterName;
		this.characterName = characterName;
		firePropertyChange(PROPERTY_CHARACTERNAME, oldValue, this.characterName);
	}
	
	/**
	 * Returns the experience points.
	 * 
	 * @return the experience, not {@code null}
	 */
	public Long getEp() {
		if (ep == null) return Long.valueOf(10000);
		return ep;
	}
	
	/**
	 * Sets the  experience points
	 * 
	 * @param ep the experience
	 */
	public void setEp(Long ep) {
		Long oldValue = this.ep;
		Long oldLevel = Long.valueOf(getLevel());
		this.ep = ep;
		firePropertyChange(PROPERTY_EXPPOINTS, oldValue, this.ep);
		Long newLevel = Long.valueOf(getLevel());
		firePropertyChange(PROPERTY_LEVEL, oldLevel, newLevel);
	}
	
	public void setLevel(long newLevel) {
		long oldLevel = getLevel();
		long level = newLevel;
		if (level < 1) {
			level = 1;
		}
		if (level != oldLevel) {
			int l = 0;
			long ep = 0;
			do {
				ep += 10000;
				l = getLevel(ep);
			} while (l != level);
			firePropertyChange(PROPERTY_LEVEL, Long.valueOf(oldLevel), Long.valueOf(level));
			setEp(Long.valueOf(ep));
		}
	}
	
	public long getLevel() {
		return getLevel(getEp().longValue());
	} 
	
	private int getLevel(final long ep) {
		int level = 0;
		int ep1000th = BigDecimal.valueOf(ep).divide( BigDecimal.valueOf(1000), RoundingMode.CEILING).intValue();
		if (ep1000th >= 500 /* 500.000 EP */) {
			ep1000th -= 500;
			level = 20 + (int) Math.ceil( ep1000th / 50 );
		} else if (ep1000th >= 300 /* 300.000 EP */) {
			ep1000th -= 300;
			level = 15 + (int) Math.ceil( ep1000th / 40 );
		} else if (ep1000th >= 150 /* 150.000 EP */) {
			ep1000th -= 150;
			level = 10 + (int) Math.ceil( ep1000th / 30 );
		} else if (ep1000th >= 50 /* 50.000 EP */) {
			ep1000th -= 50;
			level = 5 + (int) Math.ceil( ep1000th / 20 );
		} else {
			level = (int) Math.ceil( ep1000th / 10 );
		}
		return level;
	}

	/**
	 * 
	 * @return the race or {@code null} if not selected, yet
	 */
	public Race getRace() {
		if (raceId == null) return null;
		return data.getRace(raceId);
	}

	/**
	 * 
	 * @param race the new race, not {@code null}
	 */
	public void setRace(Race race) {
		if ( ! State.RACE_PROF_SELECTION.equals(getState()) ) throw new IllegalStateException("You can only set the race in state "+State.RACE_PROF_SELECTION);	
		Race oldValue = getRace();
		if (race != null) {
			this.raceId = race.getId();
		} else {
			this.raceId = null;
		}
		firePropertyChange(PROPERTY_RACE, oldValue, race);
		if (oldValue == null) {
			updateMagicProgessionAndRealm();
		}
		List<Culture> cultureForRace = data.getCultureForRace(race);
		if (log.isDebugEnabled()) log.debug("found "+cultureForRace.size()+" cultures for race id "+raceId);
		if (cultureForRace.size() > 1) {
			firePropertyChange(PROPERTY_SHEET_ENABLE_CULTURE, null, Boolean.TRUE);
			setCulture(null);
		} else {
			firePropertyChange(PROPERTY_SHEET_ENABLE_CULTURE, null, Boolean.FALSE);
			if (cultureForRace.size() > 0) {
				setCulture(cultureForRace.get(0));
			}
		}
		updateProgressionBody();
	}

	public void setCulture(Culture culture) {
		if ( ! State.RACE_PROF_SELECTION.equals(getState()) ) throw new IllegalStateException("You can only set the culture in state "+State.RACE_PROF_SELECTION);			
		Culture oldValue = getCulture();
		if (culture == null) {
			this.cultureId = null;
		} else {
			this.cultureId = culture.getId();
		}
		firePropertyChange(PROPERTY_CULTURE, oldValue, culture);
		/* set avg values weight and height */
		if (culture != null && getCharacteristics() != null) {
			getCharacteristics().setWeight(culture.getWeightHeight().getWeightAvg(getCharacteristics().isFemale()));
			getCharacteristics().setHeight(culture.getWeightHeight().getHeightAvg(getCharacteristics().isFemale()));
		}
	}
	
	
	public Culture getCulture() {
		if (cultureId == null) return null;
		return data.getCulture(cultureId);
	}
	
	/**
	 * 
	 * @return the profession or {@code null} if not selected, yet
	 */
	public Profession getProfession() {
		if (professionId == null) return null;
		return data.getProfession(professionId);
	}

	public void setProfession(Profession profession) {
		if ( ! State.RACE_PROF_SELECTION.equals(getState()) ) throw new IllegalStateException("You can only set the profession in state "+State.RACE_PROF_SELECTION);
		Profession old = getProfession();
		this.professionId = profession.getId();
		firePropertyChange(PROPERTY_PROFESSION, old, getProfession());
		updateMagicProgessionAndRealm();
	}

	public void setStatTemp(StatEnum stat, int value, boolean sendChangeEvent) {
		Integer oldDevPoints = Integer.valueOf(getDevPoints());
		int oldBonus = getStatBonus(stat);
		Integer oldValue = tempAttr.get(stat);
		tempAttr.put(stat, Integer.valueOf(value));
		int newBonus = getStatBonus(stat);
		firePropertyChange(PROPERTY_STAT_BONUS_PREFIX+stat.name(), oldValue, Integer.valueOf(value));
		firePropertyChange(PROPERTY_DEVPPOINTS, oldDevPoints, Integer.valueOf(getDevPoints()));
		if (sendChangeEvent) {
			firePropertyChange(PROPERTY_STAT_TEMP_PREFIX+stat.name(), oldValue, Integer.valueOf(value));
		}
		updateTempSum(true);
		if (newBonus != oldBonus) {
			/* one bonus has changed, so possible all skills and skillgroups changed, too*/
			firePropertyChange(PROPERTY_SKILLCATEGORY_CHANGED, null, null);
			firePropertyChange(PROPERTY_SKILLS_CHANGED, null, null);
		}
		
	}
	
	public void setStatPot(StatEnum stat, int value, boolean sendChangeEvent) {
		Integer oldValue = potAttr.get(stat);
		potAttr.put(stat, Integer.valueOf(value));
		if (sendChangeEvent) {
			firePropertyChange(PROPERTY_STAT_POT_PREFIX+stat.name(), oldValue, Integer.valueOf(value));
		}
	}

	/**
	 * Return the {@link Progression} depending on the race.
	 * If the is no {@link Progression} available, it returns an empty {@link Progression}.
	 * 
	 * @return the hit point {@link Progression}, not {@code null}
	 * 
	 */
	public IProgression getProgressionBody() {
		IProgression prog = null;
		if (progressionBody == null) {
			prog = new Progression(0, 0, 0, 0, 0);
		} else {
			prog = progressionBody;
		}
		/* search in talent flaw */
		if (talentsFlaws != null) {
			for (TalentFlaw tf : talentsFlaws) {
				if (tf.getProgressionBody() != null) {
					prog = prog.modify(tf.getProgressionBody());
				}
			}
		}
		return prog;
	}

	private void setProgressionBody(IProgression progressionBody) {
		IProgression old = this.progressionBody;
		this.progressionBody = progressionBody;
		firePropertyChange(PROPERTY_PROGRESSION_BODY, old, getProgressionBody());
	}

	/**
	 * Return the {@link Progression} depending on the magic realm (Magiebereich).
	 * If the is no {@link Progression} available, it returns an empty {@link Progression}.
	 * 
	 * @return the magical {@link Progression}, not {@code null}
	 * 
	 */
	public IProgression getProgressionPower() {
		IProgression prog = null;
		if (progressionPower == null) {
			prog = new Progression(0, 0, 0, 0, 0);
		} else {
			prog = progressionPower;
		}
		/* search in talent flaw */
		if (talentsFlaws != null) {
			for (TalentFlaw tf : talentsFlaws) {
				if (tf.getProgressionPower() != null) {
					prog = prog.modify(tf.getProgressionPower());
				}
			}
		}
		return prog;
	}

	private void setProgressionPower(Progression progressionPower) {
		Progression oldVal = this.progressionPower;
		this.progressionPower = progressionPower;
		if (oldVal == null || this.progressionPower == null || getProgressionPower().compareTo(oldVal) != 0) {
			firePropertyChange(PROPERTY_PROGRESSION_POWER, oldVal, getProgressionPower());
		}
	}

	public int getStatBonus(StatEnum attr) {
		int value = getStatTemp(attr);
		return getStatBonus(value);
	}

	/**
	 * 
	 * @param value the stat bonus
	 * @return the stat bonus
	 */
	public int getStatBonus(int value) {
		int bonus;
		if (value > 100) {
			bonus = (int) Math.round((value - 95.0) * 2);
		} else if (value > 89) {
			bonus = (int) Math.round((value - 81.0) / 2);
		} else if (value > 69) {
			bonus = (int) Math.round((value - 67.0) / 5);
		} else if (value > 30) {
			bonus = 0;
		} else if (value > 10) {
			bonus = (int) Math.round((value - 33.0) / 5);
		} else {
			bonus = (int) Math.round((value - 21.0) / 2);
		}
		return bonus;
	}

	public int getStatBonusTotal(StatEnum stat) {
		if (getState() == null || getState() == State.RACE_PROF_SELECTION) return 0;
		int b = getStatBonus(stat);
		b += data.getRace(raceId).getStatBonus(stat);
		b += getStatMiscBonus(stat);
		b += getStatMisc2Bonus(stat);
		return b;
	}

	/**
	 * Returns the resistance stat bonus.
	 * 
	 * @param res the resistance type, not {@code null}
	 * @return the bonus for given resistance
	 */
	public int getResistanceStatBonus(ResistanceEnum res) {
		return 3 * getStatBonusTotal(res.getStat());
	}
	
	/**
	 * Returns the resistance bonus from the talent and flaws.
	 * 
	 * @param res the resistance type, not {@code null}
	 * @return the special bonus
	 */
	public int getResistanceSpecialBonus(ResistanceEnum res) {
		int bonus = 0;
		if (talentsFlaws != null) {
			for (TalentFlaw tf : talentsFlaws) {
				if (tf.getResistanceBonus(res) != null) {
					bonus += tf.getResistanceBonus(res).intValue();
				}
			}
		}
		return bonus;
	}
	
	/**
	 * Returns the resistance total bonus including race, stat and talent flaw bonus.
	 * 
	 * @param res the resistance type, not {@code null}
	 * @return the total bonus
	 */
	public int getResistanceBonusTotal(ResistanceEnum res) {
		return getResistanceStatBonus(res) + 
				getResistanceSpecialBonus(res) + 
				getRace().getResistanceBonus(res);
	}

	public int getTempSum() {
		return tempAttrSum;
	}

	/**
	 * 
	 * @return the sum of available education points (Ausbildungspunkte)
	 */
	public int getDevPoints() {
		int ap = 0;
		int count = 0;
		for (StatEnum at : StatEnum.values()) {
			if (at.isForAPCalculation()) {
				ap += getStatTemp(at);
				count++;
			}
		}
		return Math.round( ap / count );
	}

	public int getSkillcategoryStatBonus(SkillCategory category) {
		int bonus = 0;
		for (StatEnum stat : getSkillcategoryStats(category)) {
			bonus += getStatBonusTotal(stat);
		}
		/* for dual and arcane we have to use the average */
		if (category.getRankType().isMagical()) {
			if (getSkillcategoryStats(category).size() > 0) {
				bonus = (int) Math.ceil(bonus
						/ getSkillcategoryStats(category).size());
			}
		}
		return bonus;
	}

	/**
	 * Returns the correct attributes for the given {@link SkillCategory}. It
	 * resolves the {@link StatEnum} for the {@link SkillCategory} if it has
	 * {@link RankType#M}.
	 * 
	 * @param skillCategory
	 * @return a list of attribute
	 */
	public List<StatEnum> getSkillcategoryStats(SkillCategory skillCategory) {
		if (skillCategory.getRankType().isProgressionMagic() || skillCategory.getRankType().isOwnRealm()) {			
			List<StatEnum> attr = new ArrayList<StatEnum>();
			if (isMagicRealmEditable()) {
				for (StatEnum ae : getMagicRealm()) {
					attr.add(ae);
				}
			} else {
				if (getProfession() != null) {
					for (StatEnum ae : getProfession().getStats()) {
						if (ae.isForMagic()) {
							attr.add(ae);
						}
					}
				}
			}
			return attr;
		} else {
			return skillCategory.getAttributes();
		}
	}

	public Rank getSkillcategoryRank(SkillCategory category) {
		if (skillgroupRanks.containsKey(category.getId())) {
			Rank rank = skillgroupRanks.get(category.getId());
			return rank;
		} else {
			Rank r = new Rank();
			r.setId(category.getId());
			r.setRank(BigDecimal.valueOf(0));
			skillgroupRanks.put(category.getId(), r);
			return r;
		}		
	}

	/**
	 * Fires {@link #PROPERTY_SKILLCATEGORY_CHANGED} and {@link #PROPERTY_SKILLS_CHANGED} property change event.
	 * 
	 * @param category the category to set the new rank for, not {@code null}
	 * @param rank
	 * @throws LevelUpVetoException if skill group rank operation is not allowed
	 */
	public void setSkillcategoryRank(SkillCategory category, BigDecimal rank) throws LevelUpVetoException {
		levelUp.modifySkillcategoryRank(category, rank);
		internalSetSkillcategoryRank(category, rank);
	}

	/**
	 * 
	 * @param category
	 * @param rank
	 */
	private void internalSetSkillcategoryRank(SkillCategory category, BigDecimal rank) {
		if ( category.getRankType().isGroupRankEditable() ) {
			Rank rankObj = getSkillcategoryRank(category);
			rankObj.setRank(rank);
			if (log.isDebugEnabled()) log.debug("increased rank of skill category "+category.getName() +" to "+rank);
			firePropertyChange(PROPERTY_SKILLCATEGORY_CHANGED, null, rankObj);
			firePropertyChange(PROPERTY_SKILLS_CHANGED, null, null);
			/* check, if the modified skill category is for current armor */
			if (data.getArmorSkill(getArmor()) != null && getSkillcategory(data.getArmorSkill(getArmor())).equals(category)) {
				firePropertyChange(PROPERTY_ARMOR_SKILL, null, null);
			}
		} else {
			if (log.isWarnEnabled()) log.warn("Cannot edit the skillgroup rank for group id "+category.getId());
		}
	}

	/**
	 * 
	 * @param category the skill category
	 * @return the bonus depending on the current rank
	 */
	public int getSkillcategoryRankBonus(SkillCategory category) {
		if (skillgroupRanks.containsKey(category.getId())) {
			Rank rank = skillgroupRanks.get(category.getId());
			IProgression prog = data.getSkillgroupProgression(category.getRankType());
			return prog.getBonus(rank.getRank().toBigInteger().intValue());
		}
		return 0;
	}

	/**
	 * Return the current rank object or creates a new rank object for the given skill. 
	 * 
	 * @param skill the skill
	 * @return the rank object of skill, not {@code null}
	 */
	public Rank getSkillRank(ISkill skill) {
		if (skillRanks.containsKey(skill.getId())) {
			return skillRanks.get(skill.getId());
		}
		Rank r = new Rank();
		r.setId(skill.getId());
		r.setRank(BigDecimal.valueOf(0));
		return r;
	}
	
	public void removeSkillRank(ISkill skill) {
		levelUp.removeSkill(skill);
		if (skillRanks.containsKey(skill.getId())) {
			skillRanks.remove(skill.getId());
		}
	}
		

	/**
	 * Fires {@link #PROPERTY_SKILLS_CHANGED} property change event.
	 * @param skill the skill to set the rank for 
	 * @param rank the new rank
	 * @throws LevelUpVetoException if in level up mode the operation is not allowed
	 */
	public void setSkillRank(ISkill skill, BigDecimal rank) throws LevelUpVetoException {
		boolean needSecondRun = levelUp.modifySkillRank(skill, rank);
		internalSetSkillRank(skill, rank);
		if (needSecondRun) {
			levelUp.decreaseSkillRankSecondRun(skill);
		}
	}

	/*
	 * Fires {@link #PROPERTY_SKILLS_CHANGED} property change event.
	 * @param skill the skill to set the rank for 
	 * @param rank the new rank
	 */
	private void internalSetSkillRank(ISkill skill, BigDecimal rank) {
		Rank r = null;
		if (skillRanks.containsKey(skill.getId())) {
			 r = skillRanks.get(skill.getId());
		} else {
			r = new Rank();
			r.setId(skill.getId());
			r.setRank(BigDecimal.valueOf(0));
			skillRanks.put(skill.getId(), r);
		}
		BigDecimal delta = rank.subtract(r.getRank());
		if (delta.doubleValue() > 0) {
			/* increase */
			BigDecimal newRank = r.getRank().add( getSkillType(skill).getStep() );
			r.setRank(newRank);
			if (log.isDebugEnabled()) log.debug("increased rank of skill "+skill.getName()+" to "+newRank);
		} else if (delta.doubleValue() < 0) {
			/* decrease */
			BigDecimal newRank = r.getRank().subtract( getSkillType(skill).getStep() );
			if (newRank.floatValue() >= 0) {
				r.setRank(newRank);
				if (log.isDebugEnabled()) log.debug("decreased rank of skill "+skill.getName()+" to "+newRank);
			}
		}
		firePropertyChange(PROPERTY_SKILLS_CHANGED, null, null);
		/* check, if the modified skill is for current armor */
		if (data.getArmorSkill(getArmor()) != null && data.getArmorSkill(getArmor()).equals(skill)) {
			firePropertyChange(PROPERTY_ARMOR_SKILL, null, null);
		}
	}

	/**
	 * Returns an unmodifiable collection of the current ranks.
	 * 
	 * @return an unmodifiable list of ranks, not {@code null}
	 */
	public Collection<Rank> getSkillRanks() {
		return Collections.unmodifiableCollection(skillRanks.values());
	}
	
	/**
	 * Fires {@link #PROPERTY_SKILLS_CHANGED} property change event.
	 * @param skill the skill to set the rank for 
	 * @param bonus the new bonus
	 */
	public void setSkillSpecialBonus(ISkill skill, int bonus) {
		Rank r = null;
		if (skillRanks.containsKey(skill.getId())) {
			 r = skillRanks.get(skill.getId());
		} else {
			r = new Rank();
			r.setId(skill.getId());
			r.setRank(BigDecimal.valueOf(0));
			skillRanks.put(skill.getId(), r);
		}
		r.setSpecialBonus(Integer.valueOf(bonus));
		firePropertyChange(PROPERTY_SKILLS_CHANGED, null, null);
		/* check, if the modified skill is for current armor */
		if (data.getArmorSkill(getArmor()) != null && data.getArmorSkill(getArmor()).equals(skill)) {
			firePropertyChange(PROPERTY_ARMOR_SKILL, null, null);
		}
	}

	/**
	 * @param skill the skill 
	 * @return the rank bonus of the current skill rank
	 * 
	 */
	public int getSkillRankBonus(ISkill skill) {
		int rk = 0;
		if (skillRanks.containsKey(skill.getId())) {
			Rank rank = skillRanks.get(skill.getId());
			/* ignore precision parts */
			if (rank.getRank() != null) {
				rk = rank.getRank().toBigInteger().intValue();
			}
		}
		IProgression prog = getSkillProgression(getSkillcategory(skill).getRankType());
		return prog.getBonus(rk);
	}

	/**
	 * Returns the sum of rank bonus, skillgroup total bonus, special skill bonus.
	 * Bonus does not include item skill bonus because the magical items will be
	 * printed under the skill. Example: Character will not get bonus of 
	 * two 2h-weapons.
	 * 
	 * @param skill the skill
	 * @return the total bonus of the given skill 
	 */
	public int getSkillTotalBonus(ISkill skill) {
		int bonus = getSkillRankBonus(skill);
		bonus += getSkillcategoryTotalBonus(getSkillcategory(skill));
		/* special bonus */
		if (skillRanks.containsKey(skill.getId())) {
			Rank rank = skillRanks.get(skill.getId());
			if (rank.getSpecialBonus() != null) {
				bonus += rank.getSpecialBonus().intValue();
			}
		}
		bonus += getSkillSpecialBonus(skill);
		return bonus;
	}
	
	/**
	 * Returns the calculated bonus for the given skill. The bonus is
	 * calculated from all talents and flaws.
	 * 
	 * @param skill the skill, not {@code null}
	 * @return the calculated bonus
	 */
	public int getSkillSpecialBonus(ISkill skill) {
		int bonus = 0;
		/* Talent/Flaw special bonus */
		for (TalentFlaw tf : getTalentsFlaws()) {
			if (tf.getSkillBonus() != null) {
				if (tf.getSkillBonus().containsKey(skill.getId())) {
					bonus += tf.getSkillBonus().get(skill.getId()).intValue();
				}
			} else if (skill instanceof Spelllist) {
				Spelllist spelllist = (Spelllist)skill;
				for (StatEnum stat : spelllist.getAttributes()) {
					Integer spellBonus = tf.getSpellRealmBonus(stat);
					if (spellBonus != null) {
						bonus += spellBonus.intValue();
					}
				}
			}
		}
		return bonus;
	}

	/**
	 * Returns the {@link Progression} for the given skill.
	 * 
	 * @param rankType
	 * @return progression for skill, not {@code null}
	 * @throws IllegalArgumentException if the {@link RankType} is not supported
	 */
	public IProgression getSkillProgression(RankType rankType) {
		if (rankType.isLimited()) {
			return data.getLimitedSkillProg();
		} else if (rankType.isDefaultProgression()) {
			return data.getDefaultSkillProg();
		} else if (rankType.isCombined()) {
			return data.getCombinedSkillProg();
		} else if (rankType.isProgressionBody()) {
			return getProgressionBody();
		} else if (rankType.isProgressionMagic()) {
			return getProgressionPower();
		}
		throw new IllegalArgumentException("Unsupported RankType");
	}

	public int getSkillcategoryTotalBonus(SkillCategory category) {
		int bonus = getSkillcategoryRankBonus(category);
		bonus += getSkillcategoryStatBonus(category);
		/* profession bonus */
		if (getProfession() != null) {
			bonus += getProfession().getSkillgroupBonus(category.getId().intValue());
		}
		/* special */
		Integer userDefinedSpecialBonus = getSkillcategoryRank(category).getSpecialBonus();
		if (userDefinedSpecialBonus != null) {
			bonus += userDefinedSpecialBonus.intValue();
		}
		/* Special bonus (calculated) */
		bonus += getSkillcategorySpecial1Bonus(category);
		return bonus;
	}

	public int getStatTemp(StatEnum stat) {
		if (tempAttr == null) return 0;
		if (tempAttr.containsKey(stat)) {
			return tempAttr.get(stat).intValue();
		}
		return 0;
	}

	public int getStatPot(StatEnum at) {
		if (potAttr == null) return 0;
		if (potAttr.containsKey(at)) {
			return potAttr.get(at).intValue();
		}
		return 0;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		String oldValue = this.playerName;
		this.playerName = playerName;
		firePropertyChange(PROPERTY_PLAYERNAME, oldValue, playerName);
	}

	public int getRecoverHits(boolean sleep) {
		int reg = getStatBonusTotal(StatEnum.CONSTITUTION);
		if (sleep) {
			reg = reg * 2;
			if (reg < 3) {
				reg = 3;
			}
		} else {
			reg = Math.round((float) reg / 2);
			if (reg < 1) {
				reg = 1;
			}
		}
		return reg;
	}
	
	public int getRecoverExhaust() {
		int rec = 1 + getStatBonusTotal(StatEnum.CONSTITUTION) / 2;
		if (rec < 1) {
			rec = 1;
		}
		return rec;
	}
	
	public SkillCategory getSkillcategory(ISkill skill) {
		if (skill instanceof Spelllist) {
			Spelllist spelllist = (Spelllist) skill;
			/* compare magic realm */
			boolean spelllistIsOwnRealm = false;
			if (getMagicRealm().containsAll(spelllist.getAttributes())) {
				spelllistIsOwnRealm = true;
			}
			boolean charIsArcane = false;
			if (getMagicRealm().size() == 3) {
				charIsArcane = true;
			}
			/* resolve the skillgroup */			
			Integer profId = getProfession() == null ? Integer.valueOf(-1) : getProfession().getId();
			for (SkillCategory sg : data.getSkillCategories()) {
				if (sg.getRankType().isMagical() && sg.getRankSubType() != null) {
					if ( /* skill group && spelllist are own realm
					        or both other realm 
					        or special handling for arcane users */
					  sg.getRankType().isOwnRealm() && spelllistIsOwnRealm ||
					  ! sg.getRankType().isOwnRealm() && ! spelllistIsOwnRealm ||
					  charIsArcane && sg.getAttributes().size() == 3) {
						
						if (spelllist.getSpelllistType().isOpen() && sg.getRankSubType().isOpen() ||
						    spelllist.getSpelllistType().isClosed() && sg.getRankSubType().isClosed()) {
							/* open/closed skill group  */
							if ((sg.getAttributes().size() == 0 && sg.getAttributes().size() != 3) || statListEquals(spelllist.getAttributes(), sg.getAttributes())) {
								return sg;
							}
						} else if (spelllist.getSpelllistType().isProfession()) {
							boolean containsProfId = spelllist.getSpelllistType().getProfessionIds().contains(profId);
							if (containsProfId && sg.getRankSubType().isBase()) {
								/* base skill group */
								return sg;
							} else if ((!containsProfId) && sg.getRankSubType().isProfession()) {
								/* other profession skill group */
								      /*    other base group    AND   not arcane */
								if ( ( sg.getAttributes().size() == 0 && sg.getAttributes().size() != 3 ) || spelllist.getAttributes().containsAll(sg.getAttributes())) {
									return sg;
								}
							}
						}
					}
				}
			}
		} else if (skill instanceof Skill) {
			return ((Skill)skill).getCategory();
		} else if (skill instanceof CustomSkill) {
			ISkill actualSkill = data.getSkill( ((CustomSkill)skill).getActualSkillId() );
			return getSkillcategory(actualSkill);
		}
		log.error("Could not find any skillgroup for skill id "+skill.getId());
		return null;
	}
	
	private boolean statListEquals(Collection<StatEnum> c1, Collection<StatEnum> c2) {
		if (c1 == null && c2 == null)  return true;
		if (c1 == null || c2 == null) return false;
		if (c1.size() != c2.size()) return false;
		Iterator<StatEnum> it1 = c1.iterator();
		while (it1.hasNext()) {
			if (!c2.contains(it1.next())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the PP regeneration including divine status modifications.
	 * 
	 * @param sleep whether sleeping or resting recovery
	 * @return recovery points
	 */
	public int getRecoverPP(boolean sleep) {
		ISkill skillMEntw = null;
		for (ISkill skill : data.getSkills()) {
			if (getSkillcategory(skill).getRankType().isProgressionMagic()) {
				skillMEntw = skill;
				break;
			}
		}
		if (skillMEntw == null)
			return 0;

		int recovery = 0;
		if (sleep) {
			/* sleeping recovery */
			recovery = getSkillTotalBonus(skillMEntw);
			recovery = Math.round(recovery / 2);
			if (recovery < 3) {
				recovery = 3;
			}
			recovery = Math.round(recovery  * getDivineStatusObject().getPPRegenerationFactor());
		} else {
			/* resting recovery */
			List<StatEnum> skillgroupAttributes = getSkillcategoryStats(getSkillcategory(skillMEntw));
			for (StatEnum at : skillgroupAttributes) {
				recovery += getStatBonusTotal(at);
			}
			if (skillgroupAttributes.size() == 0) {
				recovery = 0;
			} else {
				recovery = Math.round(recovery / skillgroupAttributes.size());
			}

			recovery = Math.round((float) recovery / 2);
			if (recovery < 1) {
				recovery = 1;
			}
			recovery = Math.round(recovery * getDivineStatusObject().getPPRegenerationFactor());
		}
		if (recovery > getPowerPoints()) {
			recovery = getPowerPoints();
		}
		return recovery;
	}

	/**
	 * Returns the exhaustion points of the character including the talent and flaw modification.
	 * 
	 * @return exhaustion points
	 */
	public int getExhaustionPoints() {
		int exh = 40 + getStatBonusTotal(StatEnum.CONSTITUTION) * 3;
		if (getRace() != null) {
			exh += getRace().getExhaustionPoints();
		}
		float factor = 1;
		if (talentsFlaws != null) {
			for (TalentFlaw tf : talentsFlaws) {
				if (tf.getExhaustion() != null) {
					exh += tf.getExhaustion().intValue();
				}
				if (tf.getExhaustionMultiplier() != null) {
					factor *= tf.getExhaustionMultiplier().floatValue();
				}
			}
		}
		
		return Math.round(exh * factor);
	}

	public boolean isMagicRealmEditable() {
		return magicRealmEditable;
	}

	private void setMagicRealmEditable(boolean editable) {
		if (editable != this.magicRealmEditable) {
			boolean oldValue = this.magicRealmEditable;
			this.magicRealmEditable = editable;
			firePropertyChange(PROPERTY_MAGICREALM_EDITABLE,
					Boolean.valueOf(oldValue),
					Boolean.valueOf(this.magicRealmEditable));
		}
	}

	public Set<StatEnum> getMagicRealm() {
		if (magicrealm == null) {
			return new HashSet<StatEnum>();
		} else {
			return magicrealm;
		}
	}

	/**
	 * Check {@link #isMagicRealmEditable()} before. Only is its editable you
	 * are allowed to set it.
	 * 
	 * @param stat the magical stat set by user
	 * @throws UnsupportedOperationException
	 *             if {@link #isMagicRealmEditable()} is not editable
	 * @throws IllegalArgumentException
	 *             if the {@link StatEnum#isForMagic()} is false
	 */
	public void setMagicRealm(StatEnum stat) {
		if (isMagicRealmEditable()) {
			if (!stat.isForMagic()) {
				throw new IllegalArgumentException("Not a magical attribute: "+stat);
			}
			magicrealm = new HashSet<StatEnum>();
			magicrealm.add(stat);
			/* we don't send the old value, to avoid an unselected magic realm */
			firePropertyChange(PROPERTY_MAGICREALM_CHANGED, null, magicrealm);
			updateMagicProgessionAndRealm();
			firePropertyChange(PROPERTY_SKILLCATEGORY_CHANGED, null, null);
			firePropertyChange(PROPERTY_SKILL_CATEGORIES, null, null);
		} else {
			throw new UnsupportedOperationException("magic realm is not editable.");
		}
	}
	
	
	/**
	 * 
	 * @return the characteristics, not {@code null}
	 */
	public Characteristics getCharacteristics() {
		if (characteristics == null) {
			characteristics = new Characteristics();
		}
		return characteristics;
	}
	
	public void switchSkillCategoryCosts(SkillCategory group1, SkillCategory group2) {
		Integer val1 = costsSwitchable.get(group1.getId());
		Integer val2 = costsSwitchable.get(group2.getId());
		costsSwitchable.put(group1.getId(), val2);
		costsSwitchable.put(group2.getId(), val1);
		firePropertyChange(PROPERTY_SKILLCATEGORY_CHANGED, null, null);
		firePropertyChange(PROPERTY_SKILLS_CHANGED, null, null);
	}
	
	/**
	 * Returns the {@link Skillcost}s of the given {@link SkillCategory}. If costs are switchable
	 * the switched costs are returned.
	 * 
	 * @param category the category, not {@code null}
	 * @return the {@link Skillcost}
	 */
	public Skillcost getSkillcost(SkillCategory category) {
		SkillCategory cat = category;
		if (cat.getRankType().isCostSwitchable()) {
			Integer id = costsSwitchable.get(cat.getId());
			cat = data.getSkillCategory(id);
		}
		Skillcost returnValue = data.getSkillcost(getProfession(), cat);
		// talent flaw
		if (talentsFlaws != null) {
			for (TalentFlaw tf : talentsFlaws) {
				if (tf.getSkillCategoryCostReplacement(cat) != null) {
					returnValue = tf.getSkillCategoryCostReplacement(cat);
				}
			}
		}
		
		return returnValue;
	}
	
	/**
	 * Returns the {@link Skillcost} of the given {@link ISkill}. If the costs are switchable
	 * the switched costs are returned. It returns the the rank depended costs for spell lists.
	 * 
	 * @param skill the given skill
	 * @return the {@link Skillcost}s
	 */
	public Skillcost getSkillcost(ISkill skill) {
		SkillCategory cat = getSkillcategory(skill);
		if (cat.getRankType().isCostSwitchable()) {
			Integer id = costsSwitchable.get(cat.getId());
			cat = data.getSkillCategory(id);
		}
		/* spell lists have costs that depends on rank */
		BigDecimal rank = getSkillRank(skill).getRank();
		Skillcost returnValue = data.getSkillcost(getProfession(), cat, skill, rank);
		
		// talent flaw
		if (talentsFlaws != null) {
			for (TalentFlaw tf : talentsFlaws) {
				if (tf.getSkillCostReplacement(skill) != null) {
					returnValue = tf.getSkillCostReplacement(skill);
				}
			}
		}
		return returnValue;
	}
	
	/**
	 * 
	 * @return the base width of movement in {@link LengthUnit#CM}
	 */ 
	public int getBaseMovement() {
		if (characteristics == null) {
			return 0;
		}
		/*US   1' == 30,48cm    1" == 2,54cm
		 * 50' + 3xQu + stride  == 15,24m + 3xQu + stride
		 * Stride
		 * 7'10" - 8'3" +20'  =>  238,78cm - 251,46cm   +6,096m
		 * 
		 * 5'10" - 6'3" +0'   =>   177,80cm - 190,5cm   +0cm
		 * 3'10" - 4'3" -20'  =>   116,84cm - 129,54cm  -6,096m
		 * 2'10" - 3'3" -30'  =>    86,36cm -  99,06cm  -9,144m
		 * 2'4"  - 2'9" -35'  =>    71,12cm -  83,82cm  -10,669m
		 * 1'10" - 2'3" -40'  =>    55,88cm -  68,58cm  -12,192m
		 * */
		int width = 15 + getStatBonusTotal(StatEnum.QUICKNESS);
		/* we use the german rules (very similar to US rules)! */
		int heightCm = Math.round( getLengthUnit().asBaseUnit(characteristics.getHeight()) );
		if (heightCm <= 70) {
			width += -14;
		} else if (heightCm <= 85) {
			width += -12;
		} else if (heightCm <= 101) {
			width += -10;
		} else if (heightCm <= 116) {
			width += -8;
		} else if (heightCm <= 131) {
			width += -6;
		} else if (heightCm <= 139) {
			width += -5;
		} else if (heightCm <= 149) {
			width += -4;
		} else if (heightCm <= 162) {
			width += -3;
		} else if (heightCm <= 169) {
			width += -2;
		} else if (heightCm <= 177) {
			width += -1;
		} else if (heightCm <= 192) {
			
		} else if (heightCm <= 198) {
			width += 1;
		} else if (heightCm <= 207) {
			width += 2;
		} else if (heightCm <= 222) {
			width += 3;
		} else if (heightCm <= 229) {
			width += 4;
		} else if (heightCm <= 237) {
			width += 5;
		} else {
			width += 6;
		}
		
		
		width = width * 100;
		
		// talent flaw modifier
		float factor = 1;
		if (talentsFlaws != null) {
			for (TalentFlaw tf : talentsFlaws) {
				if (tf.getBaseMovement() != null) {
					width += Math.round(tf.getBaseMovement().floatValue());
				}
				if (tf.getBasemoverateMultiplier() != null) {
					factor *= tf.getBasemoverateMultiplier().floatValue();
				}
			}
		}
		return Math.round(width * factor);
	}
	
	public int getReactionBonus() {
		return 3 * getStatBonusTotal(StatEnum.QUICKNESS);
	}
	
	/**
	 * Returns the {@link Shield} of the character. If no shield is set than 
	 * ir returns the first shield of {@link MetaData}. 
	 * 
	 * @return the {@link Shield} or {@code null}
	 */
	public Shield getShield() {
		if (data == null) {
			return null;
		}
		if (shieldId != null) {
			Shield shield = data.getShield(shieldId.intValue());
			if (shield != null) {
				return shield;
			}
		}
		return data.getShields().get(0);
	}
	
	/**
	 * 
	 * 
	 * @param shield the selected {@link Shield} or {@code null} to resets
	 */
	public void setShield(Shield shield) {
		Integer oldShieldId = shieldId;
		if (shield == null) {
			shieldId = Integer.valueOf( data.getShields().get(0).getId() );
		} else {
			shieldId = Integer.valueOf( shield.getId() );
		}
		if (oldShieldId == null || ! shieldId.equals(shieldId)) {
			Shield oldSh = null;
			if (oldShieldId != null) {
				oldSh = data.getShield( oldShieldId.intValue() );
			}
			Shield newSh = data.getShield( shieldId.intValue() );
			firePropertyChange(PROPERTY_SHIELD, oldSh, newSh);
		}
	}
	
	public void setArmor(int armor) {
		Integer oldValue = Integer.valueOf(getArmor());
		this.armor = armor;
		firePropertyChange(PROPERTY_ARMOR, oldValue, Integer.valueOf(getArmor()));
	}
	
	public int getArmor() {
		return armor;
	}
	
	public int getArmorManeuverModi() {
		if (data != null && data.getArmorSkill(getArmor()) != null && data.getArmorManeuverModiMin(getArmor()) != 0) {
			ISkill skill = data.getArmorSkill(getArmor());
			int modi = data.getArmorManeuverModiMax(getArmor()) + getSkillTotalBonus(skill);
			if (modi > data.getArmorManeuverModiMin(getArmor())) {
				return data.getArmorManeuverModiMin(getArmor());
			} else {
				return modi;
			}
		}
		return 0;
	}
	
	public int getArmorRangeModi() {
		if (data == null) {
			return 0;
		}
		return data.getArmorRangeCombatModi(getArmor());
	}
	
	public int getArmorReactionModi() {
		if (data == null) {
			return 0;
		}
		return data.getArmorReactionModi(getArmor());
	}
	
	/**
	 * 
	 * @return the apprentice ship; not {@code null}
	 */
	public String getApprenticeShip() {
		return StringUtils.trimToEmpty(apprenticeShip);
	}

	public void setApprenticeShip(String apprenticeShip) {
		String oldValue = this.apprenticeShip;
		this.apprenticeShip = apprenticeShip;
		firePropertyChange(PROPERTY_APPRENTICESHIP, oldValue, this.apprenticeShip);
	}
	
	
	public State getState() {
		return state;
	}
	
	private void setState(State state) {
		State oldValue = this.state;
		this.state = state;
		firePropertyChange(PROPERTY_STATE, oldValue, this.state);
	}
	
	public List<ToDo> getToDos() {
		return Collections.unmodifiableList(todos);
	}
	
	public void addToDo(ToDo todo) {
		todos.add(todo);
		firePropertyChange(PROPERTY_TODO_CHANGED, null, null);
	}
	
	public void finishTodo(ToDo todo) {
		boolean exists = todos.contains(todo);
		if (exists) {
			boolean success = false;
			if (ToDoType.RACE_PROF.equals(todo.getType())) {
				if (professionId != null && raceId != null && cultureId != null) {
					setState(State.NORMAL);
					success = true;
					addToDo(new ToDo(RESOURCE.getString("todo.backgroundoptions")+" "+getRace().getBackgroundOptions(), ToDoType.SYSTEM));
					for (String todoString : getCulture().getTodos()) {
						addToDo(new ToDo(todoString, ToDoType.SYSTEM));
					}
					/* reset the realm */
					this.magicrealm = new HashSet<StatEnum>();
					updateMagicProgessionAndRealm();
					updateYouthRanks();		
					updateProgressionBody();		
					firePropertyChange(PROPERTY_SHEET_ENABLE_ALL, null, Boolean.TRUE);
					firePropertyChange(PROPERTY_SHEET_ENABLE_RACE, null, Boolean.FALSE);
					firePropertyChange(PROPERTY_SHEET_ENABLE_PROF, null, Boolean.FALSE);
					firePropertyChange(PROPERTY_SHEET_ENABLE_CULTURE, null, Boolean.FALSE);
					firePropertyChange(PROPERTY_SKILL_CATEGORIES, null, null);
					firePropertyChange(PROPERTY_SKILLCATEGORY_CHANGED, null, null);
					firePropertyChange(PROPERTY_SKILLS_CHANGED, null, null);
				}
			} else {
				success = true;
			}
			if (success) {
				todos.remove(todo);
				firePropertyChange(PROPERTY_TODO_CHANGED, null, null);
			}
		}
	}

	/* package private for unittest */
	void updateMagicProgessionAndRealm() {
		Progression progr = null;
		Set<StatEnum> mb = new HashSet<StatEnum>();
		/* we resolve the attributes */
		if (getProfession() != null && getRace() != null) {
			for (StatEnum stat : getProfession().getStats()) {
				if (stat.isForMagic()) {
					Progression p = getRace().getProgMagic(stat);
					/* search the lowest progression */
					if (progr == null || p.compareTo(progr) < 0) {
						progr = p;
					}
					mb.add(stat);
				}
			}
		}
		/* the user may choose the magical realm (magicrealm) (e.g. fighter) */
		setMagicRealmEditable(progr == null);
		if (mb.size() > 0) {
			/* we have to set after setting the realm-editability*/
			setMagicRealm(mb);
		}
		if (progr == null && getProfession() != null && getRace() != null) {
			/* here we have a non-magical profession, we get the current realm (magicrealm) */
			mb = getMagicRealm();
			/* mb should contain one attribute */
			if (mb.size() != 1) {
				setMagicRealm(StatEnum.INTUITION);
				mb = getMagicRealm();
			}
			if (getRace() != null && mb.size() > 0) {
				for (StatEnum stat : mb) {					
					Progression p = getRace().getProgMagic(stat);
					/* search the lowest progression */
					if (progr == null || p.compareTo(progr) < 0) {
						progr = p;
					}
				}
			}
		}
		setProgressionPower(progr);
	}
	
	private void setMagicRealm(Set<StatEnum> attributes) {
		Set<StatEnum> oldValue = this.magicrealm;
		this.magicrealm = attributes;
		if (oldValue == null || this.magicrealm == null || (! oldValue.equals(this.magicrealm)) ) {
			firePropertyChange(PROPERTY_MAGICREALM_CHANGED, oldValue, this.magicrealm);
		}
		/* we don't call updateMagicProgressionAndRealm because this method is called from there*/
	}
	
	private void updateTempSum(boolean firePropertyChange) {
		int oldValue = getTempSum();
		int newSum = getStatTempSum();
		if (firePropertyChange && oldValue != newSum) {
			this.tempAttrSum = newSum;
			firePropertyChange(PROPERTY_STAT_COST_SUM, Integer.valueOf(oldValue), Integer.valueOf(newSum));
		}
	}

	/**
	 * 
	 * @return the sum of the stats
	 */
	public int getStatTempSum() {
		int newSum = 0;
		for (StatEnum at : StatEnum.values()) {
			int value = getStatTemp(at);
			if (value > 100) {
				newSum += (90 + Math.pow(value - 90, 2));
			} else if (value > 89) {
				newSum += (90 + Math.pow(value - 90, 2));
			} else if (value > 69) {
				newSum += value;
			} else if (value > 30) {
				newSum += value;
			} else if (value > 10) {
				newSum += value;
			} else {
				newSum += value;
			}
		}
		return newSum;
	}

	private void updateProgressionBody() {
		if (getRace() != null) {
			setProgressionBody(getRace().getProgKoerperentw());
		}
	}
	
	/* removes all youth ranks and sets youth ranks for the current race */
	private void updateYouthRanks() {
		if (log.isDebugEnabled()) log.debug("update youth ranks");
		/* remove all youth ranks */		
		skillgroupRanks.clear();		
		skillRanks.clear();
		/* add new youth ranks (skill category)*/
		for (SkillCategory category : getCulture().getYouthSkillgroups()) {
			Rank rank = getSkillcategoryRank(category);
			rank.setRank(BigDecimal.valueOf(getCulture().getYouthRank(category)));
			skillgroupRanks.put(category.getId(), rank);
		}
		/* add new youth ranks (skills)*/
		for (ISkill skill : getCulture().getYouthSkills()) {
			int rankVal = getCulture().getYouthRank(skill);
			if (log.isDebugEnabled()) log.debug("youth skill "+skill.getName()+"="+rankVal);
			Rank rank = getSkillRank(skill);
			rank.setRank(BigDecimal.valueOf(rankVal));
			skillRanks.put(skill.getId(), rank);
		}
		firePropertyChange(PROPERTY_SKILL_STRUCTURE_CHANGED, null, null);
		if (log.isDebugEnabled()) log.debug("update youth ranks finished");
	}



	/**
	 * Returns the defensive bonus
	 * 
	 * @return the DB
	 */
	public int getDefensiveBonus() {
		int db = getReactionBonus() + getArmorReactionModi() + getDefensiveBonusSpecial();
		if (db < 0) {
			return 0;
		} else {
			return db;
		}
	}
	
	/**
	 * Returns the defensive bonus from the talents and flaws.
	 * 
	 * @return db
	 */
	public int getDefensiveBonusSpecial() {
		int dbSpecial = 0;
		if (talentsFlaws != null) {
			for (TalentFlaw tf : talentsFlaws) {
				if (tf.getDb() != null) {
					dbSpecial += tf.getDb().intValue();
				}
			}
		}
		return dbSpecial;
	}

	/**
	 * Returns the special shield bonus from talent and flaws.
	 * 
	 * @return shield db bonus
	 */
	public int getShieldDbBonusSpecial() {
		int dbSpecial = 0;
		if (talentsFlaws != null) {
			for (TalentFlaw tf : talentsFlaws) {
				if (tf.getShieldDb() != null) {
					dbSpecial += tf.getShieldDb().intValue();
				}
			}
		}
		return dbSpecial;
	}
	
	/**
	 * 
	 * @param skill the skill to inherit from
	 * @param newName the new name
	 * @param type the new type or {@code null} to inherit
	 * @return the new registered {@link ISkill}
	 */
	public ISkill registerCustomSkill(ISkill skill, String newName, SkillType type) {
		Integer id = Integer.valueOf(nextCustomSkillId--);
		CustomSkill customSkill = new CustomSkill(skill, id, newName, type);
		if (log.isDebugEnabled()) log.debug("created custom skill "+newName+" with id "+id+", actual skill id "+skill.getId());
		customSkills.put(id, customSkill);
		skillRanks.put(id, getSkillRank(customSkill));
		return customSkill;
	}
	
	/**
	 * Returns the custom {@link ISkill} or the {@link ISkill} from {@link MetaData}.
	 * @param id the skill id
	 * @return the custom {@link ISkill} or {@link ISkill} from {@link MetaData}.
	 */
	public ISkill getSkill(Integer id) {
		if ( customSkills.containsKey(id) ) {
			return customSkills.get(id);
		} else {
			return data.getSkill(id);
		} 
	}
	/**
	 * Returns all skills from meta data and all custom skills. All excluded skills
	 * won't be returned. 
	 * 
	 * @return a list a all skills sorted by name
	 */
	public List<ISkill> getSkills() {
		List<ISkill> skills = new ArrayList<ISkill>();
		for (ISkill skill : data.getSkills()) {
			if ( ! RMPreferences.getInstance().isExcluded(skill.getSource())) {
				skills.add(skill);
			}
		}
		skills.addAll(customSkills.values());
		/* sort */
		Collections.sort(skills, new Comparator<ISkill>() {
			@Override
			public int compare(ISkill o1, ISkill o2) {
				return o1.getName().compareTo(o2.getName());
			}});
		return skills;
	}

	/**
	 * Adds the data from training pack to current sheet,
	 * skills, {@link SkillType}s, skill groups and todos.
	 * 
	 * @param trainPack the pack to add
	 */
	public void addTrainPack(TrainPack trainPack) {
		try {
			/* validate*/
			levelUp.addTrainPack(trainPack);
			/* process */
			String oldAppr = apprenticeShip;
			/* TODO check if training pack is allowed before */
			if (StringUtils.trimToNull(apprenticeShip) == null) {
				apprenticeShip = trainPack.getName();
			} else {
				apprenticeShip = apprenticeShip + ", " + trainPack.getName();
			}
			firePropertyChange(PROPERTY_APPRENTICESHIP, oldAppr, apprenticeShip);
			for (String todo : trainPack.getTodos()) {
				addToDo(new ToDo(todo, ToDoType.TRAINPACK));
			}
			for (Integer id : trainPack.getSkills().keySet()) {
				Integer rank = trainPack.getSkills().get(id);
				ISkill skill = getSkill(id);
				Rank skillRank = getSkillRank(skill);
				BigDecimal newRank = BigDecimal.valueOf(rank.longValue()).add( skillRank.getRank() );
				/* no further level-up validation */
				internalSetSkillRank(skill, newRank);
			}
			for (Integer id : trainPack.getSkillsGroups().keySet()) {
				Integer rank = trainPack.getSkillsGroups().get(id);
				SkillCategory skillgroup = data.getSkillCategory(id);
				Rank skillRank = getSkillcategoryRank(skillgroup);
				BigDecimal newRank = BigDecimal.valueOf(rank.longValue()).add( skillRank.getRank() );
				internalSetSkillcategoryRank(skillgroup, newRank);
			}
			/* skill types */
			Map<Integer, SkillType> st = trainPack.getSkillTypes();
			for (Integer skillId : st.keySet()) {
				skillTypes.put(skillId, st.get(skillId));			
			}
			firePropertyChange(PROPERTY_SKILL_STRUCTURE_CHANGED, null, null);
		} catch (LevelUpVetoException veto) {
			if (log.isDebugEnabled()) log.debug(veto.getMessage());
		}
	}

	/**
	 * Returns an unmodifiable {@link List} of {@link TrainPack}s.
	 * 
	 * @return available {@link TrainPack}s
	 */
	public List<TrainPack> getAvailableDevPacks() {
		List<TrainPack> avail = new ArrayList<TrainPack>();
		avail.addAll(data.getTrainPacks());
		return Collections.unmodifiableList( avail );
	}
	
	public Integer getDevPackCosts(TrainPack devPack) {
		if (getProfession() != null) {
			return Integer.valueOf( data.getTrainPackCosts(devPack, getProfession()) );
		}
		return Integer.valueOf(0);
	}

	/**
	 * Disposes the sheet. Disposes the property change listeners.
	 */
	public void dispose() {
		for (PropertyChangeListener list : propertyChangeSupport.getPropertyChangeListeners()) {
			propertyChangeSupport.removePropertyChangeListener(list);
		}
	}

	/**
	 * Returns the miscellaneous bonus for given attribute that is defined by the user.
	 * @param stat the attribute
	 * @return the miscellaneous attribute bonus
	 */
	public int getStatMiscBonus(StatEnum stat) {
		if (miscAttrBonus == null) return 0;
		Integer bonus = miscAttrBonus.get(stat);
		if (bonus == null) {
			return 0;
		}
		return bonus.intValue();
	}
	
	/**
	 * Returns the miscellaneous bonus for given attribute calculates from items, talents and flaws.
	 * @param stat the attribute
	 * @return the miscellaneous attribute bonus
	 */
	public int getStatMisc2Bonus(StatEnum stat) {
		int bonus = 0;
		if (misc2StatBonus != null) {
			Integer bonusInt = misc2StatBonus.get(stat);
			if (bonusInt != null) {
				bonus += bonusInt.intValue();
			}
		}
		// divine status
		if (StatEnum.INTUITION.equals(stat)) {
			bonus += getDivineStatusObject().getIntuitionBonus();
		}
		// talent flaw
		if (talentsFlaws != null) {
			for (TalentFlaw tf : talentsFlaws) {
				if (tf.getStatBonus(stat) != null) {
					bonus += tf.getStatBonus(stat).intValue();
				}
			}
		}
		return bonus;
	}
	
	/**
	 * Sets a new miscellaneous bonus for the given attribute.
	 * 
	 * @param stat the attribute
	 * @param bonus the bonus for given attribute
	 * @param sendChangeEvent whether to send a propertyChangeEvent for the value itself or not
	 */
	public void setStatMiscBonus(StatEnum stat, int bonus, boolean sendChangeEvent) {
		Object oldValue = miscAttrBonus.get(stat);
		Integer newValue = Integer.valueOf(bonus);
		miscAttrBonus.put(stat, newValue);
		if (sendChangeEvent) {			
			firePropertyChange(PROPERTY_STAT_MISCBONUS_PREFIX+stat, oldValue, newValue);
		}
		firePropertyChange(PROPERTY_STAT_BONUS_PREFIX+stat.name(), null, Integer.valueOf(getStatBonusTotal(stat)));
	}

	/**
	 * Sets the user defined special bonus.
	 * 
	 * @param category the skill category, not {@code null}
	 * @param bonus the new bonus
	 */
	public void setSkillcategorySpecialBonus(SkillCategory category, int bonus) {
		Rank rankObj = getSkillcategoryRank(category);
		rankObj.setSpecialBonus(Integer.valueOf(bonus));
		firePropertyChange(PROPERTY_SKILLCATEGORY_CHANGED, null, rankObj);
		firePropertyChange(PROPERTY_SKILLS_CHANGED, null, null);
	}

	/**
	 * Returns the calculated bonus without user defined bonus,
	 * e.g. body development, talent and flaws.
	 * 
	 * @param category the skill category
	 * @return the sum of special bonus
	 */
	public int getSkillcategorySpecial1Bonus(SkillCategory category) {
		/* body development */
		int bonus = category.getRankType().isProgressionBody() ? 10 : 0;
		/* talent and flaws */
		for (TalentFlaw tf : getTalentsFlaws()) {
			if (tf.getSkillCatBonus() != null) {
				if (tf.getSkillCatBonus().containsKey(category.getId())) {
					bonus += tf.getSkillCatBonus().get(category.getId()).intValue();
				}
			}
		}
		return bonus;
	}

	/**
	 * Gets an unmodifiable list of {@link Equipment}s.
	 * 
	 * @return list of {@link Equipment}s
	 */
	public List<Equipment> getEquipments() {
		if (equipments == null) {
			equipments = new ArrayList<Equipment>();
		}
		return Collections.unmodifiableList(equipments);
	}
	
	public void setEquipments(List<Equipment> equipment) {
		Object oldValue = this.equipments;
		this.equipments = equipment;
		firePropertyChange(PROPERTY_EQUIPMENTS, oldValue, equipment);
	}
	
	public List<Herb> getHerbs() {
		if (herbs == null) {
			herbs = new ArrayList<Herb>();
		}
		return Collections.unmodifiableList(herbs);
	}

	public void setHerbs(List<Herb> herbs) {
		Object oldValue = this.herbs;
		this.herbs = herbs;
		firePropertyChange(PROPERTY_HERBS, oldValue, this.herbs);
	}

	/**
	 * The coin and jewelry save object.
	 * 
	 * @return the coins instance, not {@code null}
	 */
	public Coins getCoins() {
		if (coins == null) {
			coins = new Coins();
		}
		return coins;
	}
	
	/**
	 * Returns base encumbrance with one scale digit in current unit.
	 * 
	 * @return base encumbrance
	 */
	public float getEncumbranceBase() {
		return Math.round(getCharacteristics().getWeight() * 10) / 100;
	}
	
	public int getWeightPenalty() {
		/* sum the equipment */
		if (getCharacteristics().getWeight() == 0) {
			return 0;
		}
		float weightCustUnit = 0;
		for (Equipment eq : getEquipments()) {
			if (eq.isCarried()) {
				weightCustUnit += eq.getWeight();
			}
		}
		int fac = (int) Math.ceil(weightCustUnit / getEncumbranceBase());
		if (fac < 1) {
			fac = 1;
		}
		// Talent Flaw Modifier
		float tfModifier = 1;
		if (talentsFlaws != null) {
			for (TalentFlaw tf : talentsFlaws) {
				if (tf.getWeightPenalty() != null) {
					tfModifier *= tf.getWeightPenalty().floatValue(); 
				}
			}
		}
		return Math.round( tfModifier * (-8 * (fac - 1)) );
	}
	
	/**
	 * For editing a skill this returns the skills type.
	 * 
	 * @param skill
	 * @return the customs skills type
	 */
	public SkillType getSkillTypeCustom(INamed skill) {
		if (skill instanceof CustomSkill) {
			return ((CustomSkill)skill).getType();
		}
		return null;
	}
	
	/**
	 * Returns the {@link SkillType} for the given skill.
	 * If any talent or flaw provides a type for the given skill this is used. If different 
	 * talents and flaws provides a {@link SkillType} the restricted or the best is used.
	 * If the profession provides a {@link SkillType} this is used. Otherwise the {@link Race} is
	 * checked and after that the custom {@link SkillType}s from training packs.
	 * 
	 * @param skill the skill
	 * @return the skill type, not {@code null}
	 */
	public SkillType getSkillType(ISkill skill) {
		if (skill == null || getProfession() == null) {
			return SkillType.DEFAULT;
		}
		/* determine the skill type */
		SkillType retVal = null;
		int skillId = 0;
		if (skill instanceof CustomSkill) {
			retVal = ((CustomSkill)skill).getType();
			skillId = ((CustomSkill)skill).getActualSkillId().intValue();
		} else {
			skillId = skill.getId().intValue();
		}
		/* check talent and flaws (skills types) */
		if (retVal == null) {
			for (TalentFlaw tf : getTalentsFlaws()) {
				if (tf.getSkillType() != null && tf.getSkillType().containsKey(skill.getId())) {
					SkillType newType = tf.getSkillType().get(skill.getId());
					if (SkillType.RESTRICTED.equals(newType)) {
						retVal = SkillType.RESTRICTED; // this is always stronger
						break;
					} else if (retVal == null || newType.ordinal() > retVal.ordinal() ){
						retVal = newType;
					}
				}
			}
		}
		/* check talent and flaws (skill category types) */
		if (retVal == null) {
			SkillCategory skillCat = getSkillcategory(skill);
			for (TalentFlaw tf : getTalentsFlaws()) {
				if (tf.getSkillCatType() != null && tf.getSkillCatType().containsKey(skillCat.getId())) {
					SkillType newType = tf.getSkillCatType().get(skillCat.getId());
					if (SkillType.RESTRICTED.equals(newType)) {
						retVal = SkillType.RESTRICTED; // this is always stronger
						break;
					} else if (retVal == null || newType.ordinal() > retVal.ordinal() ){
						retVal = newType;
					}
				}
			}
		}
		if (retVal == null) {
			retVal = getProfession().getSkillType(skillId);
		}
		if (retVal == null) {
			retVal = getProfession().getSkillGroupType(skillId);
		}
		if (retVal == null) {
			retVal = getCulture().getSkillType(skillId);
		}
		if (retVal == null) {
			retVal = getCulture().getSkillgroupType(getSkillcategory(skill));
		}
		if (retVal == null && skillTypes.containsKey(Integer.valueOf(skillId))) {
			retVal = skillTypes.get(Integer.valueOf(skillId));
		}
		if (SkillType.RESTRICTED_IF_NOT_CHANNELING.equals(retVal)) {
			if (!getMagicRealm().contains(StatEnum.INTUITION)) {
				retVal = SkillType.RESTRICTED;
			}
		}
		if (retVal == null) {
			retVal = SkillType.DEFAULT;
		}
		return retVal;
	}
	
	/**
	 * Returns the {@link SkillType} for the given skill category.
	 * The {@link SkillType#RESTRICTED_IF_NOT_CHANNELING} will be translated
	 * to {@link SkillType#DEFAULT} or {@link SkillType#RESTRICTED} depending
	 * on the realm. 
	 * 
	 * @param skillCat the skill category
	 * @return the skill type, not {@code null}
	 */
	public SkillType getSkillcategoryType(SkillCategory skillCat) {
		if (skillCat == null || getProfession() == null) {
			return SkillType.DEFAULT;
		}
		SkillType retVal = null;
		for (TalentFlaw tf : getTalentsFlaws()) {
			if (tf.getSkillCatType() != null && tf.getSkillCatType().containsKey(skillCat.getId())) {
				SkillType newType = tf.getSkillCatType().get(skillCat.getId());
				if (SkillType.RESTRICTED.equals(newType)) {
					retVal = SkillType.RESTRICTED; // this is always stronger
					break;
				} else if (retVal == null || newType.ordinal() > retVal.ordinal() ){
					retVal = newType;
				}
			}
		}
		if (retVal == null) {
			retVal = getProfession().getSkillGroupType(skillCat.getId().intValue());
		}
		if (SkillType.RESTRICTED_IF_NOT_CHANNELING.equals(retVal)) {
			if (!getMagicRealm().contains(StatEnum.INTUITION)) {
				retVal = SkillType.RESTRICTED;
			}
		}
		if (retVal == null) {
			retVal = SkillType.DEFAULT;
		}
		return retVal;
	}

	/**
	 * Returns the level up instance to active or in-activate it.
	 * 
	 * @return the level up instance
	 */
	public RMLevelUp getLevelUp() {
		return levelUp;
	}

	/**
	 * 
	 * @return the list of {@link InfoPage}s
	 */
	public List<InfoPage> getInfoPages() {
		return infoPages;
	}

	/**	
	 * Sets the list of {@link InfoPage}s.
	 * 
	 * @param infoPages the list
	 */
	public void setInfoPages(List<InfoPage> infoPages) {
		List<InfoPage> oldValue = this.infoPages;
		this.infoPages = infoPages;
		firePropertyChange(PROPERTY_INFO_PAGES, oldValue, this.infoPages);
	}
	
	/**
	 * 
	 * @return the list of {@link MagicalItem}s
	 */
	public List<MagicalItem> getMagicalitems() {
		return magicalitems;
	}
	
	/**
	 * Sets the list of {@link MagicalItem}s.
	 * 
	 * @param magicalitems
	 */
	public void setMagicalitems(List<MagicalItem> magicalitems) {
		List<MagicalItem> oldValue = this.magicalitems;
		this.magicalitems = magicalitems;
		firePropertyChange(PROPERTY_MAGICALITEMS, oldValue, this.magicalitems);
	}
	
	/**
	 * Returns the current weight unit.
	 * 
	 * @return weightUnit, not {@code null}.
	 */
	public WeightUnit getWeightUnit() {
		if (weightUnit == null) {
			return WeightUnit.KILOGRAM;
		}
		return weightUnit;
	}

	/**
	 * Sets the new weight unit. If {@code null} the default will be used.
	 * 
	 * @param weightUnit, may be {@code null}
	 */
	public void setWeightUnit(WeightUnit weightUnit) {
		WeightUnit oldValue = getWeightUnit();
		this.weightUnit = weightUnit;
		if (this.weightUnit == null) {
			this.weightUnit = WeightUnit.KILOGRAM;
		}
		if (! oldValue.equals(this.weightUnit)) {
			firePropertyChange(PROPERTY_WEIGHT_UNIT, oldValue, this.weightUnit);
			int converted = Math.round(oldValue.convertTo(getCharacteristics().getWeight(), this.weightUnit));
			getCharacteristics().setWeight(converted);
		}
	}

	/**
	 * Returns the current length unit.
	 * 
	 * @return the length unit, not {@code null}
	 */
	public LengthUnit getLengthUnit() {
		if (lengthUnit == null) {
			return LengthUnit.CM;
		}
		return lengthUnit;
	}

	/**
	 * Sets the new length unit. If {@code null} the default unit will be used.
	 * 
	 * @param lengthUnit, may be {@code null}
	 */
	public void setLengthUnit(LengthUnit lengthUnit) {
		LengthUnit oldUnit = getLengthUnit();
		this.lengthUnit = lengthUnit;
		if (this.lengthUnit == null) {
			this.lengthUnit = LengthUnit.CM;
		}
		if (! this.lengthUnit.equals(oldUnit)) {
			RMPreferences.getInstance().setLengthUnit(this.lengthUnit);
			firePropertyChange(PROPERTY_LENGTH_UNIT, oldUnit, this.lengthUnit);
			/* recalculate weights in characteristics */
			int converted = oldUnit.convertTo(getCharacteristics().getHeight(), this.lengthUnit);
			getCharacteristics().setHeight(converted);
		}
	}

	/**
	 * Returns true if the given skill has a skill rank, already.
	 * 
	 * @param skill the skill
	 * @return whether the skill has a skill rank or not
	 */
	public boolean hasSkillRank(ISkill skill) {
		return skillRanks.containsKey(skill.getId());
	}
	
	public boolean getLvlUpActive() {
		if (levelUp == null) {
			return false;
		}
		return levelUp.getLvlUpActive();
	}
	
	public void setLvlUpActive(boolean active) {
		if (levelUp != null) {
			Object oldValue = Boolean.valueOf(levelUp.getLvlUpActive());
			levelUp.setLvlUpActive(active);
			firePropertyChange(PROPERTY_LEVELUP_MODE, oldValue, Boolean.valueOf(levelUp.getLvlUpActive()));
		}
	}
	
	public String getLvlUpDevPoints() {
		if (levelUp == null) {
			return "";
		}
		return levelUp.getLvlUpDevPoints();
	}
	
	public void setLvlUpDevPoints(String s) {
		if (log.isDebugEnabled()) log.debug("ignoring setLvlUpDevPoints="+s);
	}
	
	public String getLvlUpStatusText() {
		if (levelUp == null) {
			return "";
		}
		return levelUp.getLvlUpStatusText();
	}
	
	public void setLvlUpStatusText(String s) {
		if (levelUp != null) {
			levelUp.setLvlUpStatusText(s);
		}
	}
	
	public String getLvlUpSpellLists() {
		if (levelUp == null) {
			return "";
		}
		return levelUp.getLvlUpSpellLists();
	}
	
	public void setLvlUpSpellLists(String s) {
		if (log.isDebugEnabled()) log.debug("ignoring setLvlUpSpellLists="+s);
	}
	
	/**
	 * Convenience method to get the hit points.
	 * 
	 * @return hit points
	 */
	public int getHitPoints() {
		int hits = getProgressionBodyTotalBonus();
		// talent flaw tolerance
		float tolerance = 1f;
		if (getTalentsFlaws() != null) {
			for (TalentFlaw tf : getTalentsFlaws()) {
				if (tf.getTolerance() != null) {
					tolerance *= tf.getTolerance().floatValue();
				}
			}
		}
		hits = Math.round( tolerance * hits);
		return hits;
	}

	private int getProgressionBodyTotalBonus() {
		ISkill skillHits = null;
		for (ISkill skill : data.getSkills()) {
			if (getSkillcategory(skill).getRankType().isProgressionBody()) {
				skillHits = skill;				
			}
		}
		int hits = getSkillTotalBonus(skillHits);
		return hits;
	}
	
	/**
	 * Returns the unconscious points without the hit points.
	 * 
	 * @return unconscious points without the hit points
	 */
	public int getUnconsciousPoints() {
		int cp = getStatTemp(StatEnum.CONSTITUTION);
		int bodyBonus = getProgressionBodyTotalBonus();
		int hits = getHitPoints();
		return cp - hits + bodyBonus;
	}
	
	/**
	 * Convenience method to get the power points.
	 * 
	 * @return power points
	 */
	public int getPowerPoints() {
		ISkill skillPPs = null;
		for (ISkill skill : data.getSkills()) {
			if (getSkillcategory(skill).getRankType().isProgressionMagic()) {
				skillPPs = skill;
			}
		}
		if (skillPPs != null) {
			return getSkillTotalBonus(skillPPs);
		}
		return 0;
	}

	/**
	 * 
	 * @param skill the skill to modify
	 * @param skillName the new skill name
	 * @param skillType the new skill type or {@code null} for inherit from base skill
	 */
	public void modifySkill(ISkill skill, String skillName, SkillType skillType) {
		/* check changed type */
		SkillType oldSkillType = getSkillType(skill);
		BigDecimal newRank = null;
		if (skillType != null && ! skillType.equals(oldSkillType)) {
			/* re-calculate ranks */
			int levelUpSteps = levelUp.getLevelUpSteps(skill);
			BigDecimal ranksThisLevelNormal =  new BigDecimal(levelUpSteps).multiply(oldSkillType.getStep());
			BigDecimal rankWithOutLevelUp = getSkillRank(skill).getRank().subtract(ranksThisLevelNormal);
			newRank = new BigDecimal(levelUpSteps).multiply(skillType.getStep()).add(rankWithOutLevelUp);
		}
		
		if (skill instanceof CustomSkill) {
			((CustomSkill) skill).setName(skillName); 
			((CustomSkill) skill).setType(skillType);
			/* modify skill rank */
			if (skillRanks.containsKey(skill.getId())) {
				skillRanks.get(skill.getId()).setRank(newRank);
			}
				
		} else {
			/* remap skill rank */
			if (skillRanks.containsKey(skill.getId())) {
				Rank oldSkillRank = skillRanks.remove(skill.getId());				
				ISkill newSkill = registerCustomSkill(skill, skillName, skillType);
				oldSkillRank.setId(newSkill.getId());
				if (newRank != null) {
					oldSkillRank.setRank(newRank);
				}
				skillRanks.put(newSkill.getId(), oldSkillRank);
				levelUp.modifySkill(skill, newSkill);
			}
		}
		firePropertyChange(PROPERTY_SKILL_STRUCTURE_CHANGED, null, null);
	}
	
	public void fireSkillStructureChanged() {
		firePropertyChange(PROPERTY_SKILL_STRUCTURE_CHANGED, null, null);
	}

	/**
	 * Notifies the sheet about changes in magical items and talents and flaws.
	 */
	public void notifyItemBonusChanged() {
		Map<StatEnum, Integer> newStatMisc2Bonus = new HashMap<StatEnum, Integer>();
		Map<Integer, String> newSkillItemBonus = new HashMap<Integer, String>();
		
		List<MagicalItem> mitems = getMagicalitems();
		for (int i=0; i<mitems.size(); i++) {
			MagicalItem item = mitems.get(i);
			if (Boolean.TRUE.equals(item.getFavorite())) {
				for (MagicalFeature mf : item.getFeatures()) {
					if (mf.getBonus().intValue() != 0) {
						int bonus = mf.getBonus().intValue();
						if (mf.getStat() != null && MagicalItemFeatureType.STAT.equals(mf.getType())) {
							if (newStatMisc2Bonus.containsKey(mf.getStat())) {
								bonus += newStatMisc2Bonus.get(mf.getStat()).intValue();
							}
							newStatMisc2Bonus.put(mf.getStat(), Integer.valueOf(bonus));
						} else if (mf.getId() != null && MagicalItemFeatureType.SKILL.equals(mf.getType())) {
							String str;
							if (newSkillItemBonus.containsKey(mf.getId())) {
								str = newSkillItemBonus.get(mf.getId()) + "|";
							} else {
								str = "";
							}
							str += (i+1)+":+"+bonus;
							newSkillItemBonus.put(mf.getId(), str);
						}
					}
				}
			}
		}
		/* update stat bonus */
		misc2StatBonus = newStatMisc2Bonus;
		for (StatEnum stat : StatEnum.values()) {
			firePropertyChange(PROPERTY_STAT_MISC2BONUS_PREFIX+stat.name(), null, Integer.valueOf(getStatMisc2Bonus(stat)));
		}
		/* update skill  */
		itemSkillBonus = newSkillItemBonus;
		firePropertyChange(PROPERTY_SKILLS_CHANGED, null, null);
	}

	/**
	 * 
	 * 
	 * @param skill the skill
	 * @return the special bonus from magical items
	 */
	public String getSkillItemBonus(ISkill skill) {
		return itemSkillBonus.get(skill.getId());
	}
	
	public boolean getPrintOutlineImage() {
		if (printOutlineImage == null) {
			return RMPreferences.getInstance().printOutlineImage();
		}
		return printOutlineImage.booleanValue();
	}

	public void setPrintOutlineImage(boolean showOutlineImage) {
		Boolean oldValue = this.printOutlineImage;
		this.printOutlineImage = Boolean.valueOf(showOutlineImage);
		if (this.printOutlineImage == null) {
			this.printOutlineImage = Boolean.valueOf(getPrintOutlineImage());
		}
		firePropertyChange(PROPERTY_PRINT_OUTLINE_IMG, oldValue, this.printOutlineImage);
	}

	public void setMetaData(MetaData data) {
		this.data = data;
	}

	public CharImagePos getImagePos() {
		if (this.imagePos == null) {
			setImagePos(CharImagePos.PAGE1_AND_EQUIPMENT);
		}
		return imagePos;
	}

	public void setImagePos(CharImagePos imagePos) {
		CharImagePos oldValue = this.imagePos;
		this.imagePos = imagePos;
		if (this.imagePos == null) {
			this.imagePos = CharImagePos.PAGE1_AND_EQUIPMENT;
		}
		firePropertyChange(IMG_POS_PROP, oldValue, this.imagePos);
	}

	/**
	 * Returns the list of talent and flaws. If you want to modify the list, you have
	 * to set the list again in {{@link #setTalentsFlaws(List)} to notify about changes.
	 * 
	 * @return unmodifiable list of talent and flaws, not {@code null}
	 */
	public List<TalentFlaw> getTalentsFlaws() {
		if (talentsFlaws == null) {
			return Collections.unmodifiableList(new ArrayList<TalentFlaw>());
		}
		return Collections.unmodifiableList(talentsFlaws);
	}

	/**
	 * Sets the new list of talent and flaws.
	 * 
	 * @param talentsFlaws the new talent flaw list, not {@code null}
	 */
	public void setTalentsFlaws(List<TalentFlaw> talentsFlaws) {
		Object oldValue = this.talentsFlaws;
		this.talentsFlaws = new ArrayList<TalentFlaw>();
		if (talentsFlaws != null) {
			this.talentsFlaws.addAll(talentsFlaws);
		}
		firePropertyChange(TALENTSFLAWS_PROP, oldValue, this.talentsFlaws);
		firePropertyChange(PROPERTY_SKILLCATEGORY_CHANGED, null, null);
		firePropertyChange(PROPERTY_SKILL_CATEGORIES, null, null);
		firePropertyChange(PROPERTY_SKILLS_CHANGED, null, null);
		firePropertyChange(PROPERTY_PROGRESSION_BODY, null, getProgressionBody());
		firePropertyChange(PROPERTY_PROGRESSION_POWER, null, getProgressionPower());
		for (StatEnum stat : StatEnum.values()) {
			firePropertyChange(PROPERTY_STAT_MISC2BONUS_PREFIX+stat.name(), null, Integer.valueOf(getStatMisc2Bonus(stat)));
		}
		
	}

	/**
	 * Returns the initiative bonus including the modifications by talent and flaws.
	 * 
	 * @return the initiative bonus
	 */
	public int getInitiativeBonus() {
		int bonus = getStatBonusTotal(StatEnum.QUICKNESS);
		// check the talents/flaws
		for (TalentFlaw tf : getTalentsFlaws()) {
			if (tf.getInitiativeBonus() != null) {
				bonus += tf.getInitiativeBonus().intValue();
			}
		}
		return bonus;
	}

	public Long getFatepoints() {
		return fatepoints;
	}

	public void setFatepoints(Long fatepoints) {
		Object oldValue = this.fatepoints;
		this.fatepoints = fatepoints;
		firePropertyChange(FATEPOINTS_PROP, oldValue, this.fatepoints);
	}

	public Long getGracepoints() {
		if (gracepoints == null) {
			return Long.valueOf(0);
		}
		return gracepoints;
	}

	public void setGracepoints(Long gracePoints) {
		Object oldStatus = getDivinestatus();
		Object oldValue = this.gracepoints;
		this.gracepoints = gracePoints;
		firePropertyChange(GRACEPOINTS_PROP, oldValue, this.gracepoints);
		firePropertyChange(DIVINESTATUS_PROP, oldStatus, getDivinestatus());
		firePropertyChange(PROPERTY_STAT_MISC2BONUS_PREFIX+StatEnum.INTUITION.name(), null, Integer.valueOf(getStatMisc2Bonus(StatEnum.INTUITION)));
	}
	
	public String getDivinestatus() {
		long level = getDivineStatusObject().getLevel();
		return RESOURCE.getString("rolemaster.divinestatus."+level);
	}

	public DivineStatus getDivineStatusObject() {
		int grace = getGracepoints().intValue();
		return new DivineStatus(grace);
	}

	public void setTalentFlawOwnPage(Boolean talentFlawOwnPage) {
		Object oldValue = this.talentFlawOwnPage;
		this.talentFlawOwnPage = talentFlawOwnPage;
		firePropertyChange(TALENTFLAW_OWN_PAGE_PROP, oldValue, this.talentFlawOwnPage);
	}
	
	/**
	 * Returns {@link Boolean#TRUE} if the talent and flaws shall be printed on a new PDF page.
	 * 
	 * @return {@link Boolean#TRUE} for new PDF page, {@link Boolean#FALSE} or {@code null} for equipment page.
	 */
	public Boolean getTalentFlawOwnPage() {
		return talentFlawOwnPage;
	}
}
