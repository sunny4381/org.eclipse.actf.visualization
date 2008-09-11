/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.flash.internal;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.gui.internal.util.GuiImages;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public final class FlashImages {

	public static final String OVER_RED = "icons/ovr16/red.gif"; //$NON-NLS-1$
	public static final String OVER_YELLOW = "icons/ovr16/yellow.gif"; //$NON-NLS-1$
	public static final String OVER_GREEN = "icons/ovr16/green.gif"; //$NON-NLS-1$
	public static final String OVER_BLACK = "icons/ovr16/black.gif"; //$NON-NLS-1$

	public static final String FLASH_TYPE = "icons/flash16/type_{0}.gif"; //$NON-NLS-1$

	public static final String TYPE_flash = "flash"; //$NON-NLS-1$

	public static final ISharedImages sharedImages = PlatformUI.getWorkbench()
			.getSharedImages();
	public static Map<String, Image> imageMap = new HashMap<String, Image>();

	public static Image getImage(String path) {
		if (null == path) {
			return null;
		}
		Image image = (Image) imageMap.get(path);
		if (null == image) {
			ImageDescriptor descriptor = getImageDescriptor(path);
			if (null != descriptor) {
				image = descriptor.createImage(true);
				imageMap.put(path, image);
			}
		}
		return image;
	}

	@SuppressWarnings("restriction")
	public static Image getImage(String basePath, String overlayPath, Point size) {
		if (null == basePath || null == overlayPath) {
			return null;
		}
		String totalPath = basePath + "|" + overlayPath; //$NON-NLS-1$
		Image image = (Image) imageMap.get(totalPath);
		if (null == image) {
			ImageDescriptor descriptor = getImageDescriptor(basePath);
			if (null != descriptor) {
				ImageDescriptor overlayDescriptor = getImageDescriptor(overlayPath);
				if (null != overlayDescriptor) {
					descriptor = new org.eclipse.ui.internal.OverlayIcon(
							descriptor, overlayDescriptor, size);
					image = descriptor.createImage(true);
				}
			}
			imageMap.put(totalPath, image);
		}
		return image;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		ImageDescriptor descriptor = AbstractUIPlugin
				.imageDescriptorFromPlugin(
						"org.eclipse.actf.visualization.flash", path);
		if (null != descriptor) {
			return descriptor;
		}
		return GuiImages.getImageDescriptor(path);
	}

	public static String flashIcon(String typeString) {
		return getFilename(FLASH_TYPE, typeString);
	}

	public static String getFilename(String format, String name) {
		String path = MessageFormat.format(format, new Object[] { name
				.toLowerCase() });
		if (null != getImageDescriptor(path)) {
			return path;
		}
		DebugPrintUtil.devOrDebugPrintln("Missing Flash icon " + path); //$NON-NLS-1$
		return null;
	}

}
