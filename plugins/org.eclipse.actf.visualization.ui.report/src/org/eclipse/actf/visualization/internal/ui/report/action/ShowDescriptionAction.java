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

package org.eclipse.actf.visualization.internal.ui.report.action;

import java.util.List;

import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.IProblemConst;
import org.eclipse.actf.visualization.internal.ui.report.Messages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;


public class ShowDescriptionAction extends Action {

    private TableViewer tableViewer;
    
    private IProblemItem curItem;
    
    /**
     * 
     */
    public ShowDescriptionAction(TableViewer tableViewer) {
        super(Messages.getString("ProblemTable.6"));
        this.tableViewer = tableViewer;
        
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent arg0) {
                List tmpList = ((IStructuredSelection) arg0.getSelection()).toList();
                if(tmpList==null||tmpList.size()>1||tmpList.size()==0){
                    setIProblemItem(null);
                }else{
                    try{
                        setIProblemItem((IProblemItem)tmpList.get(0));
                    }catch(Exception e){
                        setIProblemItem(null);
                    }
                }
            }
        });
        
        this.setEnabled(false);
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        MessageDialog.openInformation(tableViewer.getControl().getShell(),IProblemConst.TITLE_DESCRIPTION,curItem.getDescription());
    }
    
    public void setIProblemItem(IProblemItem target){
        if(target==null){
            this.setEnabled(false);
        }else{
            this.setEnabled(true);
            curItem = target;
        }        
    }
    
}
