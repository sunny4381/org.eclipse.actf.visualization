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

public class StaticFORMRenderer implements IElementRenderer {

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.internal.IElementRenderer#getPacketCollectionIn(Element, Context)
	 */
	public PacketCollection getPacketCollectionIn(
		Element element,
		Context curContext,
		String url,
		MessageCollection mc) {

		// set `context in' flags
		setContextIn(element, curContext);

		// get the form number
		int num = DomUtil.getFormNum(element);

		// build result string
		String result =
			OutLoud.buildResultString(
				mc,
				url,
				element,
				"in",
				null,
				"name=num1",
				Integer.toString(num));

		if (result == null && OutLoud.hprDefltMsg)
			result = "(Start of form " + num + ".)";

		if (result != null)
			result = result.trim();
		return new PacketCollection(
			new Packet(element, result, curContext, true));
	}

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.internal.IElementRenderer#getPacketCollectionOut(Element, Context)
	 */
	public PacketCollection getPacketCollectionOut(
		Element element,
		Context curContext,
		String url,
		MessageCollection mc) {

		// set `context out' flags
		setContextOut(element, curContext);

		// get the form number
		int num = DomUtil.getFormNum(element);

		// build result string
		String result =
			OutLoud.buildResultString(
				mc,
				url,
				element,
				"out",
				null,
				"name=num1",
				Integer.toString(num));
		if (result == null && OutLoud.hprDefltMsg)
			result = "(End of form " + num + ".)";

		if (result != null)
			result = result.trim();

		return new PacketCollection(
			new Packet(element, result, curContext, true));
	}

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.internal.IElementRenderer#setContextIn(Context)
	 */
	public void setContextIn(Element element, Context curContext) {
		curContext.setStartSelect(true);
		curContext.setGoChild(true);
		curContext.setInsideForm(true);
		curContext.setLineDelimiter(true);
	}

	/**
	 * @see org.eclipse.actf.visualization.engines.voicebrowser.internal.IElementRenderer#setContextOut(Context)
	 */
	public void setContextOut(Element element, Context curContext) {
		curContext.setStartSelect(true);
		curContext.setGoChild(true);
		curContext.setInsideForm(false);
		//		curContext.setLineDelimiter(false);
		curContext.setLineDelimiter(true);
	}
}
