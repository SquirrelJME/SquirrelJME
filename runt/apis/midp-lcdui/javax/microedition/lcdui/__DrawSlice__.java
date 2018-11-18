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
 * The slice of the display that is available.
 *
 * @since 2018/11/18
 */
final class __DrawSlice__
{
	/** The X position. */
	public final int x;
	
	/** The Y position. */
	public final int y;
	
	/** The width. */
	public final int w;
	
	/** The height. */
	public final int h;
	
	/**
	 * Initializes the slice which specified space available.
	 *
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/11/18
	 */
	public __DrawSlice__(int __x, int __y, int __w, int __h)
	{
		this.x = __x;
		this.y = __y;
		this.w = __w;
		this.h = __h;
	}
}
