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

import org.eclipse.actf.accservice.swtbridge.IA2;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleText;
import org.eclipse.actf.visualization.gui.msaa.properties.AttributePropertySource;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IA2CoordTypeField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.TextOffsetField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;
import org.eclipse.swt.graphics.Rectangle;


public class IA2TextCharacterExtentsMethod extends MethodData {

	private AccessibleText accessibleText;
	private TextOffsetField offsetField;
	private IA2CoordTypeField coordTypeField;
	
	public IA2TextCharacterExtentsMethod(AccessibleText accessibleText) {
		super("characterExtents",true); //$NON-NLS-1$
		this.accessibleText = accessibleText;
		offsetField = new TextOffsetField("offset",0,accessibleText); //$NON-NLS-1$
		coordTypeField = new IA2CoordTypeField("coordType",IA2.IA2_COORDTYPE_SCREEN_RELATIVE); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{offsetField,coordTypeField});
	}

	public Object getResult() {
		int offset = offsetField.getIntValue();
		int coordType = coordTypeField.getIntValue();
        Rectangle extents = accessibleText.getCharacterBounds(offset,coordType);
        if( null != extents ) {
        	AttributePropertySource attrSource = new AttributePropertySource(null,formatResult(true));
        	attrSource.put("x", new Integer(extents.x)); //$NON-NLS-1$
        	attrSource.put("y", new Integer(extents.y)); //$NON-NLS-1$
        	attrSource.put("width", new Integer(extents.width)); //$NON-NLS-1$
        	attrSource.put("height", new Integer(extents.height)); //$NON-NLS-1$
        	return attrSource;
        }
        return null;
	}
}
