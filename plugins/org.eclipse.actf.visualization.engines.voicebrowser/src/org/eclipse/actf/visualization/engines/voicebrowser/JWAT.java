/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Masahide WASHIZAWA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.voicebrowser;

import org.eclipse.actf.visualization.internal.engines.voicebrowser.JWATControllerImpl;

public class JWAT {

	/**
	 * Method createJWATController.
	 * 
	 * @return JWATController
	 */
	static public JWATController createJWATController() {
		return new JWATControllerImpl();
	}
}
