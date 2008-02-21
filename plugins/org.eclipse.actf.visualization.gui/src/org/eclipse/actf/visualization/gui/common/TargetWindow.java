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

package org.eclipse.actf.visualization.gui.common;

import org.eclipse.actf.model.IModelService;
import org.eclipse.actf.model.IWebBrowserACTF;
import org.eclipse.actf.util.win32.WindowUtil;
import org.eclipse.actf.visualization.gui.targetdata.GuiTargetWindowData;
import org.eclipse.swt.widgets.Composite;




public class TargetWindow {

    private static Object currentElement = null;

    public static void setCurrentElement(Object element) {
        currentElement = element;
    }
    
    private static void update() {
        if( null == currentElement ) {
            setCurrentElement(TargetWindowDataCollector.getDefaultElement());
        }
    }
    
    public static boolean isEmbeddedBrowser() {
        update();
        return isEmbeddedBrowser(currentElement);
    }
    
    public static int getWindowHandle() {
        update();
        return getWindowHandle(currentElement);
    }
    
    public static String getID() {
        update();
        return getID(currentElement);
    }
    
    public static int getRootWindow() {
        update();
        return getRootWindow(currentElement);
    }

    /*
     * Utility functions
     */
    private static IModelService getModelService(Object element) {
        if( element instanceof IModelService ) {
            return (IModelService)element;
        }
        if( element instanceof TargetWindowDataProvider ) {
            return ((TargetWindowDataProvider)element).getActiveModelService();
        }
        return null;
    }

    private static int getWindowHandle(Object element) {
        IModelService modelService = getModelService(element);
        if( null != modelService ) {
            Composite composite = modelService.getTargetComposite();
            if( null != composite ) {
                return composite.handle;
            }
            Object objHandle = modelService.getAttribute(IModelService.ATTR_WINDOWHANDLE);
            if( objHandle instanceof Integer ) {
                return ((Integer)objHandle).intValue();
            }
        }
        return 0;
    }

    public static boolean isEmbeddedBrowser(Object element) {
        IModelService modelService = getModelService(element);
        if( null != modelService ) {
            return null != modelService.getTargetComposite();
        }
        return false;
    }

    private static boolean isBrowser(Object element) {
        IModelService modelService = getModelService(element);
        if( modelService instanceof GuiTargetWindowData ) {
            return ((GuiTargetWindowData)modelService).isBrowser();
        }
        return modelService instanceof IWebBrowserACTF;
    }

    public static String getTitle(Object element) {
        IModelService modelService = getModelService(element);
        if( null != modelService ) {
            return modelService.getTitle();
        }
        return null;
    }

    public static String getID(Object element) {
        IModelService modelService = getModelService(element);
        if( null != modelService ) {
            return modelService.getID();
        }
        return null;
    }

    public static int getRootWindow(Object element) {
        int hwnd = getWindowHandle(element);
        if( isBrowser(element) ) {
            int hwndChild = findBrowserChildren(hwnd);
            if( 0 != hwndChild ) {
                return hwndChild;
            }
        }
        return hwnd;
    }
    
    private static int findBrowserChildren(int hwnd) {
        if( 0 != hwnd ) {
            String className = WindowUtil.GetWindowClassName(hwnd);
            if( WebBrowserUtil.isBrowserClass(className) ) {
                return hwnd;
            }
            for( int hwndChild=WindowUtil.GetChildWindow (hwnd); 0!=hwndChild;
                     hwndChild=WindowUtil.GetNextWindow (hwndChild) ) 
            {
                int hwndFound = findBrowserChildren(hwndChild);
                if( 0 != hwndFound ) {
                    return hwndFound;
                }
            }
        }
        return 0;
    }

}
