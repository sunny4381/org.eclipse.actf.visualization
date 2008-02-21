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

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleHyperlink;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2hyperlink.IA2HyperlinkAnchorMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2hyperlink.IA2HyperlinkAnchorTargetMethod;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;


public class AccessibleHyperlinkPropertySource extends AbstractPropertyInvokeSource implements IPropertySource {

	private AccessibleHyperlink accessibleHyperlink;

	private static final String
	    PID_IA2_HYPERLINK_anchor = "anchor", //$NON-NLS-1$
	    PID_IA2_HYPERLINK_anchorTarget = "anchorTarget", //$NON-NLS-1$
	    PID_IA2_HYPERLINK_startIndex = "startIndex", //$NON-NLS-1$
	    PID_IA2_HYPERLINK_endIndex = "endIndex", //$NON-NLS-1$
	    PID_IA2_HYPERLINK_valid = "valid"; //$NON-NLS-1$
	
	private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] 
	{
        new PropertyDescriptor(PID_IA2_HYPERLINK_anchor,"anchor"),       //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_HYPERLINK_anchorTarget,"anchorTarget"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_HYPERLINK_startIndex,"startIndex"),   //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_HYPERLINK_endIndex,"endIndex"),     //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_HYPERLINK_valid,"valid")         //$NON-NLS-1$
	};
    private static final IPropertyDescriptor[] descriptorsEx = new IPropertyDescriptor[] {};
	
	public AccessibleHyperlinkPropertySource(AccessibleHyperlink input) {
		this.accessibleHyperlink = input;
        addMethodData(PID_IA2_HYPERLINK_anchor, new IA2HyperlinkAnchorMethod(input));
        addMethodData(PID_IA2_HYPERLINK_anchorTarget, new IA2HyperlinkAnchorTargetMethod(input));
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
		if( PID_IA2_HYPERLINK_startIndex.equals(id) ) {
            strValue = Integer.toString(accessibleHyperlink.getStartIndex());
		}
		else if( PID_IA2_HYPERLINK_endIndex.equals(id) ) {
            strValue = Integer.toString(accessibleHyperlink.getEndIndex());
		}
		else if( PID_IA2_HYPERLINK_valid.equals(id) ) {
            strValue = Boolean.toString(accessibleHyperlink.isValid());
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
