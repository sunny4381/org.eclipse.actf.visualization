/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Norimasa HAYASHIDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.blind.ui.views;

import org.eclipse.actf.mediator.MediatorEvent;
import org.eclipse.actf.ui.util.AbstractPartListener;
import org.eclipse.actf.visualization.blind.ui.internal.PartControlBlind;
import org.eclipse.actf.visualization.blind.ui.internal.SelectionListenerBlind;
import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.ElementViewerManagerFactory;
import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IElementViewerManager;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.actf.visualization.ui.VisualizationStatusLineContributionItem;
import org.eclipse.actf.visualization.ui.report.table.ResultTableLabelProvider;
import org.eclipse.actf.visualization.ui.report.table.ResultTableSorter;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class BlindView extends ViewPart implements IVisualizationView {

	private IBaseLabelProvider baseLabelProvider;

	private ResultTableSorter viewerSorter;

	private IElementViewerManager elementViewerManager;

	private PartControlBlind partRightBlind;

	public BlindView() {
		super();
		baseLabelProvider = new ResultTableLabelProvider();
		viewerSorter = new ResultTableSorter();
		elementViewerManager = ElementViewerManagerFactory.getInstance();
	}

	public void init(IViewSite site) throws PartInitException {
		setSite(site);
		setStatusLine();
	}

	public void createPartControl(Composite parent) {
		partRightBlind = new PartControlBlind(this, parent);

		// TODO use mediator
		getSite().getPage().addSelectionListener(IVisualizationView.DETAILED_REPROT_VIEW_ID,
				new SelectionListenerBlind(partRightBlind));

		// for element viewer
		elementViewerManager.setHighlightElementListener(partRightBlind);
		addPartListener();

	}

	public void setFocus() {
	}

	public void setStatusMessage(String statusMessage) {
		IContributionItem[] items = getViewSite().getActionBars()
				.getStatusLineManager().getItems();
		for (int i = 0; i < items.length; i++) {
			if (null != items[i]
					&& items[i].getId().equals(
							VisualizationStatusLineContributionItem.ID
									+ IVisualizationView.ID_BLINDVIEW)) {
				((VisualizationStatusLineContributionItem) items[i])
						.setStatusMessage(statusMessage);
			}
		}
	}

	public void setInfoMessage(String infoMessage) {
		IContributionItem[] items = getViewSite().getActionBars()
				.getStatusLineManager().getItems();
		for (int i = 0; i < items.length; i++) {
			if (null != items[i]
					&& items[i].getId().equals(
							VisualizationStatusLineContributionItem.ID
									+ IVisualizationView.ID_BLINDVIEW)) {
				((VisualizationStatusLineContributionItem) items[i])
						.setInfoMessage(infoMessage);
			}
		}
	}

	private void addPartListener() {
		IWorkbenchPage activePage = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		activePage.addPartListener(new AbstractPartListener() {
			public void partActivated(IWorkbenchPartReference partRef) {
				IWorkbenchPart part = partRef.getPart(false);
				if (part instanceof IVisualizationView) {
					if (part.equals(BlindView.this)) {
						elementViewerManager.activateElementViewer();
					} else {
						elementViewerManager.hideElementViewer();
					}
				}
			}
		});
	}

	private void setStatusLine() {
		getViewSite().getActionBars().getStatusLineManager().add(
				new VisualizationStatusLineContributionItem(
						IVisualizationView.ID_BLINDVIEW));
	}

	public IBaseLabelProvider getLabelProvider() {
		return baseLabelProvider;
	}

	public ViewerSorter getTableSorter() {
		viewerSorter.reset();
		return viewerSorter;
	}

	public int getResultTableMode() {
		return MODE_DEFAULT;
	}

	public void doVisualize() {
		if (partRightBlind != null) {
			partRightBlind.doVisualize();
		}
	}

	public void modelserviceChanged(MediatorEvent event) {
		// do nothing
	}

	public void modelserviceInputChanged(MediatorEvent event) {
		// do nothing
	}

	public void reportChanged(MediatorEvent event) {
		// TODO implement report update here
	}

	public void reportGeneratorChanged(MediatorEvent event) {
		// do nothing
	}

	public void setVisualizeMode(int mode) {
		// do nothing
	}

}
