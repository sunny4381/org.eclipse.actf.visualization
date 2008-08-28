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
package org.eclipse.actf.visualization.gui.internal.util;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;
import org.eclipse.swt.graphics.Point;

public class TextMap extends LinkedHashMap<AccessibleObject, Point> {

    private static final long serialVersionUID = -8640673640566349712L;

    public void clear() {
		super.clear();
	}

	public Point put(AccessibleObject accObject, Point point) {
		return super.put(accObject,point);
	}

	public Point getPoint(AccessibleObject accObject) {
		return get(accObject);
	}
	
	public AccessibleObject getAccessibleObject(Point point) {
		AccessibleObject retObject = null;
		for( Iterator it = keySet().iterator(); it.hasNext(); ) {
			AccessibleObject accObject = (AccessibleObject)it.next();
			Point accPoint = getPoint(accObject);
			if( accPoint.x <= point.x && point.x < accPoint.y ) {
				retObject = accObject;
				if( accPoint.x != accPoint.y ) break;
			}
		}
		return retObject;
	}
}
