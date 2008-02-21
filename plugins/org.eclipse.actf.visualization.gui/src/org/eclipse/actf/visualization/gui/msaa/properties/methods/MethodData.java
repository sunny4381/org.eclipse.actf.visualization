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
package org.eclipse.actf.visualization.gui.msaa.properties.methods;

import java.text.MessageFormat;

import org.eclipse.actf.accservice.swtbridge.IA2;
import org.eclipse.actf.visualization.gui.Messages;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IA2CoordTypeField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IA2ScrollTypeField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IA2TextBoundaryTypeField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IntegerField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.MSAASELFLAGField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.TextField;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;



public abstract class MethodData {

	private String title;
	private boolean supportInvoke;
    private AbstractInputField[] inputFields = new AbstractInputField[0];
	protected Object result = PRESS_ENTER_MSG;
    
    private static final String PRESS_ENTER_MSG = Messages.getString("props.pressEnter"); //$NON-NLS-1$
    private static final String PRESS_OK_MSG = Messages.getString("props.pressOK"); //$NON-NLS-1$
    private static final String INVALID_FIELD_MSG = Messages.getString("props.invalid"); //$NON-NLS-1$
    private static final String VALID_RANGE_MSG = Messages.getString("props.range"); //$NON-NLS-1$
	private static final String SUCCESS_MSG = Messages.getString("props.success"); //$NON-NLS-1$
	private static final String FAIL_MSG = Messages.getString("props.fail"); //$NON-NLS-1$
	
	protected String getInvalidArgMessage(AbstractInputField field) {
        String label = field.getLabelText();
        if( field instanceof IntegerField ) {
            int minValue = ((IntegerField)field).minValue; 
            int maxValue = ((IntegerField)field).maxValue;
            if( minValue <= maxValue ) {
                return MessageFormat.format(VALID_RANGE_MSG,new Object[]{label,new Integer(minValue),new Integer(maxValue)});
            }
        }
		return MessageFormat.format(INVALID_FIELD_MSG,new Object[]{label});
	}
	
	public MethodData(String title, boolean supportInvoke) {
		this.title = title;
		this.supportInvoke = supportInvoke;
	}

    public void setInputFields(AbstractInputField[] inputFields) {
        this.inputFields = inputFields;
    }
	public final String getTitle() {
		return title;
	}
	
	public final boolean canInvole() {
		return supportInvoke;
	}
	
	public boolean invoke() {
		return supportInvoke;
	}
	
    public Object getResult() {
        return result;
    }
    
	protected final void createControl(Composite parent) {
        for( int i=0; i<inputFields.length; i++ ) {
            inputFields[i].createLabelAndControl(parent);
        }
    }
	
	protected final boolean validate() {
        for( int i=0; i<inputFields.length; i++ ) {
            AbstractInputField field = inputFields[i];
            if( !field.validate() ) { 
                return false;
            }
        }
		return true;
	}
	
	protected final String getMessage() {
        for( int i=0; i<inputFields.length; i++ ) {
            AbstractInputField field = inputFields[i];
            if( !field.validate() ) { 
                return getInvalidArgMessage(field);
            }
        }
		return PRESS_OK_MSG;
	}
	
	protected final void setModifyListener(ModifyListener listener) {
        for( int i=0; i<inputFields.length; i++ ) {
            AbstractInputField field = inputFields[i];
            field.setModifyListener(listener);
        }
	}
	
	protected final void update() {
        for( int i=0; i<inputFields.length; i++ ) {
            AbstractInputField field = inputFields[i];
            field.update();
        }
	}
    
    protected final String getParameters() {
        StringBuffer sb = new StringBuffer();
        for( int i=0; i<inputFields.length; i++ ) {
            if( i > 0 ) {
                sb.append(", "); //$NON-NLS-1$
            }
            AbstractInputField field = inputFields[i];
            sb.append(field.getLabelText());
            sb.append("="); //$NON-NLS-1$
            if( field instanceof IA2CoordTypeField ) {
                int coordType = ((IA2CoordTypeField)field).getIntValue();
                sb.append(IA2.getCoordTypeText(coordType));
            }
            else if( field instanceof IA2ScrollTypeField ) {
                int scrollType = ((IA2ScrollTypeField)field).getIntValue();
                sb.append(IA2.getScrollTypeText(scrollType));
            }
            else if( field instanceof IA2TextBoundaryTypeField ) {
                int textBoundaryType = ((IA2TextBoundaryTypeField)field).getIntValue();
                sb.append(IA2.getTextBoundaryTypeText(textBoundaryType));
            }
            else if( field instanceof MSAASELFLAGField ) {
                int flag = ((MSAASELFLAGField)field).getIntValue();
                sb.append("0x"+Integer.toHexString(flag)); //$NON-NLS-1$
            }
            else if( field instanceof IntegerField ) {
                int value = ((IntegerField)field).getIntValue();
                sb.append(Integer.toString(value));
            }
            else if( field instanceof TextField ) {
                String value = ((TextField)field).getStringValue();
                sb.append(T(value));
            }
        }
        return sb.toString();
    }
    
    protected final String formatResult(boolean success) {
        return formatResult(success ? SUCCESS_MSG : FAIL_MSG);
    }
    
    protected final String formatResult(String rc) {
        String param = getParameters();
        if( param.length()>0 ) {
            return rc + " ["+param+"]"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return rc;
    }
    
    protected static String T(String str) {
        if( null==str ) {
            return "(null)"; //$NON-NLS-1$
        }
        return "\""+str+"\""; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
