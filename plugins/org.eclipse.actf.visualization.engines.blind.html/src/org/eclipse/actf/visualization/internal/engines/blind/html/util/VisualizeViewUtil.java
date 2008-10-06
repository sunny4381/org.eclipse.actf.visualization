/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.blind.html.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.actf.visualization.engines.blind.BlindVizResourceUtil;
import org.eclipse.actf.visualization.engines.blind.html.VisualizeEngine;
import org.eclipse.actf.visualization.engines.blind.html.eval.BlindProblem;
import org.eclipse.actf.visualization.engines.voicebrowser.IPacket;
import org.eclipse.actf.visualization.engines.voicebrowser.IPacketCollection;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetId;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VisualizeViewUtil {

	// separated from VisualizeEngine

	public static File prepareActions(Document result,
			VisualizeMapDataImpl mapData, String baseUrl, boolean servletMode) {

		Map linkMap = mapData.getIntraPageLinkMap();
		List targetElementList = mapData.getNodeInfoList();

		NodeList bodyEl = result.getElementsByTagName("body");

		for (int i = 0; i < bodyEl.getLength(); i++) {
			Element tmpE = (Element) bodyEl.item(i);
			DocumentCleaner.removeOnMouse(tmpE);
			DocumentCleaner.removeOnLoad(tmpE);
		}

		// TODO move to appropriate place
		// to handle no body page
		if (bodyEl.getLength() == 0) {
			Node tmpN = result.getDocumentElement();
			if (tmpN != null) {
				tmpN.appendChild(result.createElement("body"));
			} else {
				System.out.println("VisualizeViewUtil: no doc element");
				// TODO test
				return null;
			}
		}

		// div for arrow
		Element div = result.createElement("div");
		div
				.setAttribute("style",
						"position:absolute;pixelLeft= 10;pixelTop=10; color:red;font-size=12pt");
		div.setAttribute("id", "test");

		// bodyEl.item(bodyEl.getLength() - 1).appendChild(div);
		Node tmpBody = bodyEl.item(0);
		tmpBody.insertBefore(div, tmpBody.getFirstChild());

		insertLinkIcon(result, linkMap, baseUrl);

		// div for control_pane
		insertControlPane(result);

		// remove links of "map"

		bodyEl = result.getElementsByTagName("map");
		if (bodyEl != null) {
			for (int i = 0; i < bodyEl.getLength(); i++) {
				Element x = (Element) bodyEl.item(i);
				x.setAttribute("onClick", "cancelMapLink(event)");
			}
		}

		return (createScriptFile(result, targetElementList, baseUrl,
				servletMode));

	}

	private static void insertLinkIcon(Document doc, Map linkMap, String baseUrl) {
		Set linkIconSet = linkMap.keySet();
		Iterator it = linkIconSet.iterator();
		Set<String> alreadySet = new HashSet<String>();
		int id = 0;
		while (it.hasNext()) {
			Element lel = (Element) it.next();
			Element ael = (Element) linkMap.get(lel);

			Element imgel1 = doc.createElement("img");
			String href = lel.getAttribute("href").substring(1);
			imgel1.setAttribute("alt", "Intra-page link: " + href);
			imgel1.setAttribute("title", "Intra-page link: " + href);
			imgel1.setAttribute("src", baseUrl + "img/jump.gif");
			imgel1.setAttribute("name", "jump" + id);

			if (lel.hasChildNodes()) {
				lel.insertBefore(imgel1, lel.getFirstChild());
			} else {
				lel.appendChild(imgel1);
			}

			if (!alreadySet.contains(href)) {

				Element imgel2 = doc.createElement("img");
				imgel2.setAttribute("alt", "Intra-page link destination: "
						+ href);
				imgel2.setAttribute("title", "Intra-page link destination: "
						+ href);
				imgel2.setAttribute("src", baseUrl + "img/dest.gif");
				imgel2.setAttribute("name", href);

				if (ael.hasChildNodes()) {
					ael.insertBefore(imgel2, ael.getFirstChild());
				} else {
					ael.appendChild(imgel2);
				}
				alreadySet.add(href);
			}
			id++;
		}
	}

	private static void insertControlPane(Document result) {
		NodeList bodyEl = result.getElementsByTagName("body");
		Element div = result.createElement("div");
		div
				.setAttribute(
						"style",
						"position:absolute;font-size: medium; background-color: #FFFFFF; border-color: #333333 #000000 #000000; padding-top: 5px; padding-right: 5px; padding-bottom: 5px; padding-left: 5px; border-style: solid; border-top-width: 1px; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px");
		div.setAttribute("id", "control_pane");

		Element input1 = result.createElement("input");
		input1.setAttribute("type", "image");
		input1.setAttribute("src", "img/stop.gif");
		input1.setAttribute("alt", "Stop/Move Balloon");
		input1.setAttribute("onClick", "control_moving()");
		div.appendChild(input1);

		Element input2 = result.createElement("input");
		input2.setAttribute("type", "image");
		input2.setAttribute("src", "img/clear.gif");
		input2.setAttribute("alt", "Clear Line");
		input2.setAttribute("onClick", "clean_Line()");

		Element input3 = result.createElement("input");
		input3.setAttribute("type", "image");
		input3.setAttribute("src", "img/refresh.gif");
		input3.setAttribute("alt", "Refresh Line");
		input3.setAttribute("onClick", "refresh_Jump()");

		Element input4 = result.createElement("input");
		input4.setAttribute("type", "image");
		input4.setAttribute("src", "img/draw.gif");
		input4.setAttribute("alt", "Draw All Line");
		input4.setAttribute("onClick", "draw_all_Line()");

		div.appendChild(input2);
		div.appendChild(input3);
		div.appendChild(input4);
		// bodyEl.item(bodyEl.getLength() - 1).appendChild(div);
		Node tmpBody = bodyEl.item(0);
		tmpBody.insertBefore(div, tmpBody.getFirstChild());
	}

	private static File createScriptFile(Document result, List elementList,
			String baseUrl, boolean servletMode) {
		try {
			PrintWriter pw;

			File valiantFile = BlindVizResourceUtil.createTempFile("variant",
					".js");
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
					valiantFile), "UTF-8"));

			StringBuffer sb = new StringBuffer();
			sb.append("var id2time = new Array();");
			sb.append("var id2comment = new Array();");

			int size = elementList.size();
			for (int i = 0; i < size; i++) {
				VisualizationNodeInfo curInfo = (VisualizationNodeInfo) elementList
						.get(i);

				//
				String comment = curInfo.getComment();
				StringBuffer comment_sb = new StringBuffer();
				// if (871 == curInfo.getId()) {
				for (int x = 0; x < comment.length(); x++) {
					if (comment.charAt(x) == '\"') {
						comment_sb.append("\\");
					}
					if (comment.charAt(x) == '\'') {
						comment_sb.append('\\');
					}

					comment_sb.append(comment.charAt(x));
				}

				sb.append("id2time['id");
				sb.append(curInfo.getId());
				sb.append("']=");
				sb.append(curInfo.getTime());
				sb.append(";");

				sb.append("id2comment['id");
				sb.append(curInfo.getId());
				sb.append("']='");
				sb.append(comment_sb.toString());
				sb.append("';");

			}

			String tmpS = sb.toString().replaceAll("\n", "").replaceAll("\r",
					"");
			pw.write(tmpS);

			sb = new StringBuffer();
			sb.append("var baloonSwitch = 1; ");
			sb.append("var baseUrl = '" + baseUrl + "'; ");
			sb.append("var acc_imageDir = 'img/'; ");
			if (servletMode) {
				sb.append("var servletMode = true; ");
			} else {
				sb.append("var servletMode = false; ");
			}

			// speed up -> sb.append("var isAlert = false; ");

			sb.append("var isAlert = true; ");

			pw.write(sb.toString());

			pw.flush();
			pw.close();

			NodeList nl = result.getElementsByTagName("head");
			if (nl.getLength() > 0) {
				Element el = (Element) nl.item(0);
				Element script = result.createElement("script");
				// script.setAttribute("src", "file:///C:/C/highlight.js");
				script.setAttribute("src", baseUrl + valiantFile.getName());
				el.appendChild(script);

				Element script2 = result.createElement("script");
				// script.setAttribute("src", "file:///C:/C/highlight.js");
				script2.setAttribute("src", baseUrl + "img/highlight.js");
				el.appendChild(script2);
			}

			Element div = result.createElement("div");
			div
					.setAttribute(
							"style",
							"position:absolute;font-size: medium; background-color: #FFFFFF; border-color: #333333 #000000 #000000; padding-top: 5px; padding-right: 5px; padding-bottom: 5px; padding-left: 5px; border-style: solid; border-top-width: 1px; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px");
			div.setAttribute("id", "balloon");

			Element messageDiv = result.createElement("div");
			messageDiv.setAttribute("id", "message");
			messageDiv.appendChild(result.createTextNode(""));
			div.appendChild(messageDiv);

			NodeList bodyNl = result.getElementsByTagName("body");
			if (bodyNl.getLength() > 0) {
				Element bodyEl = (Element) bodyNl.item(0);
				bodyEl.insertBefore(div, bodyEl.getFirstChild());
			}
			return valiantFile;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void visualizeError(Document doc, List problems,
			VisualizeMapDataImpl mapData, String baseUrlS) {
		int size = problems.size();

		// TODO setNodeId might be duplicated
		for (int i = 0; i < size; i++) {
			BlindProblem prob = (BlindProblem) problems.get(i);
			Node node = prob.getTargetNodeInResultDoc();
			Integer idObj = mapData.getIdOfNode(node);

			int subType = prob.getProblemSubType();
			switch (subType) {
			case BlindProblem.WRONG_ALT_AREA:
			case BlindProblem.NO_ALT_AREA:
			case BlindProblem.TOO_LESS_STRUCTURE:
			case BlindProblem.NO_SKIPTOMAIN_LINK:
				break;
			default:
				if (idObj != null) {
					int id = idObj.intValue();
					prob.setNodeId(id);
				}
			}

			VisualizationNodeInfo info = mapData.getNodeInfo(node);
			if (info != null) {
				info.appendComment(prob.getDescription());
			} else {
				if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
					try {
						String id = ((Element) node).getAttribute("id");
						if (id.startsWith("id")) {
							id = id.substring(2);
							// info =

						}
					} catch (Exception e) {
						//
					}
				}
			}

			switch (prob.getProblemSubType()) {
			case BlindProblem.NO_ALT_IMG:
			case BlindProblem.WRONG_ALT_IMG:
				Element el = (Element) node;
				Node replacement = mapData.getReplacement(el);
				if (replacement != null) {
					el = (Element) replacement;
				}
				Element img = createErrorImageElement(node, prob, idObj,
						baseUrlS);
				el.appendChild(img);
				break;
			case BlindProblem.REDUNDANT_ALT:
				int startId = -1;
				int endId = -1;
				try {
					List nl = prob.getNodeList();
					node = (Node) nl.get(1);
					Integer endNodeId = mapData.getIdOfNode(node);
					if (endNodeId != null) {
						endId = endNodeId.intValue();
					}
				} catch (NullPointerException npe) {
					npe.printStackTrace();
				}

				if (idObj != null) {
					startId = idObj.intValue();
				} else if (endId > -1) {
					startId = endId;
				}

				prob.setNodeId(startId);

				prob.addNodeIds(new HighlightTargetId(startId, startId));
				prob.addNodeIds(new HighlightTargetId(endId, endId));

				break;
			case BlindProblem.NO_DEST_LINK:
			case BlindProblem.NO_TEXT_INTRAPAGELINK:
			case BlindProblem.NO_DEST_SKIP_LINK:
			case BlindProblem.WRONG_SKIP_LINK_TEXT:
			case BlindProblem.TOOFAR_SKIPTOMAIN_LINK:
			case BlindProblem.NO_TEXT_WITH_TITLE_INTRAPAGELINK:
			case BlindProblem.INVISIBLE_INTRAPAGE_LINK:
			case BlindProblem.WRONG_SKIP_LINK_TITLE:
				Element element = (Element) node;
				Element image = createErrorImageElement(element, prob, idObj,
						baseUrlS);
				element.appendChild(image);
				break;
			case BlindProblem.WRONG_TEXT:
				// Node node = prob.getNode();
				Element image2 = createErrorImageElement(node, prob, idObj,
						baseUrlS);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					node.appendChild(image2);
				} else {
					node.getParentNode().insertBefore(image2, node);
				}
				break;
			case BlindProblem.NO_VALUE_INPUT_BUTTON:
			case BlindProblem.SEPARATE_DBCS_INPUT_VALUE:
				Element image3 = createErrorImageElement(node, prob, idObj,
						baseUrlS);
				node.getParentNode().insertBefore(image3, node);
				break;

			default:
			}
		}
	}

	private static Element createErrorImageElement(Node target,
			IProblemItem prob, Integer idObj, String baseUrlS) {
		Element img = target.getOwnerDocument().createElement("img");
		img.setAttribute("alt", "error icon");
		img.setAttribute("title", prob.getDescription());
		img.setAttribute("onmouseover", "updateBaloon('id" + idObj + "');");
		img.setAttribute("src", baseUrlS + "img/"
				+ VisualizeEngine.ERROR_ICON_NAME);
		return (img);
	}

	public static Document returnTextView(Document result,
			IPacketCollection allPc, String baseUrl) {
		NodeList bodyNl = result.getElementsByTagName("body");

		// TODO remove second Body, script, etc.
		if (bodyNl.getLength() > 0) {
			Element bodyEl = (Element) bodyNl.item(0);
			NodeList nl = bodyEl.getChildNodes();
			int size = nl.getLength();
			for (int i = size - 1; i >= 0; i--) {
				bodyEl.removeChild(nl.item(i));
			}
			String str;
			size = allPc.size();
			boolean brFlag = false;
			boolean insideLink = false;
			for (int i = 0; i < size; i++) {
				IPacket p = (IPacket) allPc.get(i);

				if (p.getContext().isLinkTag()) {
					insideLink = true;
				}
				str = p.getText();
				if (str != null && !str.equals("")) {

					Element spanEl = result.createElement("span");
					spanEl.appendChild(result.createTextNode(str));
					bodyEl.appendChild(spanEl);

					if (insideLink)
						spanEl.setAttribute("style",
								"text-decoration: underline;");
					brFlag = false;
				}
				if (p.getContext().isLineDelimiter()) {
					if (brFlag) {
						/*
						 * Element spanEl = result.createElement("span");
						 * spanEl.appendChild( result.createTextNode(" SKIPPED
						 * ")); bodyEl.appendChild(spanEl);
						 */
					} else {
						Element br = result.createElement("br");
						bodyEl.appendChild(br);
						brFlag = true;
					}
				}
				if (!p.getContext().isLinkTag()) {
					insideLink = false;
				}
			}
		}

		NodeList nl = result.getElementsByTagName("head");
		if (nl.getLength() > 0) {

			Element el = (Element) nl.item(0);
			Element script = result.createElement("script");
			script.setAttribute("src", baseUrl + "img/highlight-dummy.js");
			el.appendChild(script);

		}

		return result;
	}

}
