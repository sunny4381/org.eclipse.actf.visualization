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

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.views.properties.IPropertySource;

public class PropertyTreeContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		if( parentElement instanceof PropertyTreeEntry ) {
			return ((PropertyTreeEntry)parentElement).getChildren();
		}
		return new Object[0];
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		if( element instanceof PropertyTreeEntry ) {
			return ((PropertyTreeEntry)element).hasChildren();
		}
		return false;
	}

	public Object[] getElements(Object inputElement) {
		Object adapter = Platform.getAdapterManager().getAdapter(inputElement, IPropertySource.class);
		if( null != adapter ) {
			return PropertyTreeEntry.getElements(null,(IPropertySource)adapter);
		}
		return new Object[0];
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
	
}
