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
import java.util.List;

import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.meta.enums.ResistanceEnum;
import net.sf.rmoffice.ui.dialog.SelectionDialog;

public class ChooseResistancePart extends AbstractTalentFlawPart {
	private static final String ID = TalentFlawFactory.registerID("choose_resistance");
	
	private final int amount;
	private final int bonus;
	private final ResistanceEnum[] selectables;
	private final int spellBonus;
	
	public ChooseResistancePart(int amount, int bonus, int spellBonus, ResistanceEnum... selectables) {
		this.amount = amount;
		this.bonus = bonus;
		this.spellBonus = spellBonus;
		this.selectables = selectables;
	}
	
	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void addToTalentFlaw(TalentFlawContext context, TalentFlaw talentFlaw) {
		List<ResistanceEnum> selection = showSelectionDialog(context);
		for (ResistanceEnum res : selection) {
			talentFlaw.setResistanceBonus(res, Integer.valueOf(bonus));
			if (spellBonus != 0) {
				talentFlaw.addSpellRealmBonus(res.getStat(), Integer.valueOf(spellBonus));
			}
		}
	}

	@Override
	public String asText() {
		StringBuilder what = new StringBuilder();
		for (ResistanceEnum res : selectables) {
			if (what.length() > 0) {
				what.append(", ");
			}
			what.append(res.toString());
		}
		String msg = MessageFormat.format(RESOURCE.getString("ui.talentflaw.value.choosefrom"), ""+amount, what, Integer.valueOf(bonus));
		if (spellBonus != 0) {
			msg += "( + " + RESOURCE.getString("rolemaster.spells") + " " + format(spellBonus) + ")";
		}
		return msg;
	}
	
	protected List<ResistanceEnum> showSelectionDialog(TalentFlawContext context) {
		SelectionDialog<ResistanceEnum> dialog = new SelectionDialog<ResistanceEnum>(context.getOwner(), amount, selectables);
		dialog.setVisible(true);
		List<ResistanceEnum> checkedItems = dialog.getCheckedItems();
		return checkedItems;
	}

}
