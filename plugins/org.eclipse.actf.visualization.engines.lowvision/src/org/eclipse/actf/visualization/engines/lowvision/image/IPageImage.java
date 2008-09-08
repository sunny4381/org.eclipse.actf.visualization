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
package org.eclipse.actf.visualization.engines.lowvision.image;

import java.awt.image.BufferedImage;
import java.util.List;

import org.eclipse.actf.model.ui.ImagePositionInfo;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.internal.engines.lowvision.problem.LowVisionProblemException;

public interface IPageImage {

	public abstract IInt2D getInt2D();

	public int getWidth();

	public int getHeight();

	public void disposeInt2D();

	public void extractCharacters() throws ImageException;

	public ImagePositionInfo[] getInteriorImagePosition();

	public void setInteriorImagePosition(ImagePositionInfo[] infoArray);

	public boolean isInteriorImageArraySet();

	public List<IProblemItem> checkCharacters(LowVisionType _lvType, String urlS, int frameId)
			throws ImageException, LowVisionProblemException;

	public BufferedImage getBufferedImage();

}