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

package org.eclipse.actf.visualization.eval.html;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.eclipse.actf.util.xpath.XPathUtil;
import org.eclipse.actf.visualization.eval.EvaluationPreferencesUtil;
import org.eclipse.actf.visualization.eval.html.statistics.FlashData;
import org.eclipse.actf.visualization.eval.html.statistics.HeadingsData;
import org.eclipse.actf.visualization.eval.html.statistics.IPageStatisticsTag;
import org.eclipse.actf.visualization.eval.html.statistics.ImageStatData;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLImageElement;

public class HtmlEvalUtil extends HtmlTagUtil {

	private static final int LONG_TEXT_NUM = 200; // TODO check

	private static final String[] HEADING_LEVEL = {
			"h1", "h2", "h3", "h4", "h5", "h6" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	public static final String[] EVENT_MOUSE_BUTTON = { ATTR_ONCLICK,
			ATTR_ONDBLCLICK, ATTR_ONMOUSEUP, ATTR_ONMOUSEDOWN };

	public static final String[] EVENT_MOUSE_FOCUS = { ATTR_ONMOUSEOVER,
			ATTR_ONMOUSEOUT, ATTR_ONMOUSEMOVE };

	public static final String[] EVENT_ON_KEY = { ATTR_ONKEYDOWN,
			ATTR_ONKEYPRESS, ATTR_ONKEYUP };

	public static final String[] EVENT_LOAD = { ATTR_ONLOAD, ATTR_ONUNLOAD,
			ATTR_ONABORT, ATTR_ONERROR };

	public static final String[] EVENT_WINDOW = { ATTR_ONRESIZE, ATTR_ONMOVE,
			ATTR_ONDRAGDROP };

	public static final String[] EVENT_FOCUS = { ATTR_ONFOCUS, ATTR_ONBLUR,
			ATTR_ONSELECT };

	private Document target;

	private Document resultDoc;

	private Document origDom;

	private Document ieDom;

	private URL baseUrl;

	private Map document2IdMap;

	private boolean isDBCS;

	private boolean isIEDom;

	private boolean hasAwithHref = false;

	private boolean hasJavascript = false;

	private Element[] aWithHref_elements;

	private String[] aWithHref_hrefs;

	private String[] aWithHref_strings;

	private HTMLImageElement[] img_elements;

	private Element[] table_elements;

	private Element[] body_elements;

	private Element[] frame_elements;

	private Element[] iframe_elements;

	private Element[] object_elements;

	private Element[] parent_table_elements;

	private Element[] bottom_data_tables;

	private Element[] bottom_1row1col_tables;

	private Element[] bottom_notdata_tables;

	private Element[] headings;

	private Element[] embed_elements;

	private Element[] script_elements;

	private Element[] javascript_elements;

	private Element[] eventMouseButtonElements; // on Click/Dblclick, onMouse

	// up/down

	private Element[] eventMouseFocusElements; // onMouse over/out/move

	private Element[] eventOnKeyElements; // onKey Down/up/press

	private Element[] eventLoadElements; // on load/unload/abort/error

	private Element[] eventWindowElements; // on Resize/Mode/DragDrop

	private Element[] eventFocusElements; // on Focus/Blur/Select

	private Element[] javascriptHref_elements;

	private String[] javascriptHref_hrefs;

	private String[] javascriptHref_strings;

	private Set blockEleSet;

	private String curUrl;

	private double invalidLinkRatio;

	private PageData pageData;

	private int invisibleElementCount = 0;

	private String[] invisibleLinkStrings = new String[0];

	private HashSet<String> notExistHrefSet = new HashSet<String>();

	public HtmlEvalUtil(Document target, Document resultDoc, String curUrl,
			Map document2IdMap, Document origDom, Document ieDom,
			PageData pageData, boolean isDBCS, boolean isIEDom) {
		this(target, resultDoc, curUrl, document2IdMap, origDom, ieDom,
				pageData, 0, null, isDBCS, isIEDom);
	}

	/**
	 * 
	 */
	public HtmlEvalUtil(Document target, Document resultDoc, String curUrl,
			Map document2IdMap, Document origDom, Document ieDom,
			PageData pageData, int invisibleElementCount,
			String[] invisibleLinkStrings, boolean isDBCS, boolean isIEDom) {
		this.target = target;
		this.resultDoc = resultDoc;

		this.origDom = origDom;
		this.ieDom = ieDom;
		this.isIEDom = isIEDom;

		this.pageData = pageData;

		this.curUrl = curUrl;
		baseUrl = null;
		try {
			baseUrl = new URL(curUrl); // ToDo handle base
		} catch (MalformedURLException e) {
			// e.printStackTrace();
		}

		this.invalidLinkRatio = 0;
		this.invisibleElementCount = invisibleElementCount;
		if (invisibleLinkStrings != null) {
			this.invisibleLinkStrings = invisibleLinkStrings;
		}

		this.document2IdMap = document2IdMap;
		// this.html2ViewMapData = html2ViewMapData;

		this.isDBCS = isDBCS;

		// prepare freq use elements
		// System.out.println(df.format(new Date(System.currentTimeMillis()))
		// + ": checker engine init");

		NodeList tmpNL = XPathUtil.evalXPathNodeList(target, ".//" + "a"
				+ XPathUtil.predAttExists(ATTR_HREF));
		int length = tmpNL.getLength();

		if (length > 0) {
			hasAwithHref = true;
		}

		aWithHref_elements = new Element[length];
		aWithHref_hrefs = new String[length];
		aWithHref_strings = new String[length];

		for (int i = 0; i < length; i++) {
			Element tmpE = (Element) tmpNL.item(i);
			aWithHref_elements[i] = tmpE;
			aWithHref_hrefs[i] = tmpE.getAttribute(ATTR_HREF);
			aWithHref_strings[i] = getTextAltDescendant(tmpE);
		}

		// System.out.println(df.format(new Date(System.currentTimeMillis()))
		// + ": href fin");

		tmpNL = target.getElementsByTagName("img"); //$NON-NLS-1$
		length = tmpNL.getLength();
		img_elements = new HTMLImageElement[length];
		Vector<IPageStatisticsTag> tmpV = new Vector<IPageStatisticsTag>();
		HashMap<HTMLImageElement, ImageStatData> tmpMap = new HashMap<HTMLImageElement, ImageStatData>();
		HashMap<Element, ImageStatData> linkImgMap = new HashMap<Element, ImageStatData>();
		for (int i = 0; i < length; i++) {
			img_elements[i] = (HTMLImageElement) tmpNL.item(i);
			ImageStatData isd = new ImageStatData(img_elements[i], baseUrl);
			tmpV.add(isd);
			tmpMap.put(img_elements[i], isd);
			if (isd.getAncestorLink() != null) {
				linkImgMap.put(isd.getAncestorLink(), isd);
			}
		}
		pageData.setImageDataV(tmpV);
		pageData.setImageDataMap(tmpMap);
		pageData.setLinkImageDataMap(linkImgMap);

		// TODO use XPath
		tmpNL = target.getElementsByTagName("table"); //$NON-NLS-1$
		length = tmpNL.getLength();
		table_elements = new Element[length];
		Vector<Element> bottomV = new Vector<Element>();
		Vector<Element> parentV = new Vector<Element>();
		Vector<Element> b1row1colV = new Vector<Element>();
		Vector<Element> bNotDataV = new Vector<Element>();
		for (int i = 0; i < length; i++) {
			table_elements[i] = (Element) tmpNL.item(i);
			if (table_elements[i].getElementsByTagName("table").getLength() //$NON-NLS-1$
			== 0) {
				if (is1Row1ColTable(table_elements[i])) {
					b1row1colV.add(table_elements[i]);
				} else if (isDataTable(table_elements[i])) {
					bottomV.add(table_elements[i]);
				} else {
					bNotDataV.add(table_elements[i]);
				}
			} else {
				parentV.add(table_elements[i]);
			}
		}
		bottom_data_tables = new Element[bottomV.size()];
		bottom_1row1col_tables = new Element[b1row1colV.size()];
		bottom_notdata_tables = new Element[bNotDataV.size()];
		parent_table_elements = new Element[parentV.size()];
		bottomV.toArray(bottom_data_tables);
		b1row1colV.toArray(bottom_1row1col_tables);
		bNotDataV.toArray(bottom_notdata_tables);
		parentV.toArray(parent_table_elements);

		body_elements = getElementsArray(target, "body");
		frame_elements = getElementsArray(target, "frame");
		iframe_elements = getElementsArray(target, "iframe");

		HashSet<Element> embedInObjectSet = new HashSet<Element>();

		// TODO ieDOM
		tmpNL = target.getElementsByTagName("object"); //$NON-NLS-1$
		length = tmpNL.getLength();
		object_elements = new Element[length];
		for (int i = 0; i < length; i++) {
			object_elements[i] = (Element) tmpNL.item(i);
			if (FLASH_OBJECT.equalsIgnoreCase(object_elements[i]
					.getAttribute("classid"))) {
				// TODO check codebase
				// TODO get width hight align ... loop quality...
				NodeList paramNL = object_elements[i]
						.getElementsByTagName("param");
				String src = "";
				for (int j = 0; j < paramNL.getLength(); j++) {
					try {
						Element tmpE = (Element) paramNL.item(j);
						String name = tmpE.getAttribute("name");
						String value = tmpE.getAttribute("value");
						if (name.equalsIgnoreCase("movie")) {
							src = value;
						}
					} catch (Exception e) {

					}
				}
				if (src != null && src.length() > 0) {
					FlashData flashD = new FlashData(src, true);
					pageData.addFlashData(flashD);

					NodeList embedNL = object_elements[i]
							.getElementsByTagName("embed");
					for (int j = 0; j < embedNL.getLength(); j++) {
						Element tmpE = (Element) embedNL.item(j);
						if (FLASH_TYPE.equalsIgnoreCase(tmpE
								.getAttribute("type"))) {
							// TODO check PLUGINSPAGE
							// TODO get width hight align ... loop quality...

							String src2 = tmpE.getAttribute("src");
							if (src2 != null && src2.length() > 0) {
								if (src.equalsIgnoreCase(src2)) {
									embedInObjectSet.add(tmpE);
									flashD.setWithEmbed(true);
								} else {
									pageData.addFlashData(new FlashData(src,
											false));
								}
							}
						}
					}

				}

			}
		}

		blockEleSet = getBlockElementSet();

		embed_elements = getElementsArray(target, "embed");
		for (int i = 0; i < embed_elements.length; i++) {
			Element tmpE = embed_elements[i];
			if (!embedInObjectSet.contains(tmpE)
					&& FLASH_TYPE.equals(tmpE.getAttribute("type"))) {
				// TODO check PLUGINSPAGE
				// TODO get width hight align ... loop quality...
				String src = tmpE.getAttribute("src");
				if (src != null && src.length() > 0) {
					pageData.addFlashData(new FlashData(src, false));
				}
			}
		}

		NodeList headingsNL = XPathUtil.evalXPathNodeList(target,
				"//h1|//h2|//h3|//h4|//h5|//h6");
		length = headingsNL.getLength();
		tmpV = new Vector<IPageStatisticsTag>();
		headings = new Element[headingsNL.getLength()];
		for (int i = 0; i < length; i++) {
			Element tmpE = (Element) headingsNL.item(i);
			headings[i] = tmpE;
			tmpV.add(new HeadingsData(tmpE, getTextAltDescendant(tmpE)));
		}
		pageData.setHeadingsV(tmpV);

		collectScriptElements();
		calcDomDifference();
	}

	private Element[] getElementsArray(Document target, String tagName) {
		NodeList tmpNL = target.getElementsByTagName(tagName);
		int length = tmpNL.getLength();
		Element[] result = new Element[length];
		for (int i = 0; i < length; i++) {
			result[i] = (Element) tmpNL.item(i);
		}
		return (result);
	}

	private Element[] getElementsArrayByXPath(Document target, String xpath) {
		NodeList tmpNL = XPathUtil.evalXPathNodeList(target, xpath);
		int length = tmpNL.getLength();
		Element[] result = new Element[length];
		for (int i = 0; i < length; i++) {
			Element tmpE = (Element) tmpNL.item(i);
			result[i] = tmpE;
		}
		return result;
	}

	private void collectScriptElements() {
		script_elements = getElementsArray(target, "script");

		javascript_elements = getElementsArrayByXPath(target,
				"//script[@type=\"text/javascript\"]");

		// allEventElements = getElementsArrayByXPath(
		// target,
		// "//*[@onclick or @ondblclick or @onmouseup or @onmousedown or
		// @onmouseover or @onmouseout or @onmousemove or
		// @onkeydown or @onkeyup or @onkeypress or @onload or @onunload or
		// @onabort or @onerror or @onresize or @onmove
		// or @ondragdrop or @onfocus or @onblur or @onselect]");

		eventMouseButtonElements = getElementsArrayByXPath(target,
				"//*[@onclick or @ondblclick or @onmouseup or @onmousedown]");
		eventMouseFocusElements = getElementsArrayByXPath(target,
				"//*[@onmouseover or @onmouseout or @onmousemove]");
		eventOnKeyElements = getElementsArrayByXPath(target,
				"//*[@onkeydown or @onkeyup or @onkeypress]");
		eventLoadElements = getElementsArrayByXPath(target,
				"//*[@onload or @onunload or @onabort or @onerror]");
		eventWindowElements = getElementsArrayByXPath(target,
				"//*[@onresize or @onmove or @ondragdrop]");
		eventFocusElements = getElementsArrayByXPath(target,
				"//*[@onfocus or @onblur or @onselect]");

		Vector<Element> tmpV1 = new Vector<Element>();
		Vector<String> tmpV2 = new Vector<String>();
		Vector<String> tmpV3 = new Vector<String>();
		for (int i = 0; i < aWithHref_hrefs.length; i++) {
			if (aWithHref_hrefs[i].startsWith("javascript:")) {
				tmpV1.add(aWithHref_elements[i]);
				tmpV2.add(aWithHref_hrefs[i]);
				tmpV3.add(aWithHref_strings[i]);
			}
		}

		int size = tmpV1.size();
		javascriptHref_elements = new Element[size];
		javascriptHref_hrefs = new String[size];
		javascriptHref_strings = new String[size];
		tmpV1.toArray(javascriptHref_elements);
		tmpV2.toArray(javascriptHref_hrefs);
		tmpV3.toArray(javascriptHref_strings);

		int javascriptNum = javascript_elements.length
				+ eventFocusElements.length + eventLoadElements.length
				+ eventMouseButtonElements.length
				+ eventMouseFocusElements.length + eventOnKeyElements.length
				+ eventWindowElements.length + javascriptHref_hrefs.length;

		hasJavascript = (javascriptNum > 0);
		pageData.setHasJavascript(hasJavascript);
	}

	private void calcDomDifference() {

		if (EvaluationPreferencesUtil.isOriginalDOM()) {
			// target = orig DOM
			if (isIEDom||null==ieDom) {
				// parse error
				return;
			}

			TreeSet<String> existSet = new TreeSet<String>(Arrays
					.asList(aWithHref_hrefs));
			// trim()?

			NodeList ieNL = XPathUtil.evalXPathNodeList(ieDom, ".//" + "a"
					+ XPathUtil.predAttExists(ATTR_HREF));
			int size = ieNL.getLength();
			for (int i = 0; i < size; i++) {
				Element tmpE = (Element) ieNL.item(i);
				String tmpS = tmpE.getAttribute(ATTR_HREF);
				if (!existSet.contains(tmpS)) {
					notExistHrefSet.add(tmpS);
				}
			}
		} else {
			// target = IE DOM
			NodeList orgNL = XPathUtil.evalXPathNodeList(origDom, ".//" + "a"
					+ XPathUtil.predAttExists(ATTR_HREF));
			int size = orgNL.getLength();
			TreeSet<String> existSet = new TreeSet<String>();
			for (int i = 0; i < size; i++) {
				existSet.add(((Element) orgNL.item(i)).getAttribute(ATTR_HREF));
			}

			size = aWithHref_hrefs.length;
			for (int i = 0; i < size; i++) {
				if (!existSet.contains(aWithHref_hrefs[i])) {
					notExistHrefSet.add(aWithHref_hrefs[i]);
				}
			}

		}

	}

	private boolean is1Row1ColTable(Element el) {
		NodeList cellNl = el.getElementsByTagName("tr"); //$NON-NLS-1$
		if (cellNl.getLength() <= 1) {
			return true;
		} else {
			boolean bMultiCol = false;
			int length = cellNl.getLength();
			for (int i = 0; i < length; i++) {
				NodeList thNl = ((Element) cellNl.item(i))
						.getElementsByTagName("th"); //$NON-NLS-1$
				NodeList tdNl = ((Element) cellNl.item(i))
						.getElementsByTagName("td"); //$NON-NLS-1$
				if ((thNl.getLength() + tdNl.getLength()) > 1) {
					bMultiCol = true;
					break;
				}
			}
			if (!bMultiCol) {
				return true;
			}
		}
		return false;
	}

	private boolean isDataTable(Element el) {
		if (hasFormControl(el)) {
			return false;
		}

		NodeList cellNl = el.getElementsByTagName("td"); //$NON-NLS-1$
		if (cellNl.getLength() == 0) {
			return false;
		} else {
			int length = cellNl.getLength();
			for (int j = 0; j < length; j++) {
				if (!isDataCell((Element) cellNl.item(j))) {
					return false;
				}
			}

			cellNl = el.getElementsByTagName("th"); //$NON-NLS-1$
			length = cellNl.getLength();
			for (int j = 0; j < length; j++) {
				if (!isDataCell((Element) cellNl.item(j))) {
					return false;
				}
			} // image?
		}

		return true;
	}

	private boolean hasFormControl(Element formEl) {
		NodeList nl = formEl.getElementsByTagName("form"); //$NON-NLS-1$
		if (nl.getLength() > 0)
			return true;
		nl = formEl.getElementsByTagName("input"); //$NON-NLS-1$
		if (nl.getLength() > 0)
			return true;
		nl = formEl.getElementsByTagName("select"); //$NON-NLS-1$
		if (nl.getLength() > 0)
			return true;
		nl = formEl.getElementsByTagName("textarea"); //$NON-NLS-1$
		if (nl.getLength() > 0)
			return true;
		nl = formEl.getElementsByTagName("html:text"); //$NON-NLS-1$
		if (nl.getLength() > 0)
			return true;
		nl = formEl.getElementsByTagName("html:radio"); //$NON-NLS-1$
		if (nl.getLength() > 0)
			return true;
		return false;
	}

	private boolean isDataCell(Element el) {
		if (getTextAltDescendant(el).length() > LONG_TEXT_NUM) {
			return false;
		}
		NodeList aNl = el.getElementsByTagName("a"); //$NON-NLS-1$
		NodeList liNl = el.getElementsByTagName("li"); //$NON-NLS-1$
		NodeList imgNl = el.getElementsByTagName("img"); //$NON-NLS-1$
		if ((aNl.getLength() + liNl.getLength() + imgNl.getLength()) > 3) {
			return false;
		}

		return true;
	}

	public int getHeadingLevel(String strNodeName) {
		for (int i = 0; i < HEADING_LEVEL.length; i++) {
			if (strNodeName.equalsIgnoreCase(HEADING_LEVEL[i])) {
				return Integer.valueOf(strNodeName.substring(1)).intValue();
			}
		}
		return 0;
	}

	public Element[] getAWithHref_elements() {
		return aWithHref_elements;
	}

	public String[] getAWithHref_hrefs() {
		return aWithHref_hrefs;
	}

	public String[] getAWithHref_strings() {
		return aWithHref_strings;
	}

	public URL getBaseUrl() {
		return baseUrl;
	}

	public Set getBlockEleSet() {
		return blockEleSet;
	}

	public Element[] getBody_elements() {
		return body_elements;
	}

	public Element[] getBottom_1row1col_tables() {
		return bottom_1row1col_tables;
	}

	public Element[] getBottom_data_tables() {
		return bottom_data_tables;
	}

	public Element[] getBottom_notdata_tables() {
		return bottom_notdata_tables;
	}

	public String getCurUrl() {
		return curUrl;
	}

	public Map getDocument2IdMap() {
		return document2IdMap;
	}

	public Element[] getFrame_elements() {
		return frame_elements;
	}

	public boolean isHasAwithHref() {
		return hasAwithHref;
	}

	public boolean isHasJavascript() {
		return hasJavascript;
	}

	public Element[] getHeadings() {
		return headings;
	}

	public Document getIeDom() {
		return ieDom;
	}

	public Element[] getIframe_elements() {
		return iframe_elements;
	}

	public HTMLImageElement[] getImg_elements() {
		return img_elements;
	}

	public double getInvalidLinkRatio() {
		return invalidLinkRatio;
	}

	public int getInvisibleElementCount() {
		return invisibleElementCount;
	}

	public String[] getInvisibleLinkStrings() {
		return invisibleLinkStrings;
	}

	public boolean isDBCS() {
		return isDBCS;
	}

	public boolean isIEDom() {
		return isIEDom;
	}

	public HashSet getNotExistHrefSet() {
		return notExistHrefSet;
	}

	public Element[] getObject_elements() {
		return object_elements;
	}

	public Document getOrigDom() {
		return origDom;
	}

	public PageData getPageData() {
		return pageData;
	}

	public Element[] getParent_table_elements() {
		return parent_table_elements;
	}

	public Document getResultDoc() {
		return resultDoc;
	}

	public Element[] getTable_elements() {
		return table_elements;
	}

	public Document getTarget() {
		return target;
	}

	public Element[] getEmbed_elements() {
		return embed_elements;
	}

	public Element[] getJavascriptHref_elements() {
		return javascriptHref_elements;
	}

	public String[] getJavascriptHref_hrefs() {
		return javascriptHref_hrefs;
	}

	public String[] getJavascriptHref_strings() {
		return javascriptHref_strings;
	}

	public Element[] getEventLoadElements() {
		return eventLoadElements;
	}

	public Element[] getEventMouseButtonElements() {
		return eventMouseButtonElements;
	}

	public Element[] getEventMouseFocusElements() {
		return eventMouseFocusElements;
	}

	public Element[] getEventOnKeyElements() {
		return eventOnKeyElements;
	}

	public Element[] getScript_elements() {
		return script_elements;
	}

	public Element[] getEventWindowElements() {
		return eventWindowElements;
	}

	public Element[] getEventFocusElements() {
		return eventFocusElements;
	}
}
