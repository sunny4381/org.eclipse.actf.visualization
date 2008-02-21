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

package org.eclipse.actf.visualization.gui.msaa.properties.fields;

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleAction;



public class ActionIndexField extends IntegerField {

    private AccessibleAction accessibleAction;
    
    public ActionIndexField(String labelText, int initValue,AccessibleAction accessibleACtion) {
        super(labelText,initValue,0);
        this.accessibleAction = accessibleACtion;
    }

    protected boolean validateControl() {
        if( null != accessibleAction ) {
            maxValue = accessibleAction.getAccessibleActionCount()-1;
        }
        return super.validateControl();
    }
    
}
