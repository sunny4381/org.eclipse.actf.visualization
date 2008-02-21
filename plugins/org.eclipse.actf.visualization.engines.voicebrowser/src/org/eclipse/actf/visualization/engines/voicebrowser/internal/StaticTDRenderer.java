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
package org.eclipse.actf.visualization.engines.voicebrowser.internal;

import org.eclipse.actf.visualization.engines.voicebrowser.Context;
import org.eclipse.actf.visualization.engines.voicebrowser.Packet;
import org.eclipse.actf.visualization.engines.voicebrowser.PacketCollection;
import org.w3c.dom.Element;

public class StaticTDRenderer implements IElementRenderer {

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.internal.IElementRenderer#getPacketCollectionIn(Element, Context)
	 */
	public PacketCollection getPacketCollectionIn(
		Element element,
		Context curContext,
		String url,
		MessageCollection mc) {
		setContextIn(element, curContext);

		String result = null;
		result = OutLoud.buildResultString(mc, url, element, null, null);
		if (result != null) {
			result = result.trim();
			return new PacketCollection(
				new Packet(element, result, curContext, true));
		} else
			return null;
	}

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.internal.IElementRenderer#getPacketCollectionOut(Element, Context)
	 */
	public PacketCollection getPacketCollectionOut(
		Element element,
		Context curContext,
		String url,
		MessageCollection mc) {

		if (curContext.isLineDelimiter()) {
			return null;
		} else {
			setContextOut(element, curContext);
			Packet newPacket = new Packet(element, "", curContext, false);
			return new PacketCollection(newPacket);
		}
	}

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.internal.IElementRenderer#setContextIn(Context)
	 */
	public void setContextIn(Element element, Context curContext) {
		curContext.setGoChild(true);
		curContext.setLineDelimiter(false);
		curContext.setLinkTag(false);
	}

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.internal.IElementRenderer#setContextOut(Context)
	 */
	public void setContextOut(Element element, Context curContext) {
		curContext.setGoChild(true);
		curContext.setLineDelimiter(true);
		curContext.setLinkTag(false);
	}
}
