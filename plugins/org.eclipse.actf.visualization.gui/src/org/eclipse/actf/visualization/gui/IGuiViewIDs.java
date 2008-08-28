/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.gui;

import org.eclipse.actf.visualization.gui.ui.views.JAWSTextView;
import org.eclipse.actf.visualization.gui.ui.views.MSAAEventView;
import org.eclipse.actf.visualization.gui.ui.views.MSAAListView;
import org.eclipse.actf.visualization.gui.ui.views.MSAAOutlineView;
import org.eclipse.actf.visualization.gui.ui.views.MSAAProblemsView;
import org.eclipse.actf.visualization.gui.ui.views.MSAAPropertiesView;

public interface IGuiViewIDs {
	
	public static final String ID_FLASHDOMVIEW = "org.eclipse.actf.visualization.flash.ui.views.FlashDOMView";

	public static final String ID_SUMMARYVIEW = JAWSTextView.class.getName();
	public static final String ID_EVENTVIEW = MSAAEventView.class.getName();
	public static final String ID_SIBLINGSVIEW = MSAAListView.class.getName();
	public static final String ID_OUTLINEVIEW = MSAAOutlineView.class.getName();
	public static final String ID_REPORTVIEW = MSAAProblemsView.class.getName();
	public static final String ID_PROPERTIESVIEW = MSAAPropertiesView.class.getName();
}
