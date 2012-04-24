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

/**
 * This is one part of a talent or flaw. It is a base interface 
 * for all interactive or non-interactive talent/flaw parts.
 */
public interface ITalentFlawPart {
	
	/**
	 * Returns an identifying string for the part, not {@code null}
	 */
	public String getId();
	
	/**
	 * Adds the talent flaw part to the given {@link TalentFlaw}.
	 * @param owner the UI owner for UI interactions, not {@code null}
	 * 
	 * @param talentFlaw the talent flaw to modify, not {@code null}
	 */
	public void addToTalentFlaw(Frame owner, TalentFlaw talentFlaw);
	
	/**
	 * Returns the part content as text in a human readable and localized way.
	 * 
	 * @return the part content as text, nut {@code null}
	 */
	public String asText();
}
