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

package org.eclipse.actf.visualization.eval.problem;

import org.eclipse.actf.util.html2view.Html2ViewMapData;
import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.actf.visualization.eval.IGuidelineItem;
import org.eclipse.actf.visualization.eval.guideline.EvaluationItem;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetId;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetNodeInfo;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetSourceInfo;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.IProblemItemVisitor;
import org.eclipse.swt.graphics.Image;
import org.w3c.dom.Node;



public class ProblemItemImpl implements IProblemItem {

    private static GuidelineHolder GUIDELINE_HOLDER = GuidelineHolder.getInstance();

    private boolean canHighlight = false;

    private IEvaluationItem checkItem;

    private String description = "";

    private int serialNumber = -1;

    private int line = -1;

    private HighlightTargetNodeInfo highlightTargetNodeInfo;

    private HighlightTargetId[] targetIds = new HighlightTargetId[0];

    private HighlightTargetSourceInfo[] targetSources = new HighlightTargetSourceInfo[0];

    private Node targetNode = null;

    protected String targetStringForHPB = "";

    protected String targetString = "";

    // TODO add Icon(for Result doc) info

    /**
     * 
     */
    public ProblemItemImpl(String id) {
        checkItem = GUIDELINE_HOLDER.getCheckItem(id);

        if (checkItem == null) {
            checkItem = new EvaluationItem("unknown", EvaluationItem.SEV_INFO_STR);
            System.err.println("Problem Item: unknown id \"" + id + "\"");

        } else {
            description = checkItem.createDescription();
        }

    }

    public ProblemItemImpl(String id, Node targetNode) {
        this(id);
        setTargetNode(targetNode);
    }

    public IEvaluationItem getEvaluationItem() {
        return checkItem;
    }

    public String getId() {
        return checkItem.getId();
    }

    public String[] getTableDataGuideline() {
        return checkItem.getTableDataGuideline();
    }

    public String[] getTableDataMetrics() {
        return checkItem.getTableDataMetrics();
    }

    public int[] getMetricsScores() {
        return checkItem.getMetricsScores();
    }

    public Image[] getMetricsIcons() {
        return checkItem.getMetricsIcons();
    }

    public int getSeverity() {
        return checkItem.getSeverity();
    }

    public String getSeverityStr() {
        return checkItem.getSeverityStr();
    }

    public String getDescription() {
        return description;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public Node getTargetNode() {
        return targetNode;
    }

    public String getTargetStringForHPB() {
        return targetStringForHPB;
    }

    public boolean isCanHighlight() {
        return canHighlight;
    }

    public void setCanHighlight(boolean canHighlight) {
        this.canHighlight = canHighlight;
    }

    public void setCheckItem(IEvaluationItem checkItem) {
        this.checkItem = checkItem;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setHighlightTargetIds(HighlightTargetId[] targetIds) {
        if (targetIds != null) {
            this.targetIds = targetIds;
            if (targetIds.length > 0) {
                canHighlight = true;
            }
        }
    }

    public HighlightTargetId[] getHighlightTargetIds() {
        return (targetIds);
    }

    public void setTargetNode(Node targetNode) {
        this.targetNode = targetNode;
    }

    public void setTargetStringForHPB(String targetString) {
        this.targetStringForHPB = targetString;
    }

    public String getTargetString() {
        return targetString;
    }

    public void setTargetString(String targetString) {
        this.targetString = targetString;
        if (targetString != null && targetString.length() > 0) {
            this.description = checkItem.createDescription(targetString);
        }
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getLineStr() {
        int curLine = getLine();
        if (curLine > -1) {
            return (Integer.toString(curLine));
        } else {
            return ("");
        }
    }

    public String getLineStrMulti() {
        StringBuffer tmpSB = new StringBuffer();
        // TODO check multiple same line number
        if (targetSources.length > 0) {
            for (int i = 0; i < targetSources.length; i++) {
                int tmp = targetSources[i].getStartLine() + 1;
                tmpSB.append(tmp);
                tmpSB.append(", ");
            }
            String result = tmpSB.substring(0, tmpSB.length() - 2);
            return result;
        }
        if (line > -1) {
            return (Integer.toString(line));
        } else {
            return ("");
        }
    }

    public void setHighlightTargetSourceInfo(HighlightTargetSourceInfo[] targetSourceInfo) {
        if (targetSourceInfo != null) {
            targetSources = targetSourceInfo;
            int tmpLine = Integer.MAX_VALUE;
            for (int i = 0; i < targetSources.length; i++) {
                if (tmpLine > targetSources[i].getStartLine()) {
                    tmpLine = targetSources[i].getStartLine();
                }
                // TODO
                if (tmpLine != Integer.MAX_VALUE) {
                    line = tmpLine;
                }
            }
        }
    }

    public HighlightTargetSourceInfo[] getHighlightTargetSoruceInfo() {
        if (targetSources.length == 0 && line > -1) {
            Html2ViewMapData dummy = new Html2ViewMapData(new int[] { line, -1 }, new int[] { line, -1 });
            return (new HighlightTargetSourceInfo[] { new HighlightTargetSourceInfo(dummy, dummy) });
        }
        return (targetSources);
    }

    public void accept(IProblemItemVisitor visitor) {
        visitor.visit(this);
    }

    public HighlightTargetNodeInfo getHighlightTargetNodeInfo() {
        return highlightTargetNodeInfo;
    }


    public void setHighlightTargetNodeInfo(HighlightTargetNodeInfo targetNodeInfo) {
        this.highlightTargetNodeInfo = targetNodeInfo;
    }

    public void setHighlightTargetIds(HighlightTargetId targetId) {
        if(targetId!=null){
            this.setHighlightTargetIds(new HighlightTargetId[]{targetId});
        }
    }

    public void setHighlightTargetSourceInfo(HighlightTargetSourceInfo targetSourceInfo) {
        if(targetSourceInfo!=null){
            this.setHighlightTargetSourceInfo(new HighlightTargetSourceInfo[]{targetSourceInfo});
        }
    }

    public IGuidelineItem[] getGuidelines() {
        if(checkItem!=null){
            return checkItem.getGuidelines();
        }
        return new IGuidelineItem[0];
    }
}
