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

package org.eclipse.actf.visualization.eval;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.eclipse.actf.mediator.IACTFReport;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.IProblemItemVisitor;
import org.eclipse.swt.custom.LineStyleListener;




public interface IEvaluationResult extends IACTFReport{

    public void setProblemList(List<IProblemItem> problemList);

    public void addProblemItems(Collection<IProblemItem> c);
    
    public void addProblemItems(IProblemItem[] items);

    public List<IProblemItem> getProblemList();

    public void setSummaryReportText(String summaryReportText);

    public String getSummaryReportText();

    public String getSummaryReportUrl();

    public void setSummaryReportUrl(String reportUrl);

    public void accept(IProblemItemVisitor visitor);

    public String getTargetUrl();

    public void setTargetUrl(String targetUrl);

    public File getSourceFile();

    public void setSourceFile(File sourceFile);
    
    public boolean isShowAllGuidelineItems();
    
    public void setShowAllGuidelineItems(boolean b);

    void setLineStyleListener(LineStyleListener lsl);
    
    LineStyleListener getLineStyleListener();    

    public boolean addAssociateFile(File target);

    public boolean removeAssociatedFile(File target);
    
    public File[] getAssociateFiles();
    
}
