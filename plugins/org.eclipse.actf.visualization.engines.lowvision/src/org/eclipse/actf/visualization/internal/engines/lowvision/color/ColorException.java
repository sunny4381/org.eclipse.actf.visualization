/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.engines.lowvision.color;

public class ColorException extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3622924352947460191L;
	
	public ColorException(){
		super();
    }
    public ColorException( String _s ){
		super(_s);
    }
}
