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
 * 
 */
public class Progression implements IProgression {
	private final float[] bonus;
	
	public Progression(float bonus1, float bonus2, float bonus3, float bonus4, float bonus5) {
		this.bonus = new float[] {bonus1, bonus2, bonus3, bonus4, bonus5};
	}
	
	/** {@inheritDoc} */
	@Override
	public int getBonus(int rangCount) {
		if (rangCount == 0) return Math.round( bonus[0] );
		float b = 0;
		if (rangCount > 30) {
			b += (rangCount - 30f) * bonus[4];
			rangCount = 30;
		}
		if (rangCount > 20) {
			b += (rangCount - 20f) * bonus[3];
			rangCount = 20;
		}
		if (rangCount > 10) {
			b += (rangCount - 10f) * bonus[2];
			rangCount = 10;
		}
		if (rangCount > 0) {
			b += rangCount * bonus[1];
		}
		return Math.round( b );
	}

	@Override
	public float getDigit(int step) {
		return bonus[step];
		
	}

	@Override
	public int compareTo(IProgression progr) {
		float f1 = 0;
		float f2 = 0;
		for (int i=0; i<bonus.length; i++) {
			f1 += getDigit(i);
			f2 += progr.getDigit(i);
		}
		return Float.valueOf(f1).compareTo(Float.valueOf(f2));
	}
	
	@Override
	public String getFormattedString() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<5; i++) {
			if (i > 0) sb.append("*");
			sb.append(""+(int)getDigit(i));
		}
		return sb.toString();
	}

	@Override
	public IProgression modify(IProgression progression) {
		float[] newBonus = new float[bonus.length];
		for (int step = 0; step < bonus.length; step++) {
			newBonus[step] = bonus[step] + progression.getDigit(step);
		}
		return new Progression(newBonus[0], newBonus[1], newBonus[2], newBonus[3], newBonus[4]);
	}
}
