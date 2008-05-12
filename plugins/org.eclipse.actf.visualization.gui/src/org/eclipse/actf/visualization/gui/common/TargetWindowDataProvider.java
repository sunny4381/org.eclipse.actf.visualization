/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.gui.common;

import org.eclipse.actf.model.ui.IModelService;



public abstract class TargetWindowDataProvider {

    /**
     * Get all ModelService
     * @return array of IModelService
     * getActiveModelService is called if null is returned
     */
    public IModelService[] getModelService() {
        return null;
    }
    
    /**
     * Get current active Data source
     * @return IModelService
     */
    public IModelService getActiveModelService() {
        return null;
    }
    
}
