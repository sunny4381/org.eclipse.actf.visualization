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

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public abstract class AbstractFlagIntegerField extends AbstractInputField {

	private int currValue;
	CheckboxTableViewer viewer;
	
	public AbstractFlagIntegerField(String labelText,int initValue) {
        super(labelText);
		this.currValue = initValue;
	}
	
	public int getIntValue() {
		return currValue;
	}

	protected void createControl(Composite parent) {
		viewer = CheckboxTableViewer.newCheckList(parent, SWT.BORDER);
		viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.getTable().setFont(parent.getFont());
		viewer.setContentProvider(new IStructuredContentProvider(){
			public Object[] getElements(Object inputElement) {
				if( inputElement instanceof Object[][] ) {
					return (Object[])inputElement;
				}
				return null;
			}
			public void dispose() {
			}
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
		});
		viewer.setLabelProvider(new LabelProvider(){
			public String getText(Object element) {
				if( element instanceof Object[] ) {
					return ((Object[])element)[0].toString();
				}
				return super.getText(element);
			}
		});
		Object[][] input = getLabelsAndValues();
		viewer.setInput(input);
		for( int i=0; i<input.length; i++ ) {
			int flag = ((Integer)(input[i][1])).intValue();
			boolean state = (flag==currValue) || (0 != (flag&currValue));
			viewer.setChecked(input[i], state);
		}
	}

	protected abstract Object[][] getLabelsAndValues();

	public boolean update() {
		if( null != viewer ) {
			currValue = 0;
			Object[] checked = viewer.getCheckedElements();
			for( int i=0; i<checked.length; i++ ) {
				currValue |= ((Integer)((Object[])(checked[i]))[1]).intValue();
			}
			return true;
		}
		return false;
	}
}
