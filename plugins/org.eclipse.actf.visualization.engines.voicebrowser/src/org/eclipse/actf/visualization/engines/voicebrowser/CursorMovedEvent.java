/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hironobu TAKAGI - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.voicebrowser;

import java.util.EventObject;

public class CursorMovedEvent extends EventObject {
	
	private int cCursorPos;

	/**
	 * Constructor for CursorChangedEvent.
	 * @param source
	 */
	public CursorMovedEvent(Object source, int pos) {
		super(source);
			cCursorPos = pos;
	}

	/**
	 * Returns the cCursorPos.
	 * @return int
	 */
	public int getCCursorPos() {
		return cCursorPos;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return (getClass().getName() +
                "[source=" + getSource() + ",cCursorPos=" + cCursorPos + "]");
	}

}
