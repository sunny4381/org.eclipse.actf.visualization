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
import org.eclipse.actf.accservice.swtbridge.MSAA;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.MSAASELFLAGField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;


public class MsaaSelectMethod extends MethodData {

	private AccessibleObject accObject;
	private MSAASELFLAGField selFlagField;
	
	public MsaaSelectMethod(AccessibleObject accObject) {
		super("accSelect",true); //$NON-NLS-1$
		this.accObject = accObject;
		selFlagField = new MSAASELFLAGField("flagSelect", MSAA.SELFLAG_TAKEFOCUS); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{selFlagField});
	}

    public boolean invoke() {
        int flagSelect = selFlagField.getIntValue();
        boolean success = accObject.select(flagSelect);
        result = formatResult(success); 
        return true;
    }
}
