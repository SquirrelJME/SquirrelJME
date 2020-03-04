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
 * This is an interface for writing memory at the byte-level.
 *
 * @since 2020/03/03
 */
public interface WritableByteMemory
{
	/**
	 * Writes the given byte to the specified address.
	 *
	 * @param __addr The address to write.
	 * @param __b The value to write.
	 * @throws MemoryAccessException If memory could not be written.
	 * @since 2020/03/03
	 */
	void write(long __addr, byte __b)
		throws MemoryAccessException;
	
	/**
	 * Writes multiple bytes at the given address.
	 *
	 * @param __addr The address to write to.
	 * @param __b The byte array.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to transfer.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws MemoryAccessException If memory could not be written.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/03
	 */
	void write(long __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, MemoryAccessException,
			NullPointerException;
}
