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
package org.eclipse.actf.visualization.gui.preferences;

import org.eclipse.actf.accservice.swtbridge.MSAA;
import org.eclipse.actf.visualization.gui.internal.GuiPlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;


/**
 * Class used to initialize default preference values.
 */
public class GuiPreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = GuiPlugin.getDefault().getPreferenceStore();
		boolean isAT = MSAA.getScreenReader();
		store.setDefault(GuiPreferenceConstants.AlwaysOnTop, false);
		store.setDefault(GuiPreferenceConstants.UseOverlayWindow, !isAT);
		if( isAT ) {
			store.setValue(GuiPreferenceConstants.AlwaysOnTop, false);
			store.setValue(GuiPreferenceConstants.UseOverlayWindow, false);
		}
		GuiPreferenceManager.init();
	}

}
