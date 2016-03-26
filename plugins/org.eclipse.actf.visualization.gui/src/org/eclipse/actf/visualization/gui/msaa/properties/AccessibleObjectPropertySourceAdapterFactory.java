/*******************************************************************************
 * Copyright (c) 2007, 2016 IBM Corporation and Others
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
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

public class AccessibleObjectPropertySourceAdapterFactory implements IAdapterFactory {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (IPropertySource.class == adapterType) {
			if (adaptableObject instanceof AccessibleObject) {
				return new AccessibleObjectPropertySource((AccessibleObject) adaptableObject);
			}
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Class[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}

}
