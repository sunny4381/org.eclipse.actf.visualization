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

import java.io.PrintStream;
import java.io.PrintWriter;

public class ColorUtil {
	// pre-defined 16 colors (HTML4.0, XHTML1.0, CSS2)
	private static final String predefinedColors[] = {
		"black", "#000000", "silver", "#c0c0c0",
		"gray", "#808080", "grey", "#808080", "white", "#ffffff",
		"maroon", "#800000", "red", "#ff0000",
		"purple", "#800080", "fuchsia", "#ff00ff",
		"green", "#008000", "lime", "#00ff00",
		"olive", "#808000", "yellow", "#ffff00",
		"navy", "#000080", "blue", "#0000ff",
		"teal", "#008080", "aqua", "#00ffff"
	};
	private static final int numPredefinedColors = predefinedColors.length/2;

	public static boolean isPredefinedColor( String _s ){
		String s = _s.toLowerCase();
		for( int i=0; i<numPredefinedColors; i++ ){
			if( predefinedColors[i*2].equals(s) ){
				return( true );
			}
		}
		return( false );
	}

	public static String predefinedColor2Pound( String _s ){
		String s = _s.toLowerCase();
		for( int i=0; i<numPredefinedColors; i++ ){
			if( predefinedColors[i*2].equals(s) ){
				return( predefinedColors[i*2+1] );
			}
		}
		return( null );
	}

	// TYPE_INT_RGB <--> R,G,B
	public static int[] intToRGB( int _i ){
		int[] rgb = new int[3];
		rgb[0] = (_i>>16) & 0xff;
		rgb[1] = (_i>>8) & 0xff;
		rgb[2] = _i & 0xff;
		return( rgb );
	}
	public static int RGBToInt( int _r, int _g, int _b ){
		return( (_r&0xff)<<16 | (_g&0xff)<<8 | (_b&0xff) );
	}
	
	public static void dumpColor( PrintStream _ps, int _i ){
		(new ColorIRGB(_i)).dump(_ps);
	}

	public static void dumpColor( PrintWriter _pw, int _i ){
		(new ColorIRGB(_i)).dump(_pw);
	}
	
	/*
	 * assign distinguishable colors for sequential IDs
	 * 
	 */
	public static int distinguishableColor( int _id ){
		int r = 0;
		int g = 0;
		int b = 0;
		if( _id == 0 ){
			return( 0x00ffffff );
		}
		else{
			for( int l=0; l<8; l++ ){
				r += ((_id>>(l*3))&1)<<(7-l);
				g += ((_id>>(l*3+1))&1)<<(7-l);
				b += ((_id>>(l*3+2))&1)<<(7-l);
			}
			return( (r<<16) | (g<<8) | b );
		}
	}
}
