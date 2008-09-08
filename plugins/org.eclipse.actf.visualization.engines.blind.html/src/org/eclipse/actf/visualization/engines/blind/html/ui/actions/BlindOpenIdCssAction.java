/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.html.ui.actions;

import org.eclipse.actf.visualization.engines.blind.BlindVizResourceUtil;
import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.ElementViewerManager;
import org.eclipse.actf.visualization.internal.engines.blind.html.Messages;
import org.eclipse.jface.action.Action;

public class BlindOpenIdCssAction extends Action {

	public BlindOpenIdCssAction() {
		setToolTipText(Messages.getString("BlindView.Open_ID"));
		setImageDescriptor(BlindVizResourceUtil
				.getImageDescriptor("icons/ButtonIdCss.png"));
		setText("ID/CSS");
	}

	public void run() {
		ElementViewerManager.getInstance().openElementViewer();
	}

}
