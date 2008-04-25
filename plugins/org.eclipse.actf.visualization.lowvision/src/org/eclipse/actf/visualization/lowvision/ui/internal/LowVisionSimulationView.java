/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.lowvision.ui.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.actf.mediator.Mediator;
import org.eclipse.actf.model.IModelService;
import org.eclipse.actf.model.IModelServiceScrollManager;
import org.eclipse.actf.model.ModelServiceSizeInfo;
import org.eclipse.actf.visualization.eval.problem.IPositionSize;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

public class LowVisionSimulationView {

	private Display _display;

	private Canvas _imageCanvas;

	private Image _image = null;

	private ImageData _imageData = null; // the currently-displayed image

	// data

	private int _ix;

	private int _iy;

	private ScrollBar _horizontalBar;

	private ScrollBar _verticalBar;

	private boolean _isLocked = false;

	private List<IPositionSize> _highlightTargetList = new ArrayList<IPositionSize>();

	private LowVisionToolbar _lowVisionToolbar;

	private Mediator mediator = Mediator.getInstance();

	private IModelService target;

	// separate from PartRightLowVision
	public LowVisionSimulationView(Composite parent,
			PartControlLowVision lowVisionCtrl) {

		this._display = parent.getDisplay();
		this._ix = 0;
		this._iy = 0;

		GridData gridData;

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		parent.setLayout(gridLayout);

		this._lowVisionToolbar = new LowVisionToolbar(parent, SWT.NONE,
				lowVisionCtrl);

		Composite compositeLowVisionHalf2 = new Composite(parent, SWT.NONE);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		compositeLowVisionHalf2.setLayoutData(gridData);

		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		compositeLowVisionHalf2.setLayout(gridLayout);

		// Canvas to show the image.
		this._imageCanvas = new Canvas(compositeLowVisionHalf2, SWT.V_SCROLL
				| SWT.H_SCROLL);
		// | SWT.NO_REDRAW_RESIZE
		this._imageCanvas.setBackground(new Color(_display, 255, 255, 255));
		// white color
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		this._imageCanvas.setLayoutData(gridData);

		this._imageCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {

				if (null == _imageData) {
					return;
				}

				if (null != _image) {
					paintImage(event);
				}

				resizeScrollBars();

				if (null == _image || _isLocked) {
					_lowVisionToolbar.getSyncButton().setSelection(true);
					_lowVisionToolbar.getSyncButton().setEnabled(false);
				} else {
					int imageHeight = Math.round(_imageData.height);

					int ieHeight = Integer.MIN_VALUE;

					IModelService modelService = mediator
							.getActiveModelService();
					if (modelService != null) {
						ieHeight = modelService.getScrollManager()
								.getSize(true).getWholeSizeY();
					}

					// TODO use bar height
					if (Math.abs(imageHeight - ieHeight) < 10) {
						_lowVisionToolbar.getSyncButton().setEnabled(true);
					} else {
						_lowVisionToolbar.getSyncButton().setEnabled(false);
					}
				}

			}
		});

		// _imageCanvas.addMouseMoveListener(new MouseMoveListener() {
		// public void mouseMove(MouseEvent event) {
		// // do nothing
		// }
		// });

		// Set up the image canvas scroll bars.
		_horizontalBar = _imageCanvas.getHorizontalBar();
		_horizontalBar.setVisible(true);
		_horizontalBar.setMinimum(0);
		_horizontalBar.setEnabled(false);
		_horizontalBar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scrollHorizontally((ScrollBar) event.widget);
			}
		});
		_verticalBar = _imageCanvas.getVerticalBar();
		_verticalBar.setVisible(true);
		_verticalBar.setMinimum(0);
		_verticalBar.setEnabled(false);
		_verticalBar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scrollVertically((ScrollBar) event.widget);
			}
		});

		parent.getShell().addListener(100, new Listener() {
			public void handleEvent(Event e) {
				setVScrollBar(e.detail);
			}
		});

	}

	protected Point getVarticalBarSize() {
		return _verticalBar.getSize();
	}

	protected void highlight(List<IPositionSize> target) {

		if (target != null) {
			// int topX = 0;
			int topY = -1;

			for (IPositionSize ips : target) {
				if (topY < 0 || topY > ips.getY()) {
					topY = ips.getY();
					// TODO topX = ips.getX();
				}
			}

			if (topY >= 0) {
				Rectangle canvasBounds = this._imageCanvas.getClientArea();
				int height = Math.round(this._imageData.height); // yscale
				if (height > canvasBounds.height) {
					if (height - topY < canvasBounds.height) {
						// Don't scroll past the end of the image.
						topY = height - canvasBounds.height;
					}
					if (this._lowVisionToolbar.getSyncButton().isEnabled()
							&& this._lowVisionToolbar.getSyncButton()
									.getSelection()) {
						// TODO use VizResult ModelService mapping
						// BrowserEventListenerManager.getInstance().fireScrollY(this._iy
						// + topY);
					}
					setVScrollBar(topY);
				}
			}
			this._highlightTargetList = target;
		}

		this._imageCanvas.redraw();

	}

	public void disposeImage() {
		if (_image != null && _image.isDisposed()) {
			_image.dispose();

		}
		_image = null;
		if (_imageData != null) {
			_imageData = null;
		}
	}

	protected void clearImage() {
		if (_image != null) {
			_image.dispose();
			_image = null;
		}
		if (_imageData != null) {
			_imageData = null;
		}
		_highlightTargetList = new ArrayList<IPositionSize>();
		resetScrollBars();
		_imageCanvas.redraw();
	}

	protected void displayImage(ImageData newImageData) {
		// Dispose of the old image, if there was one.
		if (null != this._image) {
			this._image.dispose();
		}

		this._imageData = newImageData;
		this._image = null;

		if (null != this._imageData) {
			try {
				// Cache the new image and imageData.
				this._image = new Image(_display, newImageData);
			} catch (SWTException se) {
				se.printStackTrace();
			}
		}

		// Redraw the canvases.
		_imageCanvas.redraw();
	}

	private void paintImage(PaintEvent event) {
		if (null == _image || null == _imageData) {
			return;
		}

		Image paintImage = _image;
		// int transparentPixel = _imageData.transparentPixel;
		int w = Math.round(_imageData.width); // xscale
		int h = Math.round(_imageData.height); // yscale
		event.gc
				.drawImage(paintImage, 0, 0, _imageData.width,
						_imageData.height, _ix + _imageData.x, _iy
								+ _imageData.y, w, h);

		// Vector tmpV = lowVisionControl.getSelectedItemsInfo();
		List<IPositionSize> tmpV = _highlightTargetList;
		if (tmpV != null) {
			event.gc.setXORMode(false);
			event.gc.setLineWidth(2);
			for (IPositionSize ips : tmpV) {

				event.gc
						.setForeground(_display.getSystemColor(SWT.COLOR_WHITE));
				event.gc.drawRectangle(_ix + ips.getX(), _iy + ips.getY(), ips
						.getWidth(), ips.getHeight());

				event.gc
						.setForeground(_display.getSystemColor(SWT.COLOR_BLACK));
				event.gc.drawRectangle(_ix + ips.getX() - 2, _iy + ips.getY()
						- 2, ips.getWidth() + 4, ips.getHeight() + 4);

			}
		}
	}

	protected void resetScrollBars() {
		if (_image == null) {
			_horizontalBar.setEnabled(false);
			_verticalBar.setEnabled(false);
			return;
		}

		_ix = 0;
		_iy = 0;
		resizeScrollBars();
		_horizontalBar.setSelection(0);
		_verticalBar.setSelection(0);
	}

	private void resizeScrollBars() {
		if (_imageData == null)
			return;

		// Set the max and thumb for the image canvas scroll bars.

		Rectangle canvasBounds = _imageCanvas.getClientArea();
		int width = Math.round(_imageData.width); // xscale
		if (width > canvasBounds.width) {
			// The image is wider than the canvas.
			_horizontalBar.setEnabled(true);
			_horizontalBar.setMaximum(width);
			_horizontalBar.setThumb(canvasBounds.width);
			_horizontalBar.setPageIncrement(canvasBounds.width);
		} else {
			// The canvas is wider than the image.
			_horizontalBar.setEnabled(false);
			if (_ix != 0) {
				// Make sure the image is completely visible.
				_ix = 0;
				_imageCanvas.redraw();
			}
		}
		int height = Math.round(_imageData.height); // yscale
		if (height > canvasBounds.height) {
			// The image is taller than the canvas.
			_verticalBar.setEnabled(true);
			_verticalBar.setMaximum(height);
			_verticalBar.setThumb(canvasBounds.height);
			_verticalBar.setPageIncrement(canvasBounds.height);
		} else {
			// The canvas is taller than the image.
			_verticalBar.setEnabled(false);
			if (_iy != 0) {
				// Make sure the image is completely visible.
				_iy = 0;
				_imageCanvas.redraw();
			}
		}
	}

	private void scrollHorizontally(ScrollBar scrollBar) {
		if (_image == null)
			return;
		Rectangle canvasBounds = _imageCanvas.getClientArea();
		int width = Math.round(_imageData.width); // xscale
		int height = Math.round(_imageData.height); // yscale
		if (width > canvasBounds.width) {
			// Only scroll if the image is bigger than the canvas.
			int x = -scrollBar.getSelection();
			if (x + width < canvasBounds.width) {
				// Don't scroll past the end of the image.
				x = canvasBounds.width - width;
			}
			_imageCanvas.scroll(x, _iy, _ix, _iy, width, height, false);
			_ix = x;

		}
	}

	/*
	 * Called when the image canvas' vertical scrollbar is selected.
	 */
	private void scrollVertically(ScrollBar scrollBar) {
		if (null != this._image) {
			Rectangle canvasBounds = _imageCanvas.getClientArea();
			int width = Math.round(this._imageData.width); // xscale
			int height = Math.round(this._imageData.height); // yscale
			if (height > canvasBounds.height) {
				// Only scroll if the image is bigger than the canvas.
				int y = -scrollBar.getSelection();
				if (y + height < canvasBounds.height) {
					// Don't scroll past the end of the image.
					y = canvasBounds.height - height;
				}
				// System.out.println("height=" + height + ", ix=" + _ix+", iy="
				// + _iy + ", y=" + y);
				_imageCanvas.scroll(_ix, y, _ix, _iy, width, height, false);

				if (this._lowVisionToolbar.getSyncButton().isEnabled()
						&& this._lowVisionToolbar.getSyncButton()
								.getSelection()) {

					if (target != null) {
						IModelServiceScrollManager scrollManager = target
								.getScrollManager();
						switch (scrollManager.getScrollType()) {
						case IModelServiceScrollManager.ABSOLUTE_COORDINATE:
							scrollManager.absoluteCoordinateScroll(
									this._iy - y, false);
							break;
						case IModelServiceScrollManager.INCREMENTAL:
							// TODO
							break;
						case IModelServiceScrollManager.PAGE:
							ModelServiceSizeInfo sizeInfo = scrollManager
									.getSize(false);
							int page = Math.abs(y / sizeInfo.getViewSizeY()) + 1;
							// System.out.println(y+"
							// "+sizeInfo.getViewSizeY()+" "+page);
							if (page < 1) {
								page = 1;
							} else if (page > scrollManager.getLastPageNumber()) {
								page = scrollManager.getCurrentPageNumber();
							}
							if (page != scrollManager.getCurrentPageNumber()) {
								scrollManager.jumpToPage(page, false);
							}

							break;
						case IModelServiceScrollManager.NONE:
						default:
						}
					}

					// BrowserEventListenerManager.getInstance().fireScrollY(this._iy
					// - y);
				}

				_iy = y;
			}
		}
	}

	/**
	 * @return
	 */
	public boolean isWholepage() {
		Button button = _lowVisionToolbar.getWholePageButton();

		return (button.isEnabled() && button.getSelection());
	}

	private void setVScrollBar(int iPos) {
		if (this._lowVisionToolbar.getSyncButton().isEnabled()
				&& this._lowVisionToolbar.getSyncButton().getSelection()) {
			if (_image == null) {
				return;
			}

			// Rectangle canvasBounds = _imageCanvas.getClientArea();
			int width = Math.round(_imageData.width); // xscale
			int height = Math.round(_imageData.height); // yscale

			this._imageCanvas
					.scroll(_ix, -iPos, _ix, _iy, width, height, false);
			this._iy = -iPos;

			this._imageCanvas.getVerticalBar().setSelection(iPos);

		}
	}

	public void setWholePage(boolean isWholePage) {
		this._lowVisionToolbar.getWholePageButton().setSelection(isWholePage);
	}

	public void setTarget(IModelService target) {
		this.target = target;
		Button button = _lowVisionToolbar.getWholePageButton();
		Button button2 = _lowVisionToolbar.getSyncButton();

		if (null == target) {
			button.setEnabled(false);
			button2.setEnabled(false);
			return;
		}

		switch (target.getScrollManager().getScrollType()) {
		case IModelServiceScrollManager.ABSOLUTE_COORDINATE:
		case IModelServiceScrollManager.INCREMENTAL:
		case IModelServiceScrollManager.PAGE:
			button.setEnabled(true);
			button2.setEnabled(true);
			break;
		case IModelServiceScrollManager.NONE:
		default:
			button.setEnabled(false);
			button2.setEnabled(false);
		}

	}

}
