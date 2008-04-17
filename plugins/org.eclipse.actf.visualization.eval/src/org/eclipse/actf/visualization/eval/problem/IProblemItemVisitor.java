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

package org.eclipse.actf.visualization.eval.problem;



public interface IProblemItemVisitor {

//    public void visit(CheckerProblemNew item);
//
//    public void visit(BlindProblemNew item);
//
//    public void visit(ProblemItemImpl item);
//
//    public void visit(ProblemItemLV item);
    
    public void visit(IProblemItem item);
}
