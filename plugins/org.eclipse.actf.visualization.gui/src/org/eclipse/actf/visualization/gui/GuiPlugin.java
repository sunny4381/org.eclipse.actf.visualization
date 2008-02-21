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
package org.eclipse.actf.visualization.gui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class GuiPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "org.eclipse.actf.visualization.gui";
	
	//The shared instance.
	private static GuiPlugin plugin;
    
    //Images
    public static final ImageDescriptor IMAGE_OVERLAY =     getImageDescriptor("icons/action16/overlay.gif"); //$NON-NLS-1$
    public static final ImageDescriptor IMAGE_REFRESH =     getImageDescriptor("icons/action16/refresh.gif"); //$NON-NLS-1$
    public static final ImageDescriptor IMAGE_CLEAR =       getImageDescriptor("icons/action16/clear.gif"); //$NON-NLS-1$
    public static final ImageDescriptor IMAGE_EXPAND_ALL =  getImageDescriptor("icons/action16/expandall.gif"); //$NON-NLS-1$
    public static final ImageDescriptor IMAGE_COLLAPSE_ALL =getImageDescriptor("icons/action16/collapseall.gif"); //$NON-NLS-1$
    public static final ImageDescriptor IMAGE_SPEAK =       getImageDescriptor("icons/action16/speak.gif"); //$NON-NLS-1$
    public static final ImageDescriptor IMAGE_STOP =        getImageDescriptor("icons/action16/stop.gif"); //$NON-NLS-1$
    public static final ImageDescriptor IMAGE_CHECKER =     getImageDescriptor("icons/action16/checker.gif"); //$NON-NLS-1$
	
	/**
	 * The constructor.
	 */
	public GuiPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

    /**
	 * Returns the shared instance.
	 */
	public static GuiPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path); //$NON-NLS-1$
	}
}
