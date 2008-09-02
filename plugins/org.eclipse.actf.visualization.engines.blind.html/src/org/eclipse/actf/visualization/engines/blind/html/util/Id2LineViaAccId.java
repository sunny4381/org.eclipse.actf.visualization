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

package org.eclipse.actf.visualization.engines.blind.html.util;

import java.util.Map;
import java.util.Vector;

import org.eclipse.actf.util.html2view.Html2ViewMapData;

public class Id2LineViaAccId {
	private Map<Integer, Integer> id2AccId;

	private Vector<Html2ViewMapData> html2ViewMapDataV;

	// TODO
	private boolean is1base = true;

	public Id2LineViaAccId(Map<Integer, Integer> id2AccId,
			Vector<Html2ViewMapData> html2ViewMapDataV) {
		this(id2AccId, html2ViewMapDataV, true);
	}

	public Id2LineViaAccId(Map<Integer, Integer> id2AccId,
			Vector<Html2ViewMapData> html2ViewMapDataV, boolean is1base) {
		this.id2AccId = id2AccId;
		this.html2ViewMapDataV = html2ViewMapDataV;
		this.is1base = is1base;
	}

	public int getLine(int nodeId) {
		int result = -1;
		Integer id = new Integer(nodeId);

		if (id2AccId.containsKey(id)) {
			int accId = id2AccId.get(id).intValue();
			if (accId > -1 && accId < html2ViewMapDataV.size()) {
				Html2ViewMapData tmpData = html2ViewMapDataV.get(accId);
				result = tmpData.getStartLine();// ? +1 ?
			}
		}

		return (result);
	}

	public Html2ViewMapData getViewMapData(int nodeId) {
		return (getViewMapData(new Integer(nodeId)));
	}

	public Html2ViewMapData getViewMapData(Integer nodeId) {
		Html2ViewMapData result = null;
		if (id2AccId.containsKey(nodeId)) {
			int accId = id2AccId.get(nodeId).intValue();
			if (accId > -1 && accId < html2ViewMapDataV.size()) {
				result = html2ViewMapDataV.get(accId);
			}
		}
		return (result);
	}

}
