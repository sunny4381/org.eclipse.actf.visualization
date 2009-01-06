/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *    Shin SAITO - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.flash.ui.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;
import org.eclipse.actf.model.flash.ASAccInfo;
import org.eclipse.actf.model.flash.FlashPlayerFactory;
import org.eclipse.actf.model.flash.IASNode;
import org.eclipse.actf.model.flash.IFlashConst;
import org.eclipse.actf.model.flash.IFlashPlayer;
import org.eclipse.actf.model.flash.util.FlashDetect;
import org.eclipse.actf.model.flash.util.FlashMSAAUtil;
import org.eclipse.actf.visualization.flash.ui.properties.FlashNodePropertySource;
import org.eclipse.actf.visualization.gui.IGuiViewIDs;
import org.eclipse.actf.visualization.gui.internal.util.GuiImages;
import org.eclipse.actf.visualization.gui.ui.actions.RefreshRootAction;
import org.eclipse.actf.visualization.gui.ui.views.IFlashDOMView;
import org.eclipse.actf.visualization.gui.ui.views.MSAAViewRegistory;
import org.eclipse.actf.visualization.internal.flash.FlashImages;
import org.eclipse.actf.visualization.internal.flash.Messages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

import com.ibm.icu.text.MessageFormat;

public class FlashDOMView extends ViewPart implements IFlashDOMView,
		IFlashConst {
	public static final String ID = FlashDOMView.class.getName();

	private static final String ON_RELEASE = "onRelease";

	private TreeViewer viewer;
	private Action expandAction;
	private Action expandAllAction;
	private Action collapseAllAction;
	private RefreshRootAction refreshAction;
	private Action informativeTreeAction;
	private Action visualTreeAction;
	private Action debugTreeAction;
	private Action scanWindowlessAction;
	private Color colorFound = Display.getCurrent().getSystemColor(
			SWT.COLOR_CYAN);

	private boolean debugMode = false;

	/**
	 * The constructor.
	 */
	public FlashDOMView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new FlashTreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new FlashDOMContentProvider());
		viewer.setLabelProvider(new FlashDOMLabelProvider());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					Object selectedItem = ((IStructuredSelection) selection)
							.getFirstElement();
					if (selectedItem instanceof IASNode) {
						((IASNode) selectedItem).setMarker();
					} else if (!(selectedItem instanceof IFlashPlayer)) {
						return;
					}
					MSAAViewRegistory.showProperties(selectedItem);
				}
			}
		});
		makeActions();
		hookContextMenu();
		contributeToActionBars();
		MSAAViewRegistory.findView(IGuiViewIDs.ID_OUTLINEVIEW);
		refresh();
	}

	public void refresh() {
		AccessibleObject rootObject = MSAAViewRegistory.getRootObject();
		if (null != rootObject) {
			IFlashPlayer[] players = FlashMSAAUtil.getFlashPlayers(rootObject
					.getWindow());
			for (int i = 0; i < players.length; i++) {
				players[i].clearAllMarkers();
			}
			viewer.setInput(players);
			FlashDetect.showDialog();
		}
	}

	public void findRectangle(Rectangle flashRect, Object objUnknown) {
		viewer.setInput(new Object[] { objUnknown });
		String strMessage = Messages.flash_error_no_element; 
		RectangleFinder finder = new RectangleFinder(flashRect);
		try {
			finder.find(viewer.getTree().getItems());
		} catch (Error e) {
			e.printStackTrace();
		}
		if (finder.foundCount > 0) {
			strMessage = MessageFormat
					.format(
							Messages.flash_element_found, new Object[] { new Integer(finder.foundCount) }); 
		}
		MessageDialog.openInformation(viewer.getControl().getShell(), Messages.flash_flash_dom, //$NON-NLS-1$
				strMessage);
	}

	public void addWindowlessElement(final Object objUnknown) {
		if (!scanWindowlessAction.isChecked()) {
			Display.getCurrent().asyncExec(new Runnable() {
				public void run() {
					Object currentInput = viewer.getInput();
					if (currentInput instanceof Object[]) {
						List<Object> list = Arrays
								.asList((Object[]) currentInput);
						if (!list.contains(objUnknown)) {
							ArrayList<Object> newList = new ArrayList<Object>(
									list);
							newList.add(objUnknown);
							viewer.setInput(newList.toArray());
						}
					}
				}
			});
		}
	}

	private class RectangleFinder {
		private static final int MARGIN = 10;
		public int foundCount = 0;
		private int errorCount = 0;
		private int flashLeft, flashTop, flashRight, flashBottom;

		public RectangleFinder(Rectangle flashRect) {
			flashLeft = flashRect.x;
			flashTop = flashRect.y;
			flashRight = flashRect.x + flashRect.width;
			flashBottom = flashRect.y + flashRect.height;
		}

		public void find(TreeItem[] items) {
			for (int i = 0; i < items.length; i++) {
				TreeItem item = items[i];
				try {
					IASNode flashNode = (IASNode) item.getData();
					if (flashNode.getLevel() >= 40) {
						throw new Error(
								MessageFormat
										.format(
												Messages.flash_error_target_length, new Object[] { new Integer(flashNode.getLevel()) }) + "\n" + flashNode.getTarget()); //$NON-NLS-1$ //$NON-NLS-2$
					}
					double x = flashNode.getX();
					if (x >= flashRight + MARGIN) {
						continue;
					}
					double y = flashNode.getY();
					if (y >= flashBottom + MARGIN) {
						continue;
					}
					double w = flashNode.getWidth();
					if (x + w <= flashLeft - MARGIN) {
						continue;
					}
					double h = flashNode.getHeight();
					if (y + h <= flashTop - MARGIN) {
						continue;
					}
					if (isMatch(x, y, w, h)) {
						item.setBackground(colorFound);
						foundCount++;
						viewer.reveal(flashNode);
					}
					if (Boolean.TRUE.equals(flashNode
							.getObject("isOpaqueObject"))) { //$NON-NLS-1$
						// System.out.println("Skip Opaque Object
						// "+flashNode.getTarget()); //$NON-NLS-1$
						continue;
					}
					find(((FlashTreeViewer) viewer).getChildItems(item));
				} catch (Exception e) {
					e.printStackTrace();
				} catch (FindError e) {
					throw e;
				} catch (Error e) {
					System.err.println(e.getMessage());
					if (++errorCount >= 20) {
						throw new FindError("Error count reached " + errorCount); //$NON-NLS-1$
					}
				}
			}
		}

		private class FindError extends Error {
			private static final long serialVersionUID = -7707451728015676479L;

			public FindError(String message) {
				super(message);
			}
		};

		private boolean isMatch(double x, double y, double w, double h) {
			return flashLeft - MARGIN <= x && x < flashLeft + MARGIN
					&& flashTop - MARGIN <= y && y < flashTop + MARGIN
					&& flashRight - MARGIN <= x + w
					&& x + w <= flashRight + MARGIN
					&& flashBottom - MARGIN <= y + h
					&& y + h <= flashBottom + MARGIN;
		}
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				FlashDOMView.this.fillContextMenu(manager);
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
		manager.add(informativeTreeAction);
		manager.add(visualTreeAction);
		manager.add(scanWindowlessAction);
		manager.add(debugTreeAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillContextMenu(IMenuManager manager) {
		Object selected = getSelectedItem();
		if (null != selected) {
			manager.add(expandAction);
		}
		manager.add(new Separator());
		manager.add(refreshAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(expandAllAction);
		manager.add(collapseAllAction);
		manager.add(refreshAction);
	}

	private void makeActions() {
		expandAction = new Action(
				org.eclipse.actf.visualization.gui.internal.util.Messages.msaa_expand) {
			public void run() {
				try {
					Object selected = getSelectedItem();
					if (null != selected) {
						viewer.expandToLevel(selected,
								AbstractTreeViewer.ALL_LEVELS);
					}
				} catch (Error e) {
					e.printStackTrace();
				}
			}
		};

		expandAllAction = new Action(
				org.eclipse.actf.visualization.gui.internal.util.Messages.msaa_expand_all) {
			public void run() {
				try {
					viewer.expandAll();
				} catch (Error e) {
					System.err.println(e.getMessage());
				}
			}
		};
		expandAllAction
				.setToolTipText(org.eclipse.actf.visualization.gui.internal.util.Messages.msaa_expand_all); //$NON-NLS-1$
		expandAllAction.setImageDescriptor(GuiImages.IMAGE_EXPAND_ALL);

		collapseAllAction = new Action(
				org.eclipse.actf.visualization.gui.internal.util.Messages.msaa_collapse_all) { //$NON-NLS-1$
			public void run() {
				try {
					viewer.collapseAll();
				} catch (Error e) {
					e.printStackTrace();
				}
			}
		};
		collapseAllAction
				.setToolTipText(org.eclipse.actf.visualization.gui.internal.util.Messages.msaa_collapse_all); //$NON-NLS-1$
		collapseAllAction.setImageDescriptor(GuiImages.IMAGE_COLLAPSE_ALL);

		refreshAction = new RefreshRootAction();

		informativeTreeAction = new Action(Messages.flash_filter_noninformative, Action.AS_CHECK_BOX) { //$NON-NLS-1$
			public void run() {
				FlashDOMContentProvider provider = (FlashDOMContentProvider) viewer
						.getContentProvider();
				provider.informativeTree = informativeTreeAction.isChecked();
				MSAAViewRegistory.refreshRootObject();
			}
		};

		visualTreeAction = new Action(
				Messages.flash_show_visual, Action.AS_CHECK_BOX) { 
			public void run() {
				FlashDOMContentProvider provider = (FlashDOMContentProvider) viewer
						.getContentProvider();
				provider.visualTree = visualTreeAction.isChecked();
				MSAAViewRegistory.refreshRootObject();
			}
		};

		debugTreeAction = new Action(
				Messages.flash_debugMode, Action.AS_CHECK_BOX) { 
			public void run() {
				debugMode = debugTreeAction.isChecked();
				FlashNodePropertySource.setDebugMode(debugMode);
				MSAAViewRegistory.refreshRootObject();
			}
		};

		scanWindowlessAction = new Action(Messages.flash_scanWindowless, Action.AS_CHECK_BOX) { //$NON-NLS-1$
			public void run() {
				FlashMSAAUtil.setScanAll(scanWindowlessAction.isChecked());
				MSAAViewRegistory.refreshRootObject();
			}
		};

	}

	public void setDebugMode(boolean isDebug) {
		debugTreeAction.setChecked(isDebug);
		scanWindowlessAction.setChecked(isDebug);
		debugMode = isDebug;
		FlashNodePropertySource.setDebugMode(isDebug);
		FlashMSAAUtil.setScanAll(isDebug);
		MSAAViewRegistory.refreshRootObject();
	}

	private Object getSelectedItem() {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		if (selection.size() == 1) {
			return selection.getFirstElement();
		}
		return null;
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private class FlashDOMContentProvider implements ITreeContentProvider {

		public boolean visualTree = false;
		public boolean informativeTree = false;

		public Object[] getChildren(Object parentElement) {
			IASNode[] result = new IASNode[0];
			if (parentElement instanceof IASNode) {
				if (debugMode) {
					result = ((IASNode) parentElement).getEntireChildren();

				} else {
					result = ((IASNode) parentElement).getChildren(visualTree);
					if (informativeTree) {
						Vector<IASNode> tmpV = new Vector<IASNode>();
						for (IASNode node : result) {
							if (!node.isAccProperties()) {
								if (null == node.getText()
										&& !ASNODE_TYPE_MOVIECLIP.equals(node
												.getType())
										&& //$NON-NLS-1$
										!ASNODE_CLASS_BUTTON.equals(node
												.getClassName())
										&& //$NON-NLS-1$
										!ACC_PROPS.equals(node.getObjectName())
										&& //$NON-NLS-1$
										!ACC_IMPL.equals(node.getObjectName())
										&& //$NON-NLS-1$
										!ON_RELEASE
												.equals(node.getObjectName())) //$NON-NLS-1$
								{
									continue;
								}
								tmpV.add(node);
							} else {
								tmpV.add(node);
							}
						}
						result = new IASNode[tmpV.size()];
						tmpV.toArray(result);
					}
				}
			}
			return result;
		}

		public Object getParent(Object element) {
			if (element instanceof IASNode) {
				return ((IASNode) element).getParent();
			}
			return null;
		}

		public boolean hasChildren(Object element) {
			if (element instanceof IASNode) {
				return ((IASNode) element).hasChild(visualTree, debugMode);
			}
			return false;
		}

		public Object[] getElements(Object inputElement) {
			List<Object> elements = new ArrayList<Object>();
			if (inputElement instanceof Object[]) {
				Object[] objects = (Object[]) inputElement;
				for (int i = 0; i < objects.length; i++) {
					IFlashPlayer player = null;
					if (objects[i] instanceof AccessibleObject) {
						AccessibleObject accObj = (AccessibleObject) objects[i];
						player = FlashPlayerFactory.getPlayerFromPtr(accObj
								.getPtr());
					} else if (objects[i] instanceof IFlashPlayer) {
						player = (IFlashPlayer) objects[i];
					}
					if (null != player) {
						IASNode rootNode = player.getRootNode();
						if (null != rootNode) {
							elements.add(rootNode);
						} else {
							elements.add(player);
						}

					}

				}
			}
			return elements.toArray();
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private static String getNodeImage(Object element) {
		String iconType = null;
		if (element instanceof IASNode) {
			iconType = ((IASNode) element).getIconType();
			if (iconType.startsWith(ASNODE_ICON_ACCROLE)) {
				try {
					String tmpS = iconType.substring(ASNODE_ICON_ACCROLE
							.length());
					return GuiImages.roleIcon(Integer.parseInt(tmpS));
				} catch (Exception e) {
				}
			}
		} else if (element instanceof IFlashPlayer) {
			iconType = FlashImages.TYPE_flash;
		}
		return null != iconType ? FlashImages.flashIcon(iconType) : null;
	}

	private static String getNodeText(Object element) {
		if (element instanceof IASNode) {
			IASNode flashNode = (IASNode) element;
			StringBuffer sb = new StringBuffer();
			String text = flashNode.getText();
			if (null != text) {
				sb.append(text);
			}
			String title = flashNode.getTitle();
			if (null != title) {
				if (sb.length() > 0) {
					sb.append(" "); //$NON-NLS-1$
				}
				sb.append(title);
			}
			String value = flashNode.getValue();
			if (null != value) {
				if (sb.length() > 0) {
					sb.append(" "); //$NON-NLS-1$
				}
				sb.append(value);
			}
			if (sb.length() == 0) {
				sb
						.append(org.eclipse.actf.visualization.gui.internal.util.Messages.msaa_NAMELESS);
			}
			String objectName = flashNode.getObjectName();
			if (sb.length() > 0) {
				sb.append(" "); //$NON-NLS-1$
			}
			sb.append(objectName);
			String type = flashNode.getType();
			if (null != type) {
				sb.append("("); //$NON-NLS-1$
				sb.append(type);
				sb.append(")"); //$NON-NLS-1$
			}
			return sb.toString();
		} else if (element instanceof IFlashPlayer) {
			return ((IFlashPlayer) element).getStatus();
		}
		if (null == element)
			return "null"; //$NON-NLS-1$
		return element.toString();
	}

	public String getNodeError(Object element) {
		if (element instanceof IASNode) {
			IASNode flashNode = (IASNode) element;
			if (!flashNode.getPlayer().isVisible()) {
				return FlashImages.OVER_BLACK;
			}
			ASAccInfo accInfo = flashNode.getAccInfo();

			if (flashNode.isUIComponent()) {
				if (null == accInfo || -1 == accInfo.getRole()) {
					return FlashImages.OVER_RED;
				}
			}
			boolean shouldWarn = "movieclip".equals(flashNode.getType()) || //$NON-NLS-1$
					"Button".equals(flashNode.getClassName()); //$NON-NLS-1$
			if (shouldWarn && null != accInfo && accInfo.isSilent()) {
				return FlashImages.OVER_BLACK;
			}
			if (null != flashNode.getText()) {
				return FlashImages.OVER_GREEN;
			}
			if (shouldWarn && (null == accInfo || null == accInfo.getName())) {
				return FlashImages.OVER_RED;
			}
			if ("Button".equals(flashNode.getClassName())) { //$NON-NLS-1$
				return FlashImages.OVER_YELLOW;
			}
			String objectName = flashNode.getObjectName();
			if ("onRelease".equals(objectName)) { //$NON-NLS-1$
				return FlashImages.OVER_YELLOW;
			}
			if ("_accProps".equals(objectName) || //$NON-NLS-1$
					"_accImpl".equals(objectName)) { //$NON-NLS-1$
				return FlashImages.OVER_GREEN;
			}
		} else if (element instanceof IFlashPlayer) {
			return FlashImages.OVER_BLACK;
		}
		return null;
	}

	private class FlashDOMLabelProvider extends LabelProvider implements
			IColorProvider {

		public FlashDOMLabelProvider() {
			super();
		}

		public Image getImage(Object element) {
			String nodeImage = getNodeImage(element);
			String nodeError = getNodeError(element);
			if (null != nodeImage) {
				if (null != nodeError) {
					return FlashImages.getImage(nodeImage, nodeError,
							new Point(16, 16));
				}
				return FlashImages.getImage(nodeImage);
			} else if (null != nodeError) {
				return FlashImages.getImage(nodeError);
			}
			return null;
		}

		public String getText(Object element) {
			return getNodeText(element);
		}

		public Color getBackground(Object element) {
			return null;
		}

		public Color getForeground(Object element) {
			return null;
		}

	}

	private class FlashTreeViewer extends TreeViewer {

		public FlashTreeViewer(Composite parent, int style) {
			super(new Tree(parent, style));
		}

		public TreeItem[] getChildItems(TreeItem item) {
			super.createChildren(item);
			return item.getItems();
		}
	}
}
