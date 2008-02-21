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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public abstract class AbstractStringField extends AbstractInputField {

	private Text textField;
	private String curText;
	
	
	public AbstractStringField(String labelText) {
        this(labelText,null);
	}
	
	public AbstractStringField(String labelText,String initText) {
        super(labelText);
		this.curText = initText;
	}

	protected void createControl(Composite parent) {
		textField = new Text(parent,SWT.SINGLE | SWT.BORDER);
		textField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		textField.setFont(parent.getFont());
		textField.setText(getStringValue());
		textField.addModifyListener(this);
	}
	
	protected String getText() {
		if( null != textField ) {
			return textField.getText();
		}
		return null;
	}
	
	protected String getStringValue() {
		return curText;
	}
	
	protected boolean validateControl() {
		return null != getText();
	}
}
