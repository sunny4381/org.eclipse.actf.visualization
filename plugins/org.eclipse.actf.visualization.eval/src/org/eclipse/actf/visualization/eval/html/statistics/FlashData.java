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

import org.eclipse.actf.util.xpath.XPathUtil;

public class FlashData implements IPageStatisticsTag, IFlashTag {

	private String src = "";// movie

	// private String codebase="";
	// private String id="";

	private String width = "";

	private String height = "";

	private String quality = "";

	private String bgcolor = "";

	private String align = "";

	private String salign = "";

	private String wmode = "";

	private String base = "";

	private String menu = "";

	private String loop = "";

	private String play = "";

	private String swliveconnect = "";

	private boolean isObject = false;

	private boolean withEmbed = false;

	/**
	 * 
	 */
	public FlashData(String src, boolean isObject) {
		this.src = src;
		this.isObject = isObject;
	}

	public String getAlign() {
		return align;
	}

	public String getBase() {
		return base;
	}

	public String getBgcolor() {
		return bgcolor;
	}

	public String getHeight() {
		return height;
	}

	public String getLoop() {
		return loop;
	}

	public String getMenu() {
		return menu;
	}

	public String getPlay() {
		return play;
	}

	public String getQuality() {
		return quality;
	}

	public String getSalign() {
		return salign;
	}

	public String getSrc() {
		return src;
	}

	public String getSwliveconnect() {
		return swliveconnect;
	}

	public String getWidth() {
		return width;
	}

	public String getWmode() {
		return wmode;
	}

	public boolean isWithEmbed() {
		return withEmbed;
	}

	public void setWithEmbed(boolean withEmbed) {
		this.withEmbed = withEmbed;
	}

	public boolean isObject() {
		return isObject;
	}

	public void setObject(boolean isObject) {
		this.isObject = isObject;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setLoop(String loop) {
		this.loop = loop;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public void setPlay(String play) {
		this.play = play;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public void setSalign(String salign) {
		this.salign = salign;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public void setSwliveconnect(String swliveconnect) {
		this.swliveconnect = swliveconnect;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setWmode(String wmode) {
		this.wmode = wmode;
	}

	public String getItemXML() {
		StringBuffer tmpSB = new StringBuffer("<" + FLASH + " " + SRC + "=\""
				+ XPathUtil.canonicalize(src) + "\" " + FLASH_IS_OBJECT + "=\""
				+ isObject + "\" ");
		if (isObject && withEmbed) {
			tmpSB.append(FLASH_WITHEMBED + "=\"true\" ");
		}
		// TODO
		tmpSB.append("/>");
		return (tmpSB.toString());
	}

}
