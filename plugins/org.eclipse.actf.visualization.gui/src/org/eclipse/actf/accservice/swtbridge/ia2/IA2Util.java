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

import java.util.StringTokenizer;



public class IA2Util {

    public static String getAttribute(String attributes, String name) {
        if( null != attributes ) {
            StringTokenizer st = new StringTokenizer(attributes,";"); //$NON-NLS-1$
            while( st.hasMoreTokens() ) {
                String nameValue = st.nextToken();
                int sep = nameValue.indexOf(':');
                if( sep > 0 ) {
                    if( name.equals(nameValue.substring(0,sep)) ) {
                        return nameValue.substring(sep+1);
                    }
                }
            }
        }
        return null;
    }
    
    public static boolean getExtendedState(String[] extendedStates, String name) {
        if( null != extendedStates) {
            for( int i=0; i<extendedStates.length; i++ ) {
                if( extendedStates[i].equals(name) ) {
                    return true;
                }
            }
        }
        return false;
    }
}
