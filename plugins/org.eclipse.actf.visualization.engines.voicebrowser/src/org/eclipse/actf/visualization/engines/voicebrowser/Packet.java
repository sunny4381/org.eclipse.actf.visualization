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
import org.w3c.dom.Node;

public class Packet {
	private Node node;
	private String text;
	private Context context;
	private boolean isStartTag;

	/**
	 * Constructor for RenderedText.
	 * @param ctx packet's context
	 * @param curNode node
	 * @param curText default text for this packet
	 */
	public Packet(
		Node curNode,
		String curText,
		Context ctx,
		boolean isstarttag) {
		context = new Context(ctx);
		node = curNode;
		text = curText;
		isStartTag = isstarttag;
	}

	/**
	 * Returns the text.
	 * @return String
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * @param text The text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns the context.
	 * @return Context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * Sets the context.
	 * @param context The context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * Returns the node.
	 * @return Node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * Sets the node.
	 * @param node The node to set
	 */
	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * Returns the isStartTag.
	 * @return boolean
	 */
	public boolean isStartTag() {
		return isStartTag;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sbuf = new StringBuffer();

		try {
			sbuf.append(getNode().getNodeName());
			sbuf.append("\n");
			sbuf.append("\t");
			sbuf.append(getText());
			sbuf.append("\n");
			sbuf.append("\t");
			sbuf.append(getContext().toString());
			sbuf.append("\n");
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}

		return sbuf.toString();
	}

}
