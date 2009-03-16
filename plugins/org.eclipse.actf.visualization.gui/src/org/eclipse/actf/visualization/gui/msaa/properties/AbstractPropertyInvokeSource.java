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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodInvokeDialog;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.IPropertyDescriptor;


public abstract class AbstractPropertyInvokeSource implements IPropertyInvoke {

	private Map<Object, MethodData> methodMap = new HashMap<Object, MethodData>();
	
	protected void addMethodData(Object id, MethodData methodData) {
		methodMap.put(id, methodData);
	}
	
	private MethodData getMethodData(Object id) {
		Object data = methodMap.get(id);
		if( data instanceof MethodData ) {
			return (MethodData)data;
		}
		return null;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptorsExtra() {
		return new IPropertyDescriptor[0];
	}
	
	public Object getPropertyValue(Object id) {
		MethodData methodData = getMethodData(id);
		if( null != methodData ) {
			return methodData.getResult();
		}
		return null;
	}
	
	public final boolean canInvoke(Object id) {
		MethodData methodData = getMethodData(id);
		if( null != methodData ) {
			return methodData.canInvole();
		}
		return false;
	}


	public final boolean invoke(Object id, Shell shell) {
		MethodData methodData = getMethodData(id);
		if( null != methodData && methodData.canInvole() ) {
			if( null != methodData.getTitle() ) {
				Dialog dialog = new MethodInvokeDialog(shell,methodData);
				if( Window.OK != dialog.open() ) {
					return false;
				}
			}
			methodData.invoke();
			return true;
		}
		return false;
	}

}
