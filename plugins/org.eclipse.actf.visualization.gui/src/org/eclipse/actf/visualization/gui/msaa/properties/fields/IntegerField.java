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


public class IntegerField extends AbstractStringField {

	private int currValue;
    public int minValue, maxValue;
	
	public IntegerField(String labelText, int initValue, int minValue, int maxValue) {
        super(labelText);
		this.currValue = initValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public IntegerField(String labelText, int initValue, int minValue) {
		this(labelText,initValue,minValue,Integer.MAX_VALUE);
	}
	
	public IntegerField(String labelText, int initValue) {
		this(labelText,initValue,Integer.MIN_VALUE,Integer.MAX_VALUE);
	}

	public int getIntValue() {
		return currValue;
	}

	protected String getStringValue() {
		return Integer.toString(currValue);
	}
	
	protected boolean validateControl() {
		String text = getText();
		if( null != text ) {
			try {
				int value = Integer.parseInt(text);
				return (value>=minValue) && (value <=maxValue);
			}
			catch( Exception e ) {
			}
		}
		return false;
	}
	
	public boolean update() {
		if( validateControl() ) {
			try {
				currValue = Integer.parseInt(getText());
				return true;
			}
			catch( Exception e ) {
			}
		}
		return false;
	}
	

}
