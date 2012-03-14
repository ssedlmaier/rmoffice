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

import net.sf.rmoffice.meta.enums.ToDoType;


/**
 * 
 */
public class ToDo {	
	private final String message;
	private final ToDoType type;
	
	/* for Java 7 xStream deserializing bug */
	/* package private */ ToDo() {
		message = "";
		type = ToDoType.SYSTEM;
	}
	
	/**
	 * @param message
	 * @param type
	 */
	/* package private */ ToDo(String message, ToDoType type) {
		this.message = message;
		this.type = type;
	}
	
	/**
	 * @param message
	 */
	public ToDo(String message) {
		this.message = message;
		this.type = ToDoType.USER;
	}

	public String getMessage() {
		return message;
	}
	
	public ToDoType getType() {
		return type;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "[" +type.name() + "] " + message;
	}
}
