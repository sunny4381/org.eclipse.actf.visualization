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
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleRelation;
import org.eclipse.actf.visualization.gui.msaa.properties.AccessibleObjectPropertySource;
import org.eclipse.actf.visualization.gui.msaa.properties.ObjectArrayPropertySource;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.IntegerField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;


public class IA2RelationsMethod extends MethodData {

	private Accessible2 accessible2;
	private IntegerField maxRelationsField;

	public IA2RelationsMethod(Accessible2 accessible2) {
		super("relations", true); //$NON-NLS-1$
		this.accessible2 = accessible2;
		maxRelationsField = new IntegerField("maxRelations",AccessibleObjectPropertySource.MAX_CHILD_PROPERTIES,0); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{maxRelationsField});
	}

    public Object getResult() {
		int maxRelations = maxRelationsField.getIntValue();
        AccessibleRelation[] relations = accessible2.getAccessibleRelations(maxRelations);
        if( null != relations ) {
            return new ObjectArrayPropertySource(relations,formatResult("nRelations="+relations.length)); //$NON-NLS-1$
        }
        return formatResult(false);
	}
}
