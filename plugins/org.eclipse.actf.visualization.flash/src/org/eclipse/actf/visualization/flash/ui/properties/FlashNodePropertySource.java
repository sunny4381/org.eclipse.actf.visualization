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
package org.eclipse.actf.visualization.flash.ui.properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.actf.accservice.swtbridge.MSAA;
import org.eclipse.actf.model.flash.FlashNode;
import org.eclipse.actf.model.flash.util.ASObject;
import org.eclipse.actf.visualization.gui.flash.FlashFinder;
import org.eclipse.actf.visualization.gui.msaa.properties.AttributePropertySource;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;


public class FlashNodePropertySource implements IPropertySource {

	private FlashNode flashNode;
	private boolean isAccImpl;
	
	public static final String
	    PID_OBJECTNAME = "OBJECTNAME", //$NON-NLS-1$
	    PID_TYPE = "TYPE", //$NON-NLS-1$
	    PID_VALUE = "VALUE", //$NON-NLS-1$
	    PID_TARGET = "TARGET", //$NON-NLS-1$
	    PID_DEPTH = "depth", //$NON-NLS-1$
	    PID_CURRENTFRAME = "currentFrame", //$NON-NLS-1$
	    PID_CLASSNAME = "CLASSNAME", //$NON-NLS-1$
	    PID_TEXT = "TEXT", //$NON-NLS-1$
	    PID_TITLE = "TITLE", //$NON-NLS-1$
        PID_UICOMPONENT = "isUIComponent", //$NON-NLS-1$
	    PID_OPAQUE_OBJECT = "isOpaqueObject", //$NON-NLS-1$
	    PID_INPUTABLE = "isInputable", //$NON-NLS-1$
	    PID_LOCATION = "LOCATION", //$NON-NLS-1$
	    PID_ACC_ROLE = "ACC_ROLE", //$NON-NLS-1$
	    PID_ACC_NAME = "ACC_NAME", //$NON-NLS-1$
	    PID_ACC_STATE = "ACC_STATE", //$NON-NLS-1$
	    PID_ACC_ACTION = "ACC_ACTION", //$NON-NLS-1$
	    PID_ACC_INFO = "ACC_INFO", //$NON-NLS-1$
        PID_TABINDEX = "tabIndex", //$NON-NLS-1$
	    PID_WMODE = "WMODE";  //$NON-NLS-1$
	
	private static final IPropertyDescriptor[] DESCRIPTORS = new IPropertyDescriptor[] 
 	{
		new PropertyDescriptor(PID_OBJECTNAME,"objectName"), 		// 0 //$NON-NLS-1$
		new PropertyDescriptor(PID_TYPE,"type"), 			// 1 //$NON-NLS-1$
		new PropertyDescriptor(PID_VALUE,"value"), 			// 2 //$NON-NLS-1$
		new PropertyDescriptor(PID_TARGET,"target"), 			// 3 //$NON-NLS-1$
		new PropertyDescriptor(PID_DEPTH,PID_DEPTH),
		new PropertyDescriptor(PID_CURRENTFRAME,PID_CURRENTFRAME),
		new PropertyDescriptor(PID_CLASSNAME,"className"),		// 6 //$NON-NLS-1$
		new PropertyDescriptor(PID_TEXT,"text"),				// 7 //$NON-NLS-1$
		new PropertyDescriptor(PID_TITLE,"title"),			// 8 //$NON-NLS-1$
		new PropertyDescriptor(PID_LOCATION,"Location"),         // 10 //$NON-NLS-1$
        new PropertyDescriptor(PID_TABINDEX,PID_TABINDEX),
		new PropertyDescriptor(PID_ACC_INFO,"accInfo"),			// 11 //$NON-NLS-1$
		new PropertyDescriptor(PID_ACC_ROLE,"accRole"),			// 11 //$NON-NLS-1$
		new PropertyDescriptor(PID_ACC_NAME,"accName"),			// 12 //$NON-NLS-1$
		new PropertyDescriptor(PID_ACC_STATE,"accState"),			// 13 //$NON-NLS-1$
		new PropertyDescriptor(PID_ACC_ACTION,"accDefaultAction"),	// 14 //$NON-NLS-1$
        new PropertyDescriptor(PID_UICOMPONENT,PID_UICOMPONENT),
		new PropertyDescriptor(PID_OPAQUE_OBJECT,PID_OPAQUE_OBJECT),
		new PropertyDescriptor(PID_INPUTABLE,PID_INPUTABLE),
		new PropertyDescriptor(PID_WMODE,"wmode")	// 15 //$NON-NLS-1$
 	};
	
	public FlashNodePropertySource(FlashNode flashNode) {
		this.flashNode = flashNode;
		this.isAccImpl = "_accImpl".equals(flashNode.getObjectName()); //$NON-NLS-1$
	}

	public Object getEditableValue() {
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> result = new ArrayList<IPropertyDescriptor>();
		for( int i=0; i<DESCRIPTORS.length; i++ ) {
			if( null != getPropertyValue(DESCRIPTORS[i].getId()) ) {
				result.add(DESCRIPTORS[i]);
			}
		}
		if( FlashFinder.debugMode ) {
			Set keySet = flashNode.getKeys();
			if( null != keySet ) {
				for( Iterator it = keySet.iterator(); it.hasNext(); ) {
					Object key = it.next();
					result.add(new PropertyDescriptor(key,"["+key.toString()+"]")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		return result.toArray(new IPropertyDescriptor[result.size()]);
	}

	public Object getPropertyValue(Object id) {
		String strValue = null;
		if( PID_OBJECTNAME.equals(id) ) {
			strValue = flashNode.getObjectName();
		}
		else if( PID_TYPE.equals(id) ) {
			strValue = flashNode.getType();
		}
		else if( PID_VALUE.equals(id) ) {
			strValue = flashNode.getValue();
		}
		else if( PID_TARGET.equals(id) ) {
			strValue = flashNode.getTarget();
//			strValue += ", mName="+flashNode.getString("mName") + ", mTarget="+flashNode.getString("mTarget") + ", parentTarget="+flashNode.getString("parentTarget") + ", targetIsString="+flashNode.getString("targetIsString") + ", targetModified="+flashNode.getString("targetModified");
		}
		else if( PID_CLASSNAME.equals(id) ) {
			strValue = flashNode.getClassName();
		}
		else if( PID_TEXT.equals(id) ) {
			strValue = flashNode.getText(false);
		}
		else if( PID_TITLE.equals(id) ) {
			strValue = flashNode.getTitle();
		}
//		else if( PID_DEPTH.equals(id) ) {
//			strValue = flashNode.getString("depth"); //$NON-NLS-1$
//		}
//		else if( PID_CURRENTFRAME.equals(id) ) {
//			strValue = flashNode.getString("currentFrame"); //$NON-NLS-1$
//		}
//		else if( PID_UICOMPONENT.equals(id) ) {
//			strValue = flashNode.getString("isUIComponent"); //$NON-NLS-1$
//		}
		else if( PID_LOCATION.equals(id) ) {
            try {
                int x = Float.valueOf(flashNode.getString("x")).intValue(); //$NON-NLS-1$
                int y = Float.valueOf(flashNode.getString("y")).intValue(); //$NON-NLS-1$
                int w = Float.valueOf(flashNode.getString("w")).intValue(); //$NON-NLS-1$
                int h = Float.valueOf(flashNode.getString("h")).intValue(); //$NON-NLS-1$
                strValue = x + ", " + y + ", " + (x + w) + ", " + (y + h) + " (width="+w+", height="+h+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
            }
            catch(Exception e) {
            }
		}
		else if( PID_WMODE.equals(id) ) {
            if( null == flashNode.getParent() ) {
            	strValue = flashNode.getPlayer().getWMode();
            }
		}
		else if( PID_ACC_INFO.equals(id) ) {
			Object accInfo = flashNode.getObject("accInfo"); //$NON-NLS-1$
			if( accInfo instanceof ASObject ) {
				ASObject accInfoObject = ((ASObject)accInfo); 
				Set accInfoSet = accInfoObject.getKeys();
				if( null != accInfoSet ) {
					AttributePropertySource attrSource = new AttributePropertySource(null,accInfoObject.toString());
					for( Iterator it = accInfoSet.iterator(); it.hasNext(); ) {
						Object key = it.next();
						String keyName = key.toString();
						Object keyValue = accInfoObject.get(keyName);
						if( keyValue instanceof Integer ) {
							int intValue = ((Integer)keyValue).intValue();
							if( "role".equals(keyName) ) { //$NON-NLS-1$
								keyValue = MSAA.getRoleText(intValue);
							}
							else if( "state".equals(keyName) ) { //$NON-NLS-1$
								keyValue = MSAA.getStateText(intValue);
							}
						}
						attrSource.put(keyName, keyValue);
					}
					return attrSource;
				}
			}
		}
		else if( isAccImpl ) {
			String methodName = null;
			if( PID_ACC_ROLE.equals(id) ) {
				methodName = "get_accRole"; //$NON-NLS-1$
			}
			else if( PID_ACC_NAME.equals(id) ) {
				methodName = "get_accName"; //$NON-NLS-1$
			}
			else if( PID_ACC_STATE.equals(id) ) {
				methodName = "get_accState"; //$NON-NLS-1$
			}
			else if( PID_ACC_ACTION.equals(id) ) {
				methodName = "get_accDefaultAction"; //$NON-NLS-1$
			}
			if( null != methodName ) {
				Variant value = flashNode.getPlayer().callMethod(flashNode.getTarget(),methodName,new Variant(0)); //$NON-NLS-1$
				if( null != value ) {
					switch( value.getType() ) {
					case OLE.VT_BSTR:
						strValue = value.getString();
						break;
					case OLE.VT_I4:
						int intValue = value.getInt();
						if( PID_ACC_ROLE.equals(id) ) {
							strValue = MSAA.getRoleText(intValue);
						}
						else if( PID_ACC_STATE.equals(id) ) {
							strValue = MSAA.getStateText(intValue);
						}
						break;
					}
				}
			}
		}
		if( null==strValue ) {
			strValue = flashNode.getString(id.toString());
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

}
