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
package org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2hypertext;

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleHypertext;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleText;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.TextOffsetField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;


public class IA2HypertextHyperlinkIndexMethod extends MethodData {

	private AccessibleHypertext accessibleHypertext;
	private TextOffsetField charIndexField; 
	
	public IA2HypertextHyperlinkIndexMethod(AccessibleHypertext accessibleHyperext, AccessibleText accessibleText) {
		super("hyperlinkIndex",true); //$NON-NLS-1$
		this.accessibleHypertext = accessibleHyperext;
		charIndexField = new TextOffsetField("charIndex",0,accessibleText); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{charIndexField});
	}

	public Object getResult() {
		int charIndex = charIndexField.getIntValue();
        int hyperlinkIndex = accessibleHypertext.getHyperLinkIndex(charIndex);
        return formatResult("hyperlinkIndex="+hyperlinkIndex); //$NON-NLS-1$
	}
}
