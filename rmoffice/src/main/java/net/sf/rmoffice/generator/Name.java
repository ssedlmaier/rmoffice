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
package net.sf.rmoffice.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates styled names that sound like the Tolkien names.
 */
public class Name {
	private final static Logger log = LoggerFactory.getLogger(Name.class);

	public static enum Style {
		HUMAN, ELVES, DWARF, HOBBIT, ORC
	}

	private static Random rand = new Random();

	private final static Map<Style, String[][]> syllablesMale = new HashMap<Name.Style, String[][]>();
	private final static Map<Style, String[][]> syllablesFemale = new HashMap<Name.Style, String[][]>();
	
	static {
		syllablesMale.put(Style.HUMAN, new String[][] {
				{
						"A", "Ab", "Ac", "Ad", "Af", "Agr", "Ast", "As", "Al", "Adw", "Adr", "Ar", "B", "Br", "C", "C", "C", "Cr", "Ch", "Cad", "D",
						"Dr", "Dw", "Ed", "Eth", "Et", "Er", "El", "Eow", "F", "Fr", "G", "Gr", "Gw", "Gw", "Gal", "Gl", "H", "Ha", "Ib", "J", "Jer",
						"K", "Ka", "Ked", "L", "Loth", "Lar", "Leg", "M", "Mir", "N", "Nyd", "Ol", "Oc", "On", "P", "Pr", "Q", "R", "Rh", "S", "Sev",
						"T", "Tr", "Th", "Th", "Ul", "Um", "Un", "V", "Y", "Yb", "Z", "W", "W", "Wic"
				},
				{
						"a", "ae", "ae", "au", "ao", "are", "ale", "ali", "ay", "ardo", "e", "edri", "ei", "ea", "ea", "eri", "era", "ela", "eli",
						"enda", "erra", "i", "ia", "ie", "ire", "ira", "ila", "ili", "ira", "igo", "o", "oha", "oma", "oa", "oi", "oe", "ore", "u",
						"y"
				},
				{
						"", "", "", "", "", "", "", "", "a", "and", "b", "bwyn", "baen", "bard", "c", "ch", "can", "d", "dan", "don", "der", "dric",
						"dus", "f", "g", "gord", "gan", "han", "har", "jar", "jan", "k", "kin", "kith", "kath", "koth", "kor", "kon", "l", "li",
						"lin", "lith", "lath", "loth", "ld", "ldan", "m", "mas", "mos", "mar", "mond", "n", "nydd", "nidd", "nnon", "nwan", "nyth",
						"nad", "nn", "nnor", "nd", "p", "r", "red", "ric", "rid", "rin", "ron", "rd", "s", "sh", "seth", "sean", "t", "th", "th",
						"tha", "tlan", "trem", "tram", "v", "vudd", "w", "wan", "win", "win", "wyn", "wyn", "wyr", "wyr", "wyth"
				}
		});
		syllablesFemale.put(Style.HUMAN, new String[][] {
				{
						"A", "Ab", "Ac", "Ad", "Af", "Agr", "Ast", "As", "Al", "Adw", "Adr", "Ar", "B", "Br", "C", "C", "C", "Cr", "Ch", "Cad", "D",
						"Dr", "Dw", "Ed", "Eth", "Et", "Er", "El", "Eow", "F", "Fr", "G", "Gr", "Gw", "Gw", "Gal", "Gl", "H", "Ha", "Ib", "Jer", "K",
						"Ka", "Ked", "L", "Loth", "Lar", "Leg", "M", "Mir", "N", "Nyd", "Ol", "Oc", "On", "P", "Pr", "Q", "R", "Rh", "S", "Sev", "T",
						"Tr", "Th", "Th", "Ul", "Um", "Un", "V", "Y", "Yb", "Z", "W", "W", "Wic"
				},
				{
						"a", "a", "a", "ae", "ae", "au", "ao", "are", "ale", "ali", "ay", "ardo", "e", "e", "e", "ei", "ea", "ea", "eri", "era",
						"ela", "eli", "enda", "erra", "i", "i", "i", "ia", "ie", "ire", "ira", "ila", "ili", "ira", "igo", "o", "oa", "oi", "oe",
						"ore", "u", "y"
				},
				{
						"beth", "cia", "cien", "clya", "de", "dia", "dda", "dien", "dith", "dia", "lind", "lith", "lia", "lian", "lla", "llan",
						"lle", "ma", "mma", "mwen", "meth", "n", "n", "n", "nna", "ndra", "ng", "ni", "nia", "niel", "rith", "rien", "ria", "ri",
						"rwen", "sa", "sien", "ssa", "ssi", "swen", "thien", "thiel", "viel", "via", "ven", "veth", "wen", "wen", "wen", "wen",
						"wia", "weth", "wien", "wiel"
				}
		});
		syllablesMale.put(Style.ELVES, new String[][] {
				{
						"An", "Am", "Bel", "Cel", "C", "Cal", "Del", "El", "Elr", "Elv", "Eow", "Eär", "F", "G", "Gal", "Gl", "H", "Is", "Leg",
						"Lóm", "M", "N", "P", "R", "S", "T", "Thr", "Tin", "Ur", "Un", "V"
				},
				{
						"a", "á", "adrie", "ara", "e", "é", "ebri", "i", "io", "ithra", "ilma", "il-Ga", "o", "orfi", "ó", "u", "y"
				},
				{
						"l", "las", "lad", "ldor", "ldur", "lith", "mir", "n", "nd", "ndel", "ndil", "ndir", "nduil", "ng", "mbor", "r", "ril",
						"riand", "rion", "wyn"
				}
		});
		syllablesFemale.put(Style.ELVES, new String[][] {
				{
						"An", "Am", "Bel", "Cel", "C", "Cal", "Del", "El", "Elr", "Elv", "Eow", "Eär", "F", "G", "Gal", "Gl", "H", "Is", "Leg",
						"Lóm", "M", "N", "P", "R", "S", "T", "Thr", "Tin", "Ur", "Un", "V"
				},
				{
						"a", "á", "adrie", "ara", "e", "é", "ebri", "i", "io", "ithra", "ilma", "il-Ga", "o", "orfi", "ó", "u", "y"
				},
				{
						"clya", "lindë", "dë", "dien", "dith", "dia", "lith", "lia", "ndra", "ng", "nia", "niel", "rith", "thien", "thiel", "viel",
						"wen", "wien", "wiel"
				}
		});
		/* no female version available */
		syllablesMale.put(Style.DWARF, new String[][] {
				{
						"B", "D", "F", "G", "Gl", "H", "K", "L", "M", "N", "R", "S", "T", "V"
				}, {
						"a", "e", "i", "o", "oi", "u"
				}, {
						"bur", "fur", "gan", "gnus", "gnar", "li", "lin", "lir", "mli", "nar", "nus", "rin", "ran", "sin", "sil", "sur"
				}
		});
		/* no female version available */
		syllablesMale.put(Style.HOBBIT, new String[][] {

				{
						"B", "Dr", "Fr", "Mer", "Per", "R", "S"
				}, {
						"a", "e", "i", "ia", "o", "oi", "u"
				}, {
						"bo", "do", "doc", "go", "grin", "m", "ppi", "rry"
				}
		});
		/* no female version available */
		syllablesMale.put(Style.ORC, new String[][] {
				{
						"B", "Er", "G", "Gr", "H", "P", "Pr", "R", "V", "Vr"
				},
				{
						"a", "i", "o", "u"
				},
				{
						"dash", "dish", "dush", "gar", "gor", "gdush", "lo", "gdish", "k", "lg", "nak", "rag", "rbag", "rg", "rk", "ng", "nk", "rt",
						"ol", "urk", "shnak"
				}
		});

	}

	private Name() {
	}

	/**
	 * Returns a styled name. 
	 * 
	 * @param style the 
	 * @param male whether the name should be male or female
	 * @return styled name, not {@code null}
	 */
	public static String getName(Style style, boolean male) {
		StringBuilder sb = new StringBuilder();
		String[][] syllable = syllablesMale.get(style);
		if (! male && syllablesFemale.containsKey(style)) {
			syllable = syllablesFemale.get(style);	
		}
		for (int i = 0; i < 3; i++) {
			int ix = (int) (rand.nextDouble() * syllable[i].length);
			String str = syllable[i][ix];
			log.debug("name generator: "+i+"/"+ix+"="+str);
			sb.append(str);
		}
		return sb.toString();
	}
}