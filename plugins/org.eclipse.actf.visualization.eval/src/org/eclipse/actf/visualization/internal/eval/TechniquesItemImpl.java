/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.eval;

import org.eclipse.actf.visualization.eval.ITechniquesItem;

public class TechniquesItemImpl implements ITechniquesItem {

	private String guideline = "";
	private String id = "";
	private String url = "";
	
	public String getGuidelineName() {
		return guideline;
	}

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public void setGuidelineName(String guideline){
		this.guideline = guideline;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setUrl(String url) {
		this.url = url;
	}


}
