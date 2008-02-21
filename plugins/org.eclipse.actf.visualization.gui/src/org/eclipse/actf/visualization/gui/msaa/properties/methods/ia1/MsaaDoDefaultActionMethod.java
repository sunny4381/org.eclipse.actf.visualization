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
package org.eclipse.actf.visualization.gui.msaa.properties.methods.ia1;

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;


public class MsaaDoDefaultActionMethod extends MethodData {

	private AccessibleObject accObject;
	
	public MsaaDoDefaultActionMethod(AccessibleObject accObject) {
		super(null/*"accDoDefaultAction()"*/,true);
		this.accObject = accObject;
        setInputFields(new AbstractInputField[]{});
	}

	public boolean invoke() {
        boolean success = accObject.doDefaultAction();
		result = formatResult(success);
		return true;
	}
}
