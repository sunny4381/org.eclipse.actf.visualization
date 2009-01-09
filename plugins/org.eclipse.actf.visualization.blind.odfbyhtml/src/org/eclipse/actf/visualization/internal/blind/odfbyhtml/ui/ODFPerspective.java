/*******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Norimasa HAYASHIDA - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.blind.odfbyhtml.ui;

import org.eclipse.actf.visualization.ui.IVisualizationPerspective;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class ODFPerspective implements IPerspectiveFactory, IVisualizationPerspective {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);

		//layout info: moved to fragment XML
		//initializer: moved to Startup.java
	}
}
