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
package org.eclipse.actf.visualization.gui.msaa.properties;

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleComponent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class AccessibleComponentPropertySource extends AbstractPropertyInvokeSource implements IPropertySource {

	private AccessibleComponent accessibleComponent;

	private static final String
	    PID_IA2_COMP_locationInParent = "locationInParent", //$NON-NLS-1$
	    PID_IA2_COMP_foreground = "foreground", //$NON-NLS-1$
	    PID_IA2_COMP_background = "background"; //$NON-NLS-1$
	
	private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] 
	{
        new PropertyDescriptor(PID_IA2_COMP_locationInParent,"locationInParent"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_COMP_foreground,"foreground"),       //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_COMP_background,"background")        //$NON-NLS-1$
	};
    private static final IPropertyDescriptor[] descriptorsEx = new IPropertyDescriptor[] {};
	
	public AccessibleComponentPropertySource(AccessibleComponent input) {
		this.accessibleComponent = input;
	}

	public Object getEditableValue() {
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return descriptors;
	}

    public IPropertyDescriptor[] getPropertyDescriptorsExtra() {
        return descriptorsEx;
    }
    
	public Object getPropertyValue(Object id) {
        Object result = super.getPropertyValue(id);
        if( null != result ) {
            return result;
        }   
		String strValue = null;
		if( PID_IA2_COMP_locationInParent.equals(id) ) {
            Point location = accessibleComponent.getLocation();
            if( null != location ) {
                strValue = location.x +", "+location.y; //$NON-NLS-1$
            }
		}
		else if( PID_IA2_COMP_foreground.equals(id) ) {
			int value = accessibleComponent.getForeground();
            strValue = value+" (0x"+Integer.toHexString(value)+")"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		else if( PID_IA2_COMP_background.equals(id) ) {
			int value = accessibleComponent.getBackground();
            strValue = value+" (0x"+Integer.toHexString(value)+")"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return null==strValue ? "null" : strValue; //$NON-NLS-1$
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {
	}

	public void setPropertyValue(Object id, Object value) {
	}
}
