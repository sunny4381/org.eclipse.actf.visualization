/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.html.statistics;

import org.eclipse.actf.util.xpath.XPathCreator;
import org.eclipse.actf.visualization.internal.eval.XMLStringUtil;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;

public class HeadingsData implements IPageStatisticsTag {

	protected String tagName;

	protected String text;

	protected String xpath;

	// TODO orgTime/curTime?

	/**
	 * @param target
	 *            Element
	 * @param text
	 */
	public HeadingsData(Element targetE, String text) {
		super();
		this.tagName = targetE.getTagName();
		this.xpath = XPathCreator.childPathSequence(targetE);
		this.text = text;
	}

	/**
	 * @param tagName
	 * @param text
	 * @param xpath
	 */
	public HeadingsData(String tagName, String text, String xpath) {
		super();
		this.tagName = tagName;
		this.text = text;
		this.xpath = xpath;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getTagName() {
		return tagName;
	}

	public String getText() {
		return text;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public void setText(String text) {
		this.text = text;
	}

	private String getAttr(String name, String value) {
		return ((name + "=\"" + XMLStringUtil.canonicalize(value) + "\" "));
	}

	public String getItemXML() {
		return ("<" + HEADING + " " + getAttr(NAME, tagName)
				+ getAttr(VALUE, text) + getAttr(XPATH, xpath) + " />");
	}

	public static HeadingsData parseItem(Attributes atts)
			throws StatisticsDataFormatException {
		String tmpName = atts.getValue(NAME);
		String tmpValue = atts.getValue(VALUE);
		String tmpXPath = atts.getValue(XPATH);
		if (tmpName != null && tmpValue != null) {
			return (new HeadingsData(tmpName, tmpValue, tmpXPath));
		}
		throw (new StatisticsDataFormatException(
				"Invalid item format: HeadingsData"));
	}
}
