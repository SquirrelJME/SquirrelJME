// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

/**
 * Contains the packing information for the widget which determines how it
 * it is placed on the display.
 *
 * @since 2018/11/18
 */
final class __Packing__
{
	/** The X position. */
	int _x;
	
	/** The Y position. */
	int _y;
	
	/** The width. */
	int _w;
	
	/** The height. */
	int _h;
	
	/**
	 * Initializes the default packing.
	 *
	 * @since 2018/11/18
	 */
	__Packing__()
	{
	}
	
	/**
	 * Initializes with the given packing.
	 *
	 * @param __x The x position.
	 * @param __y The y position.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/11/18
	 */
	__Packing__(int __x, int __y, int __w, int __h)
	{
		this._x = __x;
		this._y = __y;
		this._w = __w;
		this._h = __h;
	}
}

