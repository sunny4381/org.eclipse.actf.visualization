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

package org.eclipse.actf.visualization.internal.engines.lowvision.checker;

import org.eclipse.actf.visualization.internal.engines.lowvision.color.ColorIRGB;

public class W3CColorChecker extends ColorChecker {
	private static final double THRESHOLD_Y = 125.0;

	private static final int THRESHOLD_C = 500;

	private double diffY;

	private int diffC;

	private double sevY;

	private double sevC;

	private double severity;

	public W3CColorChecker(ColorIRGB _c1, ColorIRGB _c2) {
		super(_c1, _c2);
		int r1 = color1.getR();
		int g1 = color1.getG();
		int b1 = color1.getB();
		int r2 = color2.getR();
		int g2 = color2.getG();
		int b2 = color2.getB();
		double y1 = calcY(r1, g1, b1);
		double y2 = calcY(r2, g2, b2);
		diffY = Math.abs(y1 - y2);
		diffC = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
		sevY = calcSevY(diffY);
		sevC = calcSevC(diffC);
		severity = Math.min(sevY, sevC);
	}

	public W3CColorChecker(int _i1, int _i2) {
		this(new ColorIRGB(_i1), new ColorIRGB(_i2));
	}

	public double calcSeverity() {
		return (this.severity);
	}

	public static double calcSeverity(ColorIRGB _c1, ColorIRGB _c2) {
		return ((new W3CColorChecker(_c1, _c2)).calcSeverity());
	}

	public double calcLuminanceSeverity() {
		return (this.sevY);
	}

	public static double calcLuminanceSeverity(ColorIRGB _c1, ColorIRGB _c2) {
		return ((new W3CColorChecker(_c1, _c2)).calcLuminanceSeverity());
	}

	public double calcChrominanceSeverity() {
		return (this.sevC);
	}

	public static double calcChrominanceSeverity(ColorIRGB _c1, ColorIRGB _c2) {
		return ((new W3CColorChecker(_c1, _c2)).calcChrominanceSeverity());
	}

	public int calcLuminanceDifference() {
		int iDiff = (int) (Math.round(this.diffY));
		if (iDiff < 0)
			iDiff = 0;
		else if (255 < iDiff)
			iDiff = 255;
		return (iDiff);
	}

	public static int calcLuminanceDifference(ColorIRGB _c1, ColorIRGB _c2) {
		return ((new W3CColorChecker(_c1, _c2)).calcLuminanceDifference());
	}

	public int calcChrominanceDifference() {
		return (this.diffC);
	}

	public static int calcChrominanceDifference(ColorIRGB _c1, ColorIRGB _c2) {
		return ((new W3CColorChecker(_c1, _c2)).calcChrominanceDifference());
	}

	private static double calcY(int _r, int _g, int _b) {
		return ((double) (_r * 299 + _g * 587 + _b * 114) / 1000.0);
	}

	private static double calcSevY(double _diffY) {
		double sevY = 0.0;
		if (_diffY < THRESHOLD_Y) {
			sevY = -_diffY / THRESHOLD_Y + 1.0;
		}
		if (sevY < 0.0)
			sevY = 0.0;
		else if (1.0 < sevY)
			sevY = 1.0;
		return (sevY);
	}

	private static double calcSevC(int _diffC) {
		double sevC = 0.0;
		if (_diffC < THRESHOLD_C) {
			sevC = -(double) _diffC / (double) THRESHOLD_C + 1.0;
		}
		if (sevC < 0.0)
			sevC = 0.0;
		else if (1.0 < sevC)
			sevC = 1.0;
		return (sevC);
	}
}
