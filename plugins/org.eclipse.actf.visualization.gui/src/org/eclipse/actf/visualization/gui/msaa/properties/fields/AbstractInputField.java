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
package org.eclipse.actf.visualization.gui.msaa.properties.fields;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public abstract class AbstractInputField  implements ModifyListener {

	private ModifyListener listener;
    private String labelText;
	protected Label label;
	public static final Color ERROR_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	public static final Color NORMAL_COLOR = null;
	
    public AbstractInputField(String labelText) {
        this.labelText = labelText;
    }
    
	public void setModifyListener(ModifyListener listener) {
		this.listener = listener;
	}
	
	public void modifyText(ModifyEvent e) {
		if( null != listener ) {
			listener.modifyText(e);
		}
	}
    
    public final String getLabelText() {
        return labelText;
    }
	
	public void createLabelAndControl(Composite parent) {
		label = new Label(parent,SWT.NONE);
		label.setText(labelText+":"); //$NON-NLS-1$
		createControl(parent);
	}
	
	public boolean validate() {
		boolean valid = validateControl();
        if( null != label ) {
            if( valid ) {
                label.setForeground(NORMAL_COLOR);
                label.setText(labelText+":"); //$NON-NLS-1$
            }
            else {
                label.setForeground(ERROR_COLOR);
                label.setText("*"+labelText+":"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            label.getParent().layout();
        }
		return valid;
	}

	public boolean update() {
		return true;
	}
	
	protected boolean validateControl() {
		return true;
	}
	
	abstract protected void createControl(Composite parent);


}
