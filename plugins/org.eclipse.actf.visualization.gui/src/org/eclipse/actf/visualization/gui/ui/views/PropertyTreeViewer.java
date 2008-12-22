/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.gui.ui.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.actf.visualization.gui.internal.util.Messages;
import org.eclipse.actf.visualization.gui.msaa.properties.IPropertyInvoke;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.views.properties.IPropertySource;


public class PropertyTreeViewer extends TreeViewer {

	private Map lastContents = new HashMap();
	
	public PropertyTreeViewer(Composite parent, int style) {
		this(new Tree(parent, style));
	}

	public PropertyTreeViewer(Composite parent) {
		this(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
	}

	public PropertyTreeViewer(Tree tree) {
		super(tree);
        tree.setLinesVisible(true);
        tree.setHeaderVisible(true);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        TreeColumn nameColumn = new TreeColumn(tree,SWT.NONE);
        nameColumn.setText(Messages.msaa_name); 
        TreeColumn valueColumn = new TreeColumn(tree,SWT.NONE);
        valueColumn.setText(Messages.msaa_value); 
        tree.addControlListener(new ControlAdapter() {
            public void controlResized(ControlEvent e) {
            	Tree tree = getTree();
                Rectangle area = tree.getClientArea();
                TreeColumn[] columns = tree.getColumns();
                if (area.width > 0) {
                    columns[0].setWidth(area.width * 40 / 100);
                    columns[1].setWidth(area.width - columns[0].getWidth() - 4);
                    tree.removeControlListener(this);
                }
            }
        });
        tree.addKeyListener(new KeyAdapter(){

			public void keyPressed(KeyEvent e) {
                if( SWT.CR == e.keyCode ) {
                	invokeSource();
                }
			}
        	
        });
	}

	private void invokeSource() {
        IStructuredSelection selection = (IStructuredSelection)getSelection();
        if (selection.size() == 1) {
        	invoke(selection.getFirstElement());
        }
	}
    
    public void invoke(Object element) {
        if( element instanceof PropertyTreeEntry ) {
            PropertyTreeEntry entry = (PropertyTreeEntry)element;
            IPropertySource source = entry.getPropertySource();
            if( source instanceof IPropertyInvoke ) {
                MSAAViewRegistory.addUpdateRef();
                try {
                    if( ((IPropertyInvoke)source).invoke(entry.getId(),getControl().getShell()) ) {
                        refresh(element);
                    }
                }
                finally {
                    MSAAViewRegistory.releaseUpdateRef();
                }
            }
        }
    }
	
	public void refresh() {
		setInput(getRoot());
	}

	protected void inputChanged(Object input, Object oldInput) {
        preservingSelection(new Runnable() {
            public void run() {
                Tree tree = (Tree)getControl();
				tree.setRedraw(false);
        		ISelection selected = getSelection();
        		Object[] expanded = getExpandedElements();
        		lastContents.clear();
        		getContents(lastContents, tree.getItems());
                removeAll(tree);
                tree.setData(getRoot());
                createChildren(tree);
                for( int i=0; i<expanded.length; i++ ) {
                	setExpandedState(expanded[i], true);
                }
                if( getExpandedElements().length==0 && tree.getItemCount()>0) {
                	setExpandedState(tree.getItem(0).getData(), true);
                }
				setSelection(selected,true);
				tree.setRedraw(true);
            }
        });
	}

	private void getContents(Map map, TreeItem[] items) {
        for( int i=0; i<items.length; i++ ) {
            TreeItem item = items[i];
            Object itemData = item.getData();
            if( itemData instanceof PropertyTreeEntry ) {
            	PropertyTreeEntry entry = (PropertyTreeEntry)itemData;
            	map.put(entry.getPath(), item.getText(1));
            }
            getContents(map, item.getItems());
        }
	}
	
	public Map getLastContents() {
		return lastContents;
	}
}
