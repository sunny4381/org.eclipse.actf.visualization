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
package org.eclipse.actf.visualization.internal.flash;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.actf.visualization.internal.flash.messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String flash_element_found;
	public static String flash_error_no_element;
	public static String flash_filter_noninformative;
	public static String flash_flash_dom;
	public static String flash_show_visual;
	public static String flash_debugMode;
	public static String flash_scanWindowless;
	public static String flash_error_no_location;
	public static String flash_error_select_flash;
	public static String flash_warning;
	public static String flash_error_target_length;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}