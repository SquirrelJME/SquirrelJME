// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

/**
 * This interface is used to flag the types used for flags.
 *
 * @since 2016/04/23
 */
public interface Flag
{
	/**
	 * Returns the flag ordinal.
	 *
	 * @return The ordinal of the flag.
	 * @since 2016/04/23
	 */
	public abstract int ordinal();
}

