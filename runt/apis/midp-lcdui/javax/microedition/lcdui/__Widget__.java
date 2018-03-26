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

import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.LcdServiceCall;

/**
 * This class acts as the lowest base for displays and items.
 *
 * @since 2018/03/23
 */
abstract class __Widget__
	extends __Collectable__
{
	/**
	 * Initializes the widget using a handle which is registered on the
	 * remote end.
	 *
	 * @param __h The handle to use.
	 * @since 2018/03/23
	 */
	__Widget__()
	{
		super();
	}
	
	/**
	 * Initializes the widget using the given handle.
	 *
	 * @param __h The handle to use.
	 * @since 2018/03/23
	 */
	__Widget__(int __h)
	{
		super(__h);
	}
	
	/**
	 * This is called when a repaint is to be performed.
	 *
	 * @param __g The graphics to draw into.
	 * @param __bw The buffer width.
	 * @param __bh The buffer height.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	@__SerializedEvent__
	abstract void __doPaint(Graphics __g, int __bw, int __bh)
		throws NullPointerException;
	
	/**
	 * This is called when a widget's visibility has changed.
	 *
	 * @param __shown Is the widget shown?
	 * @since 2018/03/24
	 */
	@__SerializedEvent__
	abstract void __doShown(boolean __shown);
	
	/**
	 * This is called when the size of the widget has changed.
	 *
	 * @param __w The new width.
	 * @param __h The new height.
	 * @since 2018/03/23
	 */
	@__SerializedEvent__
	abstract void __doSizeChanged(int __w, int __h);
	
	/**
	 * Returns the height of the displayable or the maximum size of the
	 * default display.
	 *
	 * @return The displayable height or the maximum height of the default
	 * display.
	 * @since 2017/05/24
	 */
	final int __getHeight()
	{
		int rv = LcdServiceCall.<Integer>call(Integer.class,
			LcdFunction.WIDGET_GET_HEIGHT, this._handle);
		if (rv < 0)
			return Display.getDisplays(0)[0].getHeight();
		return rv;
	}
	
	/**
	 * Returns the width of the displayable or the maximum size of the
	 * default display.
	 *
	 * @return The displayable width or the maximum width of the default
	 * display.
	 * @since 2017/05/24
	 */
	final int __getWidth()
	{
		int rv = LcdServiceCall.<Integer>call(Integer.class,
			LcdFunction.WIDGET_GET_WIDTH, this._handle);
		if (rv < 0)
			return Display.getDisplays(0)[0].getWidth();
		return rv;
	}
}

