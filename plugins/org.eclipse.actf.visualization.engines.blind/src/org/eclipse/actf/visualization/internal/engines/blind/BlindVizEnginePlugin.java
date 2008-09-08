/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.blind;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.visualization.engines.blind.BlindVizResourceUtil;
import org.eclipse.actf.visualization.engines.blind.TextChecker;
import org.eclipse.actf.visualization.engines.blind.ui.preferences.IBlindPreferenceConstants;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle
 */
public class BlindVizEnginePlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.actf.visualization.engines.blind";

	// The shared instance
	private static BlindVizEnginePlugin plugin;

	private static File tmpDir = null;

	/**
	 * The constructor
	 */
	public BlindVizEnginePlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		if (!getPluginPreferences().getBoolean(
				IBlindPreferenceConstants.NOT_FIRST_TIME)) {
			TextChecker.getInstance();
		}

		createTempDirectory();
		String tmpS;
		if (tmpDir != null) {
			tmpS = tmpDir.getAbsolutePath() + File.separator + "img";
			if (FileUtils.isAvailableDirectory(tmpS)) {
				String tmpS2 = tmpS + File.separator;
				BlindVizResourceUtil.saveImages(tmpS2);
				BlindVizResourceUtil.saveScripts(tmpS2);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		getPluginPreferences().setValue(
				IBlindPreferenceConstants.NOT_FIRST_TIME, true);
		plugin = null;
		super.stop(context);

		if (tmpDir != null) {
			FileUtils.deleteFiles(tmpDir);
		}
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static BlindVizEnginePlugin getDefault() {
		return plugin;
	}

	public String getConfigDir() {
		try {
			URL url = plugin.getBundle().getEntry("config");
			url = FileLocator.resolve(url);
			return new Path(url.getPath()).makeAbsolute().toOSString();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return "";
		}
	}

	private static void createTempDirectory() {
		if (tmpDir == null) {
			String tmpS = plugin.getStateLocation().toOSString()
					+ File.separator + "tmp";
			if (FileUtils.isAvailableDirectory(tmpS)) {
				tmpDir = new File(tmpS);
			} else {
				System.err.println(PLUGIN_ID + " : can't create tmp Directory");
				tmpDir = new File(System.getProperty("java.io.tmpdir"));
			}
		}
	}

	public static File createTempFile(String prefix, String suffix)
			throws Exception {
		createTempDirectory();
		return (File.createTempFile(prefix, suffix, tmpDir));
	}

	public static File getTempDirectory() {
		if (tmpDir == null) {
			createTempDirectory();
		}
		return tmpDir;
	}
		
}
