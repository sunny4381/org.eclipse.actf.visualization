/*******************************************************************************
 * Copyright (c) 2007,2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.eval;

import org.eclipse.actf.visualization.eval.preferences.IPreferenceConstants;
import org.eclipse.actf.visualization.internal.eval.CheckerExtension;
import org.eclipse.actf.visualization.internal.eval.EvaluationPlugin;

public class EvaluationUtil {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.actf.visualization.eval";

	/**
	 * @return
	 */
	public static boolean isOriginalDOM() {
		return IPreferenceConstants.CHECKER_ORG_DOM.equals(EvaluationPlugin
				.getDefault().getPluginPreferences().getString(
						IPreferenceConstants.CHECKER_TARGET));
	}
	
	public static IChecker[] getCheckers() {
		return CheckerExtension.getCheckers();
	}

	public static ICheckerInfoProvider[] getCheckerInfoProviders() {
		return CheckerExtension.getCheckerInfoProviders();
	}

}
