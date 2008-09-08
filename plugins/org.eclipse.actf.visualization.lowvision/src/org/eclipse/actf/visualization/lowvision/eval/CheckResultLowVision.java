/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.lowvision.eval;

import java.util.Iterator;

import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;
import org.eclipse.actf.visualization.eval.EvaluationResultImpl;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ProblemItemLV;



public class CheckResultLowVision extends EvaluationResultImpl {

    //serial number
    public CheckResultLowVision() {
        setSummaryReportUrl("about:blank");
        setShowAllGuidelineItems(true);
    }
    
    public void setFrameOffsetToProblems(IPageImage[] framePageImages){
        int frameOffset[];

        if (null != framePageImages) {

            frameOffset = new int[framePageImages.length];
            frameOffset[0] = 0;

            for (int i = 1; i < framePageImages.length; i++) {
                frameOffset[i] = frameOffset[i - 1] + framePageImages[i - 1].getInt2D().getHeight();
            }

            for (Iterator<IProblemItem> i = getProblemList().iterator(); i.hasNext();) {
                try {
                    ProblemItemLV tmpP = (ProblemItemLV) i.next();
                    int frameId = tmpP.getFrameId();
                    if (frameId > -1 && frameId < frameOffset.length) {
                        tmpP.setFrameOffset(frameOffset[frameId]);
                        tmpP.setY(tmpP.getY() + frameOffset[frameId]);
                    }
                } catch (Exception e) {
                }
            }
        }
        
//        for (Iterator<IProblemItem> i = getProblemList().iterator(); i.hasNext();) {
//            ProblemItemLV tmpP = (ProblemItemLV) i.next();
//            System.out.println(tmpP.getFrameId()+":"+tmpP.getFrameOffset());
//        }
    }

}
