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

package org.eclipse.actf.visualization.internal.eval;

import java.text.MessageFormat;

import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.actf.visualization.eval.IGuidelineItem;
import org.eclipse.actf.visualization.eval.problem.IProblemConst;
import org.eclipse.actf.visualization.internal.eval.guideline.GuidelineItemDescription;
import org.eclipse.actf.visualization.internal.eval.guideline.MetricsItem;
import org.eclipse.swt.graphics.Image;

public class EvaluationItemImpl implements IEvaluationItem {

	private static final Image ERROR_C_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ErrC.png").createImage();
	private static final Image ERROR_N_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ErrN.png").createImage();
	private static final Image ERROR_L_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/ErrL.png").createImage();
	private static final Image ERROR_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/Err.png").createImage();

	private static final Image WARN_C_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/WarnC.png").createImage();
	private static final Image WARN_N_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/WarnN.png").createImage();
	private static final Image WARN_L_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/WarnL.png").createImage();
	private static final Image WARN_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/Warn.png").createImage();

	private static final Image INFO_C_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/InfoC.png").createImage();
	private static final Image INFO_N_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/InfoN.png").createImage();
	private static final Image INFO_L_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/InfoL.png").createImage();
	private static final Image INFO_IMAGE = EvaluationPlugin
			.getImageDescriptor("icons/Info.png").createImage();

	private String id = "";

	private GuidelineItemImpl[] guidelines = new GuidelineItemImpl[0];

	private MetricsItem[] metrics = new MetricsItem[0];

	private String description;

	private String[] tableDataMetrics = new String[0];

	private String[] tableDataGuideline = new String[0];

	private Image[] metricsIcons = new Image[0];

	private int[] metricsScores = new int[0];

	private short severity = SEV_INFO;

	private String severityStr = SEV_INFO_STR;

	/**
	 * @param id
	 */
	public EvaluationItemImpl(String id, String severity) {
		this.id = id;
		setSeverity(severity);
		description = GuidelineItemDescription.getDescription(id);
	}

	public String createDescription(String targetString) {
		return MessageFormat.format(description, new Object[] { targetString });
	}

	public String createDescription() {
		return (description);
	}

	public GuidelineItemImpl[] getGuidelines() {
		return guidelines;
	}

	public void setGuidelines(GuidelineItemImpl[] guidelines) {
		this.guidelines = guidelines;
	}

	public String getId() {
		return id;
	}

	public int getSeverity() {
		return severity;
	}

	public String getSeverityStr() {
		return severityStr;
	}

	private void setSeverity(String _severityStr) {
		severity = SEV_INFO;
		severityStr = SEV_INFO_STR;
		if (_severityStr != null) {
			_severityStr = _severityStr.trim();
			if (SEV_ERROR_STR.equalsIgnoreCase(_severityStr)) {
				severity = SEV_ERROR;
				severityStr = SEV_ERROR_STR;
			} else if (SEV_WARNING_STR.equalsIgnoreCase(_severityStr)) {
				severity = SEV_WARNING;
				severityStr = SEV_WARNING_STR;
			}
			// else{
			// severity = SEV_INFO;
			// }
		}
	}

	public MetricsItem[] getMetricsItems() {
		return metrics;
	}

	public void setMetrics(MetricsItem[] metrics) {
		this.metrics = metrics;
	}

	public void initTableData(String[] guidelineNames, String[] metricsNames) {
		tableDataGuideline = new String[guidelineNames.length];
		tableDataMetrics = new String[metricsNames.length];
		metricsScores = new int[metricsNames.length];
		metricsIcons = new Image[metricsNames.length];
		for (int i = 0; i < guidelineNames.length; i++) {
			StringBuffer tmpSB = new StringBuffer();
			boolean notFirst = false;
			for (int j = 0; j < guidelines.length; j++) {
				IGuidelineItem tmpItem = guidelines[j];
				if (guidelineNames[i].equalsIgnoreCase(tmpItem
						.getGuidelineName())) {
					if (notFirst) {
						tmpSB.append(", ");
					} else {
						notFirst = true;
					}
					if (tmpItem.getLevel().length() > 0) {
						tmpSB.append(tmpItem.getLevel() + ": "
								+ tmpItem.getId());
					} else {
						tmpSB.append(tmpItem.getId());
					}
				}
			}
			tableDataGuideline[i] = tmpSB.toString();
		}

		for (int i = 0; i < metricsNames.length; i++) {
			String curName = metricsNames[i];
			tableDataMetrics[i] = "";
			metricsScores[i] = 0;
			metricsIcons[i] = null;
			for (int j = 0; j < metrics.length; j++) {
				MetricsItem tmpItem = metrics[j];
				if (curName.equalsIgnoreCase(tmpItem.getMetricsName())) {
					metricsScores[i] = tmpItem.getScore();
					if (tmpItem.getScore() != 0) {
						tableDataMetrics[i] = Integer.toString(-tmpItem
								.getScore())
								+ " ";
					}

					switch (this.severity) {
					case SEV_ERROR:
						if (curName.equalsIgnoreCase("compliance")) {
							metricsIcons[i] = ERROR_C_IMAGE;
						} else if (curName.equalsIgnoreCase("navigability")) {
							metricsIcons[i] = ERROR_N_IMAGE;
						} else if (curName.equalsIgnoreCase("listenability")) {
							metricsIcons[i] = ERROR_L_IMAGE;
						} else {
							metricsIcons[i] = ERROR_IMAGE;
						}
						tableDataMetrics[i] = tableDataMetrics[i] + "("
								+ IProblemConst.ESSENTIAL + ")";
						break;
					case SEV_WARNING:
						if (curName.equalsIgnoreCase("compliance")) {
							metricsIcons[i] = WARN_C_IMAGE;
						} else if (curName.equalsIgnoreCase("navigability")) {
							metricsIcons[i] = WARN_N_IMAGE;
						} else if (curName.equalsIgnoreCase("listenability")) {
							metricsIcons[i] = WARN_L_IMAGE;
						} else {
							metricsIcons[i] = WARN_IMAGE;
						}
						tableDataMetrics[i] = tableDataMetrics[i] + "("
								+ IProblemConst.USER_CHECK + ")";
						break;
					case SEV_INFO:
						if (curName.equalsIgnoreCase("compliance")) {
							metricsIcons[i] = INFO_C_IMAGE;
						} else if (curName.equalsIgnoreCase("navigability")) {

							metricsIcons[i] = INFO_N_IMAGE;
						} else if (curName.equalsIgnoreCase("listenability")) {
							metricsIcons[i] = INFO_L_IMAGE;
						} else {
							metricsIcons[i] = INFO_IMAGE;
						}
						tableDataMetrics[i] = tableDataMetrics[i] + "("
								+ IProblemConst.INFO + ")";
						break;
					}

				}
			}
		}

	}

	public String toString() {
		StringBuffer tmpSB = new StringBuffer();
		tmpSB.append(id + " " + severity + " : " + FileUtils.LINE_SEP);
		for (int i = 0; i < guidelines.length; i++) {
			tmpSB.append("  " + guidelines[i] + FileUtils.LINE_SEP);
		}

		for (int i = 0; i < tableDataGuideline.length; i++) {
			tmpSB.append("  " + tableDataGuideline[i] + FileUtils.LINE_SEP);
		}

		for (int i = 0; i < metrics.length; i++) {
			tmpSB.append("  " + metrics[i] + FileUtils.LINE_SEP);
		}

		for (int i = 0; i < tableDataMetrics.length; i++) {
			tmpSB.append("  " + tableDataMetrics[i] + FileUtils.LINE_SEP);
		}

		return (tmpSB.toString());
	}

	public int[] getMetricsScores() {
		return metricsScores;
	}

	public String[] getTableDataGuideline() {
		return tableDataGuideline;
	}

	public String[] getTableDataMetrics() {
		return tableDataMetrics;
	}

	public Image[] getMetricsIcons() {
		return metricsIcons;
	}
}
