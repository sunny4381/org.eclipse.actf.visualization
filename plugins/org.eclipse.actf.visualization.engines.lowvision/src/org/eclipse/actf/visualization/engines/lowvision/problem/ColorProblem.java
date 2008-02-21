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

package org.eclipse.actf.visualization.engines.lowvision.problem;

import org.eclipse.actf.visualization.engines.lowvision.LowVisionType;
import org.eclipse.actf.visualization.engines.lowvision.PageElement;
import org.eclipse.actf.visualization.engines.lowvision.character.CharacterMS;
import org.eclipse.actf.visualization.engines.lowvision.character.CharacterSM;
import org.eclipse.actf.visualization.engines.lowvision.character.CharacterSS;
import org.eclipse.actf.visualization.engines.lowvision.image.PageComponent;
import org.eclipse.actf.visualization.engines.lowvision.internal.Messages;


public class ColorProblem extends LowVisionProblem{
	private int foregroundColor = -1;
	private int backgroundColor = -1;

	public ColorProblem( PageComponent _pc, LowVisionType _lvType, double _proba ) throws LowVisionProblemException{
		super( LOWVISION_COLOR_PROBLEM, _lvType, Messages.getString("ColorProblem.Foreground_and_background_colors_are_too_close._1"), _pc, _proba );
		setComponentColors();
		setRecommendations();
	}
	
	public ColorProblem( PageElement _pe, LowVisionType _lvType, double _proba ) throws LowVisionProblemException{
		super( LOWVISION_COLOR_PROBLEM, _lvType, Messages.getString("ColorProblem.Foreground_and_background_colors_are_too_close._1"), _pe, _proba );
		foregroundColor = _pe.getForegroundColor();
		backgroundColor = _pe.getBackgroundColor();
		setRecommendations();
	}
	
	protected void setRecommendations() throws LowVisionProblemException{
		numRecommendations = 1;
		recommendations = new LowVisionRecommendation[numRecommendations];		
		recommendations[0] = new EnoughContrastRecommendation( this, foregroundColor, backgroundColor );
	}
	
	private void setComponentColors() throws LowVisionProblemException{
		if( componentType == PageComponent.SS_CHARACTER_TYPE ){
			foregroundColor = ((CharacterSS)pageComponent).getForegroundColor();
			backgroundColor = ((CharacterSS)pageComponent).getBackgroundColor();
		}
		else if( componentType == PageComponent.MS_CHARACTER_TYPE ){
			backgroundColor = ((CharacterMS)pageComponent).getBackgroundColor();
			//use average color
			foregroundColor = ((CharacterMS)pageComponent).getForegroundColor();
		}
		else if( componentType == PageComponent.SM_CHARACTER_TYPE ){
			foregroundColor = ((CharacterSM)pageComponent).getForegroundColor();
		}
		else{
			throw new LowVisionProblemException( "Invalid component type." ); //$NON-NLS-1$
		}
	}
	
	public String getDescription() throws LowVisionProblemException{
		return( super.getDescription() );
	}

	public int getForegroundColor(){
		return( foregroundColor );
	}

	public int getBackgroundColor(){
		return( backgroundColor );
	}
}
