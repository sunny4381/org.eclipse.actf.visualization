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

package org.eclipse.actf.visualization.eval.problem;


public class HighlightTargetId {
    int startId;
    int endId;
    
    public HighlightTargetId(int startId, int endId){
        if(endId < startId){
            endId = startId;
        }
        this.startId = startId;
        this.endId = endId;
    }

    public int getEndId() {
        return endId;
    }

    public int getStartId() {
        return startId;
    }    
    
}
