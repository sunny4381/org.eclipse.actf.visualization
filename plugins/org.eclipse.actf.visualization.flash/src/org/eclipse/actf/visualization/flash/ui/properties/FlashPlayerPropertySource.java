/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *    Daisuke SATO- initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation 
 *******************************************************************************/
package org.eclipse.actf.visualization.flash.ui.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.actf.model.flash.IFlashConst;
import org.eclipse.actf.model.flash.IFlashPlayer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class FlashPlayerPropertySource implements IPropertySource, IFlashConst {

	public static final String PID_STATUS = "STATUS", //$NON-NLS-1$
			PID_ID = "ID", //$NON-NLS-1$
			PID_ALLOW_ACCESS = "ALLOW_ACCESS", //$NON-NLS-1$
			PID_TAGNAME = "TAGNAME", //$NON-NLS-1$
			PID_CLASSID = "CLASSID", //$NON-NLS-1$
			PID_CODEBASE = "CODEBASE", //$NON-NLS-1$
			PID_TYPE = "TYPE", //$NON-NLS-1$
			PID_MOVIE = "MOVIE"; //$NON-NLS-1$

	public static final String STR_STATUS = "status", // 2 //$NON-NLS-1$
			STR_ID = "id", // 3 //$NON-NLS-1$
			STR_ALLOW_ACCESS = "AllowScriptAccess", // 4 //$NON-NLS-1$
			STR_TAGNAME = "tagName", // 5 //$NON-NLS-1$
			STR_CLASSID = "classid", // 6 //$NON-NLS-1$
			STR_CODEBASE = "codeBase", // 7 //$NON-NLS-1$
			STR_TYPE = "type", // 8 //$NON-NLS-1$
			STR_MOVIE = "movie"; // 11 //$NON-NLS-1$

	private static final IPropertyDescriptor[] DESCRIPTORS = new IPropertyDescriptor[] {
			new PropertyDescriptor(PLAYER_VERSION, "Flash player version"), //$NON-NLS-1$
			new PropertyDescriptor(PID_STATUS, STR_STATUS),
			new PropertyDescriptor(PID_ID, STR_ID),
			new PropertyDescriptor(PID_ALLOW_ACCESS, STR_ALLOW_ACCESS),
			new PropertyDescriptor(PID_TAGNAME, STR_TAGNAME),
			new PropertyDescriptor(PID_CLASSID, STR_CLASSID),
			new PropertyDescriptor(PID_CODEBASE, STR_CODEBASE),
			new PropertyDescriptor(PID_TYPE, STR_TYPE),
			new PropertyDescriptor(PID_MOVIE, STR_MOVIE),
			new PropertyDescriptor(READY_STATE, READY_STATE),
			new PropertyDescriptor(ATTR_WMODE, ATTR_WMODE) };

	private IFlashPlayer flashPlayer;

	public FlashPlayerPropertySource(IFlashPlayer flashPlayer) {
		this.flashPlayer = flashPlayer;
	}

	public Object getEditableValue() {
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> result = new ArrayList<IPropertyDescriptor>();
		for (int i = 0; i < DESCRIPTORS.length; i++) {
			if (null != getPropertyValue(DESCRIPTORS[i].getId())) {
				result.add(DESCRIPTORS[i]);
			}
		}
		return result.toArray(new IPropertyDescriptor[result.size()]);
	}

	public Object getPropertyValue(Object id) {
		String strValue = null;
		String propertyName = null;
		if (PLAYER_VERSION.equals(id)) {
			return flashPlayer.getPlayerVersion();
		} else if (PID_STATUS.equals(id)) {
			return flashPlayer.getStatus();
		} else if (PID_ID.equals(id)) {
			propertyName = STR_ID;
		} else if (PID_ALLOW_ACCESS.equals(id)) {
			propertyName = STR_ALLOW_ACCESS;
		} else if (PID_TAGNAME.equals(id)) {
			propertyName = STR_TAGNAME;
		} else if (PID_CLASSID.equals(id)) {
			propertyName = STR_CLASSID;
		} else if (PID_CODEBASE.equals(id)) {
			propertyName = STR_CODEBASE;
		} else if (PID_TYPE.equals(id)) {
			propertyName = STR_TYPE;
		} else if (PID_MOVIE.equals(id)) {
			propertyName = STR_MOVIE;
		} else if (READY_STATE.equals(id)) {
			propertyName = READY_STATE;
		} else if (ATTR_WMODE.equals(id)) {
			return flashPlayer.getWMode();
		}
		
		if (null != propertyName) {
			strValue = flashPlayer.getPlayerProperty(propertyName);
		}
		return strValue;// null==strValue ? "null" : strValue;
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {
	}

	public void setPropertyValue(Object id, Object value) {
	}
}
