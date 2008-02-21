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
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;




public class IA2TableIsColumnSelectedMethod extends MethodData {

    private AccessibleTable accessibleTable;
    private TableColumnField columnField;
    
    public IA2TableIsColumnSelectedMethod(AccessibleTable accessibleTable) {
        super("isColumnSelected",true); //$NON-NLS-1$
        this.accessibleTable = accessibleTable;
        columnField = new TableColumnField("column",0,accessibleTable); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{columnField});
    }

    public boolean invoke() {
        int column = columnField.getIntValue();
        boolean isSelected = accessibleTable.isAccessibleColumnSelected(column);
        result = formatResult("isSelected="+isSelected); //$NON-NLS-1$
        return true;
    }
}
