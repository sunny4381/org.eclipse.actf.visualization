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
import org.eclipse.actf.visualization.gui.msaa.properties.fields.TableColumnField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.TableRowField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;




public class IA2TableChildIndexMethod extends MethodData {

    private AccessibleTable accessibleTable;
    private TableRowField rowIndexField;
    private TableColumnField columnIndexField;
    
    public IA2TableChildIndexMethod(AccessibleTable accessibleTable) {
        super("childIndex",true); //$NON-NLS-1$
        this.accessibleTable = accessibleTable;
        rowIndexField = new TableRowField("rowIndex",0,accessibleTable); //$NON-NLS-1$
        columnIndexField = new TableColumnField("columnIndex",0,accessibleTable); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{rowIndexField,columnIndexField});
    }

    public boolean invoke() {
        int rowIndex = rowIndexField.getIntValue();
        int columnIndex = columnIndexField.getIntValue();
        int childIndex = accessibleTable.getAccessibleIndex(rowIndex, columnIndex);
        result = formatResult("childIndex="+childIndex); //$NON-NLS-1$
        return true;
    }
}
