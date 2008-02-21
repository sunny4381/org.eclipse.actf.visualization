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

package org.eclipse.actf.visualization.engines.lowvision.problem;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.engines.lowvision.PageElement;
import org.eclipse.actf.visualization.engines.lowvision.image.PageComponent;
import org.eclipse.actf.visualization.engines.lowvision.internal.Messages;

public class ProhibitedBothColorsProblem extends LowVisionProblem{
	private int foregroundColor = -1;
	private int backgroundColor = -1;
	
	public ProhibitedBothColorsProblem( PageComponent _pc, LowVisionType _lvType, double _proba ) throws LowVisionProblemException{
		super( LOWVISION_PROHIBITED_BOTH_COLORS_PROBLEM, _lvType, Messages.getString("ProhibitedBothColorsProblem.Both_of_the_foreground_and_background_colors_are_not_allowed_by_the_design_guideline._1"), _pc, _proba );
		setRecommendations();
	}

	public ProhibitedBothColorsProblem( PageElement _pe, LowVisionType _lvType, double _proba ) throws LowVisionProblemException{
		super( LOWVISION_PROHIBITED_BOTH_COLORS_PROBLEM, _lvType, Messages.getString("ProhibitedBothColorsProblem.Both_of_the_foreground_and_background_colors_are_not_allowed_by_the_design_guideline._1"), _pe, _proba );
		foregroundColor = _pe.getForegroundColor();
		backgroundColor = _pe.getBackgroundColor();
		setRecommendations();
	}
	
	protected void setRecommendations() throws LowVisionProblemException{
		numRecommendations = 1;
		recommendations = new LowVisionRecommendation[numRecommendations];
		recommendations[0] = new UseAllowedColorRecommendation( this );
	}
	
	public int getForegroundColor(){
		return( foregroundColor );
	}

	public int getBackgroundColor(){
		return( backgroundColor );
	}
}
