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

package org.eclipse.actf.visualization.gui.ui.views;

import org.eclipse.swt.graphics.Rectangle;



public interface IFlashDOMView {

    public void findRectangle(Rectangle flashRect, Object objUnknown);

    public void adjustID();
    
    public void refresh();
    
    public void addWindowlessElement(Object objUnknown);

}
