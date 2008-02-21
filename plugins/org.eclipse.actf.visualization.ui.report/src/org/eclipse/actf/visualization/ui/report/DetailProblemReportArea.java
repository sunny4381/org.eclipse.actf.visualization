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
package org.eclipse.actf.visualization.ui.report;

import org.eclipse.actf.model.IModelService;
import org.eclipse.actf.model.events.IModelServiceEventListener;
import org.eclipse.actf.visualization.events.IVisualizationEventListener;
import org.eclipse.actf.visualization.events.VisualizationEvent;
import org.eclipse.actf.visualization.ui.report.table.ResultTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class DetailProblemReportArea extends SashForm implements IVisualizationEventListener, IModelServiceEventListener {

	private ProblemTree _problemTree;
    private ResultTableViewer resultTableViewer;

	public DetailProblemReportArea(Composite parent, int style) {
		super(parent, style);
		init();
	}

	private void init(){		
		setOrientation(SWT.HORIZONTAL);
		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.horizontalSpan = 1;
		setLayoutData(gridData);

		this._problemTree = new ProblemTree(this);
		resultTableViewer = new ResultTableViewer(this);
        
		this._problemTree.setResultTableViewer(resultTableViewer);
		
		setWeights(new int[] { 2, 9 });
		
	}
	
    public void visualizerChanged(VisualizationEvent checkerEvent) {
        this._problemTree.visualizerChanged(checkerEvent);
    }

    public ProblemTree getProblemTree() {
        return _problemTree;
    }

    public void modelserviceChanged(IModelService modelService) {
        resultTableViewer.modelserviceChanged(modelService);
    }

	public void inputChanged(IModelService modelService) {
		// TODO Auto-generated method stub
		
	}
}
