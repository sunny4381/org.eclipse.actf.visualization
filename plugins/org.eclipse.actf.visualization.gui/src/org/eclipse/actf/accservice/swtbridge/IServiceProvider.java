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

package org.eclipse.actf.accservice.swtbridge;

import org.eclipse.actf.util.win32.COMUtil;
import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.GUID;
import org.eclipse.swt.internal.ole.win32.IUnknown;
import org.eclipse.swt.internal.win32.OS;




public class IServiceProvider extends IUnknown {
    public static final GUID IID = COMUtil.IIDFromString("{6d5140c1-7436-11ce-8034-00aa006009fa}"); //$NON-NLS-1$

    public IServiceProvider(int address) {
        super(address);
    }

    public int QueryService(int pGuidService, int pRiid, int ppvObject[]) {
        return COM.VtblCall(3, getAddress(), pGuidService, pRiid, ppvObject);
    }
    
    public int QueryService(GUID guidService, GUID riid, int ppvObject[]) {
        int pGuidService = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, GUID.sizeof);
        int pRiid = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, GUID.sizeof);
        try {
            COM.MoveMemory(pGuidService, guidService, GUID.sizeof);
            COM.MoveMemory(pRiid, riid, GUID.sizeof);
            return QueryService(pGuidService, pRiid, ppvObject);
        }
        finally {
            OS.GlobalFree(pGuidService);
            OS.GlobalFree(pRiid);
        }
    }
}
