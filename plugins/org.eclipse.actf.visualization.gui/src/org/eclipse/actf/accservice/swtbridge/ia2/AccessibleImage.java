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

import org.eclipse.swt.graphics.Point;



public interface AccessibleImage {

	public void dispose();

	public String getAccessibleImageDescription();

	public Point getAccessibleImagePosition(int coordinateType);

	public Point getAccessibleImageSize();

}
