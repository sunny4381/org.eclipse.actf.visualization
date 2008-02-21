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
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.InputStream;

import org.eclipse.actf.visualization.engines.lowvision.internal.io.BMPReader;
import org.eclipse.actf.visualization.engines.lowvision.internal.io.BMPWriter;
import org.eclipse.actf.visualization.engines.lowvision.io.LowVisionIOException;

/*
 * data[y][x] = BufferdImage.TYPE_INT_RGB (null, R, G, B)
 * 
 * data[0][0] is top left
 */
public class Int2D {
	public int width;

	public int height;

	public int[][] data;

	public Int2D(int _w, int _h) {
		width = _w;
		height = _h;
		data = new int[height][width];
	}

	public Int2D(int _w, int _h, int[][] _array) throws ImageException {
		this(_w, _h);
		if (_array.length < _h || _array[0].length < _w) {
			throw new ImageException("Out of range");
		}
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				this.data[j][i] = _array[j][i];
			}
		}
	}

	public Int2D(Int2D _src) {
		this(_src.width, _src.height);
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				this.data[j][i] = _src.data[j][i];
			}
		}
	}

	public Int2D(BufferedImage _bi) {
		this(_bi.getWidth(), _bi.getHeight());
		WritableRaster srcRaster = _bi.copyData(null);
		DataBufferInt srcBufInt = (DataBufferInt) (srcRaster.getDataBuffer());
		int[] srcArray = srcBufInt.getData();
		int k = 0;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				data[j][i] = srcArray[k];
				k++;
			}
		}
	}

	public static Int2D deepCopy(Int2D _src) {
		int width = _src.width;
		int height = _src.height;
		Int2D dest = new Int2D(width, height);
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				dest.data[j][i] = _src.data[j][i];
			}
		}
		return (dest);
	}

	public Int2D deepCopy() {
		return (deepCopy(this));
	}

	public int getWidth() {
		return (width);
	}

	public int getHeight() {
		return (height);
	}

	public static Int2D readFromBMPFile(String _fileName)
			throws LowVisionIOException {
		return (BMPReader.readInt2D(_fileName));
	}

	public static Int2D readFromBMPInputStream(InputStream _is)
			throws LowVisionIOException {
		return (BMPReader.readInt2D(_is));
	}

	public BufferedImage toBufferedImage() {
		return (ImageUtil.int2DToBufferedImage(this));
	}

	public void writeToBMPFile(String _fileName) throws LowVisionIOException {
		BMPWriter.writeInt2D(this, _fileName);
	}

	public void writeToBMPFile(String _fileName, int _bitCount)
			throws LowVisionIOException {
		BMPWriter.writeInt2D(this, _fileName, _bitCount);
	}

	public static void writeToBMPFile(Int2D _src, String _fileName)
			throws LowVisionIOException {
		BMPWriter.writeInt2D(_src, _fileName);
	}

	public static void writeToBMPFile(Int2D _src, String _fileName,
			int _bitCount) throws LowVisionIOException {
		BMPWriter.writeInt2D(_src, _fileName, _bitCount);
	}

	public void fill(int _color) {
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				data[j][i] = _color;
			}
		}
	}

	public Int2D cutMargin(int _m) throws ImageException {
		if (width <= 2 * _m || height <= 2 * _m) {
			throw new ImageException("The margin is too wide.");
		}
		int newW = width - 2 * _m;
		int newH = height - 2 * _m;
		Int2D i2d = new Int2D(newW, newH);
		for (int j = 0; j < newH; j++) {
			for (int i = 0; i < newW; i++) {
				i2d.data[j][i] = this.data[j + _m][i + _m];
			}
		}
		return (i2d);
	}

	public void drawContour(ConnectedComponent _cc, int _color,
			boolean _overWrite) {
		Int2D dest = this;
		if (!_overWrite) {
			dest = this.deepCopy();
		}

		int ccLeft = _cc.left;
		int ccTop = _cc.top;
		int ccWidth = _cc.shape.width;
		int ccHeight = _cc.shape.height;

		for (int j = 0; j < ccHeight; j++) {
			if (_cc.shape.data[j][0] != 0) {
				dest.data[j + ccTop][ccLeft] = _color;
			}
			if (_cc.shape.data[j][ccWidth - 1] != 0) {
				dest.data[j + ccTop][ccWidth - 1 + ccLeft] = _color;
			}
		}
		for (int i = 0; i < ccWidth; i++) {
			if (_cc.shape.data[0][i] != 0) {
				dest.data[ccTop][i + ccLeft] = _color;
			}
			if (_cc.shape.data[ccHeight - 1][0] != 0) {
				dest.data[ccHeight - 1 + ccTop][ccLeft] = _color;
			}
		}

		for (int j = 1; j < ccHeight - 1; j++) {
			for (int i = 1; i < ccWidth - 1; i++) {
				if (_cc.shape.data[j][i] != 0) {
					if (_cc.shape.data[j][i - 1] == 0
							|| _cc.shape.data[j][i + 1] == 0
							|| _cc.shape.data[j - 1][i] == 0
							|| _cc.shape.data[j + 1][i] == 0) {
						dest.data[j + ccTop][i + ccLeft] = _color;
					}
				}
			}
		}
	}
}
