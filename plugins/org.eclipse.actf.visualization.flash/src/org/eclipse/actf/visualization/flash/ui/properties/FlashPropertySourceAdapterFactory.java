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

import org.eclipse.actf.model.flash.FlashNode;
import org.eclipse.actf.model.flash.FlashPlayer;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;



public class FlashPropertySourceAdapterFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if( IPropertySource.class == adapterType ) {
			if( adaptableObject instanceof FlashPlayer ) {
				return new FlashPlayerPropertySource((FlashPlayer)adaptableObject);
			}
			if( adaptableObject instanceof FlashNode ) {
				return new FlashNodePropertySource((FlashNode)adaptableObject);
			}
		}
		return null;
	}

	public Class[] getAdapterList() {
		return new Class[] {IPropertySource.class };
	}

}
