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
package org.eclipse.actf.visualization.gui.preferences;


import org.eclipse.actf.ui.preferences.GroupFieldEditorPreferencePage;
import org.eclipse.actf.visualization.gui.internal.GuiPlugin;
import org.eclipse.actf.visualization.gui.internal.util.Messages;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class GuiPreferencePage extends GroupFieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

    private static final String SAMPLE_STRING = "Sample"; //$NON-NLS-1$
	public GuiPreferencePage() {
		super();
		setPreferenceStore(GuiPlugin.getDefault().getPreferenceStore());
		setDescription(Messages.getString("msaa.preference_description")); //$NON-NLS-1$
	}

	public void createFieldEditors() {
        Group jawsGroup = createFieldGroup(Messages.getString("msaa.jaws_simulation")); //$NON-NLS-1$
        addField(new FontFieldEditor(GuiPreferenceConstants.JAWSTextView_Font,Messages.getString("msaa.font"),SAMPLE_STRING,jawsGroup)); //$NON-NLS-1$
        Group eventGroup = createFieldGroup(Messages.getString("msaa.msaa_event")); //$NON-NLS-1$
        addField(new FontFieldEditor(GuiPreferenceConstants.MSAAEventView_Font,Messages.getString("msaa.font"),SAMPLE_STRING,eventGroup)); //$NON-NLS-1$
        Group accGroup = createFieldGroup(Messages.getString("msaa.vis_label")); //$NON-NLS-1$
        Label accNote = new Label(accGroup,SWT.NONE);
        accNote.setText(Messages.getString("msaa.vis_notice") ); //$NON-NLS-1$
        GridData gd = new GridData();
        gd.horizontalSpan = 2;
        accNote.setLayoutData(gd);
        addField(new BooleanFieldEditor(GuiPreferenceConstants.UseOverlayWindow,Messages.getString("msaa.vis_use_overlay"),accGroup)); //$NON-NLS-1$
        addField(new BooleanFieldEditor(GuiPreferenceConstants.AlwaysOnTop,Messages.getString("msaa.vis_always_top"),accGroup)); //$NON-NLS-1$
    }

	public void init(IWorkbench workbench) {
	}
}
