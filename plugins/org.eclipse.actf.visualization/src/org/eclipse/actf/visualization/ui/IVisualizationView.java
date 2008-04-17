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

package org.eclipse.actf.visualization.ui;

import org.eclipse.actf.mediator.IACTFReportGenerator;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ViewerSorter;

public interface IVisualizationView extends IACTFReportGenerator{
    public static final int MODE_DEFAULT = 0;
    public static final int MODE_LOWVISION = 1;
    
    IBaseLabelProvider getLabelProvider();
    ViewerSorter getTableSorter();
    
    public void setStatusMessage(String statusMessage);
    public void setInfoMessage(String infoMessage);
    
    int getResultTableMode();    
    void doVisualize();
    
}
