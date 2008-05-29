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

package org.eclipse.actf.visualization.lowvision.eval;

import java.util.List;

import org.eclipse.actf.ui.util.HighlightStringListener;
import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ProblemItemLV;
import org.eclipse.actf.visualization.lowvision.ui.internal.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;


public class SummaryEvaluationLV {

	public static HighlightStringListener getHighLightStringListener() {
		HighlightStringListener hlsl = new HighlightStringListener();
		Color blue = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
		Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
		Color green = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);

		hlsl
				.addTarget(
						Messages
								.getString("EvalLV.no.color.difficult.distinguish"), blue, SWT.BOLD); //$NON-NLS-1$
		hlsl
				.addTarget(
						Messages.getString("EvalLV.font.might.enough.to.read"), blue, SWT.BOLD); //$NON-NLS-1$

		hlsl
				.addTarget(
						Messages
								.getString("EvalLV.color.difficult.distinguish"), red, SWT.BOLD); //$NON-NLS-1$
		hlsl
				.addTarget(
						Messages
								.getString("EvalLV.color.might.difficult.distinguish"), red, SWT.BOLD); //$NON-NLS-1$
		hlsl
				.addTarget(
						Messages.getString("EvalLV.font.too.small.to.read"), red, SWT.BOLD); //$NON-NLS-1$
		hlsl
				.addTarget(
						Messages
								.getString("EvalLV.font.might.too.small.to.read"), red, SWT.BOLD); //$NON-NLS-1$
		hlsl
				.addTarget(
						Messages.getString("EvalLV.page.have.fixed.font"), red, SWT.BOLD); //$NON-NLS-1$

		hlsl.addTarget(Messages.getString("EvalLV.0"), green, SWT.BOLD);
		// TODO

		return (hlsl);
	}

	private ProblemItemLV[] _problems = new ProblemItemLV[0];

	public SummaryEvaluationLV(List<IProblemItem> problemList) {
		try {
			_problems = new ProblemItemLV[problemList.size()];
			problemList.toArray(_problems);
		} catch (Exception e) {
			_problems = new ProblemItemLV[0];
		}
	}

	public static String notSupported() {
		return (Messages.getString("EvalLV.0")); //$NON-NLS-1$
	}

	public String getOverview() {
		StringBuffer tmpSB = new StringBuffer(512);

		int problemCount[] = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		int severeCount[] = new int[] { 0, 0, 0, 0, 0, 0, 0 };

		for (int i = 0; i < _problems.length; i++) {
			ProblemItemLV curProblem = _problems[i];
			problemCount[curProblem.getSubType()]++;
			if (curProblem.getSeverityLV() > 49) {
				severeCount[curProblem.getSubType()]++;
			}
		}

		// for(int i=0;i<problemCount.length;i++){
		// System.out.print(problemCount[i]+"/"+severeCount[i]+", ");
		// }

		if (problemCount[1] > 0 || problemCount[3] > 0) {
			if (severeCount[1] > 0 || severeCount[3] > 0) {
				tmpSB
						.append(Messages
								.getString("EvalLV.color.difficult.distinguish") + FileUtils.LINE_SEP); //$NON-NLS-1$
			} else {
				tmpSB
						.append(Messages
								.getString("EvalLV.color.might.difficult.distinguish") + FileUtils.LINE_SEP); //$NON-NLS-1$
			}
			tmpSB
					.append(Messages.getString("EvalLV.click.detailed.report") + FileUtils.LINE_SEP); //$NON-NLS-1$

			tmpSB.append(FileUtils.LINE_SEP);
			if (problemCount[1] > 0) {
				tmpSB
						.append(Messages
								.getString("EvalLV.text.color.combination") + " " + problemCount[1] + FileUtils.LINE_SEP); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (problemCount[3] > 0) {
				tmpSB
						.append(Messages
								.getString("EvalLV.img.color.combination") + " " + problemCount[3] + FileUtils.LINE_SEP); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			tmpSB
					.append(Messages
							.getString("EvalLV.no.color.difficult.distinguish") + FileUtils.LINE_SEP); //$NON-NLS-1$
		}

		tmpSB.append(FileUtils.LINE_SEP);

		if (problemCount[5] > 0 || problemCount[6] > 0) {
			if (severeCount[5] > 0 || severeCount[6] > 0) {
				tmpSB
						.append(Messages
								.getString("EvalLV.font.too.small.to.read") + FileUtils.LINE_SEP); //$NON-NLS-1$
			} else {
				tmpSB
						.append(Messages
								.getString("EvalLV.font.might.too.small.to.read") + FileUtils.LINE_SEP); //$NON-NLS-1$
			}
			tmpSB
					.append(Messages.getString("EvalLV.click.detailed.report") + FileUtils.LINE_SEP); //$NON-NLS-1$

			tmpSB.append(FileUtils.LINE_SEP);
			if (problemCount[5] > 0) {
				tmpSB
						.append(Messages.getString("EvalLV.font.too.small") + " " + problemCount[5]); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (problemCount[6] > 0) {
				tmpSB
						.append(Messages
								.getString("EvalLV.font.too.small.fixed") + " " + problemCount[6]); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else if (problemCount[4] > 0) {
			tmpSB
					.append(Messages.getString("EvalLV.page.have.fixed.font") + FileUtils.LINE_SEP); //$NON-NLS-1$
			tmpSB.append(FileUtils.LINE_SEP);
			tmpSB
					.append(Messages.getString("EvalLV.font.fixed") + " " + problemCount[4]); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			tmpSB
					.append(Messages
							.getString("EvalLV.font.might.enough.to.read") + FileUtils.LINE_SEP); //$NON-NLS-1$
		}

		return (tmpSB.toString());
	}

}
