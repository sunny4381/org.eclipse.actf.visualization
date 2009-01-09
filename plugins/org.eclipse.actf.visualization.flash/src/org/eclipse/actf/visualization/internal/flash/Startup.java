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
package org.eclipse.actf.visualization.internal.flash;

import org.eclipse.actf.model.flash.proxy.FlashCacheUtil;
import org.eclipse.actf.model.ui.util.PerspectiveListenerForBrowserLaunch;
import org.eclipse.actf.visualization.ui.IVisualizationPerspective;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class Startup implements IStartup {

	private class PerspectiveListenerForCacheCheck implements
			IPerspectiveListener {
		public void perspectiveActivated(IWorkbenchPage page,
				IPerspectiveDescriptor perspective) {
			if (IVisualizationPerspective.ID_FLASH_PERSPECTIVE
					.equals(perspective.getId())) {
				// // tentative code for avoiding
				// // isWorkbenchRunning()'s bug
				// if (((Workbench) (PlatformUI.getWorkbench())).isStarting()) {
				// // do nothing
				// } else {
				FlashCacheUtil.checkCache();
				// }
			}
		}

		public void perspectiveChanged(IWorkbenchPage page,
				IPerspectiveDescriptor perspective, String changeId) {
		}
	};

	public void earlyStartup() {

		// can't use activeWindow in startup phase
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench()
				.getWorkbenchWindows();
		for (IWorkbenchWindow window : windows) {
			addListener(window);
		}

		PlatformUI.getWorkbench().addWindowListener(new IWindowListener() {
			public void windowActivated(IWorkbenchWindow window) {
			}

			public void windowClosed(IWorkbenchWindow window) {
			}

			public void windowDeactivated(IWorkbenchWindow window) {
			}

			public void windowOpened(IWorkbenchWindow window) {
				addListener(window);
			}

		});
	}

	private void addListener(IWorkbenchWindow window) {
		final PerspectiveListenerForBrowserLaunch listener = new PerspectiveListenerForBrowserLaunch(
				IVisualizationPerspective.ID_FLASH_PERSPECTIVE);
		final PerspectiveListenerForCacheCheck listener2 = new PerspectiveListenerForCacheCheck();
		window.addPerspectiveListener(listener);
		window.addPerspectiveListener(listener2);
		final IWorkbenchPage page = window.getActivePage();
		if (page != null) {
			window.getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					listener.perspectiveActivated(page, page.getPerspective());
					listener2.perspectiveActivated(page, page.getPerspective());
				}
			});
		}
	}

}
