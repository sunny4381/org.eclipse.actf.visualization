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

import org.eclipse.actf.accservice.swtbridge.IA2;

public class IA2TextBoundaryTypeField extends AbstractEnumIntegerField {

	private static final Object[][] IA2_TEXT_BOUNDARY = {
        getLabelAndValue(IA2.IA2_TEXT_BOUNDARY_CHAR),
        getLabelAndValue(IA2.IA2_TEXT_BOUNDARY_WORD),
        getLabelAndValue(IA2.IA2_TEXT_BOUNDARY_SENTENCE),
        getLabelAndValue(IA2.IA2_TEXT_BOUNDARY_PARAGRAPH),
        getLabelAndValue(IA2.IA2_TEXT_BOUNDARY_LINE),
        getLabelAndValue(IA2.IA2_TEXT_BOUNDARY_ALL),
	};
	
	private static final Object[] getLabelAndValue(int type) {
		return new Object[] {IA2.getTextBoundaryTypeText(type),new Integer(type)};
	}
	
	public IA2TextBoundaryTypeField(String labelText, int initValue) {
		super(labelText,initValue);
	}

	protected Object[][] getLabelsAndValues() {
		return IA2_TEXT_BOUNDARY;
	}
}
