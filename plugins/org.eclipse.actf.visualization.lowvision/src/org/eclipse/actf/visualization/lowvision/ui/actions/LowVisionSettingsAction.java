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

package org.eclipse.actf.visualization.lowvision.ui.actions;

import org.eclipse.actf.util.ui.Messages;
import org.eclipse.actf.util.ui.PreferenceUtils;
import org.eclipse.actf.visualization.lowvision.LowVisionVizPlugin;
import org.eclipse.actf.visualization.lowvision.ui.preferences.LowVisionPreferencePage;
import org.eclipse.jface.action.Action;



public class LowVisionSettingsAction extends Action {
		
    public LowVisionSettingsAction() {
        setToolTipText(Messages.getString("Tooltip.Settings")); //$NON-NLS-1$
        setImageDescriptor(LowVisionVizPlugin.imageDescriptorFromPlugin(LowVisionVizPlugin.PLUGIN_ID, "icons/setting.png"));
        setText(Messages.getString("MenuConst.Settings")); //$NON-NLS-1$
    }

    public void run() {
        PreferenceUtils.openPreferenceDialog(LowVisionPreferencePage.ID);
    }

}
