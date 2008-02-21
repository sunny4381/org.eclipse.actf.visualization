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

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;
import org.eclipse.actf.accservice.swtbridge.MSAA;
import org.eclipse.actf.visualization.gui.common.WebBrowserUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;




public class FlashUtil {

    private static Color colorFlash = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
    private static Color colorInvisibleFlash = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
    
    public static boolean isFlash(AccessibleObject accObject) {
    	return isFlashClass(accObject.getClassName()) || isInvisibleFlash(accObject);
    }

    public static boolean isInvisibleFlash(AccessibleObject accObject) {
        if( MSAA.ROLE_SYSTEM_CLIENT == accObject.getAccRole() ) {
            String description = accObject.getAccDescription();
            if( null != description && description.startsWith("PLUGIN: type=") ) { //$NON-NLS-1$
                return null != WebBrowserUtil.getHtmlAttribute(accObject,"WMode"); //$NON-NLS-1$
            }
        }
        return false;
    }

    public static boolean isFlashClass(String className) {
        return "MacromediaFlashPlayerActiveX".equals(className) ||  //$NON-NLS-1$
               "ShockwaveFlashPlugin".equals(className);  //$NON-NLS-1$
    }

    public static Object[] getFlashElements(Object inputElement) {
        FlashFinder finder = new FlashFinder();
        if (inputElement instanceof Object[]) {
            finder.find((Object[]) inputElement);
        } else {
            finder.find(new Object[] { inputElement });
        }
        return finder.getResults();
    }

    public static Color getFlashBackground(AccessibleObject accObject) {
        switch( accObject.getAccRole() ) {
            case MSAA.ROLE_SYSTEM_WINDOW:
                if( FlashUtil.isFlash(accObject) ) {
                    return colorFlash;
                }
                break;
            case MSAA.ROLE_SYSTEM_CLIENT:
                if( FlashUtil.isInvisibleFlash(accObject) ) {
                    return colorInvisibleFlash;
                }
                break;
        }
        return null;
    }
}
