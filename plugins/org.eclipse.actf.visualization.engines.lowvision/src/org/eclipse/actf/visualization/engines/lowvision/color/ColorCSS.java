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

package org.eclipse.actf.visualization.engines.lowvision.color;

import java.util.StringTokenizer;

/*
 * colors in CSS
 * RGB(256)
 * 
 * valid syntax
 *  #RRGGBB
 *  #RGB
 *  rgb(R,G,B)
 *  rgb(R%,G%,B%)
 *  pre-defined colors(16) (see ColorUtil.java)
 */
public class ColorCSS extends ColorIRGB{
	private static final String DELIM = "/";

	// default values
	static final int TRANSPARENT_R = 0xff; // "transparent"
	static final int TRANSPARENT_G = 0xff; // "transparent"
	static final int TRANSPARENT_B = 0xff; // "transparent"
	public static final int TRANSPARENT = ((TRANSPARENT_R&0xff)<<16) | ((TRANSPARENT_G&0xff)<<8) | (TRANSPARENT_B&0xff); 
	public static final int DEFAULT_BACKGROUND_COLOR_INT = TRANSPARENT;
	public static final int DEFAULT_COLOR_INT = 0;

	String originalString = "";

	public ColorCSS( String _s ) throws ColorException{
		this( _s, true );
	}

	public ColorCSS( String _s, boolean _check ) throws ColorException{
		if( !(_s.endsWith(DELIM)) ){
			originalString = _s;
		}else{
			originalString = _s.substring( 0, _s.length()-1 );
		}

		// foreground color
		if( originalString.indexOf(DELIM) == -1 ){			
			ColorIRGB ci = new ColorIRGB( originalString );
			R = ci.getR();
			G = ci.getG();
			B = ci.getB();
		}
		else{ // background-color -> need to consider ancestor

			StringTokenizer st = new StringTokenizer( originalString.toLowerCase(), DELIM );
			boolean success = false;
			while( st.hasMoreTokens() ){
				String tmpStr = st.nextToken();
								
				if( !(tmpStr.equals( "transparent" )) ){
					// ColorIRGB ci = interpret( tmpStr );
					ColorIRGB ci = new ColorIRGB( tmpStr );
					R = ci.getR();
					G = ci.getG();
					B = ci.getB();
					success = true;
					break;
				}
			}
			if( !success ){
				R = TRANSPARENT_R;
				G = TRANSPARENT_G;
				B = TRANSPARENT_B;
			}
		}

		if( _check ){ rangeCheck(); }
		else{ rangeAdjust(); }
	}

	public ColorCSS() throws ColorException{
		throw new ColorException( "Constructor in wrong format." );
	}
	public ColorCSS( int _i ) throws ColorException{
		throw new ColorException( "Constructor in wrong format." );
	}
	public ColorCSS( int _i1, int _i2, int _i3 ) throws ColorException{
		throw new ColorException( "Constructor in wrong format." );
	}
	public ColorCSS( int _i1, int _i2, int _i3, boolean _b ) throws ColorException{
		throw new ColorException( "Constructor in wrong format." );
	}

	private void rangeCheck() throws ColorException{
		if( R<0 || R>255){
			throw new ColorException( "R is out of range: " + R + ", inputString = " + originalString );
		}
		if( G<0 || G>255){
			throw new ColorException( "G is out of range: " + G + ", inputString = " + originalString );
		}
		if( B<0 || B>255){
			throw new ColorException( "B is out of range: " + B + ", inputString = " + originalString );
		}
	}
	private void rangeAdjust(){
		if( R<0 ) R = 0;
		else if( R>255 ) R = 255;
		if( G<0 ) G = 0;
		else if( G>255 ) G = 255;
		if( B<0 ) B = 0;
		else if( B>255 ) B = 255;
	}
	
	public String getOriginalString(){
		return( originalString );
	}
}
