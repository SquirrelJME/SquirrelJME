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

import cc.squirreljme.runtime.lcdui.LCDUIProbe;
import cc.squirreljme.runtime.lcdui.ui.UIDrawable;
import cc.squirreljme.runtime.lcdui.ui.UIStack;

/**
 * This is the actual probe implementation which allows access to LCDUI
 * internals.
 *
 * @since 2018/12/02
 */
final class __LCDUIProbe__
	extends LCDUIProbe
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public final void draw(UIDrawable __drawable, UIStack __parent,
		UIStack __self, Graphics __g)
	{
		((__Drawable__)__drawable).__draw(__parent, __self, __g);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public void hintDimensions(UIDrawable __draw, int __w, int __h)
	{
		if (__draw instanceof __Widget__)
		{
			__Widget__ w = ((__Widget__)__draw);
			
			w._lastuiwidth = __w;
			w._lastuiheight = __h;
		}
	}
}

