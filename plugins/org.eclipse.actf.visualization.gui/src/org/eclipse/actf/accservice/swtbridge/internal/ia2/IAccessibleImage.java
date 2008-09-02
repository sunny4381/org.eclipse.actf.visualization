/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.accservice.swtbridge.internal.ia2;

import org.eclipse.actf.util.win32.COMUtil;
import org.eclipse.swt.internal.ole.win32.GUID;
import org.eclipse.swt.internal.ole.win32.IUnknown;



public class IAccessibleImage extends IUnknown {
    public static final GUID IID = COMUtil.IIDFromString("{FE5ABB3D-615E-4f7b-909F-5F0EDA9E8DDE}"); //$NON-NLS-1$
    
    int address;
    public IAccessibleImage(int address) {
        super(address);
        this.address = address;
    }

    public int get_description(int pszDescription) {
        return COMUtil.VtblCall(3, address, pszDescription);
    }
    public int get_imagePosition(int coordinateType, int pX, int pY) {
        return COMUtil.VtblCall(4, address, coordinateType, pX, pY);
    }
    public int get_imageSize(int pWidth, int pHeight) {
        return COMUtil.VtblCall(5, address, pWidth, pHeight);
    }
}
