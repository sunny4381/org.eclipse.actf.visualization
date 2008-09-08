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

package org.eclipse.actf.visualization.engines.lowvision;

public interface ILowVisionConstant {

	//IE fontsize
	static final String IE_LARGEST_FONT = "16pt";
	static final double IE_EM_SCALING = 1.33; // "1em" in largest (experimental)
	static final double IE_LARGER_SCALING = 1.67; // "larger" in largest (experimental)
	static final double IE_SMALLER_SCALING = 1.00; // "smaller" in largest (experimental)

	// scroll bar
	int SURROUNDINGS_WIDTH = 2; 
	int SCROLL_BAR_WIDTH = 20;
	boolean REMOVE_SURROUNDINGS = true;
	boolean REMOVE_SCROLL_BAR_AT_RIGHT = false;
	boolean REMOVE_SCROLL_BAR_AT_BOTTOM = false;
	
	// TODO relative value?
	// the color which have only small portions in image is not handled as text candidate
	int THRESHOLD_MIN_OCCURRENCES = 300;

	// thresholds for char check
	double THRESHOLD_MIN_CHAR_RATIO = 0.1;
	double THRESHOLD_MAX_CHAR_RATIO = 10.0;
	double THRESHOLD_MIN_CONTAINER_DENSITY = 0.4;
	double THRESHOLD_MAX_CHARACTER_DENSITY = 0.75;
	double THRESHOLD_MIN_THINNING_RATIO = 0.25; // 
	double THRESHOLD_MIN_UNDERLINE_POSITION = 0.85;
	double THRESHOLD_MIN_UNDERLINE_WIDTH_RATIO = 0.95;
	double THRESHOLD_MIN_UNDERLINE_RATIO = 3.0;

	int THRESHOLD_MIN_CHAR_WIDTH = 5;
	int THRESHOLD_MAX_CHAR_WIDTH = 72;
	int THRESHOLD_MIN_CHAR_HEIGHT = 5; 
	int THRESHOLD_MAX_CHAR_HEIGHT = 100;

	int THRESHOLD_MAX_THINNED_BRANCHES = 8;
	int THRESHOLD_MAX_THINNED_CROSSES = 8;

	int THRESHOLD_MIN_MSCHAR_WIDTH = 10;
	int THRESHOLD_MIN_MSCHAR_HEIGHT = 10;
	int THRESHOLD_MAX_MSCHAR_HEIGHT = 100;

	int THRESHOLD_MIN_SMCHAR_WIDTH = 10;
	int THRESHOLD_MIN_SMCHAR_HEIGHT = 10;
	int THRESHOLD_MAX_SMCHAR_HEIGHT = 100;

	// color combination check (text)
	float MIN_ENOUGH_DELTA_L_FOR_TEXT = 20.0f; // enough L (L*a*b*)
	float MAX_ENOUGH_DELTA_L_FOR_TEXT = 40.0f; // (L*a*b*)
	float ENOUGH_DELTA_E_FOR_TEXT = 100.0f; // (L*a*b*)
	float ENOUGH_DELTA_H_FOR_TEXT = 100.0f; // (L*a*b*)
	float RECOMMENDED_DELTA_L_FOR_TEXT = MAX_ENOUGH_DELTA_L_FOR_TEXT + 5.0f;
	float MID_L = 50.0f;

	// color combination check (image)
	float ENOUGH_DELTA_E_FOR_IMAGE = 40.0f;

	/* 
	 * Extract method for SM char is not perfect. 
	 * (e.g., extract chars with shadow, etc...)
	 * So, set strict threshold. 
	 */
	double THRESHOLD_MIN_SM_COLOR_PROBLEM_RATIO = 0.8; 
	

	// check color combination (image)
	// See InteriorImage.java
	double THRESHOLD_MIN_IMAGE_COLOR_PROBLEM_PROBABILITY = 0.2;

	// check fg/bg color
	double THRESHOLD_FOREGROUND_RATIO = 0.25;
	// error margin
	double THRESHOLD_FOREGROUND_ERROR_MARGIN = 1.5;
	
	float THRESHOLD_LIMIT_BLURRED_WIDTH_RATIO = 3.0f;
	float THRESHOLD_LIMIT_BLURRED_HEIGHT_RATIO = 3.0f;

	//// InteriorImage
	// minimum pixels/occupation to be considered as LargeComponent
	int THRESHOLD_MIN_LARGE_COMPONENT_PIXELS = 100;
	double THRESHOLD_MIN_LARGE_COMPONENT_OCCUPATION = 0.0005;
	
	// Problem grouping
	int THRESHOLD_MAX_CHARACTER_SPACE = 7; // word
	int THRESHOLD_MAX_REGION_ELEMENT_SPACE = 80;
	int THRESHOLD_MAX_GROUPED_CONTAINER_WIDTH = 300; 
	int THRESHOLD_MAX_GROUPED_CONTAINER_HEIGHT = 300;
	
	//severity for fixed size font (0-1)
	double SEVERITY_FIXED_SIZE_FONT = 0.25;
	double SEVERITY_SMALL_FONT = 0.25;
	double SEVERITY_FIXED_SMALL_FONT = SEVERITY_FIXED_SIZE_FONT + SEVERITY_SMALL_FONT;

	// severity for color problems 
	double SEVERITY_PROHIBITED_FOREGROUND_COLOR = 0.5;
	double SEVERITY_PROHIBITED_BACKGROUND_COLOR = 0.5;
	double SEVERITY_PROHIBITED_BOTH_COLORS = SEVERITY_PROHIBITED_FOREGROUND_COLOR + SEVERITY_PROHIBITED_BACKGROUND_COLOR;
	
	// severity/color mapping
	double SCORE_ORANGE = 0.5; 
	double SCORE_RED = 1.0; 
}
