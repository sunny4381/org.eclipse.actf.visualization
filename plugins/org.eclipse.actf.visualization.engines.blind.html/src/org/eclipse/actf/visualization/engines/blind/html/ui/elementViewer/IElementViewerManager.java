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
package org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer;

/**
 * IElementViewerManager is used to control element information viewer
 */
public interface IElementViewerManager {

	/**
	 * Set {@link IHighlightElementListener} to highlight corresponding position
	 * of selected item in visualization result
	 * 
	 * @param hel
	 *            target {@link IHighlightElementListener}
	 */
	public abstract void setHighlightElementListener(
			IHighlightElementListener hel);

	/**
	 * Open element information viewer.
	 */
	public abstract void openElementViewer();

	/**
	 * Activate element information viewer if exists.
	 */
	public abstract void activateElementViewer();

	/**
	 * Hide element information viewer if exists.
	 */
	public abstract void hideElementViewer();

}