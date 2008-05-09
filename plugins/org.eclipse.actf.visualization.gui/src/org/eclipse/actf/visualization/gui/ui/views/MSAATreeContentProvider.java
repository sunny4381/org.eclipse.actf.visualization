/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.gui.ui.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;
import org.eclipse.actf.accservice.swtbridge.MSAA;
import org.eclipse.actf.util.win32.FlashUtil;
import org.eclipse.actf.util.win32.IAccessibleObject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;


public class MSAATreeContentProvider implements ITreeContentProvider {

    private boolean hideHtml = false;
    private boolean showOffscreen = false;
    
	private static MSAATreeContentProvider instance =  new MSAATreeContentProvider();
	
	public static MSAATreeContentProvider getDefault() {
		if( null == instance ) {
			instance =  new MSAATreeContentProvider();
		}
		return instance;
	}

    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof AccessibleObject) {
            AccessibleObject[] accChildren = ((AccessibleObject) parentElement).getChildren();
            List<AccessibleObject> childList = new Vector<AccessibleObject>();
            for (int i = 0; i < accChildren.length; i++) {
                AccessibleObject accChild = accChildren[i];
                if( null != accChild ) {
                    int accState = accChild.getAccState();
                    if ( 0 == (accState & MSAA.STATE_INVISIBLE)) {
                        childList.add(accChild);
                    }
                    else if ( showOffscreen && 0 != (accState & MSAA.STATE_OFFSCREEN)) {
                        childList.add(accChild);
                    }
                }
            }
            return childList.toArray();
        }
        return new Object[0];
    }

    public Object getParent(Object element) {
        return null;
    }

    public boolean hasChildren(Object element) {
        if (element instanceof AccessibleObject) {
            return ((AccessibleObject) element).getChildCount() > 0;
        }
        return false;
    }

    public Object[] getElements(Object inputElement) {
        Object[] elements;
        if (inputElement instanceof Object[]) {
            elements = (Object[]) inputElement;
        } else {
            elements = getChildren(inputElement);
        }
        if (hideHtml) {
        	ArrayList<IAccessibleObject> result = new ArrayList<IAccessibleObject>();
        	for(Object i : elements){
        		if(i instanceof IAccessibleObject){
        			IAccessibleObject[] flashElements = FlashUtil.getFlashElements((IAccessibleObject)i);
        			for(IAccessibleObject j : flashElements){
        				result.add(j);
        			}
        		}
        	}
            return result.toArray();
        }
        return elements;
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        if (oldInput instanceof Object[]) {
            oldInput = ((Object[]) oldInput)[0];
        }
        if (oldInput instanceof AccessibleObject) {
            try {
                ((AccessibleObject) oldInput).dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

	public boolean isHideHtml() {
		return hideHtml;
	}

	public void setHideHtml(boolean hideHtml) {
		this.hideHtml = hideHtml;
	}

	public boolean isShowOffscreen() {
		return showOffscreen;
	}

	public void setShowOffscreen(boolean showOffscreen) {
		this.showOffscreen = showOffscreen;
		FlashUtil.setShowOffscreen(showOffscreen);
	}
}
