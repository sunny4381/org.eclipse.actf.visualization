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

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleRelation;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class AccessibleRelationPropertySource extends AbstractPropertyInvokeSource implements IPropertySource {

	private AccessibleRelation accessibleRelation;
	
	private static final String
	    PID_IA2_RELATION_relationType = "relationType", //$NON-NLS-1$
	    PID_IA2_RELATION_localizedRelationType = "localizedRelationType", //$NON-NLS-1$
	    PID_IA2_RELATION_nTarget = "nTarget", //$NON-NLS-1$
	    PID_IA2_RELATION_targets = "targets"; //$NON-NLS-1$

	private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] 
	{
	    new PropertyDescriptor(PID_IA2_RELATION_relationType,"relationType"),               //$NON-NLS-1$
	    new PropertyDescriptor(PID_IA2_RELATION_localizedRelationType,"localizedRelationType"),                //$NON-NLS-1$
	    new PropertyDescriptor(PID_IA2_RELATION_nTarget,"nTarget"),            //$NON-NLS-1$
	    new PropertyDescriptor(PID_IA2_RELATION_targets,"targets"),        //$NON-NLS-1$
	};
    private static final IPropertyDescriptor[] descriptorsEx = new IPropertyDescriptor[] {};
	
	public AccessibleRelationPropertySource(AccessibleRelation accessibleRelation) {
		this.accessibleRelation = accessibleRelation;
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
		if( PID_IA2_RELATION_relationType.equals(id)) {
			strValue = accessibleRelation.getRelationType();
		}
		else if( PID_IA2_RELATION_localizedRelationType.equals(id) ) {
			strValue = accessibleRelation.getLocalizedRelationType();
		}
		else if( PID_IA2_RELATION_nTarget.equals(id) ) {
			strValue = Integer.toString(accessibleRelation.getTargetCount());
		}
		else if( PID_IA2_RELATION_targets.equals(id) ) {
			AccessibleObject[] targets = accessibleRelation.getTargets(AccessibleObjectPropertySource.MAX_CHILD_PROPERTIES);  // TODO: use MethodData
			if( null != targets ) {
				return new ObjectArrayPropertySource(targets);
			}
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
