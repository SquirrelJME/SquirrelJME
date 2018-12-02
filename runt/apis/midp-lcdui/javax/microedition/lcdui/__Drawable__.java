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
 * This is a base class which represents something drawbale.
 *
 * @since 2018/12/02
 */
abstract class __Drawable__
{
	/** The draw chain of this drawable. */
	final __DrawChain__ _drawchain =
		new __DrawChain__();
	
	/**
	 * Performs drawing of the widgets.
	 *
	 * @param __g The graphics to draw into.
	 * @since 2018/11/18
	 */
	abstract void __drawChain(Graphics __g);
	
	/**
	 * Updates the draw chain for this widget.
	 *
	 * @param __sl The draw slice which specifies the area that is available.
	 * @since 2018/11/18
	 */
	abstract void __updateDrawChain(__DrawSlice__ __sl);
	
	/**
	 * Performs drawing of the widgets, but with wrappers and such.
	 *
	 * @param __g The graphics to draw into.
	 * @since 2018/11/18
	 */
	final void __drawChainWrapped(Graphics __g)
	{
		// Normal draw chain
		this.__drawChain(__g);
		
		// Draw the children widgets
		this._drawchain.drawChildren(__g);
	}
}

