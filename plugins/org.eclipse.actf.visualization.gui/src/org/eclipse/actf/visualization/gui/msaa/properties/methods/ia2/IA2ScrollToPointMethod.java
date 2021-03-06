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
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IA2CoordTypeField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IntegerField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;


public class IA2ScrollToPointMethod extends MethodData {

	private Accessible2 accessible2;
	private IA2CoordTypeField coordTypeField;
	private IntegerField xField,yField;


	public IA2ScrollToPointMethod(Accessible2 accessible2) {
		super("scrollToPoint", true); //$NON-NLS-1$
		this.accessible2 = accessible2;
		coordTypeField = new IA2CoordTypeField("coordinateType",IA2.IA2_COORDTYPE_SCREEN_RELATIVE); //$NON-NLS-1$
		xField = new IntegerField("x",0); //$NON-NLS-1$
		yField = new IntegerField("y",0); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{coordTypeField,xField,yField});
	}

    public boolean invoke() {
        int coordType = coordTypeField.getIntValue();
        int x = xField.getIntValue();
        int y = yField.getIntValue();
        boolean success = accessible2.scrollToPoint(coordType, x, y);
        result = formatResult(success); 
        return true;
    }
}
