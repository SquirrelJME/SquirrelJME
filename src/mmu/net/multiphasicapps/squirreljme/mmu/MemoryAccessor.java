// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.mmu;

/**
 * This is an interface which is used to access memory.
 *
 * Memory is split into 4 billion blocks/pages/banks with an address space for
 * each of them having 64-bit access. For example on an NES where code and data
 * may be banked, the block number is used to access data that exists in
 * another bank. Another example on the 286 would be segmented memory, although
 * the address space is only 16-bits wide, segments can allow access to more
 * memory.
 *
 * @since 2016/06/09
 */
public interface MemoryAccessor
{
	/**
	 * Reads a single byte value.
	 *
	 * @param __bl The block to read from.
	 * @param __addr The address to read to.
	 * @return The read value.
	 * @throws MemoryReadException If the memory could not be read from.
	 * @since 2016/06/09
	 */
	public abstract byte readByte(int __bl, long __addr)
		throws MemoryReadException;
	
	/**
	 * Reads a single int value.
	 *
	 * @param __bl The block to read from.
	 * @param __addr The address to read to.
	 * @return The read value.
	 * @throws MemoryReadException If the memory could not be read from.
	 * @since 2016/06/09
	 */
	public abstract int readInt(int __bl, long __addr)
		throws MemoryReadException;
	
	/**
	 * Reads a single long value.
	 *
	 * @param __bl The block to read from.
	 * @param __addr The address to read to.
	 * @return The read value.
	 * @throws MemoryReadException If the memory could not be read from.
	 * @since 2016/06/09
	 */
	public abstract long readLong(int __bl, long __addr)
		throws MemoryReadException;
	
	/**
	 * Reads a single short value.
	 *
	 * @param __bl The block to read from.
	 * @param __addr The address to read to.
	 * @return The read value.
	 * @throws MemoryReadException If the memory could not be read from.
	 * @since 2016/06/09
	 */
	public abstract short readShort(int __bl, long __addr)
		throws MemoryReadException;
}

