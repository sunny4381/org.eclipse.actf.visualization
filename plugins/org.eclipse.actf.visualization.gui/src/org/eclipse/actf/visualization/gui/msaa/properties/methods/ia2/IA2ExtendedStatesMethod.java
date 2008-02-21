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

package org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2;

import org.eclipse.actf.accservice.swtbridge.ia2.Accessible2;
import org.eclipse.actf.visualization.gui.msaa.properties.AccessibleObjectPropertySource;
import org.eclipse.actf.visualization.gui.msaa.properties.ObjectArrayPropertySource;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IntegerField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;




public class IA2ExtendedStatesMethod extends MethodData {

    private Accessible2 accessible2;
    private IntegerField maxExtendedStatesField;

    public IA2ExtendedStatesMethod(Accessible2 accessible2) {
        super("extendedStates", true); //$NON-NLS-1$
        this.accessible2 = accessible2;
        maxExtendedStatesField = new IntegerField("maxExtendedStates",AccessibleObjectPropertySource.MAX_CHILD_PROPERTIES,0); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{maxExtendedStatesField});
    }

    public Object getResult() {
        int maxExtendedStates = maxExtendedStatesField.getIntValue();
        String[] strArray = accessible2.getExtendedStates(maxExtendedStates);
        if( null != strArray ) {
            return new ObjectArrayPropertySource(strArray);
        }
        return null;
    }
}
