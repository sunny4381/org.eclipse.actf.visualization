/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.problem;

import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.swt.graphics.Image;
import org.w3c.dom.Node;



public interface IProblemItem{// extends IEvaluationItem {

    public static final int SEV_ERROR = IEvaluationItem.SEV_ERROR;

    public static final int SEV_WARNING = IEvaluationItem.SEV_WARNING;

    public static final int SEV_INFO = IEvaluationItem.SEV_INFO;
    
    public static final String COMMA = ",";

    public abstract IEvaluationItem getEvaluationItem();

    public abstract String getDescription();

    public abstract int getSerialNumber();

    public abstract Node getTargetNode();

    public abstract String getTargetString();

    public abstract String getTargetStringForHPB();

    public abstract boolean isCanHighlight();

    public abstract void setCanHighlight(boolean canHighlight);

    public abstract void setCheckItem(IEvaluationItem checkItem);

    public abstract void setDescription(String description);

    public abstract void setSerialNumber(int serialNumber);

    void setHighlightTargetNodeInfo(HighlightTargetNodeInfo targetNodeInfo);
        
    HighlightTargetNodeInfo getHighlightTargetNodeInfo();
    
    void setHighlightTargetIds(HighlightTargetId targetId);
    
    void setHighlightTargetIds(HighlightTargetId[] targetIds);

    HighlightTargetId[] getHighlightTargetIds();

    public abstract void setTargetNode(Node targetNode);

    public abstract void setTargetString(String targetString);

    public abstract void setTargetStringForHPB(String targetString);

    public abstract int getLine();

    public abstract String getLineStr();

    public abstract void setLine(int line);

    public abstract String getLineStrMulti();

    void setHighlightTargetSourceInfo(HighlightTargetSourceInfo targetSourceInfo);

    void setHighlightTargetSourceInfo(HighlightTargetSourceInfo[] targetSourceInfo);

    HighlightTargetSourceInfo[] getHighlightTargetSoruceInfo();

    public abstract void accept(IProblemItemVisitor visitor);
    
    
    //TODO
    public String getId();

    public int getSeverity();
    
    public String getSeverityStr();
    
    public int[] getMetricsScores();
    
    public Image[] getMetricsIcons();
    
    public String[] getTableDataGuideline();

}
