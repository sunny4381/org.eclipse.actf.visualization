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
import org.eclipse.actf.visualization.gui.msaa.properties.AccessibleRelationPropertySource;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.RelationIndexField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;


public class IA2RelationMethod extends MethodData {

	private Accessible2 accessible2;
	private RelationIndexField relationIndexField;

	public IA2RelationMethod(Accessible2 accessible2) {
		super("relation", true); //$NON-NLS-1$
		this.accessible2 = accessible2;
		relationIndexField = new RelationIndexField("relationIndex",0,accessible2); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{relationIndexField});
	}

    public boolean invoke() {
		int relationIndex = relationIndexField.getIntValue();
        AccessibleRelation relations = accessible2.getAccessibleRelation(relationIndex);
        if( null != relations ) {
            result = new AccessibleRelationPropertySource(relations){
                public Object getEditableValue() {
                    return formatResult(true);
                }
            };
        }
        else {
            result = formatResult(false);
        }
        return true;
	}
}
