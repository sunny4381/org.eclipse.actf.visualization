/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.gui.ui.actions;

import org.eclipse.actf.visualization.gui.Messages;
import org.eclipse.actf.visualization.gui.ui.views.MSAATreeContentProvider;
import org.eclipse.actf.visualization.gui.ui.views.MSAAViewRegistory;
import org.eclipse.jface.action.Action;




public class HideHtmlAction extends Action {

    public HideHtmlAction() {
        super(Messages.getString("msaa.filter_html"), Action.AS_CHECK_BOX); //$NON-NLS-1$
    }
    
    public void adjust() {
        setChecked(MSAATreeContentProvider.getDefault().isHideHtml());
    }

    public void run() {
        MSAATreeContentProvider.getDefault().setHideHtml(isChecked()); 
        MSAAViewRegistory.refreshRootObject();
    }
}
