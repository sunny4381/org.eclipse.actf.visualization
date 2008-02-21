/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Norimasa HAYASHIDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.blind.ui.internal;

import org.eclipse.actf.visualization.blind.ui.actions.BlindSaveAction;
import org.eclipse.actf.visualization.blind.ui.actions.BlindVisualizationAction;
import org.eclipse.actf.visualization.engines.blind.html.ui.actions.BlindOpenIdCssAction;
import org.eclipse.actf.visualization.engines.blind.ui.actions.BlindSettingAction;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;


public class BlindToolBar extends Composite {

    PartControlBlind prb;
    
	public BlindToolBar(Composite parent, int style, PartControlBlind prb) {
		super(parent, style);
        this.prb = prb;
		initLayout(parent);
	}

	private void initLayout(Composite parent) {

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 0;
		gridLayout.marginBottom = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 1;
		setLayout(gridLayout);

		ToolBar toolBar = new ToolBar(this, SWT.RIGHT);
		ToolBarManager toolBarManager = new ToolBarManager(toolBar);

		ActionContributionItem visualizeActionItem = new ActionContributionItem(new BlindVisualizationAction(prb));
		visualizeActionItem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBarManager.add(visualizeActionItem);
		toolBarManager.add(new Separator());

		ActionContributionItem settingActionItem = new ActionContributionItem(new BlindSettingAction());
		settingActionItem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBarManager.add(settingActionItem);
		toolBarManager.add(new Separator());

		ActionContributionItem saveActionItem = new ActionContributionItem(new BlindSaveAction(prb));
		saveActionItem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBarManager.add(saveActionItem);
		toolBarManager.add(new Separator());

		ActionContributionItem openIdCssActionItem = new ActionContributionItem(new BlindOpenIdCssAction());
		openIdCssActionItem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBarManager.add(openIdCssActionItem);

		toolBarManager.update(true);
	}

}
