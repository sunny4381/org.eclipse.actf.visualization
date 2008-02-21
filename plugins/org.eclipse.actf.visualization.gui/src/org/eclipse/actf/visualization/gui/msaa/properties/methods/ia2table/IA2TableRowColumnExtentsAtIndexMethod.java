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
import org.eclipse.actf.accservice.swtbridge.ia2.RowColumnExtents;
import org.eclipse.actf.visualization.gui.msaa.properties.AttributePropertySource;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.TableIndexField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;




public class IA2TableRowColumnExtentsAtIndexMethod extends MethodData {

    private AccessibleTable accessibleTable;
    private TableIndexField indexField;
    
    public IA2TableRowColumnExtentsAtIndexMethod(AccessibleTable accessibleTable) {
        super("rowColumnExtentsAtIndex",true); //$NON-NLS-1$
        this.accessibleTable = accessibleTable;
        indexField = new TableIndexField("index",0,accessibleTable); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{indexField});
    }

    public boolean invoke() {
        int index = indexField.getIntValue();
        RowColumnExtents extents = accessibleTable.getAccessibleRowColumnExtentAtIndex(index);
        if( null != extents ) {
            AttributePropertySource attSource = new AttributePropertySource(null, formatResult(true));
            attSource.put("row", new Integer(extents.row)); //$NON-NLS-1$
            attSource.put("column", new Integer(extents.column)); //$NON-NLS-1$
            attSource.put("rowExtents", new Integer(extents.rowExtents)); //$NON-NLS-1$
            attSource.put("columnExtents", new Integer(extents.columnExtents)); //$NON-NLS-1$
            attSource.put("isSelected", new Boolean(extents.isSelected)); //$NON-NLS-1$
            result = attSource;
        }
        else {
            result = formatResult(false);
        }
        return true;
    }
}
