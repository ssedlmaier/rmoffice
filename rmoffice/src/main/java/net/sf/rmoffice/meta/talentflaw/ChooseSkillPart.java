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
package net.sf.rmoffice.meta.talentflaw;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.INamed;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.pdf.PDFCreator;
import net.sf.rmoffice.ui.dialog.SelectionDialog;

public class ChooseSkillPart extends AbstractTalentFlawPart {
	private static final String CAT_FOLLOWUP_ACTION = "CAT";
	private static final String CHOOSESKILL_BONUS = TalentFlawFactory.registerID("chooseskill_bonus");
	private static final String CHOOSESKILL_TYPE = TalentFlawFactory.registerID("chooseskill_type");
	
	protected final List<ISkill> selectableSkills;
	protected final List<SkillCategory> selectableCats;
	protected final Integer bonus;
	protected final SkillType type;
	protected final int amount;
	private String followUpAction;
	private int followUpBonus;
	private SkillType followUpType;

	public ChooseSkillPart(int bonus, int amount, List<ISkill> selectableSkills, List<SkillCategory> selectableCats) {
		this.selectableSkills = selectableSkills;
		this.selectableCats = selectableCats;
		this.bonus = Integer.valueOf(bonus);
		this.amount = amount;
		this.type = null;
	}

	public ChooseSkillPart(SkillType type, int amount, List<ISkill> selectableSkills, List<SkillCategory> selectableCats) {
		this.selectableSkills = selectableSkills;
		this.selectableCats = selectableCats;
		this.type = type;
		this.amount = amount;
		this.bonus = null;
	}
	
	public void setFollowup(String followUpAction, int followUpBonus, SkillType followUpType) {
		this.followUpAction = followUpAction;
		this.followUpBonus = followUpBonus;
		this.followUpType = followUpType;
	}
	
	@Override
	public String getId() {
		String id = null;
		if (bonus != null) {
			id = CHOOSESKILL_BONUS;
		} else if (type != null) {
			id = CHOOSESKILL_TYPE;
		}
		return id;
	}

	@Override
	public void addToTalentFlaw(TalentFlawContext context, TalentFlaw talentFlaw) {
		List<Object> selectables = new ArrayList<Object>();
		selectables.addAll(selectableSkills);
		for (ISkill skill : context.getSheet().getSkills()) {
			SkillCategory cat1 = context.getSheet().getSkillcategory(skill);
			if (selectableCats.contains(cat1)) {
				selectables.add(skill);
			}
		}
		SelectionDialog<ISkill> dialog = new SelectionDialog<ISkill>(context.getOwner(), amount, selectables.toArray());
		dialog.setVisible(true);
		for (ISkill skill : dialog.getCheckedItems()) {
			if (type != null) {
				talentFlaw.addSkillType(skill, type);	
			} else if (bonus != null) {
				talentFlaw.addSkillBonus(skill, bonus);
			}
			// optional follow-up action
			if (CAT_FOLLOWUP_ACTION.equalsIgnoreCase(followUpAction)) {
				SkillCategory cat = context.getSheet().getSkillcategory(skill);
				if (followUpType != null) {
					talentFlaw.addSkillCatType(cat, followUpType);
				} else {
					talentFlaw.addSkillCatBonus(cat, Integer.valueOf(followUpBonus));
				}
			}
		}

	}

	@Override
	public String asText() {
		String what = type != null ? RESOURCE.getString("SkillType."+type.name()) : PDFCreator.format(bonus.intValue(), false);
		StringBuilder from = new StringBuilder();
		List<Object> selectables = new ArrayList<Object>();
		selectables.addAll(selectableSkills);
		selectables.addAll(selectableCats);
		for (Object val : selectables) {
			if (from.length() > 0) {
				from.append(", ");
			}
			if (val instanceof INamed) {
				from.append(((INamed)val).getName());
			}
		}
		String followUpString = "";
		if (! StringUtils.isEmpty(followUpAction)) {
			String followUpWhat = "--> ";
			followUpWhat += (followUpType != null ? RESOURCE.getString("SkillType."+followUpType.name()) : PDFCreator.format(followUpBonus, false));
			followUpString = MessageFormat.format(RESOURCE.getString("ui.talentflaw.value.chooseskillfrom."+StringUtils.lowerCase(followUpAction)), followUpWhat);
		}
		return MessageFormat.format(RESOURCE.getString("ui.talentflaw.value.chooseskillfrom"), what, ""+amount, from.toString(), followUpString);
	}

}
