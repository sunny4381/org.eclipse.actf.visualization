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
package org.eclipse.actf.visualization.gui.ui.views;

import org.eclipse.actf.visualization.gui.msaa.properties.IPropertyInvoke;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;


public class PropertyTreeEntry {

	private IPropertySource source;
	private IPropertyDescriptor descriptor;
	private PropertyTreeEntry parent;
	private Object id;
	private String path;
	private int color = -1;
	
	public PropertyTreeEntry(PropertyTreeEntry parent, IPropertySource propertySource,IPropertyDescriptor propertyDescriptor) {
		this.parent = parent;
		this.source = propertySource;
		this.descriptor = propertyDescriptor;
		this.id = propertyDescriptor.getId();
		path = null == parent ? "" : parent.path+ "/"; //$NON-NLS-1$ //$NON-NLS-2$
		path += id.toString();
	}

	public String getDisplayName() {
		return descriptor.getDisplayName();
	}
	
	public String getValueAsString() {
		Object value = getValue();
		if( null == value ) {
			return "null"; //$NON-NLS-1$
		}
		if( value instanceof IPropertySource ) {
			value = ((IPropertySource)value).getEditableValue();
		}
		if (value == null) {
			return null;
		}
		ILabelProvider provider = descriptor.getLabelProvider();
		if (provider == null) {
			return value.toString();
		}
		String text = provider.getText(value);
		if (text == null) {
			return "";//$NON-NLS-1$
		}
		return text;
	}
	
	
	public Object getId() {
		return id;
	}
	public IPropertySource getPropertySource() {
		return source;
	}
	public String getPath() {
		return path;
	}
	public Object getValue() {
		return null != id ? source.getPropertyValue(id) : null;
	}
	public boolean canInvoke() {
		if( null != id && source instanceof IPropertyInvoke ) {
			return ((IPropertyInvoke)source).canInvoke(id);
		}
		return false;
	}
	
	public PropertyTreeEntry getParent() {
		return parent;
	}
	
	public boolean hasChildren() {
        return getChildren().length > 0;
	}
	
	public Object[] getChildren() {
		Object value = getValue();
		if( value instanceof IPropertySource ) {
			return getElements(this,(IPropertySource)value); 
		}
		return new Object[0];
	}
	
	public void setColor(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public static Object[] getElements(PropertyTreeEntry parent, IPropertySource source) {
		IPropertyDescriptor[] base = source.getPropertyDescriptors();
		IPropertyDescriptor[] extra = new IPropertyDescriptor[0];
		if( source instanceof IPropertyInvoke ) {
			extra = ((IPropertyInvoke)source).getPropertyDescriptorsExtra();
		}
		Object[] elements = new Object[base.length+extra.length];
		for( int i=0; i<base.length; i++ ) {
			elements[i] = new PropertyTreeEntry(parent,source,base[i]);
		}
		for( int i=0; i<extra.length; i++ ) {
			elements[base.length+i] = new PropertyTreeEntry(parent,source,extra[i]);
		}
		return elements;
	}

	public boolean equals(Object o) {
		if( super.equals(o) ) {
			return true;
		}
		if( o instanceof PropertyTreeEntry ) {
			return this.path.equals(((PropertyTreeEntry)o).path);
		}
		return false;
	}
}
