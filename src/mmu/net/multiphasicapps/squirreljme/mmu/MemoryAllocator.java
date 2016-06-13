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
@Deprecated
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
	public abstract long allocate(MemoryPlacementType __mp, long __bytes)
		throws IllegalArgumentException, MemoryAllocateException;
	
	/**
	 * Frees a given memory region that was previously allocated.
	 *
	 * @param __addr The address of the previously allocated region.
	 * @throws MemoryAllocateException If the specified address could not be
	 * freed such as if the specified address was never returned by a previous
	 * allocation.
	 * @since 2016/06/12
	 */
	public abstract void free(long __addr)
		throws MemoryAllocateException;
	
	/**
	 * This changes the size of an allocated region. If the resize operation
	 * cannot be performed (not enough memory supported, or it is not
	 * supported by an underlying allocator), then it must handle resize by
	 * allocating a new region, copying the old data, then free the old
	 * region.
	 *
	 * This does not relocate the allocated data, this only shrinks or
	 * potentially grows an allocated block of memory. If a growing resize
	 * operation fails then the methods using the allocator should either fail
	 * or instead reallocate a new region of memory and copy the data.
	 *
	 * @param __addr The previously allocated pointer to resize.
	 * @param __bytes The number of bytes the region should be.
	 * @throws IllegalArgumentException If the specified number of bytes is
	 * zero or negative.
	 * @throws MemoryAllocateException If the specified address was not
	 * previously returned by this allocator or if the region could not be
	 * increased or decreased in size.
	 * @since 2016/06/12
	 */
	public abstract void resize(long __addr, long __bytes)
		throws IllegalArgumentException, MemoryAllocateException;
}

