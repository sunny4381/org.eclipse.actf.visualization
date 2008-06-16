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

import java.util.Vector;


public class HtmlLine2Id {

	private Html2ViewMapData[] data;
	private int size;
	private boolean is1base;	//line count start from 1 (bobby)
	
	//(1,1) base
	public HtmlLine2Id(Vector<Html2ViewMapData> tmpV) {
		this(tmpV,true);
	}

	public HtmlLine2Id(Vector<Html2ViewMapData> tmpV, boolean is1base){
		this.is1base = is1base; 
		try{
			size = tmpV.size();
			data = new Html2ViewMapData[size+1];
			int i;
			for(i=0;i<size;i++){
				data[i] = (Html2ViewMapData)tmpV.get(i);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public Vector<Integer> getId(int target){
		int line = target;
		if(is1base){
			line--;
		}
		
		int firstId = search(0,size-1,line);
		Vector<Integer> resultV = new Vector<Integer>();
		if(firstId > -1){
			while(firstId<size && data[firstId].getStartLine()==line){
				resultV.add(new Integer(firstId));
				firstId++;
			}
		}
		
		return(resultV);
	}

	//estimate the position from StartLine
	private int search(int start, int end, int target){
		//System.out.println("search: "+start+" "+end+" "+target);
		if(data[start].getStartLine()<target && data[end].getStartLine()>target){
			int tmp = (start+end)/2;
			//System.out.println("1: "+start+" "+end+" "+tmp);
			if(data[tmp].getStartLine()>target){
				return(search(start,tmp,target));
			}else if(start==tmp){
				return(-1);
			}else{
				return(search(tmp,end,target));
			}			
		}else if(data[end].getStartLine()==target){
			//System.out.println("2:");
			do{
				//System.out.println(end);
				end--;
			}while(end>=0 && data[end].getStartLine()==target);
			end++;
			return(end);
		}else if(data[start].getStartLine()==target){
			//System.out.println("3:");
			return(start);
		}else{
			//System.out.println("4:");
			return(-1);
		}
	}

}
