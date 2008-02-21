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


public class IA2TableSelectedChildrenMethod extends MethodData {

	private AccessibleTable accessibleTable;
	private IntegerField maxChildrenField;

	public IA2TableSelectedChildrenMethod(AccessibleTable accessibleTable) {
		super("selectedChildren", true); //$NON-NLS-1$
		this.accessibleTable = accessibleTable;
		maxChildrenField = new IntegerField("maxChildren",AccessibleObjectPropertySource.MAX_CHILD_PROPERTIES,0); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{maxChildrenField});
	}

    public Object getResult() {
		int maxChildren = maxChildrenField.getIntValue();
        int[] children = accessibleTable.getSelectedAccessibleChildren(maxChildren);
        if( null != children ) {
            return new ObjectArrayPropertySource(children,formatResult("nChildren="+children.length)); //$NON-NLS-1$
        }
        return formatResult(false);
	}
}
