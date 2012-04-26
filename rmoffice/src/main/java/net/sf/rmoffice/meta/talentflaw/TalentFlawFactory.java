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
package net.sf.rmoffice.meta.talentflaw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.talentflaw.parser.ChooseParser;
import net.sf.rmoffice.meta.talentflaw.parser.DescriptionParser;
import net.sf.rmoffice.meta.talentflaw.parser.ITalentFlawPartParser;
import net.sf.rmoffice.meta.talentflaw.parser.InitiativeParser;
import net.sf.rmoffice.meta.talentflaw.parser.BonusParser;
import net.sf.rmoffice.meta.talentflaw.parser.SkillTypeParser;

/**
 * Factory for creating {@link ITalentFlawPart}.
 */
public class TalentFlawFactory {
	
	private static final Set<String> partIDs = new HashSet<String>();
	private final MetaData metaData;
	private final List<ITalentFlawPartParser<?>> parsers = new ArrayList<ITalentFlawPartParser<?>>();
	
	/**
	 * @param metaData the meta data, not null
	 */
	public TalentFlawFactory(MetaData metaData) {
		this.metaData = metaData;
		initParser();
	}

	/**
	 * Registers the given partID to be shown in the UI.
	 * 
	 * @param partID the part ID
	 * @return the part ID
	 */
	/* package */ static String registerID(String partID) {
		partIDs.add(partID);
		return partID;
	}
	
	/**
	 * Returns an unmodifiable set of registered IDs.
	 * 
	 * @return set of part IDs, not {@code null}
	 */
	public static Set<String> getPartIDs() {
		return Collections.unmodifiableSet(partIDs);
	}
	
	private void initParser() {
		// TODO find a nicer solution to "register" the parsers
		parsers.add(new InitiativeParser());
		parsers.add(new DescriptionParser());
		parsers.add(new BonusParser(metaData)); 
		parsers.add(new SkillTypeParser(metaData)); 
		parsers.add(new ChooseParser(metaData));
	}

	/**
	 * Creates a {@link ITalentFlawPart} from the given string.
	 * 
	 * @param partAsString the part as string, may be {@code null}
	 */
	public ITalentFlawPart parseTalentFlawPart(String partAsString) {
		for (Iterator<ITalentFlawPartParser<?>> it = parsers.iterator(); it.hasNext(); ){
			ITalentFlawPartParser<?> parser = it.next();
			if (parser.isParseable(partAsString)) {
				return parser.parse(partAsString);
			}
		}
		throw new IllegalArgumentException("Could not find a parser for '"+partAsString+"'");
//		if (param.startsWith("CHOOSE")) {
//			String[] parts = StringUtils.splitPreserveAllTokens(param.substring(6), "=");
//			TalentFlawChoice tfc = new TalentFlawChoice();
//			tfc.setAmount(Integer.parseInt(parts[0]));
//			// values
//			String[] valueParts = StringUtils.splitPreserveAllTokens(parts[1], ";");
//			for (String val : valueParts) {
//				if (CATEGORY_CHAR.equals(val.substring(0, 1))) {
//					Integer catID = Integer.valueOf(val.substring(1));
//					SkillCategory skillCat = metaData.getSkillCategory(catID);
//					tfc.addValue(skillCat);
//				} else if (SKILL_CHAR.equals(val.substring(0,1))) {
//					Integer skillID = Integer.valueOf(val.substring(1));
//					ISkill skill = metaData.getSkill(skillID);
//					tfc.addValue(skill);
//				}
//			}
//			// result
//			try {
//				tfc.setSkillType( SkillType.valueOf(StringUtils.trimToEmpty(parts[2])) );
//			} catch (Exception e) {
//				tfc.setBonus(Integer.valueOf(StringUtils.trimToEmpty(parts[2])));
//				// if we get here an exception, too, the format is wrong (exception is thrown --> line can not be decoded)
//			}
//			talFlawVal.putChoice(tfc);
	}
}
