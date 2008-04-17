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



public interface IGuidelineItem {

    public abstract String getGuidelineName();

    public abstract String getLevel();

    public abstract String getId();

    public abstract String getUrl();

    public abstract void setLevel(String level);

    public abstract void setId(String id);

    public abstract void setUrl(String url);

}
