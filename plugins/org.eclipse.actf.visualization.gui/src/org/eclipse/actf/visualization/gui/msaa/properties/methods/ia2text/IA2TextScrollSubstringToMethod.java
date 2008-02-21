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
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IA2ScrollTypeField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.TextOffsetField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;




public class IA2TextScrollSubstringToMethod extends MethodData {

    private AccessibleText accessibleText;
    private TextOffsetField startIndexField, endIndexField;
    private IA2ScrollTypeField scrollTypeField;
    
    public IA2TextScrollSubstringToMethod(AccessibleText accessibleText) {
        super("scrollSubstringTo",true); //$NON-NLS-1$
        this.accessibleText = accessibleText;
        startIndexField = new TextOffsetField("startIndex",0,accessibleText); //$NON-NLS-1$
        endIndexField = new TextOffsetField("endIndex",0,accessibleText); //$NON-NLS-1$
        scrollTypeField = new IA2ScrollTypeField("scrollType",IA2.IA2_SCROLL_TYPE_TOP_LEFT); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{startIndexField,endIndexField,scrollTypeField});
    }
    
    public boolean invoke() {
        int startIndex = startIndexField.getIntValue();
        int endIndex = endIndexField.getIntValue();
        int scrollType = scrollTypeField.getIntValue();
        boolean success = accessibleText.scrollSubstringTo(startIndex, endIndex, scrollType);
        result = formatResult(success);
        return true;
    }
}
