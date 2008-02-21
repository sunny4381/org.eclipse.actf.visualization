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
package org.eclipse.actf.visualization.engines.blind.html;

import java.util.Map;

import org.eclipse.actf.visualization.engines.blind.html.internal.util.VisualizationNodeInfo;
import org.w3c.dom.Node;

public interface IVisualizeMapData {

	/**
	 * @return Returns the orig2idMap.
	 */
	public abstract Map getOrig2idMap();

	public abstract void addReplacedNodeMapping(Node result, Node replacement);

	public abstract Node getOrigNode(Node result);

	public abstract Node getResultNode(Node orig);

	public abstract Node getReplacement(Node result);

	public abstract Integer getIdOfNode(Node result);

	public abstract Integer getIdOfOrigNode(Node orig);

	public abstract VisualizationNodeInfo getNodeInfo(Node result);

}