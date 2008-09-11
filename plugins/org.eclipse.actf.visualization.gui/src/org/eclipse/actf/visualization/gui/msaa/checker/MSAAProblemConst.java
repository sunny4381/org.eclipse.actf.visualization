/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.gui.msaa.checker;

import org.eclipse.actf.visualization.gui.internal.util.Messages;

public interface MSAAProblemConst {
    public static final int MSAA_ERROR = 1;

    public static final int MSAA_WARNING = 2;

    public static final int MSAA_INFORMATION = 3;

    public static final int MSAA_PROB_NO_ALT_BUTTON = 0;

    public static final int MSAA_PROB_NO_ALT_GRAHPIC = 1;

    public static final int MSAA_PROB_NO_ALT_CLICKABLE_OBJECT = 2;

    public static final int MSAA_PROB_NO_ALT_NONCLICKABLE_OBJECT = 3;

    public static final int MSAA_PROB_NO_ALT_FORM_CONTROL = 4;

    public static final int MSAA_PROB_NO_TITLE_TABLE = 5;

    public static final int MSAA_PROB_NO_TEXT_LINK = 6;

    public static final int MSAA_PROB_NO_TITLE_FRAME = 7;
    
    public static final int MSAA_PROB_INVISIBLE_FLASH = 8;
    
    public static final int MSAA_PROB_NO_ALT_FLASH_IMAGE = 9;

    public static final String[] DESCRIPTIONS = { Messages.getString("msaa.no_alt_for_button"), //$NON-NLS-1$
            Messages.getString("msaa.no_alt_for_graphic"), //$NON-NLS-1$
            Messages.getString("msaa.no_alt_for_clickable_object"), //$NON-NLS-1$
            Messages.getString("msaa.no_alt_for_nonclickable_object"),//$NON-NLS-1$
            Messages.getString("msaa.no_alt_for_form_control"), //$NON-NLS-1$
            Messages.getString("msaa.no_title_for_table"), //$NON-NLS-1$
            Messages.getString("msaa.no_text_for_link"), //$NON-NLS-1$
            Messages.getString("msaa.no_title_for_frame"), //$NON-NLS-1$
            Messages.getString("msaa.invisible_flash"),
            Messages.getString("msaa.no_alt_graphic_flash")
    };
}
