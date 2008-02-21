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


public class TextField extends AbstractStringField {

	private String currValue;
    public int miniLength, maxLength;
	
	public TextField(String labelText, String initValue, int miniLength, int maxLength) {
        super(labelText);
		this.currValue = initValue;
		this.miniLength = miniLength;
		this.maxLength = maxLength;
	}
	
	public TextField(String labelText, String initValue, int miniLength) {
		this(labelText,initValue,miniLength,Integer.MAX_VALUE);
	}
	
	public TextField(String labelText, String initValue) {
		this(labelText, initValue,0,Integer.MAX_VALUE);
	}

	public String getStringValue() {
		return currValue;
	}

	protected boolean validateControl() {
		String text = getText();
		if( null != text ) {
			try {
				int length = text.length();
				return (length>=miniLength) && (length <=maxLength);
			}
			catch( Exception e ) {
			}
		}
		return false;
	}
	
	public boolean update() {
		if( validateControl() ) {
			try {
				currValue = getText();
				return true;
			}
			catch( Exception e ) {
			}
		}
		return false;
	}
	

}
