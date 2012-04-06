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
package net.sf.rmoffice.meta;

/**
 * Contains information and modifications about the given divine status.
 */
public class DivineStatus {
	private final int level;
	private final int grace;

	public DivineStatus(int gracePoints) {
		this.grace = gracePoints;
		if (grace >= 5000) {
			level = 10;
		} else if (grace >= 1000) {
			level = 9;
		} else if (grace >= 300) {
			level = 8;
		} else if (grace >= 100) {
			level = 7;
		} else if (grace >= 30) {
			level = 6;
		} else if (grace > -5) {
			level = 5;
		} else if (grace > -10) {
			level = 4;
		} else if (grace > -20) {
			level = 3;
		} else if (grace > -30) {
			level = 2;
		} else if (grace > -50) {
			level = 1;
		} else {
			level = 0;
		}
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getIntuitionBonus() {
		switch (level) {
		  case 10: return 8;
		  case 9: return 5;
		  case 8: return 3;
		  case 7: return 2;
		  case 6: return 1;
		  default: return 0;
		}
	}
	
	public int getProtectionAgainstEvil() {
		switch (level) {
		  case 10: return 30;
		  case 9: return 20;
		  case 8: return 15;
		  case 7: return 10;
		  case 6: return 5;
		  default: return 0;
		}
	}
	
	public float getPPRegenerationFactor() {
		switch (level) {
		  case 10: return 3f;
		  case 9: return 2f;
		  case 8: return 1.5f;
		  case 7: return 1.25f;
		  case 6:
		  case 5:
		  case 4:
			  return 1;
		  case 3:
			  return 0.75f;
		  case 2:
			  return 0.5f;
		  case 1:
			  return 0.25f;
		  case 0:
			  return 0;
		  default: return 1;
		}
	}
	
	public int getStaticSpellcastModifier() {
		switch (level) {
		  case 10: return 25;
		  case 9: return 15;
		  case 8: return 10;
		  case 7: return 5;
		  default: 
			  if (grace < 0) {
				  return grace;
			  } else {
				  return 0;
			  }
		}
	}
}
