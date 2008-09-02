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

package org.eclipse.actf.visualization.gui.internal.util;

import org.eclipse.actf.util.win32.HighlightComposite;
import org.eclipse.actf.visualization.gui.ui.views.MSAAViewRegistory;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;



public class Startup implements IStartup {

    public void earlyStartup() {
        Display.getDefault().asyncExec(new Runnable(){
            public void run() {
                MSAAViewRegistory.refreshRootObject();
                HighlightComposite.getOverlayWindow();
            }
        });
    }

}