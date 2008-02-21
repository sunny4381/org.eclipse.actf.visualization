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
package org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table;

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleTable;
import org.eclipse.actf.visualization.gui.msaa.properties.AccessibleObjectPropertySource;
import org.eclipse.actf.visualization.gui.msaa.properties.ObjectArrayPropertySource;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IntegerField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;


public class IA2TableSelectedRowsMethod extends MethodData {

	private AccessibleTable accessibleTable;
	private IntegerField maxRowsField;

	public IA2TableSelectedRowsMethod(AccessibleTable accessibleTable) {
		super("selectedRows", true); //$NON-NLS-1$
		this.accessibleTable = accessibleTable;
		maxRowsField = new IntegerField("maxRows",AccessibleObjectPropertySource.MAX_CHILD_PROPERTIES,0); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{maxRowsField});
	}

    public Object getResult() {
		int maxRows = maxRowsField.getIntValue();
        int[] rows = accessibleTable.getSelectedAccessibleRows(maxRows);
        if( null != rows ) {
            return new ObjectArrayPropertySource(rows,formatResult("nRows="+rows.length)); //$NON-NLS-1$
        }
        return formatResult(false);
	}
}
