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

import java.io.InputStream;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionIOException;
import org.eclipse.actf.visualization.engines.lowvision.internal.io.BMPReader;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Int2D;

public class Int2DFactory {

	public static IInt2D createInt2D(int width, int height) {
		return new Int2D(width, height);
	}

	public static IInt2D createInt2D(int width, int height, int[][] data)
			throws ImageException {
		return new Int2D(width, height, data);
	}

	public static IInt2D createInt2D(String fileName)
			throws LowVisionIOException {
		return (BMPReader.readInt2D(fileName));
	}

	public static IInt2D createInt2D(InputStream is)
			throws LowVisionIOException {
		return (BMPReader.readInt2D(is));
	}

}
