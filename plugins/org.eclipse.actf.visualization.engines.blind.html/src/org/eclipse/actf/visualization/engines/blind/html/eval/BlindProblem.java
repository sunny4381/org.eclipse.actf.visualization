/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hironobu TAKAGI - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.html.eval;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.actf.util.html2view.Html2ViewMapData;
import org.eclipse.actf.visualization.engines.blind.html.util.Id2LineViaAccId;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetId;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetSourceInfo;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ProblemItemImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;



public class BlindProblem extends ProblemItemImpl implements IProblemItem {

    public static final int NO_ALT_IMG = 0;

    public static final int NO_ALT_INPUT = 1;

    public static final int NO_ALT_AREA = 2;

    public static final int WRONG_ALT_IMG = 4;

    public static final int WRONG_ALT_INPUT = 5;//TODO

    public static final int WRONG_ALT_AREA = 6;

    public static final int NO_DEST_LINK = 8;

    public static final int REDUNDANT_ALT = 9;

    public static final int NO_SKIPTOMAIN_LINK = 10;// without structure

    public static final int TOO_LESS_STRUCTURE = 12;

    public static final int NO_TEXT_INTRAPAGELINK = 14;

    public static final int WRONG_TEXT = 15;

    public static final int NO_ID_INPUT = 16;	//TODO

    public static final int TOOFAR_SKIPTOMAIN_LINK = 17;

    public static final int NO_DEST_SKIP_LINK = 18;

    public static final int WRONG_SKIP_LINK_TEXT = 19;

    public static final int NO_SKIPTOMAIN_WITH_STRUCTURE = 20;

    public static final int ALERT_NO_SKIPTOMAIN_NO_STRUCTURE = 21;

    public static final int LESS_STRUCTURE_WITH_SKIPLINK = 22;

    public static final int LESS_STRUCTURE_WITH_HEADING = 23;

    public static final int LESS_STRUCTURE_WITH_BOTH = 24;

    public static final int NO_TEXT_WITH_TITLE_INTRAPAGELINK = 25;

    public static final int WRONG_SKIP_LINK_TITLE = 26;

    public static final int ALERT_WRONG_ALT = 27;

    public static final int ALERT_REDUNDANT_TEXT = 28; // TODO

    public static final int SEPARATE_DBCS_ALT_IMG = 29;
    
    public static final int SEPARATE_DBCS_ALT_INPUT =30; //TODO

    public static final int SEPARATE_DBCS_ALT_AREA = 31;

    public static final int ALERT_NO_DEST_INTRA_LINK = 33;

    public static final int ALERT_SPELL_OUT = 34;

    public static final int INVISIBLE_INTRAPAGE_LINK = 35;

    public static final int NO_VALUE_INPUT_BUTTON = 36;

    public static final int SEPARATE_DBCS_INPUT_VALUE = 37;

    public static final int NUM_PROBLEMS = 38;// max id+1

    // ////////////////////////////////////////////////////

    private List<Node> nodeList = null;

    private int nodeId = -1;

    private boolean isMulti = false;

    private ArrayList<HighlightTargetId> idsList = new ArrayList<HighlightTargetId>();

    private int subType = -1; // TODO

    /**
     * Constructor for BlindProblem.
     */
    public BlindProblem(int _subtype) {
        this(_subtype, "");
    }

    /**
     * Constructor for BlindProblem.
     */
    public BlindProblem(int _subtype, String targetString) {
        super("B_" + Integer.toString(_subtype));

        subType = _subtype;

        nodeList = new Vector<Node>();
        setTargetString(targetString);

        // for HPB? use ReportUtil?
        switch (_subtype) {
        case WRONG_ALT_IMG:
        case WRONG_ALT_INPUT:
        case WRONG_ALT_AREA:
        case WRONG_TEXT:
        case ALERT_WRONG_ALT:
        case SEPARATE_DBCS_ALT_IMG:
        case SEPARATE_DBCS_ALT_INPUT:
        case SEPARATE_DBCS_ALT_AREA:
        // case WRONG_SKIP_LINK_TEXT:
        // case WRONG_SKIP_LINK_TITLE:
        // case WRONG_TITLE_IFRAME:
        case SEPARATE_DBCS_INPUT_VALUE:
        case REDUNDANT_ALT:
        case NO_DEST_LINK:
        case NO_DEST_SKIP_LINK:
        case ALERT_NO_DEST_INTRA_LINK:
        case ALERT_SPELL_OUT:
            this.targetStringForHPB = targetString;
            break;
        default:
        }
    }

    /**
     * Returns the node.
     * 
     * @return Node
     */
    public Node getTargetNodeInResultDoc() {
        if (nodeList.size() > 0) {
            return (Node) nodeList.get(0);
        } else {
            return null;
        }
    }

    /**
     * Sets the node.
     * 
     * @param node
     *            The node to set
     */
    public void setNode(Node node) {
        nodeList.add(0, node);
    }

    /**
     * Adds the node.
     * 
     * @param node
     *            The node to set
     */
    public void addNode(Node node) {
        nodeList.add(node);
    }

    /**
     * Sets the node.
     * 
     * @param node
     *            The node to set
     */
    public void setNode(Node node, int id) {
        nodeList.add(0, node);
        this.nodeId = id;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "node=" + nodeId + ":" + getDescription();
    }

    /**
     * Returns the nodeList.
     * 
     * @return List
     */
    public List<Node> getNodeList() {
        return nodeList;
    }

    /**
     * Sets the nodeId.
     * 
     * @param nodeId
     *            The nodeId to set
     */
    public boolean setNodeId(int nodeId) {
        if (this.nodeId == -1) {
            this.nodeId = nodeId;
            return true;
        } else {
            return false;
        }
    }

    public void addNodeIds(HighlightTargetId target) {
        isMulti = true;
        idsList.add(target);
    }

    public void prepareHighlight() {

        if (isMulti) {
            HighlightTargetId[] targets = new HighlightTargetId[idsList.size()];
            idsList.toArray(targets);
            setHighlightTargetIds(targets);
        } else {
            // setHighlightTargetIds(nodeId, nodeId);
            if (nodeId > -1) {
                setHighlightTargetIds(new HighlightTargetId(nodeId, nodeId));
            }
        }
    }

    private int getElementId(Element target) {
        int result = -1;
        String tmpId = target.getAttribute("id");
        if (tmpId.length() > 0 && tmpId.startsWith("id")) {
            tmpId = tmpId.substring(tmpId.indexOf("id") + 2);
            try {
                result = Integer.parseInt(tmpId);
            } catch (Exception e) {
            }
        }
        return result;
    }

    public void setLineNumber(Id2LineViaAccId id2line) {

        switch (subType) {
        case WRONG_TEXT:
            try {
                // text_node -> span_for_visualize -> real_parent
                Node tmpN = getTargetNodeInResultDoc().getParentNode().getParentNode();
                if (tmpN != null && tmpN.getNodeType() == Node.ELEMENT_NODE) {
                    int id = getElementId((Element) tmpN);
                    if (id > -1) {
                        // setLine(id2line.getLine(id));
                        Html2ViewMapData tmpData = id2line.getViewMapData(id);
                        if (tmpData != null) {
                            setHighlightTargetSourceInfo(new HighlightTargetSourceInfo(tmpData, tmpData));
                        }
                    }
                }
            } catch (Exception e1) {
                // e1.printStackTrace();
            }
            break;
        case NO_ALT_AREA:
        case WRONG_ALT_AREA:
        case SEPARATE_DBCS_ALT_AREA:
            // int tmpNodeId = nodeId;
            Node tmpN = getTargetNodeInResultDoc();
            if (tmpN.getNodeType() == Node.ELEMENT_NODE) {
                int id = getElementId((Element) tmpN);
                if (id > -1) {
                    Html2ViewMapData tmpData = id2line.getViewMapData(id);
                    setHighlightTargetSourceInfo(new HighlightTargetSourceInfo(tmpData, tmpData));
                    // setLine(id2line.getLine(id));
                }
            }
            break;
        default:
            // TODO divide more by using case 
            if (isMulti) {

                ArrayList<HighlightTargetSourceInfo> tmpArray = new ArrayList<HighlightTargetSourceInfo>();

                HighlightTargetId[] targets = new HighlightTargetId[idsList.size()];
                idsList.toArray(targets);
                for (int i = 0; i < targets.length; i++) {
                    Html2ViewMapData startData = id2line.getViewMapData(targets[i].getStartId());
                    Html2ViewMapData endData = id2line.getViewMapData(targets[i].getEndId());

                    if (startData == null) {
                        startData = endData;
                    }
                    if (endData == null) {
                        endData = startData;
                    }

                    if (startData != null) {
                        tmpArray.add(new HighlightTargetSourceInfo(startData, endData));
                    }
                }

                HighlightTargetSourceInfo[] sourceInfo = new HighlightTargetSourceInfo[tmpArray.size()];
                tmpArray.toArray(sourceInfo);
                setHighlightTargetSourceInfo(sourceInfo);

            } else {

                Html2ViewMapData tmpData = id2line.getViewMapData(nodeId);
                if (tmpData != null) {
                    setHighlightTargetSourceInfo(new HighlightTargetSourceInfo(tmpData, tmpData));
                }
                // check
                // setLine(id2line.getLine(nodeId));

            }
        }
    }

    public int getProblemSubType() {
        return (subType);
    }
}
