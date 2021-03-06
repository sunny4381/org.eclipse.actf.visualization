/*******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Norimasa HAYASHIDA - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.gui.ui;

import org.eclipse.actf.visualization.gui.IGuiViewIDs;
import org.eclipse.actf.visualization.ui.IVisualizationPerspective;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class GUIPerspective implements IPerspectiveFactory, IVisualizationPerspective {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		IFolderLayout reportFolder = layout
				.createFolder("actf.report.folder", IPageLayout.BOTTOM, //$NON-NLS-1$
						0.7f, editorArea);
		IFolderLayout rightReportFolder = layout.createFolder(
				"actf.report.left.folder", IPageLayout.RIGHT, 0.5f, //$NON-NLS-1$
				"actf.report.folder"); //$NON-NLS-1$
		IFolderLayout summaryFolder = layout.createFolder("actf.summary.folder", //$NON-NLS-1$
				IPageLayout.RIGHT, 0.3f, editorArea);
		IFolderLayout outlineFolder = layout.createFolder(
				"actf.outline.folder", IPageLayout.RIGHT, 1/3f, "actf.summary.folder"); //$NON-NLS-1$ //$NON-NLS-2$
		IFolderLayout propertyFolder = layout.createFolder(
				"actf.property.folder", IPageLayout.RIGHT, 0.5f, //$NON-NLS-1$
				"actf.outline.folder"); //$NON-NLS-1$
		try {
			reportFolder.addView(IGuiViewIDs.ID_EVENTVIEW);
			rightReportFolder.addView(IGuiViewIDs.ID_REPORTVIEW);
			rightReportFolder.addView(IGuiViewIDs.ID_SIBLINGSVIEW);
			outlineFolder.addView(IGuiViewIDs.ID_OUTLINEVIEW);
			summaryFolder.addView(IGuiViewIDs.ID_SUMMARYVIEW);
			propertyFolder.addView(IGuiViewIDs.ID_PROPERTIESVIEW);
			layout.getViewLayout(IGuiViewIDs.ID_EVENTVIEW).setCloseable(false);
			layout.getViewLayout(IGuiViewIDs.ID_REPORTVIEW).setCloseable(false);
			layout.getViewLayout(IGuiViewIDs.ID_SIBLINGSVIEW).setCloseable(
					false);
			layout.getViewLayout(IGuiViewIDs.ID_OUTLINEVIEW)
					.setCloseable(false);
			layout.getViewLayout(IGuiViewIDs.ID_SUMMARYVIEW)
					.setCloseable(false);
			layout.getViewLayout(IGuiViewIDs.ID_PROPERTIESVIEW).setCloseable(
					false);
		} catch (Exception e) {
		}
	}
}
