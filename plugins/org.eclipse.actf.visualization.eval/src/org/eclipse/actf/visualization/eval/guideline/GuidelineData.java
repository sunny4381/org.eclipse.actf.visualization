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

package org.eclipse.actf.visualization.eval.guideline;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.actf.visualization.eval.IGuidelineItem;
import org.eclipse.swt.graphics.Image;



public class GuidelineData {

    private String guidelineName;

    private int id; // define sort order

    //TODO nest
    private String levelStr = null;

    private String[] levels;

    private String[] mimetypes;

    private String description;

    private String category;

    private boolean isEnabled = false;

    private GuidelineData[] subLevelDataArray = new GuidelineData[0];

    private boolean[] hasMetrics;

    private HashSet<EvaluationItem> checkItemSet = new HashSet<EvaluationItem>();

    private Map<String, IGuidelineItem> guidelineItemMap = new HashMap<String, IGuidelineItem>();

    private String currentMIMEtype = "text/html";

    /**
     * @param guidelineName
     * @param levels
     * @param guidelineItemMap
     */
    public GuidelineData(String guidelineName, int id, String category, String description, String[] levels,
            String[] categories, String[] descriptions, String[] mimetypes, Map<String, IGuidelineItem> guidelineItemMap) {
        this.guidelineName = guidelineName;
        this.guidelineItemMap = guidelineItemMap;
        this.levels = levels;
        this.mimetypes = mimetypes;
        this.id = id;
        this.category = category;
        this.description = description;
        
        subLevelDataArray = new GuidelineData[levels.length];
        for (int i = 0; i < levels.length; i++) {
            subLevelDataArray[i] = new GuidelineData(guidelineName, id, levels[i], categories[i], descriptions[i],
                    mimetypes, guidelineItemMap);
        }

        // for(int i=0;i<levels.length;i++){
        // System.out.println(guidelineName+"("+levels[i]+"):"+guidelineItems[i].size());
        // }
    }

    private GuidelineData(String guidelineName, int id, String levelStr, String categoryStr, String descriptionStr,
            String[] mimetypes, Map guidelineItemMap) {
        this.guidelineName = guidelineName;
        this.id = id;
        this.mimetypes = mimetypes;
        this.category = categoryStr;
        this.description = descriptionStr;

        // TODO
        if (levelStr == null) {
            this.levelStr = "";
        } else {
            this.levelStr = levelStr;
        }

        for (Iterator it = guidelineItemMap.values().iterator(); it.hasNext();) {
            IGuidelineItem item = (IGuidelineItem) it.next();
            if (item.getLevel().equals(levelStr)) {
                this.guidelineItemMap.put(item.getId(), item);
            }
        }
    }

    public IGuidelineItem getGuidelineItem(String id) {
        return ((IGuidelineItem) guidelineItemMap.get(id));
    }

    public Map<String, IGuidelineItem> getGuidelineItemMap() {
        return guidelineItemMap;
    }

    public String getGuidelineName() {
        return guidelineName;
    }

    public int getId() {
        return id;
    }

    // TODO
    public String[] getLevels() {
        return levels;
    }

    // TODO
    public boolean hasLevel() {
        return (0 != levels.length);
    }

    public String[] getMIMEtypes() {
        return mimetypes;
    }

    public boolean isEnabled() {
        if (subLevelDataArray.length > 0) {
            for (int i = 0; i < subLevelDataArray.length; i++) {
                if (subLevelDataArray[i].isEnabled()) {
                    return true;
                }
            }
            return false;
        }

        return isEnabled;
    }

    public boolean isMatched() {
        return (isEnabled() && isTargetMIMEtype(currentMIMEtype));
    }

    protected void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public GuidelineData getSubLevelData(String levelStr) {
        for (int i = 0; i < subLevelDataArray.length; i++) {
            if (subLevelDataArray[i].getLevelStr().equalsIgnoreCase(levelStr)) {
                return (subLevelDataArray[i]);
            }
        }
        return (null);
    }

    protected void setCheckItems(Collection checkItemCollection, String[] metrics) {
        hasMetrics = new boolean[metrics.length];
        Arrays.fill(hasMetrics, false);

        for (Iterator i = checkItemCollection.iterator(); i.hasNext();) {
            try {
                EvaluationItem item = (EvaluationItem) i.next();
                IGuidelineItem[] guidelineArray = item.getGuidelines();
                for (int j = 0; j < guidelineArray.length; j++) {
                    if (this.guidelineName.equals(guidelineArray[j].getGuidelineName())) {
                        if (levelStr == null || levelStr.equals(guidelineArray[j].getLevel())) {
                            Image[] icons = item.getMetricsIcons();
                            for (int l = 0; l < metrics.length; l++) {
                                if (icons[l] != null) {
                                    hasMetrics[l] = true;
                                }
                            }
                            checkItemSet.add(item);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < subLevelDataArray.length; i++) {
            subLevelDataArray[i].setCheckItems(checkItemCollection, metrics);
        }

        // System.out.println(guidelineName + " " + levelStr + ":" +
        // checkItemSet.size());
        // for (int i = 0; i < metrics.length; i++) {
        // System.out.println(guidelineName + " " + levelStr + ":" + metrics[i]
        // + "\t" + hasMetrics[i]);
        // }

    }

    public String getLevelStr() {
        return levelStr;
    }

    /**
     * @return Returns the checkItemSet.
     */
    public HashSet<EvaluationItem> getCheckItemSet() {
        return checkItemSet;
    }

    /**
     * @return Returns the hasMetrics.
     */
    public boolean[] getCorrespondingMetrics() {
        return hasMetrics;
    }

    public boolean isTargetMIMEtype(String mimetype) {
        for (int i = 0; i < mimetypes.length; i++) {
            if (mimetypes[i].equals(mimetype)) {
                return (true);
            }
        }
        return (false);
    }

    protected void setCurrentMIMEtype(String currentMIMEtype) {
        this.currentMIMEtype = currentMIMEtype;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
}
