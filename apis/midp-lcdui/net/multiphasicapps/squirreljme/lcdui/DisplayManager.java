// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is a provider which always returns a fixed set of display heads that
 * are available for usage.
 *
 * This is intended to be used as a system service.
 *
 * @since 2017/08/19
 */
public abstract class DisplayManager
{
	/**
	 * Anything which happens in displays are thread safe and serialized
	 * between all potential displays. Since there can be multiple displays
	 * running at the same time all managing other widgets, it is more
	 * effective for situations where there is global state to maintain that
	 * a single lock is used, despite some performance costs.
	 */
	public static final Object GLOBAL_LOCK =
		new Object();
	
	/**
	 * Returns the display heads which are available on the system.
	 *
	 * @return The array of available display heads, this should always
	 * return the same elements.
	 * @since 2017/08/19
	 */
	public abstract DisplayHead[] heads();
}

