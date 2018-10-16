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
package net.sf.rmoffice.ui.editor;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.apache.commons.lang.StringUtils;


/**
 * 
 */
public class NumberDocument extends PlainDocument {
	private static final long serialVersionUID = 1L;
	private static final char[] NUMBERS = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private static final char[] NUMBERS_NEGATIVE = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-'};
	private final boolean negativeAllowed;

	public NumberDocument() {
		this(false);
	}
	
	public NumberDocument(boolean negativeAllowed) {
		this.negativeAllowed = negativeAllowed;
	}
	
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		if (StringUtils.containsOnly(str, getAllowedChars())) {
			super.insertString(offs, str, a);
		}
	}
	
	@Override
	public void replace(int offset, int length, String text, AttributeSet attrs) {
		try {
			if (StringUtils.containsOnly(text, getAllowedChars())) {
				super.replace(offset, length, text, attrs);
			}
		} catch (BadLocationException e) {
		}
	}
	
	private char[] getAllowedChars() {
		if (negativeAllowed) {
			return NUMBERS_NEGATIVE;
		} else {
			return NUMBERS;
		}
	}
}
