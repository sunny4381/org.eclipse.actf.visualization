/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.lowvision;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.eclipse.actf.model.ui.ImagePositionInfo;
import org.eclipse.actf.model.ui.editor.browser.ICurrentStyles;
import org.eclipse.actf.visualization.engines.lowvision.image.ImageException;
import org.eclipse.actf.visualization.engines.lowvision.image.Int2D;
import org.eclipse.actf.visualization.engines.lowvision.image.PageImage;
import org.eclipse.actf.visualization.engines.lowvision.image.SimulatedPageImage;
import org.eclipse.actf.visualization.engines.lowvision.internal.util.DebugUtil;
import org.eclipse.actf.visualization.engines.lowvision.internal.util.LowVisionProblemConverter;
import org.eclipse.actf.visualization.engines.lowvision.internal.util.ScoreUtil;
import org.eclipse.actf.visualization.engines.lowvision.io.ImageWriter;
import org.eclipse.actf.visualization.engines.lowvision.io.LowVisionIOException;
import org.eclipse.actf.visualization.engines.lowvision.problem.LowVisionProblem;
import org.eclipse.actf.visualization.engines.lowvision.problem.LowVisionProblemException;
import org.eclipse.actf.visualization.engines.lowvision.problem.LowVisionProblemGroup;
import org.eclipse.actf.visualization.engines.lowvision.util.DecisionMaker;
import org.eclipse.actf.visualization.eval.problem.IProblemItem;
import org.eclipse.actf.visualization.eval.problem.ProblemItemLV;


public class TargetPage {
	private static final int UNSET = -1;

	private PageImage pageImage = null; // rendered image in browser

	private ImagePositionInfo[] tmpInteriorImagePositions = null;

	private PageElement[] pageElements = null; // HTML Element info from DOM

	private String[] allowedForegroundColors = null;

	private String[] allowedBackgroundColors = null;

	private int pageWidth = UNSET;

	private int pageHeight = UNSET;

	private String overallRatingString = "";

	private String overallRatingImageString = "";

	public TargetPage() {
	}

	public void disposePageImage() {
		pageImage.disposeInt2D();
	}

	public PageImage getPageImage() {
		return (pageImage);
	}

	public void setPageImage(PageImage _pi) {
		pageImage = _pi;
		if (pageImage != null) {
			if (pageImage.isInteriorImageArraySet()) {
				try {
					pageImage.extractCharacters();
				} catch (ImageException e) {
					e.printStackTrace();
				}
			} else if (tmpInteriorImagePositions != null) {
				pageImage.setInteriorImagePosition(tmpInteriorImagePositions);
				this.tmpInteriorImagePositions = null;
				try {
					pageImage.extractCharacters();
				} catch (ImageException e) {
					e.printStackTrace();
				}
			}
			pageWidth = pageImage.getWidth();
			pageHeight = pageImage.getHeight();
		}
	}

	public ImagePositionInfo[] getInteriorImagePosition() {
		if (pageImage == null) {
			return (null);
		}
		return (pageImage.getInteriorImagePosition());
	}

	public void setInteriorImagePosition(ImagePositionInfo[] infoArray) {
		if (pageImage != null) {
			pageImage.setInteriorImagePosition(infoArray);
			try {
				pageImage.extractCharacters();
			} catch (ImageException e) {
				e.printStackTrace();
			}
		} else {
			this.tmpInteriorImagePositions = infoArray;
		}
	}

	public PageElement[] getPageElements() {
		return (pageElements);
	}

	public void setCurrentStyles(HashMap _styleMap) {
		Object[] keyArray = _styleMap.keySet().toArray();
		int len = keyArray.length;
		pageElements = new PageElement[len];
		for (int i = 0; i < len; i++) {
			try {
				pageElements[i] = new PageElement((String) (keyArray[i]),
						(ICurrentStyles) (_styleMap.get(keyArray[i])));
			} catch (ImageException e) {
				e.printStackTrace();
				pageElements[i] = null;
			}
		}
	}

	public String[] getAllowedForegroundColors() {
		return (allowedForegroundColors);
	}

	public String[] getAllowedBackgroundColors() {
		return (allowedBackgroundColors);
	}

	public void setAllowedColors(String[] _fg, String[] _bg) {
		allowedForegroundColors = _fg;
		allowedBackgroundColors = _bg;
	}

	public void setAllowedForegroundColors(String[] _fg) {
		allowedForegroundColors = _fg;
	}

	public void setAllowedBackgroundColors(String[] _bg) {
		allowedBackgroundColors = _bg;
	}

	public void clearAllowedColors() {
		allowedForegroundColors = null;
		allowedBackgroundColors = null;
	}

	public void clearAllowedForegroundColors() {
		allowedForegroundColors = null;
	}

	public void clearAllowedBackgroundColors() {
		allowedBackgroundColors = null;
	}

	public String getOverallRatingString() {
		return (overallRatingString);
	}

	public SimulatedPageImage simulate(LowVisionType _lvType)
			throws ImageException {
		if (this.pageImage == null) {
			return (null);
		}
		return (new SimulatedPageImage(this.pageImage, _lvType));
	}

	public List<IProblemItem> check(LowVisionType _lvType, String urlS,
			int frameId) {

		// TODO LowVisionProblem/LowVisionProblemGroup

		LowVisionProblemGroup[] pageImageProblemArray = null;
		LowVisionProblemGroup[] pageElementProblemArray = null;
		LowVisionProblemGroup[] answerProblemArray = null;

		if (pageImage != null) {
			try {
				pageImageProblemArray = pageImage.checkCharacters(_lvType);
			} catch (LowVisionProblemException lvpe) {
				lvpe.printStackTrace();
			} catch (ImageException ie) {
				ie.printStackTrace();
			}
		}

		if (pageElements != null) {
			Vector<LowVisionProblemGroup> pageElementProblemVec = new Vector<LowVisionProblemGroup>();
			int len = pageElements.length;
			for (int i = 0; i < len; i++) {
				PageElement curElement = pageElements[i];
				if (curElement == null) {
					continue;
				}
				LowVisionProblem[] curProblemArray = curElement.check(_lvType,
						this);
				int curLen = 0;
				if (curProblemArray != null) {
					curLen = curProblemArray.length;
				}

				// convert to LowVisionProblemGroup
				for (int j = 0; j < curLen; j++) {
					Vector<LowVisionProblem> tmpVec = new Vector<LowVisionProblem>();
					tmpVec.addElement(curProblemArray[j]);
					LowVisionProblemGroup lvpGroup = null;
					try {
						lvpGroup = new LowVisionProblemGroup(tmpVec);
						pageElementProblemVec.addElement(lvpGroup);
					} catch (LowVisionProblemException lvpe) {
						lvpe.printStackTrace();
					}
				}
			}

			int totalSize = pageElementProblemVec.size();
			pageElementProblemArray = new LowVisionProblemGroup[totalSize];
			for (int i = 0; i < totalSize; i++) {
				pageElementProblemArray[i] = (LowVisionProblemGroup) (pageElementProblemVec
						.elementAt(i));
			}
		}

		int len1 = 0;
		if (pageImageProblemArray != null) {
			len1 = pageImageProblemArray.length;
		}
		int len2 = 0;
		if (pageElementProblemArray != null) {
			len2 = pageElementProblemArray.length;
		}

		answerProblemArray = new LowVisionProblemGroup[len1 + len2];
		for (int i = 0; i < len1; i++) {
			answerProblemArray[i] = pageImageProblemArray[i];
		}
		for (int i = 0; i < len2; i++) {
			answerProblemArray[len1 + i] = pageElementProblemArray[i];
		}

		pageImageProblemArray = null;
		pageElementProblemArray = null;

		calcOverallRating(answerProblemArray);

		return (LowVisionProblemConverter.convert(answerProblemArray, urlS,
				frameId));
	}

	private void calcOverallRating(LowVisionProblemGroup[] _problemGroupArray) {
		int len = 0;
		if (_problemGroupArray != null) {
			len = _problemGroupArray.length;
		}

		double totalSeverity = 0.0;
		for (int k = 0; k < len; k++) {
			LowVisionProblemGroup curGroup = _problemGroupArray[k];
			if (curGroup == null) {
				continue;
			}
			totalSeverity += curGroup.getProbability();
		}
		overallRatingString = ScoreUtil.getScoreString(totalSeverity);
		overallRatingImageString = ScoreUtil.getScoreImageString(totalSeverity);

	}

	// Max size of Problem Map image
	private static final int PROBLEM_MAP_LENGTH = 100;

	public void unsupportedModeReport(File targetFile)
			throws LowVisionException {

		StringBuffer sb = new StringBuffer();
		sb.append("<HTML>\n<HEAD>\n<TITLE>Report from LowVision Evaulator</TITLE>\n");
		sb.append("</HEAD><BODY>");
		// TODO
		sb.append("</BODY>\n</HTML>\n");
		if (targetFile != null) {
			try {
				PrintWriter pw = new PrintWriter(targetFile);
				pw.println(sb.toString());
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new LowVisionException("Could not write to " + targetFile);
			}
		}
	}

	public void makeAndStoreReport(String _path, String _htmlName,
			String _imgName, List _problemGroupArray) throws LowVisionException {
		boolean doMakeProblemMap = true;
		if (this.pageImage == null)
			doMakeProblemMap = false;

		String path = _path;
		if (!(path.endsWith(File.separator))) {
			path += File.separator;
		}
		String htmlPath = path + _htmlName;
		String imgPath = path + _imgName;

		int len = 0;
		if (_problemGroupArray != null) {
			len = _problemGroupArray.size();
		}

		Int2D scoreMap = null;

		if (doMakeProblemMap) {
			int shorter = pageWidth;
			if (pageWidth > pageHeight) {
				shorter = pageHeight;
			}
			int scale = (int) (Math.ceil(((double) shorter)
					/ (double) (PROBLEM_MAP_LENGTH)));
			if (scale <= 0) {
				throw new LowVisionException("scale is out of range: " + scale);
			}
			int mapWidth = (int) (Math.ceil((double) (pageWidth)
					/ (double) (scale)));
			int mapHeight = (int) (Math.ceil((double) (pageHeight)
					/ (double) (scale)));
			scoreMap = new Int2D(mapWidth, mapHeight);

			for (int k = 0; k < len; k++) {
				if (_problemGroupArray.get(k) instanceof ProblemItemLV) {
					ProblemItemLV curProblem = (ProblemItemLV) _problemGroupArray
							.get(k);

					int groupX = curProblem.getX();
					if (groupX < 0)
						groupX = 0;
					int groupY = curProblem.getY();
					if (groupY < 0)
						groupY = 0;
					int groupWidth = curProblem.getWidth();
					int groupHeight = curProblem.getHeight();
					/*
					 *  TODO consideration for inline element
					 *   x+width or y+height might
					 *    exseed the page size
					 *    overlap the next block level element
					 *    etc.
					 */
					int rightLimit = groupX + groupWidth;
					if (pageWidth < rightLimit)
						rightLimit = pageWidth;
					int bottomLimit = groupY + groupHeight;
					if (pageHeight < bottomLimit)
						bottomLimit = pageHeight;
					for (int j = groupY; j < bottomLimit; j++) {
						for (int i = groupX; i < rightLimit; i++) {
							// scoreMap.data[j+groupY][i+groupX] = fillingColor;
							// debug
							try {
								scoreMap.data[j / scale][i / scale] += curProblem
										.getSeverityLV();
							} catch (Exception e) {
								e.printStackTrace();
								DebugUtil.errMsg(this, "i=" + i + ", j=" + j
										+ ", groupX=" + groupX + ", groupY="
										+ groupY + ", groupWidth=" + groupWidth
										+ ", groupHeight=" + groupHeight
										+ ", pageWidth=" + pageWidth
										+ ", pageHeight=" + pageHeight);
								throw new LowVisionException(
										"Error while making problem map");
							}
						}
					}
				}
			}

			double scaleDouble = (double) (scale * scale); 
			for (int j = 0; j < mapHeight; j++) {
				for (int i = 0; i < mapWidth; i++) {
					scoreMap.data[j][i] = DecisionMaker
							.getScoreMapColor(((double) (scoreMap.data[j][i]))
									/ 100.0 / scaleDouble);
				}
			}

			try {
				ImageWriter.writeBufferedImage(scoreMap.toBufferedImage(),
						imgPath);
			} catch (LowVisionIOException lvioe) {
				lvioe.printStackTrace();
				throw new LowVisionException(
						"An IO error occurred while writing the problem map file of this page.");
			}
		}
		scoreMap = null;

		StringBuffer sb = new StringBuffer();
		sb.append("<HTML>\n<HEAD>\n<TITLE>Report from LowVision Evaulator</TITLE>\n");
		sb.append("<STYLE type=\"text/css\">\n");
		sb.append("IMG {border:2 solid black}\n");
		sb.append("</STYLE>\n");
		sb.append("</HEAD><BODY>");

		// sb.append( "<DIV>\nOverall rating: <B>" + overallRatingString +
		// "</B></DIV>\n");

		// TODO lv report files -> result dir
		sb.append("<DIV>\nOverall rating: <IMG src=\"./img/"
				+ overallRatingImageString + "\" alt=\"" + overallRatingString
				+ "\"></DIV>\n");

		sb.append("<HR>");
		sb.append("<DIV align=\"center\">\n");
		if (doMakeProblemMap) {
			sb.append("Problem Map<BR>\n");
			sb.append("<IMG src=\"" + _imgName + "\" alt=\"score map\" ");
			if (pageWidth >= pageHeight) {
				sb.append("width=\"75%\"");
			} else {
				sb.append("height=\"75%\"");
			}
			sb.append(">\n");
		} else {
			sb.append("Problem map is not available for this page.");
		}
		sb.append("</DIV>\n");
		sb.append("</BODY>\n</HTML>\n");
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(htmlPath));
			pw.println(sb.toString());
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new LowVisionException("Could not write to " + htmlPath);
		}
	}

}
