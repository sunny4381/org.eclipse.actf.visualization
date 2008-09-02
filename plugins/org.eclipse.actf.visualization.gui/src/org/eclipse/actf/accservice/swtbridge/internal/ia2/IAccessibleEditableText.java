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



public class IAccessibleEditableText extends IUnknown {
    public static final GUID IID = COMUtil.IIDFromString("{A59AA09A-7011-4b65-939D-32B1FB5547E3}"); //$NON-NLS-1$
    
    int address;
    public IAccessibleEditableText(int address) {
        super(address);
        this.address = address;
    }
    
    public int copyText(int startOffset, int endOffset) {
        return COMUtil.VtblCall(3, address, startOffset, endOffset);
    }
    public int deleteText(int startOffset, int endOffset) {
        return COMUtil.VtblCall(4, address, startOffset, endOffset);
    }
    public int insertText(int offset, int pszText) {
        return COMUtil.VtblCall(5, address, offset, pszText);
    }
    public int cutText(int startOffset, int endOffset) {
        return COMUtil.VtblCall(6, address, startOffset, endOffset);
    }
    public int pasteText(int offset) {
        return COMUtil.VtblCall(7, address, offset);
    }
    public int replaceText(int startOffset, int endOffset, int pszText) {
        return COMUtil.VtblCall(8, address, startOffset, endOffset, pszText);
    }
    public int setAttributes(int startOffset, int endOffset, int pszAttributes) {
        return COMUtil.VtblCall(9, address, startOffset, endOffset, pszAttributes);
    }
}
