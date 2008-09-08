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
package org.eclipse.actf.visualization.blind.ui.internal;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.eclipse.actf.mediator.Mediator;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.actf.ui.util.DialogSave;
import org.eclipse.actf.visualization.blind.IBlindVisualizer;
import org.eclipse.actf.visualization.blind.internal.BlindVisualizerExtension;
import org.eclipse.actf.visualization.engines.blind.BlindVizResourceUtil;
import org.eclipse.actf.visualization.engines.blind.eval.EvaluationResultBlind;
import org.eclipse.actf.visualization.engines.blind.eval.PageEvaluation;
import org.eclipse.actf.visualization.engines.blind.eval.SummaryEvaluation;
import org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.IHighlightElementListener;
import org.eclipse.actf.visualization.engines.blind.html.util.SaveReportBlind;
import org.eclipse.actf.visualization.engines.blind.html.util.VisualizeReportUtil;
import org.eclipse.actf.visualization.eval.IEvaluationResult;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetId;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Document;

public class PartControlBlind implements IHighlightElementListener {

	public final static String BLIND_REPORT_FILE = "report.htm";

	private IBlindVisualizer[] blindVizualizers = BlindVisualizerExtension
			.getVisualizers();

	private BlindVisualizationBrowser _blindBrowser;

	private boolean _canSave = false;

	private PageData _pageData;

	private String maxReachingTimeS = "";

	private PageEvaluation _pageEval;

	private Document resultDoc;

	private Shell _shell;

	private String targetUrl;

	private Mediator mediator = Mediator.getInstance();

	private IVisualizationView vizView;

	private IEvaluationResult checkResult = new EvaluationResultBlind();

	public PartControlBlind(IVisualizationView vizView, Composite parent) {
		this.vizView = vizView;
		this._shell = parent.getShell();

		new BlindToolBar(parent, SWT.NONE, this);

		this._blindBrowser = new BlindVisualizationBrowser(parent);
		this._blindBrowser.setBrowserSilent();
	}

	public void doSave() {
		String saveFile = DialogSave.open(_shell, DialogSave.HTML, targetUrl,
				"_blind.htm"); //$NON-NLS-1$

		if (null == this.resultDoc || null == saveFile) {
			return;
		}

		String imageBriefDir = saveFile.substring(
				saveFile.lastIndexOf("\\") + 1, saveFile.lastIndexOf("."))
				+ "/";
		// 2007.09.25 remove space character to include JavaScript files
		imageBriefDir = imageBriefDir.replace(' ', '_');
		saveReportFile(saveFile, imageBriefDir, true);
	}

	public int doVisualize() {
		return doVisualize(true);
	}

	public int doVisualize(boolean isShowResult) {

		IModelService modelService = ModelServiceUtils.getActiveModelService();
		int ret = IBlindVisualizer.ERROR;
		if (modelService == null) {
			return ret;
		}

		String resultFilePath = "";

		targetUrl = modelService.getURL();
		maxReachingTimeS = "";

		checkResult = new EvaluationResultBlind();
		mediator.setReport(vizView, checkResult);// clear result
		_blindBrowser.navigate("about:blank");

		_canSave = false;

		vizView.setStatusMessage(Messages.getString("BlindView.Now_preparing"));

		for (IBlindVisualizer bvh : blindVizualizers) {
			if (bvh.setModelService(modelService)) {
				bvh.setVisualizationView(vizView);

				ret = bvh.visualize();

				_pageData = bvh.getPageData();
				checkResult = bvh.getCheckResult();
				resultDoc = bvh.getResultDocument();
				maxReachingTimeS = bvh.createMaxTimeString();

				resultFilePath = bvh.getResultFile().getAbsolutePath();

				if (ret > IBlindVisualizer.ERROR) { // OK, Frame
					vizView.setStatusMessage(Messages
							.getString("BlindView.Now_rendering"));
					CreateReport cr = new CreateReport(checkResult, new File(
							BlindVizResourceUtil.getTempDirectory(),
							BLIND_REPORT_FILE));
					if (isShowResult) {
						_blindBrowser.navigate(resultFilePath);
						_shell.getDisplay().asyncExec(cr);
					} else {
						_blindBrowser.navigate("about:blank");
						_shell.getDisplay().syncExec(cr);
					}

					_canSave = true;
				}
				return ret;
			}
		}

		System.out.println("not supported: " + modelService.getID() + " "
				+ modelService.getCurrentMIMEType());
		return IBlindVisualizer.ERROR;
	}

	/**
	 * @return
	 */
	public PageEvaluation getPageEvaluation() {
		return _pageEval;
	}

	public PageData getPageData() {
		return _pageData;
	}

	public void saveReportFile(String sFileName, String imageBriefDir,
			boolean bAccessory) {
		if (_canSave) {
			vizView.setStatusMessage(Messages
					.getString("BlindView.saving_file")); // //$NON-NLS-1$

			// TODO encoding
			SaveReportBlind.saveReport((Document) resultDoc.cloneNode(true),
					mediator.getReport(vizView), sFileName, imageBriefDir,
					maxReachingTimeS, _pageEval, bAccessory);

			vizView.setStatusMessage(Messages
					.getString("BlindView.end_saving_file")); // //$NON-NLS-1$
		}
	}

	private class CreateReport extends Thread {
		IEvaluationResult _checkResult;

		File targetFile;

		CreateReport(IEvaluationResult checkResult, File filePath) {
			this._checkResult = checkResult;
			this.targetFile = filePath;
		}

		public void run() {
			_pageEval = new PageEvaluation(_checkResult.getProblemList(),
					_pageData);
			VisualizeReportUtil.createReport(this.targetFile, _pageEval);
			_shell.getDisplay().asyncExec(new Runnable() {
				public void run() {

					// TODO through mediator
					_checkResult.setSummaryReportUrl(targetFile
							.getAbsolutePath());
					_checkResult.setSummaryReportText(_pageEval.getSummary());
					_checkResult.setLineStyleListener(SummaryEvaluation
							.getHighLightStringListener());
					mediator.setReport(vizView, _checkResult);

					vizView.setStatusMessage(Messages
							.getString("BlindView.Done"));
				}
			});
		}
	}

	private void execScript(String script) {
		_blindBrowser.execScript(script);
	}

	public void clearHighlight() {
		_blindBrowser.clearHighlight();
	}

	protected IEvaluationResult getCheckResult() {
		return checkResult;
	}

	public void highlight(List<HighlightTargetId> targetIdList) {
		if (null != targetIdList) {
			switch (targetIdList.size()) {
			case 0:
				break;
			case 1:
				execScript("setHighlight(" + targetIdList.get(0).getStartId()
						+ "," + targetIdList.get(0).getEndId() + ");");
				break;
			default:
				Iterator<HighlightTargetId> ite = targetIdList.iterator();
				StringBuffer strStart = new StringBuffer(512);
				StringBuffer strEnd = new StringBuffer(512);
				HighlightTargetId tmpId = ite.next();
				strStart.append(tmpId.getStartId());
				strEnd.append(tmpId.getEndId());
				while (ite.hasNext()) {
					tmpId = ite.next();
					strStart.append("," + tmpId.getStartId()); //$NON-NLS-1$
					strEnd.append("," + tmpId.getEndId()); //$NON-NLS-1$
				}

				String highlightScript = "setHighlight2(new Array(" //$NON-NLS-1$
						+ strStart.toString() + "), new Array(" //$NON-NLS-1$
						+ strEnd.toString() + "));"; //$NON-NLS-1$
				execScript(highlightScript);
			}

		}
	}
}
