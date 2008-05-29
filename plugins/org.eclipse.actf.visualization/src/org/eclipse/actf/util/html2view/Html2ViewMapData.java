/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.util.html2view;



public class Html2ViewMapData {

	public static final String ACTF_ID = "eclipse-actf-id";

	int start[] = { -1, -1 };
    int end[] = { -1, -1 };

    public Html2ViewMapData(int[] start, int[] end) {
        setStart(start);
        setEnd(end);
    }

    public String toString() {
        return ("start:" + start[0] + "," + start[1] + " end:" + end[0] + "," + end[1]);
    }

    /**
     * @return
     */
    public int[] getEnd() {
        return end;
    }

    /**
     * @return
     */
    public int[] getStart() {
        return start;
    }

    public int getStartLine() {
        return start[0];
    }

    public int getEndLine() {
        return end[0];
    }

    public int getStartColumn() {
        return start[1];
    }

    public int getEndColumn() {
        return end[1];
    }

    /**
     * @param is
     */
    private void setEnd(int[] is) {
        if (is != null && is.length == 2) {
            end = is;
        }
    }

    /**
     * @param is
     */
    private void setStart(int[] is) {
        if (is != null && is.length == 2) {
            start = is;
        }
    }

}
