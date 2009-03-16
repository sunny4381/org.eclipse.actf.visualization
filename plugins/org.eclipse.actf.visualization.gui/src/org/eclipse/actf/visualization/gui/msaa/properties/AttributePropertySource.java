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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class AttributePropertySource implements IPropertySource {

	private Map<Object, Object> attrMap = new LinkedHashMap<Object, Object>();
	private String attributes;
	private String editableText;
	
	public AttributePropertySource(String attributes) {
		this.attributes = attributes;
		if( null != attributes ) {
			StringTokenizer st = new StringTokenizer(attributes,";"); //$NON-NLS-1$
	        while( st.hasMoreTokens() ) {
	            String name = st.nextToken();
	            String value = null;
	            int sep = name.indexOf(':');
	            if( sep > 0 ) {
	            	value = name.substring(sep+1).trim();
	                name = name.substring(0,sep).trim();
	            }
	            put(name, value);
	        }
		}
	}
	public AttributePropertySource(String attributes, String editableText) {
		this(attributes);
		this.editableText = editableText;
	}
	
	public void put(Object key, Object value) {
        attrMap.put(key, value);
	}

	public Object getEditableValue() {
		if( null != editableText ) {
			return editableText;
		}
		return attributes;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
		for( Iterator<Object> it = attrMap.keySet().iterator(); it.hasNext(); ) {
			Object name = it.next();
			descriptors.add(new PropertyDescriptor(name,(String)name));
		}
		return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
	}

	public Object getPropertyValue(Object id) {
		return attrMap.get(id);
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {
	}

	public void setPropertyValue(Object id, Object value) {
	}

}
