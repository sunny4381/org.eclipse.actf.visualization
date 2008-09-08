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

import org.eclipse.actf.visualization.engines.lowvision.LowVisionIOException;

public interface IInt2D {

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract BufferedImage toBufferedImage();

	public abstract void writeToBMPFile(String _fileName)
			throws LowVisionIOException;

	public abstract void writeToBMPFile(String _fileName, int _bitCount)
			throws LowVisionIOException;

	public abstract void fill(int _color);

	public abstract IInt2D cutMargin(int _m) throws ImageException;

	public int[][] getData();

	public IInt2D deepCopy();

}