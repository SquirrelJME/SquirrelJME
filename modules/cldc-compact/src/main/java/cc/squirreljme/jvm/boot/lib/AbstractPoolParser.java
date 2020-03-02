// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot.lib;

import cc.squirreljme.jvm.boot.io.BinaryBlob;

/**
 * This is the base class for a pool parser.
 *
 * @since 2019/10/13
 */
public abstract class AbstractPoolParser
{
	/**
	 * Returns the number of pool entries that exist.
	 *
	 * @param __ft Does this fall through to the actually backed pool?
	 * @return The pool entry count.
	 * @since 2019/11/25
	 */
	public abstract int count(boolean __ft)
		throws InvalidClassFormatException;
	
	/**
	 * Returns a blob to the entry's data for parsing.
	 *
	 * @param __dx The index of the entry to get.
	 * @param __ft Does this get fall through to the actually backed pool?
	 * @return The blob to the entry data.
	 * @throws IndexOutOfBoundsException If it is outside of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/11/24
	 */
	public abstract BinaryBlob entryData(int __dx, boolean __ft)
		throws IndexOutOfBoundsException, InvalidClassFormatException;
	
	/**
	 * Returns the number of parts this entry has.
	 *
	 * @param __dx The index of the entry to get.
	 * @param __ft Does this get fall through to the actually backed pool?
	 * @return The read parts
	 * @throws IndexOutOfBoundsException If it is outside of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/11/25
	 */
	public abstract short[] entryParts(int __dx, boolean __ft)
		throws IndexOutOfBoundsException, InvalidClassFormatException;
	
	/**
	 * Return the type a pool entry is.
	 *
	 * @param __dx The index of the entry to get.
	 * @param __ft Does this get fall through to the actually backed pool?
	 * @return The entry type.
	 * @throws IndexOutOfBoundsException If it is outside of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/11/24
	 */
	public abstract int entryType(int __dx, boolean __ft)
		throws IndexOutOfBoundsException, InvalidClassFormatException;
	
	/**
	 * Returns the number of pool entries that exist.
	 *
	 * @return The pool entry count.
	 * @since 2019/11/25
	 */
	public final int count()
		throws InvalidClassFormatException
	{
		return this.count(false);
	}
		
	/**
	 * Returns a blob to the entry's data for parsing.
	 *
	 * @param __dx The index of the entry to get.
	 * @return The blob to the entry data.
	 * @throws IndexOutOfBoundsException If it is outside of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/11/24
	 */
	public final BinaryBlob entryData(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return this.entryData(__dx, false);
	}
	
	/**
	 * Returns the number of parts this entry has.
	 *
	 * @param __dx The index of the entry to get.
	 * @return The read parts
	 * @throws IndexOutOfBoundsException If it is outside of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/11/25
	 */
	public final short[] entryParts(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return this.entryParts(__dx, false);
	}
	
	/**
	 * Return the type a pool entry is.
	 *
	 * @param __dx The index of the entry to get.
	 * @return The entry type.
	 * @throws IndexOutOfBoundsException If it is outside of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/11/24
	 */
	public final int entryType(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return this.entryType(__dx, false);
	}
}

