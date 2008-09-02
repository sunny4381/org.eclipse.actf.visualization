/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.accservice.swtbridge;

import java.util.ArrayList;
import java.util.List;



public class IA2 {

    /**
     * Unknown role. The object contains some Accessible information, but its
     * role is not known.
     */
    public static final int IA2_ROLE_UNKNOWN = 0;

    /** An object that can be drawn into and to manage events from the objects
    drawn into it.  Also refer to ::IA2_ROLE_FRAME,
    ::IA2_ROLE_GLASS_PANE, and ::IA2_ROLE_LAYERED_PANE. 
   */
    public static final int IA2_ROLE_CANVAS = 0x401;
    
    // / A caption describing another object.
    public static final int IA2_ROLE_CAPTION = 0x402;

    // / Used for check buttons that are menu items.
    public static final int IA2_ROLE_CHECK_MENU_ITEM = 0x403;

    // / A specialized dialog that lets the user choose a color.
    public static final int IA2_ROLE_COLOR_CHOOSER = 0x404;

    // / A date editor.
    public static final int IA2_ROLE_DATE_EDITOR = 0x405;

    /**
     * An iconified internal frame in an ::IA2_ROLE_DESKTOP_PANE. Also refer to
     * ::IA2_ROLE_INTERNAL_FRAME.
     */
    public static final int IA2_ROLE_DESKTOP_ICON = 0x406;

    /** 
     * A desktop pane. A pane that supports internal frames and iconified 
     * versions of those internal frames.  Also refer to ::IA2_ROLE_INTERNAL_FRAME.
     */
    public static final int IA2_ROLE_DESKTOP_PANE = 0x407;

    /**
     * A directory pane. A pane that allows the user to navigate through and
     * select the contents of a directory. May be used by a file chooser. Also
     * refer to ::IA2_ROLE_FILE_CHOOSER.
     */
    public static final int IA2_ROLE_DIRECTORY_PANE = 0x408;

    // / An editable text object in a toolbar.
    public static final int IA2_ROLE_EDITBAR = 0x409;

    // / Embeded (OLE) object.
    public static final int IA2_ROLE_EMBEDDED_OBJECT = 0x40A;

    // / Text that is used as an endnote (footnote at the end of a chapter or
    // section).
    public static final int IA2_ROLE_ENDNOTE = 0x40B;

    /**
     * A file chooser. A specialized dialog that displays the files in the
     * directory and lets the user select a file, browse a different directory,
     * or specify a filename. May use the directory pane to show the contents of
     * a directory. Also refer to ::IA2_ROLE_DIRECTORY_PANE.
     */
    public static final int IA2_ROLE_FILE_CHOOSER = 0x40C;

    /**
     * A font chooser. A font chooser is a component that lets the user pick
     * various attributes for fonts.
     */
    public static final int IA2_ROLE_FONT_CHOOSER = 0x40D;

    /**
     * Footer of a document page. Also refer to ::IA2_ROLE_HEADER.
     */
    public static final int IA2_ROLE_FOOTER = 0x40E;

    // / Text that is used as a footnote. Also refer to ::IA2_ROLE_ENDNOTE.
    public static final int IA2_ROLE_FOOTNOTE = 0x40F;

    /** A container of form controls.  An example of the use of this role is to
     represent an HTML FORM tag.
    */
    public static final int IA2_ROLE_FORM =0x410;

    /**
     * Frame role. A top level window with a title bar, border, menu bar, etc.
     * It is often used as the primary window for an application. Also refer to
     * ::IA2_ROLE_CANVAS and the MSAA roles of dialog and window.
     */
    public static final int IA2_ROLE_FRAME = 0x411;

    /** 
     * A glass pane. A pane that is guaranteed to be painted on top of all panes
     * beneath it.  Also refer to ::IA2_ROLE_CANVAS, ::IA2_ROLE_INTERNAL_FRAME, and
     * ::IA2_ROLE_ROOT_PANE.
     */
    public static final int IA2_ROLE_GLASS_PANE = 0x412;

    /**
     * Header of a document page. Also refer to ::IA2_ROLE_FOOTER.
     */
    public static final int IA2_ROLE_HEADER = 0x413;

    /// Heading.  Use the IAccessible2::attributes heading-level attribute to determine the heading level.
    public static final int IA2_ROLE_HEADING = 0x414;
    
    // / A small fixed size picture, typically used to decorate components.
    public static final int IA2_ROLE_ICON = 0x415;

    /**
     * An image map object. Usually a graphic with multiple hotspots, where each
     * hotspot can be activated resulting in the loading of another document or
     * section of a document.
     */
    public static final int IA2_ROLE_IMAGE_MAP = 0x416;

    /** An object which is used to allow input of characters not found on a keyboard,
    such as the input of Chinese characters on a Western keyboard.
    */
    public static final int IA2_ROLE_INPUT_METHOD_WINDOW = 0x417;

    /**
     * An internal frame. A frame-like object that is clipped by a desktop pane.
     * The desktop pane, internal frame, and desktop icon objects are often used
     * to create multiple document interfaces within an application. Also refer
     * to ::IA2_ROLE_DESKTOP_ICON, ::IA2_ROLE_DESKTOP_PANE, and ::IA2_ROLE_FRAME
     */
    public static final int IA2_ROLE_INTERNAL_FRAME = 0x418;

    // / An object used to present an icon or short string in an interface.
    public static final int IA2_ROLE_LABEL = 0x419;

    /** 
     * A layered pane. A specialized pane that allows its children to be drawn 
     * in layers, providing a form of stacking order. This is usually the pane that 
     * holds the menu bar as  well as the pane that contains most of the visual 
     * components in a window.
     * Also refer to ::IA2_ROLE_CANVAS, ::IA2_ROLE_GLASS_PANE, and ::IA2_ROLE_ROOT_PANE.
     */
    public static final int IA2_ROLE_LAYERED_PANE = 0x41A;

    // / An embedded note which is not visible until activated.
    public static final int IA2_ROLE_NOTE = 0x41B;

    /**
     * A specialized pane whose primary use is inside a dialog. Also refer to
     * MSAA's dialog role.
     */
    public static final int IA2_ROLE_OPTION_PANE = 0x41C;

    /** An object representing a page of document content.  It is used in documents
    which are accessed by the user on a page by page basis.
    */
    public static final int IA2_ROLE_PAGE = 0x41D;

   // / A paragraph of text.
    public static final int IA2_ROLE_PARAGRAPH = 0x41E;

    /**
     * A radio button that is a menu item. Also refer to MSAA's button and menu
     * item roles.
     */
    public static final int IA2_ROLE_RADIO_MENU_ITEM = 0x41F;

    /** An object which is redundant with another object in the accessible hierarchy.
    ATs typically ignore objects with this role.
   */
    public static final int IA2_ROLE_REDUNDANT_OBJECT = 0x420;

    /**
     * A root pane. A specialized pane that has a glass pane and a layered pane
     * as its children. Also refer to ::IA2_ROLE_GLASS_PANE and
     * ::IA2_ROLE_LAYERED_PANE
     */
    public static final int IA2_ROLE_ROOT_PANE = 0x421;

    /**
     * A ruler such as those used in word processors.
     */
    public static final int IA2_ROLE_RULER = 0x422;

    /**
     * A scroll pane. An object that allows a user to incrementally view a large
     * amount of information. Its children can include scroll bars and a
     * viewport. Also refer to ::IA2_ROLE_VIEW_PORT and MSAA's scroll bar role.
     */
    public static final int IA2_ROLE_SCROLL_PANE = 0x423;

    /** A container of document content.  An example of the use of this role is to
    represent an HTML DIV tag.

    A section may be used as a region.  A region is a group of elements that 
    together form a perceivable unit.  A region does not necessarily follow the 
    logical structure of the content, but follows the perceivable structure of 
    the page.  A region may have an attribute in the set of 
    IAccessible2::attributes which indicates that it is "live".  A live region 
    is content that is likely to change in response to a timed change, a user 
    event, or some other programmed logic or event.
   */
    public static final int IA2_ROLE_SECTION = 0x424;

    // / Object with graphical representation used to represent content on draw
    // pages.
    public static final int IA2_ROLE_SHAPE = 0x425;

    /**
     * A split pane. A specialized panel that presents two other panels at the
     * same time. Between the two panels is a divider the user can manipulate to
     * make one panel larger and the other panel smaller.
     */
    public static final int IA2_ROLE_SPLIT_PANE = 0x426;

    /** An object that forms part of a menu system but which can be "undocked" 
    from or "torn off" the menu system to exist as a separate window.
   */
    public static final int IA2_ROLE_TEAR_OFF_MENU = 0x427;

   /// An object used as a terminal emulator.
    public static final int IA2_ROLE_TERMINAL = 0x428;

    // / Collection of objects that constitute a logical text entity.
    public static final int IA2_ROLE_TEXT_FRAME = 0x429;

    /**
     * A toggle button. A specialized push button that can be checked or
     * unchecked, but does not provide a separate indicator for the current
     * state. Also refer to MSAA's roles of push button, check box, and radio
     * button.
     */
    public static final int IA2_ROLE_TOGGLE_BUTTON = 0x42A;

    /**
     * A viewport. An object usually used in a scroll pane. It represents the
     * portion of the entire data that the user can see. As the user manipulates
     * the scroll bars, the contents of the viewport can change. Also refer to
     * ::IA2_ROLE_SCROLL_PANE.
     */
    public static final int IA2_ROLE_VIEW_PORT = 0x42B;
    

    public static final String[] IA2_ROLETEXTS = new String[] {
    		"IA2_ROLE_CANVAS", // 0x401 //$NON-NLS-1$
    		"IA2_ROLE_CAPTION", // 0x402 //$NON-NLS-1$
            "IA2_ROLE_CHECK_MENU_ITEM", // 0x403 //$NON-NLS-1$
            "IA2_ROLE_COLOR_CHOOSER", // 0x404 //$NON-NLS-1$
            "IA2_ROLE_DATE_EDITOR", // 0x405 //$NON-NLS-1$
            "IA2_ROLE_DESKTOP_ICON", // 0x406 //$NON-NLS-1$
            "IA2_ROLE_DESKTOP_PANE", // 0x407 //$NON-NLS-1$
            "IA2_ROLE_DIRECTORY_PANE", // 0x408 //$NON-NLS-1$
            "IA2_ROLE_EDITBAR", // 0x409 //$NON-NLS-1$
            "IA2_ROLE_EMBEDDED_OBJECT", // 0x40A //$NON-NLS-1$
            "IA2_ROLE_ENDNOTE", // 0x40B //$NON-NLS-1$
            "IA2_ROLE_FILE_CHOOSER", // 0x40C //$NON-NLS-1$
            "IA2_ROLE_FONT_CHOOSER", // 0x40D //$NON-NLS-1$
            "IA2_ROLE_FOOTER", // 0x40E //$NON-NLS-1$
            "IA2_ROLE_FOOTNOTE", // 0x40F //$NON-NLS-1$
            "IA2_ROLE_FORM", // 0x410 //$NON-NLS-1$
            "IA2_ROLE_FRAME", // 0x411 //$NON-NLS-1$
            "IA2_ROLE_GLASS_PANE", // 0x412 //$NON-NLS-1$
            "IA2_ROLE_HEADER", // 0x413 //$NON-NLS-1$
            "IA2_ROLE_HEADING", // 0x414 //$NON-NLS-1$
            "IA2_ROLE_ICON", // 0x415 //$NON-NLS-1$
            "IA2_ROLE_IMAGE_MAP", // 0x416 //$NON-NLS-1$
            "IA2_ROLE_INPUT_METHOD_WINDOW", // 0x417 //$NON-NLS-1$
            "IA2_ROLE_INTERNAL_FRAME", // 0x418 //$NON-NLS-1$
            "IA2_ROLE_LABEL", // 0x419 //$NON-NLS-1$
            "IA2_ROLE_LAYERED_PANE", // 0x41A //$NON-NLS-1$
            "IA2_ROLE_NOTE", // 0x41B //$NON-NLS-1$
            "IA2_ROLE_OPTION_PANE", // 0x41C //$NON-NLS-1$
            "IA2_ROLE_PAGE", // 0x41D //$NON-NLS-1$
            "IA2_ROLE_PARAGRAPH", // 0x41E //$NON-NLS-1$
            "IA2_ROLE_RADIO_MENU_ITEM", // 0x41F //$NON-NLS-1$
            "IA2_ROLE_REDUNDANT_OBJECT", // 0x420 //$NON-NLS-1$
            "IA2_ROLE_ROOT_PANE", // 0x421 //$NON-NLS-1$
            "IA2_ROLE_RULER", // 0x422 //$NON-NLS-1$
            "IA2_ROLE_SCROLL_PANE", // 0x423 //$NON-NLS-1$
            "IA2_ROLE_SECTION", // 0x424 //$NON-NLS-1$
            "IA2_ROLE_SHAPE", // 0x425 //$NON-NLS-1$
            "IA2_ROLE_SPLIT_PANE", // 0x426 //$NON-NLS-1$
            "IA2_ROLE_TEAR_OFF_MENU", // 0x427 //$NON-NLS-1$
            "IA2_ROLE_TERMINAL", // 0x428 //$NON-NLS-1$
            "IA2_ROLE_TEXT_FRAME", // 0x429 //$NON-NLS-1$
            "IA2_ROLE_TOGGLE_BUTTON", // 0x42A //$NON-NLS-1$
            "IA2_ROLE_VIEW_PORT" // 0x42B //$NON-NLS-1$
    };

    public static String getRoleText(int role) {
        if (IA2_ROLE_CANVAS <= role && role <= IA2_ROLE_VIEW_PORT) {
            return IA2_ROLETEXTS[role - IA2_ROLE_CANVAS];
        }
        return ""; //$NON-NLS-1$
    }

    public static final String[] IA2_STATETEXTS = new String[] {
    	   "IA2_STATE_ACTIVE", // 0x1; //$NON-NLS-1$
    	   "IA2_STATE_ARMED", // 0x2; //$NON-NLS-1$
    	   "IA2_STATE_DEFUNCT", // 0x4; //$NON-NLS-1$
    	   "IA2_STATE_EDITABLE", // 0x8; //$NON-NLS-1$
    	   "IA2_STATE_HORIZONTAL", // 0x10; //$NON-NLS-1$
    	   "IA2_STATE_ICONIFIED", // 0x20; //$NON-NLS-1$
    	   "IA2_STATE_INVALID_ENTRY", // 0x40; //$NON-NLS-1$
    	   "IA2_STATE_MANAGES_DESCENDANTS", // 0x80; //$NON-NLS-1$
    	   "IA2_STATE_MODAL", // 0x100; //$NON-NLS-1$
    	   "IA2_STATE_MULTI_LINE", // 0x200; //$NON-NLS-1$
    	   "IA2_STATE_OPAQUE", // 0x400; //$NON-NLS-1$
    	   "IA2_STATE_REQUIRED", // 0x800; //$NON-NLS-1$
    	   "IA2_STATE_SELECTABLE_TEXT", // 0x1000; //$NON-NLS-1$
    	   "IA2_STATE_SINGLE_LINE", // 0x2000; //$NON-NLS-1$
    	   "IA2_STATE_STALE", // 0x4000; //$NON-NLS-1$
    	   "IA2_STATE_SUPPORTS_AUTOCOMPLETION", // 0x8000; //$NON-NLS-1$
    	   "IA2_STATE_TRANSIENT", // 0x10000; //$NON-NLS-1$
    	   "IA2_STATE_VERTICAL" // 0x20000; //$NON-NLS-1$

    };
    public static String[] getStateTextAsArray(int state) {
    	List result = new ArrayList();
    	for( int i=0; i<IA2_STATETEXTS.length; i++ ) {
    		int bits = 1<<i;
    		if( 0 != (state & bits) ) {
    			result.add(IA2_STATETEXTS[i]+" (0x"+Integer.toHexString(bits)+")"); //$NON-NLS-1$ //$NON-NLS-2$
    		}
    	}
    	return (String[])result.toArray(new String[result.size()]);
    }
    /**
     * The change of the number or attributes of actions of an accessible object
     * is signaled by events of this type.
     */
    public static final int IA2_EVENT_ACTION_CHANGED = 0x101;

    /**
     * The active descendant of a component has changed. The active descendant
     * is used in objects with transient children.
     */
    public static final int IA2_EVENT_ACTIVE_DECENDENT_CHANGED = 0x102;

    /**
     * The document wide attributes of the document object have changed.
     */
    public static final int IA2_EVENT_DOCUMENT_ATTRIBUTE_CHANGED = 0x103;

    /**
     * The contents of the document have changed.
     */
    public static final int IA2_EVENT_DOCUMENT_CONTENT_CHANGED = 0x104;

    /**
     * The loading of the document has completed.
     */
    public static final int IA2_EVENT_DOCUMENT_LOAD_COMPLETE = 0x105;

    /**
     * The loading of the document was interrupted.
     */
    public static final int IA2_EVENT_DOCUMENT_LOAD_STOPPED = 0x106;

    /**
     * The document contents are being reloaded.
     */
    public static final int IA2_EVENT_DOCUMENT_RELOAD = 0x107;

    /**
     * The ending index of this link within the containing string has changed.
     */
    public static final int IA2_EVENT_HYPERLINK_END_INDEX_CHANGED = 0x108;

    /**
     * The number of anchors assoicated with this hyperlink object has changed.
     */
    public static final int IA2_EVENT_HYPERLINK_NUMBER_OF_ANCHORS_CHANGED = 0x109;

    /**
     * The hyperlink selected state changed from selected to unselected or from
     * unselected to selected.
     */
    public static final int IA2_EVENT_HYPERLINK_SELECTED_LINK_CHANGED = 0x10A;

    /**
     * One of the links associated with the hypertext object has been activated.
     */
    public static final int IA2_EVENT_HYPERTEXT_LINK_ACTIVATED = 0x10B;

    /**
     * One of the links associated with the hypertext object has been selected.
     */
    public static final int IA2_EVENT_HYPERTEXT_LINK_SELECTED = 0x10C;

    /**
     * The starting index of this link within the containing string has changed.
     */
    public static final int IA2_EVENT_HYPERLINK_START_INDEX_CHANGED = 0x10D;

    /**
     * Focus has changed from one hypertext object to another, or focus moved
     * from a non-hypertext object to a hypertext object, or focus moved from a
     * hypertext object to a non-hypertext object.
     */
    public static final int IA2_EVENT_HYPERTEXT_CHANGED = 0x10E;

    /**
     * The number of hyperlinks associated with a hypertext object changed
     */
    public static final int IA2_EVENT_HYPERTEXT_NLINKS_CHANGED = 0x10F;

    /**
     * An object's attributes changed. Also see
     * ::IA2_EVENT_TEXT_ATTRIBUTE_CHANGED.
     */
    public static final int IA2_EVENT_OBJECT_ATTRIBUTE_CHANGED = 0x110;

    /**
     * A slide changed in a presentation document or a page boundary was crossed
     * in a word processing document.
     */
    public static final int IA2_EVENT_PAGE_CHANGED = 0x111;

    /** The caret moved from one section to the next.
     */
    public static final int IA2_EVENT_SECTION_CHANGED = 0x112;

    /**
     * A table caption changed.
     */
    public static final int IA2_EVENT_TABLE_CAPTION_CHANGED = 0x113;

    /**
     * A table's column description changed.
     */
    public static final int IA2_EVENT_TABLE_COLUMN_DESCRIPTION_CHANGED = 0x114;

    /**
     * A table's column header changed.
     */
    public static final int IA2_EVENT_TABLE_COLUMN_HEADER_CHANGED = 0x115;

    /**
     * A table's data changed.
     */
    public static final int IA2_EVENT_TABLE_MODEL_CHANGED = 0x116;

    /**
     * A table's row description changed.
     */
    public static final int IA2_EVENT_TABLE_ROW_DESCRIPTION_CHANGED = 0x117;

    /**
     * A table's row header changed.
     */
    public static final int IA2_EVENT_TABLE_ROW_HEADER_CHANGED = 0x118;

    /**
     * A table's summary changed.
     */
    public static final int IA2_EVENT_TABLE_SUMMARY_CHANGED = 0x119;

    /**
     * A text object's attributes changed. Also see
     * ::IA2_EVENT_OBJECT_ATTRIBUTE_CHANGED.
     */
    public static final int IA2_EVENT_TEXT_ATTRIBUTE_CHANGED = 0x11A;

    /**
     * The caret has moved to a new position.
     */
    public static final int IA2_EVENT_TEXT_CARET_MOVED = 0x11B;

    /**
     * This event indicates general text changes, i.e. changes to text that is
     * exposed through the IAccessibleText and IAccessibleEditableText
     * interfaces.
     */
    public static final int IA2_EVENT_TEXT_CHANGED = 0x11C;

    /** The caret moved from one column to the next.
     */
    public static final int IA2_EVENT_TEXT_COLUMN_CHANGED = 0x11D;
    
    /**
     * Text was inserted.
     */
    public static final int IA2_EVENT_TEXT_INSERTED = 0x11E;

    /**
     * Text was removed.
     */
    public static final int IA2_EVENT_TEXT_REMOVED = 0x11F;

    /**
     * Text was updated.
     */
    public static final int IA2_EVENT_TEXT_UPDATED = 0x120;

    /**
     * The text selection changed.
     */
    public static final int IA2_EVENT_TEXT_SELECTION_CHANGED = 0x121;

    /**
     * A visibile data event indicates the change of the visual appearance of an
     * accessible object. This includes for example most of the attributes
     * available via the IAccessibleComponent interface.
     */
    public static final int IA2_EVENT_VISIBLE_DATA_CHANGED = 0x122;


/* 
 * IAccessible2 state constants. 
 */

   /** Indicates a window is currently the active window. */
   public static final int IA2_STATE_ACTIVE =					0x1;

   /** Indicates that the object is armed. */
   public static final int IA2_STATE_ARMED =					0x2;

   /** Indicates the user interface object corresponding to this object no longer exists. */
   public static final int IA2_STATE_DEFUNCT =					0x4;

   /** Indicates the user can change the contents of this object. */
   public static final int IA2_STATE_EDITABLE =					0x8;

   /** Indicates the orientation of this object is horizontal. */
   public static final int IA2_STATE_HORIZONTAL =				0x10;

   /** Indicates this object is minimized and is represented only by an icon. */
   public static final int IA2_STATE_ICONIFIED =				0x20;

   /** Indicates an input validation failure. */
   public static final int IA2_STATE_INVALID_ENTRY =			0x40;

   /** Indicates that this object manages its children.

    Used when children are transient.  In this case it is not necessary to add
     listeners to the children.

    The state is added to improve performance in the case of large containers such 
     as tables. When an object manages its children it is not necessary to iterate 
     over all the children and add listeners.  The parent object will provide state 
     notifications regarding the state of its children.
   */
   public static final int IA2_STATE_MANAGES_DESCENDANTS =		0x80;

   /** Indicates that an object is modal.

    Modal objects have the behavior that something must be done with the object 
     before the user can interact with an object in a different window.
   */
   public static final int IA2_STATE_MODAL =					0x100;

   /** Indicates this text object can contain multiple lines of text. */
   public static final int IA2_STATE_MULTI_LINE =				0x200;

   /** Indicates this object paints every pixel within its rectangular region. */
   public static final int IA2_STATE_OPAQUE =					0x400;

   /** Indicates that user interaction is required.

    An example of when this state is used is when a field in a form must be filled 
     before a form can be processed.
   */
   public static final int IA2_STATE_REQUIRED =					0x800;

   /** Indicates an object which supports text selection.

    Note: This is different than MSAA STATE_SYSTEM_SELECTABLE.
   */
   public static final int IA2_STATE_SELECTABLE_TEXT =			0x1000;

   /** Indicates that this text object can contain only a single line of text. */
   public static final int IA2_STATE_SINGLE_LINE =				0x2000;

   /** Indicates that the accessible object is stale.

    This state is used when the accessible object no longer accurately 
     represents the state of the object which it is representing such as when an
     object is transient or when an object has been or is in the process of being
     destroyed.
   */
   public static final int IA2_STATE_STALE =					0x4000;

   /** Indicates that the object implements autocompletion.

    This state indicates that that a text control will respond to the input of 
    one ore more characters and cause a sub-item to become selected.  The 
    selection may also result in events fired on the parent object.
   */
   public static final int IA2_STATE_SUPPORTS_AUTOCOMPLETION =	0x8000;

   /** Indicates this object is transient. */
   public static final int IA2_STATE_TRANSIENT =				0x10000;

   /** Indicates the orientation of this object is vertical. */
   public static final int IA2_STATE_VERTICAL =					0x20000;

   
/*
 *  These constants control the scrolling of an object or substring into a window. 
 */

      /** Scroll the top left of the object or substring to the top left of the 
       window (or as close as possible).
      */
    public static final int IA2_SCROLL_TYPE_TOP_LEFT = 0; 		

      /** Scroll the bottom right of the object or substring to the bottom right of
       the window (or as close as possible). 
      */
    public static final int IA2_SCROLL_TYPE_BOTTOM_RIGHT = 1;

      /** Scroll the top edge of the object or substring to the top edge of the 
       window (or as close as possible). 
      */
    public static final int IA2_SCROLL_TYPE_TOP_EDGE = 2;

      /** Scroll the bottom edge of the object or substring to the bottom edge of 
       the window (or as close as possible). 
      */
    public static final int IA2_SCROLL_TYPE_BOTTOM_EDGE = 3; 

      /** Scroll the left edge of the object or substring to the left edge of the 
       window (or as close as possible). 
      */
    public static final int IA2_SCROLL_TYPE_LEFT_EDGE = 4; 

      /** Scroll the right edge of the object or substring to the right edge of the 
       window (or as close as possible). 
      */
    public static final int IA2_SCROLL_TYPE_RIGHT_EDGE =5;

    public static String getScrollTypeText(int scrollType) {
		switch( scrollType ) {
			case IA2_SCROLL_TYPE_TOP_LEFT: return "IA2_SCROLL_TYPE_TOP_LEFT"; //$NON-NLS-1$
			case IA2_SCROLL_TYPE_BOTTOM_RIGHT: return "IA2_SCROLL_TYPE_BOTTOM_RIGHT"; //$NON-NLS-1$
			case IA2_SCROLL_TYPE_TOP_EDGE: return "IA2_SCROLL_TYPE_TOP_EDGE"; //$NON-NLS-1$
			case IA2_SCROLL_TYPE_BOTTOM_EDGE: return "IA2_SCROLL_TYPE_BOTTOM_EDGE"; //$NON-NLS-1$
			case IA2_SCROLL_TYPE_LEFT_EDGE: return "IA2_SCROLL_TYPE_LEFT_EDGE"; //$NON-NLS-1$
			case IA2_SCROLL_TYPE_RIGHT_EDGE: return "IA2_SCROLL_TYPE_RIGHT_EDGE"; //$NON-NLS-1$
		}
    	return null;
    }
    
/* 
 * These constants define which coordinate system a point is located in.
 */
      /** The coordinates are relative to the screen.
       */
	public static final int IA2_COORDTYPE_SCREEN_RELATIVE = 0; 

      /** The coordinates are relative to the upper left corner of the bounding box
       of the immediate parent.
      */
	public static final int IA2_COORDTYPE_PARENT_RELATIVE = 1;  
	
	public static String getCoordTypeText(int coordType) {
		switch( coordType ) {
			case IA2_COORDTYPE_SCREEN_RELATIVE: return "IA2_COORDTYPE_SCREEN_RELATIVE"; //$NON-NLS-1$
			case IA2_COORDTYPE_PARENT_RELATIVE: return "IA2_COORDTYPE_PARENT_RELATIVE"; //$NON-NLS-1$
		}
		return null;
	}
    
    // IA2TextBoundaryType
    public static final int IA2_TEXT_BOUNDARY_CHAR      = 0;
    public static final int IA2_TEXT_BOUNDARY_WORD      = 1;
    public static final int IA2_TEXT_BOUNDARY_SENTENCE  = 2;
    public static final int IA2_TEXT_BOUNDARY_PARAGRAPH = 3;
    public static final int IA2_TEXT_BOUNDARY_LINE      = 4;
    public static final int IA2_TEXT_BOUNDARY_ALL       = 5;
    
    public static String getTextBoundaryTypeText(int textBoundaryType) {
        switch( textBoundaryType ) {
            case IA2_TEXT_BOUNDARY_CHAR: return "IA2_TEXT_BOUNDARY_CHAR"; //$NON-NLS-1$
            case IA2_TEXT_BOUNDARY_WORD: return "IA2_TEXT_BOUNDARY_WORD"; //$NON-NLS-1$
            case IA2_TEXT_BOUNDARY_SENTENCE: return "IA2_TEXT_BOUNDARY_SENTENCE"; //$NON-NLS-1$
            case IA2_TEXT_BOUNDARY_PARAGRAPH: return "IA2_TEXT_BOUNDARY_PARAGRAPH"; //$NON-NLS-1$
            case IA2_TEXT_BOUNDARY_LINE: return "IA2_TEXT_BOUNDARY_LINE"; //$NON-NLS-1$
            case IA2_TEXT_BOUNDARY_ALL: return "IA2_TEXT_BOUNDARY_ALL"; //$NON-NLS-1$
        }
        return null;
    }
}
