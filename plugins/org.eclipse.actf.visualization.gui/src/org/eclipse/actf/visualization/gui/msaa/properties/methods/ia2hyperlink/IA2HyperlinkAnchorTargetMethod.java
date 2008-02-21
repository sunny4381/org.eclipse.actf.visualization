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
package org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2hyperlink;

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleHyperlink;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IntegerField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;
import org.eclipse.swt.ole.win32.Variant;


public class IA2HyperlinkAnchorTargetMethod extends MethodData {

	private AccessibleHyperlink accessibleHyperlink;
	private IntegerField indexField; 
	
	public IA2HyperlinkAnchorTargetMethod(AccessibleHyperlink accessibleHyperlink) {
		super("anchorTarget",true); //$NON-NLS-1$
		this.accessibleHyperlink = accessibleHyperlink;
		indexField = new IntegerField("index",0,0); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{indexField});
	}

	public Object getResult() {
		int index = indexField.getIntValue();
        Variant varAnchor = accessibleHyperlink.getAccessibleActionObject(index);
        if( null != varAnchor ) {
        	try {
        		return varAnchor.toString();
        	}
        	finally {
                varAnchor.dispose();
        	}
        }
        return null;
	}
}
