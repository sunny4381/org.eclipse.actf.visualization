/*******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.flash.ui;

import org.eclipse.actf.visualization.gui.IGuiViewIDs;
import org.eclipse.actf.visualization.ui.IVisualizationPerspective;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class FlashPerspective implements IPerspectiveFactory,
		IVisualizationPerspective {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);

		IFolderLayout reportFolder = layout.createFolder(
				"actf.flash.report.folder", IPageLayout.BOTTOM, 0.7f,
				editorArea);
		IFolderLayout rightReportFolder = layout.createFolder(
				"actf.flash.report.left.folder", IPageLayout.RIGHT, 0.5f,
				"actf.flash.report.folder");
		IFolderLayout simulatorFolder = layout.createFolder(
				"actf.flash.simulator.folder", IPageLayout.RIGHT, 0.5f,
				editorArea);
		IFolderLayout outlineFolder = layout.createFolder(
				"actf.flash.outline.folder", IPageLayout.RIGHT, 0.5f,
				"actf.flash.simulator.folder");
		IFolderLayout flashDomFolder = layout.createFolder(
				"actf.flash.flashdom.folder", IPageLayout.BOTTOM, 0.5f,
				"actf.flash.outline.folder");

		reportFolder.addView(IGuiViewIDs.ID_EVENTVIEW);
		rightReportFolder.addView(IGuiViewIDs.ID_PROPERTIESVIEW);
		rightReportFolder.addView(IGuiViewIDs.ID_REPORTVIEW);
		rightReportFolder.addView(IGuiViewIDs.ID_SIBLINGSVIEW);
		simulatorFolder.addView(IGuiViewIDs.ID_SUMMARYVIEW);
		outlineFolder.addView(IGuiViewIDs.ID_OUTLINEVIEW);
		flashDomFolder.addView(IGuiViewIDs.ID_FLASHDOMVIEW);

		layout.getViewLayout(IGuiViewIDs.ID_REPORTVIEW).setCloseable(false);
		layout.getViewLayout(IGuiViewIDs.ID_SIBLINGSVIEW).setCloseable(false);
		layout.getViewLayout(IGuiViewIDs.ID_PROPERTIESVIEW).setCloseable(false);
		layout.getViewLayout(IGuiViewIDs.ID_EVENTVIEW).setCloseable(false);
		layout.getViewLayout(IGuiViewIDs.ID_SUMMARYVIEW).setCloseable(false);
		layout.getViewLayout(IGuiViewIDs.ID_OUTLINEVIEW).setCloseable(false);
		layout.getViewLayout(IGuiViewIDs.ID_FLASHDOMVIEW).setCloseable(false);

		// initializer: moved to Startup.java

	}
}
