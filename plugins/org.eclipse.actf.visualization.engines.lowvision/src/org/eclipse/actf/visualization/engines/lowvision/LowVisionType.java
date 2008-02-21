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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import org.eclipse.actf.visualization.engines.lowvision.operator.CVDOp;
import org.eclipse.actf.visualization.engines.lowvision.operator.ColorFilterOp;
import org.eclipse.actf.visualization.engines.lowvision.operator.GlareOp;

/*
 * Define lowvision simulation type
 */
public class LowVisionType {

	public static final String COLOR_FILTER_STR = "senior_color";

	public static final String CVD_STR = "color";

	public static final String EYESIGHT_STR = "focus";

	public static final String GLARE_STR = "glare";

	// private static final String FIELD_STR = "field";

	private static final double EYESIGHT_DEGREE_MARGIN = 0.1;

	private final int DISPLAY_RESOLUTION = 1024; // Y

	private final double DISPLAY_HEIGHT = 20.8; // 14.1 inches

	// public static final double DISPLAY_HEIGHT = 26.5; // 19 inches
	private final double EYE_DISPLAY_DISTANCE = 30.0;

	private boolean eyesight = false;

	private float eyesightDegree;

	// radius of blur array (exclude center, 3x3 -> 1)
	private int eyesightRadius;

	// undistinguishable pixel size for the target person
	private double eyesightPixel;

	// undistinguishable length(mm) for thetarget person
	private double eyesightLength;

	// color vision deficiency
	private boolean CVD = false;

	private int CVDType;

	// color filter
	private boolean colorFilter = false;

	private float colorFilterR; // [0.0, 1.0]

	private float colorFilterG; // [0.0, 1.0]

	private float colorFilterB; // [0.0, 1.0]

	// glare
	private boolean glare = false;

	private float glareDegree = 0.0f;

	private int displayResolution = DISPLAY_RESOLUTION;

	private double displayHeight = DISPLAY_HEIGHT;

	private double eyeDisplayDistance = EYE_DISPLAY_DISTANCE;

	public LowVisionType() {
	}

	public LowVisionType(String _fileName) throws LowVisionException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(_fileName));
		} catch (FileNotFoundException fne) {
			fne.printStackTrace();
			throw new LowVisionException("File not found.");
		}
		read(br);
	}

	public LowVisionType(BufferedReader _br) throws LowVisionException {
		read(_br);
	}

	public boolean getEyesight() {
		return (eyesight);
	}

	public void setEyesight(boolean _b) {
		eyesight = _b;
	}

	public float getEyesightDegree() {
		return (eyesightDegree);
	}

	public void setEyesightDegree(float _deg) throws LowVisionException {
		if (_deg <= 0.0) {
			throw new LowVisionException("Eyesight degree must be positive.");
		}
		eyesightDegree = _deg;
		eyesightPixel = calcUndistinguishablePixel(eyesightDegree);
		eyesightRadius = calcRadius(eyesightPixel);
		eyesightLength = calcUndistinguishableLength(eyesightDegree);
	}

	public double getEyesightPixel() {
		return (eyesightPixel);
	}

	public int getEyesightRadius() {
		return (eyesightRadius);
	}

	public double getEyesightLength() {
		return (eyesightLength);
	}

	private double calcUndistinguishablePixel(double _degree) {
		double degree = _degree + EYESIGHT_DEGREE_MARGIN;
		double thetaD = 1.0 / degree; // distinguishable degree (arcmin)
		double thetaR = thetaD * Math.PI / 10800.0; // distinguishable degree(radian)
		return (2.0 * Math.tan(thetaR / 2.0) * displayResolution
				* eyeDisplayDistance / displayHeight);
	}

	private int calcRadius(double _pixel) {
		return ((int) (Math.ceil(_pixel)));
	}

	private double calcUndistinguishableLength(double _degree) {
		double degree = _degree + EYESIGHT_DEGREE_MARGIN;
		double thetaD = 1.0 / degree;
		double thetaR = thetaD * Math.PI / 10800.0;
		return (20.0 * Math.tan(thetaR / 2.0) * eyeDisplayDistance);
		/*
		 * 20.0 = 10.0*2.0 2.0 equals in calcPixel() 10.0-> cm to mm
		 */
	}

	public boolean getCVD() {
		return (CVD);
	}

	public void setCVD(boolean _b) {
		CVD = _b;
	}

	public int getCVDType() {
		return (CVDType);
	}

	public void setCVDType(int _i) throws LowVisionException {
		if (_i != 1 && _i != 2 && _i != 3) {
			throw new LowVisionException("CVD type must be 1,2, or 3");
		}
		CVDType = _i;
	}

	public boolean getColorFilter() {
		return (colorFilter);
	}

	public void setColorFilter(boolean _b) {
		colorFilter = _b;
	}

	public float[] getColorFilterRGB() {
		float[] rgb = { colorFilterR, colorFilterG, colorFilterB };
		return (rgb);
	}

	public void setColorFilterRGB(float _r, float _g, float _b)
			throws LowVisionException {
		if (_r < 0.0 || 1.0 < _r)
			throw new LowVisionException("Value of R(" + _r
					+ ") is out of range.");
		colorFilterR = _r;
		if (_g < 0.0 || 1.0 < _g)
			throw new LowVisionException("Value of G(" + _g
					+ ") is out of range.");
		colorFilterG = _g;
		if (_b < 0.0 || 1.0 < _b)
			throw new LowVisionException("Value of B(" + _b
					+ ") is out of range.");
		colorFilterB = _b;
	}

	public void setColorFilterRGB(float[] rgb) throws LowVisionException {
		if (rgb.length != 3) {
			throw new LowVisionException("The parameter's length must be 3.");
		}
		setColorFilterRGB(rgb[0], rgb[1], rgb[2]);
	}


	public void setColorFilterDegree(float _degree) throws LowVisionException {
		// TODO check color filter values
		float bDegree = _degree;
		// float bDegree = 1.0f-(1.0f-_degree)*0.9f;
		float rgDegree = 1 - (1 - bDegree) / 2.0f;
		setColorFilterRGB(rgDegree, rgDegree, bDegree);
	}

	public boolean getGlare() {
		return (glare);
	}

	public void setGlare(boolean _g) {
		glare = _g;
	}

	public float getGlareDegree() {
		return (glareDegree);
	}

	public void setGlareDegree(float _deg) {
		glareDegree = _deg;
	}

	//read simulation type from config file
	public void read(BufferedReader _br) throws LowVisionException {
		String oneLine = null;
		try {
			while ((oneLine = _br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(oneLine);
				String typeName = st.nextToken().toLowerCase();
				if (typeName.equals(COLOR_FILTER_STR)) {
					readColorFilter(st);
				} else if (typeName.equals(CVD_STR)) {
					readCVD(st);
				} else if (typeName.equals(EYESIGHT_STR)) {
					readEyesight(st);
				} else if (typeName.equals(GLARE_STR)) {
					readGlare(st);
				} else {
					throw new LowVisionException("Unknown type: " + typeName);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new LowVisionException("IO Error occurred while reading.");
		}
	}

	private void readColorFilter(StringTokenizer _st) throws LowVisionException {
		if (_st.countTokens() != 3) {
			throw new LowVisionException("Color filter needs three parameters.");
		}
		float tmpR = Float.parseFloat(_st.nextToken());
		float tmpG = Float.parseFloat(_st.nextToken());
		float tmpB = Float.parseFloat(_st.nextToken());
		setColorFilterRGB(tmpR, tmpG, tmpB);
		colorFilter = true;
	}

	private void readCVD(StringTokenizer _st) throws LowVisionException {
		if (_st.countTokens() != 1) {
			throw new LowVisionException("CVD needs only one parameter(type).");
		}
		int type = Integer.parseInt(_st.nextToken());
		setCVDType(type); 
		CVD = true;
	}

	private void readEyesight(StringTokenizer _st) throws LowVisionException {
		if (_st.countTokens() != 1) {
			throw new LowVisionException(
					"Eyesight needs only one parameter(degree).");
		}
		float tmp = Float.parseFloat(_st.nextToken());
		setEyesightDegree(tmp);
		eyesight = true;
	}

	private void readGlare(StringTokenizer _st) throws LowVisionException {
		if (_st.countTokens() != 1) {
			throw new LowVisionException(
					"Glare needs only one parameter(degree).");
		}
		float tmp = Float.parseFloat(_st.nextToken());
		setGlareDegree(tmp);
		glare = true;
	}

	public int countTypes() {
		int num = 0;

		if (eyesight)
			num++;
		if (CVD)
			num++;
		if (colorFilter)
			num++;
		if (glare)
			num++;

		return (num);
	}

	public boolean doBlur() {
		return (eyesight);
	}

	public boolean doChangeColors() {
		return (CVD || colorFilter || glare);
	}

	// need to use same order as in the LowVisionFilter
	public int convertColor(int _src) throws LowVisionException {
		int dest = _src;
		if (colorFilter) {
			dest = ColorFilterOp.convertColor(dest, colorFilterR, colorFilterG,
					colorFilterB);
		}
		if (glare) {
			dest = GlareOp.convertColor(dest, glareDegree);
		}
		if (CVD) {
			dest = CVDOp.convertColor(dest, CVDType);
		}
		return (dest);
	}

	public static int convertColor(int _src, LowVisionType _lvt)
			throws LowVisionException {
		return (_lvt.convertColor(_src));
	}

	public LowVisionType extractColorTypes() {
		if (!doChangeColors()) {
			return (null);
		}
		LowVisionType lv = null;
		try {
			lv = new LowVisionType();
			if (this.colorFilter) {
				lv.setColorFilter(true);
				lv.setColorFilterRGB(this.getColorFilterRGB());
			}
			if (this.CVD) {
				lv.setCVD(true);
				lv.setCVDType(this.getCVDType());
			}
			if (this.glare) {
				lv.setGlare(true);
				lv.setGlareDegree(this.glareDegree);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (lv);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (eyesight) {
			sb.append("Eyesight:on Degree=");
			sb.append("" + eyesightDegree);
		} else {
			sb.append("Eyesight:off,");
		}
		if (CVD) {
			sb.append("  CVD:on Type=" + CVDType + ",");
		} else {
			sb.append("  CVD: off,");
		}
		if (colorFilter) {
			sb.append("  ColorFilter:on Degree=" + colorFilterB);
		} else {
			sb.append("  ColorFilter:off");
		}
		return (sb.toString());
	}

	public double getDisplayHeight() {
		return displayHeight;
	}

	public int getDisplayResolution() {
		return displayResolution;
	}

	public double getEyeDisplayDistance() {
		return eyeDisplayDistance;
	}

	public void setDisplayHeight(double displayHeight) {
		this.displayHeight = displayHeight;
	}

	public void setDisplayResolution(int displayResolution) {
		this.displayResolution = displayResolution;
	}

	public void setEyeDisplayDistance(double eyeDisplayDistance) {
		this.eyeDisplayDistance = eyeDisplayDistance;
	}
}
