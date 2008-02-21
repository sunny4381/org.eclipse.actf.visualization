/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.html.eval;

import java.util.Vector;

import org.eclipse.actf.model.dom.sgml.IErrorLogListener;
import org.eclipse.actf.model.dom.sgml.ISGMLConstants;
import org.eclipse.actf.util.html2view.Html2ViewMapData;
import org.eclipse.actf.visualization.eval.EvaluationPreferencesUtil;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ProblemItemImpl;




public class HtmlErrorLogListener implements IErrorLogListener {

	//TODO move to eval plugin (eval.html) (& move IErrorLogListener to common/util)
	
	private boolean isNoDoctype = false;

	private boolean isNonPublic = false;

	private boolean isInvalidDoctype = false;

	private Vector<IProblemItem> problemV = new Vector<IProblemItem>();

	private String orgDoctype = "";
	
	private boolean flag = true;

	public void errorLog(int arg0, String arg1) {
		if (arg0 != ISGMLConstants.ILLEGAL_ATTRIBUTE || arg1.indexOf(Html2ViewMapData.ACTF_ID) < 0) {
			// TODO create HTML problems
			switch (arg0) {
			case ISGMLConstants.DOCTYPE_MISSED:
				isNoDoctype = true;
				break;
			case ISGMLConstants.ILLEGAL_DOCTYPE:
				if (arg1.indexOf("Invalid DOCTYPE declaration.") > -1) {
					isNonPublic = true;
				} else if (arg1.matches(".*Instead of \".*\" use \".*\" as a DTD.")) {
					orgDoctype = arg1.substring(arg1.indexOf("\"") + 1);
					orgDoctype = orgDoctype.substring(0, orgDoctype.indexOf("\""));
					if (orgDoctype.matches("-//W3C//DTD XHTML ((1.0 (Strict|Transitional|Frameset))|1.1|Basic 1.0)//EN")) {
						orgDoctype = "";
					} else {
						isInvalidDoctype = true;
					}
				}
				break;
			case ISGMLConstants.ILLEGAL_CHILD:
                //TBD "li" case (C_1000.7)
                //System.out.println(arg0+" : "+arg1);
				if (arg1.matches(".*<head.*> must be before <body.*")) {
					addHtmlProblem("C_1000.1", arg1);
				} else if (arg1.matches(".*<html.*> is not allowed as a child of <.*")) {
					addHtmlProblem("C_1000.2", arg1);
				} else if (arg1.matches(".*<body.*> is not allowed as a child of <.*")) {
					addHtmlProblem("C_1000.3", arg1);
				} else if (arg1.matches(".*Order of <html.*>'s children is wrong.*")) {
					addHtmlProblem("C_1000.5", arg1);
				}
				break;
			default:
			}
		}
	}

	private void addHtmlProblem(String id, String target) {
		IProblemItem tmpCP = new ProblemItemImpl(id);
		int line = -1;
		String tmpS[] = target.split(":");
		if (tmpS.length > 0) {
			try {
				line = Integer.parseInt(tmpS[0].trim());
			} catch (Exception e) {
			}
		}
		if (line > -1) {
			tmpCP.setLine(line);
		}
		problemV.add(tmpCP);
	}

	public boolean isNoDoctypeDeclaration() {
		return (isNoDoctype || isNonPublic || isInvalidDoctype);
	}

	public boolean isNonPublicDoctype() {
		return (isNonPublic);
	}

	public boolean isInvalidDoctype() {
		return (isInvalidDoctype);
	}

	public String getDeclaratedDoctype() {
		return (orgDoctype);
	}

	public Vector<IProblemItem> getHtmlProblemVector() {
		if(flag){
			// (IE based LIVE DOM)->DOCTYPE was removed by IE
			if (EvaluationPreferencesUtil.isOriginalDOM()
					&& isNoDoctypeDeclaration()) {
				if (isInvalidDoctype() || isNonPublicDoctype()) {
					problemV.add(new ProblemItemImpl("C_1000.6"));
				} else {
					problemV.add(new ProblemItemImpl("C_1000.7"));
				}
			}			
			flag = false;
		}
		return (problemV);
	}
}
