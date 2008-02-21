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

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleApplication;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class AccessibleApplicationPropertySource extends AbstractPropertyInvokeSource implements IPropertySource {

	private AccessibleApplication accessibleApplication;

	private static final String
	    PID_IA2_APP_appName = "appName", //$NON-NLS-1$
	    PID_IA2_APP_appVersion = "appVersion", //$NON-NLS-1$
	    PID_IA2_APP_toolkitName = "toolkitName", //$NON-NLS-1$
	    PID_IA2_APP_toolkitVersion = "toolkitVersion";  //$NON-NLS-1$
	
	private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] 
	{
        new PropertyDescriptor(PID_IA2_APP_appName,"appName"),          //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_APP_appVersion,"appVersion"),       //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_APP_toolkitName,"toolkitName"),      //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_APP_toolkitVersion,"toolkitVersion")    //$NON-NLS-1$
	};
    private static final IPropertyDescriptor[] descriptorsEx = new IPropertyDescriptor[] {};
	
	public AccessibleApplicationPropertySource(AccessibleApplication input) {
		this.accessibleApplication = input;
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
		if( PID_IA2_APP_appName.equals(id) ) {
            strValue = accessibleApplication.getApplicationName();
		}
		else if( PID_IA2_APP_appVersion.equals(id) ) {
            strValue = accessibleApplication.getApplicationVersion();
		}
		else if( PID_IA2_APP_toolkitName.equals(id) ) {
            strValue = accessibleApplication.getToolkitName();
		}
		else if( PID_IA2_APP_toolkitVersion.equals(id) ) {
            strValue = accessibleApplication.getToolkitVersion();
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
