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
package org.eclipse.actf.visualization.gui.internal.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.gui.internal.GuiPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.ibm.icu.text.MessageFormat;


public final class GuiImages {
	
	//Images
	public static final ImageDescriptor IMAGE_OVERLAY =     GuiPlugin.getImageDescriptor("icons/action16/overlay.gif"); //$NON-NLS-1$
	public static final ImageDescriptor IMAGE_REFRESH =     GuiPlugin.getImageDescriptor("icons/action16/refresh.gif"); //$NON-NLS-1$
	public static final ImageDescriptor IMAGE_CLEAR =       GuiPlugin.getImageDescriptor("icons/action16/clear.gif"); //$NON-NLS-1$
	public static final ImageDescriptor IMAGE_EXPAND_ALL =  GuiPlugin.getImageDescriptor("icons/action16/expandall.gif"); //$NON-NLS-1$
	public static final ImageDescriptor IMAGE_COLLAPSE_ALL =GuiPlugin.getImageDescriptor("icons/action16/collapseall.gif"); //$NON-NLS-1$
	public static final ImageDescriptor IMAGE_SPEAK =       GuiPlugin.getImageDescriptor("icons/action16/speak.gif"); //$NON-NLS-1$
	public static final ImageDescriptor IMAGE_STOP =        GuiPlugin.getImageDescriptor("icons/action16/stop.gif"); //$NON-NLS-1$
	public static final ImageDescriptor IMAGE_CHECKER =     GuiPlugin.getImageDescriptor("icons/action16/checker.gif"); //$NON-NLS-1$

	
	public static final String ACC_ROLE = "icons/acc16/role_{0}.gif"; //$NON-NLS-1$
	
	public static final ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
	public static Map<String, Image> imageMap = new HashMap<String, Image>();
	
    public static Image getImage(String path) {
        if( null == path ) {
            return null;
        }
        Image image = (Image)imageMap.get(path);
        if( null == image) {
            ImageDescriptor descriptor = getImageDescriptor(path);
            if( null != descriptor ) {
                image = descriptor.createImage(true);
                imageMap.put(path,image);
            }
        }
        return image;
    }
    
	public static ImageDescriptor getImageDescriptor(String path) {
		ImageDescriptor descriptor = GuiPlugin.getImageDescriptor(path);
		if( null == descriptor ) {
			descriptor = sharedImages.getImageDescriptor(path);
		}
		return descriptor;
	}

	public static String roleIcon(String roleString) {
		return getFilename(ACC_ROLE, roleString);
	}
	
	public static String roleIcon(int roleNumber) {
		return getFilename(ACC_ROLE, "0x"+Integer.toHexString(roleNumber)); //$NON-NLS-1$
	}
	
	public static String getFilename(String format, String name) {
		String path = MessageFormat.format(format, new Object[]{name.toLowerCase()});
		if( null != GuiPlugin.getImageDescriptor(path) ) {
			return path;
		}
		DebugPrintUtil.devOrDebugPrintln("Missing MSAA icon "+path); //$NON-NLS-1$
		return null;
	}

	
}
