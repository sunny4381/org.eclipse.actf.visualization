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
import org.eclipse.actf.visualization.gui.msaa.properties.fields.TextOffsetField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;




public class IA2TextScrollSubstringToPointMethod extends MethodData {

    private AccessibleText accessibleText;
    private TextOffsetField startIndexField, endIndexField;
    private IA2CoordTypeField coordinateTypeField;
    private IntegerField xField,yField;
    
    public IA2TextScrollSubstringToPointMethod(AccessibleText accessibleText) {
        super("scrollSubstringToPoint",true); //$NON-NLS-1$
        this.accessibleText = accessibleText;
        startIndexField = new TextOffsetField("startIndex",0,accessibleText); //$NON-NLS-1$
        endIndexField = new TextOffsetField("endIndex",0,accessibleText); //$NON-NLS-1$
        coordinateTypeField = new IA2CoordTypeField("coordinateType",IA2.IA2_COORDTYPE_SCREEN_RELATIVE); //$NON-NLS-1$
        xField = new IntegerField("x",0); //$NON-NLS-1$
        yField = new IntegerField("y",0); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{startIndexField,endIndexField,coordinateTypeField,xField,yField});
    }
    
    public boolean invoke() {
        int startIndex = startIndexField.getIntValue();
        int endIndex = endIndexField.getIntValue();
        int coordinateType = coordinateTypeField.getIntValue();
        int x = xField.getIntValue();
        int y = yField.getIntValue();
        boolean success = accessibleText.scrollSubstringToPoint(startIndex, endIndex, coordinateType, x, y);
        result = formatResult(success);
        return true;
    }
}
