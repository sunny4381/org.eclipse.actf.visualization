/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.internal.engines.lowvision.problem;

import org.eclipse.actf.visualization.internal.engines.lowvision.Messages;

public class UseAllowedColorRecommendation extends LowVisionRecommendation{
	public UseAllowedColorRecommendation( LowVisionProblem _prob ) throws LowVisionProblemException{
		super( USE_ALLOWED_COLOR_RECOMMENDATION, _prob, Messages.getString("UseAllowedColorRecommendation.Use_a_color_allowed_by_the_design_guideline._1") );
	}
}
