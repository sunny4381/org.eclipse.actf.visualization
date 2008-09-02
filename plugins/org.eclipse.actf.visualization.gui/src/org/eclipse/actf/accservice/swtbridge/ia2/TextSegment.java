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


public class TextSegment {

    public String text;
    public int start;
    public int end; 
    
    public TextSegment() {
    }
    public TextSegment(String text, int start, int end) {
        this.text = text;
        this.start = start;
        this.end = end;
    }

}
