/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.eval;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.eclipse.actf.visualization.IVisualizationConst;
import org.eclipse.actf.visualization.engines.blind.BlindVizEnginePlugin;
import org.eclipse.actf.visualization.engines.blind.internal.Messages;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.util.RadarChart;

public class PageEvaluation {

	private GuidelineHolder guidelineHolder = GuidelineHolder.getInstance();

	private PageData pageData;

	private String[] metrics = guidelineHolder.getMetricsNames();

	private int metricsSize = metrics.length;

	private int[] scores = new int[metricsSize];

	public PageEvaluation(List<IProblemItem> problems, PageData pageData) {
		this.pageData = pageData;
		Arrays.fill(scores, 100);

		createPageReport(problems, pageData);
	}

	public void addProblem(IProblemItem pti) {
		int[] curScores = pti.getEvaluationItem().getMetricsScores();

		if (curScores.length == metricsSize) {
			for (int i = 0; i < metricsSize; i++) {
				scores[i] -= curScores[i];
			}
		}
	}

	private void checkMinus() {
		for (int i = 0; i < metricsSize; i++) {
			if (scores[i] < 0) {
				scores[i] = 0;
			}
		}

	}

	public void setInvalidLinkRatio(double ratio) {

		// TODO
	}

	protected PageData getPageData() {
		return pageData;
	}

	protected boolean isHasComplianceError() {
		for (int i = 0; i < metricsSize; i++) {
			if (metrics[i].equalsIgnoreCase("compliance")) {
				return (scores[i] != 100);
			}
		}
		return (false);
	}

	public String[] getAllResult() {
		Vector<String> tmpV = new Vector<String>();
		tmpV.add("Page Rating"); //$NON-NLS-1$
		tmpV.add(getOverallRating());

		boolean[] enabledMetrics = guidelineHolder.getMatchedMetrics();

		for (int i = 0; i < metricsSize; i++) {
			if (enabledMetrics[i]) {
				tmpV.add(metrics[i]);
				tmpV.add(String.valueOf(scores[i]));
			}
		}

		String[] result = new String[tmpV.size()];
		tmpV.toArray(result);
		return (result);

	}

	private int getMinScore() {
		int minValue = 100;

		boolean[] enabled = GuidelineHolder.getInstance().getMatchedMetrics();

		for (int i = 0; i < metricsSize; i++) {
			if (enabled[i] && minValue > scores[i]) {
				minValue = scores[i];
			}
		}
		return (minValue);
	}

	public String getOverallRating() {

		int minValue = getMinScore();

		String rating = Messages.getString("PageEvaluation.Bad"); //$NON-NLS-1$

		if (!isHasComplianceError()) {
			if (minValue >= 90) {
				rating = Messages.getString("PageEvaluation.Excellent"); //$NON-NLS-1$
			} else if (minValue >= 70) {
				rating = Messages.getString("PageEvaluation.Good"); //$NON-NLS-1$
			} else if (minValue >= 60) {
				rating = Messages.getString("PageEvaluation.Poor"); //$NON-NLS-1$
			}
		}
		return (rating);
	}

	public String getSummary() {
		SummaryEvaluation se = new SummaryEvaluation(this);
		return (se.getOverview());
	}

	public String getRatingIcon() {

		int minValue = getMinScore();

		String rating = IVisualizationConst.RATING_BAD; //$NON-NLS-1$

		if (!isHasComplianceError()) {
			if (minValue >= 90) {
				rating = IVisualizationConst.RATING_V_GOOD; //$NON-NLS-1$
			} else if (minValue >= 80) {
				rating = IVisualizationConst.RATING_GOOD; //$NON-NLS-1$
			} else if (minValue >= 60) {
				rating = IVisualizationConst.RATING_POOR; //$NON-NLS-1$
			}
		}
		return (rating);
	}

	private void createPageReport(List<IProblemItem> problems, PageData pageData) {
		for (IProblemItem item : problems) {
			this.addProblem(item);
		}

		this.setInvalidLinkRatio(pageData.getInvalidLinkRatio());

		this.checkMinus();

		boolean[] enabled = GuidelineHolder.getInstance().getMatchedMetrics();
		String[] metrics = this.getMetrics();
		int[] scores = this.getScores();

		Vector<String> metricsV = new Vector<String>();
		Vector<Integer> scoresV = new Vector<Integer>();

		for (int i = 0; i < enabled.length; i++) {
			if (enabled[i]) {
				metricsV.add(metrics[i]);
				scoresV.add(new Integer(scores[i]));
			}
		}
		metrics = new String[metricsV.size()];
		metricsV.toArray(metrics);

		scores = new int[scoresV.size()];
		for (int i = 0; i < scoresV.size(); i++) {
			scores[i] = scoresV.get(i).intValue();
		}

		try {
			RadarChart chart = new RadarChart(metrics, scores);

			// TODO use tmp file
			chart.writeToPNG(new File(BlindVizEnginePlugin.getTempDirectory(),
					"pagerating.png")); //$NON-NLS-1$
		} catch (Exception e) {
			// e.printStackTrace();
			// TODO create empty png
		}
	}

	public String[] getMetrics() {
		return metrics;
	}

	public int[] getScores() {
		return scores;
	}
}
