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
package org.eclipse.actf.visualization.gui.msaa.checker;

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;

public class MSAAProblem implements MSAAProblemConst {

    /**
     * One of ERROR, WARNING, or INFO.
     */
    private int errorCategory;

    private int errorCode;

    private String errorDescription;

    private AccessibleObject errorObject;

    public MSAAProblem(int errorCategory, int errorCode, AccessibleObject errorObject) {
        this.errorCategory = errorCategory;
        this.errorCode = errorCode;
        this.errorDescription = DESCRIPTIONS[errorCode];
        this.errorObject = errorObject;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getErrorCategory() {
        return this.errorCategory;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public AccessibleObject getErrorObject() {
        return errorObject;
    }
}
