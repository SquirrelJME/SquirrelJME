// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

/**
 * This is used by both data streams to indicate that they allow their default
 * endianess to be set and obtained.
 *
 * @since 2016/07/10
 */
public interface SettableEndianess
	extends GettableEndianess
{
	/**
	 * Sets the endianess of the data.
	 *
	 * @param __end The new default endianess to use.
	 * @return The old endianess.
	 * @throws NullPointerException If no endianess was specified.
	 * @since 2016/07/10
	 */
	public abstract DataEndianess setEndianess(DataEndianess __end)
		throws NullPointerException;
}

