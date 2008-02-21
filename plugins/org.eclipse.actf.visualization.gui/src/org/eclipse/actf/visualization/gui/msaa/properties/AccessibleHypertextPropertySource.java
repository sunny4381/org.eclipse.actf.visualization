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

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleHypertext;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleText;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2hypertext.IA2HypertextHyperlinkIndexMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2hypertext.IA2HypertextHyperlinkMethod;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;


public class AccessibleHypertextPropertySource extends AbstractPropertyInvokeSource implements IPropertySource {

	private AccessibleHypertext accessibleHypertext;

	private static final String
	    PID_IA2_HYPERTEXT_nHyperlinks = "nHyperlinks", //$NON-NLS-1$
	    PID_IA2_HYPERTEXT_hyperlink = "hyperlink", //$NON-NLS-1$
	    PID_IA2_HYPERTEXT_hyperlinkIndex = "hyperlinkIndex"; //$NON-NLS-1$
	
	private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] 
	{
		new PropertyDescriptor(PID_IA2_HYPERTEXT_nHyperlinks,"nHyperlinks"), //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_HYPERTEXT_hyperlink,"hyperlink"),       //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_HYPERTEXT_hyperlinkIndex,"hyperlinkIndex")    //$NON-NLS-1$
	};
	private static final IPropertyDescriptor[] descriptorsEx = new IPropertyDescriptor[] {};
	
	public AccessibleHypertextPropertySource(AccessibleHypertext input, AccessibleText text) {
		this.accessibleHypertext = input;
        addMethodData(PID_IA2_HYPERTEXT_hyperlink, new IA2HypertextHyperlinkMethod(input));
        addMethodData(PID_IA2_HYPERTEXT_hyperlinkIndex, new IA2HypertextHyperlinkIndexMethod(input,text));
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
		if( PID_IA2_HYPERTEXT_nHyperlinks.equals(id) ) {
            strValue = Integer.toString(accessibleHypertext.getHyperLinkCount());
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
