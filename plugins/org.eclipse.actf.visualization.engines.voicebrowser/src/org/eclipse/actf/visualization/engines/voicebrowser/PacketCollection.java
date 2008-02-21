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

import java.util.ArrayList;

import org.w3c.dom.Node;


public class PacketCollection extends ArrayList<Packet> {

	/**
	 * Method PacketCollection.
	 * @param text
	 */
	public PacketCollection(Packet text) {
		super();
		add(text);
	}

	/**
	 * @see java.lang.Object#Object()
	 */
	public PacketCollection() {
		super();
	}

	/**
	 * Method getText.
	 * @return String
	 */
	public String getText() {
		return "";
	}

	/**
	 * Method add.
	 * @param p
	 * @return boolean
	 */
	public boolean add(Packet p) {
		return super.add(p);
	}

	/**
	 * Method addAll.
	 * @param c
	 * @return boolean
	 */
	public boolean addAll(PacketCollection c) {
		return super.addAll(c);
	}

	/**
	 * Method isLineDelimiter.
	 * @param i
	 * @return boolean
	 */
	public boolean isLineDelimiter(int i) {
		try {
			if (i < this.size())
				return ((Packet) get(i)).getContext().isLineDelimiter();
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method isLinkTag.
	 * @param i
	 * @return boolean
	 */
	public boolean isLinkTag(int i) {
		try {
			if (i < this.size())
				return ((Packet) get(i)).getContext().isLinkTag();
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method isInsideForm.
	 * @param i
	 * @return boolean
	 */
	public boolean isInsideForm(int i) {
		try {
			if (i < this.size())
				return ((Packet) get(i)).getContext().isInsideForm();
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method isInsideAnchor.
	 * @param i
	 * @return boolean
	 */
	public boolean isInsideAnchor(int i) {
		try {
			if (i < this.size())
				return ((Packet) get(i)).getContext().isInsideAnchor();
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method isStartSelect.
	 * @param i
	 * @return boolean
	 */
	public boolean isStartSelect(int i) {
		try {
			if (i < this.size())
				return ((Packet) get(i)).getContext().isStartSelect();
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method isStringOutput.
	 * @param i
	 * @return boolean
	 */
	public boolean isStringOutput(int i) {
		try {
			if (i < this.size())
				return ((Packet) get(i)).getContext().isStringOutput();
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method getFirstNode.
	 * @return Node
	 */
	public Node getFirstNode() {
		try {
			int size = this.size();
			for (int i = 0; i < size; i++) {
				if (((Packet) this.get(i)).isStartTag()) {
					return ((Packet) this.get(i)).getNode();
				}
			}
			return null;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return null;
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			return null;
		}
	}

	/**
	 * Method getLastNode.
	 * @return Node
	 */
	public Node getLastNode() {
		try {
			int size = this.size();
			for (int i = size - 1; i >= 0; i--) {
				if (((Packet) this.get(i)).isStartTag()) {
					return ((Packet) this.get(i)).getNode();
				}
			}
			return null;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return null;
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			return null;
		}
	}

	/**
	 * Method getTopNodePosition.
	 * @return int
	 */
	public int getTopNodePosition() {
		try {
			int size = this.size();
			for (int i = 0; i < size; i++) {
				String str = ((Packet) this.get(i)).getText();
				if (str != null && str.length() > 0) {
					if (this.isInsideAnchor(i)) {
						for (int j = i; j >= 0; j--)
							if (this.isLinkTag(j))
								return j;
					}
					return i;
				}
			}
			return -1;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return -1;
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			return -1;
		}
	}

	/**
	 * Method getBottomNodePosition.
	 * @return int
	 */
	public int getBottomNodePosition() {
		try {
			int size = this.size();
			boolean found = false;
			for (int i = size - 1; i >= 0; i--) {
				String str = ((Packet) this.get(i)).getText();
				if (((str != null) && (str.length() > 0)))
					found = true;
				if (found) {
					if (this.isLineDelimiter(i)) {
						if (i == size - 1)
							return i;
						else {
							if (this.isLinkTag(i + 1)
								&& (i + 2 < size && this.isLinkTag(i + 2)))
								return i + 2;
							else
								return i + 1;
						}
					}
				}
			}
			return -1;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return -1;
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			return -1;
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		int size = this.size();
		for (int i = 0; i < size; i++) {
			try {
				sbuf.append(((Packet) this.get(i)).toString());
				sbuf.append("\n");
			} catch (ClassCastException cce) {
				sbuf.append("error: " + i + "the object is not a packet.\n");
			}
		}
		return sbuf.toString();
	}

	/**
	 * Method getNodePosition.
	 * @param node
	 * @return int
	 */
	public int getNodePosition(Node node) {
		try {
			int size = this.size();
			for (int i = 0; i < size; i++) {
				if (node == ((Packet) this.get(i)).getNode()) {
					// System.out.println ("found: " + i + "/" + size);
					return i;
				}
			}
			return 0;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return 0;
		} catch (ClassCastException cce) {
			cce.printStackTrace();
			return 0;
		}
	}
}
