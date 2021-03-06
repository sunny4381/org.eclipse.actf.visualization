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




public interface AccessibleAction {

	public void dispose();

	public int getAccessibleActionCount();

	public boolean doAccessibleAction(int actionIndex);

	public String getAccessibleActionDescription(int actionIndex);

	public String[] getAccessibleActionKeyBinding(int actionIndex, int nMaxBinding);

	public String getAccessibleActionName(int actionIndex);

	public String getLocalizedAccessibleActionName(int actionIndex);

}
