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

package org.eclipse.actf.visualization.gui.targetdata;

import java.io.File;

import org.eclipse.actf.model.IModelService;
import org.eclipse.actf.model.IModelServiceScrollManager;
import org.eclipse.actf.model.ui.editor.ImagePositionInfo;
import org.eclipse.actf.util.win32.WindowUtil;
import org.eclipse.actf.visualization.gui.Messages;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Node;




public class GuiTargetWindowData implements IModelService {

    private static final String categoryBrowser = Messages.getString("msaa.external_browsers"); //$NON-NLS-1$
    private static final String categoryWindow = Messages.getString("msaa.external_windows"); //$NON-NLS-1$
    private int hwnd;
    private boolean isBrowser;
    
    public GuiTargetWindowData(int hwnd, boolean isBrowser) {
        this.hwnd = hwnd;
        this.isBrowser = isBrowser;
    }

    /**
     * Get category string for menu bar
     * @return
     */
    public String getCategory() {
    	return isBrowser ? categoryBrowser : categoryWindow; 
    }

    /**
     * Check if this data source is Web browser
     * @return
     */
    public boolean isBrowser() {
        return isBrowser;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.model.IModelService#getTitle()
     */
    public String getTitle() {
        if( 0 != hwnd ) {
            String title = WindowUtil.GetWindowText(hwnd);
            if( title.length()==0 ) {
                title = Messages.getString("msaa.NAMELESS"); //$NON-NLS-1$
            }
            return title + " ("+WindowUtil.GetWindowClassName(hwnd)+")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.actf.model.IModelService#getID()
     */
    public String getID() {
        return getCategory()+"/"+Integer.toString(hwnd); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.model.IModelService#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name) {
    	if( IModelService.ATTR_WINDOWHANDLE.equals(name) ) {
    		return new Integer(hwnd);
    	}
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.model.IModelService#getCurrentMIMEType()
     */
    public String getCurrentMIMEType() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.model.IModelService#getDocument()
     */
    public Document getDocument() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.model.IModelService#getLiveDocument()
     */
    public Document getLiveDocument() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.model.IModelService#getScrollManager()
     */
    public IModelServiceScrollManager getScrollManager() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.model.IModelService#getSupportExtensions()
     */
    public String[] getSupportExtensions() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.model.IModelService#getSupportMIMETypes()
     */
    public String[] getSupportMIMETypes() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.model.IModelService#getTargetComposite()
     */
    public Composite getTargetComposite() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.model.IModelService#getURL()
     */
    public String getURL() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.model.IModelService#jumpToNode(org.w3c.dom.Node)
     */
    public void jumpToNode(Node target) {
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.model.IModelService#open(java.io.File)
     */
    public void open(File target) {
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.model.IModelService#open(java.lang.String)
     */
    public void open(String url) {
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.model.IModelService#saveDocumentAsHTMLFile(java.lang.String)
     */
    public File saveDocumentAsHTMLFile(String file) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.actf.model.IModelService#setScrollbarWidth(int)
     */
    public void setScrollbarWidth(int width) {
    }

	public File saveOriginalDocument(String file) {
		return null;
	}

	public ImagePositionInfo[] getAllImagePosition() {
		return new ImagePositionInfo[0];
	}
    

}
