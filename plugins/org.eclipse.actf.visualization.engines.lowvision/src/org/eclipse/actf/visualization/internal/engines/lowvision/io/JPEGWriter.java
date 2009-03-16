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
package org.eclipse.actf.visualization.internal.engines.lowvision.io;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionIOException;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class JPEGWriter {
	public static void writeBufferedImage(BufferedImage _bi, String _fileName)
			throws LowVisionIOException {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(_fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new LowVisionIOException("The file was not found: " //$NON-NLS-1$
					+ _fileName);
		}

		try {
			JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(bos);
			enc.encode(_bi);
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new LowVisionIOException(
					"IO error occurred while decoding/closing."); //$NON-NLS-1$
		}
	}
}
