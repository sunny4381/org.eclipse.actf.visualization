/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.ui.report.table.popup;

import org.eclipse.actf.visualization.ui.report.internal.Messages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;



public class ClearSelectionAction extends Action {


    private TableViewer _tableViewer;
    
    
    /**
     * @param tableViewer
     */

    public ClearSelectionAction(TableViewer tableViewer) {
        super(Messages.getString("ProblemTable.Clear_Selection_15"));
        this._tableViewer = tableViewer;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        //resultViewer.clearSelection();
        _tableViewer.setSelection(null);
        
        //TODO
        //guidelineSubMenu.setGuidelineItem(new ArrayList());
            
        //TODO recover highlight (use mediator/IVisualization,IDataSoruce)
    }
    
}
