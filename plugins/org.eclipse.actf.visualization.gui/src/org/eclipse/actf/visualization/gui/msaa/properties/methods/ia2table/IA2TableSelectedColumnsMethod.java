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


public class IA2TableSelectedColumnsMethod extends MethodData {

	private AccessibleTable accessibleTable;
	private IntegerField maxColumnsField;

	public IA2TableSelectedColumnsMethod(AccessibleTable accessibleTable) {
		super("selectedColumns", true); //$NON-NLS-1$
		this.accessibleTable = accessibleTable;
		maxColumnsField = new IntegerField("maxColumns",AccessibleObjectPropertySource.MAX_CHILD_PROPERTIES,0); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{maxColumnsField});
	}

    public Object getResult() {
		int maxColumns = maxColumnsField.getIntValue();
        int[] columns = accessibleTable.getSelectedAccessibleColumns(maxColumns);
        if( null != columns ) {
            return new ObjectArrayPropertySource(columns,formatResult("nColumns="+columns.length)); //$NON-NLS-1$
        }
        return formatResult(false);
	}
}
