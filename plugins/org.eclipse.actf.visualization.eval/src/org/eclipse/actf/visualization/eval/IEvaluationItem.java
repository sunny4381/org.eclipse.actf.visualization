/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval;

import org.eclipse.swt.graphics.Image;


public interface IEvaluationItem {
    public static final int SEV_ERROR = 1;

    public static final int SEV_WARNING = 2; //add "Possible error"?

    public static final int SEV_INFO = 4;
    
    public static final String SEV_ERROR_STR = "error";

    public static final String SEV_WARNING_STR = "warning";

    public static final String SEV_INFO_STR = "info";


    public String getId();

    public int getSeverity();

    public String getSeverityStr();
    
    public int[] getMetricsScores();

    public String[] getTableDataGuideline();

    public String[] getTableDataMetrics();

    public Image[] getMetricsIcons();
    
    public IGuidelineItem[] getGuidelines();
    
    public String createDescription();
    
    public String createDescription(String targetString);

}
