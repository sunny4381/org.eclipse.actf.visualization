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
		setDescription(Messages.msaa_preference_description); 
	}

	public void createFieldEditors() {
        Group jawsGroup = createFieldGroup(Messages.msaa_jaws_simulation); 
        addField(new FontFieldEditor(GuiPreferenceConstants.JAWSTextView_Font,Messages.msaa_font,SAMPLE_STRING,jawsGroup)); 
        Group eventGroup = createFieldGroup(Messages.msaa_msaa_event); 
        addField(new FontFieldEditor(GuiPreferenceConstants.MSAAEventView_Font,Messages.msaa_font,SAMPLE_STRING,eventGroup)); 
        Group accGroup = createFieldGroup(Messages.msaa_vis_label); 
        Label accNote = new Label(accGroup,SWT.NONE);
        accNote.setText(Messages.msaa_vis_notice ); 
        GridData gd = new GridData();
        gd.horizontalSpan = 2;
        accNote.setLayoutData(gd);
        addField(new BooleanFieldEditor(GuiPreferenceConstants.UseOverlayWindow,Messages.msaa_vis_use_overlay,accGroup)); 
        addField(new BooleanFieldEditor(GuiPreferenceConstants.AlwaysOnTop,Messages.msaa_vis_always_top,accGroup)); 
    }

	public void init(IWorkbench workbench) {
	}
}
