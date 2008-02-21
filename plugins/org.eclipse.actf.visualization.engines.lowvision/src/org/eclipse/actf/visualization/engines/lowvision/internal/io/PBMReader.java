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
package org.eclipse.actf.visualization.engines.lowvision.internal.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.actf.visualization.engines.lowvision.image.BinaryImage;
import org.eclipse.actf.visualization.engines.lowvision.io.LowVisionIOException;


public class PBMReader{
    public static BinaryImage readBinaryImage( String _fileName ) throws LowVisionIOException{
		FileInputStream fis = null;
		try{
	    	fis = new FileInputStream( _fileName );
		}catch( FileNotFoundException e ){
	    	e.printStackTrace();
	    	throw new LowVisionIOException( "The file was not found: " + _fileName );
		}
		BufferedReader br = new BufferedReader( new InputStreamReader( fis ) );

		try{
	    	char magic1 = (char)(br.read());
	    	char magic2 = (char)(br.read());
	    	// support "P1" (not "P4")
	    	if( magic1 != 'P' || magic2 != '1' ){
			throw new LowVisionIOException( "Bad magic number: " + magic1 + magic2 + "\n\"P4\" cannot be treated in this version.\n(Only \"P1\" is accepted.)" );
	    	}
		}catch( IOException e ){
	    	throw new LowVisionIOException( "IOException occurred while reading the magic number." );
		}

		StringBuffer sb = new StringBuffer();
		try{
			// remove comments, etc.
	    	String oneLine = null;
	    	while( (oneLine=br.readLine()) != null ){
			int index = oneLine.indexOf("#");
			if( index == -1 )
		    	sb.append( oneLine + "\n" );
			else
		    	sb.append( oneLine.substring( 0, index ) + "\n" );
	    	}
		}catch( IOException e ){
	    	e.printStackTrace();
	    	throw new LowVisionIOException( "IO error occurred when reading." );
		}

		String content = sb.toString();
		if( !Character.isWhitespace( content.charAt(0) ) ){
	    	throw new LowVisionIOException( "The magic number must be followed by a white space." );
		}
		content = content.substring(1);

		Pattern pat = Pattern.compile( "(\\d+)\\s(\\d+)\\s([01\\s]+)" );
		Matcher mat = pat.matcher( content );
		if( !(mat.find()) ){
			throw new LowVisionIOException( "Invalid data format (1)." );
		}
		int count = mat.groupCount();
		if( count != 3 ){
			throw new LowVisionIOException( "Invalid data format (2). count = " + count );
		}
		int width = Integer.parseInt(mat.group(1));
		int height = Integer.parseInt(mat.group(2));
		String dataStr = mat.group(3);
		Pattern pat2 = Pattern.compile( "\\s+" );
		Matcher mat2 = pat2.matcher( dataStr );
		String data = mat2.replaceAll( "" );

		if( data.length() != width*height ){
	    	throw new LowVisionIOException( "Data size does not equal to width*height" );
		}

		BinaryImage bi = new BinaryImage( width, height );
		byte[][] bidata = bi.getData();
		int k=0;
		for( int j=0; j<height; j++ ){
	    	for( int i=0; i<width; i++ ){
				if( data.charAt(k) == '1' )
		    	bidata[j][i] = 1;
				k++;
	    	}
		}

		return( bi );
    }
}
