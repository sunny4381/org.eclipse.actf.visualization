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

import org.eclipse.actf.accservice.swtbridge.ia2.Accessible2;



public class RelationIndexField extends IntegerField {

    private Accessible2 accessible2;
    
    public RelationIndexField(String labelText, int initValue,Accessible2 accessible2) {
        super(labelText,initValue,0);
        this.accessible2 = accessible2;
    }

    protected boolean validateControl() {
        if( null != accessible2 ) {
            maxValue = accessible2.getAccessibleRelationCount()-1;
        }
        return super.validateControl();
    }
    
}
