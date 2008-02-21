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

package org.eclipse.actf.visualization.engines.blind.eval;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.actf.util.xpath.XPathUtil;
import org.eclipse.actf.visualization.eval.EvaluationResultImpl;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.w3c.dom.NodeList;



public class EvaluationResultBlind extends EvaluationResultImpl {

    private int count = 0;
    
    public void addProblemItems(Collection<IProblemItem> c) {
        stripProblem(c);
        super.addProblemItems(c);
    }

    public void setProblemList(List<IProblemItem> problemList) {
        count = 0;
        stripProblem(problemList);
        super.setProblemList(problemList);
    }

    private void stripProblem(Collection c){
        GuidelineHolder holder = GuidelineHolder.getInstance();
        for (Iterator i = c.iterator(); i.hasNext();) {
            try {
                IProblemItem tmpItem = (IProblemItem) i.next();
                if (holder.isMatchedCheckItem(tmpItem.getEvaluationItem())) {
                    tmpItem.setSerialNumber(count);
                    if (tmpItem.isCanHighlight() && tmpItem.getTargetNode() != null) {
                        NodeList tmpNL = XPathUtil.evalXPathNodeList(tmpItem.getTargetNode(), "ancestor::noscript");
                        // noframes can highlight
                        if (tmpNL != null && tmpNL.getLength() > 0) {
                            tmpItem.setCanHighlight(false);
                        }
                    }
                    count++;
                } else {
                    i.remove();
                }
            } catch (Exception e) {
                // e.printStackTrace();
                i.remove();
            }
        }
    }
    
}
