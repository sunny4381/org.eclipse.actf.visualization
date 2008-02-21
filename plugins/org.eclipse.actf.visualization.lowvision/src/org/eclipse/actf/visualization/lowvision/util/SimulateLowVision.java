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

package org.eclipse.actf.visualization.lowvision.util;

import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;
import org.eclipse.actf.visualization.engines.lowvision.image.Int2D;
import org.eclipse.actf.visualization.engines.lowvision.image.PageImage;
import org.eclipse.actf.visualization.engines.lowvision.image.SimulatedPageImage;
import org.eclipse.actf.visualization.engines.lowvision.io.ImageDumpUtil;
import org.eclipse.actf.visualization.engines.lowvision.io.LowVisionIOException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

public class SimulateLowVision {

	//separated from Lowvision part
	
	public static synchronized Int2D getSimulatedInt2D(PageImage target,
			ParamLowVision currentSetting) {

		Int2D result = new Int2D(0, 0);

		try {
			SimulatedPageImage simulatedPageImage;
			simulatedPageImage = new SimulatedPageImage(target, currentSetting
					.getLowVisionType());
			result = simulatedPageImage.getInt2D();
		} catch (ImageException ie) {
			ie.printStackTrace();
		}

		return result;
	}

	public static synchronized ImageData[] doSimulate(PageImage target,
			ParamLowVision currentSetting, String fileName) {

		ImageData[] imageDataArray = new ImageData[0];

		// TODO use memory
		try {
			Int2D int2d_sim = getSimulatedInt2D(target, currentSetting);
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

	public static synchronized Image doSimulate(PageImage target,
			ParamLowVision currentSetting, Display display, String fileS) {

		Image image = null;

		try {
			SimulatedPageImage simulatedPageImage;
			simulatedPageImage = new SimulatedPageImage(target, currentSetting
					.getLowVisionType());
			Int2D int2d_sim = simulatedPageImage.getInt2D();

			// TODO do not use file

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

	public static PageImage getPageImage(String fileName, boolean withoutFrame) {

		PageImage pageImage = null;

		try {
			Int2D int2d = Int2D.readFromBMPFile(fileName);
			pageImage = new PageImage(int2d, withoutFrame);
		} catch (LowVisionIOException lvioe) {
			lvioe.printStackTrace();
		}

		return pageImage;
	}
}
