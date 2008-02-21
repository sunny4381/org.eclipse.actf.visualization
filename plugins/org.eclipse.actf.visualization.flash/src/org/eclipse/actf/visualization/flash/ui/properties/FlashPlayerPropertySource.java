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
import java.util.List;

import org.eclipse.actf.model.flash.FlashPlayer;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;


public class FlashPlayerPropertySource implements IPropertySource {

	private OleAutomation automation;
	
	public static final String
	    PID_VERSION = "VERSION", //$NON-NLS-1$
	    PID_AVAILABLE = "AVAILABLE", //$NON-NLS-1$
	    PID_DEBUG = "DEBUG", //$NON-NLS-1$
	    PID_ID = "ID", //$NON-NLS-1$
	    PID_ALLOW_ACCESS = "ALLOW_ACCESS", //$NON-NLS-1$
	    PID_TAGNAME = "TAGNAME", //$NON-NLS-1$
	    PID_CLASSID = "CLASSID", //$NON-NLS-1$
	    PID_CODEBASE = "CODEBASE", //$NON-NLS-1$
	    PID_TYPE = "TYPE", //$NON-NLS-1$
	    PID_MOVIE = "MOVIE", //$NON-NLS-1$
	    PID_READY_STATE = "READY_STATE", //$NON-NLS-1$
	    PID_WMODE = "WMODE"; //$NON-NLS-1$

	public static final String 
		STR_VERSION = "$version",             // 0 //$NON-NLS-1$
		STR_AVAILABLE = "Eclipse_ACTF_is_available",  // 1 //$NON-NLS-1$
		STR_DEBUG = "aDesignerError",       // 2 //$NON-NLS-1$
		STR_ID = "id",                   // 3 //$NON-NLS-1$
		STR_ALLOW_ACCESS = "AllowScriptAccess",    // 4 //$NON-NLS-1$
		STR_TAGNAME = "tagName",              // 5 //$NON-NLS-1$
		STR_CLASSID = "classid",              // 6 //$NON-NLS-1$
		STR_CODEBASE = "codeBase",             // 7 //$NON-NLS-1$
		STR_TYPE = "type",                 // 8 //$NON-NLS-1$
		STR_MOVIE = "movie",                // 9 //$NON-NLS-1$
		STR_READY_STATE = "ReadyState",           // 10 //$NON-NLS-1$
		STR_WMODE = "wmode";                 // 11 //$NON-NLS-1$

	private static final IPropertyDescriptor[] DESCRIPTORS = new IPropertyDescriptor[] 
	{
		new PropertyDescriptor(PID_VERSION,STR_VERSION),
		new PropertyDescriptor(PID_AVAILABLE,STR_AVAILABLE),
		new PropertyDescriptor(PID_DEBUG,STR_DEBUG),
		new PropertyDescriptor(PID_ID,STR_ID),
		new PropertyDescriptor(PID_ALLOW_ACCESS,STR_ALLOW_ACCESS),
		new PropertyDescriptor(PID_TAGNAME,STR_TAGNAME),
		new PropertyDescriptor(PID_CLASSID,STR_CLASSID),
		new PropertyDescriptor(PID_CODEBASE,STR_CODEBASE),
		new PropertyDescriptor(PID_TYPE,STR_TYPE),
		new PropertyDescriptor(PID_MOVIE,STR_MOVIE),
		new PropertyDescriptor(PID_READY_STATE,STR_READY_STATE),
		new PropertyDescriptor(PID_WMODE,STR_WMODE)
	};
	
	public FlashPlayerPropertySource(FlashPlayer flashPlayer) {
        this.automation = flashPlayer.getAutomation();
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
		return result.toArray(new IPropertyDescriptor[result.size()]);
	}

	public Object getPropertyValue(Object id) {
		String strValue = null;
		String variableName = null;
		String propertyName = null;
		if( PID_VERSION.equals(id) ) {
			variableName = "$version"; //$NON-NLS-1$
		}
		else if( PID_AVAILABLE.equals(id) ) {
			variableName = STR_AVAILABLE;
		}
		else if( PID_DEBUG.equals(id) ) {
			propertyName = STR_DEBUG;
		}
		else if( PID_ID.equals(id) ) {
			propertyName = STR_ID;
		}
		else if( PID_ALLOW_ACCESS.equals(id) ) {
			propertyName = STR_ALLOW_ACCESS;
		}
		else if( PID_TAGNAME.equals(id) ) {
			propertyName = STR_TAGNAME;
		}
		else if( PID_CLASSID.equals(id) ) {
			propertyName = STR_CLASSID;
		}
		else if( PID_CODEBASE.equals(id) ) {
			propertyName = STR_CODEBASE;
		}
		else if( PID_TYPE.equals(id) ) {
			propertyName = STR_TYPE;
		}
		else if( PID_MOVIE.equals(id) ) {
			propertyName = STR_MOVIE;
		}
		else if( PID_READY_STATE.equals(id) ) {
			propertyName = STR_READY_STATE;
		}
		else if( PID_WMODE.equals(id) ) {
			propertyName = STR_WMODE;
		}
		if( null != variableName ) {
            int[] idGetVariable = automation.getIDsOfNames(new String[]{"GetVariable"}); //$NON-NLS-1$
            if( null!=idGetVariable ) {
                Variant varImposed = automation.invoke(idGetVariable[0],new Variant[]{new Variant(variableName)}); //$NON-NLS-1$
                strValue = getVariantString(varImposed);
            }
		}
		else if( null != propertyName ) {
            int[] idProperty = automation.getIDsOfNames(new String[]{propertyName});
            if( null != idProperty ) {
                Variant varValue = automation.getProperty(idProperty[0]);
                strValue = getVariantString(varValue);
            }
		}
		return strValue;//null==strValue ? "null" : strValue; //$NON-NLS-1$
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {
	}

	public void setPropertyValue(Object id, Object value) {
	}

    private static String getVariantString(Variant var) {
        if( null != var ) {
            switch( var.getType() ) {
                case OLE.VT_BSTR:
                    return var.getString();
                case OLE.VT_EMPTY:
                    break;
                case OLE.VT_I4:
                    return Integer.toString(var.getInt());
                default:
                    return var.toString();
            }
        }
        return ""; //$NON-NLS-1$
    }
}
