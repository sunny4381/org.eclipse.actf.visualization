/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.actf.visualization.engines.blind.ui.preferences.IBlindPreferenceConstants;
import org.eclipse.actf.visualization.internal.engines.blind.BlindVizEnginePlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.PreferenceStore;

/**
 * Utility class to check alternative text
 */
public class TextChecker {

	private static String KIGOU = "(\\p{InMathematicalOperators}|\\p{InGeometricShapes}|\\p{InMiscellaneousSymbols}|\\p{InBoxDrawing}|\\p{InGeneralPunctuation}|\\p{InCJKSymbolsandPunctuation}|\\p{InArrows})";

	private static String NIHONGO = "(\\p{InCJKUnifiedIdeographs}|\\p{InHiragana}|\\p{InKatakana})";

	private static String KANJI = "(\\p{InCJKUnifiedIdeographs})";

	private static final String ALT_TEXT_PROPERTIES_FILE = "altText.properties";

	private static String INAPP_ALT = "blindViz.inappropriateAlt_";

	private static String POSSIBLE_INAPP_ALT = "blindViz.possible_inappAlt_";

	private static TextChecker INSTANCE;

	private Set<String> ngwordset = new TreeSet<String>();

	private Set<String> ngwordset2 = new TreeSet<String>();

	private Preferences pref = BlindVizEnginePlugin.getDefault()
			.getPluginPreferences();

	// TODO spell out check

	// separated from VisualizeEngine
	private TextChecker() {

		if (!pref.getBoolean(IBlindPreferenceConstants.NOT_FIRST_TIME)) {

			Properties prop = new Properties();
			try {
				InputStream prefIS = FileLocator.openStream(Platform
						.getBundle(BlindVizEnginePlugin.PLUGIN_ID), new Path(
						"config/" + ALT_TEXT_PROPERTIES_FILE), false);
				if (prefIS != null) {
					prop.load(prefIS);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (Object key : prop.keySet()) {
				String keyS = (String) key;
				String value;
				if (keyS.startsWith(INAPP_ALT)) {
					value = prop.getProperty(keyS);
					if (value.length() > 0) {
						ngwordset.add(value);
					}
				} else if (keyS.startsWith(POSSIBLE_INAPP_ALT)) {
					value = prop.getProperty(keyS);
					if (value.length() > 0) {
						ngwordset2.add(value);
					}
				}
			}

			resetPreferences();

		} else {
			for (int i = 0; pref.contains(INAPP_ALT + i); i++) {
				String value = pref.getString(INAPP_ALT + i);
				if (value.length() > 0) {
					ngwordset.add(value);
				}
			}
			for (int i = 0; pref.contains(POSSIBLE_INAPP_ALT + i); i++) {
				String value = pref.getString(POSSIBLE_INAPP_ALT + i);
				if (value.length() > 0) {
					ngwordset2.add(value);
				}
			}
		}
	}

	/**
	 * Get instance of {@link TextChecker}
	 * 
	 * @return instance of {@link TextChecker}
	 */
	public static TextChecker getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TextChecker();
		}
		return INSTANCE;
	}

	/**
	 * Check redundancy of text
	 * 
	 * @param prevText
	 *            previous text
	 * @param curText
	 *            target text to check
	 * @return true if the target text is redundant with previous text
	 */
	public boolean isRedundantText(String prevText, String curText) {

		if ((prevText != null) && (prevText.length() > 1)
				&& (curText.length() > 1)) {

			String prevText2 = prevText.replaceAll("\\[|\\]|\\.|\\!|\\>", "");
			prevText2 = prevText2.trim();

			String curText2 = curText.replaceAll("\\[|\\]|\\.|\\!|\\>", "");
			curText2 = curText2.trim();

			if (curText2.equalsIgnoreCase(prevText2)) {
				return true;
			}
		}
		return (false);
	}

	/**
	 * Check appropriateness of alternative text.
	 * 
	 * @param alt
	 *            target alternative text
	 * @return true if the alternative text matches with inappropriate
	 *         alternative text {@link Set}
	 */
	public boolean isInappropriateAlt(String alt) {
		String tmpAlt = alt.trim();
		tmpAlt = tmpAlt.toLowerCase();

		if (ngwordset.contains(tmpAlt) || isEndWithImageExt(tmpAlt)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check possibly inappropriate alternative text.
	 * 
	 * @param alt
	 *            target alternative text
	 * @return
	 *            <ul>
	 *            <li>0: OK</li>
	 *            <li>1: possibly inappropriate</li>
	 *            <li>2: inappropriate</li>
	 *            <li>3: space separated chars</li>
	 *            </ul>
	 */
	public int checkInappropriateAlt(String alt) {
		String[] tmpSA = alt.toLowerCase().split(
				"(" + KIGOU + "|\\p{Punct}|\\p{Space})");
		int count = 0;
		int all = 0;
		int charLength = 0;
		for (int i = 0; i < tmpSA.length; i++) {
			if (tmpSA[i].length() > 0) {
				all++;
			}
			charLength += tmpSA[i].length();
			if (ngwordset2.contains(tmpSA[i])) {
				// System.out.println("alt: "+tmpSA[i]);
				count++;
			}
		}

		int org = alt.length();

		// TODO combination
		if (org > 0 && alt.matches(".*(\\p{Alpha}\\p{Space}){4,}.*")) {// TODO
			// 4 is
			// appropriate?
			return 3;
		}

		// TODO Japanese check
		if (org > 0 && ((double) charLength / (double) org) < 0.5) {
			// TODO divide error (use ratio)

			// spaces
			if (!alt.matches("\\p{Space}*")) {
				return 1;
			}
		}

		// System.out.println(count+" "+all+":"+(double)count/(double)all);
		if ((double) count / (double) all > 0.6) {
			return 2;
		} else if ((double) count / (double) all > 0.3) {
			return 1;
		}
		return 0;
	}

	private boolean isEndWithImageExt(String alt) {
		String tmpS = alt.trim().toLowerCase();
		String regexp3 = "\\p{Print}*\\.(jpg|jpeg|gif|png)";
		return (tmpS.matches(regexp3));
	}

	/**
	 * Check if the target String is space separated Japanese chars or not
	 * 
	 * @param target
	 *            target String
	 * @return true if the target String is space separated Japanese chars
	 */
	public boolean isSeparatedJapaneseChars(String target) {
		String tmpS = target.trim();
		tmpS = tmpS.toLowerCase();

		// \u3000 = double byte space
		String regexp1 = KIGOU + "*(\\p{Space}|\u3000)?(" + NIHONGO
				+ "((\\p{Space}|\u3000)+" + NIHONGO + ")+)"
				+ "(\\p{Space}|\u3000)?" + KIGOU + "*";
		String regexp2 = ".*" + KANJI + ".*";
		// TODO rewrite regexp (combination of Japanese and English)

		if ((tmpS.matches(regexp1) && tmpS.matches(regexp2))) {
			return true;
		} else {
			return (false);
		}
	}

	/**
	 * Get inappropriate alternative text {@link Set}
	 * 
	 * @return {@link Set} of inappropriate alternative text
	 */
	public Set<String> getInappropriateALTSet() {
		Set<String> tmpSet = new HashSet<String>();
		tmpSet.addAll(ngwordset);
		return tmpSet;
	}

	private void resetPreferences() {
		int i = 0;
		for (String value : ngwordset) {
			pref.setValue(INAPP_ALT + i, value);
			i++;
		}
		for (int j = i; pref.contains(INAPP_ALT + j); j++) {
			pref.setValue(INAPP_ALT + j, "");
		}

		i = 0;
		for (String value : ngwordset2) {
			pref.setValue(POSSIBLE_INAPP_ALT + i, value);
			i++;
		}
		for (int j = i; pref.contains(POSSIBLE_INAPP_ALT + j); j++) {
			pref.setValue(POSSIBLE_INAPP_ALT + j, "");
		}
	}

	/**
	 * Set inappropriate alternative text {@link Set}. The new {@link Set} will
	 * be stored into {@link PreferenceStore}.
	 * 
	 * @param inappAltSet
	 *            {@link Set} of inappropriate alternative text
	 */
	public void setInappropriateAltSet(Set<String> inappAltSet) {
		if (inappAltSet != null) {
			ngwordset = inappAltSet;
		}
		resetPreferences();
	}

}
