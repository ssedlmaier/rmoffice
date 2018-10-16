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
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.ui.dialog.SelectionDialog;

public class ChooseStatPart extends AbstractTalentFlawPart {
	private static final String ID = TalentFlawFactory.registerID("choose_statbonus");
	
	protected final int amount;
	protected final int bonus;
	
	public ChooseStatPart(int amount, int bonus) {
		this.amount = amount;
		this.bonus = bonus;
	}
	
	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void addToTalentFlaw(TalentFlawContext context, TalentFlaw talentFlaw) {
		List<StatEnum> checkedItems = showStatsSelectionDialog(context);
		for (StatEnum stat : checkedItems) {
			talentFlaw.addStatBonus(stat, Integer.valueOf(bonus));
		}
	}


	@Override
	public String asText() {
		StringBuilder sb = new StringBuilder();
		String msg = MessageFormat.format(RESOURCE.getString("ui.talentflaw.value.choosefrom"), ""+amount, RESOURCE.getString("ui.talentflaw.value.choosefrom.allstats"), format(bonus) );
		appendBonusLine(sb, msg, Integer.valueOf(bonus));
		return sb.toString();
	}

	protected List<StatEnum> showStatsSelectionDialog(TalentFlawContext context) {
		SelectionDialog<StatEnum> dialog = new SelectionDialog<StatEnum>(context.getOwner(), amount, (Object[]) StatEnum.values());
		dialog.setVisible(true);
		List<StatEnum> checkedItems = dialog.getCheckedItems();
		return checkedItems;
	}
}
