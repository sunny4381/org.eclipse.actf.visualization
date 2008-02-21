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

package org.eclipse.actf.visualization.engines.lowvision.internal.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.actf.visualization.engines.lowvision.problem.ColorProblem;
import org.eclipse.actf.visualization.engines.lowvision.problem.LowVisionProblem;
import org.eclipse.actf.visualization.engines.lowvision.problem.LowVisionProblemGroup;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ProblemItemLV;



public class LowVisionProblemConverter {

    public static List<IProblemItem> convert(LowVisionProblemGroup[] target, String urlS, int frameId) {

        ArrayList<IProblemItem> result = new ArrayList<IProblemItem>();

        for (int i = 0; i < target.length; i++) {

            ProblemItemLV tmp = new ProblemItemLV("L_" + target[i].getLowVisionProblemType());
            tmp.setSubType(target[i].getLowVisionProblemType());
            try {
                tmp.setDescription(target[i].getDescription());
            } catch (Exception e) {
                tmp.setDescription("unknown");
            }
            tmp.setCanHighlight(true);


            tmp.setFrameId(frameId);
            tmp.setFrameUrlS(urlS);

            tmp.setSeverityLV(target[i].getIntProbability());
            tmp.setForegroundS(getLVProblemColorString(target[i], true));
            tmp.setBackgroundS(getLVProblemColorString(target[i], false));
            tmp.setX(target[i].getX());
            tmp.setY(target[i].getY());
            tmp.setWidth(target[i].getWidth());
            tmp.setHeight(target[i].getHeight());
            tmp.setArea(target[i].getWidth() * target[i].getHeight());

            //TODO recommendation            
            result.add(tmp);
        }

        return (result);
    }

    private static String getLVProblemColorString(LowVisionProblemGroup problem, boolean isFore) {
        short probType;
        int origAll;
        int origR;
        int origG;
        int origB;

        probType = problem.getLowVisionProblemType();

        if (probType == LowVisionProblem.LOWVISION_COLOR_PROBLEM) {
            ColorProblem cp = (ColorProblem) (problem.getRepresentative());
            if (isFore) {
                origAll = cp.getForegroundColor();
            } else {
                origAll = cp.getBackgroundColor();
            }
            origR = origAll >> 16 & 0xff;
            origG = origAll >> 8 & 0xff;
            origB = origAll & 0xff;
            return origR + "," + origG + "," + origB;
        } else {
            return " ";
        }
    }

}
