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
import org.eclipse.actf.ai.voice.IVoice;
import org.eclipse.actf.ai.voice.IVoiceEventListener;
import org.eclipse.actf.ai.voice.VoiceUtil;
import org.eclipse.actf.visualization.gui.IGuiViewIDs;
import org.eclipse.actf.visualization.gui.internal.GuiPlugin;
import org.eclipse.actf.visualization.gui.internal.util.GuiImages;
import org.eclipse.actf.visualization.gui.internal.util.Messages;
import org.eclipse.actf.visualization.gui.internal.util.ScreenReaderRenderer;
import org.eclipse.actf.visualization.gui.internal.util.TextMap;
import org.eclipse.actf.visualization.gui.preferences.GuiPreferenceConstants;
import org.eclipse.actf.visualization.gui.ui.actions.HideHtmlAction;
import org.eclipse.actf.visualization.gui.ui.actions.RefreshRootAction;
import org.eclipse.actf.visualization.gui.ui.actions.ShowOffscreenAction;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.part.ViewPart;


public class JAWSTextView extends ViewPart implements IJAWSTextView {
    private StyledText text;
	private RefreshRootAction refreshAction;
    private ShowOffscreenAction showOffscreenAction;
    private HideHtmlAction hideHtmlAction;
	private Action speakAction;
	private Action speakAllAction;
	private Action stopAction;
    private Action openPreferencesAction;
    
    private IPreferenceStore preferenceStore = GuiPlugin.getDefault().getPreferenceStore();
    
    private TextMap textMap = new TextMap();
    private Point lastSelection = null;
    private boolean ignoreSelection = false;
	
	public void createPartControl(Composite parent) {
		text = new StyledText(parent,SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL){
            public void invokeAction(int action) {
                super.invokeAction(action);
            }
        };
		text.setEditable(false);
		text.setLayout(new GridLayout());
		text.setLayoutData(new GridData());
        resetFont();
        preferenceStore.addPropertyChangeListener(new IPropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                if( GuiPreferenceConstants.JAWSTextView_Font.equals(event.getProperty())) {
                    resetFont();
                }
            }
        });
		makeActions();
		hookContextMenu();
		contributeToActionBars();
        MSAAViewRegistory.findView(IGuiViewIDs.ID_OUTLINEVIEW);
		refresh();
		text.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
                ScreenReaderRenderer.setBusy(true);
            }
            public void keyReleased(KeyEvent e) {
                ScreenReaderRenderer.setBusy(true);
                if( SWT.CR == e.keyCode ) {
                    doClick(e);
                }
                else if( ' ' == e.keyCode ) {
                    doSelect(e);
                }
                else {
                    checkSelection(e);
                }
            }
		});
        text.setDoubleClickEnabled(false);
		text.addMouseListener(new MouseAdapter(){
			public void mouseUp(MouseEvent e) {
				if( 1 == e.button ) {
					checkSelection(e);
				}
			}
            public void mouseDoubleClick(MouseEvent e) {
				if( 1 == e.button ) {
	                doClick(e);
				}
            }
		});
		
		VoiceUtil.getVoice().setEventListener(new IVoiceEventListener(){
			public void indexReceived(final int index) {
				text.getDisplay().asyncExec(new Runnable(){
					public void run() {
						selectLine(index);
					}});
			}
		});
	}
	
	private void checkSelection(TypedEvent event) {
		Point newSelection = text.getSelection();
		if( !newSelection.equals(lastSelection) ) {
			lastSelection = newSelection;
			AccessibleObject accObject = textMap.getAccessibleObject(newSelection);
			if( null != accObject ) {
		        IMSAAOutlineView outlineView = (IMSAAOutlineView)MSAAViewRegistory.findView(IGuiViewIDs.ID_OUTLINEVIEW);
		        if( null != outlineView ) {
		        	ignoreSelection = true;
		        	try {
			            outlineView.setSelection(accObject);
		        	}
		        	finally {
		        		ignoreSelection = false;
		        	}
		        }
				
			}
		}
	}
    
    private void doClick(TypedEvent event) {
        AccessibleObject accObject = getSelectedItem();
        if( null != accObject ) {
            accObject.doDefaultAction();
        }
    }
    
    private void doSelect(TypedEvent event) {
        AccessibleObject accObject = getSelectedItem();
        if( null != accObject ) {
            accObject.select(MSAA.SELFLAG_TAKEFOCUS);
        }
    }
    
    private AccessibleObject getSelectedItem() {
    	return textMap.getAccessibleObject(text.getSelection());
    }

    private void resetFont() {
        FontData fontData = PreferenceConverter.getFontData(preferenceStore,GuiPreferenceConstants.JAWSTextView_Font);
        text.setFont(new Font(text.getDisplay(), fontData));
    }
    
	public void setFocus() {
		text.setFocus();
	}

	public void setSelection(AccessibleObject object) {
		VoiceUtil.getVoice().stop();
		if( ignoreSelection ) return;
        object = MSAAViewRegistory.adjustSelection(object);
		if( null != object ) {
			Point point = textMap.getPoint(object);
			if( null != point ) {
				if( point.x == point.y ) {
					if( point.x > 0 ) {
						point.x--;
					}
					else if( point.y < text.getCharCount() ) {
						point.y++;
					}
				}
				text.setSelection(point.x,point.y);
			}
		}
	}
	
	public void refresh() {
        AccessibleObject object = MSAAViewRegistory.adjustSelection(null);
        VoiceUtil.getVoice().stop();
        if( null == object ) {
            setText(Messages.msaa_jaws_notarget,SWT.COLOR_DARK_GRAY); 
        }
        else {
            new ScreenReaderRenderer(text,textMap).renderAll(object);
		}
	}
    
    private void setText(String message, int color) {
        text.setText(message);
        StyleRange range = new StyleRange();
        range.foreground = Display.getCurrent().getSystemColor(color);
        range.start = 0;
        range.length = text.getCharCount();
        text.setStyleRange(range);
    }
    
	private void selectLine(int line) {
		if( -1 == line ) {
			Point selection = text.getSelection();
			if( selection.x != selection.y ) {
				text.setSelection(selection.y,selection.y);
			}
			return;
		}
		int start = text.getOffsetAtLine(line);
		int end = start + text.getContent().getLine(line).length();
		text.setSelection(start,end);
	}
	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				JAWSTextView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(text);
		text.setMenu(menu);
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
        manager.add(showOffscreenAction);
		manager.add(new Separator());
    	manager.add(hideHtmlAction);
        manager.addMenuListener(new IMenuListener(){
            public void menuAboutToShow(IMenuManager manager) {
                showOffscreenAction.adjust();
                hideHtmlAction.adjust();
            }
        });
		manager.add(new Separator());
        manager.add(openPreferencesAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		// Other plug-ins can contribute there actions here
		manager.add(speakAction);
		manager.add(new Separator());
		manager.add(speakAllAction);
		manager.add(stopAction);
		manager.add(new Separator());
		manager.add(refreshAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(refreshAction);
		manager.add(speakAllAction);
		manager.add(stopAction);
	}

	private void makeActions() {
		refreshAction = new RefreshRootAction();
        showOffscreenAction = new ShowOffscreenAction();
        hideHtmlAction = new HideHtmlAction();
        
		speakAction = new Action(Messages.msaa_speak_here) { 
			public void run() {
				IVoice voice = VoiceUtil.getVoice();
				try {
					voice.stop();
					int lineCount = text.getLineCount();
					int offset = text.getSelection().x;
					int startLine = text.getLineAtOffset(offset);
					for( int line = startLine; line < lineCount ;line++ ) {
						voice.speak(text.getContent().getLine(line),false,line);
					}
				}
				catch( Exception e) {
				}
			}
		};
		speakAllAction = new Action(Messages.msaa_speak) { 
			public void run() {
				text.setCaretOffset(0);
				speakAction.run();
			}
		};
		speakAllAction.setToolTipText(Messages.msaa_speak_tip); 
		speakAllAction.setImageDescriptor(GuiImages.IMAGE_SPEAK);
		
		stopAction = new Action(Messages.msaa_stop) { 
			public void run() {
				VoiceUtil.getVoice().stop();
				selectLine(-1);
			}
		};
		stopAction.setToolTipText(Messages.msaa_stop_tip); 
		stopAction.setImageDescriptor(GuiImages.IMAGE_STOP);
		
        final Shell shell = this.getViewSite().getShell();
        openPreferencesAction = new Action(Messages.msaa_preferences) { 
        	public void run() {
        		PreferencesUtil
				.createPreferenceDialogOn(shell, "org.eclipse.actf.visualization.gui.preferences.GuiPreferencePage", null, null) //$NON-NLS-1$
        		.open();
        	}
        };
	}
}
