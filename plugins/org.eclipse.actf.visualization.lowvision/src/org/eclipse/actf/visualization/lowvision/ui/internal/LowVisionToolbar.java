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

package org.eclipse.actf.visualization.lowvision.ui.internal;

import org.eclipse.actf.visualization.lowvision.ui.actions.LowVisionSaveAction;
import org.eclipse.actf.visualization.lowvision.ui.actions.LowVisionSettingsAction;
import org.eclipse.actf.visualization.lowvision.ui.actions.LowVisionSimulateAction;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;


public class LowVisionToolbar extends Composite {

	private Button _wholePageButton;

	private PartControlLowVision lowVisionCtrl;

	public LowVisionToolbar(Composite parent, int style,
			PartControlLowVision lowVisionCtrl) {
		super(parent, style);
		this.lowVisionCtrl = lowVisionCtrl;
		initLayout(parent);
	}

	private void initLayout(Composite parent) {

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 0;
		gridLayout.marginBottom = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 1;
		gridLayout.numColumns = 3;
		setLayout(gridLayout);

		ToolBar toolBar = new ToolBar(this, SWT.RIGHT);
		ToolBarManager toolBarManager = new ToolBarManager(toolBar);

		ActionContributionItem simulateActionItem = new ActionContributionItem(
				new LowVisionSimulateAction(lowVisionCtrl));
		simulateActionItem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBarManager.add(simulateActionItem);
		toolBarManager.add(new Separator());

		ActionContributionItem settingsActionItem = new ActionContributionItem(
				new LowVisionSettingsAction());
		settingsActionItem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBarManager.add(settingsActionItem);
		toolBarManager.add(new Separator());

		ActionContributionItem saveActionItem = new ActionContributionItem(
				new LowVisionSaveAction(lowVisionCtrl));
		saveActionItem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBarManager.add(saveActionItem);

		toolBarManager.update(true);

		this._wholePageButton = new Button(this, SWT.CHECK);
		this._wholePageButton.setText(Messages.LowVisionView_whole_page_1);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		this._wholePageButton.setLayoutData(gridData);
		this._wholePageButton.setSelection(true);
	}

	public Button getWholePageButton() {
		return this._wholePageButton;
	}
}
