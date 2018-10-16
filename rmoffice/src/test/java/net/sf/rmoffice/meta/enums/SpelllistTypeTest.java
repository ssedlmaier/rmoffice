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
package net.sf.rmoffice.meta.enums;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


import net.sf.rmoffice.meta.enums.SpelllistType;

import org.junit.Test;


public class SpelllistTypeTest {
	
	@Test
	public void testEquals() {
		SpelllistType t1 = new SpelllistType(SpelllistType.OPEN, false, null, null);
		SpelllistType t2 = new SpelllistType(SpelllistType.OPEN, false,  null, null);
		SpelllistType t3 = new SpelllistType(SpelllistType.CLOSED, false,  null, null);
		assertTrue(t1.equals(t2));
		assertEquals(t1.hashCode(), t2.hashCode());
		assertFalse(t1.equals(t3));
		assertFalse(t2.equals(t3));
		/* */
		List<Integer> prof1 = new ArrayList<Integer>();
		prof1.add(Integer.valueOf(4));
		SpelllistType tp1 = new SpelllistType(SpelllistType.PROFESSION, false, prof1, null);
		
		assertTrue(tp1.equals(tp1));
		assertFalse(tp1.equals(null));
		assertFalse(tp1.equals("xyz"));
		
		List<Integer> prof2 = new ArrayList<Integer>();
		prof2.add(Integer.valueOf(4));
		SpelllistType tp2 = new SpelllistType(SpelllistType.PROFESSION, false, prof2, null);
		
		assertTrue( tp1.equals( tp2 ));
		assertEquals( tp1.hashCode(), tp2.hashCode());
		
		prof2.add(Integer.valueOf(5));
		SpelllistType tp3 = new SpelllistType(SpelllistType.PROFESSION, false, prof2, null);
		assertFalse( tp1.equals( tp3 ));
		
		SpelllistType tp4 = new SpelllistType(SpelllistType.PROFESSION, false, null, null);
		assertFalse( tp4.equals(tp1) );
		assertFalse( tp1.equals(tp4) );
		
		List<Integer> prof5 = new ArrayList<Integer>();
		prof5.add(Integer.valueOf(5));
		SpelllistType tp5 = new SpelllistType(SpelllistType.PROFESSION, false, prof5, null);
		assertFalse( tp1.equals( tp5 ));
		
		List<Integer> prof6 = new ArrayList<Integer>();
		prof6.add(new Integer(2829));
		SpelllistType tp6 = new SpelllistType(SpelllistType.PROFESSION, false, prof6, null);
		List<Integer> prof7 = new ArrayList<Integer>();
		prof7.add(new Integer(2829));
		SpelllistType tp7 = new SpelllistType(SpelllistType.PROFESSION, false, prof7, null);
		assertTrue(tp6.equals(tp7));
		assertTrue(tp7.equals(tp6));
		
		SpelllistType tpTP = new SpelllistType(SpelllistType.TRAINING, false, null, Integer.valueOf(62));
		SpelllistType tpTP2 = new SpelllistType(SpelllistType.TRAINING, false, null, Integer.valueOf(62));
		SpelllistType tpTP3 = new SpelllistType(SpelllistType.TRAINING, false, null, Integer.valueOf(63));
		assertTrue(tpTP.equals(tpTP2));
		assertFalse(tpTP.equals(tpTP3));
	}
	
}
