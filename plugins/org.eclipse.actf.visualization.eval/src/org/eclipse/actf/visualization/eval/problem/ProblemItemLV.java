/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.problem;

import org.eclipse.actf.visualization.eval.EvaluationPlugin;
import org.eclipse.swt.graphics.Image;




public class ProblemItemLV extends ProblemItemImpl implements IPositionSize, ILowvisionProblemSubtype {

    public static final int ICON_BOKE = 3;

    public static final int ICON_IRO = 1;

    //TODO i18n, consider plugin
    private static final String ERR_IRO = "Color problem";

    private static final String ERR_COMPLIANCEALERT = "Compliance information";

    private static final String ERR_BOKE = "Blur problem";

    private static final String ERR_HIGHLIGHT = "(can highlight)";
    
    
    private int area = 0;

    private String backgroundS = "";

    private String foregroundS = "";

    private int frameId = -1;

    private int frameOffset = 0;

    private String frameUrlS = "";

    private int height = 0;

    private int iconId;

    private int severityLV = 0;

    private int width = 0;

    private int x = 0;

    private int y = 0;

    private short subType;

    //TODO recommendation

    
    /**
     * @param id
     */
    public ProblemItemLV(String id) {
        super(id);
    }

    public int getArea() {
        return area;
    }

    public String getBackgroundS() {
        return backgroundS;
    }

    public String getForegroundS() {
        return foregroundS;
    }

    public int getFrameId() {
        return frameId;
    }

    public int getFrameOffset() {
        return frameOffset;
    }

    public String getFrameUrlS() {
        return frameUrlS;
    }

    public int getHeight() {
        return height;
    }

    public int getIconId() {
        return iconId;
    }

    public Image getImageIcon() {
        //return imageIcon;
        if (isCanHighlight()) {
            switch (iconId) {
            case ICON_IRO:
                return EvaluationPlugin.getImageDescriptor("icons/lowvision/HiIro21.gif").createImage();
            case ICON_BOKE:
                return EvaluationPlugin.getImageDescriptor("icons/lowvision/HiBoke21.gif").createImage();
            default:
                return null;
            }
        } else {
            switch (iconId) {
            case ICON_IRO:
                return EvaluationPlugin.getImageDescriptor("icons/lowvision/ErrIro21.gif").createImage();
            case ICON_BOKE:
                return EvaluationPlugin.getImageDescriptor("icons/lowvision/ErrBoke21.gif").createImage();
            default:
                return null;
            }
        }
    }

    public String getImageIconTooltip() {
        //return imageIcon;
        if (isCanHighlight()) {
            switch (iconId) {
            case ICON_IRO:
                return ERR_IRO + " " + ERR_HIGHLIGHT;
            case ICON_BOKE:
                return ERR_BOKE + " " + ERR_HIGHLIGHT;
            default:
                System.out.println("Icon not found: " + iconId);
                return ERR_COMPLIANCEALERT + " " + ERR_HIGHLIGHT;

            }
        } else {
            switch (iconId) {
            case ICON_IRO:
                return ERR_IRO;
            case ICON_BOKE:
                return ERR_BOKE;
            default:
                System.out.println("Icon not found: " + iconId);
                return ERR_COMPLIANCEALERT;
            }
        }
    }

    public int getSeverityLV() {
        return severityLV;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public void setBackgroundS(String backgroundS) {
        this.backgroundS = backgroundS;
    }

    public void setForegroundS(String foregroundS) {
        this.foregroundS = foregroundS;
    }

    public void setFrameId(int frameId) {
        this.frameId = frameId;
    }

    public void setFrameOffset(int frameOffset) {
        this.frameOffset = frameOffset;
    }

    public void setFrameUrlS(String frameUrlS) {
        this.frameUrlS = frameUrlS;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSeverityLV(int severityLV) {
        this.severityLV = severityLV;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public short getSubType() {
        return subType;
    }

    public void setSubType(short subType) {
        this.subType = subType;
        //TODO
        if (subType == LOWVISION_COLOR_PROBLEM || subType == LOWVISION_IMAGE_COLOR_PROBLEM) {
            iconId=ICON_IRO;
        } else {
            iconId=ICON_BOKE;
        }
        
    }
}
