// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

import cc.squirreljme.jvm.io.BinaryBlob;

/**
 * This is the base class for a pool parser.
 *
 * @since 2019/10/13
 */
public abstract class AbstractPoolParser
{
	/**
	 * Returns a blob to the entry's data for parsing.
	 *
	 * @param __dx The index of the entry to get.
	 * @return The blob to the entry data.
	 * @throws IndexOutOfBoundsException If it is outside of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/11/24
	 */
	public abstract BinaryBlob entryData(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException;
	
	/**
	 * Return the type a pool entry is.
	 *
	 * @param __dx The index of the entry to get.
	 * @return The entry type.
	 * @throws IndexOutOfBoundsException If it is outside of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/11/24
	 */
	public abstract int entryType(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException;
}

