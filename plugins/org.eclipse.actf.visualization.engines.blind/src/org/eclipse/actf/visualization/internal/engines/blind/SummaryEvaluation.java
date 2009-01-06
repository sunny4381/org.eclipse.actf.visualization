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

package org.eclipse.actf.visualization.internal.engines.blind;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.visualization.engines.blind.eval.PageEvaluation;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;

import com.ibm.icu.text.MessageFormat;

/**
 * 
 */
public class SummaryEvaluation {

	private PageEvaluation pe;

	private PageData pageData;

	private int noImageAltCount;

	private int wrongImageAltCount;

	private int redundantImageAltCount = 0;

	private GuidelineHolder guidelineHolder = GuidelineHolder.getInstance();

	private boolean hasError;

	public SummaryEvaluation(PageEvaluation pe, PageData pd, boolean hasError) {
		this.pe = pe;
		this.pageData = pd;
		this.hasError = hasError;
	}

	/**
	 * @return
	 */
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
				if (hasError) {
					aboutComp
							.append(Messages.Eval_completely_compliant_with_some_errors
									+ FileUtils.LINE_SEP);

					if (totalAltError > 0) {
						aboutComp
								.append(Messages.Eval_confirm_alt_attributes_first);
						aboutComp.append(getImageAltStatistics());
					} else {
						aboutComp
								.append(Messages.Eval_confirm_errors_detailed_report);
					}
				} else {
					if (hasOther && other != 100) {
						aboutComp.append(MessageFormat.format(
								Messages.Eval_some_errors_on_metrics,
								(Object[]) (new String[] {
										noGoodMetrics.substring(0,
												noGoodMetrics.length() - 1),
										FileUtils.LINE_SEP })));
					} else {
						if (comp == 100) {
							isGood = true;
							aboutComp
									.append(Messages.Eval_completely_compliant);
						} else {
							isGood = true;
							aboutComp
									.append(MessageFormat
											.format(
													Messages.Eval_completely_compliant_with_user_check_items,
													new Object[]{FileUtils.LINE_SEP}));
						}
					}
				}
			} else {
				if (comp > 50) {
					aboutComp.append(Messages.Eval_some_accessibility_issues
							+ FileUtils.LINE_SEP);
				} else {
					aboutComp.append(Messages.Eval_many_accessibility_issues
							+ FileUtils.LINE_SEP);
				}

				if (totalAltError > 0) {
					aboutComp.append(Messages.Eval_confirm_alt_attributes_first
							+ FileUtils.LINE_SEP);
					aboutComp.append(getImageAltStatistics());
				} else {
					aboutComp
							.append(Messages.Eval_confirm_errors_detailed_report);
				}
			}
		}

		//
		if (hasNav) {
			if (nav > 80) {
				if (pageData.getMaxTime() > 240) {
					aboutNav.append(MessageFormat.format(
							Messages.Eval_navigability_long_time_error_msg,
							(Object[]) (new String[] { FileUtils.LINE_SEP,
									FileUtils.LINE_SEP, FileUtils.LINE_SEP,
									FileUtils.LINE_SEP }))
							+ FileUtils.LINE_SEP);

				} else {
					aboutNav.append(MessageFormat.format(
							Messages.Eval_navigability_good_msg,
							(Object[]) (new String[] { FileUtils.LINE_SEP,
									FileUtils.LINE_SEP }))
							+ FileUtils.LINE_SEP);
				}
			} else {
				isGood = false;
				aboutNav.append(MessageFormat.format(
						Messages.Eval_navigability_low_score_error_msg,
						(Object[]) (new String[] { FileUtils.LINE_SEP,
								FileUtils.LINE_SEP, FileUtils.LINE_SEP,
								FileUtils.LINE_SEP }))
						+ FileUtils.LINE_SEP);
			}
		}

		if ((hasComp || hasNav) && isGood) {
			tmpSB.append(Messages.Eval_excellent + FileUtils.LINE_SEP
					+ FileUtils.LINE_SEP);
		}
		tmpSB.append(aboutNav + FileUtils.LINE_SEP);
		tmpSB.append(aboutComp);

		return (tmpSB.toString());
	}

	private String getImageAltStatistics() {
		StringBuffer tmpSB = new StringBuffer();

		if (noImageAltCount > 0) {
			tmpSB.append(" -" + Messages.Eval_no_img_alt_error_msg
					+ FileUtils.LINE_SEP);
		}
		if (wrongImageAltCount > 0) {
			tmpSB.append(" -" + Messages.Eval_wrong_img_alt_error_msg
					+ FileUtils.LINE_SEP);
		}
		if (redundantImageAltCount > 0) {
			tmpSB.append(" -" + Messages.Eval_redundant_img_alt_error_msg
					+ FileUtils.LINE_SEP);
		}
		tmpSB.append(FileUtils.LINE_SEP);

		if (noImageAltCount > 0) {
			tmpSB.append(" " + Messages.Eval_no_img_alt + " " + noImageAltCount
					+ FileUtils.LINE_SEP);
		}
		if (wrongImageAltCount > 0) {
			tmpSB.append(" " + Messages.Eval_wrong_img_alt + " "
					+ wrongImageAltCount + FileUtils.LINE_SEP);
		}
		if (redundantImageAltCount > 0) {
			tmpSB.append(" " + Messages.Eval_redundant_img_alt + " "
					+ redundantImageAltCount + FileUtils.LINE_SEP);
		}

		return (tmpSB.toString());
	}
}
