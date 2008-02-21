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
package org.eclipse.actf.visualization.gui.msaa.properties;

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleTable;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableAccessibleAtMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableChildIndexMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableColumnDescriptionMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableColumnExtentAtMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableColumnIndexMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableIsColumnSelectedMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableIsRowSelectedMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableIsSelectedMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableRowColumnExtentsAtIndexMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableRowDescriptionMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableRowExtentAtMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableRowIndexMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableSelectColumnMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableSelectRowMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableSelectedChildrenMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableSelectedColumnsMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableSelectedRowsMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableUnselectColumnMethod;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2table.IA2TableUnselectRowMethod;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;


public class AccessibleTablePropertySource extends AbstractPropertyInvokeSource implements IPropertySource {

	private AccessibleTable accessibleTable;

	private static final String
	    PID_IA2_TABLE_nRows = "nRows", //$NON-NLS-1$
	    PID_IA2_TABLE_nColumns = "nColumns", //$NON-NLS-1$
	    PID_IA2_TABLE_nSelectedChildren = "nSelectedChildren", //$NON-NLS-1$
	    PID_IA2_TABLE_nSelectedRows = "nSelectedRows", //$NON-NLS-1$
	    PID_IA2_TABLE_nSelectedColumns = "nSelectedColumns", //$NON-NLS-1$
	    PID_IA2_TABLE_selectedChildren = "selectedChildren", //$NON-NLS-1$
	    PID_IA2_TABLE_selectedRows = "selectedRows", //$NON-NLS-1$
	    PID_IA2_TABLE_selectedColumns = "selectedColumns", //$NON-NLS-1$
	    PID_IA2_TABLE_caption = "caption", //$NON-NLS-1$
	    PID_IA2_TABLE_summary = "summary", //$NON-NLS-1$
        PID_IA2_TABLE_accessibleAt = "accessibleAt", //$NON-NLS-1$
        PID_IA2_TABLE_childIndex = "childIndex", //$NON-NLS-1$
        PID_IA2_TABLE_columnDescription = "columnDescription", //$NON-NLS-1$
        PID_IA2_TABLE_columnExtentAt = "columnExtentAt", //$NON-NLS-1$
        PID_IA2_TABLE_columnHeader = "columnHeader", //$NON-NLS-1$
        PID_IA2_TABLE_columnIndex = "columnIndex", //$NON-NLS-1$
        PID_IA2_TABLE_rowDescription = "rowDescription", //$NON-NLS-1$
        PID_IA2_TABLE_rowExtentAt = "rowExtentAt", //$NON-NLS-1$
        PID_IA2_TABLE_rowHeader = "rowHeader", //$NON-NLS-1$
        PID_IA2_TABLE_rowIndex = "rowIndex", //$NON-NLS-1$
        PID_IA2_TABLE_isColumnSelected = "isColumnSelected", //$NON-NLS-1$
        PID_IA2_TABLE_isRowSelected = "isRowSelected", //$NON-NLS-1$
        PID_IA2_TABLE_isSelected = "isSelected", //$NON-NLS-1$
        PID_IA2_TABLE_selectColumn = "selectColumn", //$NON-NLS-1$
        PID_IA2_TABLE_unselectColumn = "unselectColumn", //$NON-NLS-1$
        PID_IA2_TABLE_selectRow = "selectRow", //$NON-NLS-1$
        PID_IA2_TABLE_unselectRow = "unselectRow", //$NON-NLS-1$
        PID_IA2_TABLE_rowColumnExtentsAtIndex = "rowColumnExtentsAtIndex"; //$NON-NLS-1$
	
	private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] 
	{
		new PropertyDescriptor(PID_IA2_TABLE_nRows,"nRows"),            //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_TABLE_nColumns,"nColumns"),         //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_TABLE_nSelectedChildren,"nSelectedChildren"),    //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_TABLE_nSelectedRows,"nSelectedRows"),    //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_TABLE_nSelectedColumns,"nSelectedColumns"), //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_TABLE_selectedChildren,"selectedChildren"),     //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_TABLE_selectedRows,"selectedRows"),     //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_TABLE_selectedColumns,"selectedColumns"),  //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_TABLE_caption,"caption"),          //$NON-NLS-1$
		new PropertyDescriptor(PID_IA2_TABLE_summary,"summary"),           //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_columnHeader,"columnHeader"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_rowHeader,"rowHeader"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_rowIndex,"rowIndex"), //$NON-NLS-1$
	};
    private static final IPropertyDescriptor[] descriptorsEx = new IPropertyDescriptor[] 
    {
        new PropertyDescriptor(PID_IA2_TABLE_accessibleAt,"accessibleAt"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_childIndex,"childIndex"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_columnDescription,"columnDescription"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_columnExtentAt,"columnExtentAt"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_columnIndex,"columnIndex"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_rowDescription,"rowDescription"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_rowExtentAt,"rowExtentAt"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_rowColumnExtentsAtIndex,"rowColumnExtentsAtIndex"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_isColumnSelected,"isColumnSelected"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_isRowSelected,"isRowSelected"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_isSelected,"isSelected"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_selectRow,"selectRow"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_selectColumn,"selectColumn"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_unselectRow,"unselectRow"), //$NON-NLS-1$
        new PropertyDescriptor(PID_IA2_TABLE_unselectColumn,"unselectColumn") //$NON-NLS-1$
    };
	
	public AccessibleTablePropertySource(AccessibleTable input) {
		this.accessibleTable = input;
        addMethodData(PID_IA2_TABLE_accessibleAt, new IA2TableAccessibleAtMethod(input));
        addMethodData(PID_IA2_TABLE_childIndex, new IA2TableChildIndexMethod(input));
        addMethodData(PID_IA2_TABLE_columnDescription, new IA2TableColumnDescriptionMethod(input));
        addMethodData(PID_IA2_TABLE_columnExtentAt, new IA2TableColumnExtentAtMethod(input));
        addMethodData(PID_IA2_TABLE_columnIndex, new IA2TableColumnIndexMethod(input));
        addMethodData(PID_IA2_TABLE_rowDescription, new IA2TableRowDescriptionMethod(input));
        addMethodData(PID_IA2_TABLE_rowExtentAt, new IA2TableRowExtentAtMethod(input));
        addMethodData(PID_IA2_TABLE_rowIndex, new IA2TableRowIndexMethod(input));
        addMethodData(PID_IA2_TABLE_isColumnSelected, new IA2TableIsColumnSelectedMethod(input));
        addMethodData(PID_IA2_TABLE_isRowSelected, new IA2TableIsRowSelectedMethod(input));
        addMethodData(PID_IA2_TABLE_isSelected, new IA2TableIsSelectedMethod(input));
        addMethodData(PID_IA2_TABLE_rowColumnExtentsAtIndex, new IA2TableRowColumnExtentsAtIndexMethod(input));
        addMethodData(PID_IA2_TABLE_selectRow, new IA2TableSelectRowMethod(input));
        addMethodData(PID_IA2_TABLE_selectColumn, new IA2TableSelectColumnMethod(input));
        addMethodData(PID_IA2_TABLE_unselectRow, new IA2TableUnselectRowMethod(input));
        addMethodData(PID_IA2_TABLE_unselectColumn, new IA2TableUnselectColumnMethod(input));
        addMethodData(PID_IA2_TABLE_selectedChildren, new IA2TableSelectedChildrenMethod(input));
        addMethodData(PID_IA2_TABLE_selectedColumns, new IA2TableSelectedColumnsMethod(input));
        addMethodData(PID_IA2_TABLE_selectedRows, new IA2TableSelectedRowsMethod(input));
	}

	public Object getEditableValue() {
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return descriptors;
	}

    public IPropertyDescriptor[] getPropertyDescriptorsExtra() {
        return descriptorsEx;
    }
    
	public Object getPropertyValue(Object id) {
        Object result = super.getPropertyValue(id);
        if( null != result ) {
            return result;
        }   
		String strValue = null;
		if( PID_IA2_TABLE_nRows.equals(id) ) {
            strValue = Integer.toString(accessibleTable.getAccessibleRowCount());
		}
		else if( PID_IA2_TABLE_nColumns.equals(id) ) {
            strValue = Integer.toString(accessibleTable.getAccessibleColumnCount());
		}
		else if( PID_IA2_TABLE_nSelectedChildren.equals(id) ) {
            strValue = Integer.toString(accessibleTable.getSelectedAccessibleChildCount());
		}
		else if( PID_IA2_TABLE_nSelectedRows.equals(id) ) {
            strValue = Integer.toString(accessibleTable.getSelectedAccessibleRowCount());
		}
		else if( PID_IA2_TABLE_nSelectedColumns.equals(id) ) {
            strValue = Integer.toString(accessibleTable.getSelectedAccessibleColumnCount());
		}
		else if( PID_IA2_TABLE_caption.equals(id) ) {
            Object accObject = accessibleTable.getAccessibleCaption();
            if( null != accObject ) {
                strValue = accObject.toString();
            	if( accObject instanceof AccessibleObject ) {
                    try {
                        ((AccessibleObject)accObject).dispose();
                    }
                    catch(Exception e) {
                    }
            	}
            }
		}
		else if( PID_IA2_TABLE_summary.equals(id) ) {
            Object accObject = accessibleTable.getAccessibleSummary();
            if( null != accObject ) {
                strValue = accObject.toString();
            	if( accObject instanceof AccessibleObject ) {
                    try {
                        ((AccessibleObject)accObject).dispose();
                    }
                    catch(Exception e) {
                    }
            	}
            }
		}
        else if( PID_IA2_TABLE_columnHeader.equals(id) ) {
            final int startingRowIndex[] = new int[1];
            AccessibleTable accTable = accessibleTable.getAccessibleColumnHeaders(startingRowIndex);
            if( null != accTable ) {
                return new AccessibleTablePropertySource(accTable){
                    public Object getEditableValue() {
                        return "startingRowIndex="+startingRowIndex[0]; //$NON-NLS-1$
                    }
                };
            }
        }
        else if( PID_IA2_TABLE_rowHeader.equals(id) ) {
            final int startingColumnIndex[] = new int[1];
            AccessibleTable accTable = accessibleTable.getAccessibleRowHeaders(startingColumnIndex);
            if( null != accTable ) {
                return new AccessibleTablePropertySource(accTable){
                    public Object getEditableValue() {
                        return "startingColumnIndex="+startingColumnIndex[0]; //$NON-NLS-1$
                    }
                };
            }
        }
		return null==strValue ? "null" : strValue; //$NON-NLS-1$
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {
	}

	public void setPropertyValue(Object id, Object value) {
	}
	
}
