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

package org.eclipse.actf.visualization.internal.blind.odfbyhtml.ui;

import org.eclipse.actf.model.ui.editors.ooo.OOoEditor;
import org.eclipse.actf.model.ui.editors.ooo.initializer.util.OOoEditorInitUtil;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.actf.visualization.ui.IVisualizationPerspective;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class ODFPerspective implements IPerspectiveFactory, IVisualizationPerspective {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);

		IFolderLayout reportFolder = layout.createFolder(
				"actf.odf.report.folder", IPageLayout.BOTTOM, 0.7f,
				editorArea);
		reportFolder.addView(IVisualizationView.SUMMARY_REPORT_VIEW_ID);
		reportFolder.addView(IVisualizationView.DETAILED_REPROT_VIEW_ID);
		layout.getViewLayout(IVisualizationView.SUMMARY_REPORT_VIEW_ID).setCloseable(false);
		layout.getViewLayout(IVisualizationView.DETAILED_REPROT_VIEW_ID).setCloseable(false);

		IFolderLayout simulatorFolder = layout.createFolder(
				"actf.odf.simulator.folder", IPageLayout.RIGHT, 0.5f,
				editorArea);
		simulatorFolder.addView(IVisualizationView.ID_BLINDVIEW);
		simulatorFolder.addView(IVisualizationView.ID_LOWVISIONVIEW);
		layout.getViewLayout(IVisualizationView.ID_BLINDVIEW).setCloseable(false);
		layout.getViewLayout(IVisualizationView.ID_LOWVISIONVIEW).setCloseable(false);
		// Add Room Simulator view
		simulatorFolder.addView(IVisualizationView.ID_PRESENTATIONVIEW);
		layout.getViewLayout(IVisualizationView.ID_PRESENTATIONVIEW).setCloseable(false);

		PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.addPerspectiveListener(new IPerspectiveListener() {
					public void perspectiveActivated(IWorkbenchPage page,
							IPerspectiveDescriptor perspective) {
						if (ID_ODF_PERSPECTIVE.equals(perspective.getId())) {
							if (OOoEditorInitUtil.isOOoInstalled(true)) {
								ModelServiceUtils.launch(null, OOoEditor.ID);
							}
						}
					}

					public void perspectiveChanged(IWorkbenchPage page,
							IPerspectiveDescriptor perspective, String changeId) {
					}
				});
	}
}
