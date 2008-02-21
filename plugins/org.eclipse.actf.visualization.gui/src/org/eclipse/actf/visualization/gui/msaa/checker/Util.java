/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.gui.msaa.checker;

import java.util.List;
import java.util.Vector;

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;
import org.eclipse.actf.accservice.swtbridge.MSAA;

public class Util {
    public static String getRoleInfo(AccessibleObject ao) {
        return ao.getAccRole() + " (" + ao.getRoleText() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static AccessibleObject[] getVisibleChildren(AccessibleObject ao) {
        if (ao == null)
            return null;

        AccessibleObject[] accChildren = ao.getChildren();
        List<AccessibleObject> childList = new Vector<AccessibleObject>();
        for (int i = 0; i < accChildren.length; i++) {
            try {
                int accState = accChildren[i].getAccState();
                if (0 == (accState & MSAA.STATE_INVISIBLE)) {
                    childList.add(accChildren[i]);
                }
            } catch (Exception e) {
            }
        }
        AccessibleObject[] vChildren = new AccessibleObject[childList.size()];
        childList.toArray(vChildren);
        return vChildren;
    }

    public static AccessibleObject getFirstVisibleChildren(AccessibleObject ao) {
        if (ao == null)
            return null;
        AccessibleObject[] children = getVisibleChildren(ao);
        if (children.length == 0)
            return null;
        return children[0];
    }
}
