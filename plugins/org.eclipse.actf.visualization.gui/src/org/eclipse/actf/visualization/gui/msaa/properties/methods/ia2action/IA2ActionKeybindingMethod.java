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
import org.eclipse.actf.visualization.gui.msaa.properties.AccessibleObjectPropertySource;
import org.eclipse.actf.visualization.gui.msaa.properties.ObjectArrayPropertySource;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.ActionIndexField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IntegerField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;




public class IA2ActionKeybindingMethod extends MethodData {

    private AccessibleAction accessibleAction;
    private ActionIndexField actionIndexField;
    private IntegerField nMaxBingingField;
    
    public IA2ActionKeybindingMethod(AccessibleAction accessibleAction) {
        super("keyBinding",true); //$NON-NLS-1$
        this.accessibleAction = accessibleAction;
        actionIndexField = new ActionIndexField("actionIndex",0,accessibleAction); //$NON-NLS-1$
        nMaxBingingField = new IntegerField("nMaxBinding",AccessibleObjectPropertySource.MAX_CHILD_PROPERTIES,0); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{actionIndexField,nMaxBingingField});
    }
    
    public Object getResult() {
        int actionIndex = actionIndexField.getIntValue();
        int nMaxBinding = nMaxBingingField.getIntValue();
        String[] keyBinding = accessibleAction.getAccessibleActionKeyBinding(actionIndex, nMaxBinding);
        if( null != keyBinding ) {
            return new ObjectArrayPropertySource(keyBinding,formatResult("nBinding="+keyBinding.length)); //$NON-NLS-1$
        }
        return formatResult(false);
    }
}
