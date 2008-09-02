/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.accservice.swtbridge.ia2;

import java.util.Locale;



public interface Accessible2 {

	public void dispose();

	public int getAccessibleRelationCount();

	public AccessibleRelation getAccessibleRelation(int relationIndex);

	public AccessibleRelation[] getAccessibleRelations(int maxRelations);

	public int getAccessibleRole();

	public boolean scrollTo(int scrollType);

	public boolean scrollToPoint(int coordinateType, int x, int y);

	public int[] getGroupPosition();

	public int getStates();

	public String getExtendedRole();

	public String getLocalizedExtendedRole();

	public int getExtendedStateCount();

	public String[] getExtendedStates(int maxExtendedStates);

	public String[] getLocalizedExtendedStates(int maxLocalizedExtendedStates);

	public int getUniqueID();

	public int getWindowHandle();

	public int getAccessibleIndexInParent();

	public Locale getLocale();

	public String getAttributes();

}
