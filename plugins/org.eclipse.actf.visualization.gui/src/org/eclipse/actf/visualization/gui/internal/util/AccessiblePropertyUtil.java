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
package org.eclipse.actf.visualization.gui.internal.util;

import org.eclipse.actf.visualization.gui.msaa.properties.AccessibleObjectPropertySource;
import org.eclipse.actf.visualization.gui.ui.views.PropertyTreeContentProvider;
import org.eclipse.actf.visualization.gui.ui.views.PropertyTreeEntry;
import org.eclipse.jface.viewers.ITreeContentProvider;


public class AccessiblePropertyUtil {

	private static final ITreeContentProvider provider = new PropertyTreeContentProvider();
	
	public static String[][] getPropertyStrings(Object input) {
		Object[] rootElements = provider.getElements(input);
		for( int i=0; i<rootElements.length; i++ ) {
			if( rootElements[i] instanceof PropertyTreeEntry ) {
				PropertyTreeEntry entry = (PropertyTreeEntry)rootElements[i];
				if( AccessibleObjectPropertySource.PID_IA1.equals(entry.getId()) ) {
					Object[] children = provider.getChildren(entry);
					String[][] result = new String[children.length][2];
					for( int j=0; j<children.length; j++ ) {
						PropertyTreeEntry child = (PropertyTreeEntry)children[j];
						result[j][0]=child.getDisplayName();
						result[j][1]=child.getValueAsString();
					}
					return result;
				}
			}
		}
		return new String[0][];
	}
}
