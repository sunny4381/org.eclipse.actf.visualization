/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.ui.pdt;

import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class VisualizeActionForPdt extends AbstructVisualizationActionForPdt
		implements IWorkbenchWindowActionDelegate {

	@Override
	String getTargetViewId() {
		return IVisualizationView.ID_BLINDVIEW;
	}

}
