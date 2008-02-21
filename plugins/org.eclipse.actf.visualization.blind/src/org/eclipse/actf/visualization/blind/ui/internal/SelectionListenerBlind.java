/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.blind.ui.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.actf.mediator.Mediator;
import org.eclipse.actf.model.IModelService;
import org.eclipse.actf.model.IWebBrowserACTF;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetId;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetSourceInfo;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.ui.report.table.SrcViewerForPT;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.w3c.dom.Node;

public class SelectionListenerBlind implements ISelectionListener {

	private PartControlBlind prb;

	public SelectionListenerBlind(PartControlBlind prb) {
		this.prb = prb;
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection == null || !(selection instanceof IStructuredSelection)) {
			System.err.println(this.getClass().getName() + ":" + "Iselection");
			return;
		}

		IModelService modelService = Mediator.getInstance()
				.getActiveModelService();

		// TODO
		SrcViewerForPT srcViewerForPT = SrcViewerForPT.getInstance();
		ArrayList<HighlightTargetSourceInfo> srcLineList = new ArrayList<HighlightTargetSourceInfo>();

		if (modelService != null && modelService instanceof IWebBrowserACTF) {
			((IWebBrowserACTF) modelService).recoveryHighlight();
		}

		prb.clearHighlight(); //$NON-NLS-1$
		List<HighlightTargetId> validIdList = new ArrayList<HighlightTargetId>();

		IStructuredSelection structuredSelection = (IStructuredSelection) selection;

		Node targetNode = null;
		for (Iterator i = structuredSelection.iterator(); i.hasNext();) {
			Object target = i.next();
			if (target instanceof IProblemItem) {
				IProblemItem tmpItem = (IProblemItem) target;
				if (prb.getCheckResult().getProblemList().contains(tmpItem)) {
					targetNode = tmpItem.getTargetNode();
					srcLineList.addAll(Arrays.asList(tmpItem
							.getHighlightTargetSoruceInfo()));

					if (tmpItem.isCanHighlight()) {
						HighlightTargetId[] ids = tmpItem
								.getHighlightTargetIds();
						for (int j = 0; j < ids.length; j++) {
							validIdList.add(ids[j]);
						}
					}
				}
			}
		}

		// TODO
		if (srcViewerForPT != null) {
			HighlightTargetSourceInfo[] srcInfo = new HighlightTargetSourceInfo[srcLineList
					.size()];
			srcLineList.toArray(srcInfo);
			srcViewerForPT.highlightSrcViewer(srcInfo, prb.getCheckResult()
					.getSourceFile());
		}

		HighlightTargetId[] targets = new HighlightTargetId[validIdList.size()];
		validIdList.toArray(targets);

		prb.highlight(validIdList);

		if (targets.length == 1) {
			// Scroll Browser
			if (null != modelService) {
				modelService.jumpToNode(targetNode);
			}

		}
	}

}
