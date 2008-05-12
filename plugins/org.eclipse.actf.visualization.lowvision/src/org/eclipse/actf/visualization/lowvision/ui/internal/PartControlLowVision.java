/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	  Junji MAEDA - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.lowvision.ui.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.actf.mediator.IMediatorEventListener;
import org.eclipse.actf.mediator.Mediator;
import org.eclipse.actf.mediator.MediatorEvent;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.ImagePositionInfo;
import org.eclipse.actf.model.ui.ModelServiceImageCreator;
import org.eclipse.actf.model.ui.editor.browser.CurrentStyles;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.actf.visualization.IVisualizationConst;
import org.eclipse.actf.visualization.engines.lowvision.TargetPage;
import org.eclipse.actf.visualization.engines.lowvision.image.PageImage;
import org.eclipse.actf.visualization.engines.lowvision.io.ImageDumpUtil;
import org.eclipse.actf.visualization.eval.problem.IPositionSize;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.lowvision.LowVisionVizPlugin;
import org.eclipse.actf.visualization.lowvision.eval.CheckResultLowVision;
import org.eclipse.actf.visualization.lowvision.eval.SaveReportLowVision;
import org.eclipse.actf.visualization.lowvision.eval.SummaryEvaluationLV;
import org.eclipse.actf.visualization.lowvision.util.LowVisionUtil;
import org.eclipse.actf.visualization.lowvision.util.ParamLowVision;
import org.eclipse.actf.visualization.lowvision.util.SimulateLowVision;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.w3c.dom.Document;


public class PartControlLowVision implements ISelectionListener,
		IVisualizationConst, IMediatorEventListener {

	private static final CheckResultLowVision dummyResult = new CheckResultLowVision();
	
	private String[] frameUrl;

	private PageImage[] framePageImage;

	private ImagePositionInfo[][] imageInfoInHtmlArray;

	private ArrayList<HashMap<String, CurrentStyles>> styleInfoArray;

	private Vector<ExtractCheckThread> checkThreads;

	private LowVisionSimulationView lowVisionView;

	private ParamLowVision paramLowVision;

	private Shell _shell;

	private String targetUrlS;

	private boolean[] isFinished;

	private SaveReportLowVision _saveReportLowVision;

	private boolean _isInSimulate;

	private boolean is1stSimulateDone = false;

	// 256M -> 7000000 400M->10000000
	private int dump_image_size = 7000000;

	private CheckResultLowVision checkResult = new CheckResultLowVision();

	private File reportFile;

	private File reportImageFile;

	private File visResultFile;

	private String dumpImageFile;

	private IVisualizationView checker;

	private Mediator mediator = Mediator.getInstance();

	private IWebBrowserACTF webBrowser = null;

	private class ExtractCheckThread extends Thread {
		int frameId;

		String address;

		TargetPage targetPage;

		private List<IProblemItem> lowvisionProblemList;

		ExtractCheckThread(int _frameId, String _address) {
			frameId = _frameId;
			address = _address;
		}

		public void run() {
			try {
				targetPage = new TargetPage();
				targetPage.setPageImage(framePageImage[frameId]);
				targetPage
						.setInteriorImagePosition(imageInfoInHtmlArray[frameId]);
				targetPage.setCurrentStyles(styleInfoArray.get(frameId));

				_shell.getDisplay().syncExec(new Runnable() {
					public void run() {
						checker
								.setStatusMessage(Messages
										.getString("LowVisionView.begin_to_check_problems._4"));
					}
				});

				lowvisionProblemList = targetPage.check(paramLowVision
						.getLowVisionType(), address, frameId);

				// TODO frames
				try {
					reportFile = LowVisionVizPlugin.createTempFile(
							PREFIX_REPORT, SUFFIX_HTML);
					// TODO modelservice type
					if (webBrowser != null) {
						reportImageFile = LowVisionVizPlugin.createTempFile(
								PREFIX_REPORT, SUFFIX_BMP);
						targetPage
								.makeAndStoreReport(reportFile.getParent(),
										reportFile.getName(), reportImageFile
												.getName(),
										lowvisionProblemList);
					} else {// current lv mode doesn't support ODF
						reportImageFile = null;
						targetPage.unsupportedModeReport(reportFile);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				targetPage = null;

				checkResult.addProblemItems(lowvisionProblemList);

				// ext checker here

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void disposeTargetPage() {
			if (targetPage != null) {
				targetPage.disposePageImage();
			}
		}

	}

	// TODO use event
	private class WaitExtractThread extends Thread {
		Thread simulateThread;

		WaitExtractThread(Thread _simulateThread) {
			simulateThread = _simulateThread;
		}

		public void run() {
			if (simulateThread == null && checkThreads == null)
				return;
			boolean flag = true;
			int count = 0;

			while (flag) {
				if (simulateThread == null || !simulateThread.isAlive()) {
					flag = false;
					simulateThread = null;
					for (int i = 0; i < checkThreads.size(); i++) {
						flag = flag || checkThreads.get(i).isAlive();
					}
				}
				if (count >= 200) {
					// //$NON-NLS-1$
					break;
				}
				count++;
				try {
					WaitExtractThread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			_shell.getDisplay().syncExec(new Runnable() {

				public void run() {

					if (webBrowser != null) {
						checkResult
								.setSummaryReportText(new SummaryEvaluationLV(
										checkResult.getProblemList())
										.getOverview());
					} else {
						checkResult.setSummaryReportText(SummaryEvaluationLV
								.notSupported());
					}
					checkResult.setLineStyleListener(SummaryEvaluationLV
							.getHighLightStringListener());

					checkResult.setSummaryReportUrl(reportFile
							.getAbsolutePath());
					mediator.setEvaluationResult(checker, checkResult);

					checker
							.setStatusMessage(Messages
									.getString("LowVisionView.simulation_of_current_page_is_over._8"));
					_shell.setCursor(null);
					_isInSimulate = false;

					for (ExtractCheckThread tmpT : checkThreads){
						tmpT.disposeTargetPage();
					}
					checkThreads = new Vector<ExtractCheckThread>();
				}
			});
		}
	}

	public PartControlLowVision(IVisualizationView checker, Composite parent) {

		this.checker = checker;

		this._shell = parent.getShell();

		paramLowVision = ParamLowVision.getDefaultInstance();
		this._saveReportLowVision = new SaveReportLowVision(_shell);

		lowVisionView = new LowVisionSimulationView(parent, this);

		_isInSimulate = false;

		try {
			File dumpImgFile = LowVisionVizPlugin.createTempFile(
					PREFIX_SCREENSHOT, SUFFIX_BMP);
			dumpImageFile = dumpImgFile.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Mediator.getInstance().addMediatorEventListener(this);

	}

	public void saveReport() {
		IModelService modelService = ModelServiceUtils.getActiveModelService();
		if (is1stSimulateDone && !isBInSimulate() && modelService != null) {
			this._saveReportLowVision.doSave(modelService.getURL(), checkResult
					.getProblemList(), visResultFile, reportImageFile);
		}
	}

	public void saveReport(int processNo, int[] paramNo, String paramStr,
			String folder, String probName, Document probStatisticsDoc,
			String urlS, int depth) {

		_saveReportLowVision.doSave(processNo, paramNo, paramStr, folder,
				probStatisticsDoc, urlS, checkResult.getProblemList(),
				visResultFile, reportImageFile);
	}

	private void allocate(int frameSize) {
		framePageImage = new PageImage[frameSize];
		imageInfoInHtmlArray = new ImagePositionInfo[frameSize][];
		styleInfoArray = new ArrayList<HashMap<String,CurrentStyles>>(frameSize);
		for(int i=0; i<frameSize; i++){
			styleInfoArray.add(new HashMap<String, CurrentStyles>());
		}
		// htmlLine2Id = new HtmlLine2Id[frameSize];
		// nodeId2Position = new HashMap[frameSize];
		isFinished = new boolean[frameSize];
		for (int i = 0; i < frameSize; i++) {
			isFinished[i] = false;
		}
	}

	public void doSimulate() {
		is1stSimulateDone = true;
		// TODO button: enable,disable
		if (isBInSimulate()) {
			return;
		}

		this._isInSimulate = true;
		this._shell.setCursor(new Cursor(_shell.getDisplay(), SWT.CURSOR_WAIT));

		Mediator.getInstance().setEvaluationResult(checker, dummyResult);
		checkResult = new CheckResultLowVision();

		lowVisionView.clearImage();
		_shell.getDisplay().update();

		checkThreads = new Vector<ExtractCheckThread>();

		IModelService modelService = ModelServiceUtils.getActiveModelService();
		if (modelService == null) {
			this._shell.setCursor(new Cursor(_shell.getDisplay(),
					SWT.CURSOR_ARROW));
			this._isInSimulate = false;
			return;
		}

		lowVisionView.setTarget(modelService);

		webBrowser = null;
		if (modelService instanceof IWebBrowserACTF) {
			webBrowser = (IWebBrowserACTF) modelService;
		}

		frameUrl = new String[0];
		int frameSize = 0;
		if (webBrowser != null) {
			if (lowVisionView.isWholepage()) {
				frameUrl = LowVisionUtil.frameAnalyze(webBrowser);
			}

			frameSize = frameUrl.length;
			if (frameSize == 0) {
				allocate(1);
			} else {
				allocate(frameSize);
				int tmpSize = dump_image_size / frameSize
						- (dump_image_size / 10) * (frameSize - 1);
				if (tmpSize < dump_image_size / 10) {
					tmpSize = dump_image_size / 10;
				}
			}
		} else {
			allocate(1);
		}

		targetUrlS = modelService.getURL();

		if (webBrowser != null) {
			try {
				File sourceHtmlFile = LowVisionVizPlugin.createTempFile(
						PREFIX_SOURCE_HTML, SUFFIX_HTML);
				webBrowser.saveOriginalDocument(sourceHtmlFile
						.getAbsolutePath());
				checkResult.setSourceFile(sourceHtmlFile);
			} catch (Exception e) {
			}

		}

		if (frameSize == 0) {
			frameUrl = new String[1];
			frameUrl[0] = targetUrlS;
			prepareInt2Ds(modelService, 0, 0);
		} else {
			if (webBrowser != null) {
				// TODO replace with WaitForBrowserReadyHandler
				// webBrowser.syncNavigate(frameUrl[0]);
				// new WaitSyncNavigateThread(0, frameSize - 1).start();
			}
		}

	}

	private void prepareInt2Ds(IModelService modelService, int frameId,
			int lastFrame) {
		try {
			checker
					.setStatusMessage(Messages
							.getString("LowVisionView.dump_the_image_in_the_web_browser._26"));

			modelService
					.setScrollbarWidth(lowVisionView.getVarticalBarSize().x);
			ModelServiceImageCreator imgCreator = new ModelServiceImageCreator(
					modelService);
			imgCreator.getScreenImageAsBMP(dumpImageFile, lowVisionView
					.isWholepage());

			framePageImage[frameId] =
			// partLeftWebBrowser.dumpWebBrowserImg(
			ImageDumpUtil.createPageImage(dumpImageFile, _shell);
			// System.out.println("finish dump");

			IWebBrowserACTF browser = null;
			if (modelService instanceof IWebBrowserACTF) {
				browser = (IWebBrowserACTF) modelService;
			}

			if (framePageImage[frameId] != null) {
				checker
						.setStatusMessage(Messages
								.getString("LowVisionView.get_information_of_all_images._25"));
				if (browser != null) {
					imageInfoInHtmlArray[frameId] = browser
							.getAllImagePosition();
					// styleInfoArray.set(frameId, browser.getNodeStyles());//TODO recover getNodeStyles function
					styleInfoArray.set(frameId, new HashMap<String, CurrentStyles>());
				} else {
					styleInfoArray.set(frameId, new HashMap<String, CurrentStyles>());
				}

				if (lastFrame > 1) { // TODO frameURL.length?
					imageInfoInHtmlArray[frameId] = LowVisionUtil
							.trimInfoImageInHtml(imageInfoInHtmlArray[frameId],
									framePageImage[frameId].getHeight());
					styleInfoArray.set(frameId, LowVisionUtil.trimStyleInfoArray(
							styleInfoArray.get(frameId), framePageImage[frameId]
									.getHeight()));
				}

				checker.setStatusMessage(Messages
						.getString("LowVisionView.begin_to_make_PageImage._2"));

				ExtractCheckThread checkThread = new ExtractCheckThread(
						frameId, frameUrl[frameId]);
				checkThread.start();

				checkThreads.add(checkThread);

				isFinished[frameId] = true;
				if (browser != null) {
					if (frameId == lastFrame) {

						if (lastFrame > 0) {
							// browser.syncNavigate(targetUrlS);
							browser.navigate(targetUrlS);
						}
						doSimulateAfterHalf();
					} else {
						frameId++;
						// TODO replace with WaitForBrowserReadyHandler
						// browser.syncNavigate(frameUrl[frameId]);
						// new WaitSyncNavigateThread(frameId,
						// lastFrame).start();
					}
				} else {
					doSimulateAfterHalf();
				}
			} else {
				_shell.setCursor(null);
				_isInSimulate = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doSimulateAfterHalf() {
		PageImage pageImageWhole;
		if (framePageImage.length > 1) {
			File mergedImageFile;
			try {
				mergedImageFile = LowVisionVizPlugin.createTempFile(
						PREFIX_MERGED_IMAGE, SUFFIX_BMP);
				pageImageWhole = ImageDumpUtil.joinPageImages(mergedImageFile
						.getAbsolutePath(), //$NON-NLS-1$
						framePageImage);
				if (mergedImageFile != null) {
					mergedImageFile.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
				pageImageWhole = framePageImage[0];
			}
		} else {
			pageImageWhole = framePageImage[0];
		}

		checker.setStatusMessage(Messages
				.getString("LowVisionView.prepare_Simulation_Image._29"));

		try {
			visResultFile = LowVisionVizPlugin.createTempFile(
					PREFIX_VISUALIZATION, SUFFIX_BMP);
			ImageData[] imageDataArray = SimulateLowVision.doSimulate(
					pageImageWhole, paramLowVision, visResultFile
							.getAbsolutePath());
			if (imageDataArray.length > 0) {
				lowVisionView.displayImage(imageDataArray[0]);
				lowVisionView.resetScrollBars();
				imageDataArray = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		checkResult.setFrameOffsetToProblems(framePageImage);

		new WaitExtractThread(null).start();
	}

	public void setHighlightPositions(List<IPositionSize> infoPositionSizeList) {
		lowVisionView.highlight(infoPositionSizeList);
	}

	/**
	 * @param vision
	 */
	public void setParamLowVision(ParamLowVision vision) {
		paramLowVision = vision;
	}

	public boolean isChildThread() {
		// TODO for HPB integration
		// return (aDesigner.isChildThread());
		return false;
	}

	public void setWholePage(boolean isWhole) {
		lowVisionView.setWholePage(isWhole);
	}

	public void setLVParamStatus() {
		if (!isBInSimulate()) {
			checker.setInfoMessage(paramLowVision.toString());
		}
	}

	public void simulateForMoreParameter() {
		_isInSimulate = true;

		checkResult = new CheckResultLowVision();
		// ADesignerMediator.getInstance().setCheckResult(checker, checkResult);

		lowVisionView.clearImage();
		checkThreads = new Vector<ExtractCheckThread>();

		IModelService modelService = ModelServiceUtils.getActiveModelService();
		// TODO null check?

		if (frameUrl.length == 0) {
			checker.setStatusMessage(Messages
					.getString("LowVisionView.begin_to_make_PageImage._2"));
			// TODO check(original is getAddressText())
			ExtractCheckThread checkThread = new ExtractCheckThread(0,
					modelService.getURL());

			checkThread.start();
			checkThreads.add(checkThread);
		} else {
			for (int i = 0; i < frameUrl.length; i++) {
				ExtractCheckThread checkThread = new ExtractCheckThread(i,
						frameUrl[i]);
				checkThread.start();
				checkThreads.add(checkThread);
			}
		}
		doSimulateAfterHalf();
	}

	/**
	 * @return
	 */
	public boolean isBInSimulate() {
		return _isInSimulate;
	}

	public boolean isInSaveReport() {
		return _saveReportLowVision.isInSaveReport();
	}

	public int getReportColorNum() {
		return _saveReportLowVision.getNumColorProblem();
	}

	public int getReportFontNum() {
		return _saveReportLowVision.getNumFontProblem();
	}

	/**
	 * @param dump_image_size
	 *            The dump_image_size to set.
	 */
	public void setDump_image_size(int dump_image_size) {
		this.dump_image_size = dump_image_size;
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection == null || !(selection instanceof IStructuredSelection)) {
			System.err.println(this.getClass().getName() + ":" + "Iselection");
			return;
		}
		ArrayList<IPositionSize> result = new ArrayList<IPositionSize>();

		//TODO check
		for (Iterator i = ((IStructuredSelection) selection).iterator(); i
				.hasNext();) {
			IProblemItem item = (IProblemItem) i.next();
			if (checkResult.getProblemList().contains(item)) {
				IPositionSize ips = (IPositionSize) item;
				result.add(ips);
			}
		}
		setHighlightPositions(result);
	}

	public void modelserviceChanged(MediatorEvent event) {
		lowVisionView.setTarget(event.getModelServiceHolder().getModelService());
	}

	public void modelserviceInputChanged(MediatorEvent event) {
		lowVisionView.setTarget(event.getModelServiceHolder().getModelService());		
	}

	public void reportChanged(MediatorEvent event) {
		
	}

	public void reportGeneratorChanged(MediatorEvent event) {
		
	}

}
