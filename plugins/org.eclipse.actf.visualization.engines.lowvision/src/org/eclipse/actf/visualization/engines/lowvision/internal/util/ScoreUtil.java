/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.lowvision.internal.util;

import org.eclipse.actf.visualization.Constants;
import org.eclipse.actf.visualization.engines.lowvision.internal.Messages;



public class ScoreUtil {

	static final double MIN_OVERALL_SCORE_A = 5.0;

	static final double MIN_OVERALL_SCORE_B = 30.0;

	static final double VERY_GOOD = 5.0;

	static final double GOOD = 20.0;

	static final double POOR = 30.0;
        
	public static String getScoreString(double _score) {
		if (_score <= VERY_GOOD) {
			return (Messages.getString("PageEvaluation.Excellent"));
		} else if (_score <= GOOD) {
			return (Messages.getString("PageEvaluation.Good"));
		} else if (_score <= POOR) {
			return (Messages.getString("PageEvaluation.Poor"));
		} else {
			return (Messages.getString("PageEvaluation.Bad"));
		}

	}

	public static String getScoreImageString(double _score) {
		if (_score <= VERY_GOOD) {
			return (Constants.RATING_V_GOOD );
		} else if (_score <= GOOD) {
			return (Constants.RATING_GOOD );
		} else if (_score <= POOR) {
			return (Constants.RATING_POOR );
		} else {
			return (Constants.RATING_BAD);
		}
	}


}
