/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.ui.report.table.popup;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.actf.visualization.eval.IGuidelineItem;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.ui.report.internal.Messages;
import org.eclipse.actf.visualization.ui.report.table.ResultTableViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;


public class GuidelineSubMenu extends MenuManager {

	private ResultTableViewer _resultTableViewer;

	private TableViewer _tableViewer;

	private Action _dummy;

	private GuidelineHolder _guidelineHolder = GuidelineHolder
			.getInstance();

	public GuidelineSubMenu(ResultTableViewer resultTableViewer) {
		super(Messages.getString("ProblemTable.View_Guideline_16"));

		this._resultTableViewer = resultTableViewer;
		this._tableViewer = resultTableViewer.getTableViewer();

		this._tableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent arg0) {
						List tmpList = ((IStructuredSelection) arg0
								.getSelection()).toList();
						setGuidelineItem(tmpList);
					}
				});

		this._dummy = new Action("guideline help is not available.") {
		};
		this._dummy.setEnabled(false);
		add(this._dummy);
	}

	public void setGuidelineItem(List target) {
		TreeSet<IGuidelineItem> tmpSet = new TreeSet<IGuidelineItem>(new Comparator<IGuidelineItem>() {
			public int compare(IGuidelineItem o1, IGuidelineItem o2) {
				return (o1.toString().compareTo(o2.toString()));//TODO
			}
		});

		for (Iterator i = target.iterator(); i.hasNext();) {
			try {
				IProblemItem tmpItem = (IProblemItem) i.next();
				tmpSet.addAll(Arrays.asList(tmpItem.getEvaluationItem()
						.getGuidelines()));
			} catch (Exception e) {
			}
		}

		this.removeAll();

		for (Iterator i = tmpSet.iterator(); i.hasNext();) {
			IGuidelineItem tmpItem = (IGuidelineItem) i.next();
			if (tmpItem.getUrl() != null && tmpItem.getUrl().length() != 0) {
				// Lowvision-> show all
				if (_resultTableViewer.isShowAllGuidelineItems()
						|| this._guidelineHolder.isMatchedInTopLevel(tmpItem)) {
					add(new ShowGuidelineAction(tmpItem));
				}
			}
		}

		if (this.getItems().length == 0) {
			this.add(this._dummy);
		}
	}

}
