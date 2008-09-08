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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.actf.visualization.engines.blind.ParamBlind;
import org.eclipse.actf.visualization.engines.blind.html.IVisualizeMapData;
import org.eclipse.swt.graphics.RGB;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class VisualizeColorUtil {

    private static final double WORD_JP = (60.0 / (488.0 / 1.3));
    
    private Document result;

    private IVisualizeMapData mapData;

    private List nodeInfoList;

    private Map linkMap;

    private ParamBlind param;

    /**
     * @param result
     * @param mapData
     * @param nodeList
     * @param linkMap
     * @param param
     */
    public VisualizeColorUtil(Document result, VisualizeMapDataImpl mapData, ParamBlind param) {
        this.result = result;
        this.mapData = mapData;
        this.nodeInfoList = mapData.getNodeInfoList();
        this.linkMap = mapData.getIntraPageLinkMap();
        this.param = param;
    }

    public void setColorAll() {
        int changed = 0;

        DocumentCleaner.removeBgcolor(result);

        initHeadings();
        
        for (int i = 0; i < 10; i++) { // appropriate? 10?
            changed = calcWords();
            //calcWordsRefresh();

            if (changed == 0)
                break;
        } //set color onto each element.

        calcTime();
        
        calcOrgTime();

        setColor();
    }

    private void setColor() {

        String strRGB = "#000000";

        if (param.bVisualizeTable) {
            NodeList nl = result.getElementsByTagName("head");
            try {
                Element headEl = (Element) nl.item(0);

                Element styleEl = result.createElement("style");
                styleEl.setAttribute("type", "text/css");
                strRGB = getRGBString(param.tableBorderColor, "#000000");
                Comment comment = result.createComment("td {border-width: 1px; border-style: dashed; border-color: "
                        + strRGB + "}");
                styleEl.appendChild(comment);
                headEl.appendChild(styleEl);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Iterator it = nodeInfoList.iterator();
        while (it.hasNext()) {
            VisualizationNodeInfo info = (VisualizationNodeInfo) it.next();
            Node node = info.getNode();
            Element el = null;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                el = (Element) node;
            } else if (node.getNodeType() == Node.TEXT_NODE) {
                /*
                 * System.out.println( "VisualizeEngine: 709: parent of span: " +
                 * node.getParentNode().getNodeName());
                 */

                if (node.getParentNode().getNodeName().equals("textarea")) {
                    continue;
                }
                el = result.createElement("span");
                node.getParentNode().insertBefore(el, node);
                if (info.isInvisible()) {
                    //System.out.println("invisible:"+node);
                    node.getParentNode().removeChild(node);
                } else {
                    el.appendChild(node);
                }
                Integer idObj = mapData.getIdOfNode(node);
                if (idObj != null) {
                    el.setAttribute("id", "id" + idObj.toString());
                }

            } else {
                //error
                System.out.println("VisualizeEngine: 710: unknown node in the nodeList");
                continue;
            }

            if (param.bColorizeTags
                    && (info.isHeading() | info.isTableHeader() | info.isLabel() | info.isIdRequiredInput())) {
                if (info.isHeading()) {
                    strRGB = getRGBString(param.headingTagsColor, "#33CCFF");
                }
                if (info.isTableHeader()) {
                    strRGB = getRGBString(param.tableHeaderColor, "#99FF00");
                }
                if (info.isLabel()) {
                    strRGB = getRGBString(param.labelTagsColor, "#FFFF00");
                }
                if (info.isIdRequiredInput()) {
                    strRGB = getRGBString(param.inputTagsColor, "#FF9900");
                }
                el.setAttribute("style", "color: black; background-image: none; background-color:" + strRGB);//+
                // "}");
                //} else if (info.getWords() > 0) {
            } else {
                int time = info.getTime();
                if (time == 0) {
                    switch (param.iLanguage) {
                    case 1: //japanese
                        time = calcTimeJp(info.getTotalWords(), info.getTotalLines());
                        break;
                    default: //english
                        time = calcTime(info.getTotalWords(), info.getTotalLines());
                        break;
                    }
                    info.setTime(time);
                }

                if (param.bVisualizeTime == true) {
                    el.setAttribute("style", "color: black; background-image: none; background-color: #"
                            + calcColor(time, param.maxTimeColor, param.iMaxTime));

                } else {
                    el.setAttribute("style", "color: black; background-image: none; background-color: transparent");
                }
            } /*
               * else { }
               */
            /*
             * el.setAttribute( "comment", info.getPacketId() + "," +
             * info.getId() + "," + info.getTotalWords() + "," + info.getWords() +
             * "," + info.getTotalLines() + "," + info.getLines());
             */
        }
    }

    private String calcColor(int time, RGB rgb, int maxTime) {

        double timeD = time;
        double maxTimeD = maxTime;

        if (time >= maxTime) {
            java.awt.Color color = new java.awt.Color(rgb.red, rgb.green, rgb.blue);
            return Integer.toHexString(color.getRGB()).substring(2);
        } else {

            int colorValueR = (int) (255.0 - (timeD / maxTimeD) * (255.0 - rgb.red));
            int colorValueG = (int) (255.0 - (timeD / maxTimeD) * (255.0 - rgb.green));
            int colorValueB = (int) (255.0 - (timeD / maxTimeD) * (255.0 - rgb.blue));

            java.awt.Color color = new java.awt.Color(colorValueR, colorValueG, colorValueB);
            return Integer.toHexString(color.getRGB()).substring(2);
        }
    }

    private String getRGBString(RGB target, String defaultValue) {
        if (target != null) {
            return ("rgb(" + target.red + "," + target.green + "," + target.blue + ")");
        }
        return (defaultValue);
    }

    private void initHeadings(){
        //TODO consider combination of skip nav and Headings

        int headingCount = 0;
        
        int curTotalWords = 0;
        int curTotalLines = 0;
        int size = nodeInfoList.size();
        
        for (int i = 0; i < size; i++) {
            VisualizationNodeInfo curInfo = (VisualizationNodeInfo) nodeInfoList.get(i);

            if (curInfo.isHeading()) {
                if(curInfo.getNode().getNodeName().matches("h[1-6]")){
                    headingCount++;
                }

                int tmpTotalWords = wordcountForHeading(headingCount);
                
                //System.out.println(headingCount+": "+curTotalWords+" "+tmpTotalWords+" "+curInfo.getTotalWords()+" "+curInfo.getWords());
                if(calcTime(curTotalWords,curTotalLines)>=calcTime(tmpTotalWords,0)){
                    curTotalWords = tmpTotalWords;
                    curTotalLines = 0;
                }

                curInfo.setTotalWords(curTotalWords);
                curInfo.setTotalLines(curTotalLines);
                
                curTotalWords +=  curInfo.getWords();
                curTotalLines +=  curInfo.getLines();                

            } else {
                
                if (calcTime(curInfo.getTotalWords(),curInfo.getTotalLines()) > calcTime(curTotalWords,curTotalLines)) {
                    curInfo.setTotalWords(curTotalWords);
                    curInfo.setTotalLines(curTotalLines);

                    curTotalWords +=  curInfo.getWords();
                    curTotalLines +=  curInfo.getLines();
                    
                } else {

                    curTotalWords = curInfo.getTotalWords() + curInfo.getWords();
                    curTotalLines = curInfo.getTotalLines() + curInfo.getLines();
                    
                }
            }
        }
    }    

    private void calcTime(){
        int size = nodeInfoList.size();
        for (int i = 0; i < size; i++) {
            VisualizationNodeInfo curInfo = (VisualizationNodeInfo) nodeInfoList.get(i);

            int time = calcTime(curInfo.getTotalWords(), curInfo.getTotalLines());

            curInfo.setTime(time);
            if(curInfo.getNode().getNodeName().matches("h[1-6]")){
                replaceParentInfoTime(curInfo.getNode(),time);
            }
        }        
    }
    
    private void calcOrgTime() {
        int size = nodeInfoList.size();
        for (int i = 0; i < size; i++) {
            VisualizationNodeInfo curInfo = (VisualizationNodeInfo) nodeInfoList.get(i);

            int time = calcTime(curInfo.getOrgTotalWords(), curInfo.getOrgTotalLines());

            curInfo.setOrgTime(time);
        }
    }

    private void replaceParentInfoTime(Node target, int time) {
        if (target != null) {
            Node parent = target.getParentNode();
            while (parent != null) {
                if (parent.getFirstChild() == target) {
                    VisualizationNodeInfo nodeInfo = mapData.getNodeInfo(parent);
                    if (nodeInfo != null && nodeInfo.getTime() > time) {
                        nodeInfo.setTime(time);
                    }
                    target = parent;
                    parent = target.getParentNode();
                } else {
                    break;
                }
            }
        }
    }

    private void replaceParentInfoWord(Node target, int word, int line, int newTime) {
        if (target != null) {
            Node parent = target.getParentNode();
            while (parent != null) {
                if (parent.getFirstChild() == target) {
                    VisualizationNodeInfo nodeInfo = mapData.getNodeInfo(parent);
                    if (nodeInfo != null && calcTime(nodeInfo.getTotalWords(),nodeInfo.getTotalLines()) > newTime) {
                        nodeInfo.setTotalWords(word);
                        nodeInfo.setTotalLines(line);
                    }

                    target = parent;
                    parent = target.getParentNode();
                } else {
                    break;
                }
            }
        }
    }

    private int calcWords() {
        int countChanged = 0;
        Set linkSet = linkMap.keySet();
        Iterator it = linkSet.iterator();
        while (it.hasNext()) {
            Node fromNode = (Node) it.next();
            Node toNode = (Node) linkMap.get(fromNode);

            Integer fromIdInt = mapData.getIdOfNode(fromNode);
            Integer toIdInt = mapData.getIdOfNode(toNode);
            if (fromIdInt == null || toIdInt == null) {
                //toIdInt=null -> Alert is moved to other checker
                continue;
            }

            //TODO might be able to use mapData.getNodeInfo(node)
            int fromId = fromIdInt.intValue();
            int toId = toIdInt.intValue();

            VisualizationNodeInfo fromInfo = (VisualizationNodeInfo) nodeInfoList.get(fromId);
            if (fromInfo.getNode() != fromNode) {
                System.out.println("from node does not exists: " + fromId + " " + fromNode);
                continue;
            }
            VisualizationNodeInfo toInfo = (VisualizationNodeInfo) nodeInfoList.get(toId);
            if (toInfo.getNode() != toNode) {
                System.err.println("to node does not exists: " + toId + " " + toNode);
                continue;
            }

            VisualizationNodeInfo curInfo = toInfo;
            int curId = toId;
            int curTotalWords = fromInfo.getTotalWords() + getWordcountFor2sec();
            int curTotalLines = fromInfo.getTotalLines();
            int newTime = calcTime(curTotalWords,curTotalLines);

            while (calcTime(curInfo.getTotalWords(),curInfo.getTotalLines()) > newTime) {
                countChanged++;
                curInfo.setTotalWords(curTotalWords);
                curInfo.setTotalLines(curTotalLines);

                replaceParentInfoWord(curInfo.getNode(), curTotalWords, curTotalLines, newTime);

                //elements after intra page link
                curId++;
                if (curId >= nodeInfoList.size()) {
                    break;
                }

                curTotalWords = curTotalWords + curInfo.getWords();
                curTotalLines = curTotalLines + curInfo.getLines();
                curInfo = (VisualizationNodeInfo) nodeInfoList.get(curId);
                newTime = calcTime(curTotalWords,curTotalLines);
            }
        }
        return countChanged;
    }

    private int calcTime(int words, int lines) {
        switch (param.iLanguage) {
        case ParamBlind.EN:
            return calcTimeEn(words, lines);
        case ParamBlind.JP:
            return calcTimeJp(words, lines);
        default:
            return calcTimeEn(words, lines);
        }
    }

    //seconds
    private int calcTimeEn(int words, int lines) {
        return (int)((words / (3.0)) +(lines * (0.7)));// 3.0 = 60/180
    }

    //seconds
    private int calcTimeJp(int words, int lines) {
        return (int) ((words * WORD_JP) + (lines * (0.6)));
    }

    private int wordcountForHeading(int headingNumber){
        switch (param.iLanguage) {
        case ParamBlind.EN:
            return 6*(headingNumber-1)+15;
        case ParamBlind.JP:
            return 13*(headingNumber-1)+31;
        default:
            return 6*(headingNumber-1)+15;
        }
        
    }
    
    private int getWordcountFor2sec(){
        switch (param.iLanguage) {
        case ParamBlind.EN:
            return 6;
        case ParamBlind.JP:
            return 13;
        default:
            return 6;
        }        
    }
    
}
