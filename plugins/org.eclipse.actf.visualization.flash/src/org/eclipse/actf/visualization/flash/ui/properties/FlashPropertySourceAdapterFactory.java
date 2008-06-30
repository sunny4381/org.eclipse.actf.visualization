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
package org.eclipse.actf.visualization.flash.ui.properties;

import org.eclipse.actf.model.flash.IASNode;
import org.eclipse.actf.model.flash.IFlashPlayer;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

public class FlashPropertySourceAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (IPropertySource.class == adapterType) {
			if (adaptableObject instanceof IFlashPlayer) {
				return new FlashPlayerPropertySource(
						(IFlashPlayer) adaptableObject);
			}
			if (adaptableObject instanceof IASNode) {
				return new FlashNodePropertySource((IASNode) adaptableObject);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Class[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}

}
