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

package org.eclipse.actf.visualization.engines.blind.eval;

import org.eclipse.actf.util.ui.HighlightStringListener;
import org.eclipse.actf.visualization.Constants;
import org.eclipse.actf.visualization.engines.blind.internal.Messages;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class SummaryEvaluation {

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

	private PageEvaluation pe;

	private PageData pageData;

	private int noImageAltCount;

	private int wrongImageAltCount;

	private int redundantImageAltCount = 0;

	private GuidelineHolder guidelineHolder = GuidelineHolder
			.getInstance();

	/**
	 * 
	 */
	public SummaryEvaluation(PageEvaluation pe) {
		this.pe = pe;
		this.pageData = pe.getPageData();
	}

	public String getOverview() {
		StringBuffer tmpSB = new StringBuffer(512);
		StringBuffer noGoodMetrics = new StringBuffer();

		boolean hasComp = false;
		boolean hasNav = false;
		boolean hasOther = false;

		String[] metrics = pe.getMetrics();
		int[] scores = pe.getScores();

		int comp = 100;
		int nav = 100;
		int other = 100;

		// boolean[] enabledMetrics = guidelineHolder.getMatchedMetrics();

		for (int i = 0; i < metrics.length; i++) {
			int score = scores[i];
			if (metrics[i].equalsIgnoreCase("compliance")
					&& guidelineHolder.isMatchedMetric(metrics[i])) {
				comp = score;
				hasComp = true;
				if (score != 100) {
					noGoodMetrics.append(metrics[i] + ",");
				}
			} else if (metrics[i].equalsIgnoreCase("navigability")
					&& guidelineHolder.isMatchedMetric(metrics[i])) {
				nav = score;
				hasNav = true;
			} else {
				hasOther = true;
				if (other > score) {
					other = score;
				}
				if (score != 100) {
					noGoodMetrics.append(metrics[i] + ",");
				}
			}
		}

		noImageAltCount = pageData.getMissingAltNum();
		wrongImageAltCount = pageData.getWrongAltNum();
		// alertImageAltCount = pageData.get;
		// redundantImageAltCount = pageData.get;//TODO
		int totalAltError = noImageAltCount + wrongImageAltCount;// +
		// redundantImageAltCount;
		// +alertImageAltCount

		StringBuffer aboutComp = new StringBuffer();
		StringBuffer aboutNav = new StringBuffer();

		boolean isGood = false;

		if (hasComp) {
			if (comp >= 80) {
				if (pe.isHasComplianceError()) {
					aboutComp
							.append(Messages
									.getString("Eval.completely.compliant.with.some.errors")
									+ Constants.LINE_SEP);

					if (totalAltError > 0) {
						aboutComp
								.append(Messages
										.getString("Eval.confirm.alt.attributes.first"));
						aboutComp.append(getImageAltStatistics());
					} else {
						aboutComp
								.append(Messages
										.getString("Eval.confirm.errors.detailed.report"));
					}
				} else {
					if (hasOther && other != 100) {
						aboutComp.append(Messages.formatResourceString(
								"Eval.some.errors.on.metrics", new String[] {
										noGoodMetrics.substring(0,
												noGoodMetrics.length() - 1),
										Constants.LINE_SEP }));
					} else {
						if (comp == 100) {
							isGood = true;
							aboutComp.append(Messages
									.getString("Eval.completely.compliant"));
						} else {
							isGood = true;
							aboutComp
									.append(Messages
											.formatResourceString(
													"Eval.completely.compliant.with.user.check.items",
													Constants.LINE_SEP));
						}
					}
				}
			} else {
				if (comp > 50) {
					aboutComp.append(Messages
							.getString("Eval.some.accessibility.issues")
							+ Constants.LINE_SEP);
				} else {
					aboutComp.append(Messages
							.getString("Eval.many.accessibility.issues")
							+ Constants.LINE_SEP);
				}

				if (totalAltError > 0) {
					aboutComp.append(Messages
							.getString("Eval.confirm.alt.attributes.first")
							+ Constants.LINE_SEP);
					aboutComp.append(getImageAltStatistics());
				} else {
					aboutComp.append(Messages
							.getString("Eval.confirm.errors.detailed.report"));
				}
			}
		}

		//
		if (hasNav) {
			if (nav > 80) {
				if (pageData.getMaxTime() > 240) {
					aboutNav.append(Messages.formatResourceString(
							"Eval.navigability.long.time.error.msg",
							new String[] { Constants.LINE_SEP,
									Constants.LINE_SEP, Constants.LINE_SEP,
									Constants.LINE_SEP })
							+ Constants.LINE_SEP);

				} else {
					aboutNav.append(Messages.formatResourceString(
							"Eval.navigability.good.msg", new String[] {
									Constants.LINE_SEP, Constants.LINE_SEP })
							+ Constants.LINE_SEP);
				}
			} else {
				isGood = false;
				aboutNav.append(Messages.formatResourceString(
						"Eval.navigability.low.score.error.msg", new String[] {
								Constants.LINE_SEP, Constants.LINE_SEP,
								Constants.LINE_SEP, Constants.LINE_SEP })
						+ Constants.LINE_SEP);
			}
		}

		if ((hasComp || hasNav) && isGood) {
			tmpSB.append(Messages.getString("Eval.excellent")
					+ Constants.LINE_SEP + Constants.LINE_SEP);
		}
		tmpSB.append(aboutNav + Constants.LINE_SEP);
		tmpSB.append(aboutComp);

		return (tmpSB.toString());
	}

	private String getImageAltStatistics() {
		StringBuffer tmpSB = new StringBuffer();

		if (noImageAltCount > 0) {
			tmpSB.append(" -"
					+ Messages.formatResourceString(
							"Eval.no.img.alt.error.msg", Constants.LINE_SEP));
		}
		if (wrongImageAltCount > 0) {
			tmpSB
					.append(" -"
							+ Messages.formatResourceString(
									"Eval.wrong.img.alt.error.msg",
									Constants.LINE_SEP));
		}
		if (redundantImageAltCount > 0) {
			tmpSB.append(" -"
					+ Messages.formatResourceString(
							"Eval.redundant.img.al.error.msg",
							Constants.LINE_SEP));
		}
		tmpSB.append(Constants.LINE_SEP);

		if (noImageAltCount > 0) {
			tmpSB.append(" " + Messages.getString("Eval.no.img.alt") + " "
					+ noImageAltCount + Constants.LINE_SEP);
		}
		if (wrongImageAltCount > 0) {
			tmpSB.append(" " + Messages.getString("Eval.wrong.img.alt") + " "
					+ wrongImageAltCount + Constants.LINE_SEP);
		}
		if (redundantImageAltCount > 0) {
			tmpSB.append(" " + Messages.getString("Eval.redundant.img.alt")
					+ " " + redundantImageAltCount + Constants.LINE_SEP);
		}

		return (tmpSB.toString());
	}

}
