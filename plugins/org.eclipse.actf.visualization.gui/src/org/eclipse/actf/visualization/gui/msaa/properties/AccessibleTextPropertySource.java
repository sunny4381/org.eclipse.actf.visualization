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

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleText;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2text.IA2TextAddSelectionMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2text.IA2TextAttributesMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2text.IA2TextCharacterExtentsMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2text.IA2TextOffsetAtPointMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2text.IA2TextRemoveSelectionMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2text.IA2TextScrollSubstringToMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2text.IA2TextScrollSubstringToPointMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2text.IA2TextSelectionMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2text.IA2TextSetCaretOffsetMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2text.IA2TextSetSelectionMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2text.IA2TextTextAfterOffsetMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2text.IA2TextTextAtOffsetMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2text.IA2TextTextBeforeOffsetMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2text.IA2TextTextMethod;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;


public class AccessibleTextPropertySource extends AbstractPropertyInvokeSource implements IPropertySource {

	private AccessibleText accessibleText;

	private static final String
	    PID_IA2_TEXT_caretOffset = "caretOffset", //$NON-NLS-1$
	    PID_IA2_TEXT_characterExtents = "characterExtents", //$NON-NLS-1$
	    PID_IA2_TEXT_nSelections = "nSelections", //$NON-NLS-1$
	    PID_IA2_TEXT_nCharacters = "nCharacters", //$NON-NLS-1$
	    PID_IA2_TEXT_attributes = "attributes", //$NON-NLS-1$
        PID_IA2_TEXT_addSelection = "addSelection", //$NON-NLS-1$
        PID_IA2_TEXT_offsetAtPoint = "offsetAtPoint", //$NON-NLS-1$
        PID_IA2_TEXT_selection = "selection", //$NON-NLS-1$
        PID_IA2_TEXT_text = "text", //$NON-NLS-1$
        PID_IA2_TEXT_textBeforeOffset = "textBeforeOffset", //$NON-NLS-1$
        PID_IA2_TEXT_textAfterOffset = "textAfterOffset", //$NON-NLS-1$
        PID_IA2_TEXT_textAtOffset = "textAtOffset", //$NON-NLS-1$
        PID_IA2_TEXT_removeSelection = "removeSelection", //$NON-NLS-1$
        PID_IA2_TEXT_setCaretOffset = "setCaretOffset", //$NON-NLS-1$
        PID_IA2_TEXT_setSelection = "setSelection", //$NON-NLS-1$
        PID_IA2_TEXT_scrollSubstringTo = "scrollSubstringTo", //$NON-NLS-1$
        PID_IA2_TEXT_scrollSubstringToPoint = "scrollSubstringToPoint"; //$NON-NLS-1$
	
	private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] 
	{
		new PropertyDescriptor(PID_IA2_TEXT_caretOffset,"caretOffset"),      //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_TEXT_characterExtents,"characterExtents"), //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_TEXT_nSelections,"nSelections"),      //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_TEXT_nCharacters,"nCharacters"),       //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TEXT_attributes,"attributes"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TEXT_text,"text"),  //$NON-NLS-1$
	};
	private static final IPropertyDescriptor[] descriptorsEx = new IPropertyDescriptor[] 
    {
        new PropertyDescriptor(PID_IA2_TEXT_addSelection,"addSelection"),  //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TEXT_removeSelection,"removeSelection"),  //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TEXT_setCaretOffset,"setCaretOffset"),  //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TEXT_setSelection,"setSelection"),  //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TEXT_offsetAtPoint,"offsetAtPoint"),  //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TEXT_selection,"selection"),  //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TEXT_textBeforeOffset,"textBeforeOffset"),  //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TEXT_textAfterOffset,"textAfterOffset"),  //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TEXT_textAtOffset,"textAtOffset"),  //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TEXT_scrollSubstringTo,"scrollSubstringTo"),  //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TEXT_scrollSubstringToPoint,"scrollSubstringToPoint"),  //$NON-NLS-1$
    };
	
	public AccessibleTextPropertySource(AccessibleText input) {
		this.accessibleText = input;
		addMethodData(PID_IA2_TEXT_attributes, new IA2TextAttributesMethod(input));
        addMethodData(PID_IA2_TEXT_characterExtents, new IA2TextCharacterExtentsMethod(input));
        addMethodData(PID_IA2_TEXT_addSelection, new IA2TextAddSelectionMethod(input));
        addMethodData(PID_IA2_TEXT_offsetAtPoint, new IA2TextOffsetAtPointMethod(input));
        addMethodData(PID_IA2_TEXT_removeSelection, new IA2TextRemoveSelectionMethod(input));
        addMethodData(PID_IA2_TEXT_scrollSubstringTo, new IA2TextScrollSubstringToMethod(input));
        addMethodData(PID_IA2_TEXT_scrollSubstringToPoint, new IA2TextScrollSubstringToPointMethod(input));
        addMethodData(PID_IA2_TEXT_selection, new IA2TextSelectionMethod(input));
        addMethodData(PID_IA2_TEXT_setCaretOffset, new IA2TextSetCaretOffsetMethod(input));
        addMethodData(PID_IA2_TEXT_setSelection, new IA2TextSetSelectionMethod(input));
        addMethodData(PID_IA2_TEXT_text, new IA2TextTextMethod(input));
        addMethodData(PID_IA2_TEXT_textAfterOffset, new IA2TextTextAfterOffsetMethod(input));
        addMethodData(PID_IA2_TEXT_textAtOffset, new IA2TextTextAtOffsetMethod(input));
        addMethodData(PID_IA2_TEXT_textBeforeOffset, new IA2TextTextBeforeOffsetMethod(input));
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
		if( PID_IA2_TEXT_caretOffset.equals(id) ) {
            strValue = Integer.toString(accessibleText.getCaretPosition());
		}
		else if( PID_IA2_TEXT_nSelections.equals(id) ) {
            strValue = Integer.toString(accessibleText.getSelectionCount());
		}
		else if( PID_IA2_TEXT_nCharacters.equals(id) ) {
            strValue = Integer.toString(accessibleText.getCharacterCount());
		}
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
