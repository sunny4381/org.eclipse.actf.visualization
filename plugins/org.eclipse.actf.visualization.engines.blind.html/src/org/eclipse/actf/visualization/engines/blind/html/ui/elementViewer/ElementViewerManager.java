/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ElementViewerManager implements IVisualizeStyleInfoListener {
	private static ElementViewerManager INSTANCE;

	private IViewerPanel viewerPanel;

	private IHighlightElementListener prb;

	private VisualizeStyleInfo styleInfo;

	private Shell shell;

	public static ElementViewerManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ElementViewerManager();
			VisualizeStyleInfoManager.getInstance().addLisnter(INSTANCE);
		}
		return INSTANCE;
	}

	private ElementViewerManager() {
		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	public void setPartRightBlind(IHighlightElementListener prb) {
		this.prb = prb;
	}

	public void openElementViewer() {
		if (isExist()) {
			viewerPanel.forceActive();
		} else {
			viewerPanel = new ViewerPanelJFace(shell, styleInfo, prb);
		}
	}

	public void activateElementViewer() {
		if (isExist()) {
			viewerPanel.forceActive();
		}
	}

	public void hideElementViewer() {
		if (isExist()) {
			viewerPanel.hide();
		}
	}

	public void update(VisualizeStyleInfo styleInfo) {
		this.styleInfo = styleInfo;
		if (isExist()) {
			viewerPanel.asyncUpdateValue(styleInfo);
		}
	}

	private boolean isExist() {
		return ((null != viewerPanel) && (!viewerPanel.isDisposed()));
	}

}
