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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.actf.visualization.gui.GuiPlugin;
import org.eclipse.actf.visualization.gui.Messages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetEntry;



public class MSAAPropertiesView extends ViewPart implements IMSAAPropertiesView {
    public static final String ID = MSAAPropertiesView.class.getName();

	private PropertyTreeViewer viewer;
	private Action refreshAction;
	private Action showNullAction;
	private Action changeColorAction;
	private Action copyAction;
    private Action invokeAction;
	private PropertiesFilter filter;
    public static final String SEPARATOR = "-"; //$NON-NLS-1$
    private static final ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new PropertyTreeViewer(parent, SWT.FULL_SELECTION | SWT.SINGLE | SWT.HIDE_SELECTION);
		viewer.setContentProvider(new PropertyTreeContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		filter = new PropertiesFilter();
        viewer.addFilter(filter);
        makeActions();
        hookContextMenu();
        contributeToActionBars();
		if( null != MSAAViewRegistory.outlineObject ) {
			setInput(MSAAViewRegistory.outlineObject);
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	public void setInput(Object input) {
		viewer.setInput(input);
	}
	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				MSAAPropertiesView.this.fillContextMenu(manager);
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
		manager.add(showNullAction);
		manager.add(new Separator());
		manager.add(changeColorAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(new Separator());
		manager.add(copyAction);
		manager.add(new Separator());
		manager.add(refreshAction);
        Object selected = getSelectedItem();
        if (selected instanceof PropertyTreeEntry && 
            ((PropertyTreeEntry)selected).canInvoke() ) {
            manager.add(new Separator());
            manager.add(invokeAction);
        }
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
    private Object getSelectedItem() {
        IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
        if (selection.size() == 1) {
            return selection.getFirstElement();
        }
        return null;
    }
    
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(refreshAction);
	}

	private void makeActions() {
		refreshAction = new Action(Messages.getString("msaa.refresh")) { //$NON-NLS-1$
			public void run() {
				viewer.refresh();
			}
		};
		refreshAction.setToolTipText(Messages.getString("msaa.refresh")); //$NON-NLS-1$
		refreshAction.setImageDescriptor(GuiPlugin.IMAGE_REFRESH);
		
		showNullAction = new Action(Messages.getString("msaa.showNull"),Action.AS_CHECK_BOX) { //$NON-NLS-1$
			public void run() {
				filter.showNull = showNullAction.isChecked();
				viewer.refresh();
			}
		};
		showNullAction.setChecked(filter.showNull);
		
		changeColorAction = new Action(Messages.getString("msaa.changePropertiesColor"),Action.AS_CHECK_BOX) { //$NON-NLS-1$
			public void run() {
				filter.changeColor = changeColorAction.isChecked();
				viewer.refresh();
			}
		};
		changeColorAction.setChecked(filter.changeColor);
		
		copyAction = new Action(Messages.getString("msaa.copy")) { //$NON-NLS-1$
			public void run() {
				ISelection selection = viewer.getSelection();
				if( selection instanceof IStructuredSelection && !selection.isEmpty() ) {
					String strText = null, strValue = null;
					Object element = ((IStructuredSelection)selection).getFirstElement();
					if( element instanceof PropertyTreeEntry ) {
						strText = ((PropertyTreeEntry)element).getDisplayName();
						strValue = ((PropertyTreeEntry)element).getValueAsString();
					}
					else if( element instanceof IPropertySheetEntry ) {
						strText = ((IPropertySheetEntry)element).getDisplayName();
						strValue = ((IPropertySheetEntry)element).getValueAsString();
					}
					if( null != strText ) {
						if( null != strValue ) {
							strText += "\t"+strValue; //$NON-NLS-1$
						}
						new Clipboard(Display.getCurrent()).setContents(new Object[] { strText }, new Transfer[] { TextTransfer.getInstance() });
					}
				}
			}
		};
        copyAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
        
        invokeAction = new Action(Messages.getString("msaa.invoke")) { //$NON-NLS-1$
            public void run() {
                viewer.invoke(getSelectedItem());
            }
        };
	}
	
	private class ViewLabelProvider extends LabelProvider implements ITableLabelProvider, IColorProvider {
		public String getColumnText(Object obj, int index) {
			if( obj instanceof PropertyTreeEntry ) {
				PropertyTreeEntry entry = (PropertyTreeEntry)obj;
				if( 0==index ) {
					String text = entry.getDisplayName();
					if( entry.canInvoke() ) {
						text += "..."; //$NON-NLS-1$
					}
					return text;
				}
				return	entry.getValueAsString();
			}
			return ""; //$NON-NLS-1$
		}
		public Image getColumnImage(Object obj, int index) {
			return null;
		}
        public Color getForeground(Object element) {
        	if( element instanceof PropertyTreeEntry ) {
        		int color =((PropertyTreeEntry)element).getColor();
        		if ( -1 != color) {
        			return Display.getCurrent().getSystemColor(color);
        		}
        	}
    		return null;
        }
        public Color getBackground(Object element) {
			return null;
        }
	}
    
    private class PropertiesFilter extends ViewerFilter {
    	public boolean showNull = false;
    	public boolean changeColor = false;
    	public String separateColor = Integer.toString(SWT.COLOR_GRAY);
    	public String hilightColor = Integer.toString(SWT.COLOR_BLUE); 
        
        public Object[] filter(Viewer viewer, Object parent, Object[] elements) {
            List outList = new ArrayList();
            for (int i = 0; i < elements.length; ++i) {
            	if( elements[i] instanceof PropertyTreeEntry ) {
            		PropertyTreeEntry entry = (PropertyTreeEntry)elements[i];
        			String value = entry.getValueAsString();
            		if( !showNull && "null".equals(value) ) { //$NON-NLS-1$
                    	continue;
                	}
            		if( changeColor && viewer instanceof PropertyTreeViewer ) {
            			Map lastContents = ((PropertyTreeViewer)viewer).getLastContents();
            			if( !lastContents.isEmpty() ) {
            				String lastValue = (String)lastContents.get(entry.getPath());
            				if( null != lastValue ) {
                				if( null == value ) {
                					value = ""; //$NON-NLS-1$
                				}
                				entry.setColor(value.equals(lastValue) ? -1 : SWT.COLOR_BLUE);
            				}
            			}
            		}
                    outList.add(entry);
            	} 
                else {
                    outList.add(elements[i]);
                }
            }
            return outList.toArray();
        }

        public boolean select(Viewer viewer, Object parentElement, Object element) {
            return false;
        }
        
    }
}
