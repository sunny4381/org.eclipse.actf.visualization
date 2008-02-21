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
package org.eclipse.actf.visualization.flash.ui.actions;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


import org.eclipse.actf.accservice.swtbridge.AccessibleObject;
import org.eclipse.actf.visualization.flash.Messages;
import org.eclipse.actf.visualization.gui.common.WebBrowserUtil;
import org.eclipse.actf.visualization.gui.flash.FlashUtil;
import org.eclipse.actf.visualization.gui.ui.views.IFlashDOMView;
import org.eclipse.actf.visualization.gui.ui.views.MSAAViewRegistory;

public class FlashRectFinder {
	private boolean viewVisible = false;
    private AccessibleObject playerWindow = null;
    private Rectangle msaaRect = null;
    private AccessibleObject targetObject = null;
    
	public FlashRectFinder(Object object) {
        if (object instanceof AccessibleObject ) {
        	if( null != MSAAViewRegistory.findViewReference(MSAAViewRegistory.FlashDOMView_ID) ) {
            	viewVisible = null != MSAAViewRegistory.showView(MSAAViewRegistory.FlashDOMView_ID,false);
            	if( viewVisible ) {
                    for (AccessibleObject accObject = (AccessibleObject) object; null != accObject; accObject = accObject.getCachedParent()) {
        		        if (FlashUtil.isFlash(accObject)) {
        		            playerWindow = accObject;
        		        }
        		        if (null == msaaRect) {
        		            msaaRect = accObject.getAccLocation();
        		            targetObject = accObject;
        		        }
        		    }
            	}
        	}
        }
	}
	
	public void find(Shell shell) {
        if (viewVisible) {
            if (null == msaaRect) {
                MessageDialog.openInformation(shell, Messages.getString("flash.flash_dom"), //$NON-NLS-1$
                        Messages.getString("flash.error_no_location")); //$NON-NLS-1$
            }
            else if (null == playerWindow) {
                MessageDialog.openInformation(shell, Messages.getString("flash.flash_dom"), //$NON-NLS-1$
                        Messages.getString("flash.error_select_flash")); //$NON-NLS-1$
            }
            else {
            	reCalculateRect();
            	IFlashDOMView flashDOMView = (IFlashDOMView) MSAAViewRegistory.showView(MSAAViewRegistory.FlashDOMView_ID, true);
                if( null != flashDOMView ) {
                    Rectangle playerRect = playerWindow.getAccLocation();
                    msaaRect.x -= playerRect.x;
                    msaaRect.y -= playerRect.y;
                    flashDOMView.findRectangle(msaaRect, playerWindow);
                }
            }
        }
	}
	
	private void reCalculateRect() {
        Variant varFlash = WebBrowserUtil.getHTMLElementFromObject(playerWindow);
        if( null != varFlash ) {
        	OleAutomation automation = varFlash.getAutomation();
        	if( null != automation ) {
        		int idAlignMode[] = automation.getIDsOfNames(new String[]{"AlignMode"}); //$NON-NLS-1$
        		if( null != idAlignMode ) {
        			AccessibleObject[] siblings = targetObject.getCachedParent().getChildren();
        			Rectangle[] siblingRects = new Rectangle[siblings.length];
        			for( int i=0; i<siblings.length; i++ ) {
        				if( null != siblings[i] ) {
        					siblingRects[i] = siblings[i].getAccLocation();
        				}
        			}
            		Variant varAlignMode = automation.getProperty(idAlignMode[0]);
            		automation.setProperty(idAlignMode[0], new Variant(1+4));
            		wait(500,1000);
            		Rectangle rect = targetObject.getAccLocation();
            		if( rect.equals(msaaRect) ) {
                		for( int i=0; i<siblings.length; i++ ) {
            				if( null != siblingRects[i] ) {
            					rect = siblings[i].getAccLocation();
            					if( !rect.equals(siblingRects[i]) ) {
            						msaaRect.x += rect.x-siblingRects[i].x;
            						msaaRect.y += rect.y-siblingRects[i].y;
            						break;
            					}
            				}
                		}
            		}
            		else {
                		msaaRect = rect;
            		}
            		automation.setProperty(idAlignMode[0], varAlignMode);
            		wait(500,1000);
        		}
        	}
        	varFlash.dispose();
        }
	}
	private static void wait(int min, int max) {
		int count = 0;
		int sleep = 1;
		Display display = Display.getCurrent();
		while( count++ < max ) {
			if( !display.readAndDispatch() ) {
				if( count >= min ) break;
				if( sleep-- > 0 ) {
					display.sleep();
				}
			}
		}
		if( count > min ) {
			System.out.println("wait count="+count); //$NON-NLS-1$
		}
	}
	
	public boolean IsValid() {
		return viewVisible && null!=playerWindow && null != msaaRect;
	}
}
