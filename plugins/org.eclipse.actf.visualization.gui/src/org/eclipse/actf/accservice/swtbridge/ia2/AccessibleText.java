/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.accservice.swtbridge.ia2;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;



public interface AccessibleText {

    public void dispose();

    public boolean addSelection(int startOffset, int endOffset);

    public TextSegment getCharacterAttributes(int offset);

    public int getCaretPosition();

    public Rectangle getCharacterBounds(int offset, int coordType);

    public int getSelectionCount();

    public int getIndexAtPoint(Point point, int coordType);

    public Point getSelection(int selectionIndex);

    public String getTextRange(int startOffset, int endOffset);

    public TextSegment getTextBeforeIndex(int offset, int boundaryType);

    public TextSegment getTextAfterIndex(int offset, int boundaryType);

    public TextSegment getTextAtIndex(int offset, int boundaryType);

    public boolean removeSelection(int selectionIndex);

    public boolean setCaretPosition(int offset);

    public boolean setSelection(int selectionIndex, int startOffset, int endOffset);

    public int getCharacterCount();

    public boolean scrollSubstringTo(int startIndex, int endIndex, int scrollType);

    public boolean scrollSubstringToPoint(int startIndex, int endIndex, int coordinateType, int x, int y);

    public TextSegment getNewText();

    public TextSegment getOldText();

}
