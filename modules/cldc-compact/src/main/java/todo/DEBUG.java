// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package todo;

import cc.squirreljme.runtime.cldc.Poking;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This class contains static methods for debug printing messages as needed.
 *
 * @since 2018/04/09
 */
@Deprecated
public final class DEBUG
{
	/**
	 * Not used.
	 *
	 * @since 2018/04/09
	 */
	@Deprecated
	private DEBUG()
	{
	}
	
	/**
	 * Prints a debug note to standard error about something that is
	 * incomplete.
	 *
	 * @param __fmt The format string.
	 * @param __args The arguments to the call.
	 * @since 2018/04/09
	 */
	@Deprecated
	public static final void note(String __fmt, Object... __args)
	{
		Debugging.debugNote(__fmt, __args);
	}
}

