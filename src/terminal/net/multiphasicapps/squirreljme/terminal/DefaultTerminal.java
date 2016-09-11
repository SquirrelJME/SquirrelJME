// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terminal;

/**
 * This caches and returns the default terminal device to use.
 *
 * @since 2016/09/11
 */
public final class DefaultTerminal
{
	/**
	 * Not used.
	 *
	 * @since 2016/09/11
	 */
	private DefaultTerminal()
	{
	}
	
	/**
	 * Returns the default terminal or {@code null} if there is no default.
	 *
	 * @return The default terminal or {@code null} if there is none.
	 * @since 2016/09/11
	 */
	public static Terminal getDefault()
	{
		throw new Error("TODO");
	}
}

