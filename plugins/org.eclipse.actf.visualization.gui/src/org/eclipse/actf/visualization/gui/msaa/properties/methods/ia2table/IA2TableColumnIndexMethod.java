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
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.TableIndexField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;




public class IA2TableColumnIndexMethod extends MethodData {

    private AccessibleTable accessibleTable;
    private TableIndexField childIndexField;
    
    public IA2TableColumnIndexMethod(AccessibleTable accessibleTable) {
        super("columnIndex",true); //$NON-NLS-1$
        this.accessibleTable = accessibleTable;
        childIndexField = new TableIndexField("childIndex",0,accessibleTable); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{childIndexField});
    }

    public boolean invoke() {
        int childIndex = childIndexField.getIntValue();
        int columnIndex = accessibleTable.getAccessibleColumnIndex(childIndex);
        result = formatResult("columnIndex="+columnIndex); //$NON-NLS-1$
        return true;
    }
}
