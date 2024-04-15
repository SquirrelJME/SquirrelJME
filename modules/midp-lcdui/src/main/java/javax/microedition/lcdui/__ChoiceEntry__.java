// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

/**
 * This is a package public mutable class which represents single choices
 * within anything which uses choices.
 * 
 * These choices are only comparable against themselves
 *
 * @since 2017/08/20
 */
final class __ChoiceEntry__
{
	/** The string to display for this choice. */
	volatile String _label;
	
	/** The image to display for this choice. */
	volatile Image _image;
	
	/** The font to use for this choice. */
	volatile Font _font;
	
	/** Is this entry selected? */
	volatile boolean _selected;
	
	/** Is this item disabled? (is enabled by default) */
	volatile boolean _disabled;
	
	/**
	 * Initializes a choice entry with default values.
	 *
	 * @param __s The string to display.
	 * @param __i The image to display.
	 * @since 2017/08/20
	 */
	__ChoiceEntry__(String __s, Image __i)
	{
		// Set
		this._label = __s;
		this._image = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/18
	 */
	@Override
	public final boolean equals(Object __o)
	{
		return this == __o;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/18
	 */
	@Override
	public final int hashCode()
	{
		int rv = System.identityHashCode(this);
		
		rv ^= (this._selected ? 0x8000_0000 : 0x4000_0000);
		rv ^= (this._disabled ? 0x0800_0000 : 0x0400_0000);
		rv ^= (this._label != null ?
			(this._label.hashCode() | 0x0080_0000) : 0x0040_0000);
		rv ^= (this._image != null ?
			(this._image.hashCode() | 0x0008_0000) : 0x0004_0000);
		rv ^= (this._font != null ?
			(this._font.hashCode() | 0x0000_8000) : 0x0000_4000);
		
		return rv;
	}
}

