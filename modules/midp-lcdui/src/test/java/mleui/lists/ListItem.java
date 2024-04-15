// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package mleui.lists;

import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import java.util.Objects;

/**
 * Represents a single item's data on a list.
 *
 * @since 2020/10/27
 */
public final class ListItem
{
	/** Is this enabled? */
	boolean _disabled;
	
	/** Is this selected? */
	boolean _selected;
	
	/** Icon dimension. */
	int _iconDimension;
	
	/** ID Code. */
	int _idCode;
	
	/** The label. */
	String _label;
	
	/** The font to use. */
	int _font;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/27
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof ListItem))
			return false;
		
		ListItem o = (ListItem)__o;
		return this.hashCode() == o.hashCode() &&
			this._disabled == o._disabled &&
			this._selected == o._selected &&
			this._iconDimension == o._iconDimension &&
			this._idCode == o._idCode &&
			Objects.equals(this._label, o._label) &&
			this._font == o._font;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/27
	 */
	@Override
	public final int hashCode()
	{
		int rv = 0;
		
		rv ^= (this._selected ? 0x8000_0000 : 0x4000_0000);
		rv ^= (this._disabled ? 0x0800_0000 : 0x0400_0000);
		rv ^= (this._label != null ?
			(this._label.hashCode() | 0x0080_0000) : 0x0040_0000);
		rv ^= this._iconDimension;
		rv ^= this._idCode;
		rv ^= this._font;
		
		return rv;
	}
	
	/**
	 * Stores this item into the list.
	 * 
	 * @param __backend The backend.
	 * @param __list The list to read from.
	 * @param __i The index to read.
	 * @since 2020/10/29
	 */
	public void into(UIBackend __backend, UIItemBracket __list, int __i)
	{
		__backend.widgetProperty(__list, 
			UIWidgetProperty.INT_LIST_ITEM_DISABLED, __i,
			(this._disabled ? 1 : 0));
		__backend.widgetProperty(__list,
			UIWidgetProperty.INT_LIST_ITEM_SELECTED, __i,
			(this._selected ? 1 : 0));
		__backend.widgetProperty(__list,
			UIWidgetProperty.INT_LIST_ITEM_ICON_DIMENSION, __i,
			this._iconDimension);
		__backend.widgetProperty(__list,
			UIWidgetProperty.INT_LIST_ITEM_ID_CODE, __i,
			this._idCode);
			
		__backend.widgetProperty(__list,
			UIWidgetProperty.STRING_LIST_ITEM_LABEL, __i,
			this._label);
		__backend.widgetProperty(__list,
			UIWidgetProperty.INT_LIST_ITEM_FONT, __i,
			this._font);
	}
	
	/**
	 * Reads a list item from the backend.
	 * 
	 * @param __backend The backend.
	 * @param __list The list to read from.
	 * @param __i The index to read.
	 * @return The list item for the given item.
	 * @since 2020/10/29
	 */
	public static ListItem of(UIBackend __backend, UIItemBracket __list,
		int __i)
	{
		ListItem rv = new ListItem();
		
		rv._disabled = (0 != __backend.widgetPropertyInt(__list,
			UIWidgetProperty.INT_LIST_ITEM_DISABLED, __i));
		rv._selected = (0 != __backend.widgetPropertyInt(__list,
			UIWidgetProperty.INT_LIST_ITEM_SELECTED, __i));
		rv._iconDimension = __backend.widgetPropertyInt(__list,
			UIWidgetProperty.INT_LIST_ITEM_ICON_DIMENSION, __i);
		rv._idCode = __backend.widgetPropertyInt(__list,
			UIWidgetProperty.INT_LIST_ITEM_ID_CODE, __i);
		rv._font = __backend.widgetPropertyInt(__list,
			UIWidgetProperty.INT_LIST_ITEM_FONT, __i);
			
		rv._label = __backend.widgetPropertyStr(__list,
			UIWidgetProperty.STRING_LIST_ITEM_LABEL, __i);
		
		return rv;
	}
}
