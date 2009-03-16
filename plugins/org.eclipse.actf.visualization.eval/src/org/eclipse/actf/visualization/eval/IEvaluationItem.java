/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval;

import org.eclipse.swt.graphics.Image;

/**
 * Interface for evaluation item information
 */
public interface IEvaluationItem {
	/**
	 * Severity ID: error
	 */
	public static final int SEV_ERROR = 1;

	/**
	 * Severity ID: warning
	 */
	public static final int SEV_WARNING = 2; // add "Possible error"?

	/**
	 * Severity ID: informational
	 */
	public static final int SEV_INFO = 4;

	/**
	 * String for error severity
	 */
	public static final String SEV_ERROR_STR = "error"; //$NON-NLS-1$

	/**
	 * String for warning severity
	 */
	public static final String SEV_WARNING_STR = "warning"; //$NON-NLS-1$

	/**
	 * String for informational severity
	 */
	public static final String SEV_INFO_STR = "info"; //$NON-NLS-1$

	/**
	 * @return ID of evaluation item
	 */
	public String getId();

	/**
	 * @return severity of evaluation item
	 */
	public int getSeverity();

	/**
	 * @return severity as String
	 */
	public String getSeverityStr();

	/**
	 * @return array of scores for each evaluation metric
	 */
	public int[] getMetricsScores();

	/**
	 * @return guideline information to be shown in the result table
	 */
	public String[] getTableDataGuideline();

	/**
	 * @return evaluation metrics information to be shown in the result table
	 */
	public String[] getTableDataMetrics();

	/**
	 * @return icons for evaluation metrics to be shown in the result table
	 */
	public Image[] getMetricsIcons();

	/**
	 * @return get corresponding guideline items of this evaluation item
	 */
	public IGuidelineItem[] getGuidelines();

	/**
	 * @return description about this evaluation item
	 */
	public String createDescription();

	/**
	 * 
	 * @param targetString
	 *            target String to embed
	 * @return description about this evaluation item. Specified target String
	 *         will be embedded into the description.
	 */
	public String createDescription(String targetString);

}
