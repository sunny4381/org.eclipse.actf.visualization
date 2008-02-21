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
package org.eclipse.actf.visualization.engines.blind.util;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.visualization.engines.blind.BlindVizEnginePlugin;
import org.eclipse.actf.visualization.eval.EvaluationPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;


public class BlindVizResourceUtil {

	public static void saveImages(String path) {
		Bundle bundleChecker = Platform.getBundle(EvaluationPlugin.PLUGIN_ID);
		
	    FileUtils.saveToFile(bundleChecker, new Path("icons/Err.png"), false, path
	            + "Err.png", true);
	    FileUtils.saveToFile(bundleChecker, new Path("icons/Warn.png"), false, path
	            + "Warn.png", true);
	    FileUtils.saveToFile(bundleChecker, new Path("icons/Info.png"), false, path
	            + "Info.png", true);
	    FileUtils.saveToFile(bundleChecker, new Path("icons/star.gif"), false, path
	            + "star.gif", true);
	
	    FileUtils.saveToFile(bundleChecker, new Path("icons/rating/Bad.png"), false, path
	            + "Bad.png", true);
	    FileUtils.saveToFile(bundleChecker, new Path("icons/rating/Good.png"), false,
	            path + "Good.png", true);
	    FileUtils.saveToFile(bundleChecker, new Path("icons/rating/Poor.png"), false,
	            path + "Poor.png", true);
	    FileUtils.saveToFile(bundleChecker, new Path("icons/rating/VeryGood.png"), false,
	            path + "VeryGood.png", true);

	    Bundle bundleBlind = Platform.getBundle(BlindVizEnginePlugin.PLUGIN_ID);
	    
	    FileUtils.saveToFile(bundleBlind, new Path("vizResources/images/clear.gif"),
	            false, path + "clear.gif", true);
	    FileUtils.saveToFile(bundleBlind, new Path("vizResources/images/dest.gif"), false,
	            path + "dest.gif", true);
	    FileUtils.saveToFile(bundleBlind, new Path("vizResources/images/draw.gif"), false,
	            path + "draw.gif", true);
	    FileUtils.saveToFile(bundleBlind,
	            new Path("vizResources/images/exclawhite21.gif"), false, path + "exclawhite21.gif", true);
	    FileUtils.saveToFile(bundleBlind, new Path("vizResources/images/face-sad.gif"),
	            false, path + "face-sad.gif", true);
	    FileUtils.saveToFile(bundleBlind, new Path("vizResources/images/face-smile.gif"),
	            false, path + "face-smile.gif", true);
	    FileUtils.saveToFile(bundleBlind, new Path("vizResources/images/face-usual.gif"),
	            false, path + "face-usual.gif", true);
	    FileUtils.saveToFile(bundleBlind, new Path("vizResources/images/jump.gif"), false,
	            path + "jump.gif", true);
	    FileUtils.saveToFile(bundleBlind, new Path("vizResources/images/line_filled.gif"),
	            false, path + "line_filled.gif", true);
	    FileUtils.saveToFile(bundleBlind, new Path("vizResources/images/logo.gif"), false,
	            path + "logo.gif", true);
	    FileUtils.saveToFile(bundleBlind, new Path("vizResources/images/move.gif"), false,
	            path + "move.gif", true);
	    FileUtils.saveToFile(bundleBlind, new Path("vizResources/images/refresh.gif"),
	            false, path + "refresh.gif", true);
	    FileUtils.saveToFile(bundleBlind, new Path("vizResources/images/stop.gif"), false,
	            path + "stop.gif", true);
	
	}
	
	public static void saveScripts(String path){
	    Bundle bundleBlind = Platform.getBundle(BlindVizEnginePlugin.PLUGIN_ID);
	    FileUtils.saveToFile(bundleBlind, new Path("vizResources/scripts/highlight.js"), false,
	            path + "highlight.js", true);
	    FileUtils.saveToFile(bundleBlind, new Path("vizResources/scripts/highlight_moz.js"), false,
	            path + "highlight_moz.js", true);
	    FileUtils.saveToFile(bundleBlind, new Path("vizResources/scripts/highlight-dummy.js"), false,
	            path + "highlight-dummy.js", true);
		
	}

}
