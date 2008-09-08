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

package org.eclipse.actf.visualization.engines.lowvision.image;

import org.eclipse.actf.visualization.internal.engines.lowvision.Messages;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.Int2D;
import org.eclipse.actf.visualization.internal.engines.lowvision.image.PageImage;
import org.eclipse.actf.visualization.internal.engines.lowvision.io.BMPReader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class ImageDumpUtil {

	private static boolean LV16BIT = false;

	public static synchronized IPageImage joinPageImages(String saveFileName,
			IPageImage[] targets) {
		Int2D int2dWhole = new Int2D(0, 0);
		int iWholeWidth = 0;
		int iWholeHeight = 0;

		if (targets != null && saveFileName != null) {
			for (int i = 0; i < targets.length; i++) {
				IInt2D tmpInt2d;
				tmpInt2d = targets[i].getInt2D();
				if (iWholeWidth < tmpInt2d.getWidth())
					iWholeWidth = tmpInt2d.getWidth();
				iWholeHeight += tmpInt2d.getHeight();
			}

			int2dWhole = new Int2D(iWholeWidth, iWholeHeight);

			try {

				int iDrawY = 0;
				for (int i = 0; i < targets.length; i++) {
					IInt2D tmpInt2d = targets[i].getInt2D();
					for (int k = 0; k < tmpInt2d.getHeight(); k++) {
						System.arraycopy(tmpInt2d.getData()[k], 0,
								int2dWhole.getData()[iDrawY + k], 0, tmpInt2d.getWidth());
					}
					iDrawY += tmpInt2d.getHeight();
				}

				int2dWhole.writeToBMPFile(saveFileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (new PageImage(int2dWhole));
	}

	static public IPageImage createPageImage(String bmpFileName, Shell _shell) {

		// TODO without file
		IInt2D int2dWhole = new Int2D(0, 0);

		try {
			int2dWhole = Int2DFactory.createInt2D(bmpFileName);
			PageImage result = new PageImage(int2dWhole);
			int2dWhole = null;

			try {
				LV16BIT = BMPReader.getBitCount(bmpFileName) == 16;
			} catch (Exception e2) {
				// TODO
				LV16BIT = true;
			}

			return (result);
		} catch (Exception e) {
			MessageBox box1 = new MessageBox(_shell, SWT.OK | SWT.ICON_WARNING);
			box1.setMessage(Messages.getString("ImageDumpUtil.TrueColor"));
			box1.open();
		}

		return (null);
	}

	public static boolean isLV16BIT() {
		return LV16BIT;
	}

}
