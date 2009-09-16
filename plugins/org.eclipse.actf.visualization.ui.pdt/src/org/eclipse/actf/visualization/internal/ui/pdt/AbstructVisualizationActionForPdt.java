/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.internal.ui.pdt;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.IModelServiceHolder;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.editor.browser.WaitForBrowserReadyHandler;
import org.eclipse.actf.model.ui.util.ModelServiceUtils;
import org.eclipse.actf.ui.util.PlatformUIUtil;
import org.eclipse.actf.ui.util.timer.WaitExecSyncEventHandler;
import org.eclipse.actf.ui.util.timer.WaitExecSyncEventListener;
import org.eclipse.actf.visualization.ui.IVisualizationView;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

abstract class AbstructVisualizationActionForPdt {

	private static final String BROWSER_EDITOR_ID = "org.eclipse.ui.browser.editor"; //$NON-NLS-1$
	private static final String SWT_BROWSER_GET_TEXT_METHOD = "getText"; //$NON-NLS-1$
	private static final String SWT_BROWSER_CLASS = "org.eclipse.swt.browser.Browser"; //$NON-NLS-1$
	private static final String PHPBROWSER_ID = "org.eclipse.debug.ui.PHPBrowserOutput"; //$NON-NLS-1$
	private static final String LISTENER_KEY = "org.eclipse.actf.visualization.internal.ui.pdt.AbstructVisualizationActionForPdt"; //$NON-NLS-1$

	private IModelServiceHolder browserHolder;
	private IWebBrowserACTF browser;
	private String targetHtml = ""; //$NON-NLS-1$
	private File targetHtmlFile = null;
	private IWorkbenchWindow _window;
	private HashMap<String, WaitExecSyncEventListener> eventhandlerHolder = new HashMap<String, WaitExecSyncEventListener>();

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
		this._window = window;
	}

	public void run(IAction action) {

		IModelService modelService = ModelServiceUtils.getActiveModelService();
		if ((modelService != null && modelService != browser)) {
			visualize();
			return;
		}

		IEditorPart editor = PlatformUIUtil.getActiveEditor();
		if (editor != null
				&& BROWSER_EDITOR_ID.equals(editor.getEditorSite().getId())) {
			visualize();
			return;
		}

		boolean updated = false;
		String text = getText(_window.getActivePage());
		if (text != null && !text.equals(targetHtml)) {
			updated = true;
			targetHtml = text;
			if(targetHtmlFile!=null){
				targetHtmlFile.delete();
			}
			targetHtmlFile = saveToFile(targetHtml);
		}

		for (IEditorReference editorRef : _window.getActivePage()
				.getEditorReferences()) {
			if (editorRef.getEditor(false) == browserHolder) {
				if (updated) {
					browser.navigate(targetHtmlFile.getAbsolutePath());
					waitAndVisualize();
					return;
				} else {
					_window.getActivePage().activate(
							browserHolder.getEditorPart());
					visualize();
					return;
				}
			}
		}

		if (text != null) {
			ModelServiceUtils.launch(targetHtmlFile.getAbsolutePath());
			browser = (IWebBrowserACTF) ModelServiceUtils
					.getActiveModelService();
			browserHolder = ModelServiceUtils.getActiveModelServiceHolder();
			waitAndVisualize();
			return;
		}

	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

	private void visualize() {
		try {
			IViewPart viewPart = this._window.getActivePage().showView(
					getTargetViewId());
			if (viewPart instanceof IVisualizationView) {
				((IVisualizationView) viewPart).doVisualize();
			}
		} catch (PartInitException pie) {
		}
	}

	private File saveToFile(String text) {
		File tmpFile = null;
		try {
			tmpFile = PdtVisualizationPlugin.getDefault().createTempFile("php", //$NON-NLS-1$
					".html"); //$NON-NLS-1$
			PrintWriter pw = new PrintWriter(tmpFile, "UTF-8"); //$NON-NLS-1$
			pw.print(text);
			pw.flush();
			pw.close();
		} catch (Exception e) {
		}
		return tmpFile;
	}

	private void waitAndVisualize() {

		if (browser != null) {
			IViewPart viewPart = null;
			try {
				viewPart = this._window.getActivePage().showView(
						getTargetViewId());
			} catch (PartInitException pie) {
			}
			if (viewPart instanceof IVisualizationView) {
				final IVisualizationView targetView = (IVisualizationView) viewPart;
				WaitExecSyncEventHandler handler = new WaitForBrowserReadyHandler(
						browser, 30, false, new Runnable() {
							public void run() {
								eventhandlerHolder.remove(LISTENER_KEY);
								targetView.doVisualize();
								PlatformUIUtil.showView(getTargetViewId());
							}
						});
				eventhandlerHolder.put(LISTENER_KEY,
						new WaitExecSyncEventListener(handler));
			}
		}
	}

	private String getText(IWorkbenchPage activePage) {
		if (activePage == null) {
			return null;
		}
		for (IViewReference viewRef : activePage.getViewReferences()) {
			if (PHPBROWSER_ID.equals(viewRef.getId())) {
				IViewPart[] views = activePage.getViewStack(viewRef
						.getView(false));
				try {
					for (IViewPart viewpart : views) {
						if (PHPBROWSER_ID
								.equals(viewpart.getViewSite().getId())) {
							Field[] fields = viewpart.getClass()
									.getDeclaredFields();
							for (Field field : fields) {
								if (SWT_BROWSER_CLASS.equals(field.getType()
										.getName())) {
									field.setAccessible(true);
									Object view = field.get(viewpart);

									Method getText = view.getClass().getMethod(
											SWT_BROWSER_GET_TEXT_METHOD,
											new Class[] {});
									String text = (String) getText.invoke(view,
											new Object[] {});

									field.setAccessible(false);
									return (text);
								}
							}
						}
					}
				} catch (Exception e) {
				}
			}
		}
		return null;
	}

	abstract String getTargetViewId();

}