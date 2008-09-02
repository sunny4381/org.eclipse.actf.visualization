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



public class IAccessibleAction extends IUnknown {
    public static final GUID IID = COMUtil.IIDFromString("{B70D9F59-3B5A-4dba-AB9E-22012F607DF5}"); //$NON-NLS-1$
    
    int address;
    public IAccessibleAction(int address) {
        super(address);
        this.address = address;
    }

    public int nActions(int pnActions) {
        return COMUtil.VtblCall(3, address, pnActions); 
    }
    public int doAction(int actionIndex) {
        return COMUtil.VtblCall(4, address, actionIndex); 
    }
    public int get_description(int actionIndex, int pszDescription) {
        return COMUtil.VtblCall(5, address, actionIndex, pszDescription); 
    }
    public int get_keyBinding(int actionIndex, int nMaxBinding, int ppszKeyBinding, int pnBinding) {
        return COMUtil.VtblCall(6, address, actionIndex, nMaxBinding, ppszKeyBinding, pnBinding); 
    }
    public int get_name(int actionIndex, int pszName) {
        return COMUtil.VtblCall(7, address, actionIndex, pszName); 
    }
    public int get_localizedName(int actionIndex, int pszLocalizedName) {
        return COMUtil.VtblCall(8, address, actionIndex, pszLocalizedName); 
    }
}
