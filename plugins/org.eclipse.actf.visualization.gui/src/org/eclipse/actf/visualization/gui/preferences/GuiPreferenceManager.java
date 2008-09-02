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
package org.eclipse.actf.visualization.gui.preferences;

import org.eclipse.actf.util.win32.OverlayWindow;
import org.eclipse.actf.visualization.gui.internal.GuiPlugin;
import org.eclipse.actf.visualization.gui.ui.actions.WindowListAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;


public class GuiPreferenceManager {

    private static IPreferenceStore preferenceStore = GuiPlugin.getDefault().getPreferenceStore();
    
    public static void init() {
    	resetOverlayWindow();
    	resetAlwaysOnTop();
        preferenceStore.addPropertyChangeListener(new IPropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
            	String name = event.getProperty();
                if( GuiPreferenceConstants.UseOverlayWindow.equals(name)) {
                	resetOverlayWindow();
                }
                else if( GuiPreferenceConstants.AlwaysOnTop.equals(name)) {
                	resetAlwaysOnTop();
                }
            }
        });
    }
    
    private static void resetOverlayWindow() {
		OverlayWindow.setVisible(getPreferenceBoolean(GuiPreferenceConstants.UseOverlayWindow));
    }
    
    private static void resetAlwaysOnTop() {
    	WindowListAction.setWindowOrder(getPreferenceBoolean(GuiPreferenceConstants.AlwaysOnTop));
    }
    
    public static IPreferenceStore getPreferenceStore() {
    	return preferenceStore;
    }
    
    public static String getPreferenceString(String name) {
    	return preferenceStore.getString(name);
    }
    
    public static boolean getPreferenceBoolean(String name) {
    	return preferenceStore.getBoolean(name);
    }
    
    public static void setPreference(String name, String value) {
    	preferenceStore.setValue(name,value);
    }
    
    public static void setPreference(String name, boolean value) {
    	preferenceStore.setValue(name,value);
    }
    
}
