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

import org.eclipse.actf.accservice.swtbridge.MSAA;

public class MSAASELFLAGField extends AbstractFlagIntegerField {

	private static final Object[][] SELFLAGS = {
		{"SELFLAG_NONE",new Integer(MSAA.SELFLAG_NONE)}, //$NON-NLS-1$
		{"SELFLAG_TAKEFOCUS",new Integer(MSAA.SELFLAG_TAKEFOCUS)}, //$NON-NLS-1$
		{"SELFLAG_TAKESELECTION",new Integer(MSAA.SELFLAG_TAKESELECTION)}, //$NON-NLS-1$
		{"SELFLAG_EXTENDSELECTION",new Integer(MSAA.SELFLAG_EXTENDSELECTION)}, //$NON-NLS-1$
		{"SELFLAG_ADDSELECTION",new Integer(MSAA.SELFLAG_ADDSELECTION)}, //$NON-NLS-1$
		{"SELFLAG_REMOVESELECTION",new Integer(MSAA.SELFLAG_REMOVESELECTION)} //$NON-NLS-1$
	};
	
	public MSAASELFLAGField(String labelText, int initValue) {
		super(labelText,initValue);
	}

	protected Object[][] getLabelsAndValues() {
		return SELFLAGS;
	}
}
