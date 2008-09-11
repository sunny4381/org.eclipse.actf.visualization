/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Masahide WASHIZAWA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.voicebrowser;

import java.util.ArrayList;

public class Message {

	private String tagName;
	private ArrayList conditions = new ArrayList();
	private String tagType;
	private String tagState;
	private ArrayList messages = new ArrayList();

	public Message(
		String curTagName,
		ArrayList curConditions,
		String curTagType,
		String curTagState,
		ArrayList curMessages) {
		tagName = curTagName;
		conditions = curConditions;
		tagType = curTagType;
		tagState = curTagState;
		messages = curMessages;
	}
	/**
	 * Returns the tagName.
	 * @return String
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * Returns the tagState.
	 * @return String
	 */
	public String getTagState() {
		return tagState;
	}

	/**
	 * Returns the tagType.
	 * @return String
	 */
	public String getTagType() {
		return tagType;
	}

	/**
	 * Sets the tagName.
	 * @param tagName The tagName to set
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	/**
	 * Sets the tagState.
	 * @param tagState The tagState to set
	 */
	public void setTagState(String tagState) {
		this.tagState = tagState;
	}

	/**
	 * Sets the tagType.
	 * @param tagType The tagType to set
	 */
	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	/**
	 * Returns the messages.
	 * @return ArrayList
	 */
	public ArrayList getMessages() {
		return messages;
	}

	/**
	 * Sets the messages.
	 * @param messages The messages to set
	 */
	public void setMessages(ArrayList messages) {
		this.messages = messages;
	}

	/**
	 * Returns the conditions.
	 * @return ArrayList
	 */
	public ArrayList getConditions() {
		return conditions;
	}

	/**
	 * Sets the conditions.
	 * @param conditions The conditions to set
	 */
	public void setConditions(ArrayList conditions) {
		this.conditions = conditions;
	}

}
