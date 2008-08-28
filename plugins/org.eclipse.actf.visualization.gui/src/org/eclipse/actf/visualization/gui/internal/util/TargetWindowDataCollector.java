/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.gui.internal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.actf.visualization.gui.GuiPlugin;
import org.eclipse.actf.visualization.gui.TargetWindowDataProvider;
import org.eclipse.actf.visualization.gui.targetdata.GuiTargetWindowData;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;




public class TargetWindowDataCollector {
    
    private static final String TARGETDATA_EXTENSION = GuiPlugin.PLUGIN_ID+".targetWindowData"; //$NON-NLS-1$
    private static TargetWindowDataProvider[] targetDataProviders = new TargetWindowDataProvider[0];
    
    // Initialize TargetWindowDataProvider
    static {
        List<TargetWindowDataProvider> dspList = new ArrayList<TargetWindowDataProvider>();
        IConfigurationElement[] targetDataElements = Platform.getExtensionRegistry().getConfigurationElementsFor(TARGETDATA_EXTENSION);
        for( int i=0; i<targetDataElements.length; i++ ) {
            try {
                TargetWindowDataProvider provider = (TargetWindowDataProvider)targetDataElements[i].createExecutableExtension("class"); //$NON-NLS-1$
                if( null != provider ) {
                    dspList.add(provider);
                }
            }
            catch( Exception e ) {
                e.printStackTrace();
            }
        }
        targetDataProviders = dspList.toArray(new TargetWindowDataProvider[dspList.size()]);
    }
    
    public static Object[] getElements() {
        List resultList = new ArrayList();
        for( int i=0; i<targetDataProviders.length; i++ ) {
            if( null != targetDataProviders[i] ) {
                Object[] elements = targetDataProviders[i].getModelService();
                if( null != elements ) {
                    resultList.addAll(Arrays.asList(elements));
                }
                else {
                    resultList.add(targetDataProviders[i]);
                }
            }
        }
        return resultList.toArray();
    }
    
    public static Object getDefaultElement() {
        Object[] elements = getElements();
        for( int i=0; i<elements.length; i++ ) {
            if( TargetWindow.isEmbeddedBrowser(elements[i]) ) {
                return elements[i];
            }
        }
        return null;
    }
    
    private Map<String, List> categoryMap = new HashMap<String, List>();
    
    public TargetWindowDataCollector() {
        Object[] elements = getElements();
        for( int i=0; i<elements.length; i++ ) {
            String category = null;
            if( elements[i] instanceof GuiTargetWindowData ) {
                category = ((GuiTargetWindowData)elements[i]).getCategory();
            }
            List entry = (List)categoryMap.get(category);
            if( null==entry ) {
                entry = new ArrayList();
                categoryMap.put(category,entry);
            }
            entry.add(elements[i]);
        }
    }
    
    public String[] getCategories(Comparator comparator) {
        List<String> resultList = new ArrayList<String>(categoryMap.keySet());
        if( null != comparator ) {
            Collections.sort(resultList, comparator); 
        }
        return (String[])resultList.toArray(new String[resultList.size()]);
    }
    
    public Object[] getElements(String category) {
        List resultList = (List)categoryMap.get(category);
        if( null != resultList ) {
            return resultList.toArray();
        }
        return new Object[0];
    }
}
