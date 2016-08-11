// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.data;

/**
 * This interface is used to describe classes which allow access to data via
 * a specific position.
 *
 * @since 2016/08/11
 */
public interface RandomAccessData
	extends GettableEndianess
{
	/**
	 * Reads a byte at the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds.
	 * @since 2016/08/11
	 */
	public abstract int readByte(int __p)
		throws IndexOutOfBoundsException;
}

