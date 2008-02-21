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
import org.eclipse.actf.accservice.swtbridge.ia2.TextSegment;
import org.eclipse.actf.visualization.gui.msaa.properties.AttributePropertySource;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IA2TextBoundaryTypeField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.TextOffsetField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;




public class IA2TextTextBeforeOffsetMethod extends MethodData {

    private AccessibleText accessibleText;
    private TextOffsetField offsetField;
    private IA2TextBoundaryTypeField boundaryTypeField;
    
    public IA2TextTextBeforeOffsetMethod(AccessibleText accessibleText) {
        super("textBeforeOffset",true); //$NON-NLS-1$
        this.accessibleText = accessibleText;
        offsetField = new TextOffsetField("offset",0,accessibleText); //$NON-NLS-1$
        boundaryTypeField = new IA2TextBoundaryTypeField("boundaryType",IA2.IA2_TEXT_BOUNDARY_CHAR); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{offsetField,boundaryTypeField});
    }
    
    public boolean invoke() {
        int offset = offsetField.getIntValue();
        int boundaryType = boundaryTypeField.getIntValue();
        TextSegment segment = accessibleText.getTextBeforeIndex(offset, boundaryType);
        if( null != segment ) {
            AttributePropertySource attrSource = new AttributePropertySource(null,formatResult(true));
            attrSource.put("startOffset", new Integer(segment.start)); //$NON-NLS-1$
            attrSource.put("endOffset", new Integer(segment.end)); //$NON-NLS-1$
            attrSource.put("text", segment.text); //$NON-NLS-1$
            result = attrSource;
        }
        else {
            result = formatResult(false);
        }
        return true;
    }
}
