/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Norimasa HAYASHIDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.ui.report.views;

import org.eclipse.actf.model.IModelService;
import org.eclipse.actf.visualization.events.VisualizationEvent;
import org.eclipse.actf.visualization.ui.report.IReportViewer;
import org.eclipse.actf.visualization.ui.report.SummaryProblemReportArea;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;



public class SummaryReportView extends ViewPart implements IReportViewer{

	public static final String ID = SummaryReportView.class.getName();

    private SummaryProblemReportArea problemArea;
    
	public SummaryReportView() {
		super();
	}

	public void createPartControl(Composite parent) {
		problemArea = new SummaryProblemReportArea(parent, SWT.NONE);
	}

	public void setFocus() {
		
	}

    public void visualizerChanged(VisualizationEvent vizEvent) {
        problemArea.visualizerChanged(vizEvent);        
    }

    public void modelserviceChanged(IModelService modelService) {
    }

	public void inputChanged(IModelService modelService) {
		// TODO Auto-generated method stub
		
	}
    
}
