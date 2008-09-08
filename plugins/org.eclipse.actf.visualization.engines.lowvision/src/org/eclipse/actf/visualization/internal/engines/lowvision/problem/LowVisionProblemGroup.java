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

package org.eclipse.actf.visualization.internal.engines.lowvision.problem;

import java.util.Vector;

import org.eclipse.actf.visualization.engines.lowvision.image.IPageImage;

/*
 * Group of LV problems
 * 
 */
public class LowVisionProblemGroup extends LowVisionProblem {
	int numProblems; // problems.length;

	LowVisionProblem[] problems;

	LowVisionProblem representative = null;

	double sumCharacterScores; // 

	double groupScore; // (sumCharacterScore/area)

	public LowVisionProblemGroup(Vector<LowVisionProblem> _vec)
			throws LowVisionProblemException {
		numProblems = _vec.size();
		if (numProblems <= 0) {
			throw new LowVisionProblemException(
					"No instance belong to the group.");
		}
		problems = new LowVisionProblem[numProblems];

		representative = _vec.elementAt(0);
		problems[0] = representative;
		this.pageImage = representative.pageImage;
		this.lowVisionType = representative.lowVisionType;
		this.problemType = representative.problemType;
		this.componentType = representative.componentType;
		this.description = representative.description;
		sumCharacterScores = representative.characterScore;
		setRecommendations();
		this.isGroupFlag = true;

		int tmpLeft = representative.getX();
		int tmpRight = tmpLeft + representative.getWidth();
		int tmpTop = representative.getY();
		int tmpBottom = tmpTop + representative.getHeight();
		for (int i = 1; i < numProblems; i++) {
			LowVisionProblem curProb = _vec.elementAt(i);
			if (curProb.problemType != this.problemType) {
				throw new LowVisionProblemException(
						"Problems of different types cannot be grouped.");
			}
			problems[i] = curProb;
			int curLeft = curProb.getX();
			int curRight = curLeft + curProb.getWidth();
			int curTop = curProb.getY();
			int curBottom = curTop + curProb.getHeight();
			if (curLeft < tmpLeft) {
				tmpLeft = curLeft;
			}
			if (tmpRight < curRight) {
				tmpRight = curRight;
			}
			if (curTop < tmpTop) {
				tmpTop = curTop;
			}
			if (tmpBottom < curBottom) {
				tmpBottom = curBottom;
			}
			sumCharacterScores += curProb.characterScore;
		}
		this.left = tmpLeft;
		this.top = tmpTop;
		this.width = tmpRight - tmpLeft;
		this.height = tmpBottom - tmpTop;
		groupScore = sumCharacterScores / (double) width / (double) height;

		calcProbability();
		calcPriority();
	}

	protected void setRecommendations() {
		this.numRecommendations = representative.numRecommendations;
		this.recommendations = representative.recommendations;
	}

	private void calcProbability() throws LowVisionProblemException {
		if (numProblems <= 0) {
			throw new LowVisionProblemException(
					"There are no Problems in this ProblemGroup.");
		}
		probability = 0.0;
		// double problemArea = 0.0;
		double maxProba = 0.0;
		for (int i = 0; i < numProblems; i++) {
			LowVisionProblem curProb = problems[i];
			if (maxProba < curProb.probability) {
				maxProba = curProb.probability;
			}
			// double curArea = curProb.width * curProb.height;
			// probability += (curProb.probability * curArea);
			// problemArea += curArea;
		}

		probability = maxProba;

		// TODO consider problem area, etc
		// if( problemType == LOWVISION_COLOR_PROBLEM ){
		// probability /= problemArea;
		// }
		// else if( problemType == LOWVISION_IMAGE_COLOR_PROBLEM ){
		// probability = maxProba;
		// }
		// else{
		// probability /= (this.width * this.height);
		// }
	}

	private void calcPriority() throws LowVisionProblemException {
		if (problems[0].pageComponent == null) {
			priority = 0;
			return;
		}

		IPageImage pi = problems[0].pageComponent.getPageImage();
		if (problemType == LowVisionProblem.LOWVISION_IMAGE_COLOR_PROBLEM) {
			priority = 0;
		} else {
			if (pi == null) {
				throw new LowVisionProblemException(
						"PageImage of the Problem is null.");
			}
			int pageWidth = pi.getWidth();
			int pageHeight = pi.getHeight();
			priority = pageWidth * pageHeight - top * pageWidth - left;
		}
	}

	public LowVisionProblem getRepresentative() {
		return (representative);
	}

	public int getNumProblems() {
		return (numProblems);
	}

	public LowVisionProblem[] getProblems() {
		return (problems);
	}

	public double getSumCharacterScores() {
		return (sumCharacterScores);
	}

	public double getGroupScore() {
		return (groupScore);
	}
}
