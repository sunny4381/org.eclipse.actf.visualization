/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.eval.problem;

import org.eclipse.actf.visualization.ui.IPositionSize;
import org.eclipse.swt.graphics.Image;

public interface IProblemItemImage extends IProblemItem, IPositionSize, ILowvisionProblemSubtype{
	
    public static final int ICON_IRO = 1;

    public static final int ICON_BOKE = 3;

	public abstract String getBackgroundS();

	public abstract String getForegroundS();

	public abstract int getFrameId();

	public abstract int getFrameOffset();

	public abstract String getFrameUrlS();

	public abstract int getIconId();

	public abstract Image getImageIcon();

	public abstract String getImageIconTooltip();

	public abstract int getSeverityLV();

	public abstract short getSubType();

	public void setArea(int area);

	public void setBackgroundS(String backgroundS);

	public void setForegroundS(String foregroundS);

	public void setFrameId(int frameId);

	public void setFrameOffset(int frameOffset);

	public void setFrameUrlS(String frameUrlS);

	public void setSeverityLV(int severityLV);

	public void setSubType(short subType);

}