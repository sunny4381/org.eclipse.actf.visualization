/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization;


public interface IVisualizationConst {
    String PREFIX_RUNTIME_HTML = "runtime";
    String PREFIX_SOURCE_HTML = "source";
    String PREFIX_RESULT = "result";
    String SUFFIX_HTML = ".html";
    String SUFFIX_BMP = ".bmp";
    String PREFIX_SCREENSHOT = "screenshot";
    String PREFIX_MERGED_IMAGE = "merged_screenshot";
    String PREFIX_REPORT = "report";
    String PREFIX_VISUALIZATION = "visualization";

    public static final String DEFAULT_ENCODING = "MS932";

	public static final String RATING_V_GOOD = "VeryGood.png";
	public static final String RATING_GOOD = "Good.png";
	public static final String RATING_POOR = "Poor.png";
	public static final String RATING_BAD = "Bad.png";
}
