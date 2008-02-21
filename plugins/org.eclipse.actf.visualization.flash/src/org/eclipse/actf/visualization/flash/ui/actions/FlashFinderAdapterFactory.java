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
package org.eclipse.actf.visualization.flash.ui.actions;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.IActionFilter;

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;

public class FlashFinderAdapterFactory implements IAdapterFactory, IActionFilter {

    public Class[] getAdapterList() {
        return new Class[] { IActionFilter.class };
    }

    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adaptableObject instanceof AccessibleObject && IActionFilter.class == adapterType) {
            return this;
        }
        return null;
    }

    public boolean testAttribute(Object target, String name, String value) {
        if (target instanceof AccessibleObject && "findFlash".equals(name)) { //$NON-NLS-1$
            return new FlashRectFinder(target).IsValid();
        }
        return false;
    }
}
