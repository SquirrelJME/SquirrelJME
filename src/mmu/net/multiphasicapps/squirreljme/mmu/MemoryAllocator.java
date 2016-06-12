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
 * This interface is specified along with the structure manager and is used as
 * an interface to allocate and free memory regions. The allocator here does
 * not perform automatic garbage collection, this is completely manual and as
 * such if a pointer is lost then the memory leaks.
 *
 * @since 2016/06/12
 */
public interface MemoryAllocator
{
	/**
	 * Allocates the given number of bytes and returns the address of the
	 * allocation.
	 *
	 * @param __mp The placement strategy of the memory region, if {@code null}
	 * then the allocator may choose any desirable amount.
	 * @param __bytes The minimum number of bytes to allocate a region of
	 * memory for.
	 * @return The address to the allocated region of memory.
	 * @throws IllegalArgumentException If the number of bytes to allocate is
	 * zero or negative.
	 * @throws MemoryAllocatException If the memory could not be allocated.
	 * @since 2016/06/12
	 */
	public long allocate(MemoryPlacementType __mp, long __bytes)
		throws IllegalArgumentException, MemoryAllocateException;
}

