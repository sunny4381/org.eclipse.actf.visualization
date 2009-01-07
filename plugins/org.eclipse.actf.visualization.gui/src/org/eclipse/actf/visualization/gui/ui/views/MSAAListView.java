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


import java.util.List;
import java.util.Vector;

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;
import org.eclipse.actf.accservice.swtbridge.MSAA;
import org.eclipse.actf.util.win32.HighlightComposite;
import org.eclipse.actf.visualization.gui.IGuiViewIDs;
import org.eclipse.actf.visualization.gui.internal.util.GuiImages;
import org.eclipse.actf.visualization.gui.internal.util.Messages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;




public class MSAAListView extends ViewPart implements IMSAAListView {
    private static final String[] HEADINGS =	{ "#", Messages.msaa_name, Messages.msaa_role, Messages.msaa_state, "X", "Y", "W", "H"}; 
    private static final int[] WEIGHTS =		{ 1,5,5,5,1,1,1,1 };
    private static final int[] ALIGNMENTS =	{ SWT.RIGHT, SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT };

    private TableViewer viewer;
    
    private MSAASiblingViewerSorter sorter = new MSAASiblingViewerSorter();
    private boolean suppressHilight = false;
    
	private Action refreshAction; 
    private AccessibleObject objectOnAppear = null;
    private static final MSAATreeContentProvider provider = MSAATreeContentProvider.getDefault();
    
    private static final Color GRAY_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);


	 
	/**
	 * The constructor.
	 */
	public MSAAListView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		Table table = new Table(parent,SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION);
	    TableLayout layout = new TableLayout();
	    table.setLayout(layout);
	    table.setLinesVisible(true);
	    table.setHeaderVisible(true);
	    for( int i=0; i<HEADINGS.length; i++ ) {
		    layout.addColumnData(new ColumnWeightData(WEIGHTS[i]));
		    TableColumn tc = new TableColumn(table, SWT.NONE);
		    tc.setText(HEADINGS[i]);
		    tc.setAlignment(ALIGNMENTS[i]);
		    tc.setResizable(true);
		    final int newSortingColumn = i+1;
		    tc.addSelectionListener(new SelectionAdapter(){
		    	public void widgetSelected(SelectionEvent e) {
		    		if( newSortingColumn== sorter.sortingColumn ) {
		    			sorter.sortingColumn = -newSortingColumn;
		    		}
		    		else {
		    			sorter.sortingColumn = newSortingColumn;
		    		}
		    		viewer.refresh();
		    	}
		    });
	    }

	    viewer = new TableViewer(table);
		MSAASiblingContentAndLabelProvider provider = new MSAASiblingContentAndLabelProvider();
		viewer.setContentProvider(provider);
		viewer.setLabelProvider(provider);
		viewer.setSorter(sorter);
		viewer.addSelectionChangedListener(new ISelectionChangedListener(){
		    public void selectionChanged(SelectionChangedEvent event) {
		    	if( suppressHilight ) return;
		        ISelection selection = event.getSelection();
		        if (selection instanceof IStructuredSelection) {
		            Object selectedElement = ((IStructuredSelection) selection).getFirstElement();
		            if (selectedElement instanceof AccessibleObject) {
		                AccessibleObject object = (AccessibleObject) selectedElement;
		                if (null != object) {
                            IMSAAOutlineView outlineView = (IMSAAOutlineView)MSAAViewRegistory.findView(IGuiViewIDs.ID_OUTLINEVIEW);
                            if( null != outlineView ) {
                                outlineView.setSelection(object);
                            }
                            else {
                                HighlightComposite.flashRectangle(object.getAccLocation());
                            }
		                }
		            }
		        }
				
		    }
		});
		
		makeActions();
		hookContextMenu();
		contributeToActionBars();
		if( null != MSAAViewRegistory.outlineObject ) {
			setSelection(MSAAViewRegistory.outlineObject);
		}
		
		HighlightComposite.initOverlayWindow();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
        if( null != objectOnAppear ) {
            setSelection(objectOnAppear);
        }
	}

	public void setSelection(AccessibleObject object) {
        if( !viewer.getControl().isVisible() ) {
            objectOnAppear = object;
            return;
        }
        objectOnAppear = null;
		suppressHilight = true;
		AccessibleObject newInput = object.getCachedParent();
        if( null != newInput ) {
        	AccessibleObject oldInput = (AccessibleObject)viewer.getInput();
       		if( oldInput != newInput ) {
        		viewer.setInput(newInput);
    		}
        }
		viewer.setSelection(new StructuredSelection(object),true);
		suppressHilight = false;
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				MSAAListView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(refreshAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(refreshAction);
	}

	private void makeActions() {
        refreshAction = new Action(Messages.msaa_refresh) { 
            public void run() {
				viewer.refresh();
            }
        };
        refreshAction.setToolTipText(Messages.msaa_refresh); 
        refreshAction.setImageDescriptor(GuiImages.IMAGE_REFRESH);
	}
	
	private class MSAASiblingContentAndLabelProvider extends LabelProvider implements IStructuredContentProvider,
	IPropertyChangeListener, ITableLabelProvider, ITableColorProvider {
		
		private Object[] lastElements;

		public MSAASiblingContentAndLabelProvider() {
			super();
		}

		public Object[] getElements(Object inputElement) {
			lastElements = null;
			if( inputElement instanceof AccessibleObject ) {
                Object[] accChildren = provider.getElements(inputElement);
	            List childList = new Vector();
	            for (int i = 0; i < accChildren.length; i++) {
	                try {
                        int visibleState = ((AccessibleObject)accChildren[i]).getAccState() & (MSAA.STATE_INVISIBLE|MSAA.STATE_OFFSCREEN);
                        if( MSAA.STATE_INVISIBLE != visibleState ) {
                            childList.add(accChildren[i]);
                        }
	                } catch (Exception e) {
	                }
	                ;
	            }
	            lastElements = childList.toArray();
			}
	        return lastElements;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		/*
		 * IPropertyChangeListener
		 */
		public void propertyChange(PropertyChangeEvent event) {
		}

		/*
		 * ITableLabelProvider
		 */

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if( element instanceof AccessibleObject ) {
				switch( columnIndex ) {
				case 0:
					if( null!=lastElements ) {
						for( int i=0; i<lastElements.length; i++ ) {
							if( element.equals(lastElements[i]) ) {
								return Integer.toString(i+1);
							}
						}
					}
					return ""; //$NON-NLS-1$
				case 1:
					return ((AccessibleObject)element).getAccName();
				case 2:
					return ((AccessibleObject)element).getRoleText();
				case 3:
					return MSAA.getStateText(((AccessibleObject)element).getAccState());
				case 4:
				case 5:
				case 6:
				case 7:
					Rectangle location = ((AccessibleObject)element).getAccLocation();
					if( null==location ) return ""; //$NON-NLS-1$
					switch( columnIndex-4 ) {
						case 0: return Integer.toString(location.x);
						case 1: return Integer.toString(location.y);
						case 2: return Integer.toString(location.width);
						case 3: return Integer.toString(location.height);
					}
				default:
						return "?"; //$NON-NLS-1$
				}
			}
			return null;
		}

        public Color getBackground(Object element, int columnIndex) {
            return null;
        }

        public Color getForeground(Object element, int columnIndex) {
            if( element instanceof AccessibleObject ) {
                int accState = ((AccessibleObject)element).getAccState();
                if( 0 != (accState & MSAA.STATE_INVISIBLE) ) {
                    return GRAY_COLOR;
                }
            }
            return null;
        }
	}

	private class MSAASiblingViewerSorter extends ViewerSorter {

		public int sortingColumn = 0;

		public int compare(Viewer viewer, Object e1, Object e2) {
			if( 0 != sortingColumn && viewer instanceof TableViewer ) {
				IBaseLabelProvider labelProvider = ((TableViewer)viewer).getLabelProvider();
				if( labelProvider instanceof ITableLabelProvider ) {
					int columnIndex = Math.abs(sortingColumn)-1;
					String s1 = ((ITableLabelProvider)labelProvider).getColumnText(e1,columnIndex);
					String s2 = ((ITableLabelProvider)labelProvider).getColumnText(e2,columnIndex);
					int result = 0;
					switch( columnIndex ) {
						default:
							try {
								result = Integer.parseInt(s1)-Integer.parseInt(s2);
								break;
							}
							catch( Exception e ) {
							}
						case 1:
						case 2:
						case 3:
							result = collator.compare(s1,s2);
							break;
					}
					return sortingColumn > 0 ? result : -result;
				}
			}
			return 0;
		}

	}}
