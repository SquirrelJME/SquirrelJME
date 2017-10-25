// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.widget;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.lcdui.DisplayManager;

/**
 * This is the base interface for items which can be embedded into a
 * displayable.
 *
 * @since 2017/10/25
 */
public abstract class Embedded
{
	/** The widget this is embedded into. */
	private volatile Reference<DisplayableWidget> _inside;
	
	/**
	 * Specifies that this widget was embedded to the given displayable.
	 *
	 * @param __dw The displayable widget.
	 * @throws IllegalStateException If the widget has already been embedded.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/25
	 */
	final void __embeddedInto(DisplayableWidget __dw)
		throws IllegalStateException, NullPointerException
	{
		if (__dw == null)
			throw new NullPointerException("NARG");
		
		// Use global lock
		synchronized (DisplayManager.GLOBAL_LOCK)
		{
			// {@squirreljme.error EB1z This embedded has already been
			// embedded into a widget.}
			if (this._inside != null)
				throw new IllegalStateException("EB1z");
			
			// Store
			this._inside = new WeakReference<>(__dw);
		}
	}
}

