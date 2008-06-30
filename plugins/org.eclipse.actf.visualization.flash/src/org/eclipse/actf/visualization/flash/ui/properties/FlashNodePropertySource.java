/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *    Kentarou FUKUDA - initial API and implementation
 *    Daisuke SATO - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.flash.ui.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.actf.accservice.swtbridge.MSAA;
import org.eclipse.actf.model.flash.ASAccInfo;
import org.eclipse.actf.model.flash.IASNode;
import org.eclipse.actf.model.flash.IFlashConst;
import org.eclipse.actf.visualization.gui.msaa.properties.AttributePropertySource;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class FlashNodePropertySource implements IPropertySource, IFlashConst {

	private static boolean DEBUG_MODE = false;

	private IASNode flashNode;

	public static final String PID_LOCATION = "Location", //$NON-NLS-1$
			PID_TABINDEX = "tabIndex"; //$NON-NLS-1$

	private static final IPropertyDescriptor[] DESCRIPTORS = new IPropertyDescriptor[] {
			new PropertyDescriptor(ASNODE_OBJECT_NAME, ASNODE_OBJECT_NAME), // 0
			new PropertyDescriptor(ASNODE_TYPE, ASNODE_TYPE), // 1
			new PropertyDescriptor(ASNODE_VALUE, ASNODE_VALUE), // 2
			new PropertyDescriptor(ASNODE_TARGET, ASNODE_TARGET), // 3
			new PropertyDescriptor(ASNODE_DEPTH, ASNODE_DEPTH),
			new PropertyDescriptor(ASNODE_CURRENT_FRAME, ASNODE_CURRENT_FRAME),
			new PropertyDescriptor(ASNODE_CLASS_NAME, ASNODE_CLASS_NAME), // 6
			new PropertyDescriptor(ASNODE_TEXT, ASNODE_TEXT), // 7
			new PropertyDescriptor(ASNODE_TITLE, ASNODE_TITLE), // 8
			new PropertyDescriptor(PID_LOCATION, PID_LOCATION), // 10
			new PropertyDescriptor(PID_TABINDEX, PID_TABINDEX),
			new PropertyDescriptor(ASNODE_ACCINFO, ASNODE_ACCINFO), // 11
			new PropertyDescriptor(ASNODE_IS_UI_COMPONENT,
					ASNODE_IS_UI_COMPONENT),
			new PropertyDescriptor(ASNODE_IS_OPAQUE_OBJECT,
					ASNODE_IS_OPAQUE_OBJECT),
			new PropertyDescriptor(ASNODE_IS_INPUTABLE, ASNODE_IS_INPUTABLE),
			new PropertyDescriptor(ATTR_WMODE, ATTR_WMODE) // 15 //$NON-NLS-1$
	};

	public FlashNodePropertySource(IASNode flashNode) {
		this.flashNode = flashNode;
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
		if (DEBUG_MODE) {
			Set<String> keySet = flashNode.getKeys();
			if (null != keySet) {
				for (String key : keySet) {
					result.add(new PropertyDescriptor(key, "[" + key + "]")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		return result.toArray(new IPropertyDescriptor[result.size()]);
	}

	public Object getPropertyValue(Object id) {
		String strValue = null;
		if (ASNODE_OBJECT_NAME.equals(id)) {
			strValue = flashNode.getObjectName();
		} else if (ASNODE_TYPE.equals(id)) {
			strValue = flashNode.getType();
		} else if (ASNODE_VALUE.equals(id)) {
			strValue = flashNode.getValue();
		} else if (ASNODE_TARGET.equals(id)) {
			strValue = flashNode.getTarget();
			// strValue += ", mName="+flashNode.getString("mName") + ",
			// mTarget="+flashNode.getString("mTarget") + ",
			// parentTarget="+flashNode.getString("parentTarget") + ",
			// targetIsString="+flashNode.getString("targetIsString") + ",
			// targetModified="+flashNode.getString("targetModified");
		} else if (ASNODE_CLASS_NAME.equals(id)) {
			strValue = flashNode.getClassName();
		} else if (ASNODE_TEXT.equals(id)) {
			strValue = flashNode.getText(false);
		} else if (ASNODE_TITLE.equals(id)) {
			strValue = flashNode.getTitle();
		} else if (PID_LOCATION.equals(id)) {
			try {
				double x = flashNode.getX(); //$NON-NLS-1$
				double y = flashNode.getY(); //$NON-NLS-1$
				double w = flashNode.getWidth(); //$NON-NLS-1$
				double h = flashNode.getHeight(); //$NON-NLS-1$
				if (Double.NaN != x) {
					strValue = x
							+ ", " + y + ", " + (x + w) + ", " + (y + h) + " (width=" + w + ", height=" + h + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				}
			} catch (Exception e) {
			}
		} else if (ATTR_WMODE.equals(id)) {
			if (null == flashNode.getParent()) {
				strValue = flashNode.getPlayer().getWMode();
			}
		} else if (ASNODE_ACCINFO.equals(id)) {
			ASAccInfo accInfo = flashNode.getAccInfo(); //$NON-NLS-1$
			if (null != accInfo) {
				AttributePropertySource attrSource = new AttributePropertySource(
						null, accInfo.toString());
				Set<String> accInfoSet = accInfo.getKeys();
				for (String keyName : accInfoSet) {
					Object keyValue = accInfo.get(keyName);
					if (keyValue instanceof Integer) {
						int intValue = ((Integer) keyValue).intValue();
						if (IFlashConst.ACCINFO_ROLE.equals(keyName)) { //$NON-NLS-1$
							keyValue = MSAA.getRoleText(intValue);
						} else if (IFlashConst.ACCINFO_STATE.equals(keyName)) { //$NON-NLS-1$
							keyValue = MSAA.getStateText(intValue);
						}
					}
					attrSource.put(keyName, keyValue);
				}
				return attrSource;
			}
		} else if (null == strValue) {
			Object obj = flashNode.getObject(id.toString());
			if (null != obj) {
				strValue = obj.toString();
			}
		}
		return strValue;
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {
	}

	public void setPropertyValue(Object id, Object value) {
	}

	public static boolean isDebugMode() {
		return DEBUG_MODE;
	}

	public static void setDebugMode(boolean debug_mode) {
		DEBUG_MODE = debug_mode;
	}

}
