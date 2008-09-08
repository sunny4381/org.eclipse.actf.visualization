/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.presentation.util;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionIOException;
import org.eclipse.actf.visualization.engines.lowvision.image.IInt2D;
import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;
import org.eclipse.actf.visualization.engines.lowvision.image.ImageDumpUtil;
import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;
import org.eclipse.actf.visualization.engines.lowvision.image.Int2DFactory;
import org.eclipse.actf.visualization.engines.lowvision.image.PageImageFactory;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

public class SimulateRoom {

	public static synchronized IInt2D getSimulatedInt2D(IPageImage target,
			ParamRoom currentSetting) {

		IInt2D result = Int2DFactory.createInt2D(0, 0);

		try {
			IPageImage simulatedPageImage = PageImageFactory
					.createSimulationPageImage(target, currentSetting
							.getLowVisionType());
			result = simulatedPageImage.getInt2D();
		} catch (ImageException ie) {
			ie.printStackTrace();
		}

		return result;
	}

	public static synchronized ImageData[] doSimulate(IPageImage target,
			ParamRoom currentSetting, String fileName) {

		ImageData[] imageDataArray = new ImageData[0];

		// TODO use memory
		try {
			IInt2D int2d_sim = getSimulatedInt2D(target, currentSetting);
			if (ImageDumpUtil.isLV16BIT()) {
				int2d_sim.writeToBMPFile(fileName, 16);
			} else {
				int2d_sim.writeToBMPFile(fileName);
			}
			ImageLoader loaderAfterSimulate = new ImageLoader();
			imageDataArray = loaderAfterSimulate.load(fileName);
		} catch (LowVisionIOException lvioe) {
			lvioe.printStackTrace();
		}

		return imageDataArray;
	}

	public static synchronized Image doSimulate(IPageImage target,
			ParamRoom currentSetting, Display display, String fileS) {

		Image image = null;

		try {
			IPageImage simulatedPageImage = PageImageFactory
					.createSimulationPageImage(target, currentSetting
							.getLowVisionType());
			IInt2D int2d_sim = simulatedPageImage.getInt2D();

			// TODO use memory

			if (ImageDumpUtil.isLV16BIT()) {
				int2d_sim.writeToBMPFile(fileS, 16);
			} else {
				int2d_sim.writeToBMPFile(fileS);
			}

			image = new Image(display, fileS);

		} catch (ImageException ie) {
			ie.printStackTrace();
		} catch (LowVisionIOException lvioe) {
			lvioe.printStackTrace();
		}

		return image;
	}

	public static IPageImage getPageImage(String fileName, boolean withoutFrame) {

		IPageImage pageImage = null;

		try {
			IInt2D int2d = Int2DFactory.createInt2D(fileName);
			pageImage = PageImageFactory.createPageImage(int2d, withoutFrame);
		} catch (LowVisionIOException lvioe) {
			lvioe.printStackTrace();
		}

		return pageImage;
	}
}
