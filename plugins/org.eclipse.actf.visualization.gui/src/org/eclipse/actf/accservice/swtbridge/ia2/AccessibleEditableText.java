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




public interface AccessibleEditableText {

    public void dispose();

    public boolean copyText(int startOffset, int endOffset);

    public boolean deleteText(int startOffset, int endOffset);

    public boolean insertText(int offset, String strText);

    public boolean cutText(int startOffset, int endOffset);

    public boolean pasteText(int offset);

    public boolean replaceText(int startOffset, int endOffset, String strText);

    public boolean setAttributes(int startOffset, int endOffset, String strAttributes);

}
