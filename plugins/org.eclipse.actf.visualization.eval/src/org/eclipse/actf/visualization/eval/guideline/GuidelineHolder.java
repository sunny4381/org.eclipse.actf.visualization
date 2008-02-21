/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.eval.guideline;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.eclipse.actf.util.DebugPrintUtil;
import org.eclipse.actf.visualization.eval.EvaluationPlugin;
import org.eclipse.actf.visualization.eval.ICheckerInfoProvider;
import org.eclipse.actf.visualization.eval.IEvaluationItem;
import org.eclipse.actf.visualization.eval.IGuidelineItem;
import org.eclipse.actf.visualization.eval.extensions.CheckerExtension;
import org.eclipse.actf.visualization.eval.preferences.ICheckerPreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;

public class GuidelineHolder {

	private static GuidelineHolder INSTANCE = null;

	private IPreferenceStore preferenceStore = EvaluationPlugin.getDefault()
			.getPreferenceStore();

	private ICheckerInfoProvider[] checkerInfos = CheckerExtension.getCheckerInfoProviders();

	public static void main(String args[]) {
		GuidelineHolder gh = getInstance();

		for (Iterator i = gh.checkitemMap.keySet().iterator(); i.hasNext();) {
			IEvaluationItem tmpCI = (IEvaluationItem) gh.checkitemMap.get(i
					.next());
			if (!gh.enabledCheckitemSet.contains(tmpCI)) {
				System.out.println(tmpCI);
			}
		}

	}

	public static GuidelineHolder getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GuidelineHolder();
		}
		return (INSTANCE);
	}

	private Map<String, GuidelineData> guidelineMaps = new HashMap<String, GuidelineData>();

	private Set<GuidelineData> guidelineSet = new TreeSet<GuidelineData>(
			new GuidelineDataComparator());

	private GuidelineData[] guidelineArray;

	private GuidelineData[] leafGuidelineArray;

	private String[] guidelineNamesWithLevels;

	private String[] guidelineNames;

	private Map<String, IEvaluationItem> checkitemMap = new HashMap<String, IEvaluationItem>();

	private String[] metricsNames = new String[0];

	private boolean[][] correspondingMetricsOfLeafGuideline;

	private boolean[] enabledMetrics;

	private Set<IEvaluationItem> enabledCheckitemSet = new HashSet<IEvaluationItem>();

	private Set<IGuidelineItem> enabledGuidelineitemSet = new HashSet<IGuidelineItem>();

	private boolean[] matchedMetrics;

	private Set<IEvaluationItem> matchedCheckitemSet = new HashSet<IEvaluationItem>();

	private Set<IGuidelineItem> matchedGuidelineitemSet = new HashSet<IGuidelineItem>();

	private Set<IGuidelineSlectionChangedListener> guidelineSelectionChangedListenerSet = new HashSet<IGuidelineSlectionChangedListener>();

	private String currentMimeType = "text/html";

	// TODO guideline base -> check item base On/Off

	/**
	 * 
	 */
	private GuidelineHolder() {

		InputStream is = GuidelineItem.class
				.getResourceAsStream("resource/wcag10.xml");
		readGuidelines(is);
		is = GuidelineItem.class.getResourceAsStream("resource/section508.xml");
		readGuidelines(is);
		is = GuidelineItem.class.getResourceAsStream("resource/jis.xml");
		readGuidelines(is);
		is = GuidelineItem.class
				.getResourceAsStream("resource/IBMGuideline.xml");
		readGuidelines(is);

		for (ICheckerInfoProvider checkerInfo : checkerInfos) {
			InputStream[] iss = checkerInfo.getGuidelineInputStreams();
			if (null != iss) {
				DebugPrintUtil.devOrDebugPrintln(checkerInfo.getClass().getName()
						+ ":" + iss.length);
				for (InputStream tmpIs : iss) {
					readGuidelines(tmpIs);
				}
			}
		}

		guidelineNames = new String[guidelineSet.size()];
		guidelineArray = new GuidelineData[guidelineSet.size()];
		guidelineSet.toArray(guidelineArray);
		Vector<GuidelineData> tmpV = new Vector<GuidelineData>();
		Vector<String> tmpV2 = new Vector<String>();
		for (int i = 0; i < guidelineArray.length; i++) {
			GuidelineData data = guidelineArray[i];
			guidelineNames[i] = data.getGuidelineName();
			String[] levels = data.getLevels();
			if (levels.length > 0) {
				for (int j = 0; j < levels.length; j++) {
					GuidelineData subData = data.getSubLevelData(levels[j]);
					tmpV.add(subData);
					String name = subData.getGuidelineName() + " ("
							+ subData.getLevelStr() + ")";
					tmpV2.add(name);
				}
			} else {
				tmpV.add(data);
				tmpV2.add(data.getGuidelineName());
			}
		}
		leafGuidelineArray = new GuidelineData[tmpV.size()];
		tmpV.toArray(leafGuidelineArray);
		guidelineNamesWithLevels = new String[tmpV2.size()];
		tmpV2.toArray(guidelineNamesWithLevels);

		// check item config

		is = GuidelineItem.class.getResourceAsStream("resource/checkitem.xml");

		CheckItemReader cir = CheckItemReader.parse(is, this);
		Set<String> metricsNameSet = new HashSet<String>();
		if (cir.isSucceed()) {
			checkitemMap = cir.getCheckItemMap();
			metricsNameSet = cir.getMetricsSet();

			for (ICheckerInfoProvider checkerInfo : checkerInfos) {
				InputStream[] iss = checkerInfo.getCheckItemInputStreams();
				if (null != iss) {
					System.out.println(checkerInfo.getClass().getName() + ":"
							+ iss.length);
					for (InputStream tmpIs : iss) {
						try {
							cir = CheckItemReader.parse(tmpIs, this);
						} catch (Exception e) {
							System.out.println("can't parse: "
									+ checkerInfo.getClass().getName());
						}
						if (cir.isSucceed()) {
							checkitemMap.putAll(cir.getCheckItemMap());
							metricsNameSet.addAll(cir.getMetricsSet());
						}
					}
				}
			}

		} else {
			// TODO error report
		}

		metricsNames = new String[metricsNameSet.size()];
		metricsNameSet.toArray(metricsNames);

		enabledMetrics = new boolean[metricsNameSet.size()];
		Arrays.fill(enabledMetrics, true);

		for (Iterator i = checkitemMap.values().iterator(); i.hasNext();) {
			EvaluationItem tmpItem = (EvaluationItem) i.next();
			tmpItem.initTableData(guidelineNames, metricsNames);
		}

		initGuidelineNameLevel2checkItem();
		initDisabledGuideline();
		initDisabledMetrics();
		initCorrespondingMetrics();
		resetMatchedItems();
	}

	public GuidelineData[] getLeafGuidelineData() {
		return (leafGuidelineArray);
	}

	public GuidelineData[] getGuidelineData() {
		return (guidelineArray);
	}

	public String[] getGuidelineNamesWithLevels() {
		return (guidelineNamesWithLevels);
	}

	public IGuidelineItem getGuidelineItem(String guidelineName, String id) {
		if (guidelineMaps.containsKey(guidelineName)) {
			GuidelineData data = (GuidelineData) guidelineMaps
					.get(guidelineName);
			return (data.getGuidelineItem(id));
		}
		return (null);
	}

	public EvaluationItem getCheckItem(String id) {
		if (checkitemMap.containsKey(id)) {
			return ((EvaluationItem) checkitemMap.get(id));
		}
		return (null);
	}

	public boolean setEnabledGuidelineWithLevels(boolean[] enabledItems) {
		if (enabledItems.length != leafGuidelineArray.length) {
			return false;
		}

		enabledCheckitemSet.clear();
		enabledGuidelineitemSet.clear();

		for (int i = 0; i < enabledItems.length; i++) {
			GuidelineData data = leafGuidelineArray[i];
			data.setEnabled(enabledItems[i]);
			if (enabledItems[i]) {
				addEnabledItems(data);
			}
		}

		storeDisabledGuideline();
		resetMatchedItems();
		notifyGuidelineSelectionChange();
		return true;
	}

	public void setEnabledGuidelines(String[] guidelineNameArray,
			String[] levelArray) {

		for (int i = 0; i < leafGuidelineArray.length; i++) {
			leafGuidelineArray[i].setEnabled(false);
		}

		if (guidelineNameArray != null && levelArray != null
				&& guidelineNameArray.length == levelArray.length) {
			enabledCheckitemSet.clear();
			enabledGuidelineitemSet.clear();

			for (int i = 0; i < guidelineNameArray.length; i++) {
				if (guidelineMaps.containsKey(guidelineNameArray[i])) {
					GuidelineData data = (GuidelineData) guidelineMaps
							.get(guidelineNameArray[i]);
					if (levelArray[i] == null) {
						data.setEnabled(true);
						addEnabledItems(data);
					} else {
						data = data.getSubLevelData(levelArray[i]);
						if (data != null) {
							data.setEnabled(true);
							addEnabledItems(data);
						}
					}

				}
			}

			storeDisabledGuideline();
			resetMatchedItems();
			notifyGuidelineSelectionChange();
		}

	}

	private void addEnabledItems(GuidelineData data) {
		if (data.isEnabled()) {
			enabledCheckitemSet.addAll(data.getCheckItemSet());
			enabledGuidelineitemSet.addAll(data.getGuidelineItemMap().values());
		}
	}

	private void resetMatchedItems() {
		matchedCheckitemSet.clear();
		matchedGuidelineitemSet.clear();
		matchedMetrics = new boolean[metricsNames.length];
		Arrays.fill(matchedMetrics, false);

		for (int i = 0; i < leafGuidelineArray.length; i++) {
			GuidelineData data = leafGuidelineArray[i];
			data.setCurrentMIMEtype(currentMimeType);
			if (data.isMatched()) {
				matchedCheckitemSet.addAll(data.getCheckItemSet());
				matchedGuidelineitemSet.addAll(data.getGuidelineItemMap()
						.values());
				for (int j = 0; j < metricsNames.length; j++) {
					matchedMetrics[j] = (enabledMetrics[j] && (matchedMetrics[j] | correspondingMetricsOfLeafGuideline[i][j]));
				}
			}
		}

		for (int i = 0; i < guidelineArray.length; i++) {
			guidelineArray[i].setCurrentMIMEtype(currentMimeType);
		}

		// System.out.println(matchedCheckitemSet.size() + " " +
		// matchedGuidelineitemSet.size());

	}

	public boolean setEnabledMetrics(boolean[] enabledMetrics) {
		if (enabledMetrics == null
				|| enabledMetrics.length != this.enabledMetrics.length) {
			return false;
		}
		this.enabledMetrics = enabledMetrics;
		storeDisabledMetrics();
		resetMatchedItems();
		notifyGuidelineSelectionChange();
		return true;
	}

	public void setEnabledMetrics(String[] enabledMetricsStringArray) {
		if (enabledMetricsStringArray != null) {
			Arrays.fill(enabledMetrics, false);

			for (int i = 0; i < metricsNames.length; i++) {
				for (int j = 0; j < enabledMetricsStringArray.length; j++) {
					if (metricsNames[i]
							.equalsIgnoreCase(enabledMetricsStringArray[j])) {
						enabledMetrics[i] = true;
						break;
					}
				}
			}

			storeDisabledMetrics();
			resetMatchedItems();
			notifyGuidelineSelectionChange();
		}
	}

	public Set getMatchedCheckitemSet() {
		// 061018 kf
		// return enabledCheckitemSet;
		return matchedCheckitemSet;
	}

	public String[] getMetricsNames() {
		return metricsNames;
	}

	private void initGuidelineNameLevel2checkItem() {
		for (Iterator i = guidelineMaps.values().iterator(); i.hasNext();) {
			((GuidelineData) i.next()).setCheckItems(checkitemMap.values(),
					metricsNames);
		}

	}

	private void initDisabledGuideline() {

		try {

			for (int i = 0; i < guidelineArray.length; i++) {
				GuidelineData data = guidelineArray[i];
				String[] subLevels = data.getLevels();
				if (subLevels.length == 0) {
					String tmpS = ICheckerPreferenceConstants.GUIDELINE_PREFIX
							+ data.getGuidelineName() + "_";
					if (preferenceStore.contains(tmpS)
							&& preferenceStore.getBoolean(tmpS)) {
						data.setEnabled(false);
					} else {
						data.setEnabled(true);
					}
				} else {
					for (int j = 0; j < subLevels.length; j++) {
						GuidelineData subData = data
								.getSubLevelData(subLevels[j]);
						String tmpS = ICheckerPreferenceConstants.GUIDELINE_PREFIX
								+ subData.getGuidelineName() + "_" + j;
						if (preferenceStore.contains(tmpS)
								|| preferenceStore.getBoolean(tmpS)) {
							subData.setEnabled(false);
						} else {
							subData.setEnabled(true);
						}
					}
				}

			}
		} catch (Exception e) {
		}
	}

	private void storeDisabledGuideline() {
		try {
			for (int i = 0; i < guidelineArray.length; i++) {
				GuidelineData data = guidelineArray[i];
				String[] subLevels = data.getLevels();
				if (subLevels.length == 0) {
					preferenceStore.setValue(
							ICheckerPreferenceConstants.GUIDELINE_PREFIX
									+ data.getGuidelineName() + "_", !data
									.isEnabled());
				} else {
					for (int j = 0; j < subLevels.length; j++) {
						GuidelineData subData = data
								.getSubLevelData(subLevels[j]);
						preferenceStore.setValue(
								ICheckerPreferenceConstants.GUIDELINE_PREFIX
										+ subData.getGuidelineName() + "_" + j,
								!subData.isEnabled());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initDisabledMetrics() {
		try {
			for (int j = 0; j < metricsNames.length; j++) {
				String tmpS = ICheckerPreferenceConstants.METRICS_PREFIX
						+ metricsNames[j];
				if (preferenceStore.contains(tmpS)
						&& preferenceStore.getBoolean(tmpS)) {
					enabledMetrics[j] = false;
				}
			}
		} catch (Exception e) {
		}
	}

	private void initCorrespondingMetrics() {
		correspondingMetricsOfLeafGuideline = new boolean[leafGuidelineArray.length][metricsNames.length];

		for (int i = 0; i < leafGuidelineArray.length; i++) {
			correspondingMetricsOfLeafGuideline[i] = leafGuidelineArray[i]
					.getCorrespondingMetrics();
		}
	}

	private void storeDisabledMetrics() {
		try {
			for (int i = 0; i < enabledMetrics.length; i++) {
				if (!enabledMetrics[i]) {
					preferenceStore.setValue(
							ICheckerPreferenceConstants.METRICS_PREFIX
									+ metricsNames[i], true);
				}
			}
		} catch (Exception e) {
		}
	}

	public boolean isMatchedCheckItem(IEvaluationItem target) {
		return (matchedCheckitemSet.contains(target));
	}

	public boolean isMatchedGuidelineItem(IGuidelineItem target) {
		return (matchedGuidelineitemSet.contains(target));
	}

	public boolean isMatchedInTopLevel(IGuidelineItem target) {
		if (guidelineMaps.containsKey(target.getGuidelineName())) {
			GuidelineData data = (GuidelineData) guidelineMaps.get(target
					.getGuidelineName());
			return (data.isEnabled() && data.isTargetMIMEtype(currentMimeType));
		}
		return (false);
	}

	public boolean isEnabledMetric(String metricName) {
		for (int i = 0; i < metricsNames.length; i++) {
			if (metricsNames[i].equalsIgnoreCase(metricName)) {
				return (enabledMetrics[i]);
			}
		}
		return (false);
	}

	public boolean isMatchedMetric(String metricName) {
		for (int i = 0; i < metricsNames.length; i++) {
			if (metricsNames[i].equalsIgnoreCase(metricName)) {
				return (matchedMetrics[i]);
			}
		}
		return (false);
	}

	private void readGuidelines(InputStream is) {
		GuidelineData data = GuidelineItemReader.getGuidelineData(is);
		if (data != null) {
			guidelineMaps.put(data.getGuidelineName(), data);
			if (!guidelineSet.add(data)) {
				guidelineSet.remove(data);
				guidelineSet.add(data);
			}
		}
	}

	public boolean[] getEnabledMetrics() {
		return enabledMetrics;
	}

	public boolean[] getMatchedMetrics() {
		return matchedMetrics;
	}

	public void addGuidelineSelectionChangedListener(
			IGuidelineSlectionChangedListener listener) {
		guidelineSelectionChangedListenerSet.add(listener);
	}

	public void removeGuidelineSelectionChangedListener(
			IGuidelineSlectionChangedListener listener) {
		guidelineSelectionChangedListenerSet.remove(listener);
	}

	private void notifyGuidelineSelectionChange() {
		GuidelineSelectionChangedEvent event = new GuidelineSelectionChangedEvent();
		for (Iterator i = guidelineSelectionChangedListenerSet.iterator(); i
				.hasNext();) {
			((IGuidelineSlectionChangedListener) i.next())
					.selectionChanged(event);
		}
	}

	public String getTargetMimeType() {
		return currentMimeType;
	}

	public void setTargetMimeType(String currentMimeType) {
		if (currentMimeType != null
				&& !currentMimeType.equals(this.currentMimeType)) {
			this.currentMimeType = currentMimeType;
			resetMatchedItems();
			notifyGuidelineSelectionChange();
		}
	}

	public boolean[][] getCorrespondingMetricsOfLeafGuideline() {
		return correspondingMetricsOfLeafGuideline;
	}

	public String[] getGuidelineNames() {
		return guidelineNames;
	}

}
