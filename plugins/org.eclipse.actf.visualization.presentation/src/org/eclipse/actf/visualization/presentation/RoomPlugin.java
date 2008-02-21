/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daisuke SATO - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.presentation;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;


/**
 * The main plugin class to be used in the desktop.
 */
public class RoomPlugin extends AbstractUIPlugin {

	public static String PLUGIN_ID = "org.eclipse.actf.visualization.presentation";
	
	//The shared instance.
	private static RoomPlugin _plugin;
	
    private ResourceBundle _resourceBundle;

    private static BundleContext _context;

    private static File tmpDir;
    
	/**
	 * The constructor.
	 */
	public RoomPlugin() {
		_plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
        _context = context;
        
        createTempDirectory();
	}
    

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		_plugin = null;
        _context = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static RoomPlugin getDefault() {
		return _plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
    
    public static String getResourceString(String key) {
        ResourceBundle bundle = RoomPlugin.getDefault().getResourceBundle();
        try {
            return (null != bundle) ? bundle.getString(key) : key;
        } catch (MissingResourceException mre) {
            return "???" + key + "???";
        }
    }
    
    public ResourceBundle getResourceBundle() {
        if (null == _resourceBundle && null != _context) {
            Bundle bundle = _context.getBundle();
            if (null != bundle) {
                _resourceBundle = Platform.getResourceBundle(bundle);
            }
        }

        return _resourceBundle;
    }

    public static String getDirectory(String dir) {

        try {
            URL url = _context.getBundle().getEntry(dir);
            url = FileLocator.resolve(url);
            return new Path(url.getPath()).makeAbsolute().toOSString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return "";
        }
    }
    
    private static void createTempDirectory(){
        if (tmpDir == null) {
            String tmpS = _plugin.getStateLocation().toOSString()+File.separator + "tmp";
            if (FileUtils.isAvailableDirectory(tmpS)) {
                tmpDir = new File(tmpS);
            }
        }
    }


    public static File createTempFile(String prefix, String suffix) throws Exception {
        createTempDirectory();
        return (File.createTempFile(prefix, suffix, tmpDir));
    }
    
    
}

