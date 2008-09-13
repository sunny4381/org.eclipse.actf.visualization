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

package org.eclipse.actf.visualization.internal.eval.guideline;

import java.util.Comparator;



public class MetricsNameComparator implements Comparator<String> {

    public int compare(String s1, String s2) {
        
        if(s1.equalsIgnoreCase(s2)){
            return(0);
        }
        
        if(s1.equalsIgnoreCase("compliance")){
            return(-1);
        }
        if(s2.equalsIgnoreCase("compliance")){
            return(1);
        }
        
        if(s1.equalsIgnoreCase("listenability")){
            return(-1);
        }
        if(s2.equalsIgnoreCase("listenability")){
            return(1);
        }
        
        if(s1.equalsIgnoreCase("navigability")){
            return(-1);
        }
        if(s2.equalsIgnoreCase("navigability")){
            return(1);
        }
        
        return(s1.compareTo(s2));
        
    }
    
}
