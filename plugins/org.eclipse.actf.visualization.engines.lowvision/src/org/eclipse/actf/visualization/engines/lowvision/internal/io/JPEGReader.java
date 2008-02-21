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
package org.eclipse.actf.visualization.engines.lowvision.internal.io;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.actf.visualization.engines.lowvision.io.LowVisionIOException;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

public class JPEGReader {
	public static BufferedImage readBufferedImage(String _fileName)
			throws LowVisionIOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(_fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new LowVisionIOException("The file was not found: "
					+ _fileName);
		}

		JPEGImageDecoder dec = null;
		try {
			dec = JPEGCodec.createJPEGDecoder(fis);
			BufferedImage bi = dec.decodeAsBufferedImage();
			fis.close();
			return (bi);
		} catch (IOException e) {
			e.printStackTrace();
			throw new LowVisionIOException(
					"IO error occurred while decoding JPEG file.");
		}
	} 
}
