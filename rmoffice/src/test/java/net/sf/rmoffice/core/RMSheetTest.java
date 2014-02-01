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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.meta.IProgression;
import net.sf.rmoffice.meta.Profession;
import net.sf.rmoffice.meta.Progression;
import net.sf.rmoffice.meta.Race;
import net.sf.rmoffice.meta.UTProfession;
import net.sf.rmoffice.meta.UTRace;
import net.sf.rmoffice.meta.enums.StatEnum;

import org.junit.Test;




/**
 * 
 */
public class RMSheetTest {
	
	@Test
	public void testUpdateProgressionMagie() {
		final UTProfession profession = new UTProfession();
		List<StatEnum> attributes = new ArrayList<StatEnum>();
		attributes.add(StatEnum.EMPATHY);
		profession.setStats(attributes );
		
		final UTRace race = new UTRace();
		race.setProgMagic(StatEnum.EMPATHY, new Progression(0, 5, 4, 3, 2));
		race.setProgMagic(StatEnum.INTUITION, new Progression(0, 3, 3, 1, 1));
		
		RMSheet sheet = new RMSheet() {
			/** {@inheritDoc} */
			@Override
			public Profession getProfession() {
				return profession;
			}
			
			/** {@inheritDoc} */
			@Override
			public Race getRace() {
				return race;
			}
			
			/** {@inheritDoc} */
			@Override
			public boolean isMagicRealmEditable() {
				return true;
			}
		};
		/* none chosen, yet: default it INTUITION */
		sheet.updateMagicProgessionAndRealm();
		IProgression progMagie = sheet.getProgressionPower();
		assertEquals(0f, progMagie.getDigit(0), 0.01f);
		assertEquals(3f, progMagie.getDigit(1), 0.01f);
		assertEquals(3f, progMagie.getDigit(2), 0.01f);
		assertEquals(1f, progMagie.getDigit(3), 0.01f);
		assertEquals(1f, progMagie.getDigit(4), 0.01f);
		
		/* choose EMPATHY */
		sheet.setMagicRealm(StatEnum.EMPATHY);
		
		progMagie = sheet.getProgressionPower();
		assertEquals(0f, progMagie.getDigit(0), 0.01f);
		assertEquals(5f, progMagie.getDigit(1), 0.01f);
		assertEquals(4f, progMagie.getDigit(2), 0.01f);
		assertEquals(3f, progMagie.getDigit(3), 0.01f);
		assertEquals(2f, progMagie.getDigit(4), 0.01f);
		
		/* choose INTUITION */
		sheet.setMagicRealm(StatEnum.INTUITION);
		
		progMagie = sheet.getProgressionPower();
		assertEquals(0f, progMagie.getDigit(0), 0.01f);
		assertEquals(3f, progMagie.getDigit(1), 0.01f);
		assertEquals(3f, progMagie.getDigit(2), 0.01f);
		assertEquals(1f, progMagie.getDigit(3), 0.01f);
		assertEquals(1f, progMagie.getDigit(4), 0.01f);
		
	}
	
	public void testHitPoints() {
		RMSheet sheet = new RMSheet() {
			@Override
			protected int getProgressionBodyTotalBonus() {
				return 5;
			}
		};
		assertEquals(5, sheet.getHitPoints());
		
		sheet = new RMSheet() {
			@Override
			protected int getProgressionBodyTotalBonus() {
				return 3;
			}
			@Override
			public List<TalentFlaw> getTalentsFlaws() {
				ArrayList<TalentFlaw> l = new ArrayList<TalentFlaw>();
				TalentFlaw t = new TalentFlaw();
				t.setTolerance(Float.valueOf(0.5f));
				l.add(t);
				return l;
			}
		};
		assertEquals(2, sheet.getHitPoints());
	}
}
