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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public abstract class AbstractEnumIntegerField extends AbstractInputField {

	private int currValue;
	private Combo comboField;
	private Object[][] labelsAndValues = getLabelsAndValues();
	
	
	public AbstractEnumIntegerField(String labelText, int initValue) {
        super(labelText);
		this.currValue = initValue;
	}
	
	public int getIntValue() {
		return currValue;
	}

	protected void createControl(Composite parent) {
		comboField = new Combo(parent,SWT.DROP_DOWN | SWT.SIMPLE | SWT.BORDER | SWT.READ_ONLY);
		comboField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		comboField.setFont(parent.getFont());
		Integer intVal = new Integer(currValue);
		for( int i=0; i<labelsAndValues.length; i++ ) {
			comboField.add(labelsAndValues[i][0].toString(), i);
			if( intVal.equals(labelsAndValues[i][1]) ) {
				comboField.select(i);
			}
		}
		comboField.addModifyListener(this);
	}
	
	public boolean update() {
		if( null != comboField ) {
			int index = comboField.getSelectionIndex();
			if( index >= 0 ) {
				if( labelsAndValues[index][1] instanceof Integer ) {
					currValue = ((Integer)labelsAndValues[index][1]).intValue();
				}
			}
		}
		return false;
	}

    protected abstract Object[][] getLabelsAndValues();

}
