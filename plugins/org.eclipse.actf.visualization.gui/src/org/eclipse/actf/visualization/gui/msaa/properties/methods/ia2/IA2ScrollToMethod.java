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
package org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2;

import org.eclipse.actf.accservice.swtbridge.IA2;
import org.eclipse.actf.accservice.swtbridge.ia2.Accessible2;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IA2ScrollTypeField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;


public class IA2ScrollToMethod extends MethodData {

	private Accessible2 accessible2;
	private IA2ScrollTypeField scrollTypeField;

	public IA2ScrollToMethod(Accessible2 accessible2) {
		super("scrollTo", true); //$NON-NLS-1$
		this.accessible2 = accessible2;
		scrollTypeField = new IA2ScrollTypeField("scrollType", IA2.IA2_SCROLL_TYPE_TOP_LEFT); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{scrollTypeField});
	}

    public boolean invoke() {
        int scrollType = scrollTypeField.getIntValue();
        boolean success = accessible2.scrollTo(scrollType);
        result = formatResult(success); 
        return true;
    }
}
