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
package org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2image;

import org.eclipse.actf.accservice.swtbridge.IA2;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleImage;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IA2CoordTypeField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;
import org.eclipse.swt.graphics.Point;


public class IA2ImagePositionMethod extends MethodData {

	private AccessibleImage accessibleImage;
	private IA2CoordTypeField coordTypeField;
	
	public IA2ImagePositionMethod(AccessibleImage accessibleImage) {
		super("imagePosition",true); //$NON-NLS-1$
		this.accessibleImage = accessibleImage;
		coordTypeField = new IA2CoordTypeField("coordType",IA2.IA2_COORDTYPE_SCREEN_RELATIVE); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{coordTypeField});
	}

	public Object getResult() {
		int coordType = coordTypeField.getIntValue();
        Point point = accessibleImage.getAccessibleImagePosition(coordType);
        if( null != point ) {
            return point.x+", "+point.y; //$NON-NLS-1$
        }
        return null;
	}
}
