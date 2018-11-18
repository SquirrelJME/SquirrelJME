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

import cc.squirreljme.runtime.lcdui.SerializedEvent;

/**
 * This class acts as the lowest base for displays and items.
 *
 * @since 2018/03/23
 */
abstract class __Widget__
{
	/** The draw chain of this widget. */
	final __DrawChain__ _drawchain =
		new __DrawChain__();
	
	/** The parent widget being used. */
	volatile __Widget__ _parent;
	
	/**
	 * Updates the draw chain for this widget.
	 *
	 * @since 2018/11/18
	 */
	abstract void __updateDrawChain(__DrawSlice__ __sl);
	
	/**
	 * Returns the default height.
	 *
	 * @return The default height.
	 * @since 2018/11/18
	 */
	final int __defaultHeight()
	{
		__Widget__ parent = this._parent;
		if (parent == null)
			return Display.getDisplays(0)[0].getHeight();
		return this._drawchain._h;
	}
	
	/**
	 * Returns the default width.
	 *
	 * @return The default width.
	 * @since 2018/11/18
	 */
	final int __defaultWidth()
	{
		__Widget__ parent = this._parent;
		if (parent == null)
			return Display.getDisplays(0)[0].getWidth();
		return this._drawchain._w;
	}
	
	/**
	 * The bit that is used to check support.
	 *
	 * @return The bit for support.
	 * @since 2018/11/17
	 */
	int __supportBit()
	{
		throw new RuntimeException("OOPS " + this.getClass().getName());
	}
}

