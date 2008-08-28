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

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;
import org.eclipse.actf.accservice.swtbridge.MSAA;
import org.eclipse.actf.util.win32.HighlightComposite;
import org.eclipse.actf.util.win32.OverlayLabel;
import org.eclipse.actf.visualization.gui.IGuiViewIDs;
import org.eclipse.actf.visualization.gui.internal.util.AccessiblePropertyUtil;
import org.eclipse.actf.visualization.gui.internal.util.GuiImages;
import org.eclipse.actf.visualization.gui.internal.util.Messages;
import org.eclipse.actf.visualization.gui.msaa.checker.MSAAProblem;
import org.eclipse.actf.visualization.gui.msaa.checker.MSAAProblemChecker;
import org.eclipse.actf.visualization.gui.msaa.checker.MSAAProblemConst;
import org.eclipse.actf.visualization.gui.preferences.GuiPreferenceConstants;
import org.eclipse.actf.visualization.gui.preferences.GuiPreferenceManager;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ArmEvent;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;


public class MSAAProblemsView extends ViewPart implements IMSAAProblemsView, MSAAProblemConst {
    private static final String[] HEADINGS = { "", Messages.getString("msaa.description"), "Name", "Role", "State", "X", "Y", "W", "H" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$

    private static final int[] WEIGHTS = { 1, 10, 5, 5, 5, 1, 1, 1, 1 };

    private static final int[] ALIGNMENTS = { SWT.RIGHT, SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT,
            SWT.RIGHT, SWT.RIGHT };

    private TableViewer viewer;

    private MSAAProblemsViewerSorter sorter = new MSAAProblemsViewerSorter();

    private boolean suppressLabelAdjust = false;

    private Action refreshAction;

    private Action showLabelsAction;
    
    private Action showErrorAction, showWarningAction, showInformationAction;
    
    private ProblemFilter filter = new ProblemFilter();

    /**
     * The constructor.
     */
    public MSAAProblemsView() {
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    public void createPartControl(Composite parent) {
        Table table = new Table(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION);
        TableLayout layout = new TableLayout();
        table.setLayout(layout);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        for (int i = 0; i < HEADINGS.length; i++) {
            layout.addColumnData(new ColumnWeightData(WEIGHTS[i]));
            TableColumn tc = new TableColumn(table, SWT.NONE);
            tc.setText(HEADINGS[i]);
            tc.setAlignment(ALIGNMENTS[i]);
            tc.setResizable(true);
            final int newSortingColumn = i + 1;
            tc.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    if (newSortingColumn == sorter.sortingColumn) {
                        sorter.sortingColumn = -newSortingColumn;
                    } else {
                        sorter.sortingColumn = newSortingColumn;
                    }
                    viewer.refresh();
                }
            });
        }

        viewer = new TableViewer(table);
        MSAAProblemsContentAndLabelProvider provider = new MSAAProblemsContentAndLabelProvider();
        viewer.setContentProvider(provider);
        viewer.setLabelProvider(provider);
        viewer.setSorter(sorter);
        viewer.addFilter(filter);
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                ISelection selection = event.getSelection();
                if (selection instanceof IStructuredSelection) {
                    Object selectedElement = ((IStructuredSelection) selection).getFirstElement();
                    if (selectedElement instanceof MSAAProblem) {
                        AccessibleObject object = ((MSAAProblem) selectedElement).getErrorObject();
                        if (null != object) {
                            IMSAAOutlineView outlineView = (IMSAAOutlineView)MSAAViewRegistory.findView(IGuiViewIDs.ID_OUTLINEVIEW);
                            if( null != outlineView ) {
                                outlineView.setSelection(object);
                            }
                            else {
                                HighlightComposite.flashRectangle(object.getAccLocation());
                            }
                        }
                        if (!suppressLabelAdjust) {
                            showOverlayLabels();
                        }
                    }
                }

            }
        });

        makeActions();
        hookContextMenu();
        contributeToActionBars();
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    public void refresh() {
        AccessibleObject rootObject = MSAAViewRegistory.getRootObject(); 
        if (null != rootObject) {
            viewer.setInput(new MSAAProblemChecker(rootObject));
            showOverlayLabels();
        }
    }
    
    private void showOverlayLabels() {
        OverlayLabel.removeAll();
        if( showLabelsAction.isChecked() ) {
            TableItem[] items = viewer.getTable().getItems();
            Object currentItem = ((StructuredSelection)viewer.getSelection()).getFirstElement();
            for( int errorCode = MSAAProblemConst.MSAA_ERROR; errorCode<=MSAA_INFORMATION; errorCode++ ) {
                createOverlayLabels(items, currentItem, errorCode);
            }
            OverlayLabel.setOwnerId(IGuiViewIDs.ID_REPORTVIEW);
        }
    }
    
    private void createOverlayLabels(TableItem[] items, Object currentItem, int errorCategory ) {
        for( int i=0; i<items.length; i++ ) {
            MSAAProblem itemData = (MSAAProblem)items[i].getData();
            if( errorCategory != itemData.getErrorCategory() ) continue;
            AccessibleObject accObject = itemData.getErrorObject();
            Rectangle location = accObject.getAccLocation();
            if( null != location ) {
                OverlayLabel label = OverlayLabel.create(itemData);
                if( itemData.equals(currentItem) ) {
                    label.setForeground(label.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
                    label.setBackground(label.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
                }
                MSAAProblemsContentAndLabelProvider labelProvider = (MSAAProblemsContentAndLabelProvider)viewer.getLabelProvider();
                label.setImage(labelProvider.getColumnImage(itemData,0));
                String text = labelProvider.getColumnText(itemData,1)+"\n"+MSAAOutlineView.getTreeText(accObject); //$NON-NLS-1$
                String[][] properties = AccessiblePropertyUtil.getPropertyStrings(accObject);
                label.setTooltop(text, properties);
                label.pack();
                label.setLocation(location.x,location.y);
                label.addMouseListener(new MouseAdapter(){
                    public void mouseDown(MouseEvent e) {
                        if( e.widget instanceof OverlayLabel ) {
                            OverlayLabel[] labels = OverlayLabel.getLabelsAtPosition(e.display.getCursorLocation());
                            if( labels.length > 0 ) {
                                showSelectionMenu(labels,(OverlayLabel)e.widget);
                            }
                            else {
                                viewer.setSelection(new StructuredSelection(((OverlayLabel)e.widget).associatedObject));
                            }
                        }
                    }
                });
                label.addMouseTrackListener(new MouseTrackAdapter(){
                    public void mouseEnter(MouseEvent e) {
                        if( e.widget instanceof OverlayLabel ) {
                            MSAAProblem problem = (MSAAProblem)((OverlayLabel)e.widget).associatedObject;
                            AccessibleObject object = problem.getErrorObject();
                            if (null != object) {
                                HighlightComposite.flashRectangle(object.getAccLocation());
                            }
                        }
                    }
                    public void mouseExit(MouseEvent e) {
                        HighlightComposite.show(false);
                    }
                });
            }
        }
    }
    
    private void showSelectionMenu(OverlayLabel[] labels, OverlayLabel parent) {
        final Menu popupMenu = parent.createPopupMenu();
        MenuItem[] menuItems = new MenuItem[labels.length];
        for( int i=0; i<labels.length; i++ ) {
            menuItems[i] = new MenuItem(popupMenu,SWT.PUSH);
            menuItems[i].setText(labels[i].getMenuText());
            menuItems[i].setImage(labels[i].getImage());
            menuItems[i].setData(labels[i].associatedObject);
            menuItems[i].addArmListener(new ArmListener() {
                public void widgetArmed(ArmEvent e) {
                    Object data = ((MenuItem)e.getSource()).getData();
                    if( data instanceof MSAAProblem ) {
                        AccessibleObject object = ((MSAAProblem)data).getErrorObject();
                        if( null != object ) {
                            HighlightComposite.flashRectangle(object.getAccLocation());
                        }
                    }
                }
            });
            menuItems[i].addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    Object data = ((MenuItem)e.getSource()).getData();
                    suppressLabelAdjust = true;
                    viewer.setSelection(new StructuredSelection(data));
                    suppressLabelAdjust = false;
                }
            });
        }
        popupMenu.setVisible(true);
    }

    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                MSAAProblemsView.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, viewer);
        getSite().registerContextMenu(menuMgr, new AccessibleObjectSelectionProvider(){
            public Object getSelectedAccessibleObject() {
                return getSelectedItem();
            }
        });
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalPullDown(IMenuManager manager) {
    	manager.add(showErrorAction);
    	manager.add(showWarningAction);
    	manager.add(showInformationAction);
    	manager.add(new Separator());
        manager.add(showLabelsAction);
    }

    private void fillContextMenu(IMenuManager manager) {
        manager.add(refreshAction);
        // Other plug-ins can contribute there actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(refreshAction);
        manager.add(showLabelsAction);
    }

    private void makeActions() {
        final Shell shell = this.getViewSite().getShell();
        refreshAction = new Action(Messages.getString("msaa.refresh")) { //$NON-NLS-1$
            public void run() {
                refresh();
            }
        };
        refreshAction.setToolTipText(Messages.getString("msaa.refresh")); //$NON-NLS-1$
        refreshAction.setImageDescriptor(GuiImages.IMAGE_REFRESH);

        showLabelsAction = new Action(Messages.getString("msaa.show_problem"), Action.AS_CHECK_BOX) { //$NON-NLS-1$
            public void run() {
                showOverlayLabels();
            }
        };
        showLabelsAction.setToolTipText(Messages.getString("msaa.show_problem")); //$NON-NLS-1$
        showLabelsAction.setImageDescriptor(GuiImages.IMAGE_OVERLAY);
        shell.addShellListener(new ShellAdapter(){
            public void shellActivated(ShellEvent e) {
                if( IGuiViewIDs.ID_REPORTVIEW.equals(OverlayLabel.getOwnerId()) ) {
                    showOverlayLabels();
                }
            }
            public void shellDeactivated(ShellEvent e) {
                if( IGuiViewIDs.ID_REPORTVIEW.equals(OverlayLabel.getOwnerId()) ) {
                    OverlayLabel.removeAll(false);
                }
            }
        });
        showLabelsAction.setEnabled(GuiPreferenceManager.getPreferenceBoolean(GuiPreferenceConstants.UseOverlayWindow));
        final IPreferenceStore store = GuiPreferenceManager.getPreferenceStore();
        store.addPropertyChangeListener(new IPropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                if( GuiPreferenceConstants.UseOverlayWindow.equals(event.getProperty())) {
                    showLabelsAction.setEnabled(GuiPreferenceManager.getPreferenceBoolean(GuiPreferenceConstants.UseOverlayWindow));
                }
            }
        });
        
        showErrorAction = new Action(Messages.getString("msaa.showError"), Action.AS_CHECK_BOX) { //$NON-NLS-1$
        	public void run() {
        		filter.categorySelect[MSAA_ERROR] = showErrorAction.isChecked();
        		refresh();
        	}
        };
        showErrorAction.setChecked(filter.categorySelect[MSAA_ERROR]);
        
        showWarningAction = new Action(Messages.getString("msaa.showWarning"), Action.AS_CHECK_BOX) { //$NON-NLS-1$
        	public void run() {
        		filter.categorySelect[MSAA_WARNING] = showWarningAction.isChecked();
        		refresh();
        	}
        };
        showWarningAction.setChecked(filter.categorySelect[MSAA_WARNING]);
        
        showInformationAction = new Action(Messages.getString("msaa.showInformation"), Action.AS_CHECK_BOX) { //$NON-NLS-1$
        	public void run() {
        		filter.categorySelect[MSAA_INFORMATION] = showInformationAction.isChecked();
        		refresh();
        	}
        };
        showInformationAction.setChecked(filter.categorySelect[MSAA_INFORMATION]);
    }

    private Object getSelectedItem() {
        IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
        if (selection.size() == 1) {
            Object element = selection.getFirstElement();
            if( element instanceof MSAAProblem ) {
                return ((MSAAProblem)element).getErrorObject();
            }
        }
        return null;
    }
    
    private class MSAAProblemsContentAndLabelProvider extends LabelProvider implements IStructuredContentProvider,
            IPropertyChangeListener, ITableLabelProvider {
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof MSAAProblemChecker) {
                return ((MSAAProblemChecker) inputElement).getProblems();
            }
            return new Object[0];
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

        public void propertyChange(PropertyChangeEvent event) {
        }

        public Image getColumnImage(Object element, int columnIndex) {
            if (0 == columnIndex && element instanceof MSAAProblem) {
                switch (((MSAAProblem) element).getErrorCategory()) {
                case MSAA_ERROR:
                    return sharedImages.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
                case MSAA_WARNING:
                    return sharedImages.getImage(ISharedImages.IMG_OBJS_WARN_TSK);
                case MSAA_INFORMATION:
                    return sharedImages.getImage(ISharedImages.IMG_OBJS_INFO_TSK);
                }
            }
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            if (element instanceof MSAAProblem) {
                MSAAProblem problem = (MSAAProblem) element;
                AccessibleObject object = problem.getErrorObject();
                switch (columnIndex) {
                case 0:
                    return ""; //$NON-NLS-1$
                case 1:
                    return ((MSAAProblem) element).getErrorDescription();
                case 2:
                    if (null == object)
                        return ""; //$NON-NLS-1$
                    return object.getAccName();
                case 3:
                    if (null == object)
                        return ""; //$NON-NLS-1$
                    return object.getRoleText();
                case 4:
                    if (null == object)
                        return ""; //$NON-NLS-1$
                    return MSAA.getStateText(object.getAccState());
                case 5:
                case 6:
                case 7:
                case 8:
                    if (null == object)
                        return ""; //$NON-NLS-1$
                    Rectangle location = object.getAccLocation();
                    if (null == location)
                        return ""; //$NON-NLS-1$
                    switch (columnIndex - 5) {
                    case 0:
                        return Integer.toString(location.x);
                    case 1:
                        return Integer.toString(location.y);
                    case 2:
                        return Integer.toString(location.width);
                    case 3:
                        return Integer.toString(location.height);
                    }
                default:
                    return "???"; //$NON-NLS-1$
                }
            }
            return null;
        }
    }

    private class MSAAProblemsViewerSorter extends ViewerSorter {
        public int sortingColumn = 0;

        public int compare(Viewer viewer, Object e1, Object e2) {
            if (0 != sortingColumn && viewer instanceof TableViewer) {
                IBaseLabelProvider labelProvider = ((TableViewer) viewer).getLabelProvider();
                if (labelProvider instanceof ITableLabelProvider) {
                    int columnIndex = Math.abs(sortingColumn) - 1;
                    String s1 = ((ITableLabelProvider) labelProvider).getColumnText(e1, columnIndex);
                    String s2 = ((ITableLabelProvider) labelProvider).getColumnText(e2, columnIndex);
                    int result = 0;
                    switch (columnIndex) {
                    case 0:
                        result = ((MSAAProblem) e1).getErrorCategory() - ((MSAAProblem) e2).getErrorCategory();
                        break;
                    default:
                        try {
                            result = Integer.parseInt(s1) - Integer.parseInt(s2);
                            break;
                        } catch (Exception e) {
                        }
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        result = collator.compare(s1, s2);
                        break;
                    }
                    return sortingColumn > 0 ? result : -result;
                }
            }
            return 0;
        }

    }
    
    private class ProblemFilter extends ViewerFilter {

    	public boolean categorySelect[] = {false,true,false,false,false}; 
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if( element instanceof MSAAProblem ) {
				int category = ((MSAAProblem)element).getErrorCategory();
				if( category < categorySelect.length ) {
					return categorySelect[category];
				}
			}
			return true;
		}
    	
    }
}
