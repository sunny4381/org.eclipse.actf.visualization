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
package org.eclipse.actf.visualization.gui.msaa.properties;

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleEditableText;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleText;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2editabletext.IA2EditableTextCopyTextMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2editabletext.IA2EditableTextCutTextMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2editabletext.IA2EditableTextDeleteTextMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2editabletext.IA2EditableTextInsertTextMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2editabletext.IA2EditableTextPasteTextMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2editabletext.IA2EditableTextReplaceTextMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2editabletext.IA2EditableTextSetAttributesTextMethod;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;


public class AccessibleEditableTextPropertySource extends AbstractPropertyInvokeSource implements IPropertySource {

	private AccessibleEditableText accessibleEditableText;

    private static final String
        PID_IA2_EDITABLE_TEXT_copyText = "copyText", //$NON-NLS-1$
        PID_IA2_EDITABLE_TEXT_deleteText = "deleteText", //$NON-NLS-1$
        PID_IA2_EDITABLE_TEXT_insertText = "insertText", //$NON-NLS-1$
        PID_IA2_EDITABLE_TEXT_cutText = "cutText", //$NON-NLS-1$
        PID_IA2_EDITABLE_TEXT_pasteText = "pasteText", //$NON-NLS-1$
        PID_IA2_EDITABLE_TEXT_replaceText = "replaceText", //$NON-NLS-1$
        PID_IA2_EDITABLE_TEXT_setAttributes = "setAttributes"; //$NON-NLS-1$
    
	private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] 
	{
	};
    private static final IPropertyDescriptor[] descriptorsEx = new IPropertyDescriptor[] 
    {
        new PropertyDescriptor(PID_IA2_EDITABLE_TEXT_copyText,"copyText"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_EDITABLE_TEXT_deleteText,"deleteText"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_EDITABLE_TEXT_insertText,"insertText"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_EDITABLE_TEXT_cutText,"cutText"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_EDITABLE_TEXT_pasteText,"pasteText"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_EDITABLE_TEXT_replaceText,"replaceText"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_EDITABLE_TEXT_setAttributes,"setAttributes"), //$NON-NLS-1$
    };
	
	public AccessibleEditableTextPropertySource(AccessibleEditableText input, AccessibleText text) {
		this.accessibleEditableText = input;
        addMethodData(PID_IA2_EDITABLE_TEXT_copyText,new IA2EditableTextCopyTextMethod(input,text));
        addMethodData(PID_IA2_EDITABLE_TEXT_deleteText,new IA2EditableTextDeleteTextMethod(input,text));
        addMethodData(PID_IA2_EDITABLE_TEXT_insertText,new IA2EditableTextInsertTextMethod(input,text));
        addMethodData(PID_IA2_EDITABLE_TEXT_cutText,new IA2EditableTextCutTextMethod(input,text));
        addMethodData(PID_IA2_EDITABLE_TEXT_pasteText,new IA2EditableTextPasteTextMethod(input,text));
        addMethodData(PID_IA2_EDITABLE_TEXT_replaceText,new IA2EditableTextReplaceTextMethod(input,text));
        addMethodData(PID_IA2_EDITABLE_TEXT_setAttributes,new IA2EditableTextSetAttributesTextMethod(input,text));
	}

	public Object getEditableValue() {
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return descriptors;
	}
    
    public IPropertyDescriptor[] getPropertyDescriptorsExtra() {
        return descriptorsEx;
    }

	public Object getPropertyValue(Object id) {
        Object result = super.getPropertyValue(id);
        if( null != result ) {
            return result;
        }   
		String strValue = null;
		return null==strValue ? "null" : strValue; //$NON-NLS-1$
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {
	}

	public void setPropertyValue(Object id, Object value) {
	}
}
