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

public class Context {
	private boolean insideForm = false;
	private boolean linkTag = false;
	private boolean insideAnchor = false;
	private boolean startSelect = false;
	private boolean stringOutput = true;
	private boolean lineDelimiter = false;
	private boolean goChild = true;

	/**
	 * Constructor for Context.
	 */
	public Context() {
	}

	/**
	 * Constructor for Context.
	 * @param ctx source context instance to be copied.s
	 */
	public Context(Context ctx) {
		insideForm = ctx.isInsideForm();
		insideAnchor = ctx.isInsideAnchor();
		lineDelimiter = ctx.isLineDelimiter();
		startSelect = ctx.isStartSelect();
		stringOutput = ctx.isStringOutput();
		linkTag = ctx.isLinkTag();
		goChild = ctx.isGoChild();
	}

	/**
	 * Returns the insideForm.
	 * @return boolean
	 */
	public boolean isInsideForm() {
		return insideForm;
	}

	/**
	 * Sets the insideForm.
	 * @param i The insideForm to set
	 */
	public void setInsideForm(boolean i) {
		insideForm = i;
	}

	/**
	 * Returns the goChild.
	 * @return boolean
	 */
	public boolean isGoChild() {
		return goChild;
	}

	/**
	 * Sets the goChild.
	 * @param goChild The goChild to set
	 */
	public void setGoChild(boolean goChild) {
		this.goChild = goChild;
	}

	/**
	 * Returns the lineDelimiter.
	 * @return boolean
	 */
	public boolean isLineDelimiter() {
		return lineDelimiter;
	}

	/**
	 * Sets the lineDelimiter.
	 * @param lineDelimiter The lineDelimiter to set
	 */
	public void setLineDelimiter(boolean lineDelimiter) {
		this.lineDelimiter = lineDelimiter;
	}

	/**
	 * Returns the linkTag.
	 * @return boolean
	 */
	public boolean isLinkTag() {
		return linkTag;
	}

	/**
	 * Sets the linkTag.
	 * @param linkTag The linkTag to set
	 */
	public void setLinkTag(boolean linkTag) {
		this.linkTag = linkTag;
	}

	/**
	 * Returns the insideAnchor.
	 * @return boolean
	 */
	public boolean isInsideAnchor() {
		return insideAnchor;
	}

	/**
	 * Sets the insideAnchor.
	 * @param insideAnchor The insideAnchor to set
	 */
	public void setInsideAnchor(boolean insideAnchor) {
		this.insideAnchor = insideAnchor;
	}

	/**
	 * Returns the startSelect.
	 * @return boolean
	 */
	public boolean isStartSelect() {
		return startSelect;
	}

	/**
	 * Sets the startSelect.
	 * @param startSelect The startSelect to set
	 */
	public void setStartSelect(boolean startSelect) {
		this.startSelect = startSelect;
	}

	/**
	 * Returns the stringOutput.
	 * @return boolean
	 */
	public boolean isStringOutput() {
		return stringOutput;
	}

	/**
	 * Sets the stringOutput.
	 * @param stringOutput The stringOutput to set
	 */
	public void setStringOutput(boolean stringOutput) {
		this.stringOutput = stringOutput;
	}

	/**
	 * Convert object into a string
	 * @return converted string
	 */
	public String toString() {
		StringBuffer sbuf = new StringBuffer();

		sbuf.append("goChild=");
		sbuf.append(goChild);
		sbuf.append(", ");

		sbuf.append("insideForm=");
		sbuf.append(insideForm);
		sbuf.append(", ");

		sbuf.append("insideAnchor=");
		sbuf.append(insideAnchor);
		sbuf.append(", ");

		sbuf.append("startSelect=");
		sbuf.append(startSelect);
		sbuf.append(", ");

		sbuf.append("stringOutput=");
		sbuf.append(stringOutput);
		sbuf.append(", ");

		sbuf.append("linkTag=");
		sbuf.append(linkTag);
		sbuf.append(", ");

		sbuf.append("lineDelimiter=");
		sbuf.append(lineDelimiter);

		return sbuf.toString();
	}
}
