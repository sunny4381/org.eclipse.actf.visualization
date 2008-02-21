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

public class ObjectArrayPropertySource implements IPropertySource {

	private Object[] objArray;
	private String editableText;
	
	public ObjectArrayPropertySource(Object[] strArray) {
		objArray = strArray;
	}
	
	public ObjectArrayPropertySource(Object[] strArray, String editableText) {
		this(strArray);
		this.editableText = editableText;
	}

	public ObjectArrayPropertySource(int[] intArray) {
		objArray = new Object[intArray.length];
		for( int i=0; i<intArray.length; i++ ) {
			objArray[i] = new Integer(intArray[i]);
		}
	}

	public ObjectArrayPropertySource(int[] intArray, String editableText) {
		this(intArray);
		this.editableText = editableText;
	}
	
	public Object getEditableValue() {
		if( null != editableText ) {
			return editableText;
		}
		return objectArrayToString(objArray);
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[objArray.length];
		for( int i=0; i<objArray.length; i++ ) {
			descriptors[i] = new PropertyDescriptor(new Integer(i),"["+i+"]"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return descriptors;
	}

	public Object getPropertyValue(Object id) {
		if( id instanceof Integer ) {
			Object obj = objArray[((Integer)id).intValue()];
			if( obj instanceof AccessibleObject ) {
				return new AccessibleObjectPropertySource((AccessibleObject)obj);
			}
			else if( obj instanceof AccessibleRelation ) {
				return new AccessibleRelationPropertySource((AccessibleRelation)obj);
			}
			return obj.toString();
		}
		return null;
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {
	}

	public void setPropertyValue(Object id, Object value) {
	}

    private static String objectArrayToString(Object[] objArray) {
        String strRet = null;
        if( null != objArray ) {
            for( int i=0; i<objArray.length; i++ ) {
                if( i==0 ) {
                    strRet = ""; //$NON-NLS-1$
                }
                else {
                    strRet += "; "; //$NON-NLS-1$
                }
                strRet += /*"#"+i+": "+*/objArray[i]; //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return strRet;
    }
}
