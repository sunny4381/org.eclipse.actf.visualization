/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.lowvision.ui.views;

import org.eclipse.actf.mediator.MediatorEvent;
import org.eclipse.actf.visualization.IVisualizationView;
import org.eclipse.actf.visualization.VisualizationStatusLineContributionItem;
import org.eclipse.actf.visualization.lowvision.ui.internal.PartControlLowVision;
import org.eclipse.actf.visualization.ui.report.table.ResultTableLabelProviderLV;
import org.eclipse.actf.visualization.ui.report.table.ResultTableSorterLV;
import org.eclipse.actf.visualization.ui.report.views.DetailedReportView;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;


public class LowVisionView extends ViewPart implements IVisualizationView {

	public static final String ID = LowVisionView.class.getName();
    
    private PartControlLowVision partRightLowVision;
    
    private IBaseLabelProvider baseLabelProvider = new ResultTableLabelProviderLV();
    private ResultTableSorterLV viewerSorter = new ResultTableSorterLV();
    

	public LowVisionView() {		
		super();
	}

	public void init(IViewSite site) throws PartInitException {
		setSite(site);
		setStatusLine();
	}
	
	

	public void createPartControl(Composite parent) {
		partRightLowVision = new PartControlLowVision(this, parent);
        //TODO
        getSite().getPage().addSelectionListener(DetailedReportView.ID, partRightLowVision);
//        ((IViewSite)getSite()).getActionBars().setGlobalActionHandler("visualizationAction", new Action(){
//            public void run() {
//                partRightLowVision.doSimulate();
//            }
//        }
//        );

	}

	public void setFocus() {
	}

	public void setStatusMessage(String statusMessage) {
		IContributionItem[] items = getViewSite().getActionBars().getStatusLineManager().getItems();
		for (int i = 0; i < items.length; i++) {
			if (null != items[i] && items[i].getId().equals(VisualizationStatusLineContributionItem.ID + ID)) {
				((VisualizationStatusLineContributionItem) items[i]).setStatusMessage(statusMessage);
			}
		}
	}

	public void setInfoMessage(String infoMessage) {
		IContributionItem[] items = getViewSite().getActionBars().getStatusLineManager().getItems();
		for (int i = 0; i < items.length; i++) {
			if (null != items[i] && items[i].getId().equals(VisualizationStatusLineContributionItem.ID + ID)) {
				((VisualizationStatusLineContributionItem) items[i]).setInfoMessage(infoMessage);
			}
		}
	}
	
	private void setStatusLine() {
		getViewSite().getActionBars().getStatusLineManager().add(new VisualizationStatusLineContributionItem(ID));
	}

    public IBaseLabelProvider getLabelProvider() {
        return baseLabelProvider;
    }

    public ViewerSorter getTableSorter() {
        viewerSorter.reset();
        return viewerSorter;
    }

    public int getResultTableMode() {
        return MODE_LOWVISION;
    }
    
    public void doVisualize(){
    	if(partRightLowVision!=null){
    		partRightLowVision.doSimulate();
    	}
    }

	public void modelserviceChanged(MediatorEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void modelserviceInputChanged(MediatorEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void reportChanged(MediatorEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void reportGeneratorChanged(MediatorEvent event) {
		// TODO Auto-generated method stub
		
	}

}
