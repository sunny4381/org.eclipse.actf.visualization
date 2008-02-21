/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.lowvision.image;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionException;
import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.engines.lowvision.io.ImageReader;
import org.eclipse.actf.visualization.engines.lowvision.io.LowVisionIOException;
import org.eclipse.actf.visualization.engines.lowvision.operator.LowVisionFilter;

/*
 * Contain simulation result
 */
public class SimulatedPageImage extends PageImage {
	PageImage original = null; // original image

	LowVisionType type = null; // type of simulation

	public SimulatedPageImage(PageImage _pi, LowVisionType _type)
			throws ImageException {
		original = _pi;
		type = _type;
		LowVisionFilter lvFilter = new LowVisionFilter(type);
		BufferedImage bi = null;
		try {
			BufferedImage tmpBufIm = original.getBufferedImage();
			bi = lvFilter.filter(tmpBufIm, null);
			tmpBufIm = null;
		} catch (LowVisionException lve) {
			// lve.printStackTrace();
			throw new ImageException(
					"The original PageImage cannot be filtered.");
		}
		init(bi);
		bi = null;
	}

	// for debug
	public static SimulatedPageImage readFromFile(
			String _originalImageFilePath, String _sequenceFilePath)
			throws ImageException, LowVisionIOException {
		Int2D i2d = ImageUtil.bufferedImageToInt2D(ImageReader
				.readBufferedImage(_originalImageFilePath));
		PageImage pi = new PageImage(i2d);
		FileReader fr = null;
		try {
			fr = new FileReader(_sequenceFilePath);
		} catch (FileNotFoundException fnfe) {
			// fnfe.printStackTrace();
			throw new LowVisionIOException("File: " + _sequenceFilePath
					+ " cannot be found.");
		}
		SimulatedPageImage spi = null;
		try {
			LowVisionType lvType = new LowVisionType(new BufferedReader(fr));
			spi = new SimulatedPageImage(pi, lvType);
		} catch (LowVisionException se) {
			// se.printStackTrace();
			throw new ImageException(
					"SequenceException occurred in readFromFile()");
		}
		return (spi);
	}
}
