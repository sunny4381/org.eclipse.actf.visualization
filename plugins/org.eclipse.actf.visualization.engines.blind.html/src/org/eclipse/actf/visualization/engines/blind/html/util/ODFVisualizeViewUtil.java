/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tatsuya ISHIHARA - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.html.util;

import java.util.List;

import org.eclipse.actf.util.xpath.XPathUtil;
import org.eclipse.actf.visualization.engines.blind.html.VisualizeEngine;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ProblemItemImpl;
import org.eclipse.actf.visualization.util.html2view.Html2ViewMapData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ODFVisualizeViewUtil {
	public static void visualizeError(Document resultDoc, List<IProblemItem> problems) {
		final String NO_ALT_IMAGE = "10101";

		int size = problems.size();

		for (int i = 0; i < size; i++) {
			Object obj = problems.get(i);
			if (obj instanceof ProblemItemImpl) {
				ProblemItemImpl prob = (ProblemItemImpl) obj;
				if (prob.getId().equals("O_" + NO_ALT_IMAGE)) {
					Element imageElem = (Element) prob.getTargetNode();
					if (imageElem != null) {
						Element frameElem = (Element) imageElem.getParentNode();
						String idS = frameElem
								.getAttribute(Html2ViewMapData.ACTF_ID);
						if (idS.length() > 0) {
							Integer idObj = new Integer(idS);
							NodeList nl = XPathUtil.evalXPathNodeList(resultDoc
									.getDocumentElement(), "//*[@"
									+ Html2ViewMapData.ACTF_ID + "='" + idObj
									+ "']/span");
							if ((nl != null) && (nl.getLength() == 1)) {
								Element frameElemResultDoc = (Element) nl
										.item(0);
								Element img = createErrorImageElement(
										frameElemResultDoc, prob, idObj);
								frameElemResultDoc.appendChild(img);
							}
						}
					}
				}
			}
		}
	}

	private static Element createErrorImageElement(Node target,
			IProblemItem prob, Integer idObj) {
		Element img = target.getOwnerDocument().createElement("img");
		img.setAttribute("alt", "error icon");
		img.setAttribute("title", prob.getDescription());
		img.setAttribute("onmouseover", "updateBaloon('id" + idObj + "');");
		img.setAttribute("src", "img/" + VisualizeEngine.ERROR_ICON_NAME);
		return (img);
	}
}
