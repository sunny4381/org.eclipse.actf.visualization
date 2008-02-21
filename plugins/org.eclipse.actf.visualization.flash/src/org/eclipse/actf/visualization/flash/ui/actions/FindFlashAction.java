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

package org.eclipse.actf.visualization.flash.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionDelegate;



public class FindFlashAction extends ActionDelegate implements IObjectActionDelegate {

    private IWorkbenchPart targetPart;

    private ISelection selection;

    // IObjectActionDelegate
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.targetPart = targetPart;
    }

    // ActionDelegate
    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = selection;
    }

    public void run(IAction action) {
        if (selection instanceof IStructuredSelection) {
            Object selectedObject = ((IStructuredSelection) selection).getFirstElement();
            if (null != selectedObject) {
                new FlashRectFinder(selectedObject).find(targetPart.getSite().getShell());
            }
        }
    }

}
