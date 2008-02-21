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

package org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.actf.model.IModelService;
import org.eclipse.actf.model.IWebBrowserACTF;
import org.eclipse.actf.model.ModelServiceUtils;
import org.eclipse.actf.visualization.engines.blind.html.internal.util.VisualizationAttributeInfo;
import org.eclipse.actf.visualization.eval.problem.HighlightTargetId;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;


public class ElementViewerJFace {

	private IHighlightElementListener prb;

    private int[] sortModeArray;

    private Composite elementViewerComposite;

    private TableViewer viewer;

    private TableColumn categoryCol;

    private TableColumn descCol;

    private final static String COL_CATEGORY = "Category";

    private final static String COL_ID_STRING = "Value";

    private final static String COL_TAG_NAME = "Tag Name";

    private final static String COL_DESCRIPTION = "Description";

    private String strHelpUrl;

    public ElementViewerJFace(Composite parent, IHighlightElementListener _prb) {

        prb = _prb;
        elementViewerComposite = new Composite(parent, SWT.NULL);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        elementViewerComposite.setLayout(gridLayout);
        elementViewerComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        final Table table = new Table(elementViewerComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
                | SWT.FULL_SELECTION | SWT.BORDER);
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        sortModeArray = new int[4];
        categoryCol = new TableColumn(table, SWT.LEFT);
        categoryCol.setText(COL_CATEGORY);
        sortModeArray[0] = 0;
        TableColumn idCol = new TableColumn(table, SWT.LEFT);
        idCol.setText(COL_ID_STRING);
        idCol.setWidth(150);
        sortModeArray[1] = 0;
        TableColumn tagCol = new TableColumn(table, SWT.LEFT);
        tagCol.setText(COL_TAG_NAME);
        tagCol.setWidth(100);
        sortModeArray[2] = 0;
        descCol = new TableColumn(table, SWT.LEFT);
        descCol.setText(COL_DESCRIPTION);
        sortModeArray[3] = 0;

        table.addMouseListener(new MouseAdapter() {
            public void mouseUp(MouseEvent arg0) {
                if (arg0.stateMask == SWT.BUTTON3) {
                    if (table.getSelectionIndex() >= 0)
                        openPopupMenu();
                }
            }
        });
        viewer = new TableViewer(table);

        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent arg0) {
                List list = new ArrayList();
                for (Iterator i = ((IStructuredSelection) arg0.getSelection()).iterator(); i.hasNext();) {
                    list.add(i.next());
                }
                if (list.size() > 0) {
                    viewerSelected(list);
                }
            }
        });

        categoryCol.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                toggleCurrentSortMode(0);
                if (sortModeArray[0] == -2) {
                    sortByNodeId();
                } else {
                    viewer.setSorter(new ViewerSorter() {
                        public int compare(Viewer iviewer, Object e1, Object e2) {
                            int iRes;
                            if (e1 == null) {
                                iRes = -1;
                            } else if (e2 == null) {
                                iRes = 1;
                            } else {
                                String str1, str2;
                                str1 = ((VisualizationAttributeInfo) e1).getCategory();
                                str2 = ((VisualizationAttributeInfo) e2).getCategory();
                                if (str1.equals("")) {
                                    return 1;
                                } else if (str2.equals("")) {
                                    return -1;
                                }
                                iRes = str1.compareToIgnoreCase(str2);
                            }
                            return iRes * sortModeArray[0];
                        }
                    });
                }
            }
        });

        idCol.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                toggleCurrentSortMode(1);
                if (sortModeArray[1] == -2) {
                    sortByNodeId();
                } else {
                    viewer.setSorter(new ViewerSorter() {
                        public int compare(Viewer iviewer, Object e1, Object e2) {
                            int iRes;
                            if (e1 == null) {
                                iRes = -1;
                            } else if (e2 == null) {
                                iRes = 1;
                            } else {
                                iRes = ((VisualizationAttributeInfo) e1).getAttributeValue().compareToIgnoreCase(
                                        ((VisualizationAttributeInfo) e2).getAttributeValue());
                            }
                            return iRes * sortModeArray[1];
                        }
                    });
                }
            }
        });

        tagCol.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                toggleCurrentSortMode(2);
                if (sortModeArray[2] == -2) {
                    sortByNodeId();
                } else {
                    viewer.setSorter(new ViewerSorter() {
                        public int compare(Viewer iviewer, Object e1, Object e2) {
                            int iRes;
                            if (e1 == null) {
                                iRes = -1;
                            } else if (e2 == null) {
                                iRes = 1;
                            } else {
                                iRes = ((VisualizationAttributeInfo) e1).getTagName().compareToIgnoreCase(
                                        ((VisualizationAttributeInfo) e2).getTagName());
                            }
                            return iRes * sortModeArray[2];
                        }
                    });
                }
            }
        });

        descCol.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                toggleCurrentSortMode(3);
                if (sortModeArray[3] == -2) {
                    sortByNodeId();
                } else {
                    viewer.setSorter(new ViewerSorter() {
                        public int compare(Viewer iviewer, Object e1, Object e2) {
                            int iRes;
                            if (e1 == null) {
                                iRes = -1;
                            } else if (e2 == null) {
                                iRes = 1;
                            } else {
                                String str1, str2;
                                str1 = ((VisualizationAttributeInfo) e1).getDescription();
                                str2 = ((VisualizationAttributeInfo) e2).getDescription();
                                if (str1.equals("")) {
                                    return 1;
                                } else if (str2.equals("")) {
                                    return -1;
                                }
                                iRes = str1.compareToIgnoreCase(str2);
                            }
                            return iRes * sortModeArray[3];
                        }
                    });
                }
            }
        });

    }

    private void viewerSelected(List list) {
        prb.clearHighlight();
        VisualizationAttributeInfo selected = null;

        Vector<HighlightTargetId> tmpV = new Vector<HighlightTargetId>();
        
        for (int i = 0; i < list.size(); i++) {
            selected = (VisualizationAttributeInfo) list.get(i);
            tmpV.add(new HighlightTargetId(selected.getNodeId(),selected.getNodeId()));
            if (i==0){
                strHelpUrl = selected.getHelpUrl();            	
            }else if (!strHelpUrl.equals(selected.getHelpUrl())){
                strHelpUrl = "";
            }
        }

        prb.highlight(tmpV);
        
        IModelService dataSource =ModelServiceUtils.getActiveModelService();
        if(dataSource!=null && dataSource instanceof IWebBrowserACTF){
            ((IWebBrowserACTF)dataSource).hightlightElementByAttribute(selected.getAttribtueName(), selected.getAttributeValue());
        }
        
    }

    private void toggleCurrentSortMode(int sortKind) {
        switch (sortModeArray[sortKind]) {
        case -2:
            sortModeArray[sortKind] = 1;
            break;
        case -1:
            sortModeArray[sortKind] = -2;
            break;
        case 0:
            sortModeArray[sortKind] = 1;
            break;
        case 1:
            sortModeArray[sortKind] = -1;
            break;
        }
    }

    private void sortByNodeId() {
        viewer.setSorter(new ViewerSorter() {
            public int compare(Viewer iviewer, Object e1, Object e2) {
                int iRes;
                if (e1 == null) {
                    iRes = -1;
                } else if (e2 == null) {
                    iRes = 1;
                } else {
                    iRes = ((VisualizationAttributeInfo) e1).getNodeId()
                            - ((VisualizationAttributeInfo) e2).getNodeId();
                }
                return iRes;
            }
        });
    }

    private void openPopupMenu() {
        String[] itemName = new String[2];
        itemName[0] = "Select";
        itemName[1] = "View Help...";
        boolean[] enabled = new boolean[2];
        enabled[0] = true;
        enabled[1] = true;
        if (strHelpUrl.equals("")){
            enabled[1] = false;
        }
        PopupMenu popupMenu = new PopupMenu(new Shell(), itemName, enabled);
        String strRet = popupMenu.open();
       if (strRet.equals(itemName[1])) {
        	PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(strHelpUrl);
        }
    }

    /**
     * @return
     */
    public Composite getComposite() {
        return elementViewerComposite;
    }

    public void setContentProvider(IStructuredContentProvider contentProvider) {
        viewer.setContentProvider(contentProvider);
    }

    public void setLabelProvider(ITableLabelProvider labelProvider) {
        viewer.setLabelProvider(labelProvider);
    }

    public void setElementList(List eleList) {
        viewer.setInput(eleList);
    }

    public void setCategoryColWidth(int width) {
        categoryCol.setWidth(width);
        if (width == 0)
            categoryCol.setResizable(false);
    }

    public void setDescColWidth(int width) {
        descCol.setWidth(width);
        if (width == 0)
            descCol.setResizable(false);
    }
}
