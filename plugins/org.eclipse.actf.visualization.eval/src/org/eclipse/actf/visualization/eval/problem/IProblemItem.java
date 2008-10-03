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

package org.eclipse.actf.visualization.eval.problem;

import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.swt.graphics.Image;
import org.w3c.dom.Node;

/**
 *
 */
public interface IProblemItem {// extends IEvaluationItem {

	/**
	 * 
	 */
	public static final int SEV_ERROR = IEvaluationItem.SEV_ERROR;

	/**
	 * 
	 */
	public static final int SEV_WARNING = IEvaluationItem.SEV_WARNING;

	/**
	 * 
	 */
	public static final int SEV_INFO = IEvaluationItem.SEV_INFO;

	/**
	 * @return
	 */
	public abstract IEvaluationItem getEvaluationItem();

	/**
	 * @return
	 */
	public abstract String getDescription();

	/**
	 * @return
	 */
	public abstract int getSerialNumber();

	/**
	 * @return
	 */
	public abstract Node getTargetNode();

	/**
	 * @return
	 */
	public abstract String getTargetString();

	/**
	 * @return
	 */
	public abstract String getTargetStringForHPB();

	/**
	 * @return
	 */
	public abstract boolean isCanHighlight();

	/**
	 * @param canHighlight
	 */
	public abstract void setCanHighlight(boolean canHighlight);

	/**
	 * @param checkItem
	 */
	public abstract void setCheckItem(IEvaluationItem checkItem);

	/**
	 * @param description
	 */
	public abstract void setDescription(String description);

	/**
	 * @param serialNumber
	 */
	public abstract void setSerialNumber(int serialNumber);

	/**
	 * @param targetNodeInfo
	 */
	void setHighlightTargetNodeInfo(HighlightTargetNodeInfo targetNodeInfo);

	/**
	 * @return
	 */
	HighlightTargetNodeInfo getHighlightTargetNodeInfo();

	/**
	 * @param targetId
	 */
	void setHighlightTargetIds(HighlightTargetId targetId);

	/**
	 * @param targetIds
	 */
	void setHighlightTargetIds(HighlightTargetId[] targetIds);

	/**
	 * @return
	 */
	HighlightTargetId[] getHighlightTargetIds();

	/**
	 * @param targetNode
	 */
	public abstract void setTargetNode(Node targetNode);

	/**
	 * @param targetString
	 */
	public abstract void setTargetString(String targetString);

	/**
	 * @param targetString
	 */
	public abstract void setTargetStringForHPB(String targetString);

	/**
	 * @return
	 */
	public abstract int getLine();

	/**
	 * @return
	 */
	public abstract String getLineStr();

	/**
	 * @param line
	 */
	public abstract void setLine(int line);

	/**
	 * @return
	 */
	public abstract String getLineStrMulti();

	/**
	 * @param targetSourceInfo
	 */
	void setHighlightTargetSourceInfo(HighlightTargetSourceInfo targetSourceInfo);

	/**
	 * @param targetSourceInfo
	 */
	void setHighlightTargetSourceInfo(
			HighlightTargetSourceInfo[] targetSourceInfo);

	/**
	 * @return
	 */
	HighlightTargetSourceInfo[] getHighlightTargetSoruceInfo();

	/**
	 * @param visitor
	 */
	public abstract void accept(IProblemItemVisitor visitor);

	// TODO
	/**
	 * @return
	 */
	public String getId();

	/**
	 * @return
	 */
	public int getSeverity();

	/**
	 * @return
	 */
	public String getSeverityStr();

	/**
	 * @return
	 */
	public int[] getMetricsScores();

	/**
	 * @return
	 */
	public Image[] getMetricsIcons();

	/**
	 * @return
	 */
	public String[] getTableDataGuideline();

}
