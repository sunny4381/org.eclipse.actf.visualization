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

public class IA2CoordTypeField extends AbstractEnumIntegerField {

	private static final Object[][] IA2_COORDTYPE = {
		getLabelAndValue(IA2.IA2_COORDTYPE_SCREEN_RELATIVE),
		getLabelAndValue(IA2.IA2_COORDTYPE_PARENT_RELATIVE),
	};
	
	private static final Object[] getLabelAndValue(int type) {
		return new Object[] {IA2.getCoordTypeText(type),new Integer(type)};
	}
	
	public IA2CoordTypeField(String labelText, int initValue) {
		super(labelText,initValue);
	}

	protected Object[][] getLabelsAndValues() {
		return IA2_COORDTYPE;
	}
}
