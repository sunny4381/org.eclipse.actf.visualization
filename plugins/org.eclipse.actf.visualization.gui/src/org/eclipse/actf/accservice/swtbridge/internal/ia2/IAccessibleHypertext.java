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



public class IAccessibleHypertext extends IAccessibleText {
    public static final GUID IID = COMUtil.IIDFromString("{6B4F8BBF-F1F2-418a-B35E-A195BC4103B9}"); //$NON-NLS-1$
    
    int address;
    public IAccessibleHypertext(int address) {
        super(address);
        this.address = address;
    }

    public int get_nHyperlinks(int pHyperlinkCount) {
        return COMUtil.VtblCall(22, address, pHyperlinkCount);
    }
    public int get_hyperlink(int index, int pdispHyperlink) {
        return COMUtil.VtblCall(23, address, index, pdispHyperlink);
    }
    public int get_hyperlinkIndex(int charIndex, int pHyperlinkIndex) {
        return COMUtil.VtblCall(24, address, charIndex, pHyperlinkIndex);
    }
}
