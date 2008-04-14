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
package org.eclipse.actf.visualization.gui.flash;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;
import org.eclipse.actf.accservice.swtbridge.MSAA;
import org.eclipse.actf.util.win32.WindowUtil;
import org.eclipse.actf.visualization.gui.ui.views.MSAATreeContentProvider;


public class FlashFinder {
    public static boolean debugMode = false; //TODO
    public static boolean scanAll = false;
    
    private boolean showOffScreen = false;  //TODO

    private List<AccessibleObject> result = new ArrayList<AccessibleObject>();

    
    public void find(Object[] inputElements) {
    	showOffScreen = MSAATreeContentProvider.getDefault().showOffscreen; //TODO
        for (int i = 0; i < inputElements.length; i++) {
            if (inputElements[i] instanceof AccessibleObject) {
                AccessibleObject accObject = (AccessibleObject)inputElements[i]; 
                if( scanAll || 
                    0 != findFlashWindow(accObject.getWindow()) ) {
                    findChildren(accObject);
                }
            }
        }
    }
    
    private static int findFlashWindow(int hwnd) {
        if( 0 != hwnd ) {
            if( FlashUtil.isFlashClass(WindowUtil.GetWindowClassName(hwnd)) ) {
                return hwnd;
            }
            for( int hwndChild=WindowUtil.GetChildWindow (hwnd); 0!=hwndChild;
                     hwndChild=WindowUtil.GetNextWindow (hwndChild) ) 
            {
                int hwndFound = findFlashWindow(hwndChild);
                if( 0 != hwndFound ) {
                    return hwndFound;
                }
            }
        }
        return 0;
    }

    private void findChildren(Object[] inputElements) {
        for (int i = 0; i < inputElements.length; i++) {
            findChildren((AccessibleObject) inputElements[i]);
        }
    }
    
    private void findChildren(AccessibleObject accObject) {
        if (null != accObject) {
            int role = accObject.getAccRole();
            if ((MSAA.ROLE_SYSTEM_WINDOW==role||MSAA.ROLE_SYSTEM_CLIENT==role) && FlashUtil.isFlash(accObject)) {
                result.add(accObject);
            } else {
                switch( accObject.getAccState() & (MSAA.STATE_INVISIBLE|MSAA.STATE_OFFSCREEN) ) {
                    case MSAA.STATE_INVISIBLE|MSAA.STATE_OFFSCREEN:
                        if( !showOffScreen ) break;
                    case 0:
                        findChildren(accObject.getChildren());
                        break;
                }
            }
        }
    }
    
    public Object[] getResults() {
        return result.toArray();
    }    
    
}
