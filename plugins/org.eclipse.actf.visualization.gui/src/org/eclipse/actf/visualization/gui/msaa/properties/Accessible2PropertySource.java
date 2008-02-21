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

import java.util.Locale;

import org.eclipse.actf.accservice.swtbridge.IA2;
import org.eclipse.actf.accservice.swtbridge.MSAA;
import org.eclipse.actf.accservice.swtbridge.ia2.Accessible2;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2.IA2ExtendedStatesMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2.IA2LocalizedExtendedStatesMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2.IA2RelationMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2.IA2RelationsMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2.IA2ScrollToMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2.IA2ScrollToPointMethod;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;


public class Accessible2PropertySource extends AbstractPropertyInvokeSource implements IPropertySource {

	private Accessible2 accessible2;
	
	private static final String
	    PID_IA2_nRelations = "nRelations", //$NON-NLS-1$
        PID_IA2_relations = "relations", //$NON-NLS-1$
        PID_IA2_relation = "relation", //$NON-NLS-1$
	    PID_IA2_role = "role", //$NON-NLS-1$
	    PID_IA2_groupPosition = "groupPosition", //$NON-NLS-1$
	    PID_IA2_states = "states", //$NON-NLS-1$
	    PID_IA2_extendedRole = "extendedRole", //$NON-NLS-1$
	    PID_IA2_localizedExtendedRole = "localizedExtendedRole", //$NON-NLS-1$
	    PID_IA2_nExtendedStates = "nExtendedStates", //$NON-NLS-1$
	    PID_IA2_extendedStates = "extendedStates", //$NON-NLS-1$
	    PID_IA2_localizedExtendedStates = "localizedExtendedStates", //$NON-NLS-1$
	    PID_IA2_uniqueID = "uniqueID", //$NON-NLS-1$
	    PID_IA2_windowHandle = "windowHandle", //$NON-NLS-1$
	    PID_IA2_indexInParent = "indexInParent", //$NON-NLS-1$
	    PID_IA2_locale = "locale", //$NON-NLS-1$
	    PID_IA2_attributes = "attributes", //$NON-NLS-1$
	    PID_IA2_scrollTo = "scrollTo", //$NON-NLS-1$
	    PID_IA2_scrollToPoint = "scrollToPoint"; //$NON-NLS-1$
	
	
	private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] 
 	{
        new PropertyDescriptor(PID_IA2_nRelations,"nRelations"),               //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_relations,"relations"),                //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_role,"role"),            //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_groupPosition,"groupPosition"),            //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_states,"states"),                   //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_extendedRole,"extendedRole"),             //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_localizedExtendedRole,"localizedExtendedRole"),    //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_nExtendedStates,"nExtendedStates"),          //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_extendedStates,"extendedStates"),           //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_localizedExtendedStates,"localizedExtendedStates"),  //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_uniqueID,"uniqueID"),                 //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_windowHandle,"windowHandle"),             //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_indexInParent,"indexInParent"),            //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_locale,"locale"),                   //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_attributes,"attributes")                //$NON-NLS-1$
 	};
	private static final IPropertyDescriptor[] descriptorsEx = new IPropertyDescriptor[] 
	{
        new PropertyDescriptor(PID_IA2_relation,"relation"),                //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_scrollTo,"scrollTo"),                //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_scrollToPoint,"scrollToPoint")                //$NON-NLS-1$
	};
	
	
	public Accessible2PropertySource(Accessible2 input) {
		this.accessible2 = input;
		addMethodData(PID_IA2_scrollTo, new IA2ScrollToMethod(input));
		addMethodData(PID_IA2_scrollToPoint, new IA2ScrollToPointMethod(input));
        addMethodData(PID_IA2_relations, new IA2RelationsMethod(input));
        addMethodData(PID_IA2_extendedStates, new IA2ExtendedStatesMethod(input));
        addMethodData(PID_IA2_localizedExtendedStates, new IA2LocalizedExtendedStatesMethod(input));
        addMethodData(PID_IA2_relation, new IA2RelationMethod(input));
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
		if( PID_IA2_nRelations.equals(id) ) {
            strValue = Integer.toString(accessible2.getAccessibleRelationCount());
		}
		else if( PID_IA2_role.equals(id) ) {
			int ia2Role = accessible2.getAccessibleRole();
            strValue = MSAA.getRoleText(ia2Role)+" (0x"+Integer.toHexString(ia2Role)+")"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		else if( PID_IA2_groupPosition.equals(id) ) {
            final int[] pos = accessible2.getGroupPosition();
            if( null != pos ) {
            	AttributePropertySource attrSource = new AttributePropertySource(null,pos[0]+", "+pos[1]+" ,"+pos[2]); //$NON-NLS-1$ //$NON-NLS-2$
            	attrSource.put("GroupLevel", new Integer(pos[0])); //$NON-NLS-1$
            	attrSource.put("SimilarItemsInGroup", new Integer(pos[1])); //$NON-NLS-1$
            	attrSource.put("PositionInGroup", new Integer(pos[2])); //$NON-NLS-1$
            	return attrSource;
            }
		} 
		else if( PID_IA2_states.equals(id) ) {
			int ia2States = accessible2.getStates();
            strValue = "0x"+Integer.toHexString(ia2States); //$NON-NLS-1$
			String[] strArray = IA2.getStateTextAsArray(ia2States);
			if( strArray.length > 0 ) {
				if( strArray.length > 1 ) {
					return new ObjectArrayPropertySource(strArray,strValue);
				}
				strValue = strArray[0];
			}
		} 
		else if( PID_IA2_extendedRole.equals(id) ) {
            strValue = accessible2.getExtendedRole();
		} 
		else if( PID_IA2_localizedExtendedRole.equals(id) ) {
            strValue = accessible2.getLocalizedExtendedRole();
		} 
		else if( PID_IA2_nExtendedStates.equals(id) ) {
            strValue = Integer.toString(accessible2.getExtendedStateCount());
		} 
		else if( PID_IA2_uniqueID.equals(id) ) {
            strValue = "0x"+Integer.toHexString(accessible2.getUniqueID()); //$NON-NLS-1$
		} 
		else if( PID_IA2_windowHandle.equals(id) ) {
            strValue = "0x"+Integer.toHexString(accessible2.getWindowHandle()); //$NON-NLS-1$
		} 
		else if( PID_IA2_indexInParent.equals(id) ) {
            strValue = Integer.toString(accessible2.getAccessibleIndexInParent());
		} 
		else if( PID_IA2_locale.equals(id) ) {
            Locale locale = accessible2.getLocale();
            if( null != locale ) {
				return new AttributePropertySource(
								"Language:"+locale.getLanguage()+ //$NON-NLS-1$
								";Country:"+locale.getCountry()+ //$NON-NLS-1$
								";Variant:"+locale.getVariant(), //$NON-NLS-1$
								locale.toString());
            }
		} 
		else if( PID_IA2_attributes.equals(id) ) {
			strValue = accessible2.getAttributes();
			if( null != strValue ) {
				return new AttributePropertySource(strValue);
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
