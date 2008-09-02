/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.accservice.swtbridge.ia2;

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;



public interface AccessibleTable {

    public void dispose();

    public AccessibleObject getAccessibleCellAt(int row, int column);

    public Object getAccessibleCaption();

    public int getAccessibleIndex(int rowIndex, int columnIndex);

    public String getAccessibleColumnDescription(int column);

    public int getAccessibleColumnExtentAt(int row, int column);

    public AccessibleTable getAccessibleColumnHeaders(int[] startingRowIndex);

    public int getAccessibleColumnIndex(int childIndex);

    public int getAccessibleColumnCount();

    public int getAccessibleRowCount();

    public int getSelectedAccessibleChildCount();

    public int getSelectedAccessibleColumnCount();

    public int getSelectedAccessibleRowCount();

    public String getAccessibleRowDescription(int row);

    public int getAccessibleRowExtentAt(int row, int column);

    public AccessibleTable getAccessibleRowHeaders(int[] startingColumnIndex);

    public int getAccessibleRowIndex(int childIndex);

    public int[] getSelectedAccessibleChildren(int maxChildren);

    public int[] getSelectedAccessibleRows(int maxRows);

    public int[] getSelectedAccessibleColumns(int maxColumns);

    public Object getAccessibleSummary();

    public boolean isAccessibleColumnSelected(int column);

    public boolean isAccessibleRowSelected(int row);

    public boolean isAccessibleSelected(int row, int column);

    public boolean selectAccessibleRow(int row);

    public boolean selectAccessibleColumn(int column);

    public boolean unselectAccessibleRow(int row);

    public boolean unselectAccessibleColumn(int column);

    public RowColumnExtents getAccessibleRowColumnExtentAtIndex(int index);

    public AccessibleTableModelChange getAccessibleTableModelChange();

}
