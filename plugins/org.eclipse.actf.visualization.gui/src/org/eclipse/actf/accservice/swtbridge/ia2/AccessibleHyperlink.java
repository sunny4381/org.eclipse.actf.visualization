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

import org.eclipse.swt.ole.win32.Variant;



public interface AccessibleHyperlink {

	public void dispose();

	public Variant getAccessibleActionAnchor(int index);

	public Variant getAccessibleActionObject(int index);

	public int getStartIndex();

	public int getEndIndex();

	public boolean isValid();

}
