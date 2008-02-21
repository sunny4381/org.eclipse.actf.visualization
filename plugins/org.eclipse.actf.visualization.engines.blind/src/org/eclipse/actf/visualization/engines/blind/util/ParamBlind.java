/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hironobu TAKAGI - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.blind.util;

import org.eclipse.actf.visualization.engines.blind.BlindVizEnginePlugin;
import org.eclipse.actf.visualization.engines.blind.ui.preferences.IBlindPreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;


public class ParamBlind {

    public static final int EN = 0;

    public static final int JP = 1;
    
    public static String BLIND_LAYOUT_MODE = IBlindPreferenceConstants.BLIND_LAYOUT_MODE;

    public static String BLIND_BROWSER_MODE = IBlindPreferenceConstants.BLIND_BROWSER_MODE;

    private static ParamBlind INSTANCE;
    
    public boolean oReplaceImage;

    public boolean oVisualizArrival; // Visualize arrival time (default on)

    public int iLanguage; //

    public String visualizeMode;

    public boolean bVisualizeTime;

    public boolean bColorizeTags;

    public boolean bVisualizeTable;

    public int iMaxTime;

    public RGB maxTimeColor;

    public RGB tableHeaderColor;

    public RGB headingTagsColor;

    public RGB inputTagsColor;

    public RGB labelTagsColor;

    public RGB tableBorderColor;

    public static ParamBlind getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ParamBlind();
        }
        return (INSTANCE);
    }

    public static void refresh(){
        ParamBlind pb = getInstance();
        setValues(pb);
    }

    private static void setValues(ParamBlind pb){
        IPreferenceStore store = BlindVizEnginePlugin.getDefault().getPreferenceStore();

        if(store.getDefaultString(IBlindPreferenceConstants.BLIND_LANG).equals(IBlindPreferenceConstants.LANG_JA)){
            pb.iLanguage = JP;
        }else{
            pb.iLanguage = EN;
        }

        pb.visualizeMode = store.getString(IBlindPreferenceConstants.BLIND_MODE);
        
        pb.iMaxTime = store.getInt(IBlindPreferenceConstants.BLIND_MAX_TIME_SECOND);
        pb.maxTimeColor = PreferenceConverter.getColor(store, IBlindPreferenceConstants.BLIND_MAX_TIME_COLOR);
        pb.tableHeaderColor = PreferenceConverter.getColor(store, IBlindPreferenceConstants.BLIND_TABLE_HEADER_COLOR);
        pb.headingTagsColor = PreferenceConverter.getColor(store, IBlindPreferenceConstants.BLIND_HEADING_TAGS_COLOR);
        pb.inputTagsColor = PreferenceConverter.getColor(store, IBlindPreferenceConstants.BLIND_INPUT_TAGS_COLOR);
        pb.labelTagsColor = PreferenceConverter.getColor(store, IBlindPreferenceConstants.BLIND_LABEL_TAGS_COLOR);
        pb.tableBorderColor = PreferenceConverter.getColor(store, IBlindPreferenceConstants.BLIND_TABLE_BORDER_COLOR);
        
    }
    
    private ParamBlind() {

        oReplaceImage = true;
        oVisualizArrival = true;
        bVisualizeTime = true;
        bColorizeTags = true;
        bVisualizeTable = true;
    
        setValues(this);
    }

}
