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
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.TextSelectionField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;




public class IA2TextRemoveSelectionMethod extends MethodData {

    private AccessibleText accessibleText;
    private TextSelectionField selectionIndexField;
    
    public IA2TextRemoveSelectionMethod(AccessibleText accessibleText) {
        super("removeSelection",true); //$NON-NLS-1$
        this.accessibleText = accessibleText;
        selectionIndexField = new TextSelectionField("selectionIndex",0,accessibleText); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{selectionIndexField});
    }
    
    public boolean invoke() {
        int selectionIndex = selectionIndexField.getIntValue();
        boolean success = accessibleText.removeSelection(selectionIndex);
        result = formatResult(success);
        return true;
    }
}
