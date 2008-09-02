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


public class AccessibleTableModelChange {

    public int type;
    public int firstRow;
    public int lastRow;
    public int firstColumn;
    public int lastColumn;
    
    public AccessibleTableModelChange(int type, int firstRow, int lastRow, int firstColumn, int lastColumn) {
        this.type = type;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.firstColumn = firstColumn;
        this.lastColumn = lastColumn;
    }

}
