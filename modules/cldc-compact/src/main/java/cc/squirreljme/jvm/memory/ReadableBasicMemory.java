// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.memory;

/**
 * This represents memory which is readable at a basic type level.
 *
 * @since 2020/03/03
 */
public interface ReadableBasicMemory
	extends ReadableByteMemory
{
	/**
	 * Reads the value from the given address.
	 *
	 * @param __addr The address to read from.
	 * @return The read value.
	 * @throws MemoryAccessException If memory could not be read.
	 * @since 2020/03/03
	 */
	int readInt(long __addr)
		throws MemoryAccessException;
	
	/**
	 * Reads the value from the given address.
	 *
	 * @param __addr The address to read from.
	 * @return The read value.
	 * @throws MemoryAccessException If memory could not be read.
	 * @since 2020/03/03
	 */
	long readLong(long __addr)
		throws MemoryAccessException;
	
	/**
	 * Reads the value from the given address.
	 *
	 * @param __addr The address to read from.
	 * @return The read value.
	 * @throws MemoryAccessException If memory could not be read.
	 * @since 2020/03/03
	 */
	short readShort(long __addr)
		throws MemoryAccessException;
}
