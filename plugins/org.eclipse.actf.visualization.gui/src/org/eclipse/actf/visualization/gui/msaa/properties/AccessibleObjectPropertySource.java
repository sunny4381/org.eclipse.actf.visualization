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
package org.eclipse.actf.visualization.gui.msaa.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.actf.accservice.swtbridge.AccessibleObject;
import org.eclipse.actf.accservice.swtbridge.ia2.Accessible2;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleAction;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleApplication;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleComponent;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleEditableText;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleHyperlink;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleHypertext;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleImage;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleTable;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleText;
import org.eclipse.actf.accservice.swtbridge.ia2.AccessibleValue;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class AccessibleObjectPropertySource extends AbstractPropertyInvokeSource implements IPropertySource {

	public static final int MAX_CHILD_PROPERTIES = 100;
	
	private List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
	
	private Accessible1PropertySource accessible1PropertySource;
	private Accessible2PropertySource accessible2PropertySource;
	private AccessibleActionPropertySource accessibleActionPropertySource;
	private AccessibleApplicationPropertySource accessibleApplicationPropertySource;
	private AccessibleComponentPropertySource accessibleComponentPropertySource;
	private AccessibleEditableTextPropertySource accessibleEditableTextPropertySource;
	private AccessibleHyperlinkPropertySource accessibleHyperlinkPropertySource;
	private AccessibleHypertextPropertySource accessibleHypertextPropertySource;
	private AccessibleImagePropertySource accessibleImagePropertySource;
	private AccessibleTablePropertySource accessibleTablePropertySource;
	private AccessibleTextPropertySource accessibleTextPropertySource;
	private AccessibleValuePropertySource accessibleValuePropertySource;
	
	public static final String 
		PID_IA1 = "IA1", //$NON-NLS-1$
		PID_IA2 = "IA2", //$NON-NLS-1$
		PID_IA2Action = "IA2Action", //$NON-NLS-1$
		PID_IA2Application = "IA2Application", //$NON-NLS-1$
		PID_IA2Component = "IA2Component", //$NON-NLS-1$
		PID_IA2EditableText = "IA2EditableText", //$NON-NLS-1$
		PID_IA2Hyperlink = "IA2Hyperlink", //$NON-NLS-1$
		PID_IA2Hypertext = "IA2Hypertext", //$NON-NLS-1$
		PID_IA2Image = "IA2Image", //$NON-NLS-1$
		PID_IA2Table = "IA2Table", //$NON-NLS-1$
		PID_IA2Text = "IA2Text", //$NON-NLS-1$
		PID_IA2Value = "IA2Value"; //$NON-NLS-1$
	
	private static final String 
		STR_IA1 = "IAccessible", //$NON-NLS-1$
		STR_IA2 = "IAccessible2", //$NON-NLS-1$
		STR_IA2Action = "IAccessibleAction", //$NON-NLS-1$
		STR_IA2Application = "IAccessibleApplication", //$NON-NLS-1$
		STR_IA2Component = "IAccessibleComponent", //$NON-NLS-1$
		STR_IA2EditableText = "IAccessibleEditableText", //$NON-NLS-1$
		STR_IA2Hyperlink = "IAccessibleHyperlink", //$NON-NLS-1$
		STR_IA2Hypertext = "IAccessibleHypertext", //$NON-NLS-1$
		STR_IA2Image = "IAccessibleImage", //$NON-NLS-1$
		STR_IA2Table = "IAccessibleTable", //$NON-NLS-1$
		STR_IA2Text = "IAccessibleText", //$NON-NLS-1$
		STR_IA2Value = "IAccessibleValue"; //$NON-NLS-1$
	
	public AccessibleObjectPropertySource(AccessibleObject accObject) {
		if( null != accObject) {
	        accessible1PropertySource = new Accessible1PropertySource(accObject);
	        descriptors.add(new PropertyDescriptor(PID_IA1,STR_IA1));
			Accessible2 accessible2 = accObject.getAccessible2();
	        if( null != accessible2 ) {
	            accessible2PropertySource = new Accessible2PropertySource(accessible2);
	            descriptors.add(new PropertyDescriptor(PID_IA2,STR_IA2));
	        }
	        AccessibleAction accessibleAction = accObject.getAccessibleAction();
	        if( null != accessibleAction ) {
	        	accessibleActionPropertySource = new AccessibleActionPropertySource(accessibleAction);
	        	descriptors.add(new PropertyDescriptor(PID_IA2Action,STR_IA2Action));
	        }
	        AccessibleApplication accessibleApplication = accObject.getAccessibleApplication();
	        if( null != accessibleApplication ) {
	            accessibleApplicationPropertySource = new AccessibleApplicationPropertySource(accessibleApplication);
	            descriptors.add(new PropertyDescriptor(PID_IA2Application,STR_IA2Application));
	        }
	        AccessibleComponent accessibleComponent = accObject.getAccessibleComponent();
	        if( null != accessibleComponent ) {
	            accessibleComponentPropertySource = new AccessibleComponentPropertySource(accessibleComponent);
	            descriptors.add(new PropertyDescriptor(PID_IA2Component,STR_IA2Component));
	        }
            AccessibleText accessibleText = accObject.getAccessibleText();
            if( null != accessibleText ) {
                accessibleTextPropertySource = new AccessibleTextPropertySource(accessibleText);
                descriptors.add(new PropertyDescriptor(PID_IA2Text,STR_IA2Text));
            }
	        AccessibleEditableText accessibleEditableText = accObject.getAccessibleEditableText();
	        if( null != accessibleEditableText ) {
	            accessibleEditableTextPropertySource = new AccessibleEditableTextPropertySource(accessibleEditableText,accessibleText);
	            descriptors.add(new PropertyDescriptor(PID_IA2EditableText,STR_IA2EditableText));
	        }
	        AccessibleHyperlink accessibleHyperlink = accObject.getAccessibleHyperlink();
	        if( null != accessibleHyperlink ) {
	            accessibleHyperlinkPropertySource = new AccessibleHyperlinkPropertySource(accessibleHyperlink);
	            descriptors.add(new PropertyDescriptor(PID_IA2Hyperlink,STR_IA2Hyperlink));
	        }
	        AccessibleHypertext accessibleHypertext = accObject.getAccessibleHypertext();
	        if( null != accessibleHypertext ) {
	            accessibleHypertextPropertySource = new AccessibleHypertextPropertySource(accessibleHypertext,accessibleText);
	            descriptors.add(new PropertyDescriptor(PID_IA2Hypertext,STR_IA2Hypertext));
	        }
	        AccessibleImage accessibleImage = accObject.getAccessibleImage();
	        if( null != accessibleImage ) {
	            accessibleImagePropertySource = new AccessibleImagePropertySource(accessibleImage);
	            descriptors.add(new PropertyDescriptor(PID_IA2Image,STR_IA2Image));
	        }
	        AccessibleTable accessibleTable = accObject.getAccessibleTable();
	        if( null != accessibleTable ) {
	            accessibleTablePropertySource = new AccessibleTablePropertySource(accessibleTable);
	            descriptors.add(new PropertyDescriptor(PID_IA2Table,STR_IA2Table));
	        }
	        AccessibleValue accessibleValue = accObject.getAccessibleValue();
	        if( null != accessibleValue ) {
	            accessibleValuePropertySource = new AccessibleValuePropertySource(accessibleValue);
	            descriptors.add(new PropertyDescriptor(PID_IA2Value,STR_IA2Value));
	        }
		}
	}

	public Object getEditableValue() {
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return (IPropertyDescriptor[])descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
	}

	public Object getPropertyValue(Object id) {
		if( PID_IA1.equals(id) ) {
			return accessible1PropertySource;
		}
		else if( PID_IA2.equals(id) ) {
			return accessible2PropertySource;
		}
		else if( PID_IA2Action.equals(id) ) {
			return accessibleActionPropertySource;
		}
		else if( PID_IA2Application.equals(id) ) {
			return accessibleApplicationPropertySource;
		}
		else if( PID_IA2Component.equals(id) ) {
			return accessibleComponentPropertySource;
		}
		else if( PID_IA2EditableText.equals(id) ) {
			return accessibleEditableTextPropertySource;
		}
		else if( PID_IA2Hyperlink.equals(id) ) {
			return accessibleHyperlinkPropertySource;
		}
		else if( PID_IA2Hypertext.equals(id) ) {
			return accessibleHypertextPropertySource;
		}
		else if( PID_IA2Image.equals(id) ) {
			return accessibleImagePropertySource;
		}
		else if( PID_IA2Table.equals(id) ) {
			return accessibleTablePropertySource;
		}
		else if( PID_IA2Text.equals(id) ) {
			return accessibleTextPropertySource;
		}
		else if( PID_IA2Value.equals(id) ) {
			return accessibleValuePropertySource;
		}
		return null;
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {
	}

	public void setPropertyValue(Object id, Object value) {
	}

}
