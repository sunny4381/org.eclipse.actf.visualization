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

import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.PageImage;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.SimulatedPageImage;

public class PageImageFactory {

	public static IPageImage createPageImage() {
		return new PageImage();
	}

	public static IPageImage createPageImage(IInt2D int2d) {
		return new PageImage(int2d, true);
	}

	public static IPageImage createPageImage(IInt2D int2d,
			boolean removeScrollBar) {
		return new PageImage(int2d, removeScrollBar);
	}

	public static IPageImage createSimulationPageImage(IPageImage original,
			LowVisionType type) throws ImageException{
		return new SimulatedPageImage(original, type);
	}

}
