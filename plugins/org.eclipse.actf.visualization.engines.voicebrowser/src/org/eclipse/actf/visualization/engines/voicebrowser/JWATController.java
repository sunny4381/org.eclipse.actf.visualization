/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Masahide WASHIZAWA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.voicebrowser;

import org.eclipse.actf.ai.tts.ITTSEngine;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public interface JWATController {
	static final int DO_PREV_LINE = 1,
		DO_CURR_LINE = 2,
		DO_NEXT_LINE = 3,
		DO_PREV_LINK = 4,
		DO_CURR_LINK = 5,
		DO_NEXT_LINK = 6,
		DO_PREV_10LINE = 7,
		DO_NEXT_10LINE = 8,
		DO_CURR_ELEMENT = 9,
		DO_TOP_OF_PAGE = 10,
		DO_BOTTOM_OF_PAGE = 11,
		DO_PLAY = 12,
		DO_STOP = 13,
		DO_LINK_JUMP = 14;

	static final int HPR_MODE = 1, JAWS_MODE = 2;

	/**
	 * Method setJwatMode.
	 * @param mode
	 */
	void setJwatMode(int mode);

	/**
	 * Method setJwatMode.
	 * @param mode
	 * @param xmlpath
	 */
	void setJwatMode(int mode, String xmlpath);

	/**
	 * Method setNode.
	 * @param node
	 */
	void setNode(Node node);

	/**
	 * Method setDocument.
	 * @param document
	 */
	void setDocument(Document document);

	/**
	 * Method setNode.
	 * @param node
	 */
	void setNode(Node node, String uri, String anchorName);

	/**
	 * Method setDocument.
	 * @param document
	 */
	void setDocument(Document document, String uri, String anchorName);

	/**
	 * Method doCommandJWAT.
	 * @param type
	 * @return String
	 */
	String doCommandJWAT(int type);

	/**
	 * Method getPacketCollection.
	 * @return PacketCollection
	 */
	PacketCollection getPacketCollection();

	/**
	 * Method addView.
	 * @param view
	 */
	void addView(View view);

	/**
	 * Method removeView.
	 * @param view
	 */
	void removeView(View view);

	/**
	 * Method addSelectionObserver.
	 * @param view
	 */

	void addSelectionObserver(SelectionObserver view);

	/**
	 * Method removeSelectionObserver.
	 * @param view
	 */
	void removeSelectionObserver(SelectionObserver view);


	/**
	 * Method addCursorListener.
	 * @param listener
	 */
    public void addCursorListener(CursorListener listener);


	/**
	 * Method removeActionListener.
	 * @param listener
	 */
    public void removeCursorListener(CursorListener listener);


	/**
	 * Method setNode.
	 * @param node
	 */
	void setSpeechControl(ITTSEngine ttsEngine);

}
