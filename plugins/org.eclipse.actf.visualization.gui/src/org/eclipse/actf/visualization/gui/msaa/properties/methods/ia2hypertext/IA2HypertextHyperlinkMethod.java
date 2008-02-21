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
package org.eclipse.actf.visualization.gui.msaa.properties.methods.ia2hypertext;

import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleHyperlink;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleHypertext;
import org.eclipse.actf.visualization.gui.msaa.properties.AccessibleHyperlinkPropertySource;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.AbstractInputField;
import org.eclipse.actf.visualization.gui.msaa.properties.fields.HypertextHyperlinkIndexField;
import org.eclipse.actf.visualization.gui.msaa.properties.methods.MethodData;


public class IA2HypertextHyperlinkMethod extends MethodData {

	private AccessibleHypertext accessibleHypertext;
	private HypertextHyperlinkIndexField indexField; 
	
	public IA2HypertextHyperlinkMethod(AccessibleHypertext accessibleHyperext) {
		super("hyperlink",true); //$NON-NLS-1$
		this.accessibleHypertext = accessibleHyperext;
		indexField = new HypertextHyperlinkIndexField("index",0,accessibleHyperext); //$NON-NLS-1$
        setInputFields(new AbstractInputField[]{indexField});
	}

	public Object getResult() {
		if( 0 == accessibleHypertext.getHyperLinkCount() ) {
			return null;
		}
		int index = indexField.getIntValue();
        AccessibleHyperlink hyperlink = accessibleHypertext.getHyperLink(index);
        if( null != hyperlink ) {
            return new AccessibleHyperlinkPropertySource(hyperlink){
                public Object getEditableValue() {
                    return formatResult(true);
                }
            };
        }
        return formatResult(false);
	}
}
