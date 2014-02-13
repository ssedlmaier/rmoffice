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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sf.rmoffice.RMPreferences;
import net.sf.rmoffice.generator.Name.Style;
import net.sf.rmoffice.meta.TrainPack.Type;
import net.sf.rmoffice.meta.enums.LengthUnit;
import net.sf.rmoffice.meta.enums.RaceScope;
import net.sf.rmoffice.meta.enums.RankSubType;
import net.sf.rmoffice.meta.enums.RankType;
import net.sf.rmoffice.meta.enums.ResistanceEnum;
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.meta.enums.SpellUserType;
import net.sf.rmoffice.meta.enums.SpelllistPart;
import net.sf.rmoffice.meta.enums.SpelllistType;
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.meta.enums.TalentFlawLevel;
import net.sf.rmoffice.meta.enums.TalentFlawType;
import net.sf.rmoffice.meta.talentflaw.ITalentFlawPart;
import net.sf.rmoffice.meta.talentflaw.TalentFlawFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The meta data loader reads all configuration files and parses them.
 */
public class MetaDataLoader {
	private static final int MIN_USER_RACE_CULTURE_ID = 1000;
	private static final String CONF_SKILLCOSTS = "/conf/skillcosts.csv";
	private static final String CONF_PROF = "/conf/prof.conf";
	private static final String CONF_RACE = "/conf/race.conf";
	private static final String CONF_SKILLS = "/conf/skills.conf";
	private static final String CONF_CULTURES = "/conf/cultures.conf";
	private static final String CONF_SPELLLISTS = "/conf/spelllists.conf";
	private static final String CONF_SPELLCOSTS_BY_LEVEL = "/conf/spellcostsbylevel.conf";
	private static final String CONF_SHIELD = "/conf/shield.conf";
	private static final String CONF_ARMOR = "/conf/armor.conf";
	private static final String CONF_TRAINING_PACK = "/conf/trainingpacks.conf";
	private static final String CONF_TRAINING_PACK_COSTS = "/conf/trainingpackcosts.conf";
	private static final String CONF_TALENT_FLAW = "/conf/talentflaw.conf";
	
	private final static Logger log = LoggerFactory.getLogger(MetaDataLoader.class);
	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	private static final Charset CHARSET = Charset.forName("UTF-8");

	public static final String CATEGORY_CHAR = "C";
	public static final String SKILL_CHAR = "S";
	public static final String DESCR = "DESCR=";
	
	public MetaDataLoader() {
	}
	
	public MetaData load() throws IOException {
		MetaData metaData = new MetaData();
		metaData.setProfessions(loadProfessions());
		metaData.setRaces(loadRaces());
		metaData.setSkillgroups(loadSkillcosts(metaData));
		metaData.setSkills(loadSkills(metaData));
		loadSkillcostPerLevel(metaData);
		loadShields(metaData);
		loadCultures(metaData);
		loadArmorConf(metaData);
		loadTrainingPacks(metaData);
		loadTrainingPackCosts(metaData);
		loadTalentFlaw(metaData);
		return metaData;
	}
	
	private void loadTalentFlaw(MetaData metaData) throws IOException {
		BufferedReader reader = getReader(CONF_TALENT_FLAW);
		String line = null;
		int lineNo = 0;
		/* read lines */
		TalentFlawPreset talentFlaw = null;
		while ( (line = reader.readLine()) != null ) {
			lineNo++;
			if ( isValidLine(line)) {
				try {
					line = StringUtils.trimToEmpty(line);				
					String[] split = StringUtils.splitPreserveAllTokens(line, ",");
					if (split.length > 4) {
						Integer id = Integer.valueOf(StringUtils.trimToEmpty(split[0]));
						if (talentFlaw != null && !talentFlaw.getId().equals(id)) {
							/* new ID in line */
							metaData.addTalentFlaw(talentFlaw);
							talentFlaw = null;
						}
						if (talentFlaw == null) {
							talentFlaw = new TalentFlawPreset(id);
							/* we only have to read first 2 parameters for new talentFlaw */
							talentFlaw.setType(TalentFlawType.valueOf(StringUtils.trimToEmpty(split[1])));
							talentFlaw.setSource(StringUtils.trimToEmpty(split[2]));
							talentFlaw.setName(RESOURCE.getString(StringUtils.trimToEmpty("talentflaw."+id)));
						}
						// read the sub values
						TalentFlawPresetLevel presetLevel = new TalentFlawPresetLevel();
						presetLevel.setLevel(TalentFlawLevel.valueOf(StringUtils.trimToEmpty(split[3])));
						presetLevel.setCosts(Integer.parseInt(StringUtils.trimToEmpty(split[4])));
						TalentFlawFactory factory = new TalentFlawFactory(metaData); 
						for (int i=5; i<split.length; i++) {
							ITalentFlawPart tfPart = factory.parseTalentFlawPart(split[i]);
							presetLevel.addTalentFlawPart(tfPart);
						}
						talentFlaw.addValues(presetLevel);
					}
				} catch (Exception e) {
					log.error(CONF_TALENT_FLAW+": Could not decode lineNo "+lineNo, e);
				}
			}
		}
		if (talentFlaw != null) {
			metaData.addTalentFlaw(talentFlaw);
		}
	}

	

	private void loadSkillcostPerLevel(MetaData metaData) throws IOException {
		BufferedReader reader = getReader(CONF_SPELLCOSTS_BY_LEVEL);
		String line = null;
		int lineNo = 0;
		SpellUserType[] spellUserTypesHeader = null;
		
		/* read header */
		line = reader.readLine();
		String[] split = StringUtils.splitPreserveAllTokens(line, ",");
		spellUserTypesHeader = new SpellUserType[ Math.round((split.length - 2) / 3) ];
		for (int i=0; i<spellUserTypesHeader.length; i++) {
			spellUserTypesHeader[i] = SpellUserType.valueOf(StringUtils.trimToNull(split[2 + i * 3]));
		}
		/* read lines */
		while ( (line = reader.readLine()) != null ) {
			lineNo++;
			if ( isValidLine(line)) {
				try {
					line = StringUtils.trimToEmpty(line);				
					split = StringUtils.splitPreserveAllTokens(line, ",");
					if (split.length > 0) {
						int i=0;
						/* Skill category ID */
						Integer skillCatID = Integer.valueOf(StringUtils.trimToEmpty(split[i++]));
						/* Spell part */
						SpelllistPart part = SpelllistPart.valueOf(StringUtils.trimToEmpty(split[i++]));
						/* costs */
						for (int j=0; j<spellUserTypesHeader.length; j++) {
							/* read 3 costs */
							List<Integer> costs = new ArrayList<Integer>();
							String val = "0";
							for (int k=0; k<3 && val != null; k++) {
								val = StringUtils.trimToNull(split[i+j*3+k]);
								if (val != null) {
									int c = Integer.parseInt(val);
									if (c > 0) {
										costs.add(Integer.valueOf(c));
									}
								}
							}
							int[] costsR = new int[costs.size()];
							for (int k=0; k<costs.size(); k++) {
								costsR[k] = costs.get(k).intValue();
							}
							Skillcost sc = new Skillcost(costsR);
							metaData.addSpellcostByLevel(skillCatID, part, spellUserTypesHeader[j], sc);
						}
					}
				}  catch (Exception e) {
					log.error(CONF_SPELLCOSTS_BY_LEVEL+": Could not decode lineNo "+lineNo, e);
				}
			}
		}
	}

	private void loadTrainingPackCosts(MetaData metaData) throws IOException {
		BufferedReader reader = getReader(CONF_TRAINING_PACK_COSTS);
		String line = null;
		int lineNo = 0;
		
		List<Integer> devPackIds = new ArrayList<Integer>();  
			
		List<List<Profession>> headerLine = null;
		while ( (line = reader.readLine()) != null ) {
			lineNo++;
			if ( isValidLine(line)) {
				try {
					line = StringUtils.trimToEmpty(line);				
					String[] split = StringUtils.splitPreserveAllTokens(line, ",");
					if (split.length > 0) {
						if ("ID".equals(split[0])) {
							/* found the header line */
							headerLine = new ArrayList<List<Profession>>();
							headerLine.add(null);
							for (int i = 1; i < split.length; i++) {
								headerLine.add(new ArrayList<Profession>());
								try {
									for (String profIdStr : StringUtils.split(StringUtils.trimToEmpty(split[i]), "-")) {
										Integer profId = Integer.valueOf( profIdStr );
										Profession profession = metaData.getProfession(profId);
										if (headerLine.get(i).contains(profession)) {
											headerLine.get(i).add(null);
											if (log.isWarnEnabled()) log.warn(CONF_TRAINING_PACK_COSTS+": Duplicate profession in header, profession Id = "+profId+" token "+i);
										} else {
											headerLine.get(i).add(profession);
										}
									}
								} catch (NumberFormatException e) {
									if (log.isWarnEnabled()) log.warn(CONF_TRAINING_PACK_COSTS+": Could not decode header lineNo "+lineNo+" at token "+i+": "+e.getMessage());
									/* we ignore this column in the next step */
									headerLine.get(i).add(null);
								}
							}
						} else if (headerLine != null && headerLine.size() == split.length && StringUtils.trimToNull(split[0]) != null) {
							Integer devPackId = Integer.valueOf(StringUtils.trimToEmpty(split[0]));
							if (! devPackIds.contains(devPackId)) {							
								TrainPack devPack = metaData.getDevPack(devPackId);
								if (devPack != null) {
									for (int i = 1; i < headerLine.size(); i++) {
										List<Profession> profs = headerLine.get(i);
										if (profs != null) {
											for (Profession prof : profs) {
												if (StringUtils.trimToNull(split[i]) != null) {
													int costs = Integer.parseInt(StringUtils.trimToEmpty(split[i]));
													metaData.addTrainPackCosts(devPack, prof, costs);
												}
											}
										}
									}
								} else {
									if (log.isWarnEnabled()) log.warn(CONF_TRAINING_PACK_COSTS+": ignoring lineNo "+lineNo+": No TrainPack available with id "+devPackId);
								}
							} else {
								if (log.isWarnEnabled()) log.warn(CONF_TRAINING_PACK_COSTS+": ignoring lineNo "+lineNo+": Duplicate TrainPack Id "+devPackId);
							}
						} else {
							if (log.isWarnEnabled()) log.warn(CONF_TRAINING_PACK_COSTS+": Ignoring lineNo "+lineNo+" because no header line available or the line has different occurrences");
						}
					}
				} catch (Exception e) {
					log.error(CONF_TRAINING_PACK_COSTS+": Could not decode lineNo "+lineNo, e);
				}
			}
		}
	}
						
	
	private void loadTrainingPacks(MetaData metaData) throws IOException {
		BufferedReader reader = getReader(CONF_TRAINING_PACK);
		String line = null;
		int lineNo = 0;
		
		List<Integer> ids = new ArrayList<Integer>();
		while ( (line = reader.readLine()) != null ) {
			lineNo++;
			if ( isValidLine(line)) {
				try {
					line = StringUtils.trimToEmpty(line);				
					String[] split = StringUtils.splitPreserveAllTokens(line, ",");
					if (split != null && split.length > 6) {
						Integer tpId = Integer.valueOf(StringUtils.trimToEmpty(split[0]));
						String name = StringUtils.trimToEmpty(split[1]);
						try {
							name = RESOURCE.getString("trainingpack."+tpId);
						} catch (MissingResourceException e) {
							if (log.isDebugEnabled()) log.debug(CONF_TRAINING_PACK+": ignoring missing resource for training pack id "+tpId);							
						}
						Type tpType = null;
						if (! StringUtils.isEmpty(split[2])) {
							tpType = TrainPack.Type.valueOf(split[2]);
						}
						String startMoney = StringUtils.trimToEmpty(split[5]);
						if (startMoney.length() > 0) {
							try {
								startMoney = RESOURCE.getString("trainpack.startmoney."+startMoney);
							} catch (MissingResourceException e) {
								log.warn(CONF_TRAINING_PACK+": ignoring resource key 'trainpack.startmoney."+startMoney+"' in line "+lineNo);
							}
						}
						TrainPack tp = new TrainPack(
								tpId, 
								name,
								Integer.parseInt(split[3]),
								StringUtils.trimToEmpty(split[4]),
								startMoney,
								tpType
								);
						
						for (int i=6; i< split.length; i++) {
							String s = StringUtils.trimToEmpty(split[i]);							
							if (s.startsWith("T") && s.length() > 2) {
								/* example Tlocale.key=5 */
								String[] todoParts = StringUtils.split(s.substring(1), "=");
								String todo = todoParts[0];
								/* try to localize */
								try {
									todo = RESOURCE.getString(todo);
								} catch (MissingResourceException e) {
									if (log.isDebugEnabled()) log.debug(CONF_TRAINING_PACK + ": ignoring resource key '"+e.getMessage()+"' in line "+lineNo);
								}
								int idx = todo.indexOf("{G");
								if (idx > -1) {
									int idx2 = todo.indexOf("}", idx);
									String idString = todo.substring(idx+2, idx2);
									Integer id = Integer.valueOf(idString);
									todo = todo.substring(0, idx) + metaData.getSkillCategory(id).getName() + todo.substring(idx2+1);
								}
								if (todoParts.length > 1) {
									todo = MessageFormat.format(todo, todoParts[1]);
								}
								tp.addTodo(todo);
							} else if (s.startsWith("G")) {
								/* skill group */
								String[] g = StringUtils.splitPreserveAllTokens(s.substring(1), "=");
								int groupId = Integer.parseInt(g[0]);
								int ranks = -1;
								try {
									ranks = Integer.parseInt(g[1]);
									tp.addSkillGroup(groupId, ranks);
								} catch (NumberFormatException e) {
									/* could not parse the ranks, try with SkillType */
									SkillType type = SkillType.valueOf(StringUtils.trim(g[1]));
									tp.addSkillgroupType(groupId, type);
								}
								/* check on next elements for PREV (only if prev was a number and not a SkillType) */
								if (split.length > i+1 && StringUtils.trimToEmpty(split[i+1]).startsWith("PREV") && ranks > -1) {
									/* PREV means a skill from the previous chosen category or skill */
									String[] split2 = StringUtils.split(split[i++], "=");
									String gr = RESOURCE.getString("skillgroup."+groupId);
									tp.addTodo(MessageFormat.format(RESOURCE.getString("training.chooseskillfromgroup"), split2[1], gr ));
								}
							} else if (s.startsWith("STAT")) {
								String[] statGain = StringUtils.split(s.substring(5), "|");
								if (statGain.length > 0) {
									if ("CHOICE".equals(statGain[0])) {
										tp.addStatGain(Arrays.asList(StatEnum.values()));
									} else {
										ArrayList<StatEnum> statGains = new ArrayList<StatEnum>();
										for (String stat : statGain) {
											statGains.add( StatEnum.valueOf(StringUtils.trim(stat)) );
										}
										if (statGains.size() > 0) {
											tp.addStatGain(statGains);
										}
									}
								}
							} else if (s.startsWith("SKILL")) {
								/* skill of group */
								String[] g = StringUtils.splitPreserveAllTokens(s.substring(6), "=");
								int gid = NumberUtils.toInt(g[0]);
								String group = RESOURCE.getString("skillgroup."+gid);
								tp.addTodo(MessageFormat.format(RESOURCE.getString("training.chooseskillfromgroup"), g[1], group));
							} else if (s.startsWith(CATEGORY_CHAR)) {
								/* choice, ex: CG12|G13=3
								 * currently we create a Todo */
								String[] g = StringUtils.splitPreserveAllTokens(s.substring(1), "=");
								String[] choices = StringUtils.split(g[0], "|");
								StringBuilder sb = new StringBuilder();								
								for (String choice : choices) {
									if (choice.startsWith("G")) {
										int gid = NumberUtils.toInt(choice.substring(1));
										sb.append(RESOURCE.getString("skillgroup."+gid)).append(",");
									} else if (choice.startsWith(SKILL_CHAR)) {
										int sid = NumberUtils.toInt(choice.substring(1));
										sb.append(RESOURCE.getString("skill."+sid)).append(",");
									} else {
										if (log.isWarnEnabled()) log.warn(CONF_TRAINING_PACK + ": choice is not valid: "+choice+" in line "+lineNo);
									}
								}
								/* check on next elements for PREV */
								if (split.length > i+1 && StringUtils.trimToEmpty(split[i+1]).startsWith("PREV")) {
									/* PREV means a skill from the previous chosen category or skill */
									i++;
									String[] split2 = StringUtils.split(split[i], "=");
									Integer numberSkills = Integer.valueOf(NumberUtils.toInt(split2[0].substring(4), 1));
									tp.addTodo(MessageFormat.format(RESOURCE.getString("training.choosefrom.withskill"), g[1], split2[1], sb.toString(), numberSkills ));
								} else {
									SkillType type = null;
									try {
										type = SkillType.valueOf(StringUtils.trim(g[1]));
									} catch (Exception e) {
									}
									if (type == null) {
										tp.addTodo(MessageFormat.format(RESOURCE.getString("training.choosefrom"), g[1], sb.toString()));
									} else {
										tp.addTodo(MessageFormat.format(RESOURCE.getString("training.chooseCat.skilltype"), g[1], sb.toString()));
									}
								}
							} else if (s.startsWith("SPELLLIST")) {
								/* make a todo */
								tp.addTodo(s);
							} else if (s.startsWith(SKILL_CHAR)) {
								/* sKill */
								String[] g = StringUtils.splitPreserveAllTokens(s.substring(1), "=");
								int skillId = Integer.parseInt(g[0]);
								try {
									tp.addSkill(skillId, Integer.parseInt(g[1]));
								} catch (NumberFormatException e) {
									SkillType type = SkillType.valueOf(StringUtils.trim(g[1]));
									tp.addSkillType(skillId, type);
								}
							} else if (s.startsWith(DESCR)) {
								/* description */
								/* try to localize */
								String todo = s.substring(6);
								try {
									todo = RESOURCE.getString(todo);
								} catch (MissingResourceException e) {
									if (log.isDebugEnabled()) log.debug(CONF_TRAINING_PACK + ": ignoring resource key '"+todo+"' in line "+lineNo);
								}
								tp.addTodo( MessageFormat.format(RESOURCE.getString("training.descr"), todo) );
							} else if (StringUtils.trimToNull(s) != null){
								if (log.isWarnEnabled()) log.warn(CONF_TRAINING_PACK+": invalid token '"+s+"' on line "+lineNo);
							}
						}
						if ( ! ids.contains(tp.getId()) ) {
							metaData.addTrainPack(tp);
						} else {
							if (log.isWarnEnabled()) log.warn(CONF_TRAINING_PACK+": Duplicate ID in line "+lineNo);
						}
					} else {
						if (log.isWarnEnabled()) log.warn(CONF_TRAINING_PACK+": Could not decode line "+lineNo);
					}
				} catch (Exception e) {
					log.error(CONF_TRAINING_PACK+": Could not decode lineNo "+lineNo, e);
				}
			}
		}
	}

	private void loadArmorConf(MetaData metaData) throws IOException {
		BufferedReader reader = getReader(CONF_ARMOR);
		String line = null;
		int lineNo = 0;
		
		while ( (line = reader.readLine()) != null ) {
			lineNo++;
			if ( isValidLine(line)) {
				try {
					line = StringUtils.trimToEmpty(line);				
					String[] split = StringUtils.splitPreserveAllTokens(line, "=");
					int armor = Integer.parseInt(split[0]);
					Integer skillId = Integer.valueOf(split[1]);
					ISkill skill = metaData.getSkill(skillId);
					if (skill != null) {
						metaData.addArmourSkill(armor, skill);
					} else {
						if (log.isWarnEnabled()) log.warn("No skill found for armor "+armor);
					}
				} catch (Exception e) {
					log.error("Could not decode armor lineNo "+lineNo, e);
				}
			}
		}
	}

	private void loadShields(MetaData metaData) throws IOException {		
		BufferedReader reader = getReader(CONF_SHIELD);
		String line = null;
		int lineNo = 0;
		
		while ( (line = reader.readLine()) != null ) {
			lineNo++;
			if ( isValidLine(line)) {
				try {
					line = StringUtils.trimToEmpty(line);				
					String[] split = StringUtils.splitPreserveAllTokens(line, ",");
					Shield shield = new Shield();
					String idStr = StringUtils.trimToEmpty(split[0]);
					shield.setId( Integer.parseInt( idStr ));
					shield.setName( RESOURCE.getString("shield."+idStr) );
					shield.setCloseBonus( Integer.parseInt( StringUtils.trimToEmpty(split[1]) ));
					shield.setRangeBonus( Integer.parseInt( StringUtils.trimToEmpty(split[2]) ));
					metaData.addShield(shield);
				} catch (Exception e){
					log.error("Could not load shield lineNo "+lineNo, e);
				}
			}
		}
	}

	private void loadSpelllists(List<ISkill> skills) throws IOException {
		BufferedReader reader = getReader(CONF_SPELLLISTS);
		String line = null;
		int lineNo = 0;
		while ( (line = reader.readLine()) != null ) {
			lineNo++;
			if ( isValidLine(line)) {
				try {
					line = StringUtils.trimToEmpty(line);				
					String[] split = StringUtils.splitPreserveAllTokens(line, ",");
					int i = 0;
					Spelllist spelllist = new Spelllist();
					int id = Integer.parseInt(StringUtils.trim(split[i++]));
					spelllist.setId(Integer.valueOf(id));
					/* source */
					spelllist.setSource(StringUtils.trimToEmpty(split[i++]));
					/* name*/
					String name = StringUtils.trimToEmpty(split[i++]);
					/* try to localize */
					try {
						name = RESOURCE.getString("spelllist."+id);
					} catch (MissingResourceException e) {
						log.warn(CONF_SPELLLISTS + ": ignoring resource key 'spelllist."+id+"'");
					}
					spelllist.setName(name);
					for (int j=0; j<3; j++) {
						String attr = StringUtils.trimToNull(split[i++]);
						if (attr != null) {
							spelllist.addAttributes( StatEnum.valueOf(attr) );
					    }
					}				
					/* get the flags */
					SpelllistType.Builder builder = new SpelllistType.Builder();
					for (; i < split.length; i++) {
						String val = StringUtils.trimToNull(split[i]);
						if (val != null) {
							if (val.startsWith("E")) {
								builder.setEvil(true);
							} else if (val.startsWith("O")) {
								builder.setOpen();
							} else if (val.startsWith(CATEGORY_CHAR)) {
								builder.setClosed();
							} else if (val.startsWith("P")) {
								builder.setProf();
								builder.addProfessionId( Integer.valueOf(val.substring(1)) );
							} else if (val.startsWith("T")) {
								builder.setTrainingpack();
								builder.setTraingpackageSpelllist( Integer.valueOf(val.substring(1)) );
							}
						}
					}
					spelllist.setType(builder.build());
					skills.add(spelllist);
				} catch (Exception e) {
					log.error(CONF_SPELLLISTS + ": Could not load spell list lineNo "+lineNo, e);
				}
			}
		}
	}

	private void loadCultures(MetaData metaData) throws IOException {
		List<Culture> cultures = loadCultureFromReader(metaData, getReader(CONF_CULTURES), false);
		// load from user.dir
		File customCultures = new File(System.getProperty("user.home") + RMPreferences.RMOFFICE_DIR + CONF_CULTURES);
		if (customCultures.exists() && customCultures.isFile()) {
			BufferedReader cr = new BufferedReader( new InputStreamReader(new FileInputStream(customCultures), CHARSET) );
			cultures.addAll(loadCultureFromReader(metaData, cr, true));
		} else {
			log.debug(customCultures.getAbsolutePath()+" is not a file. ignoring user cultures.");
		}
		// add all culture
		metaData.setCultures(cultures);
	}

	private List<Culture> loadCultureFromReader(MetaData metaData, BufferedReader reader, boolean isUserFile) throws IOException {
		String line = null;
		int lineNo = 0;
		List<Culture> cultures = new ArrayList<Culture>(); 
		while ( (line = reader.readLine()) != null ) {
			lineNo++;
			if ( isValidLine(line)) {
				int i = 0;
				try {
					line = StringUtils.trimToEmpty(line);				
					String[] split = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, ",");
					Integer cultureId = Integer.valueOf( StringUtils.trim(split[i++]));
					if (isUserFile && cultureId.intValue() < MIN_USER_RACE_CULTURE_ID) {
						String msg = "Custom culture id ["+cultureId+"] must be greater or equals "+MIN_USER_RACE_CULTURE_ID;
						throw new IllegalArgumentException(msg);
					}
					Culture culture = new Culture();
					culture.setId(cultureId);
					cultures.add(culture);	
					/* races */
					String[] raceIds = StringUtils.split(StringUtils.trimToEmpty(split[i++]), '-');
					for (String raceIdStr : raceIds) {
						Integer raceId = Integer.valueOf( raceIdStr );
						Race race = metaData.getRace(raceId);
						if (race != null) {
							culture.addValidRace(race);
						} else {
							String msg = CONF_CULTURES+": Ignoring race for Culture ID "+cultureId+". Race ID "+raceIdStr+" is not available. line "+lineNo;
							log.warn(msg);
							if (isUserFile) {
								RMPreferences.getInstance().addError(msg);
							}
						}
					}
					/* name */
					if (StringUtils.trimToNull(split[i]) != null) {
						String resKey = StringUtils.trimToEmpty(split[i]);
						culture.setName(getResKey(resKey, isUserFile));
					} else {
						culture.setName("");
					}
					i++;
					/* rune */
					culture.setRune(StringUtils.trimToEmpty(split[i++]));
					/* weight */
					WeightHeight wh = new WeightHeight();
					wh.setWeightAvg( NumberUtils.toInt(split[i++]), NumberUtils.toInt(split[i++]));
					/* height */
					wh.setHeightAvg( NumberUtils.toInt(split[i++]), NumberUtils.toInt(split[i++]));
					culture.setWeightHeight(wh);
					/**/
					for (; i < split.length; i++) {
						if (StringUtils.trimToNull(split[i]) != null) {
							String[] rankStr = StringUtils.split( split[i], "=", 2);
							int rank = 0;
							SkillType skillType = null;
							if (rankStr.length > 1) {
								String[] valueParts = StringUtils.split(rankStr[1], "-");
								for (String valPart : valueParts) {
									String trimmedValPart = StringUtils.trim(valPart);
									try {
										rank = Integer.parseInt( trimmedValPart );
									} catch (NumberFormatException nfe) {
										skillType = SkillType.valueOf(trimmedValPart);
									}
								}
							}
							String idString = StringUtils.trimToEmpty(rankStr[0]);
							if (idString.length() > 1) {
								if (idString.charAt(0) == 'T') {
									String ext = (rank == 0) ? "" : (" " + rank);
									culture.addTodo(getResKey(idString.substring(1), isUserFile) + ext);
								} else if ("HOBBY".equals(idString)) {
									culture.setHobbyRanks(rank);
									culture.addTodo(RESOURCE.getString("todo.hobbies") + rank);
								} else if ("LANG".equals(idString)) {
									culture.setLanguageRanks(rank);
									culture.addTodo(RESOURCE.getString("todo.language") + rank);
								} else if ("SPELL".equals(idString)) {
									culture.setOpenSpellRanks(rank);
									culture.addTodo(RESOURCE.getString("todo.openspelllist") + rank);
								} else {
									Integer idObj = Integer.valueOf( idString.substring(1) );

									if (idString.charAt(0) == 'G') {
										SkillCategory skillgroup = metaData.getSkillCategory(idObj);
										if (skillgroup != null) {
											if (rank > 0) {
												culture.addYouthRank(skillgroup, rank);
											}
											if (skillType != null) {
												culture.addSkillgroupType(skillgroup, skillType);
											}
										} else {
											String msg = CONF_CULTURES+": Could not find skillgroup with ID "+idObj+" line "+lineNo;
											log.warn(msg);
											if (isUserFile) {
												RMPreferences.getInstance().addError(msg);
											}
										}
									} else if (idString.charAt(0) == 'S') {
										ISkill skill = metaData.getSkill(idObj);
										if (skill != null) {
											if (rank > 0) {
												culture.addYouthRank(skill, rank);
											}
											if (skillType != null) {
												culture.addSkillType(skill, skillType);
											}
										} else {
											String msg = CONF_CULTURES+": Could not find skill with ID "+idObj+" line "+lineNo;
											log.warn(msg);
											if (isUserFile) {
												RMPreferences.getInstance().addError(msg);
											}
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					String msg = CONF_CULTURES+": Could not convert lineNo "+lineNo+" i="+i+": "+e.getClass().getName()+" "+e.getMessage();
					log.error(msg, e);
					if (isUserFile) {
						RMPreferences.getInstance().addError(msg);
					}
				}
			}
		}
		return cultures;
	}

	private List<ISkill> loadSkills(MetaData metaData) throws IOException {
		List<ISkill> skills = new ArrayList<ISkill>();
		List<Integer> id = new ArrayList<Integer>();
		BufferedReader reader = getReader(CONF_SKILLS);
		String line = null;
		int lineNo = 0;
		while ( (line = reader.readLine()) != null ) {
			lineNo++;
			if ( isValidLine(line)) {
				try {
					line = StringUtils.trimToEmpty(line);				
					String[] split = StringUtils.splitPreserveAllTokens(line, ",", 6);
					Skill s = new Skill();
					s.setId( Integer.valueOf(StringUtils.trimToEmpty(split[0])) );
					/* skill category */
					s.setCategory( metaData.getSkillCategory( Integer.valueOf(StringUtils.trimToEmpty(split[1])) ) );
					/* source */
					s.setSource( StringUtils.trimToEmpty(split[2]) );
					/* Scope */
					if (StringUtils.trimToNull(split[3]) != null) {
						s.setScope( RaceScope.valueOf(StringUtils.trimToEmpty(split[3])) );
					}
					s.setName( StringUtils.trimToEmpty(split[4]) );
					try {
						s.setName(RESOURCE.getString("skill."+s.getId()));
					} catch (MissingResourceException e1) {
						if (log.isWarnEnabled()) log.warn("missing resource key 'skill."+s.getId()+"'");
					}
					loadSkillDescriptions(s);
					if (id.contains(s.getId())) {
						log.warn("Duplicate skill id "+s.getId()+". Ignoring line "+lineNo); 
					} else {
						skills.add(s);
						id.add(s.getId());
					}					
				} catch (Exception e) {
					log.error(CONF_SKILLS+": Could not read line "+lineNo+", cause "+e.getClass().getName()+": "+e.getMessage(), e);
				}
			}
		}
		loadSpelllists(skills);
		return skills;
	}

	private void loadSkillDescriptions(Skill s) {
		try {
			s.setDescription(null, RESOURCE.getString("skill."+s.getId()+".descr"));
		} catch (MissingResourceException e) {
			// ignore, description is not mandatory
		}
		for (LengthUnit lu : LengthUnit.values()) {
			try {
				s.setDescription(lu, RESOURCE.getString("skill."+s.getId()+".descr."+lu.name()));
			} catch (MissingResourceException e) {
				// ignore, description is not mandatory
			}
		}
	}

	private List<Race> loadRaces() throws IOException {
		List<Race> races = new ArrayList<Race>();
		// load the custom races
		loadRaceFromReader(races, getReader(CONF_RACE), false);
		// load from user.dir
		File customRaces = new File(System.getProperty("user.home") + RMPreferences.RMOFFICE_DIR + CONF_RACE);
		if (customRaces.exists() && customRaces.isFile()) {
			BufferedReader cr = new BufferedReader( new InputStreamReader(new FileInputStream(customRaces), CHARSET) );
			loadRaceFromReader(races, cr, true);
		} else {
			log.debug(customRaces.getAbsolutePath()+" is not a file. ignoring user races.");
		}
	    // 
		if (races.size() == 0) {
			throw new RuntimeException("Could not load any race, will exit");
		}
		/* sort */
		Collections.sort(races, new Comparator<Race>() {
			@Override
			public int compare(Race r1, Race r2) {
				if (r1.getScope().equals(r2.getScope())) {
					return r1.getName().compareTo(r2.getName());
				} else {
					return r1.getScope().compareTo(r2.getScope());
				}
			}
		});
		return races;
	}

	protected void loadRaceFromReader(List<Race> races, BufferedReader reader, boolean isUserFile) throws IOException {
		String line = null;
		int lineNo = 0;
		while ( (line = reader.readLine()) != null ) {
			lineNo++;
			if ( isValidLine(line)) {
				line = StringUtils.trimToEmpty(line);				
				String[] split = StringUtils.splitPreserveAllTokens(line, ',');
				if (split != null && split.length == (6 + StatEnum.values().length + ResistanceEnum.values().length) + 4 * 5 /* progression*/ + 6 ) {
					try {
						Race r = new Race();
						int i = 0;
						Integer id = Integer.valueOf(StringUtils.trim(split[i++]));
						if (isUserFile && id.intValue() < MIN_USER_RACE_CULTURE_ID) {
							String msg = "custom race id ["+id+"] must be greater or equals "+MIN_USER_RACE_CULTURE_ID;
							throw new IllegalArgumentException(msg);
						}
						r.setId( id );
						r.setName( getResKey(StringUtils.trim(split[i++]), isUserFile) );
						r.setScope( RaceScope.valueOf( StringUtils.trim(split[i++])) );
						r.setSource( StringUtils.trimToEmpty( StringUtils.trim(split[i++])) );
						r.setNameStyle( Style.valueOf(StringUtils.trim(split[i++])) );
						r.setOutline( StringUtils.trimToNull(split[i++]) );
						for (StatEnum at : StatEnum.values()) {
							r.setAttributeBonus(at, Integer.valueOf(StringUtils.trim(split[i++])));
						}
						for (ResistanceEnum re : ResistanceEnum.values()) {
							r.setResistanceBonus(re, Integer.valueOf(StringUtils.trim(split[i++])));
						}
						/* Progression Hits */

						int[][] tmp = new int[4][5];
						for (int k=0; k<4; k++) {
							for (int j=0; j<5; j++) {
								tmp[k][j] = Integer.parseInt( StringUtils.trimToEmpty(split[i++]) ); 
							}
						}
						r.setProgKoerperentw(new Progression(tmp[0][0], tmp[0][1], tmp[0][2], tmp[0][3], tmp[0][4]));
						r.setProgMagic(StatEnum.INTUITION, new Progression(tmp[1][0], tmp[1][1], tmp[1][2], tmp[1][3], tmp[1][4]));
						r.setProgMagic(StatEnum.EMPATHY, new Progression(tmp[2][0], tmp[2][1], tmp[2][2], tmp[2][3], tmp[2][4]));
						r.setProgMagic(StatEnum.PRESENCE, new Progression(tmp[3][0], tmp[3][1], tmp[3][2], tmp[3][3], tmp[3][4]));
						r.setSoulDeparture( Integer.parseInt(StringUtils.trim(split[i++])) );
						r.setRecoveryMultiplier( Float.parseFloat(StringUtils.trim(split[i++])));
						r.setBackgroundOptions( Integer.parseInt(StringUtils.trim(split[i++])));
						r.setExhaustionPoints( Integer.parseInt(StringUtils.trim(split[i++])) );
						i++; /* ignoring statloss */
						/*  RR  */
						String resLine = StringUtils.trimToNull(split[i++]);
						if (resLine != null) {
							r.addAdditionalResistanceLine(getResKey(resLine, isUserFile));
						}
						if (!RMPreferences.getInstance().isExcludedRaceId(r.getId())) {
							races.add(r);
						}
					} catch (Exception e) {
						String msg = CONF_RACE+": Could not load race line "+lineNo+": "+e.getClass().getName()+" "+e.getMessage();
						log.warn(msg);
						if (isUserFile) {
							RMPreferences.getInstance().addError(msg);
						}
					}
				} else {
					String msg = CONF_RACE+": Ignoring race lineNo "+lineNo+". Cause: Wrong format. Wrong number of token ("+(split==null?"0":""+split.length)+"). ";
					log.warn(msg);
					if (isUserFile) {
						RMPreferences.getInstance().addError(msg);
					}
				}
			}
		}
	}

	private boolean isValidLine(String line) {
		return StringUtils.trimToNull(line) != null &&  (! StringUtils.trimToEmpty(line).startsWith("#"));
	}

	private List<Profession> loadProfessions() throws IOException {
		List<Profession> profs = new ArrayList<Profession>();
		
		BufferedReader reader = getReader(CONF_PROF);
		String line = null;
		int lineNo = 0;
		while ( (line = reader.readLine()) != null ) {
			lineNo++;
			if ( isValidLine(line)) {
				line = StringUtils.trimToEmpty(line);
				String[] split = StringUtils.splitPreserveAllTokens(line, ',');
				if (split != null && split.length > 0) {
					Profession p = new Profession();
					int idx=0;
					p.setId(Integer.valueOf(StringUtils.trim(split[idx++]) ));
					p.setName(StringUtils.trim(split[idx++]));
					try {
						p.setName( RESOURCE.getString("profession."+p.getId()) );
					} catch (MissingResourceException e) {
						if (log.isWarnEnabled()) log.warn("No translation for profession id "+p.getId());
					}
					/* Stats */
					for (int i=0; i<4; i++) {
						if (StringUtils.trimToNull(split[idx]) != null) {
							try {
								p.getStats().add(StatEnum.valueOf(StringUtils.trim(split[idx])));
							} catch (RuntimeException e) {
								log.error("Could not decode attribute "+idx+" '"+split[idx]+"' in line "+lineNo +" "+line, e);
								throw e;
							}
						}
						idx++;
					}
					/* Realm not used yet*/
					idx++; 
					/* spell user type */
					String spellUserType = StringUtils.trimToNull(split[idx++]);
					if (spellUserType != null) {
						if (p.getStats().isArcane()) {
							spellUserType = "ARCANE" + spellUserType;
						}
						p.setSpellUserType(SpellUserType.valueOf(spellUserType));
					}
					/* Sourcebook*/
					p.setSource( StringUtils.trimToEmpty(split[idx++]) );
					/* Rune */
					p.setRune( StringUtils.trimToEmpty(split[idx++]) );
					/* the professions skill bonuses
					 * Attention: the skill groups/skills aren't loaded,yet */
					for (int i=idx; i < split.length; i++) {
						/* format : [G|S]<ID>=[<Bonus>|EVERYMAN|OCCUPATIONAL|RESTRICTED]  */
						String token = StringUtils.trimToNull(split[i]);
						if (token != null) {
							String[] parts = StringUtils.split(token, '=');
							if (parts != null && parts.length == 2 && parts[0] != null && parts[0].length() > 1) {								
								if ("ADD".equals(parts[0])) {
									/* additional info */
									try {
										parts[1] = RESOURCE.getString(parts[1]);										
									} catch (MissingResourceException e) {
										if (log.isDebugEnabled()) log.debug(CONF_PROF+": ignoring resource key '"+parts[1]+"' in line "+lineNo);
								   }
									p.addAdditionInfo(parts[1]);
								} else {
									try {
										int bonus = 0;
										SkillType skillType = null;
										try {
											bonus = Integer.parseInt(parts[1]);
										} catch (NumberFormatException e) {
											/* try the SkillType */
											skillType = SkillType.valueOf(StringUtils.trimToEmpty(parts[1]));
										}
										int id = Integer.parseInt(parts[0].substring(1));
										if (parts[0].startsWith("G")) {
											/* Group */
											if (skillType == null) {
												p.addSkillgroupBonus(id, bonus);
											} else {
												p.addSkillGroupType(id, skillType);
											}
										} else if (parts[0].startsWith(SKILL_CHAR)) {
											/* Skill */
											if (skillType == null) {
												p.addSkillBonus(id, bonus);
											} else {
												p.addSkillType(id, skillType);
											}
										}
									} catch (Exception e) {
										if (log.isWarnEnabled()) log.warn("Wrong format in lineNo "+lineNo+" token '"+token+"'");
									}
								}
							} else {
								if (log.isWarnEnabled()) log.warn("Could not parse '"+split[i]+"' in lineNo "+lineNo);
							}
						}
						idx++;
					}
					if (!RMPreferences.getInstance().isExcludedProfId(p.getId())) {
						profs.add(p);
					}
				}
			}
		}
		if (profs.size() == 0) {
			throw new RuntimeException("Could not load any professions, will exit");
		}
		Collections.sort(profs, new Comparator<Profession>() {
			@Override
			public int compare(Profession p1, Profession p2) {
				return p1.getName().compareTo(p2.getName());
			}
		});
		return profs;
	}
	
	private List<SkillCategory> loadSkillcosts(MetaData data) throws IOException {
		List<SkillCategory> skillcategories = new ArrayList<SkillCategory>();
		List<Integer> skillgroupIds = new ArrayList<Integer>();

		BufferedReader reader = getReader(CONF_SKILLCOSTS);
		String line = null;
		int lineNo = 0;
		/* first header line */
		String headerLine = reader.readLine();
		lineNo++;
		String[] firstLine = StringUtils.splitPreserveAllTokens(headerLine, ',');

		/* data */
		while ( (line = reader.readLine()) != null ) {
			lineNo++;
			if (isValidLine(line)) {
				try {
					int col = 0;
					String[] sgParts = StringUtils.splitPreserveAllTokens(line, ',');
					/* load/create skill group */
					SkillCategory skillgroup = new SkillCategory();
					Integer id = Integer.valueOf( StringUtils.trim(sgParts[col++]) );
					if (skillgroupIds.contains(id)) {
						log.warn("Duplicate skillgroup id "+id+". Because the was loaded before. lineNo="+lineNo);
					} else {
						skillgroupIds.add(id);
						skillgroup.setId(id);
						skillgroup.setName(sgParts[col++]);
						try {
							skillgroup.setName(RESOURCE.getString("skillgroup."+id));
						} catch (MissingResourceException e) {
							if (log.isWarnEnabled()) log.warn("No translation for key skillgroup."+id);
						}
						/* */
						String rankChars = StringUtils.trimToEmpty(sgParts[col++]);
						if (rankChars.length() > 0) {
							RankType rankType = RankType.valueOf( rankChars.substring(0, 1) );
							skillgroup.setRankType( rankType );
							if (rankType.isMagical() && rankChars.length() > 1) {
								RankSubType rst = RankSubType.valueOf(rankChars.substring(1, 2));
								skillgroup.setRankSubType(rst);
							}
						}
						skillgroup.setAttributes(new ArrayList<StatEnum>());
						
						for (int i=0; i<3; i++) {
							try {
								if ( StringUtils.trimToNull(sgParts[col]) != null) {
									skillgroup.getAttributes().add(StatEnum.valueOf(StringUtils.trim(sgParts[col])));
								}
							} catch (RuntimeException e) {
								log.error("Could not decode '"+sgParts[col]+"' in line "+lineNo+" "+line, e);
								throw e;
							}
							col++;
						}
						skillcategories.add(skillgroup);
						/* read the costs */
						List<Integer> costArr = new ArrayList<Integer>();
						for (int i=col; i < sgParts.length; i++) {
							/* firstLine:   ID, PDFFlag, sKill name, Flag, Attr1, Attr2, Attr3, 7./10./13./.. = ID of Profession, 8./9./11./12./.. = Name of Profession empty*/
							int headerIdx = (int) Math.floor( (i-col) / 3 ) * 3 + col;
							/* split the prof-id string */
							List<Profession> profs = new ArrayList<Profession>();
							for (String idStr : StringUtils.split(StringUtils.trim(firstLine[headerIdx]), "-")) {
								Profession profession = data.getProfession(Integer.valueOf( idStr ));
								if (profession != null) {
									profs.add(profession);
								}
							}
							if (profs.size() > 0) {						
								String cost = StringUtils.trimToEmpty(sgParts[i]);
								if (cost.length() > 0) {
									costArr.add( Integer.valueOf( Integer.parseInt(cost) ));
								}
								/* check if the last of three costs read */
								if ( (i-col) % 3 == 2 ) {
									int[] costIntArr = new int[costArr.size()];
									for (int j=0; j<costArr.size(); j++) {
										costIntArr[j] = costArr.get(j).intValue(); 
									}
									/* add for each profession */
									for (Profession prof : profs) {
										Skillcost costObj = new Skillcost(costIntArr);
										data.addSkillcost(prof, skillgroup, costObj );
									}
									costArr.clear();
								}
							} else if (! "0".equals(StringUtils.trimToEmpty(firstLine[headerIdx]))) {
								if (log.isWarnEnabled()) log.warn("ignoring costs for profession id '"+firstLine[headerIdx]+"' in line "+lineNo+ " token="+i);
							}
						}
					}
				} catch (RuntimeException e) {
					log.error("Could not read line "+lineNo+": "+e.getClass().getName()+" "+e.getMessage(),e );
					throw e;
				}
			}
		}
		/* sort skill categories */
		Collections.sort(skillcategories, new Comparator<SkillCategory>() {

			@Override
			public int compare(SkillCategory sc1, SkillCategory sc2) {
				if (sc1 == null || sc2 == null) {
					return 0;
				}
				boolean sg1IsSpell = sc1.getRankType().isMagical() && !sc1.getRankType().isProgressionMagic();
				boolean sg2IsSpell = sc2.getRankType().isMagical() && !sc2.getRankType().isProgressionMagic();
				if (sg1IsSpell && !sg2IsSpell) {
					return 1;
				}
				if (!sg1IsSpell && sg2IsSpell) {
					return -1;
				}
				return sc1.getName().compareTo(sc2.getName());
			}});
		return skillcategories;
	}
	

	private BufferedReader getReader(String file) {
		log.debug("reading configuration file "+file);
		InputStream res = getClass().getResourceAsStream( file );
		if (res == null) throw new RuntimeException("Could not find the "+file);
		return new BufferedReader( new InputStreamReader(res, CHARSET) );
	}
	
	private String getResKey(String resKey, boolean isUserFile) {
		if (isUserFile) {
			if (RESOURCE.containsKey(resKey)) {
				return RESOURCE.getString(resKey);
			} else {
				return resKey;
			}
		} else {
			return RESOURCE.getString(resKey);
		}
	}
}
