/*******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.ui;

import java.net.URL;

import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;

public class PerspectiveListenerForBrowserLaunch implements
		IPerspectiveListener {

	private static final String BROWSER_ID = "org.eclipse.actf.model.ui.editors.ie.WebBrowserEditor";
	private static String TARGET_URL = "about:blank";

	private String id;

	public static void setTargetUrl(URL targetUrl) {
		if (targetUrl != null) {
			TARGET_URL = targetUrl.toString();
		} else {
			TARGET_URL = "about:blank";
		}
	}

	public PerspectiveListenerForBrowserLaunch(String id) {
		this.id = id;
	}

	public void perspectiveActivated(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		if (id.equals(perspective.getId())) {
			if (!ModelServiceUtils.activateEditorPart(BROWSER_ID)) {
				ModelServiceUtils.launch(TARGET_URL, BROWSER_ID);
			}
		}
	}

	public void perspectiveChanged(IWorkbenchPage page,
			IPerspectiveDescriptor perspective, String changeId) {
	}

}
