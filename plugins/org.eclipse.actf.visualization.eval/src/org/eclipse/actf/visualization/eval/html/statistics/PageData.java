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

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.util.xpath.XPathUtil;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.IProblemItemVisitor;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLImageElement;

public class PageData implements IPageStatisticsTag, IProblemItemVisitor {

	private int brokenIntraPageLinkNum = 0;

	private int brokenSkipMainNum = 0;

	private Vector<FlashData> flashV = new Vector<FlashData>();

	private int forwardIntraPageLinkNum = 0;// TODO check if the link reduce
	// reaching time?

	private Vector<HeadingsData> headingsV = new Vector<HeadingsData>();

	private Vector<ImageStatData> imageDataV = new Vector<ImageStatData>();

	private Map<HTMLImageElement, ImageStatData> imageDataMap = new HashMap<HTMLImageElement, ImageStatData>();

	private Map<Element, ImageStatData> linkImageDataMap = new HashMap<Element, ImageStatData>();

	private int imageAltErrorNum = 0;

	private double invalidLinkRatio = 0;

	private int maxTime = 0;

	private int orgMaxTime = 0;

	// category(missing/wrong/...)
	private int missingAltNum = 0;

	private int skipMainNum = 0;

	private int totalImageNumber = 0;

	private int totalLinkNum = 0;

	private int wrongAltNum = 0;

	private boolean hasJavascript = false;

	/**
	 * 
	 */
	public PageData() {
	}

	public void addFlashData(FlashData flashData) {
		flashV.add(flashData);
	}

	public int getBrokenIntraPageLinkNum() {
		return brokenIntraPageLinkNum;
	}

	public int getBrokenSkipMainNum() {
		return brokenSkipMainNum;
	}

	public Vector<FlashData> getFlashData() {
		return this.flashV;
	}

	public int getForwardIntraPageLinkNum() {
		return forwardIntraPageLinkNum;
	}

	public int getHeadingCount() {
		return headingsV.size();
	}

	public Vector<HeadingsData> getHeadingsData() {
		return headingsV;
	}

	public int getImageAltErrorNum() {
		return imageAltErrorNum;
	}

	public double getInvalidLinkRatio() {
		return invalidLinkRatio;
	}

	public int getMaxTime() {
		return maxTime;
	}

	public int getMissingAltNum() {
		return missingAltNum;
	}

	public String getReportFragment() {
		StringBuffer tmpSB = new StringBuffer();
		tmpSB.append("<" + IMAGES + " " + TOTAL + "=\"" + totalImageNumber
				+ "\" " + ERROR + "=\"" + imageAltErrorNum + "\" " + MISSING
				+ "=\"" + missingAltNum + "\" " + WRONG + "=\"" + wrongAltNum
				+ "\" " + ">" + FileUtils.LINE_SEP);
		for (ImageStatData imageData : imageDataV) {
			tmpSB.append(imageData.getItemXML() + FileUtils.LINE_SEP);
		}
		tmpSB.append("</" + IMAGES + ">");

		tmpSB.append("<" + SKIPMAIN + " " + VALID + "=\"" + skipMainNum + "\" "
				+ ERROR + "=\"" + brokenSkipMainNum + "\" />"
				+ FileUtils.LINE_SEP);
		tmpSB.append("<" + REACHINGTIME + " " + MAX + "=\"" + maxTime + "\" "
				+ ORG_MAX + "=\"" + orgMaxTime + "\" />");

		if (flashV.size() > 0) {
			tmpSB.append("<" + FLASH_INFO + " " + TOTAL + "=\"" + flashV.size()
					+ "\">" + FileUtils.LINE_SEP);
			for (FlashData flashData : flashV) {
				tmpSB.append(flashData.getItemXML() + FileUtils.LINE_SEP);
			}
			tmpSB.append("</" + FLASH_INFO + ">" + FileUtils.LINE_SEP);
		}
		tmpSB.append("<" + HEADINGS + " " + TOTAL + "=\"" + headingsV.size()
				+ "\">" + FileUtils.LINE_SEP);
		for (HeadingsData headingsData : headingsV) {
			tmpSB.append(headingsData.getItemXML() + FileUtils.LINE_SEP);
		}
		tmpSB.append("</" + HEADINGS + ">" + FileUtils.LINE_SEP);
		tmpSB.append("<" + JAVASCRIPT + " " + getAttr(EXISTENCE, hasJavascript)
				+ "/>");
		return (tmpSB.toString());
	}

	private String getAttr(String name, boolean value) {
		return (getAttr(name, Boolean.toString(value)));
	}

	private String getAttr(String name, String value) {
		return ((name + "=\"" + XPathUtil.canonicalize(value) + "\" "));
	}

	public int getSkipMainNum() {
		return skipMainNum;
	}

	public int getTotalImageNumber() {
		return totalImageNumber;
	}

	public int getTotalLinkNum() {
		return totalLinkNum;
	}

	public Vector<ImageStatData> getImageData() {
		return imageDataV;
	}

	public void setImageData(Vector<ImageStatData> imageDataV) {
		this.imageDataV = imageDataV;
	}

	public int getWrongAltNum() {
		return wrongAltNum;
	}

	public int getOrgMaxTime() {
		return orgMaxTime;
	}

	public Map<HTMLImageElement, ImageStatData> getImageDataMap() {
		return imageDataMap;
	}

	public Map<Element, ImageStatData> getLinkImageDataMap() {
		return linkImageDataMap;
	}

	public boolean isHasJavascript() {
		return hasJavascript;
	}

	public void setHasJavascript(boolean hasJavascript) {
		this.hasJavascript = hasJavascript;
	}

	public void setLinkImageDataMap(Map<Element, ImageStatData> linkImageDataMap) {
		this.linkImageDataMap = linkImageDataMap;
	}

	public void setImageDataMap(
			Map<HTMLImageElement, ImageStatData> imageDataMap) {
		this.imageDataMap = imageDataMap;
	}

	public void setOrgMaxTime(int orgMaxTime) {
		this.orgMaxTime = orgMaxTime;
	}

	public void setBrokenIntraPageLinkNum(int brokenIntraPageLinkNum) {
		this.brokenIntraPageLinkNum = brokenIntraPageLinkNum;
	}

	public void setBrokenSkipMainNum(int brokenSkipMainNum) {
		this.brokenSkipMainNum = brokenSkipMainNum;
	}

	public void setFlashData(Vector<FlashData> flashV) {
		this.flashV = flashV;
	}

	public void setForwardIntraPageLinkNum(int forwardIntraPageLinkNum) {
		this.forwardIntraPageLinkNum = forwardIntraPageLinkNum;
	}

	public void setHeadingsData(Vector<HeadingsData> headings) {
		this.headingsV = headings;
	}

	public void setImageAltErrorNum(int imageAltErrorNum) {
		this.imageAltErrorNum = imageAltErrorNum;
	}

	public void setInvalidLinkRatio(double invalidLinkRatio) {
		this.invalidLinkRatio = invalidLinkRatio;
	}

	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}

	public void setMissingAltNum(int missingAltNum) {
		this.missingAltNum = missingAltNum;
	}

	public void setSkipMainNum(int skipMainNum) {
		this.skipMainNum = skipMainNum;
	}

	public void setTotalImageNumber(int totalImageNumber) {
		this.totalImageNumber = totalImageNumber;
	}

	public void setTotalLinkNum(int totalLinkNum) {
		this.totalLinkNum = totalLinkNum;
	}

	public void setWrongAltNum(int wrongAltNum) {
		this.wrongAltNum = wrongAltNum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.actf.visualization.eval.problem.IProblemItemVisitor#visit(org.eclipse.actf.visualization.eval.problem.IProblemItem)
	 */
	public void visit(IProblemItem item) {
		ImageStatData imageData = (ImageStatData) imageDataMap.get(item
				.getTargetNode());
		if (imageData != null) {
			imageData.addProblemItem(item);
		} else {
			imageData = (ImageStatData) linkImageDataMap.get(item
					.getTargetNode());
			if (imageData != null) {// TODO check
				imageData.addProblemItem(item);
			}
		}
	}

}
