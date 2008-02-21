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

package org.eclipse.actf.visualization.eval.html.statistics;

import java.net.URL;
import java.util.Vector;

import org.eclipse.actf.util.xpath.XPathUtil;
import org.eclipse.actf.visualization.Constants;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ProblemItemImpl;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLImageElement;
import org.xml.sax.Attributes;



public class ImageStatData implements IPageStatisticsTag, IIMageTag {

    protected String altS = "";

    protected Element ancestorLink = null;

    protected String destUrlS = "";

    protected boolean hasAlt = false;

    protected boolean hasHeight = false;

    protected boolean hasLongDesc = false;

    protected boolean hasUseMap = false;

    protected boolean hasWidth = false;

    protected String heightS = "";

    protected boolean isInLink = false;

    protected boolean isMap = false;

    protected String longDescS = "";

    // for problem statistics
    protected Vector<IProblemItem> problemV = new Vector<IProblemItem>();

    protected String srcS = "";

    protected String urlS = "";

    protected String useMapS = "";

    protected String widthS = "";


    protected ImageStatData() {
    }

    /**
     * @param tagName
     * @param text
     */
    public ImageStatData(HTMLImageElement target, URL baseURL) {

        srcS = target.getSrc();
        urlS = srcS;
        try {
            urlS = new URL(baseURL, urlS).toString();
        } catch (Exception e) {
        }

        //System.out.println(urlS);

        if (hasAlt = target.hasAttribute(ALT)) {
            altS = target.getAlt();
        }
        if (hasLongDesc = target.hasAttribute(LONGDESC)) {
            longDescS = target.getLongDesc();
        }
        if (hasUseMap = target.hasAttribute(USEMAP)) {
            useMapS = target.getUseMap();
        }

        if (hasWidth = target.hasAttribute(WIDTH)) {
            widthS = target.getWidth();
        }

        if (hasHeight = target.hasAttribute(HEIGHT)) {
            heightS = target.getHeight();
        }

        isMap = target.getIsMap();

        NodeList tmpNL = XPathUtil.evalXPathNodeList(target, "ancestor::a");
        //XPathUtil.showNodeList(tmpNL,true);
        int len = tmpNL.getLength();
        if (len > 0) {
            HTMLAnchorElement tmpE = (HTMLAnchorElement) tmpNL.item(0);
            if (isInLink = tmpE.hasAttribute(HREF)) {
                destUrlS = tmpE.getHref();
                ancestorLink = tmpE;
                try {
                    destUrlS = new URL(baseURL, destUrlS).toString();
                } catch (Exception e) {
                }
            }
        }
    }

    public void addProblemItem(Attributes atts) throws StatisticsDataFormatException {
        String idS = atts.getValue(ID);
        String targetS = atts.getValue(TARGET_STRING);

        if (idS != null) {
            IProblemItem tmpItem = new ProblemItemImpl(idS);
            if (!tmpItem.getId().equals("unknown")) {
                if (targetS != null) {
                    tmpItem.setTargetStringForHPB(targetS);
                }
                addProblemItem(tmpItem);
                return;
            }
        }
        throw new StatisticsDataFormatException();
    }

    public void addProblemItem(IProblemItem problemItem) {
        problemV.add(problemItem);
    }

    public String getAltS() {
        return this.altS;
    }

    public Element getAncestorLink() {
        return ancestorLink;
    }

    private String getAttr(String name, boolean value) {
        return (getAttr(name, Boolean.toString(value)));
    }

    private String getAttr(String name, String value) {
        return ((name + "=\"" + XPathUtil.canonicalize(value) + "\" "));
    }

    public String getDestUrlS() {
        return destUrlS;
    }

    public String getHeightS() {
        return heightS;
    }

    public String getItemXML() {
        StringBuffer tmpSB = new StringBuffer("<" + IMAGE + " " + getAttr(SRC, srcS) + getAttr(URL, urlS)
                + getAttr(ISMAP, isMap));
        if (hasAlt) {
            tmpSB.append(getAttr(ALT, altS));
        }
        if (hasWidth) {
            tmpSB.append(getAttr(WIDTH, widthS));
        }
        if (hasHeight) {
            tmpSB.append(getAttr(HEIGHT, heightS));
        }
        if (isInLink) {
            tmpSB.append(getAttr(DEST, destUrlS));
        }
        if (hasLongDesc) {
            tmpSB.append(getAttr(LONGDESC, longDescS));
        }
        if (hasUseMap) {
            tmpSB.append(getAttr(USEMAP, useMapS));
        }
        int size = problemV.size();
        if (size == 0) {
            tmpSB.append(" />");
        } else {
            tmpSB.append(" >" + Constants.LINE_SEP);
            for (int i = 0; i < size; i++) {
                IProblemItem pItem = (IProblemItem) problemV.get(i);
                tmpSB.append("<" + ERROR + " " + getAttr(ID, pItem.getId())
                        + getAttr(TARGET_STRING, pItem.getTargetStringForHPB()) + " />" + Constants.LINE_SEP);
            }
            tmpSB.append("</" + IMAGE + ">");
        }
        return (tmpSB.toString());
    }

    public String getLongDescS() {
        return longDescS;
    }

    public Vector getProblemV() {
        return this.problemV;
    }

    public String getSrcS() {
        return srcS;
    }

    public String getUrlS() {
        return this.urlS;
    }

    public String getUseMapS() {
        return useMapS;
    }

    public String getWidthS() {
        return widthS;
    }

    public boolean isHasAlt() {
        return this.hasAlt;
    }

    public boolean isHasHeight() {
        return hasHeight;
    }

    public boolean isHasLongDesc() {
        return hasLongDesc;
    }

    public boolean isHasUseMap() {
        return hasUseMap;
    }

    public boolean isHasWidth() {
        return hasWidth;
    }

    public boolean isInLink() {
        return isInLink;
    }

    public boolean isMap() {
        return isMap;
    }

    public void setHasHeight(boolean hasHeight) {
        this.hasHeight = hasHeight;
    }

    public void setHasWidth(boolean hasWidth) {
        this.hasWidth = hasWidth;
    }

    public void setHeightS(String heightS) {
        this.heightS = heightS;
    }

    public void setWidthS(String widthS) {
        this.widthS = widthS;
    }
}
