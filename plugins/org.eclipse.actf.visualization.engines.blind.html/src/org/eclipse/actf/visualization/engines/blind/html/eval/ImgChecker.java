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

package org.eclipse.actf.visualization.engines.blind.html.eval;

import java.util.Map;
import java.util.Vector;

import org.eclipse.actf.visualization.engines.blind.html.IVisualizeMapData;
import org.eclipse.actf.visualization.engines.blind.html.VisualizeEngine;
import org.eclipse.actf.visualization.engines.blind.html.internal.util.VisualizationNodeInfo;
import org.eclipse.actf.visualization.engines.blind.util.TextChecker;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ImgChecker {

	// separated from visualizeEngine
	// TODO refactoring
	// TODO UseMap (object)

	private IVisualizeMapData mapData;

	// private Map node2infoMap;

	// private Map idMap;

	private Map mapMap;

	private TextChecker textChecker;

	private Vector<IProblemItem> problemV;

	private String baseUrl;

	private boolean[] checkItems;

	/**
	 * @param result2documentMap
	 * @param node2infoMap
	 * @param idMap
	 * @param mapMap
	 * @param removedNodeMap
	 * @param textChecker
	 * @param problemV
	 * @param baseUrl
	 * @param checkItems
	 */
	public ImgChecker(IVisualizeMapData mapData, Map mapMap,
			TextChecker textChecker, Vector<IProblemItem> problemV,
			String baseUrl, boolean[] checkItems) {

		this.mapData = mapData;
		this.mapMap = mapMap;
		this.textChecker = textChecker;
		this.problemV = problemV;
		this.baseUrl = baseUrl;
		this.checkItems = checkItems;
	}

	public boolean checkAndReplaceImg(Element img, Document doc, boolean remove) {

		Element mapEl = null;
		NodeList areaNL = null;
		// NodeList aNL = null;

		String mapName = img.getAttribute("usemap");
		if (mapName.length() > 0) {

			mapEl = (Element) mapMap.get(mapName.substring(1));
			if (mapEl != null) {

				areaNL = mapEl.getElementsByTagName("area");
				// "a" -> CheckerEngine 57.2
			}
		}

		String imgText = checkAlt(img);

		if (remove) {
			Node parent = img.getParentNode();

			Element spanEl = doc.createElement("span");
			spanEl.setAttribute("width", img.getAttribute("width"));
			spanEl.setAttribute("height", img.getAttribute("height"));
			spanEl.setAttribute("id", img.getAttribute("id"));
			spanEl.setAttribute("style", img.getAttribute("style"));

			boolean isVisible = true;
			VisualizationNodeInfo info = mapData.getNodeInfo(img);
			if (info != null) {
				isVisible = !info.isInvisible();
			}

			if (imgText.length() > 0 && isVisible) {
				spanEl.appendChild(doc.createTextNode(imgText));
			}
			parent.insertBefore(spanEl, img);

			// image map
			if (areaNL != null) {
				int size = areaNL.getLength();
				for (int i = 0; i < size; i++) {
					Element areaEl = doc.createElement("span");
					areaEl.setAttribute("style", img.getAttribute("style"));
					spanEl.appendChild(areaEl);

					Element areaE = (Element) areaNL.item(i);
					BlindProblem prob = null;
					Integer idObj = null;

					if (!areaE.hasAttribute("alt")) {
						if (areaE.hasAttribute("href")) {
							prob = new BlindProblem(BlindProblem.NO_ALT_AREA,
									mapEl.getAttribute("name") + " , href=\""
											+ areaE.getAttribute("href") + "\"");
							prob.setTargetNode(mapData.getOrigNode(areaE));
						}
					} else {
						String alt = areaE.getAttribute("alt");
						if (alt.length() > 0) {
							if (textChecker.isInappropriateAlt(alt)) {
								prob = new BlindProblem(
										BlindProblem.WRONG_ALT_AREA, alt);

							} else if (textChecker
									.isSeparatedJapaneseChars(alt)) {
								prob = new BlindProblem(
										BlindProblem.SEPARATE_DBCS_ALT_AREA,
										alt);

							}

						} else {
							if (areaE.hasAttribute("href")) {
								prob = new BlindProblem(
										BlindProblem.WRONG_ALT_AREA, alt);
							}
						}
						if (prob != null) {
							prob.setTargetNode(mapData.getOrigNode(areaE));
						}
						areaEl.appendChild(doc
								.createTextNode("[" + alt + ".] "));
					}

					if (prob != null) {
						idObj = mapData.getIdOfNode(img);
						if (idObj != null) {
							prob.setNode(areaE, idObj.intValue());
						} else {
							prob.setNode(areaE);
						}
						problemV.add(prob);

						idObj = mapData.getIdOfNode(areaE);

						if (checkItems[prob.getProblemSubType()]) {
							Element errorImg = doc.createElement("img");
							errorImg.setAttribute("alt", "error icon");
							errorImg.setAttribute("title", prob
									.getDescription());
							if (idObj != null) {
								errorImg.setAttribute("onmouseover",
										"updateBaloon('id" + idObj + "');");
							}
							errorImg.setAttribute("src", baseUrl + "img/"
									+ VisualizeEngine.ERROR_ICON_NAME);
							areaEl.appendChild(errorImg);
						}
					}
				}
			}

			mapData.addReplacedNodeMapping(img, spanEl);
			parent.removeChild(img);
		}
		// TODO
		return true;
	}

	private String checkAlt(Element img) {

		boolean noAltError = false;
		String altS = "";

		BlindProblem prob = null;

		if (!img.hasAttribute("alt")) {
			prob = new BlindProblem(BlindProblem.NO_ALT_IMG, img
					.getAttribute("src"));
			noAltError = true;
		} else {
			altS = img.getAttribute("alt");
			if (altS.length() > 0) {
				if (textChecker.isInappropriateAlt(altS)) {
					prob = new BlindProblem(BlindProblem.WRONG_ALT_IMG, altS);
				} else if (textChecker.isSeparatedJapaneseChars(altS)) {
					prob = new BlindProblem(BlindProblem.SEPARATE_DBCS_ALT_IMG,
							altS);
				} else {
					switch (textChecker.isInappropriateAlt2(altS)) {
					case 3:
						prob = new BlindProblem(BlindProblem.ALERT_SPELL_OUT,
								altS);
						break;
					case 2:
						prob = new BlindProblem(BlindProblem.WRONG_ALT_IMG,
								altS);
						break;
					case 1:
						prob = new BlindProblem(BlindProblem.ALERT_WRONG_ALT,
								altS);
						break;
					case 0:
					default:
						break;
					}
				}
			}
		}
		if (prob != null) {
			prob.setTargetNode(mapData.getOrigNode(img));
			Integer idObj = mapData.getIdOfNode(img);
			if (idObj != null) {
				prob.setNode(img, idObj.intValue());
			} else {
				prob.setNode(img);
			}
			problemV.add(prob);
		}

		String imgText = null;
		VisualizationNodeInfo info = mapData.getNodeInfo(img);
		if (info != null && info.getPacket() != null) {
			imgText = info.getPacket().getText();
		} else {
			// alt="" or without alt
			if (noAltError) {
				imgText = "";
			} else {
				imgText = altS;
			}
		}
		return (imgText);
	}
}
