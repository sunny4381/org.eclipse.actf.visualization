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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class MethodInvokeDialog extends Dialog {

	private MethodData data;
	private Text messages;
	
	public MethodInvokeDialog(Shell parentShell, MethodData data) {
		super(parentShell);
		this.data = data;
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(data.getTitle());
	}

	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite)super.createDialogArea(parent);
		Composite inputArea = new Composite(comp,SWT.NONE);
		inputArea.setLayout(new GridLayout(2,false));
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.minimumWidth = 300;
		inputArea.setLayoutData(gd);
		data.createControl(inputArea);
		return comp;
	}
    
    protected Control createMessageArea(Composite parent) {
        Composite messageArea = new Composite(parent,SWT.NONE);
        GridLayout gl = new GridLayout();
        gl.marginWidth *= 2;
        gl.marginHeight = 0;
        messageArea.setLayout(gl);
        messageArea.setLayoutData(new GridData(GridData.FILL_BOTH));
        messages = new Text(messageArea,SWT.NONE|SWT.READ_ONLY);
        messages.setLayoutData(new GridData(GridData.FILL_BOTH));
        return messageArea;
    }
    
    protected Control createButtonBar(Composite parent) {
        createMessageArea(parent);
        Control buttonBar = super.createButtonBar(parent); 
        data.setModifyListener(new ModifyListener(){
            public void modifyText(ModifyEvent e) {
                updateMessage();
            }
        });
        updateMessage();
        return buttonBar;
    }

    protected void updateMessage() {
		messages.setText(data.getMessage());
        getButton(IDialogConstants.OK_ID).setEnabled(data.validate());
	}
	
	protected void okPressed() {
		data.update();
		super.okPressed();
	}
}
