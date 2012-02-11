/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.rmoffice.generator;

import org.apache.commons.lang.math.RandomUtils;



/**
 * 
 */
public class StatGainGenerator {
	
	/**
	 * Role the dices and returns the potential stat.
	 * 
	 * @param tempStat the temp stat
	 * @return the random potential stat
	 */
	public static int getStatPotDice(int tempStat) {
		if (tempStat <= 24) {
			return 20 + DiceUtils.roll(8, 10);
		} else if (tempStat <= 34) {
			return 30 + DiceUtils.roll(7, 10);
		} else if (tempStat <= 44) {
			return 40 + DiceUtils.roll(6, 10);
		} else if (tempStat <= 54) {
			return 50 + DiceUtils.roll(5, 10);
		} else if (tempStat <= 64) {
			return 60 + DiceUtils.roll(4, 10);
		} else if (tempStat <= 74) {
			/* "70 + 3d10 *"; */
			int r = 70 + DiceUtils.roll(3, 10);
			if (r < tempStat) {
				r = tempStat;
			}
			return r;
		} else if (tempStat <= 84) {
			/* "80 + 2d10 *"; */
			int r = 80 + DiceUtils.roll(2, 10);
			if (r < tempStat) {
				r = tempStat;
			}
			return r;
		} else if (tempStat <= 91) {
			return 90 + DiceUtils.roll(1, 10);
		} else if (tempStat <= 99) {
			return (tempStat-1) + RandomUtils.nextInt((101-tempStat)) + 1;
		} else if (tempStat == 100) {
			return 99 + RandomUtils.nextInt(2) + 1;
		}
		return tempStat;
	}
	
	/**
	 * Returns a string representation of the dices to role for stat potential.
	 * 
	 * @param tempStat the temp stat
	 * @return the dice string
	 */
	public static String getStatPotDiceString(int tempStat) {
		if (tempStat <= 24) {
			return "20 + 8d10";
		} else if (tempStat <= 34) {
			return "30 + 7d10";
		} else if (tempStat <= 44) {
			return "40 + 6d10";
		} else if (tempStat <= 54) {
			return "50 + 5d10";
		} else if (tempStat <= 64) {
			return "60 + 4d10";
		} else if (tempStat <= 74) {
			return "70 + 3d10*";
		} else if (tempStat <= 84) {
			return "80 + 2d10*";
		} else if (tempStat <= 91) {
			return "90 + 1d10";
		} else if (tempStat <= 99) {
			return (tempStat-1)+" + 1d"+(101-tempStat);
		} else if (tempStat == 100) {
			return "99 + 1d2";
		}
		return "?";
	}
	
	/**
	 * Returns the fixe potential stat of the given temp stat.
	 * 
	 * @param tempStat the temp stat
	 * @return the potential
	 */
	public static int getStatPotFix(int tempStat) {
		if (tempStat <= 24) {
			return tempStat + 44;
		} else if (tempStat <= 34) {
			return tempStat + 39;
		} else if (tempStat <= 44) {
			return tempStat + 33;
		} else if (tempStat <= 54) {
			return tempStat + 28;
		} else if (tempStat <= 64) {
			return tempStat + 22;
		} else if (tempStat <= 74) {
			return tempStat + 17;
		} else if (tempStat <= 84) {
			return tempStat + 11;
		} else if (tempStat <= 91) {
			return tempStat + 6;
		} else if (tempStat == 92) {
			return tempStat + 5;
		} else if (tempStat == 93) {
			return tempStat + 4;
		} else if (tempStat == 94) {
			return tempStat + 4;
		} else if (tempStat == 95) {
			return tempStat + 3;
		} else if (tempStat == 96) {
			return tempStat + 3;
		} else if (tempStat == 97) {
			return tempStat + 2;
		} else if (tempStat == 98) {
			return tempStat + 2;
		} else if (tempStat == 99) {
			return tempStat + 1;
		} else if (tempStat == 100) {
			return tempStat + 1;
		}
		return tempStat;
	}

	/**
	 * 
	 * @param tempStat the temp stat
	 * @param potStat the pot stat
	 * @param dice1 the first dice role 
	 * @param dice2 the second dice role
	 * @return the new potential stat 
	 */
	public static int getStatGainDice(int tempStat, int potStat, int dice1, int dice2) {
		int diff = potStat - tempStat;
		int ret;
		if (dice1 == dice2) {
			if (dice1 <= 5) {
				ret = tempStat - dice1;
			} else {
				ret = tempStat + 2 * dice1;
			}
		} else if (diff <= 10) {
			ret = tempStat + Math.min(dice1, dice2);
		} else if (diff <= 20) {
			ret = tempStat + Math.max(dice1, dice2);
		} else {
			ret = tempStat + dice1 + dice2;
		}
		if (ret > potStat) {
			ret = potStat;
		}
		return ret;
	}
}
