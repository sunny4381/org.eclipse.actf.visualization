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

package org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2editabletext;

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleEditableText;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleText;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.TextOffsetField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;




public class IA2EditableTextDeleteTextMethod extends MethodData {

    private AccessibleEditableText editableText;
    private TextOffsetField startOffsetField, endOffsetField;
    
    public IA2EditableTextDeleteTextMethod(AccessibleEditableText editableText, AccessibleText accessibleText) {
        super("deleteText",true); //$NON-NLS-1$
        this.editableText = editableText;
        int max = null!=accessibleText ? accessibleText.getCharacterCount() : 0;
        startOffsetField = new TextOffsetField("startOffset",0,accessibleText); //$NON-NLS-1$
        endOffsetField = new TextOffsetField("endOffset",max,accessibleText); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{startOffsetField,endOffsetField});
    }
    
    public boolean invoke() {
        int startOffset = startOffsetField.getIntValue();
        int endOffset = endOffsetField.getIntValue();
        boolean success = editableText.deleteText(startOffset, endOffset);
        result = formatResult(success); 
        return true;
    }
}
