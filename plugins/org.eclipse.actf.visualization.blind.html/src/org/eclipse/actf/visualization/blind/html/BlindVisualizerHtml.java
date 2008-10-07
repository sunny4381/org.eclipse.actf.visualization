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
package org.eclipse.actf.visualization.blind.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

import org.eclipse.actf.model.dom.html.HTMLParserFactory;
import org.eclipse.actf.model.dom.html.IHTMLParser;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.util.dom.DomPrintUtil;
import org.eclipse.actf.util.logging.DebugPrintUtil;
import org.eclipse.actf.visualization.IVisualizationConst;
import org.eclipse.actf.visualization.blind.BlindVisualizerBase;
import org.eclipse.actf.visualization.blind.IBlindVisualizer;
import org.eclipse.actf.visualization.blind.ui.internal.Messages;
import org.eclipse.actf.visualization.engines.blind.BlindVizResourceUtil;
import org.eclipse.actf.visualization.engines.blind.ParamBlind;
import org.eclipse.actf.visualization.engines.blind.eval.EvaluationResultBlind;
import org.eclipse.actf.visualization.engines.blind.html.IVisualizeMapData;
import org.eclipse.actf.visualization.engines.blind.html.VisualizeEngine;
import org.eclipse.actf.visualization.engines.blind.html.util.HtmlErrorLogListener;
import org.eclipse.actf.visualization.eval.CheckTargetFactory;
import org.eclipse.actf.visualization.eval.EvaluationUtil;
import org.eclipse.actf.visualization.eval.IHtmlCheckTarget;
import org.eclipse.actf.visualization.eval.IHtmlChecker;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.eval.html.HtmlEvalUtil;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetNodeInfo;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.util.html2view.Html2ViewMapData;
import org.eclipse.actf.visualization.util.html2view.Html2ViewMapMaker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BlindVisualizerHtml extends BlindVisualizerBase implements
		IBlindVisualizer {

	public static final int FRAME = 1;

	private static final String ORIG_HTML_FILE = "origSource.html";

	private static final String IE_HTML_FILE = "ieSource.html";

	private static final String MAPPED_HTML_FILE_PRE = "MappedHTML";

	private static final String HTML_SOURCE_FILE = "source.html";

	private IWebBrowserACTF webBrowser;

	@Override
	public boolean setModelService(IModelService targetModel) {
		webBrowser = null;
		if (super.setModelService(targetModel)) {
			webBrowser = (IWebBrowserACTF) targetModel;
			return true;
		}
		return false;
	}

	public boolean isTarget(IModelService modelService) {
		if (null != modelService && modelService instanceof IWebBrowserACTF) {
			return true;
		}
		return false;
	}

	public int visualize() {
		GuidelineHolder.getInstance().setTargetMimeType("text/html");

		int frameId = 0;
		checkResult = new EvaluationResultBlind();

		try {

			webBrowser.saveOriginalDocument(tmpDirS + ORIG_HTML_FILE);
			webBrowser.saveDocumentAsHTMLFile(tmpDirS + IE_HTML_FILE);
			// for srcViewer
			webBrowser.saveOriginalDocument(tmpDirS + HTML_SOURCE_FILE);

			Vector<Html2ViewMapData> html2ViewMapV = new Vector<Html2ViewMapData>();
			IHTMLParser htmlParser = HTMLParserFactory.createHTMLParser();
			HtmlErrorLogListener errorLogListener = new HtmlErrorLogListener();
			htmlParser.addErrorLogListener(errorLogListener);
			String targetFile = tmpDirS + MAPPED_HTML_FILE_PRE + frameId
					+ ".html";

			boolean isIEhtml = false;
			if (EvaluationUtil.isOriginalDOM()) {
				html2ViewMapV = Html2ViewMapMaker.makeMap(ORIG_HTML_FILE,
						MAPPED_HTML_FILE_PRE + frameId + ".html", tmpDirS);
				// decode miss
				if (html2ViewMapV.size() == 0) {
					isIEhtml = true;
				}
			} else {
				isIEhtml = true;
			}

			// for line<>id mapping
			// HtmlLine2Id htmlLine2Id = new HtmlLine2Id(html2ViewMapV);

			Document document;
			Document ieDom;
			Document originalDocument;
			if (isIEhtml) {
				ieDom = webBrowser.getLiveDocument();

				// TODO replace with DomByCom (need clone/write support)
				IHTMLParser tmpHtmlParser = HTMLParserFactory
						.createHTMLParser();
				tmpHtmlParser
						.parse(new FileInputStream(tmpDirS + IE_HTML_FILE));
				document = tmpHtmlParser.getDocument();

				tmpHtmlParser.parse(new FileInputStream(tmpDirS
						+ ORIG_HTML_FILE));
				originalDocument = tmpHtmlParser.getDocument();

			} else {
				htmlParser.parse(new FileInputStream(targetFile));
				document = htmlParser.getDocument();
				originalDocument = document;
				ieDom = webBrowser.getLiveDocument();
			}
			// System.out.println(document+" "+ _originalDom+" "+ ieDom);

			boolean hasFrame = false;

			if (document == null) {
				return ERROR;
			} else if (hasFrameset(document, webBrowser) == true) {
				hasFrame = true;
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

			// TODO invisibleElements
			// HashSet invisibleIdSet = new HashSet();
			// if (webBrowser != null) {
			// String[] invisibleIds = webBrowser.getInvisibleEleId();
			// for (int i = 0; i < invisibleIds.length; i++) {
			// invisibleIdSet.add(invisibleIds[i]);
			// }
			// }
			// engine.setInvisibleIdSet(invisibleIdSet);

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

			boolean isDBCS = false;
			if (ParamBlind.getInstance().iLanguage == ParamBlind.JP) {
				isDBCS = true;
			}

			HtmlEvalUtil edu = new HtmlEvalUtil(document, resultDocument,
					targetUrl, mapData.getOrig2idMap(), originalDocument,
					ieDom, pageData, isDBCS, isIEhtml);

			ArrayList<IProblemItem> tmpResults = new ArrayList<IProblemItem>(
					1024);

			// TODO re-impl BrowserAndStyleInfo
			//
			// BrowserAndStyleInfo data =
			// webBrowser.getBrowserAndStyleInfo();
			IHtmlCheckTarget checkTarget = CheckTargetFactory
					.createHtmlCheckTarget(document, webBrowser.getURL(), null,
							edu);

			for (int i = 0; i < checkers.length; i++) {
				if (checkers[i] instanceof IHtmlChecker) {
					tmpResults.addAll(((IHtmlChecker) checkers[i])
							.checkHtml(checkTarget));
				} else if (checkers[i].isTargetFormat(webBrowser
						.getCurrentMIMEType())
						&& checkers[i].isEnabled()) {
					tmpResults.addAll(checkers[i].check(checkTarget));
				}
			}

			// TODO support blind biz -> visitor
			for (int i = 0; i < tmpResults.size(); i++) {
				IProblemItem tmpItem = (IProblemItem) tmpResults.get(i);
				HighlightTargetNodeInfo nodeInfo = tmpItem
						.getHighlightTargetNodeInfo();
				if (nodeInfo != null) {
					tmpItem.setHighlightTargetIds(nodeInfo
							.getHighlightTargetIds(mapData.getOrig2idMap()));
					if (EvaluationUtil.isOriginalDOM()) {
						tmpItem.setHighlightTargetSourceInfo(nodeInfo
								.getHighlightTargetSourceInfo(html2ViewMapV));
					}
				}
			}
			checkResult.addProblemItems(tmpResults);
			checkResult
					.addProblemItems(errorLogListener.getHtmlProblemVector());
			checkResult.accept(pageData);

			// TODO move (add Icons into result doc) here

			resultFile = BlindVizResourceUtil.createTempFile(
					IVisualizationConst.PREFIX_RESULT,
					IVisualizationConst.SUFFIX_HTML);
			// File tmpFile = BlindVizEnginePlugin.createTempFile("tmp",
			// IVisualizationConst.SUFFIX_HTML);

			try {
				// HtmlParserUtil.saveHtmlDocumentAsUTF8(
				// (SHDocument) resultDocument, tmpFile, resultFile);
				DomPrintUtil dpu = new DomPrintUtil(resultDocument);
				dpu.writeToFile(resultFile);

			} catch (Exception e3) {
				DebugPrintUtil
						.devOrDebugPrintln("error: saveHtmlDocumentAsUTF8");
			}

			if (hasFrame) {
				return FRAME;
			} else if (webBrowser != null && !webBrowser.isUrlExists()) {
				// TODO
				return ERROR;
			}
			return OK;

		} catch (Exception e) {
			setStatusMessage("Visualization Error");
			e.printStackTrace();
			return ERROR;
		}

	}

	private boolean hasFrameset(Document document, IWebBrowserACTF webBrowser) {

		NodeList framesetNl = document.getElementsByTagName("frameset");

		if (framesetNl.getLength() > 0) {

			NodeList frameList = document.getElementsByTagName("frame");

			String sFileName = BlindVizResourceUtil.getTempDirectory()
					+ "frameList.html";

			String base = webBrowser.getURL();

			try {
				URL baseURL = new URL(base);

				NodeList baseNL = document.getElementsByTagName("base");
				if (baseNL.getLength() > 0) {
					Element baseE = (Element) baseNL
							.item(baseNL.getLength() - 1);
					String baseUrlS = baseE.getAttribute("href");
					if (baseUrlS.length() > 0) {
						URL tmpUrl = new URL(baseURL, baseUrlS);
						base = tmpUrl.toString();
					}
				}
			} catch (Exception e) {
			}

			PrintWriter fileOutput;

			try {
				fileOutput = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(sFileName), "UTF-8"));
			} catch (IOException e) {
				// e.printStackTrace();
				// TODO
				return true;
			}

			fileOutput.write("<html>");
			// " lang=\""+lang+\">"); //use var
			fileOutput.write("<head>" + FileUtils.LINE_SEP);
			fileOutput
					.write("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" >"
							+ FileUtils.LINE_SEP);
			fileOutput.write("<base href=\"" + base + "\"></head>"
					+ FileUtils.LINE_SEP + "<body><P>");
			fileOutput.write("This page contains of "); // var
			fileOutput.write(String.valueOf(frameList.getLength()));
			fileOutput.write(" frames."); // var
			fileOutput.write("<br>" + FileUtils.LINE_SEP);
			fileOutput.write("Please select one of them."); // var
			fileOutput.write("</P>" + FileUtils.LINE_SEP + "<ol>"
					+ FileUtils.LINE_SEP);

			String strTitle, strName;
			for (int i = 0; i < frameList.getLength(); i++) {
				Element frameEl = (Element) frameList.item(i);
				strTitle = frameEl.getAttribute("title");
				strName = frameEl.getAttribute("name");
				if (strTitle.equals(""))
					strTitle.equals("none");
				if (strName.equals(""))
					strName.equals("none");
				fileOutput.write("<li><a href=\"" + frameEl.getAttribute("src")
						+ "\">Title: \"" + strTitle + "\".<BR> Name: \""
						+ strName + "\".<BR> src: \""
						+ frameEl.getAttribute("src") + "\".</a>"
						+ FileUtils.LINE_SEP);
			}
			fileOutput.write("</ol></body></html>");

			fileOutput.flush();
			fileOutput.close();

			webBrowser.navigate(sFileName);
			return true;

		} else {
			return false;
		}
	}

}
