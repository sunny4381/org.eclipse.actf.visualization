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
import org.eclipse.actf.visualization.engines.blind.BlindVizEnginePlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.plugin.AbstractUIPlugin;



public class BlindSaveAction extends Action {
    PartControlBlind prb;
    
	public BlindSaveAction(PartControlBlind prb) {
		setToolTipText(Messages.getString("Tooltip.Save")); //$NON-NLS-1$
		//TODO
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(BlindVizEnginePlugin.PLUGIN_ID, "icons/ButtonSave.png"));
		setText(Messages.getString("MenuConst.Save")); //$NON-NLS-1$
        this.prb = prb;
	}

	public void run() {
        prb.doSave();
	}

}
