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

package org.eclipse.actf.visualization.gui.ui.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;



public abstract class AccessibleObjectSelectionProvider implements ISelectionProvider {

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
    }

    public ISelection getSelection() {
        Object selected = getSelectedAccessibleObject(); 
        if( null != selected ) {
            
            return new StructuredSelection(selected);
        }
        return StructuredSelection.EMPTY;
    }

    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
    }

    public void setSelection(ISelection selection) {
    }
    
    public abstract Object getSelectedAccessibleObject(); 

}
