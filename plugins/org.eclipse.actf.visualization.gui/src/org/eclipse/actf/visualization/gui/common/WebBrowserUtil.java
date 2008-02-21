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

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;
import org.eclipse.actf.accservice.swtbridge.IServiceProvider;
import org.eclipse.actf.util.win32.COMUtil;
import org.eclipse.actf.util.win32.WindowUtil;
import org.eclipse.swt.internal.ole.win32.GUID;
import org.eclipse.swt.internal.ole.win32.IDispatch;
import org.eclipse.swt.internal.ole.win32.IUnknown;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;


public class WebBrowserUtil {

    private static final GUID IID_IHTMLElement = COMUtil.IIDFromString("{3050f1ff-98b5-11cf-bb82-00aa00bdce0b}"); //$NON-NLS-1$
    
    private static final String[] BROWSER_CLASSNAMES = new String[] { 
        "Internet Explorer_Server",  //$NON-NLS-1$
        "MozillaContentWindowClass"  //$NON-NLS-1$
    };
    
    public static boolean isBrowserFrame(int hwndFrame) {
        String className = WindowUtil.GetWindowClassName(hwndFrame);
        if( "MozillaUIWindowClass".equals(className) ||  //$NON-NLS-1$
            "IEFrame".equals(className) ) { //$NON-NLS-1$
            return true;
        }
        return false;
    }
    
	public static boolean isBrowser(AccessibleObject accObject) {
		return isBrowserClass(accObject.getClassName());
	}
	
    public static boolean isBrowserClass(String className) {
        for( int i=0; i<BROWSER_CLASSNAMES.length; i++ ) {
            if( BROWSER_CLASSNAMES[i].equals(className) ) {
                return true;
            }
        }
        return false;
    }
    
	public static Variant getHTMLElementFromObject(Object objUnknown) {
        if( objUnknown instanceof AccessibleObject ) {
            objUnknown = ((AccessibleObject)objUnknown).getIAccessible();
        }
        if( objUnknown instanceof IUnknown ) {
            int[] ppvServiceProvider = new int[1];
            if( OLE.S_OK == ((IUnknown)objUnknown).QueryInterface(IServiceProvider.IID,ppvServiceProvider) ) {
                IServiceProvider sp = new IServiceProvider(ppvServiceProvider[0]);
                try {
                    int[] ppvObject = new int[1]; 
                    if( OLE.S_OK== sp.QueryService(IID_IHTMLElement,IID_IHTMLElement, ppvObject) ) {
                        return new Variant(new IDispatch(ppvObject[0]));
                    }
                    return null;
                }
                finally {
                    sp.Release();
                }
            }
        }
        return null;
    }

	public static String getHtmlAttribute(Object objUnknown, String name) {
	    Variant varElement = getHTMLElementFromObject(objUnknown);
	    if( null != varElement && OLE.VT_DISPATCH==varElement.getType() ) {
	        try {
	            OleAutomation automation = varElement.getAutomation();
	            int[] idAttr = automation.getIDsOfNames(new String[]{name});
	            if( null != idAttr  ) {
	                Variant varAttr = automation.getProperty(idAttr[0]);
	                if( null != varAttr ) {
	                    if( null!=varAttr && OLE.VT_BSTR==varAttr.getType() ) {
	                        return varAttr.getString();
	                    }
	                }
	            }
	        }
	        finally {
	            varElement.dispose();
	        }
	    }
	    return null;
	}
}
