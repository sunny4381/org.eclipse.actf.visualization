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

package org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2action;

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleAction;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.ActionIndexField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;




public class IA2ActionLocalizedNameMethod extends MethodData {

    private AccessibleAction accessibleAction;
    private ActionIndexField actionIndexField;
    
    public IA2ActionLocalizedNameMethod(AccessibleAction accessibleAction) {
        super("localizedName",true); //$NON-NLS-1$
        this.accessibleAction = accessibleAction;
        actionIndexField = new ActionIndexField("actionIndex",0,accessibleAction); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{actionIndexField});
    }
    
    public Object getResult() {
        int actionIndex = actionIndexField.getIntValue();
        String localizedName = accessibleAction.getLocalizedAccessibleActionName(actionIndex); 
        return formatResult("localizedName="+T(localizedName)); //$NON-NLS-1$
    }
}
