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

import cc.squirreljme.runtime.lcdui.common.CommonColors;
import cc.squirreljme.runtime.lcdui.common.CommonMetrics;

/**
 * This represents a scrollbar which manages.
 *
 * @since 2018/12/02
 */
public class __Scrollbar__
	extends __Drawable__
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	final void __drawChain(Graphics __g)
	{
		// If the scrollbar was hidden then draw none of it
		__DrawChain__ chain = this._drawchain;
		int x = chain.x,
			y = chain.y,
			w = chain.w,
			h = chain.h;
		if (w <= 0 || h <= 0)
			return;
		
		throw new todo.TODO();
	}
	
	/**
	 * Slices in the scrollbar, taking its region and returning the remaining
	 * region.
	 *
	 * @param __sl The slice.
	 * @return The new slice.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/02
	 */
	final __DrawSlice__ __sliceIn(__DrawSlice__ __sl)
		throws NullPointerException
	{
		if (__sl == null)
			throw new NullPointerException("NARG");
		
		__DrawChain__ chain = this._drawchain;
		
		// Only need the x and width mostly
		int x = __sl.x,
			y = __sl.y,
			w = __sl.w,
			h = __sl.h;
		
		// No room for the scrollbar?
		if (w < CommonMetrics.SCROLLBAR_WIDTH)
		{
			chain.set(0, 0, 0, 0);
			return __sl;
		}
		
		// Set scrollbar properties
		chain.set(x + (w - CommonMetrics.SCROLLBAR_WIDTH), y,
			CommonMetrics.SCROLLBAR_WIDTH, h);
		
		// Slice less room because of the scrollbar
		return new __DrawSlice__(x, y, w - CommonMetrics.SCROLLBAR_WIDTH, h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	final void __updateDrawChain(__DrawSlice__ __sl)
	{
		this.__sliceIn(__sl);
	}
}

