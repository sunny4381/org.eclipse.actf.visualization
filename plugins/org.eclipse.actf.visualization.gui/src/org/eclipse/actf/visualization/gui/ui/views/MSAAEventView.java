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


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;
import org.eclipse.actf.accservice.swtbridge.AccessibleObjectFactory;
import org.eclipse.actf.accservice.swtbridge.IA2;
import org.eclipse.actf.accservice.swtbridge.MSAA;
import org.eclipse.actf.accservice.swtbridge.event.IAccessibleEventListener;
import org.eclipse.actf.accservice.swtbridge.event.IAccessibleEventMonitor;
import org.eclipse.actf.accservice.swtbridge.ia2.Accessible2;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleText;
import org.eclipse.actf.accservice.swtbridge.ia2.TextSegment;
import org.eclipse.actf.ai.voice.VoicePlugin;
import org.eclipse.actf.util.win32.WindowUtil;
import org.eclipse.actf.visualization.gui.GuiPlugin;
import org.eclipse.actf.visualization.gui.Messages;
import org.eclipse.actf.visualization.gui.preferences.GuiPreferenceConstants;
import org.eclipse.actf.visualization.gui.util.ScreenReaderRenderer;
import org.eclipse.actf.visualization.gui.util.TTSMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.part.ViewPart;



public class MSAAEventView extends ViewPart implements IMSAAEventView, IAccessibleEventListener {
    public static final String ID = MSAAEventView.class.getName();

	private StyledText text;
	private Action clearAction;
	private Action speakEventAction;
    private Action openPreferencesAction;
    private Action eventFilterAction;
    private Action showTTSEventAction;
	
    private boolean showRawEvent = false;

    private IPreferenceStore preferenceStore = GuiPlugin.getDefault().getPreferenceStore();
    
    private static final int[] SUPPORTED_EVENTS = new int[] {
               0x001, 0x002, 0x003, 0x004, 0x005, 0x006, 0x007, 0x008, 0x009, 0x00A, 0x00B, 0x00C, 0x00D, 0x00E, 0x00F,
        0x010, 0x011, 0x012, 0x013, 0x014, 0x015, 0x016, 0x017,
        0x8000, 0x8001, 0x8002, 0x8003, 0x8004, 0x8005, 0x8006, 0x8007, 0x8008, 0x8009, 0x800A, 0x800B, 0x800C, 0x800D, 0x800E, 0x800F,
        0x8010, 0x8011, 0x8012,
               0x101, 0x102, 0x103, 0x104, 0x105, 0x106, 0x107, 0x108, 0x109, 0x10A, 0x10B, 0x10C, 0x10D, 0x10E, 0x10F,
        0x110, 0x111, 0x112, 0x113, 0x114, 0x115, 0x116, 0x117, 0x118, 0x119, 0x11A, 0x11B, 0x11C, 0x11D, 0x11E, 0x11F,
        0x120, 0x121, 0x122
    };
    
    private static final int[] DEFAULT_EVENTS = new int[] {
    	MSAA.EVENT_OBJECT_FOCUS,
    };

    private IAccessibleEventMonitor monitor = null;

    /**
	 * The constructor.
	 */
	public MSAAEventView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		text = new StyledText(parent,SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		text.setEditable(false);
		text.setLayout(new GridLayout());
		text.setLayoutData(new GridData());
        resetFont();
        preferenceStore.addPropertyChangeListener(new IPropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                if( GuiPreferenceConstants.MSAAEventView_Font.equals(event.getProperty())) {
                    resetFont();
                }
            }
        });
		makeActions();
		hookContextMenu();
		contributeToActionBars();
        reset();
        TTSMonitor.startThread(this);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				MSAAEventView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(text);
		text.setMenu(menu);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
        manager.add(eventFilterAction);
//        manager.add(showTTSEventAction);  // TODO: Tempolary disabled for alphaWorks. To be restored for ACTF
        manager.add(new Separator());
		manager.add(speakEventAction);
		manager.add(new Separator());
        manager.add(openPreferencesAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(clearAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(clearAction);
	}

	private void makeActions() {
		clearAction = new Action() {
			public void run() {
				text.setText(""); //$NON-NLS-1$
			}
		};
		clearAction.setText(Messages.getString("msaa.clear")); //$NON-NLS-1$
		clearAction.setToolTipText(Messages.getString("msaa.clear_tip")); //$NON-NLS-1$
		clearAction.setImageDescriptor(GuiPlugin.IMAGE_CLEAR);
		
		speakEventAction = new Action(Messages.getString("msaa.speak_event"),Action.AS_CHECK_BOX) { //$NON-NLS-1$
			public void run() {
			}
		};
		
        final Shell shell = this.getViewSite().getShell();
        openPreferencesAction = new Action(Messages.getString("msaa.preferences")) { //$NON-NLS-1$
        	public void run() {
        		PreferencesUtil
				.createPreferenceDialogOn(shell, "org.eclipse.actf.visualization.gui.preferences.GuiPreferencePage", null, null) //$NON-NLS-1$
        		.open();
        	}
        };
        
		showTTSEventAction = new Action(Messages.getString("msaa.showTTSEvents"),Action.AS_CHECK_BOX) { //$NON-NLS-1$
			public void run() {
			}
		};
		showTTSEventAction.setChecked(true);
        
        eventFilterAction = new Action(Messages.getString("msaa.filters_menu")) { //$NON-NLS-1$
            public void run() {
                EventFiltersDialog dialog = new EventFiltersDialog(shell,intArrayToObjectArray(SUPPORTED_EVENTS),showRawEvent);
                if( Window.OK == dialog.open() ) {
                    filters.clear();
                    Object[] result= dialog.getResult();
                    for( int i=0; i<result.length; i++ ) {
                        filters.put(result[i],Boolean.TRUE);
                    }
                    showRawEvent = dialog.getShowDetails();
                    if( null != monitor ) {
                        monitor.installEventHook(MSAAEventView.this, getFilter());
                    }
                }
            }
        };
	}
    
    class EventFiltersDialog extends ListSelectionDialog {
        private boolean showDetails;
        private Button showDetailsButton;
        
        public EventFiltersDialog(Shell parentShell, Integer[] input, boolean showDetails) {
            super(parentShell, input, new EventFiltersContentProvider(), new EventFiltersLabelProvider(), Messages.getString("msaa.filters_message")); //$NON-NLS-1$
            setTitle(Messages.getString("msaa.filters_title")); //$NON-NLS-1$
            List<Integer> initialSelection = new ArrayList<Integer>();
            for( int i=0; i<input.length; i++ ) {
                if( getFilterEnabled(input[i].intValue()) ) {
                    initialSelection.add(input[i]);
                }
            }
            setInitialElementSelections(initialSelection);
            this.showDetails = showDetails;
        }
        protected Control createDialogArea(Composite parent) {
            Composite composite = (Composite)super.createDialogArea(parent);
            Control[] children = composite.getChildren();
            Composite buttonComposite = (Composite)children[children.length-1];
            Button defaultButton = new Button(buttonComposite,SWT.PUSH);
            defaultButton.setText(Messages.getString("msaa.filters_default")); //$NON-NLS-1$
            defaultButton.addSelectionListener(new SelectionAdapter(){
                public void widgetSelected(SelectionEvent e) {
                    getViewer().setCheckedElements(intArrayToObjectArray(DEFAULT_EVENTS));
                    showDetailsButton.setSelection(false);
                }
            });
            showDetailsButton = new Button(composite,SWT.CHECK);
            showDetailsButton.setText(Messages.getString("msaa.showRawEvents")); //$NON-NLS-1$
            showDetailsButton.setSelection(showDetails);
            showDetailsButton.setFont(parent.getFont());
            // Adjust button layout
            showDetailsButton.moveAbove(buttonComposite);
            ((GridLayout)buttonComposite.getLayout()).numColumns++;
            defaultButton.moveAbove(null);
            return composite;
        }
        protected void okPressed() {
            showDetails = showDetailsButton.getSelection();
            super.okPressed();
        }
        public boolean getShowDetails() {
            return showDetails;
        }
    }
    
    class EventFiltersContentProvider implements IStructuredContentProvider {
        public Object[] getElements(Object input) {
            if( input instanceof Integer[] ) {
                return (Object[])input;
            }
            return new Object[0];
        }
        public void inputChanged(Viewer viewer, Object a, Object b) {}
        public void dispose() {}
    };
    
    class EventFiltersLabelProvider extends LabelProvider {
        public String getText(Object element) {
            if( element instanceof Integer ) {
                return MSAA.getEventTypeText(((Integer)element).intValue());
            }
            return  null;
        }
    };
    
    private Integer[] intArrayToObjectArray(int[] intArray) {
        Integer[] objArray = new Integer[intArray.length];
        for( int i=0; i<intArray.length; i++ ) {
            objArray[i] = new Integer(intArray[i]);
        }
        return objArray;
    }

    private void resetFont() {
        FontData fontData = PreferenceConverter.getFontData(preferenceStore,GuiPreferenceConstants.MSAAEventView_Font);
        text.setFont(new Font(text.getDisplay(), fontData));
    }
    
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		text.setFocus();
	}

	public void dispose() {
		super.dispose();
        TTSMonitor.stopThread();
        if( null != monitor ) {
            monitor.removeEventHook();
            monitor = null;
        }
	}
	
	
	private void showEventInfo(int event, int hwnd, int idObject, int idChild, AccessibleObject accObject) {
    	if( text.isDisposed() || text.isFocusControl() ) {
    		return;
    	}
		boolean release = false;
    	if( null == accObject) {
    		accObject = AccessibleObjectFactory.getAccessibleObjectFromEvent(hwnd,idObject,idChild);
    		release = true;
    	}
		if( null!=accObject ) {
			int accRole = accObject.getAccRole();
			int accState = accObject.getAccState();
			boolean ignore = 0!=(accState & MSAA.STATE_INVISIBLE);
            if( !ignore ) {
                switch( event ) {
                    case MSAA.EVENT_OBJECT_FOCUS:
                        Accessible2 accExt = accObject.getAccessible2();
                        if( null != accExt ) {
                            IMSAAOutlineView outlineView = (IMSAAOutlineView)MSAAViewRegistory.findView(MSAAViewRegistory.MSAAOutlineView_ID);
                            if( null != outlineView ) {
                                AccessibleObject accTarget = getCachedChildFromUID(MSAAViewRegistory.rootObject,accExt.getUniqueID());
                                outlineView.setSelection(accTarget);
                                if( null == accTarget ) {
                                	release = !MSAAViewRegistory.showProperties(accObject);
                                }
                            }
                        }
                        break;
                    case MSAA.EVENT_OBJECT_NAMECHANGE:
                        break;
                    case MSAA.EVENT_OBJECT_STATECHANGE:
                    case MSAA.EVENT_OBJECT_VALUECHANGE:
                        ignore = 0==(MSAA.STATE_FOCUSED&accState);
                        break;
                    case MSAA.EVENT_OBJECT_CREATE:
                        // For refresh IE
                        if( MSAA.ROLE_SYSTEM_PANE==accRole ) {
                            if( validate(MSAAViewRegistory.rootObject,5,10) ) {
                                break;  // Skip banner window
                            }
                            if( delayedRefresh(2000) ) {
                                break;
                            }
                        }
                        ignore = true;
                        break;
                }
            }
			if( !ignore && getFilterEnabled(event) ) {
				checkInterval();
				int buttonIndex = -1;
				if( MSAA.ROLE_SYSTEM_PUSHBUTTON == accRole ) {
					buttonIndex = getChildIndex(accObject);
				}
				if( showRawEvent/*Action.isChecked()*/ ) {
					text.append("event="+MSAA.getEventTypeText(event)		+",  "+ //$NON-NLS-1$ //$NON-NLS-2$
							"accName="+accObject.getAccName()		+",  "+ //$NON-NLS-1$ //$NON-NLS-2$
							"accRole="+accObject.getRoleText()		+",  "+ //$NON-NLS-1$ //$NON-NLS-2$
							"accState="+MSAA.getStateText(accState)	+"(0x"+Integer.toHexString(accState)+")\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				String str = null;
                boolean doRender = true;
                switch( event ) {
                    case IA2.IA2_EVENT_TEXT_CARET_MOVED:
                        AccessibleText accText = accObject.getAccessibleText();
                        if( null != accText ) {
                            Point selection = accText.getSelection(0);
                            if( null != selection ) {
                                str = accText.getTextRange(selection.x,selection.y);
                            }
                            else {
                                TextSegment ts = accText.getTextAtIndex(accText.getCaretPosition(),IA2.IA2_TEXT_BOUNDARY_CHAR);
                                if( null != ts ) {
                                    str = ts.text;
                                }
                            }
                            if( null != str ) {
                                text.append(" "); //$NON-NLS-1$
                                StyleRange range = new StyleRange();
                                range.background = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
                                range.start = text.getCharCount();
                                range.length = str.length();
                                text.append(str);
                                text.setStyleRange(range);
                            }
                        }
                        doRender = false;
                        break;
                    case IA2.IA2_EVENT_TEXT_SELECTION_CHANGED:
                        doRender = false;
                        break;
                }
                if( doRender ) {
                    ScreenReaderRenderer renderer = new ScreenReaderRenderer(text,null);
                    str = renderer.renderEvent(event);
                    if( 0==str.length() ) {
                        str = renderer.renderItem(accObject,false,buttonIndex);
                    }
                }
				text.setSelection(text.getCharCount());
                if( speakEventAction.isChecked() ) {
                    if( null != str && str.length() > 0 ) {
                        VoicePlugin.getVoice().speak(str,true);
                    }
                }
			}
			if( release ) {
				AccessibleObject parent = accObject.getCachedParent();
				try {
					if( null != parent ) {
						parent.dispose();
					}
					else {
						accObject.dispose();
					}
				}
				catch( Exception e ) {
					e.printStackTrace();
				}
			}
		}
	}
    
    private boolean refreshing = false;
    private boolean delayedRefresh(int delayMS) {
        if( refreshing ) {
            return false;
        }
        refreshing = true;
        text.getDisplay().timerExec(delayMS,new Runnable(){
            public void run() {
                try {
                    if( !text.isDisposed() ) {
                        MSAAViewRegistory.refreshRootObject();
                    }
                }
                finally {
                    refreshing = false;
                }
            }
        });
        return true;
    }

	private Map filters = getDefaultFilters();
	
	private Map getDefaultFilters() {
		Map defaultFilters = new HashMap();
		for( int i=0; i<DEFAULT_EVENTS.length; i++ ) {
			defaultFilters.put(new Integer(DEFAULT_EVENTS[i]), Boolean.TRUE);
		}
		return defaultFilters;
	}
	
	public boolean getFilterEnabled(int event) {
		Integer key = new Integer(event);
		
		Boolean enabled = (Boolean)filters.get(key);
		if( null == enabled ) {
			enabled = Boolean.FALSE;
			filters.put(key, enabled);
		}
		return enabled.booleanValue();
	}
	
	public void setTTSText(final String speakText) {
        if( null != speakText && speakText.length() > 0 ) {
            text.getDisplay().asyncExec(new Runnable(){
                public void run() {
                	if( text.isFocusControl() || !showTTSEventAction.isChecked() ) {
                		return;
                	}
                	checkInterval();
                    StyleRange range = new StyleRange();
                    range.foreground = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN);
                    range.start = text.getCharCount();
                    range.length = speakText.length();
                    text.append(speakText);
                    text.setStyleRange(range);
                    text.setSelection(text.getCharCount());
                    if( speakEventAction.isChecked() ) {
                        VoicePlugin.getVoice().speak(speakText,true);
                    }
                }
            });
        }
    }
    
    private long lastTime = 0;
    private void checkInterval() {
    	long currentTime = System.currentTimeMillis();
    	if( 0 != lastTime ) {
        	try {
        		long passed = Math.abs(currentTime - lastTime);
        		if( passed > 1000 ) {
        			String separatorString = MessageFormat.format(Messages.getString("msaa.ns_passed"),new Object[]{new Float(((float)passed)/1000)}); //$NON-NLS-1$
                    StyleRange range = new StyleRange();
                    range.foreground = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
                    range.start = text.getCharCount();
        			int lastPos = range.start-1;
        			if( lastPos > 0 && !"\n".equals(text.getText(lastPos,lastPos)) ) { //$NON-NLS-1$
    					text.append("\n"); //$NON-NLS-1$
        			}
                    text.append(separatorString);
    				text.append("\n"); //$NON-NLS-1$
                    range.length = text.getCharCount()-range.start;
                    text.setStyleRange(range);
        		}
        	}
        	catch( Exception e ) {
        		e.printStackTrace();
        	}
    	}
    	lastTime = currentTime;
    }
   
	public void handleEvent(final int event, final int hwnd, final int idObject, final int idChild, final AccessibleObject accObject) {
		if( checkShowInfo(event, hwnd) ) {
            text.getDisplay().asyncExec(new Runnable(){
                public void run() {
                    showEventInfo(event,hwnd,idObject,idChild,accObject);
                }
            });
		}
	}

    private Set getFilter() {
    	Set enabledSet = new HashSet();
    	for( Iterator it = filters.keySet().iterator(); it.hasNext(); ) {
    		Object key = it.next();
    		Boolean enabled = (Boolean)filters.get(key);
    		if( null != enabled && enabled.booleanValue() ) {
        		enabledSet.add(key);
    		}
    	}
    	enabledSet.add(new Integer(MSAA.EVENT_OBJECT_CREATE));	// For refresh IE
    	return enabledSet;
    }

	private boolean checkShowInfo(int event, int hwnd) {
		boolean showInfo = false;
		if( 0 == MSAAViewRegistory.getUpdateRef() && null != MSAAViewRegistory.rootObject ) {
			if( null != MSAA.getEventTypeText(event) ) {
				int hwndRoot = MSAAViewRegistory.rootObject.getWindow();
				if( 0 !=hwndRoot ) {
                    showInfo = MSAA.EVENT_OBJECT_FOCUS==event && WindowUtil.isPopupMenu(hwnd); 
                    for( int hwndParent = hwnd; !(showInfo || 0==hwndParent); hwndParent = WindowUtil.GetParentWindow(hwndParent) ) {
                        showInfo = (hwndRoot == hwndParent);
                    }
				}
			}
		}
        return showInfo;
	}

    private static AccessibleObject getCachedChildFromUID(AccessibleObject root,int uid) {
    	Accessible2 accessible2 = root.getAccessible2();
    	if( null != accessible2 && uid == accessible2.getUniqueID() ) {
            return root;
    	}
    	AccessibleObject cachedChildren[] = root.getCachedChildren();
        for( int i=0; i<cachedChildren.length; i++ ) {
            AccessibleObject accObj = getCachedChildFromUID(cachedChildren[i],uid);
            if( null != accObj ) {
                return accObj;
            }
        }
        return null;
    }
    
    /**
     * Get index number (for JAWS simulation)
     * @return
     */
    private static int getChildIndex(AccessibleObject accObject) {
        AccessibleObject disposeLater = null; 
        try {
            AccessibleObject parent = accObject.getCachedParent();
            if( null==parent ) {
            	parent = disposeLater = accObject.getAccParent();
            	if( null==parent ) return 0;
            }
            AccessibleObject[] children = parent.getChildren();
            Rectangle myRect = accObject.getAccLocation();
            if( null != myRect ) {
                for( int i=0; i<children.length; i++ ) {
                    if( accObject.equals(children[i]) ||
                        myRect.equals(children[i].getAccLocation()) ) {
                        return i;
                    }
                }
            }
        }
        finally {
            if( null != disposeLater ) {
                try {
                    disposeLater.dispose();
                }
                catch( Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    private static boolean validate(AccessibleObject accValidate, int depth, int bound) {
        if (null == accValidate.getAccName() &&
            null == accValidate.getAccLocation() &&
            null == accValidate.getAccKeyboardShortcut()) {
            return false;
        }
        AccessibleObject parent = accValidate.getCachedParent();
        AccessibleObject[] children = accValidate.getCachedChildren();
        if( null==parent && children.length==0 ) {
            return false;
        }
        if (depth-- > 0) {
            int max = children.length;
            int len = Math.min(max,bound);
            for (int i = 0; i < len; i++) {
                AccessibleObject ao = children[i]; 
                if (null!=ao && !validate(ao,depth,bound)) {
                    return false;
                }
            }
            len = max - len;
            if( len > 0 ) {
                for (int i = 0; i < len; i++) {
                    AccessibleObject ao = children[max-1-i]; 
                    if (null!=ao && !validate(ao,depth,bound)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void reset() {
        if( null != monitor ) {
            monitor.removeEventHook();
            monitor = null;
        }
        monitor = AccessibleObjectFactory.getAccessibleEventMonitor();
        if( null != monitor ) {
            monitor.installEventHook(this, getFilter());
        }
    }
}
