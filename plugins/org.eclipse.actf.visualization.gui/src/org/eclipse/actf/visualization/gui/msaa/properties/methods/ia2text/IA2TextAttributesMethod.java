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
package org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2text;

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleText;
import org.eclipse.actf.accservice.swtbridge.ia2.TextSegment;
import org.eclipse.actf.visualization.gui.msaa.properties.AttributePropertySource;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.TextOffsetField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;


public class IA2TextAttributesMethod extends MethodData {

	private AccessibleText accessibleText;
	private TextOffsetField offsetField; 
	
	public IA2TextAttributesMethod(AccessibleText accessibleText) {
		super("attributes",true); //$NON-NLS-1$
		this.accessibleText = accessibleText;
		offsetField = new TextOffsetField("offset",0,accessibleText); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{offsetField});
	}

	public Object getResult() {
		int offset = offsetField.getIntValue();
        TextSegment segment = accessibleText.getCharacterAttributes(offset);
        if( null != segment && null != segment.text ) {
			return new AttributePropertySource(segment.text,formatResult("startOffset="+segment.start+", endOffset="+segment.end)); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return null;
	}
}
