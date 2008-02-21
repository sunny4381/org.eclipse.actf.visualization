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

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleValue;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class AccessibleValuePropertySource extends AbstractPropertyInvokeSource implements IPropertySource {

	private AccessibleValue accessibleValue;

	private static final String
	    PID_IA2_VALUE_currentValue = "currentValue", //$NON-NLS-1$
	    PID_IA2_VALUE_maximumValue = "maximumValue", //$NON-NLS-1$
	    PID_IA2_VALUE_minimumValue = "minimumValue"; //$NON-NLS-1$
	
	private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] 
	{
		new PropertyDescriptor(PID_IA2_VALUE_currentValue,"currentValue"),      //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_VALUE_maximumValue,"maximumValue"),      //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_VALUE_minimumValue,"minimumValue")      //$NON-NLS-1$
	};
    private static final IPropertyDescriptor[] descriptorsEx = new IPropertyDescriptor[] {};
	
	public AccessibleValuePropertySource(AccessibleValue input) {
		this.accessibleValue = input;
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
        Object var = null;
		if( PID_IA2_VALUE_currentValue.equals(id) ) {
			var = accessibleValue.getCurrentValue();
		}
		else if( PID_IA2_VALUE_maximumValue.equals(id) ) {
            var = accessibleValue.getMaximumValue();
		}
		else if( PID_IA2_VALUE_minimumValue.equals(id) ) {
            var = accessibleValue.getMinimumValue();
		}
		return null==var ? "null" : var.toString(); //$NON-NLS-1$
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {
	}

	public void setPropertyValue(Object id, Object value) {
	}
}
