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
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.ui.dialog.SelectionDialog;

public class ChoosePart extends AbstractTalentFlawPart {
	public static final String CHOOSE_SKILLBONUS = TalentFlawFactory.registerID("chooseskillbonus");
	public static final String CHOOSE_CATBONUS = TalentFlawFactory.registerID("choosecategorybonus");
	public static final String CHOOSE_SKILLTYPE = TalentFlawFactory.registerID("chooseskilltype");
	public static final String CHOOSE_CATTYPE = TalentFlawFactory.registerID("choosecategorytype");
	
	protected final Object[] selectables;
	protected final Integer bonus;
	protected final SkillType type;
	protected final int amount;
	private final boolean isSkill;

	public ChoosePart(int bonus, int amount, boolean isSkill, Object... selectables ) {
		this.isSkill = isSkill;
		this.selectables = selectables;
		this.bonus = Integer.valueOf(bonus);
		this.amount = amount;
		this.type = null;
	}

	public ChoosePart(SkillType type, int amount, boolean isSkill, Object... selectables) {
		this.isSkill = isSkill;
		this.selectables = selectables;
		this.type = type;
		this.amount = amount;
		this.bonus = null;
	}
	
	@Override
	public String getId() {
		String id = null;
		if (bonus != null) {
			id = (isSkill ? CHOOSE_SKILLBONUS : CHOOSE_CATBONUS);
		} else if (type != null) {
			id = (isSkill ? CHOOSE_SKILLTYPE : CHOOSE_CATTYPE);
		}
		return id;
	}

	@Override
	public void addToTalentFlaw(Frame owner, TalentFlaw talentFlaw) {
		if (isSkill) {
			SelectionDialog<ISkill> dialog = new SelectionDialog<ISkill>(owner, amount, selectables);
			dialog.setVisible(true);
			for (ISkill skill : dialog.getCheckedItems()) {
				if (type != null) {
					talentFlaw.addSkillType(skill, type);	
				} else if (bonus != null) {
					talentFlaw.addSkillBonus(skill, bonus);
				}
			}
		} else {
			SelectionDialog<SkillCategory> dialog = new SelectionDialog<SkillCategory>(owner, amount, selectables);
			dialog.setVisible(true);
			for (SkillCategory cat : dialog.getCheckedItems()) {
				if (type != null) {
					talentFlaw.addSkillCatType(cat, type);
				} else {
					talentFlaw.addSkillCatBonus(cat, bonus);
				}
			}
		}
	}

	@Override
	public String asText() {
		return null;
	}

}
