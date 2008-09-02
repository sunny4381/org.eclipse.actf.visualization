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


public class RowColumnExtents {

    public int row;  
    public int column;  
    public int rowExtents;  
    public int columnExtents;  
    public boolean isSelected; 

    public RowColumnExtents(int row, int column, int rowExtents, int columnExtents, boolean isSelected) {
        this.row = row;
        this.column = column;
        this.rowExtents = rowExtents;
        this.columnExtents = columnExtents;
        this.isSelected = isSelected;
    }

}
