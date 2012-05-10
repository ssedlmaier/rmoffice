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

import java.text.MessageFormat;
import java.util.List;

import javax.swing.JOptionPane;

import net.sf.rmoffice.core.TalentFlaw;
import net.sf.rmoffice.core.ToDo;
import net.sf.rmoffice.meta.enums.TalentFlawSkillCategoryType;
import net.sf.rmoffice.meta.enums.ToDoType;

public class RankPart extends AbstractTalentFlawPart {
	private static final String RANK_ID = TalentFlawFactory.registerID("rank");

	private final int amount;
	private List<TalentFlawSkillCategoryType> skillCatTypes;
	
	public RankPart(int amount, List<TalentFlawSkillCategoryType> catTypes) {
		this.amount = amount;
		this.skillCatTypes = catTypes;
	}
	
	@Override
	public String getId() {
		return RANK_ID;
	}

	@Override
	public void addToTalentFlaw(TalentFlawContext context, TalentFlaw talentFlaw) {
		context.getSheet().addToDo(new ToDo(asText(), ToDoType.TALENTFLAW));
		JOptionPane.showMessageDialog(context.getOwner(), RESOURCE.getString("ui.talentflaw.value.rank.message"));
	}

	@Override
	public String asText() {
		String pattern = RESOURCE.getString("ui.talentflaw.value.rank.descr");
		StringBuilder cats = new StringBuilder();
		for (TalentFlawSkillCategoryType catType : skillCatTypes) {
			if (cats.length() > 0) {
				cats.append(", ");
			}
			cats.append(RESOURCE.getString("TalentFlawSkillCategoryType."+catType.name()));
		}
		return MessageFormat.format(pattern, Integer.valueOf(amount), cats.toString());
	}

}
