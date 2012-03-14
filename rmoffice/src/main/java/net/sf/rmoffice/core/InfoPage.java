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


/**
 * 
 */
public class InfoPage extends AbstractPropertyChangeSupport {
	public static final String PROP_TITLE = "title";
	public static final String PROP_CONTENT = "content";
	private String title;
	private String content;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		String oldValue = this.title;
		this.title = title;
		firePropertyChange(PROP_TITLE, oldValue, this.title);
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		String oldValue = this.content;
		this.content = content;
		firePropertyChange(PROP_CONTENT, oldValue, this.content);
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return title;
	}
}
