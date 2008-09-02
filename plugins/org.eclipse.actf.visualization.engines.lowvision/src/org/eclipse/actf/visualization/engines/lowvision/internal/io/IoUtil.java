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

public class IoUtil {
	public static final short TYPE_UNKNOWN = 0;

	public static final short TYPE_BMP = 1;

	public static final short TYPE_DIB = 1; // same as BMP

	public static final short TYPE_JPEG = 2;

	public static final short TYPE_PBM = 3; // Magic number = "P1"

	// public static final short TYPE_PBMRAW = 4; // Magic number = "P4"
	
	public static final short TYPE_GIF = 5;

	public static final short TYPE_PNG = 6;

	public static short getFileType(String _fileName) {
		if (_fileName == null)
			return (TYPE_UNKNOWN);
		int ind = _fileName.lastIndexOf(".");
		if (ind == -1)
			return (TYPE_UNKNOWN);
		String ext = _fileName.substring(ind + 1).toLowerCase();
		if (ext.equals("bmp"))
			return (TYPE_BMP);
		else if (ext.equals("dib"))
			return (TYPE_BMP);
		else if (ext.equals("jpg") || ext.equals("jpeg"))
			return (TYPE_JPEG);
		else if (ext.equals("pbm"))
			return (TYPE_PBM);
		else if (ext.equals("gif"))
			return (TYPE_GIF);
		else if (ext.equals("png"))
			return (TYPE_PNG);
		else
			return (TYPE_UNKNOWN);
	}

}
