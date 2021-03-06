/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *    Daisuke SATO - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.flash.ui.actions;

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;
import org.eclipse.actf.model.flash.util.FlashMSAAUtil;
import org.eclipse.actf.util.win32.VariantUtil;
import org.eclipse.actf.util.win32.comclutch.IDispatch;
import org.eclipse.actf.visualization.gui.IGuiViewIDs;
import org.eclipse.actf.visualization.gui.ui.views.IFlashDOMView;
import org.eclipse.actf.visualization.gui.ui.views.MSAAViewRegistory;
import org.eclipse.actf.visualization.internal.flash.Messages;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class FlashRectFinder {
	private boolean viewVisible = false;
	private AccessibleObject playerWindow = null;
	private Rectangle msaaRect = null;
	private AccessibleObject targetObject = null;

	public FlashRectFinder(Object object) {
		if (object instanceof AccessibleObject) {
			if (null != MSAAViewRegistory
					.findViewReference(IGuiViewIDs.ID_FLASHDOMVIEW)) {
				viewVisible = null != MSAAViewRegistory.showView(
						IGuiViewIDs.ID_FLASHDOMVIEW, false);
				if (viewVisible) {
					for (AccessibleObject accObject = (AccessibleObject) object; null != accObject; accObject = accObject
							.getCachedParent()) {

						if (FlashMSAAUtil.isFlash(accObject.getPtr())) {
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
				MessageDialog.openInformation(shell, Messages.flash_flash_dom, 
						Messages.flash_error_no_location); 
			} else if (null == playerWindow) {
				MessageDialog.openInformation(shell, Messages.flash_flash_dom, 
						Messages.flash_error_select_flash); 
			} else {
				reCalculateRect();
				IFlashDOMView flashDOMView = (IFlashDOMView) MSAAViewRegistory
						.showView(IGuiViewIDs.ID_FLASHDOMVIEW, true);
				if (null != flashDOMView) {
					Rectangle playerRect = playerWindow.getAccLocation();
					msaaRect.x -= playerRect.x;
					msaaRect.y -= playerRect.y;
					flashDOMView.findRectangle(msaaRect, playerWindow);
				}
			}
		}
	}

	private void reCalculateRect() {
		IDispatch idisp = FlashMSAAUtil.getHtmlElementFromPtr(playerWindow
				.getPtr());
		Variant varFlash = VariantUtil.createVariantFromIDispatchAddress((int) idisp
				.getPtr());
		if (null != varFlash) {
			OleAutomation automation = varFlash.getAutomation();
			if (null != automation) {
				int idAlignMode[] = automation
						.getIDsOfNames(new String[] { "AlignMode" }); //$NON-NLS-1$
				if (null != idAlignMode) {
					AccessibleObject[] siblings = targetObject
							.getCachedParent().getChildren();
					Rectangle[] siblingRects = new Rectangle[siblings.length];
					for (int i = 0; i < siblings.length; i++) {
						if (null != siblings[i]) {
							siblingRects[i] = siblings[i].getAccLocation();
						}
					}
					Variant varAlignMode = automation
							.getProperty(idAlignMode[0]);
					automation.setProperty(idAlignMode[0], new Variant(1 + 4));
					wait(500, 1000);
					Rectangle rect = targetObject.getAccLocation();
					if (rect.equals(msaaRect)) {
						for (int i = 0; i < siblings.length; i++) {
							if (null != siblingRects[i]) {
								rect = siblings[i].getAccLocation();
								if (!rect.equals(siblingRects[i])) {
									msaaRect.x += rect.x - siblingRects[i].x;
									msaaRect.y += rect.y - siblingRects[i].y;
									break;
								}
							}
						}
					} else {
						msaaRect = rect;
					}
					automation.setProperty(idAlignMode[0], varAlignMode);
					wait(500, 1000);
				}
			}
			varFlash.dispose();
		}
	}

	private static void wait(int min, int max) {
		int count = 0;
		int sleep = 1;
		Display display = Display.getCurrent();
		while (count++ < max) {
			if (!display.readAndDispatch()) {
				if (count >= min)
					break;
				if (sleep-- > 0) {
					display.sleep();
				}
			}
		}
		if (count > min) {
			System.out.println("wait count=" + count); //$NON-NLS-1$
		}
	}

	public boolean IsValid() {
		return viewVisible && null != playerWindow && null != msaaRect;
	}
}
