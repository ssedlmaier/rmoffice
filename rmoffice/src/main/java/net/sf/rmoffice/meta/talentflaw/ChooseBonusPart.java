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

import java.awt.Frame;

import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.ui.dialog.SelectionDialog;

public class ChooseBonusPart extends AbstractTalentFlawPart {
	public static final String CHOOSE_SKILLBONUS = TalentFlawFactory.registerID("chooseskillbonus");
	public static final String CHOOSE_CATBONUS = TalentFlawFactory.registerID("choosecategorybonus");
	
	protected final ISkill[] skills;
	protected final SkillCategory[] skillCategories;
	protected final Integer bonus;
	protected final int amount;

	public ChooseBonusPart(int bonus, int amount, ISkill... skills ) {
		this.skills = skills;
		this.bonus = Integer.valueOf(bonus);
		this.amount = amount;
		this.skillCategories = null;
	}

	public ChooseBonusPart(int bonus, int amount, SkillCategory... skillCats) {
		this.skillCategories = skillCats;
		this.bonus = Integer.valueOf(bonus);
		this.amount = amount;
		this.skills = null;
	}
	
	@Override
	public String getId() {
		return skills != null ? CHOOSE_SKILLBONUS : CHOOSE_CATBONUS;
	}

	@Override
	public void addToTalentFlaw(Frame owner, TalentFlaw talentFlaw) {
		if (skillCategories != null) {
			SelectionDialog<SkillCategory> dialog = new SelectionDialog<SkillCategory>(owner, amount, skillCategories);
			dialog.setVisible(true);
			for (SkillCategory cat : dialog.getCheckedItems()) {
				talentFlaw.addSkillCatBonus(cat, bonus);
			}
		} else if (skills != null) {
			SelectionDialog<ISkill> dialog = new SelectionDialog<ISkill>(owner, amount, skills);
			dialog.setVisible(true);
			for (ISkill skill : dialog.getCheckedItems()) {
				talentFlaw.addSkillBonus(skill, bonus);
			}
		}
		
	}

	@Override
	public String asText() {
		return null;
	}

}
