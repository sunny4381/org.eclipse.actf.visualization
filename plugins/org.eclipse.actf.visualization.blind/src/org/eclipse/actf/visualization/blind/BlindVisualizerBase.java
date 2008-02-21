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
package org.eclipse.actf.visualization.blind;

import java.io.File;

import org.eclipse.actf.model.IModelService;
import org.eclipse.actf.visualization.IVisualizationView;
import org.eclipse.actf.visualization.blind.ui.internal.Messages;
import org.eclipse.actf.visualization.engines.blind.BlindVizEnginePlugin;
import org.eclipse.actf.visualization.engines.blind.eval.EvaluationResultBlind;
import org.eclipse.actf.visualization.eval.IChecker;
import org.eclipse.actf.visualization.eval.IEvaluationResult;
import org.eclipse.actf.visualization.eval.extensions.CheckerExtension;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.w3c.dom.Document;

public abstract class BlindVisualizerBase implements IBlindVisualizer{

	protected IChecker[] checkers = CheckerExtension.getCheckers();
	protected String tmpDirS = BlindVizEnginePlugin.getTempDirectoryString();
	protected String targetUrl = "";
	protected IModelService modelService;

	//for reuslt
	protected IEvaluationResult checkResult = new EvaluationResultBlind();
	protected Document resultDocument;
	protected PageData pageData;
	protected int maxReachingTime = 0;
	protected File resultFile;

	private IVisualizationView vizView;
	
	protected abstract boolean isTarget(IModelService modelService);
	
	public boolean setModelService(IModelService targetModel){
		modelService = null;
		targetUrl="";
		if (!isTarget(targetModel)) {
			return false;
		}
		
		modelService = targetModel;
		targetUrl = targetModel.getURL();
		return true;
	}

	public void setVisualizationView(IVisualizationView targetView){
		vizView = targetView;
	}
	
	public String createMaxTimeString() {
		return Messages.formatResourceString("BlindView.Maximum_Time",
				maxReachingTime);
	}

	public IEvaluationResult getCheckResult() {
		return checkResult;
	}

	public PageData getPageData() {
		return pageData;
	}

	public Document getResultDocument() {
		return resultDocument;
	}

	public File getResultFile() {
		return resultFile;
	}
	
	protected void setStatusMessage(String message){
		if(null!=vizView){
			vizView.setStatusMessage(message);
		}
	}

	protected void setInfoMessage(String message){
		if(null!=vizView){
			vizView.setInfoMessage(message);
		}
	}

}