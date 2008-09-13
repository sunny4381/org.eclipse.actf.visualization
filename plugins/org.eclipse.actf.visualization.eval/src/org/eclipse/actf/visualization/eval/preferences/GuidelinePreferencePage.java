/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.eval.preferences;

import java.util.Arrays;
import java.util.List;

import org.eclipse.actf.ui.preferences.GroupFieldEditorPreferencePage;
import org.eclipse.actf.visualization.eval.guideline.GuidelineData;
import org.eclipse.actf.visualization.eval.guideline.GuidelineHolder;
import org.eclipse.actf.visualization.internal.eval.EvaluationPlugin;
import org.eclipse.actf.visualization.internal.eval.Messages;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class GuidelinePreferencePage extends GroupFieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	public static final String ID = GuidelinePreferencePage.class.getName();

	private GuidelineHolder _guidelineHolder;

	private String[] _checkerOptionNames;

	private String[] _criteriaNames;

	private Button[] _criteriaCheckButtons;

	private CheckboxTreeViewer _guidelineTreeViewer;

	private TreeItem[] _guidelineTreeItems = null;

	public GuidelinePreferencePage() {
		super();
		setPreferenceStore(EvaluationPlugin.getDefault().getPreferenceStore());
	}

	public void init(IWorkbench workbench) {
		this._guidelineHolder = GuidelineHolder.getInstance();
		this._checkerOptionNames = this._guidelineHolder
				.getGuidelineNamesWithLevels();
		this._criteriaNames = this._guidelineHolder.getMetricsNames();
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();

		createGuidelineTreePart(parent);

		// createPropertiesPart(parent);

		createCriteriaOptionPart(parent);

		createLineNumberInfoPart(parent);

		updateSelectableMetricsButton();

		Composite buttonComposite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
		gridData.verticalIndent = 5;
		buttonComposite.setLayoutData(gridData);
		buttonComposite.setLayout(new GridLayout());

		Label spacer = new Label(parent, SWT.NONE);
		gridData = new GridData();
		gridData.verticalIndent = 10;
		spacer.setLayoutData(gridData);

	}

	private void createGuidelineTreePart(Composite parent) {

		final Group guidelineTreeGroup = new Group(parent, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 280;
		guidelineTreeGroup.setLayoutData(gridData);
		guidelineTreeGroup.setLayout(new GridLayout());

		guidelineTreeGroup.setText(Messages
				.getString("adesigner.preference.guideline.list.group.text"));

		this._guidelineTreeViewer = new CheckboxTreeViewer(guidelineTreeGroup,
				SWT.BORDER);
		this._guidelineTreeViewer
				.setLabelProvider(new GuidelineTreeLabelProvider());
		this._guidelineTreeViewer
				.setContentProvider(new GuidelineTreeContentProvider());

		Tree guidelineTree = this._guidelineTreeViewer.getTree();
		guidelineTree.setLinesVisible(true);
		guidelineTree.setHeaderVisible(true);
		guidelineTree
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		TreeColumn categoryColumn = new TreeColumn(guidelineTree, SWT.NONE);
		categoryColumn.setWidth(220);
		categoryColumn.setText("Category");

		TreeColumn guidelineColumn = new TreeColumn(guidelineTree, SWT.NONE);
		guidelineColumn.setWidth(350);
		guidelineColumn.setText("Guideline");

		GuidelineTreeItemData root = new GuidelineTreeItemData(null);
		GuidelineTreeItemType htmlType = new GuidelineTreeItemType(
				GuidelineTreeItemType.TYPE_HTML);
		GuidelineTreeItemType odfType = new GuidelineTreeItemType(
				GuidelineTreeItemType.TYPE_ODF);
		root.add(htmlType);
		root.add(odfType);

		GuidelineData[] guidelineDataArray = this._guidelineHolder
				.getLeafGuidelineData();
		this._checkerOptionNames = this._guidelineHolder
				.getGuidelineNamesWithLevels();

		int guidelineNum = this._checkerOptionNames.length;

		boolean[][] correspondingCriteria = this._guidelineHolder
				.getCorrespondingMetricsOfLeafGuideline();
		for (int i = 0; i < guidelineNum; i++) {
			boolean isHTMLType = false;
			String[] mimeTypes = guidelineDataArray[i].getMIMEtypes();
			for (int j = 0; j < mimeTypes.length; j++) {
				if (mimeTypes[j].equals("text/html")
						|| mimeTypes[j].equals("application/xhtml+xml")) {
					isHTMLType = true;
					break;
				}
			}
			GuidelineTreeItemData guidelineData = new GuidelineTreeItemData(
					this._checkerOptionNames[i]);
			guidelineData.setCorrespondingCriteria(correspondingCriteria[i]);
			if (isHTMLType) {
				htmlType.add(guidelineData);
			} else {
				odfType.add(guidelineData);
			}

			guidelineData.setIndex(i);
			guidelineData.setEnabled(guidelineDataArray[i].isEnabled());
			guidelineData.setCategory(guidelineDataArray[i].getCategory());
			guidelineData
					.setDescription(guidelineDataArray[i].getDescription());
		}

		this._guidelineTreeViewer.setInput(root);
		this._guidelineTreeViewer.expandAll();

		this._guidelineTreeViewer
				.addCheckStateListener(new ICheckStateListener() {
					public void checkStateChanged(CheckStateChangedEvent event) {
						if (event.getElement() instanceof GuidelineTreeItemType) {
							GuidelineTreeItemType guidelineType = (GuidelineTreeItemType) event
									.getElement();
							List<IGuidelineTreeItem> children = guidelineType
									.getChildren();
							for (int i = 0; i < children.size(); i++) {
								GuidelineTreeItemData guidelineData = (GuidelineTreeItemData) children
										.get(i);
								boolean checkState = event.getChecked();
								guidelineData.setEnabled(checkState);
								_guidelineTreeItems[guidelineData.getIndex()]
										.setChecked(checkState);
							}
						}

						updateSelectableMetricsButton();
					}
				});

		this._guidelineTreeItems = new TreeItem[guidelineNum];
		TreeItem[] typeTreeItem = guidelineTree.getItems();
		for (int i = 0; i < typeTreeItem.length; i++) {
			boolean isAllEnabled = true;

			TreeItem[] guidelineTreeItem = typeTreeItem[i].getItems();
			for (int j = 0; j < guidelineTreeItem.length; j++) {
				GuidelineTreeItemData guidelineData = (GuidelineTreeItemData) guidelineTreeItem[j]
						.getData();

				guidelineTreeItem[j].setText(new String[] {
						guidelineData.getCategory(),
						guidelineData.getDescription() });
				guidelineTreeItem[j].setImage(new Image[] { null,
						guidelineData.getGuidelineImage() });

				boolean isGuidelineEnabled = guidelineData.isEnabled();
				guidelineTreeItem[j].setChecked(isGuidelineEnabled);
				if (!isGuidelineEnabled) {
					isAllEnabled = false;
				}

				this._guidelineTreeItems[guidelineData.getIndex()] = guidelineTreeItem[j];
			}

			typeTreeItem[i].setChecked(isAllEnabled);
		}
	}

	private void createCriteriaOptionPart(Composite parent) {

		int columnNum = 4;
		Group criteriaGroup = new Group(parent, SWT.NONE);
		criteriaGroup
				.setText(Messages
						.getString("adesigner.preference.guideline.criteria.group.text"));
		criteriaGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = columnNum;
		criteriaGroup.setLayout(gridLayout);

		int length = this._criteriaNames.length;
		this._criteriaCheckButtons = new Button[length];
		boolean[] isOptionEnabled = this._guidelineHolder.getEnabledMetrics();

		GridData gridData;
		for (int i = 0; i < length; i++) {
			this._criteriaCheckButtons[i] = new Button(criteriaGroup, SWT.CHECK);
			this._criteriaCheckButtons[i].setText(this._criteriaNames[i]);
			this._criteriaCheckButtons[i].setSelection(isOptionEnabled[i]);
			if (i % columnNum != 0) {
				gridData = new GridData();
				gridData.horizontalIndent = 20;
				this._criteriaCheckButtons[i].setLayoutData(gridData);
			}
		}
	}

	private void createLineNumberInfoPart(Composite parent) {

		addField(new RadioGroupFieldEditor(
				IPreferenceConstants.CHECKER_TARGET,
				Messages
						.getString("DialogCheckerOption.Line_Number_Information_19"),
				1,
				new String[][] {
						{
								Messages
										.getString("DialogCheckerOption.Add_line_number_20"),
								IPreferenceConstants.CHECKER_ORG_DOM },
						{ Messages.getString("DialogCheckerOption.LIVE_DOM"),
								IPreferenceConstants.CHECKER_LIVE_DOM } },
				parent));

	}

	public boolean performOk() {

		boolean isOK = super.performOk();

		setParameters();

		if (this._guidelineHolder.isEnabledMetric("Navigability")) {

			GuidelineData[] datas = this._guidelineHolder
					.getLeafGuidelineData();
			boolean isWcagOn = false;
			boolean isWcagOff = false;
			boolean isOtherComp = false;

			for (int i = 0; i < datas.length; i++) {
				if (datas[i].isEnabled()) {
					if (datas[i].getGuidelineName().matches(
							"Section508|JIS|IBMGuideline")) {
						isOtherComp = true;
					} else if (datas[i].getGuidelineName().equals("WCAG")) {
						isWcagOn = true;
					}
				} else {
					if (datas[i].getGuidelineName().equals("WCAG")) {
						isWcagOff = true;
					}
				}
			}

			if (!isOtherComp && isWcagOn && isWcagOff) {
				NavigabilityWarningDialog nwd = new NavigabilityWarningDialog(
						getShell());
				int result = nwd.open();
				switch (result) {
				case NavigabilityWarningDialog.ENABLE_ALL:
					for (int i = 0; i < this._guidelineTreeItems.length; i++) {
						if (this._guidelineTreeItems[i].getText().indexOf(
								"WCAG") > -1) {
							this._guidelineTreeItems[i].setChecked(true);
						}
					}
					return true;
				case NavigabilityWarningDialog.DISABLE_NAVIGABILITY:
					for (int i = 0; i < this._criteriaCheckButtons.length; i++) {
						if (this._criteriaCheckButtons[i].getText().indexOf(
								"Navigability") > -1) {
							this._criteriaCheckButtons[i].setSelection(false);
						}
					}
					return true;
				case NavigabilityWarningDialog.CONTINUE:
				default:
					// do nothing
				}
			}
		}

		return isOK;
	}

	private void setParameters() {

		if (null != _criteriaCheckButtons) {
			boolean[] result = new boolean[this._criteriaCheckButtons.length];
			Arrays.fill(result, false);
			for (int i = 0; i < _criteriaCheckButtons.length; i++) {
				if (this._criteriaCheckButtons[i] != null
						&& this._criteriaCheckButtons[i].isEnabled()
						&& this._criteriaCheckButtons[i].getSelection()) {
					result[i] = true;
				}
			}
			this._guidelineHolder.setEnabledMetrics(result);
		}

		if (null != this._guidelineTreeItems) {
			boolean[] result = new boolean[this._guidelineTreeItems.length];
			Arrays.fill(result, false);
			for (int i = 0; i < this._guidelineTreeItems.length; i++) {
				if (null != this._guidelineTreeItems[i]
						&& this._guidelineTreeItems[i].getChecked()) {
					result[i] = true;
				}
			}
			this._guidelineHolder.setEnabledGuidelineWithLevels(result);
		}
	}

	private void updateSelectableMetricsButton() {

		boolean[] isSelectable = new boolean[this._criteriaCheckButtons.length];
		Arrays.fill(isSelectable, false);

		for (int i = 0; i < this._guidelineTreeItems.length; i++) {
			if (this._guidelineTreeItems[i].getChecked()) {
				GuidelineTreeItemData guidelineData = (GuidelineTreeItemData) this._guidelineTreeItems[i]
						.getData();
				for (int j = 0; j < this._criteriaCheckButtons.length; j++) {
					isSelectable[j] = (isSelectable[j] | guidelineData
							.getCorrespondingCriteria()[j]);
				}
			}
		}

		for (int i = 0; i < this._criteriaCheckButtons.length; i++) {
			this._criteriaCheckButtons[i].setEnabled(isSelectable[i]);
		}
	}

}
