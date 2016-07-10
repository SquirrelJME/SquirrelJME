// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.data;

/**
 * This is used by both data streams to indicate that they allow their default
 * endianess to be set and obtained.
 *
 * @since 2016/07/10
 */
public interface SettableEndianess
{
	/**
	 * Obtains the current default endianess of the data.
	 *
	 * @return The current endianess.
	 * @since 2016/07/10
	 */
	public abstract DataEndianess getEndianess();
	
	/**
	 * Sets the endianess of the data.
	 *
	 * @param __end The new default endianess to use.
	 * @return The old endianess.
	 * @throws NullPointerException If no endianess was specified.
	 * @since 2016/07/10
	 */
	public abstract DataEndianess setEndianesss(DataEndianess __end)
		throws NullPointerException;
}

