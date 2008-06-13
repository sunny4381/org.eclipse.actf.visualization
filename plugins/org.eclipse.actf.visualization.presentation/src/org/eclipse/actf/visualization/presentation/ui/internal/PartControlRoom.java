/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daisuke SATO - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.presentation.ui.internal;

import java.io.File;
import java.util.List;

import org.eclipse.actf.mediator.Mediator;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.ModelServiceImageCreator;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.actf.visualization.IVisualizationConst;
import org.eclipse.actf.visualization.engines.lowvision.image.PageImage;
import org.eclipse.actf.visualization.engines.lowvision.io.ImageDumpUtil;
import org.eclipse.actf.visualization.presentation.RoomPlugin;
import org.eclipse.actf.visualization.presentation.eval.CheckResultPresentation;
import org.eclipse.actf.visualization.presentation.util.ParamRoom;
import org.eclipse.actf.visualization.presentation.util.SimulateRoom;
import org.eclipse.actf.visualization.ui.IPositionSize;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class PartControlRoom implements IVisualizationConst {

	private static PartControlRoom INSTANCE = null;

	private static final CheckResultPresentation dummyResult = new CheckResultPresentation();

	private PageImage targetPageImage;

	private RoomView roomView;

	private ParamRoom paramRoom;

	private Shell _shell;

	private boolean _isInSimulate;

	private CheckResultPresentation checkResult;

	private File vizResultFile;

	private String dumpImageFile;

	private IVisualizationView checker;

	private Mediator mediator = Mediator.getInstance();

	public static PartControlRoom getDefaultInstance() {
		return INSTANCE;
	}

	public PartControlRoom(IVisualizationView checker, Composite parent) {
		INSTANCE = this;

		this.checker = checker;
		this._shell = parent.getShell();

		paramRoom = new ParamRoom();

		roomView = new RoomView(parent);

		_isInSimulate = false;

		try {
			File dumpImgFile = RoomPlugin.createTempFile(PREFIX_SCREENSHOT,
					SUFFIX_BMP);
			dumpImageFile = dumpImgFile.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void doSimulate() {
		// TODO enable,disable button
		if (_isInSimulate) {
			return;
		}

		this._isInSimulate = true;
		this._shell.setCursor(new Cursor(_shell.getDisplay(), SWT.CURSOR_WAIT));

		mediator.setEvaluationResult(checker, dummyResult);
		checkResult = new CheckResultPresentation();

		roomView.clearImage();
		_shell.getDisplay().update();

		IModelService modelService = ModelServiceUtils.getActiveModelService();
		if (modelService == null) {
			return;
		}
		prepareInt2Ds(modelService);

	}

	private void prepareInt2Ds(IModelService modelService) {
		try {
			checker
					.setStatusMessage(RoomPlugin
							.getResourceString("PartRightRoom.dump_the_image_in_the_web_browser._26"));

			modelService.setScrollbarWidth(roomView.getVarticalBarSize().x);
			ModelServiceImageCreator imgCreator = new ModelServiceImageCreator(
					modelService);
			imgCreator.getScreenImageAsBMP(dumpImageFile, false);

			targetPageImage = ImageDumpUtil.createPageImage(dumpImageFile,
					_shell);

			if (targetPageImage != null) {

				checker
						.setStatusMessage(RoomPlugin
								.getResourceString("PartRightRoom.begin_to_make_PageImage._2"));

				doSimulateAfterHalf(modelService);
			} else {
				_shell.setCursor(null);
				_isInSimulate = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doSimulateAfterHalf(IModelService modelService) {
		// TODO
		PageImage pageImageWhole = targetPageImage;

		checker
				.setStatusMessage(RoomPlugin
						.getResourceString("PartRightRoom.prepare_Simulation_Image._29"));

		paramRoom.setDisplayResolution(modelService.getTargetComposite()
				.getSize().y);

		try {
			vizResultFile = RoomPlugin.createTempFile(PREFIX_VISUALIZATION,
					SUFFIX_BMP);
			ImageData[] imageDataArray = SimulateRoom.doSimulate(
					pageImageWhole, paramRoom, vizResultFile.getAbsolutePath());
			if (imageDataArray.length > 0) {
				roomView.displayImage(imageDataArray[0], modelService);
			}
			imageDataArray = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		String html = "";
		switch (paramRoom.getType()) {
		case ParamRoom.ROOM_SMALL:
			html = RoomPlugin
					.getResourceString("adesigner.room.report.small.url");
			break;
		case ParamRoom.ROOM_MIDDLE:
			html = RoomPlugin
					.getResourceString("adesigner.room.report.middle.url");
			break;
		case ParamRoom.ROOM_LARGE:
			html = RoomPlugin
					.getResourceString("adesigner.room.report.large.url");
			break;
		}

		checkResult.setSummaryReportUrl(RoomPlugin.getDirectory("html") + html);
		// TODO
		checkResult.setSummaryReportText("");
		Mediator.getInstance().setEvaluationResult(checker, checkResult);

		checker
				.setStatusMessage(RoomPlugin
						.getResourceString("PartRightRoom.simulation_of_current_page_is_over._8"));
		// //$NON-NLS-1$
		_shell.setCursor(null);
		_isInSimulate = false;

	}

	public void setHighlightPositions(List<IPositionSize> infoPositionSizeList) {
		roomView.highlight(infoPositionSizeList);
	}

	/**
	 * @return
	 */
	public ParamRoom getParamLowVision() {
		return paramRoom;
	}

	/**
	 * @param vision
	 */
	public void setParamLowVision(ParamRoom vision) {
		paramRoom = vision;
	}

	public void setLVParamStatus() {
		if (!_isInSimulate) {
			checker.setInfoMessage(paramRoom.toString());
		}
	}

	public void setCurrentModelService(IModelService modelService) {
		roomView.setCurrentModelService(modelService);
	}

}
