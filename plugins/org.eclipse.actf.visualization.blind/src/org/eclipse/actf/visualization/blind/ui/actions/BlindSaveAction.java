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

package org.eclipse.actf.visualization.blind.ui.actions;

import org.eclipse.actf.ui.util.Messages;
import org.eclipse.actf.visualization.blind.ui.internal.PartControlBlind;
import org.eclipse.actf.visualization.engines.blind.BlindVizResourceUtil;
import org.eclipse.jface.action.Action;

public class BlindSaveAction extends Action {
	PartControlBlind prb;

	public BlindSaveAction(PartControlBlind prb) {
		setToolTipText(Messages.Tooltip_Save); 
		setImageDescriptor(BlindVizResourceUtil
				.getImageDescriptor("icons/ButtonSave.png"));
		setText(Messages.MenuConst_Save); 
		this.prb = prb;
	}

	public void run() {
		prb.doSave();
	}

}
