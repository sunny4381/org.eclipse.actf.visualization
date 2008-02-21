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
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IA2CoordTypeField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IntegerField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;
import org.eclipse.swt.graphics.Point;




public class IA2TextOffsetAtPointMethod extends MethodData {

    private AccessibleText accessibleText;
    private IntegerField xField, yField;
    private IA2CoordTypeField coordTypeField;
    
    public IA2TextOffsetAtPointMethod(AccessibleText accessibleText) {
        super("offsetAtPoint",true); //$NON-NLS-1$
        this.accessibleText = accessibleText;
        xField = new IntegerField("x",0); //$NON-NLS-1$
        yField = new IntegerField("y",0); //$NON-NLS-1$
        coordTypeField = new IA2CoordTypeField("coordType",IA2.IA2_COORDTYPE_SCREEN_RELATIVE); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{xField,yField,coordTypeField});
    }
    
    public boolean invoke() {
        int x = xField.getIntValue();
        int y = yField.getIntValue();
        int coordType = coordTypeField.getIntValue();
        int offset = accessibleText.getIndexAtPoint(new Point(x,y), coordType);
        result = formatResult("offset="+offset); //$NON-NLS-1$
        return true;
    }
}
