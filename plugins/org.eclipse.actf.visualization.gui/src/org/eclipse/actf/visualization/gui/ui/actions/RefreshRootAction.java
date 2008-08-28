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

import org.eclipse.actf.visualization.gui.internal.util.GuiImages;
import org.eclipse.actf.visualization.gui.internal.util.Messages;
import org.eclipse.actf.visualization.gui.ui.views.MSAAViewRegistory;
import org.eclipse.jface.action.Action;




public class RefreshRootAction extends Action {

    public RefreshRootAction() {
        super(Messages.getString("msaa.refresh")); //$NON-NLS-1$
        setToolTipText(Messages.getString("msaa.refresh")); //$NON-NLS-1$
        setImageDescriptor(GuiImages.IMAGE_REFRESH);
    }

    public void run() {
        MSAAViewRegistory.refreshRootObject();
    }
}
