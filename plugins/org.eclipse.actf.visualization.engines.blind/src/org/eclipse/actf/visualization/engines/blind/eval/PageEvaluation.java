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

import org.eclipse.actf.ui.util.HighlightStringListener;
import org.eclipse.actf.visualization.IVisualizationConst;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.internal.engines.blind.BlindVizEnginePlugin;
import org.eclipse.actf.visualization.internal.engines.blind.Messages;
import org.eclipse.actf.visualization.internal.engines.blind.SummaryEvaluation;
import org.eclipse.actf.visualization.util.RadarChart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * Utility class to create evaluation summary of target page
 */
public class PageEvaluation {

	/**
	 * Get default {@link HighlightStringListener} for blind usability
	 * visualization result
	 * 
	 * @return {@link HighlightStringListener}
	 */
	public static HighlightStringListener getHighLightStringListener() {
		HighlightStringListener hlsl = new HighlightStringListener();
		Color blue = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
		Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);

		hlsl.addTarget(Messages.getString("Eval.excellent"), blue, SWT.BOLD);
		hlsl.addTarget(Messages.getString("Eval.completely.compliant"), blue,
				SWT.BOLD);
		hlsl.addTarget(Messages.getString("Eval.seems.completely.compliant"),
				blue, SWT.BOLD);
		hlsl.addTarget(Messages
				.getString("Eval.completely.compliant.with.some.errors"), red,
				SWT.BOLD);
		hlsl.addTarget(Messages.getString("Eval.many.accessibility.issues"),
				red, SWT.BOLD);
		hlsl.addTarget(Messages.getString("Eval.some.accessibility.issues"),
				red, SWT.BOLD);

		hlsl.addTarget(Messages
				.getString("Eval.easy.for.blind.user.to.navigate"), blue,
				SWT.BOLD);

		hlsl.addTarget(Messages.getString("Eval.page.has.skiplinks.headings"),
				red, SWT.BOLD);
		hlsl.addTarget(Messages
				.getString("Eval.darkcolored.visualization.view"), red,
				SWT.BOLD);

		return (hlsl);
	}

	private GuidelineHolder guidelineHolder = GuidelineHolder.getInstance();

	private PageData pageData;

	private String[] metrics = guidelineHolder.getMetricsNames();

	private int metricsSize = metrics.length;

	private int[] scores = new int[metricsSize];

	/**
	 * Constructor of the class
	 * 
	 * @param problems
	 *            accessibility issues of target page
	 * @param pageData
	 *            statistics data of target page
	 */
	public PageEvaluation(List<IProblemItem> problems, PageData pageData) {
		this.pageData = pageData;
		Arrays.fill(scores, 100);

		createPageReport(problems, pageData);
	}

	private void addProblem(IProblemItem item) {
		int[] curScores = item.getEvaluationItem().getMetricsScores();

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

	private void setInvalidLinkRatio(double ratio) {

		// TODO
	}

	protected boolean hasComplianceError() {
		for (int i = 0; i < metricsSize; i++) {
			if (metrics[i].equalsIgnoreCase("compliance")) {
				return (scores[i] != 100);
			}
		}
		return (false);
	}

	/**
	 * Get evaluation result as array of String.
	 * 
	 * @return evaluation result
	 */
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

	/**
	 * Get overall rating for the page
	 * 
	 * @return overall rating
	 */
	public String getOverallRating() {

		int minValue = getMinScore();

		String rating = Messages.getString("PageEvaluation.Bad"); //$NON-NLS-1$

		if (!hasComplianceError()) {
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

	/**
	 * Get evaluation summary of the page
	 * 
	 * @return evaluation summary
	 */
	public String getSummary() {
		SummaryEvaluation se = new SummaryEvaluation(this, pageData,
				hasComplianceError());
		return (se.getOverview());
	}

	/**
	 * Get rating icon name for the page
	 * 
	 * @return rating icon name
	 */
	public String getRatingIcon() {

		int minValue = getMinScore();

		String rating = IVisualizationConst.RATING_BAD; //$NON-NLS-1$

		if (!hasComplianceError()) {
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

	/**
	 * Get evaluation metrics used to evaluate this page
	 * 
	 * @return metrics names
	 */
	public String[] getMetrics() {
		return metrics;
	}

	/**
	 * Get scores for each metrics
	 * 
	 * @return scores
	 */
	public int[] getScores() {
		return scores;
	}

}
