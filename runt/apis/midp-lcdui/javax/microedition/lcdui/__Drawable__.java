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

import cc.squirreljme.runtime.lcdui.ui.UIDrawable;
import cc.squirreljme.runtime.lcdui.ui.UIStack;

/**
 * Represents something can be drawn on.
 *
 * @since 2018/12/08
 */
public abstract class __Drawable__
	implements UIDrawable
{
	/**
	 * Draws this drawble.
	 *
	 * @param __parent The parent stack.
	 * @param __self The self stack.
	 * @param __g The graphics to draw into.
	 * @since 2018/12/08
	 */
	abstract void __draw(UIStack __parent, UIStack __self, Graphics __g);
}

