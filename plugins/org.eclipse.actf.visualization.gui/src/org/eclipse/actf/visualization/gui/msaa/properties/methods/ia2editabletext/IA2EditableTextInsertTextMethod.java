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
import org.eclipse.actf.visualization.gui.msaa.properties.fields.TextField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.TextOffsetField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;




public class IA2EditableTextInsertTextMethod extends MethodData {

    private AccessibleEditableText editableText;
    private TextOffsetField offsetField;
    private TextField textField;
    
    public IA2EditableTextInsertTextMethod(AccessibleEditableText editableText, AccessibleText accessibleText) {
        super("insertText",true); //$NON-NLS-1$
        this.editableText = editableText;
        offsetField = new TextOffsetField("offset",0,accessibleText); //$NON-NLS-1$
        textField = new TextField("text",""); //$NON-NLS-1$ //$NON-NLS-2$
        setInputFields(new AbstractInputField[]{offsetField,textField});
    }
    
    public boolean invoke() {
        int offset = offsetField.getIntValue();
        String text = textField.getStringValue();
        boolean success = editableText.insertText(offset, text); 
        result = formatResult(success); 
        return true;
    }
}
