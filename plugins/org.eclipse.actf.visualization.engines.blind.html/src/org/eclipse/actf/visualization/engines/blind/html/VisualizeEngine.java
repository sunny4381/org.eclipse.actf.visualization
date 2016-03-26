/*******************************************************************************
 * Copyright (c) 2003, 2016 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hironobu TAKAGI - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.blind.html;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.engines.blind.ParamBlind;
import org.eclipse.actf.visualization.engines.blind.TextCheckResult;
import org.eclipse.actf.visualization.engines.blind.TextChecker;
import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.impl.VisualizeStyleInfo;
import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.impl.VisualizeStyleInfoManager;
import org.eclipse.actf.visualization.engines.blind.html.util.Id2LineViaActfId;
import org.eclipse.actf.visualization.engines.voicebrowser.IPacket;
import org.eclipse.actf.visualization.engines.voicebrowser.IPacketCollection;
import org.eclipse.actf.visualization.engines.voicebrowser.IVoiceBrowserController;
import org.eclipse.actf.visualization.engines.voicebrowser.VoiceBrowserControllerFactory;
import org.eclipse.actf.visualization.eval.EvaluationUtil;
import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.html.HtmlTagUtil;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.internal.engines.blind.html.BlindProblem;
import org.eclipse.actf.visualization.internal.engines.blind.html.util.DocumentCleaner;
import org.eclipse.actf.visualization.internal.engines.blind.html.util.ImgChecker;
import org.eclipse.actf.visualization.internal.engines.blind.html.util.LinkAnalyzer;
import org.eclipse.actf.visualization.internal.engines.blind.html.util.NodeInfoCreator;
import org.eclipse.actf.visualization.internal.engines.blind.html.util.VisualizationNodeInfo;
import org.eclipse.actf.visualization.internal.engines.blind.html.util.VisualizationResultCleaner;
import org.eclipse.actf.visualization.internal.engines.blind.html.util.VisualizeColorUtil;
import org.eclipse.actf.visualization.internal.engines.blind.html.util.VisualizeMapDataImpl;
import org.eclipse.actf.visualization.internal.engines.blind.html.util.VisualizeMapUtil;
import org.eclipse.actf.visualization.internal.engines.blind.html.util.VisualizeViewUtil;
import org.eclipse.actf.visualization.util.html2view.Html2ViewMapData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Blind usability visualization engine.
 */
public class VisualizeEngine {

	public static String ERROR_ICON_NAME = "exclawhite21.gif"; //$NON-NLS-1$

	private static final String errorStyle = "color: #dd0000; background-color: #FFFFFF; border-width: medium; border-style: solid; border-color: #dd0000;"; //$NON-NLS-1$

	private static final String CHECK_ITEM_PATTERN = "B_\\p{Digit}+"; //$NON-NLS-1$

	private static final boolean DEBUG = false;

	private String baseUrl = ""; // default value //$NON-NLS-1$

	private String targetUrl = ""; //$NON-NLS-1$

	private Document orig = null;

	private Document result = null;

	private List<IProblemItem> problems = null;

	private Vector<Html2ViewMapData> html2viewMapV = new Vector<Html2ViewMapData>();

	private VisualizeMapDataImpl mapData = null;

	private VisualizeStyleInfo styleInfo;

	private IVoiceBrowserController jwatc = null;

	private boolean fIsActivating = false;

	private IPacketCollection allPc = null;

	private boolean servletMode = false; // for future use

	private boolean isHTML5 = false;

	private int iMaxTime;

	private int iMaxTimeLeaf;

	private Set<String> invisibleIdSet = new HashSet<String>();

	private TextChecker textChecker;

	private PageData pageData;

	private GuidelineHolder guidelineHolder = GuidelineHolder.getInstance();

	private boolean[] checkItems = new boolean[IBlindProblem.NUM_PROBLEMS];

	private File variantFile;

	// TODO IGuidelineSelectionChangedListner

	/**
	 * Constructor for VisualizeEngine.
	 */
	public VisualizeEngine() {
		result = null;
		jwatInit();

		initCheckItems();

		textChecker = TextChecker.getInstance();
	}

	private void initCheckItems() {
		Arrays.fill(checkItems, false);

		Set<IEvaluationItem> itemSet = guidelineHolder.getMatchedCheckitemSet();

		for (IEvaluationItem cItem : itemSet) {
			// System.out.println(cItem.getId());
			String id = cItem.getId();
			if (id.matches(CHECK_ITEM_PATTERN)) {
				id = id.substring(2);
				try {
					int item = Integer.parseInt(id);
					if (item > -1 && item < IBlindProblem.NUM_PROBLEMS) {
						checkItems[item] = true;
					} else {
						// TODO
					}
				} catch (Exception e) {
				}

			}
		}
	}

	/**
	 * Sets target document.
	 * 
	 * @param document
	 *            target document
	 */
	public void setDocument(Document document) {
		// TODO move to screen reader engine
		DocumentCleaner.removeDisplayNone(document);

		if (DEBUG)
			DebugPrintUtil.debugPrintln("remove display none\t" + (new Date()).getTime());

		orig = document;
		result = (Document) document.cloneNode(true);

		if (DEBUG)
			DebugPrintUtil.debugPrintln("clone node\t" + (new Date()).getTime());

		jwatc.setDocument(result);

		if (DEBUG)
			DebugPrintUtil.debugPrintln("jwatc\t" + (new Date()).getTime());

		pageData = new PageData();
		mapData = new VisualizeMapDataImpl();

		VisualizeMapUtil.createNode2NodeMap(document, result, mapData);

		if (DEBUG)
			DebugPrintUtil.debugPrintln("create node2node map\t" //$NON-NLS-1$
					+ (new Date()).getTime());

	}

	private void cleanupPacketCollection(IPacketCollection pc) {
		// remove text in noscript tag
		if (pc != null) {
			int size = pc.size();
			for (int i = size - 1; i >= 0; i--) {
				IPacket p = pc.get(i);

				Node tmpNode = p.getNode();
				while (tmpNode != null) {
					if (tmpNode.getNodeName().equals("noscript")) { //$NON-NLS-1$
						pc.remove(i);
						break;
					}
					tmpNode = tmpNode.getParentNode();
				}
			}
		}
	}

	private void replaceMathML_SVG_PacketCollection(IPacketCollection pc) {
		// remove text in noscript tag
		if (pc != null) {
			int size = pc.size();
			for (int i = size - 1; i >= 0; i--) {
				IPacket p = pc.get(i);

				Node tmpNode = p.getNode();
				while (tmpNode != null) {
					String name = tmpNode.getNodeName();
					if (name.equals("math") || name.equals("svg")) { //$NON-NLS-1$
						// TODO replace
						pc.remove(i);
						break;
					}
					tmpNode = tmpNode.getParentNode();
				}
			}
		}
	}

	/**
	 * Visualize blind users' usability
	 */
	public void visualize() {
		if (result == null) {
			return;
		} else {
			problems = new Vector<IProblemItem>();
			allPc = jwatc.getPacketCollection();

			DebugPrintUtil.debugPrintln("packet collection\t" //$NON-NLS-1$
					+ (new Date()).getTime());

			cleanupPacketCollection(allPc);

			DebugPrintUtil.debugPrintln("cleanup packet collection\t" //$NON-NLS-1$
					+ (new Date()).getTime());

			ParamBlind curParamBlind = ParamBlind.getInstance();

			// get packet and create map and list
			NodeInfoCreator nodeInfoCreator = new NodeInfoCreator(mapData, textChecker, problems, invisibleIdSet,
					curParamBlind);

			DebugPrintUtil.debugPrintln("Nodeinfo init\t" + (new Date()).getTime());

			nodeInfoCreator.prepareNodeInfo(allPc);

			DebugPrintUtil.debugPrintln("Nodeinfo prep\t" + (new Date()).getTime());

			nodeInfoCreator.createAdditionalNodeInfo(result);

			DebugPrintUtil.debugPrintln("Nodeinfo additional\t" + (new Date()).getTime());

			// link analysis preparation
			LinkAnalyzer linkAnalyzer = new LinkAnalyzer(result, allPc, mapData, problems, invisibleIdSet,
					curParamBlind, pageData);

			DebugPrintUtil.debugPrintln("link analyzer\t" + (new Date()).getTime());

			styleInfo = new VisualizeStyleInfo(orig, mapData);

			DebugPrintUtil.debugPrintln("style info\t" + (new Date()).getTime());

			/*
			 * rewrite DOM from here
			 */
			// insert ID attributes to elements
			mapData.makeIdMapping(Html2ViewMapData.ACTF_ID);

			DebugPrintUtil.debugPrintln("id mapping\t" + (new Date()).getTime());

			styleInfo.setImportedCssSet(DocumentCleaner.removeCSS(result, targetUrl));

			// prepare head
			if (result.getElementsByTagName("head").getLength() == 0) { //$NON-NLS-1$
				Element tmpHead = result.createElement("head"); //$NON-NLS-1$
				Element tmpHtml = result.getDocumentElement();
				if (tmpHtml != null) {
					tmpHtml.insertBefore(tmpHead, tmpHtml.getFirstChild());
				}
			}

			// calculate time and set color
			VisualizeColorUtil colorUtil = new VisualizeColorUtil(result, mapData, curParamBlind, isHTML5);
			colorUtil.setColorAll();

			DebugPrintUtil.debugPrintln("color\t" + (new Date()).getTime()); //$NON-NLS-1$

			calMaxTime();

			DebugPrintUtil.debugPrintln("max time\t" //$NON-NLS-1$
					+ (new Date()).getTime());

			problems.addAll(linkAnalyzer.skipLinkCheck(iMaxTime, iMaxTimeLeaf));

			DebugPrintUtil.debugPrintln("skiplink check\t" //$NON-NLS-1$
					+ (new Date()).getTime());

			replaceImgAndCheck(result, mapData, curParamBlind.oReplaceImage);

			DebugPrintUtil.debugPrintln("image check\t" //$NON-NLS-1$
					+ (new Date()).getTime());

			int errorCount = 0;
			int missing = 0;
			int wrong = 0;

			@SuppressWarnings("unused")
			int area = 0;

			// remove unnecessary problems
			for (Iterator<IProblemItem> i = problems.iterator(); i.hasNext();) {
				IProblemItem tmpBP = i.next();
				if (tmpBP instanceof BlindProblem) {
					BlindProblem curBP = (BlindProblem) tmpBP;
					if (checkItems[curBP.getSubType()]) {
						if (curBP.getSeverity() == IEvaluationItem.SEV_ERROR) {
							switch (curBP.getSubType()) {
							case IBlindProblem.NO_ALT_IMG:
							case IBlindProblem.NO_ALT_INPUT:
								missing++;
								errorCount++;
								break;
							case IBlindProblem.WRONG_ALT_IMG:
							case IBlindProblem.WRONG_ALT_INPUT:
							case IBlindProblem.SEPARATE_DBCS_ALT_IMG:
							case IBlindProblem.SEPARATE_DBCS_ALT_INPUT:
							case IBlindProblem.WRONG_NBSP_ALT_IMG:
								wrong++;
								errorCount++;
							case IBlindProblem.NO_ALT_AREA: // TODO
							case IBlindProblem.WRONG_ALT_AREA:
							case IBlindProblem.SEPARATE_DBCS_ALT_AREA:
								area++;
							}
						}
					} else {
						// unselected guideline items
						i.remove();
					}
				}
			}
			pageData.setImageAltErrorNum(errorCount);
			pageData.setWrongAltNum(wrong);
			pageData.setMissingAltNum(missing);

			VisualizeViewUtil.visualizeError(result, problems, mapData, baseUrl);

			DebugPrintUtil.debugPrintln("error visualization\t" //$NON-NLS-1$
					+ (new Date()).getTime());

			DocumentCleaner.removeJavaScript(mapData.getNodeInfoList(), result);
			DocumentCleaner.removeMeta(result);
			DocumentCleaner.removeObject(result);
			DocumentCleaner.removeEmbed(result);
			DocumentCleaner.removeApplet(result);
			DocumentCleaner.removeBase(result);
			DocumentCleaner.removePI(result);

			DebugPrintUtil.debugPrintln("document cleaner\t" //$NON-NLS-1$
					+ (new Date()).getTime());

			VisualizationResultCleaner.clean(result, targetUrl);

			DebugPrintUtil.debugPrintln("result cleaner\t" //$NON-NLS-1$
					+ (new Date()).getTime());

			// TODO merge with visualizeError
			Id2LineViaActfId id2line = null;
			if (EvaluationUtil.isOriginalDOM()) {
				id2line = new Id2LineViaActfId(mapData.getId2AccIdMap(), html2viewMapV);
			}

			DebugPrintUtil.debugPrintln("id2line\t" //$NON-NLS-1$
					+ (new Date()).getTime());

			for (IProblemItem i : problems) {
				BlindProblem tmpBP = (BlindProblem) i;
				tmpBP.prepareHighlight();
				if (id2line != null) {
					tmpBP.setLineNumber(id2line);
				}
			}
			// merge

			VisualizeStyleInfoManager.getInstance().fireVisualizeStyleInfoUpdate(styleInfo);

			if (curParamBlind.visualizeMode.equals(ParamBlind.BLIND_BROWSER_MODE)) {
				replaceMathML_SVG_PacketCollection(allPc);
				VisualizeViewUtil.returnTextView(result, allPc, baseUrl);
				return;
			} else {
				variantFile = VisualizeViewUtil.prepareActions(result, mapData, baseUrl, servletMode);
				if (isHTML5) {
					VisualizeViewUtil.visualizeLandmark(result, baseUrl);
				}
				return;
			}
		}
	}

	@SuppressWarnings("nls")
	private void replaceElement(Document doc, String tag, String[] childTags, String message) {
		NodeList nl = doc.getElementsByTagName(tag);
		int size = nl.getLength();
		for (int i = size - 1; i >= 0; i--) {
			Element target = (Element) nl.item(i);
			Element div = doc.createElement("div");
			div.setAttribute("comment", target.getAttribute("comment"));
			div.setAttribute("id", target.getAttribute("id"));

			StringBuffer tmpSB = new StringBuffer();
			tmpSB.append("[" + message);

			for (int j = 0; j < childTags.length; j++) {
				NodeList nl2 = target.getElementsByTagName(childTags[j]);
				for (int k = 0; k < nl2.getLength(); k++) {
					Node tmpN = nl2.item(0).getFirstChild();
					String tmpS = "";
					if (tmpN != null) {
						tmpS = HtmlTagUtil.getTextDescendant(tmpN);
					}
					if (tmpS.length() > 0) {
						tmpSB.append(" " + childTags[j] + ": \"" + tmpS+"\"");
						break;
					}
				}
			}
			tmpSB.append("]");

			// remove other tags and attributes

			div.appendChild(doc.createTextNode(tmpSB.toString()));
			Node parent = target.getParentNode();
			parent.insertBefore(div, target);
			mapData.addReplacedNodeMapping(target, div);
			parent.removeChild(target);
		}

	}

	@SuppressWarnings("nls")
	private void replaceImgAndCheck(Document doc, VisualizeMapDataImpl mapData, boolean remove) {

		NodeList mapList = doc.getElementsByTagName("map");
		Map<String, Element> mapMap = new HashMap<String, Element>();
		int mapListsize = mapList.getLength();
		for (int i = 0; i < mapListsize; i++) {
			Element mapEl = (Element) mapList.item(i);
			mapMap.put(mapEl.getAttribute("name"), mapEl);
		}

		NodeList nl = doc.getElementsByTagName("img");
		int size = nl.getLength();
		Vector<IProblemItem> problemV = new Vector<IProblemItem>();

		ImgChecker imgChecker = new ImgChecker(mapData, mapMap, textChecker, problemV, baseUrl, checkItems);

		pageData.setTotalImageNumber(size);
		for (int i = size - 1; i >= 0; i--) {

			Element img = (Element) nl.item(i);
			// replaceImgAndCheckForOneImg(img, mapMap, doc, remove, problemV);
			imgChecker.checkAndReplaceImg(img, doc, remove);
		}

		size = problemV.size();

		for (int i = size - 1; i >= 0; i--) {
			IProblemItem tmpProblem = problemV.get(i);
			problems.add(tmpProblem);
		}

		// TODO 0 char is Error?
		// iframe
		nl = doc.getElementsByTagName("iframe");
		size = nl.getLength();
		for (int i = size - 1; i >= 0; i--) {
			Element iframe = (Element) nl.item(i);
			Element div = doc.createElement("div");
			// debug
			div.setAttribute("comment", iframe.getAttribute("comment"));
			div.setAttribute("id", iframe.getAttribute("id"));
			String title = null;
			boolean error = false;
			//
			NamedNodeMap map = iframe.getAttributes();
			int mapSize = map.getLength();
			Node titleNode = null;
			for (int j = 0; j < mapSize; j++) {
				Node node = map.item(j);
				if (node.getNodeName().equals("title")) {
					titleNode = node;
					break;
				}
			}
			if (titleNode != null) {
				title = titleNode.getNodeValue();
			} else {
				error = true;
				title = "";
			}

			if (remove) {
				div.setAttribute("width", iframe.getAttribute("width"));
				div.setAttribute("height", iframe.getAttribute("height"));
				if (error) {
					div.appendChild(doc.createTextNode("[iframe: (without title)]"));
					div.setAttribute("style", errorStyle);
				} else {
					div.appendChild(doc.createTextNode("[iframe: title=\"" + title + "\"]"));
					if (title.matches("^[\\s\u3000]*$")) {
						div.setAttribute("style", errorStyle);
					} else {
						div.setAttribute("style", iframe.getAttribute("style"));
					}
				}
				Node parent = iframe.getParentNode();
				parent.insertBefore(div, iframe);
				mapData.addReplacedNodeMapping(iframe, div);
				parent.removeChild(iframe);
			}
		}

		// mathML/SVG
		replaceElement(doc, "math", new String[0], "MathML");
		replaceElement(doc, "svg", new String[] { "title", "desc" }, "SVG");

		// image button
		nl = doc.getElementsByTagName("input");
		size = nl.getLength();
		Vector<IProblemItem> tmpV = new Vector<IProblemItem>();
		for (int i = size - 1; i >= 0; i--) {
			Element input = (Element) nl.item(i);
			String typeS = input.getAttribute("type").toLowerCase();
			if (typeS.equalsIgnoreCase("image")) {

				input.setAttribute("type", "button");
				//
				NamedNodeMap map = input.getAttributes();
				int mapSize = map.getLength();
				Node altNode = null;
				for (int j = 0; j < mapSize; j++) {
					Node node = map.item(j);
					if (node.getNodeName().equals("alt")) {
						altNode = node;
						break;
					}
				}
				if (altNode != null) {
					// TODO space separated
					input.setAttribute("value", altNode.getNodeValue());
				} else {
					BlindProblem prob = new BlindProblem(IBlindProblem.NO_ALT_INPUT);
					Integer idObj = mapData.getIdOfNode(input);
					if (idObj != null) {
						prob.setNode(input, idObj.intValue());
					} else {
						prob.setNode(input);
					}
					prob.setTargetNode(mapData.getOrigNode(input));
					// (Node) result2documentMap.get(input));

					tmpV.add(prob);
					input.setAttribute("value", input.getAttribute("src"));
					input.setAttribute("style", errorStyle);
				}
			} else if (typeS.matches("submit|reset|button")) {
				// System.out.println(input);
				BlindProblem prob = null;
				String valueS = input.getAttribute("value");
				if (valueS.length() == 0) {
					if (typeS.equals("button")) {
						// not readable
						prob = new BlindProblem(IBlindProblem.NO_VALUE_INPUT_BUTTON);
					}
				} else {
					TextCheckResult result = textChecker.checkAlt(valueS);
					if (result.equals(TextCheckResult.SPACE_SEPARATED)
							|| result.equals(TextCheckResult.SPACE_SEPARATED_JP)) {
						prob = new BlindProblem(IBlindProblem.SEPARATE_DBCS_INPUT_VALUE, valueS);
					}
				}
				if (prob != null) {
					Integer idObj = mapData.getIdOfNode(input);
					if (idObj != null) {
						prob.setNode(input, idObj.intValue());
					} else {
						prob.setNode(input);
					}
					prob.setTargetNode(mapData.getOrigNode(input));
					tmpV.add(prob);
				}
			}
		}

		for (int i = tmpV.size() - 1; i > -1; i--) {
			problems.add(tmpV.get(i));
		}

	}

	private void jwatInit() {
		if (fIsActivating) {
			return;
		}
		fIsActivating = true;
		if (jwatc == null) {
			try {
				jwatc = VoiceBrowserControllerFactory.createVoiceBrowserController();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return visualization result {@link Document}
	 */
	public Document getResult() {
		return result;
	}

	/**
	 * @return detected problems as List of {@link IProblemItem}
	 */
	public List<IProblemItem> getProbelems() {
		return problems;
	}

	/**
	 * Set base URL of target page
	 * 
	 * @param string
	 *            base URL
	 */
	public void setBaseUrl(String string) {
		baseUrl = string;
	}

	/**
	 * Set target URL
	 * 
	 * @param string
	 *            target URL
	 */
	public void setTargetUrl(String string) {
		targetUrl = string;
	}

	// Added on 2003/10/20
	private void calMaxTime() {
		iMaxTime = 0;
		iMaxTimeLeaf = 0;

		int orgMaxTime = 0;

		List<VisualizationNodeInfo> elementList = mapData.getNodeInfoList();
		int size = elementList.size();
		for (int i = 0; i < size; i++) {

			VisualizationNodeInfo curInfo = elementList.get(i);

			int time = curInfo.getTime();
			if (time > iMaxTime)
				iMaxTime = time;

			// TODO other sequencial elements (like list)
			if (curInfo.isBlockElement() && !curInfo.isSequence() && time > iMaxTimeLeaf) {
				iMaxTimeLeaf = time;
			}

			if (orgMaxTime < curInfo.getOrgTime()) {
				orgMaxTime = curInfo.getOrgTime();
			}

		}
		pageData.setMaxTime(iMaxTime);
		pageData.setOrgMaxTime(orgMaxTime);
	}

	// Added on 2003/10/20
	/**
	 * @return maximum reaching time in the page
	 */
	public int getMaxTime() {
		return iMaxTime;
	}

	/**
	 * Set invisible elements' ID
	 * 
	 * @param invisibleIdSet
	 *            target Set of IDs
	 */
	public void setInvisibleIdSet(Set<String> invisibleIdSet) {
		this.invisibleIdSet = invisibleIdSet;
	}

	/**
	 * @return the mapping information between original/visualization result
	 *         {@link Document}
	 * @see IVisualizeMapData
	 */
	public IVisualizeMapData getVisualizeMapData() {
		return mapData;
	}

	/**
	 * Set vector of HTML source code position information
	 * 
	 * @param html2viewMapV
	 *            target Vector of {@link Html2ViewMapData}
	 */
	public void setHtml2viewMapV(Vector<Html2ViewMapData> html2viewMapV) {
		this.html2viewMapV = html2viewMapV;
	}

	/**
	 * Set target {@link PageData} to store page statistics data through
	 * visualization
	 * 
	 * @param pageData
	 *            target {@link PageData}
	 */
	public void setPageData(PageData pageData) {
		this.pageData = pageData;
	}

	/**
	 * Set true if the target document is HTML5.
	 * 
	 * @param isHTML5
	 */
	public void setHTML5(boolean isHTML5) {
		this.isHTML5 = isHTML5;
	}

	/**
	 * @return variant for visualization as {@link File}
	 */
	public File getVariantFile() {
		return variantFile;
	}

}
