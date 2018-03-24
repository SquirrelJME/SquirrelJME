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
 * This class acts as the lowest base for displays and items.
 *
 * @since 2018/03/23
 */
abstract class __Widget__
{
	/** The handle of this item. */
	final int _handle;
	
	/**
	 * Initializes the widget using a handle which is registered on the
	 * remote end.
	 *
	 * @param __h The handle to use.
	 * @since 2018/03/23
	 */
	__Widget__()
	{
		this._handle = __Queue__.INSTANCE.__register(this);
	}
	
	/**
	 * Initializes the widget using the given handle.
	 *
	 * @param __h The handle to use.
	 * @since 2018/03/23
	 */
	__Widget__(int __h)
	{
		this._handle = __h;
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
	abstract void __doPaint(Graphics __g, int __bw, int __bh)
		throws NullPointerException;
	
	/**
	 * This is called when the size of the widget has changed.
	 *
	 * @param __w The new width.
	 * @param __h The new height.
	 * @since 2018/03/23
	 */
	abstract void __doSizeChanged(int __w, int __h);
}

