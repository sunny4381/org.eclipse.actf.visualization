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
import org.eclipse.actf.visualization.eval.IEvaluationResult;
import org.eclipse.actf.visualization.eval.html.statistics.PageData;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.w3c.dom.Document;

public interface IBlindVisualizer {

	public static final int ERROR = -1;

	public static final int OK = 0;
	
	public static final String MAPPED_HTML_FILE_PRE = "MappedHTML";
	
	public static final String HTML_SOURCE_FILE = "source.html";

	public abstract int visualize();

	public abstract String createMaxTimeString();

	public abstract IEvaluationResult getCheckResult();

	public abstract PageData getPageData();

	public abstract Document getResultDocument();

	public abstract File getResultFile();		

	public abstract boolean setModelService(IModelService modelService);
	
	public abstract void setVisualizationView(IVisualizationView targetView);
}