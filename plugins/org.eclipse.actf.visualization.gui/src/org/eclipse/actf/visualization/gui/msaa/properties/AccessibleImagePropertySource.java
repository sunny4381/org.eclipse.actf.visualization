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

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleImage;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2image.IA2ImagePositionMethod;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;


public class AccessibleImagePropertySource extends AbstractPropertyInvokeSource implements IPropertySource {

	private AccessibleImage accessibleImage;

	private static final String
	    PID_IA2_IMAGE_description = "description", //$NON-NLS-1$
	    PID_IA2_IMAGE_imagePosition = "imagePosition", //$NON-NLS-1$
	    PID_IA2_IMAGE_imageSize = "imageSize"; //$NON-NLS-1$
	
	private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] 
	{
		new PropertyDescriptor(PID_IA2_IMAGE_description,"description"), //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_IMAGE_imagePosition,"imagePosition"),       //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_IMAGE_imageSize,"imageSize")        //$NON-NLS-1$
	};
    private static final IPropertyDescriptor[] descriptorsEx = new IPropertyDescriptor[] {};
	
	public AccessibleImagePropertySource(AccessibleImage input) {
		this.accessibleImage = input;
        addMethodData(PID_IA2_IMAGE_imagePosition, new IA2ImagePositionMethod(input));
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
		if( PID_IA2_IMAGE_description.equals(id) ) {
            strValue = accessibleImage.getAccessibleImageDescription();
		}
		else if( PID_IA2_IMAGE_imageSize.equals(id) ) {
            Point point = accessibleImage.getAccessibleImageSize();
            if( null != point ) {
                strValue = point.x+" x "+point.y; //$NON-NLS-1$
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
