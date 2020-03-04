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
 * This represents memory that is writable at a basic type level.
 *
 * @since 2020/03/03
 */
public interface WritableBasicMemory
	extends WritableByteMemory
{
	/**
	 * Writes the given value to memory.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @throws MemoryAccessException If the value could not be written.
	 * @since 2020/03/03
	 */
	void writeInt(long __addr, int __v)
		throws MemoryAccessException;
	
	/**
	 * Writes the given value to memory.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @throws MemoryAccessException If the value could not be written.
	 * @since 2020/03/03
	 */
	void writeLong(long __addr, long __v)
		throws MemoryAccessException;
	
	/**
	 * Writes the given value to memory.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @throws MemoryAccessException If the value could not be written.
	 * @since 2020/03/03
	 */
	void writeShort(long __addr, long __v)
		throws MemoryAccessException;
}
