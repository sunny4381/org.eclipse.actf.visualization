/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.blind.odfbyhtml;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import org.eclipse.actf.model.dom.html.HTMLParserFactory;
import org.eclipse.actf.model.dom.html.IHTMLParser;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.util.dom.DomPrintUtil;
import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.IVisualizationConst;
import org.eclipse.actf.visualization.blind.BlindVisualizerBase;
import org.eclipse.actf.visualization.blind.IBlindVisualizer;
import org.eclipse.actf.visualization.blind.ui.internal.Messages;
import org.eclipse.actf.visualization.engines.blind.BlindVizResourceUtil;
import org.eclipse.actf.visualization.engines.blind.eval.EvaluationResultBlind;
import org.eclipse.actf.visualization.engines.blind.html.IVisualizeMapData;
import org.eclipse.actf.visualization.engines.blind.html.VisualizeEngine;
import org.eclipse.actf.visualization.engines.blind.html.util.HtmlErrorLogListener;
import org.eclipse.actf.visualization.engines.blind.html.util.VisualizeReportUtil;
import org.eclipse.actf.visualization.eval.CheckTargetFactory;
import org.eclipse.actf.visualization.eval.EvaluationUtil;
import org.eclipse.actf.visualization.eval.ICheckTarget;
import org.eclipse.actf.visualization.eval.IChecker;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetNodeInfo;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ProblemItemImpl;
import org.eclipse.actf.visualization.util.html2view.Html2ViewMapData;
import org.eclipse.actf.visualization.util.html2view.Html2ViewMapMaker;
import org.w3c.dom.Document;

public class BlindVisualizerOdfByHtml extends BlindVisualizerBase implements
		IBlindVisualizer {

	private final String ODF_HTML_FILE_NAME = "ODF.html";

	private final String odf_html_fileS = tmpDirS + ODF_HTML_FILE_NAME;

	private final String NO_ALT_IMAGE = "10101";

	private static final String MAPPED_HTML_FILE_PRE = "MappedHTML";

	private static final String HTML_SOURCE_FILE = "source.html";

	public boolean setModelService(IModelService targetModel) {
		if (super.setModelService(targetModel)) {
			if (targetUrl.startsWith("file://")) {
				targetUrl = targetUrl.substring(8);
			}
			targetUrl = targetUrl.replaceAll("%20", " ");
			return true;
		}
		return false;
	}

	public int visualize() {
		if (null == modelService) {
			return ERROR;
		}

		GuidelineHolder.getInstance().setTargetMimeType(
				modelService.getCurrentMIMEType());

		modelService.saveDocumentAsHTMLFile(odf_html_fileS);

		checkResult = new EvaluationResultBlind();

		try {

			FileUtils
					.copyFile(odf_html_fileS, tmpDirS + HTML_SOURCE_FILE, true);

			Vector<Html2ViewMapData> html2ViewMapV = new Vector<Html2ViewMapData>();

			IHTMLParser htmlParser = HTMLParserFactory.createHTMLParser();
			HtmlErrorLogListener errorLogListener = new HtmlErrorLogListener();
			htmlParser.addErrorLogListener(errorLogListener);
			String targetFile = tmpDirS + MAPPED_HTML_FILE_PRE + ".html";

			html2ViewMapV = Html2ViewMapMaker.makeMap(ODF_HTML_FILE_NAME,
					MAPPED_HTML_FILE_PRE + ".html", tmpDirS);
			if (html2ViewMapV.size() == 0) {
				targetFile = odf_html_fileS;
			}

			IHTMLParser tmpHtmlParser = HTMLParserFactory.createHTMLParser();
			tmpHtmlParser.parse(new FileInputStream(targetFile));
			Document document = tmpHtmlParser.getDocument();

			if (document == null) {
				return ERROR;
			}

			setStatusMessage(Messages.getString("BlindView.Now_processing")); // //$NON-NLS-1$

			pageData = new PageData();

			VisualizeEngine engine = new VisualizeEngine();
			engine.setBaseUrl(""); //$NON-NLS-1$
			// TODO "\"->"/" windows
			// engine.setBaseUrl("file:///"+RESULT_DIR);

			engine.setTargetUrl(targetUrl);
			engine.setDocument(document);
			engine.setHtml2viewMapV(html2ViewMapV);
			engine.setInvisibleIdSet(new HashSet<String>());
			engine.setPageData(pageData);
			engine.visualize();

			maxReachingTime = engine.getMaxTime();
			setInfoMessage(getMaxReachingTime());

			resultDocument = engine.getResult();
			checkResult.setProblemList(engine.getProbelems());
			checkResult.setTargetUrl(targetUrl);
			checkResult.addAssociateFile(engine.getVariantFile());

			IVisualizeMapData mapData = engine.getVisualizeMapData();

			// TODO
			checkResult.setSourceFile(new File(tmpDirS + HTML_SOURCE_FILE));

			ArrayList<IProblemItem> tmpResults = new ArrayList<IProblemItem>(
					1024);

			Document odfDoc = modelService.getDocument();
			ICheckTarget checkTarget = CheckTargetFactory.createCheckTarget(odfDoc, modelService
					.getURL());
			checkTarget.setAdditionalDocument("html", document);
			IChecker[] checkers = EvaluationUtil.getCheckers();
			for (int i = 0; i < checkers.length; i++) {
				// TODO mimetype -> ???
				if (checkers[i].isTargetFormat(modelService
						.getCurrentMIMEType())
						&& checkers[i].isEnabled()) {
					long startTime = System.currentTimeMillis();
					tmpResults.addAll(checkers[i].check(checkTarget));
					DebugPrintUtil.devOrDebugPrintln(System.currentTimeMillis()
							- startTime);
				}
			}

			visualizeError(resultDocument, tmpResults);

			// TODO support blind biz -> visitor
			for (int i = 0; i < tmpResults.size(); i++) {
				IProblemItem tmpItem = (IProblemItem) tmpResults.get(i);
				HighlightTargetNodeInfo nodeInfo = tmpItem
						.getHighlightTargetNodeInfo();
				if (nodeInfo != null) {
					tmpItem.setHighlightTargetIds(nodeInfo
							.getHighlightTargetIds(mapData.getOrig2idMap()));
				}
			}
			checkResult.addProblemItems(tmpResults);
			checkResult.accept(pageData);

			// TODO move (add Icons into result doc) here

			resultFile = BlindVizResourceUtil.createTempFile(
					IVisualizationConst.PREFIX_RESULT,
					IVisualizationConst.SUFFIX_HTML);

			try {
				// PrintWriter pw = new PrintWriter(new OutputStreamWriter(
				// new FileOutputStream(resultFile), "UTF-8"));
				// ((IHTMLDocumentACTF) resultDocument).printAsSGML(pw, true);
				// pw.flush();
				// pw.close();
				DomPrintUtil dpu = new DomPrintUtil(resultDocument);
				dpu.writeToFile(resultFile);
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		} catch (Exception e) {
			setStatusMessage("Visualization Error");
			e.printStackTrace();
			return ERROR;
		}

		return 0;
	}

	public boolean isTarget(IModelService modelService) {
		return (null != modelService && modelService.getCurrentMIMEType()
				.startsWith("application/vnd.oasis.opendocument."));
	}

	private void visualizeError(Document resultDoc, List<IProblemItem> problems) {

		int size = problems.size();

		for (int i = 0; i < size; i++) {
			Object obj = problems.get(i);
			if (obj instanceof ProblemItemImpl) {
				ProblemItemImpl prob = (ProblemItemImpl) obj;
				if (prob.getId().equals("O_" + NO_ALT_IMAGE)) {
					VisualizeReportUtil.visualizeError(resultDoc, prob);
				}
			}
		}
	}

}
