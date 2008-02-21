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

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleAction;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2action.IA2ActionDescriptionMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2action.IA2ActionDoActionMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2action.IA2ActionKeybindingMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2action.IA2ActionLocalizedNameMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2action.IA2ActionNameMethod;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;


public class AccessibleActionPropertySource extends AbstractPropertyInvokeSource implements IPropertySource {

	private AccessibleAction accessibleAction;

	private static final String
        PID_IA2_ACTION_nAction = "nAction", //$NON-NLS-1$
        PID_IA2_ACTION_doAction = "doAction", //$NON-NLS-1$
	    PID_IA2_ACTION_description = "description", //$NON-NLS-1$
	    PID_IA2_ACTION_keyBinding = "keyBinding", //$NON-NLS-1$
	    PID_IA2_ACTION_name = "name", //$NON-NLS-1$
	    PID_IA2_ACTION_localizedName = "localizedName"; //$NON-NLS-1$
	
	private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] 
	{
        new PropertyDescriptor(PID_IA2_ACTION_nAction,"nAction"),          //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_ACTION_description,"description"),      //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_ACTION_keyBinding,"keyBinding"),       //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_ACTION_name,"name"),             //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_ACTION_localizedName,"localizedName")     //$NON-NLS-1$
	};
    private static final IPropertyDescriptor[] descriptorsEx = new IPropertyDescriptor[] 
    {
        new PropertyDescriptor(PID_IA2_ACTION_doAction,"doAction"),          //$NON-NLS-1$
    };
	
	public AccessibleActionPropertySource(AccessibleAction input) {
		this.accessibleAction = input;
        addMethodData(PID_IA2_ACTION_description, new IA2ActionDescriptionMethod(input));
        addMethodData(PID_IA2_ACTION_keyBinding, new IA2ActionKeybindingMethod(input));
        addMethodData(PID_IA2_ACTION_name, new IA2ActionNameMethod(input));
        addMethodData(PID_IA2_ACTION_localizedName, new IA2ActionLocalizedNameMethod(input));
        addMethodData(PID_IA2_ACTION_doAction, new IA2ActionDoActionMethod(input));
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
		if( PID_IA2_ACTION_nAction.equals(id) ) {
            strValue = Integer.toString(accessibleAction.getAccessibleActionCount());
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
