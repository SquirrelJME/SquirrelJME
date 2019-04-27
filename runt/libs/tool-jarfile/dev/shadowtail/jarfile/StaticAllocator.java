// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

/**
 * This class is used to assist in static allocation of data.
 *
 * @since 2019/04/24
 */
public final class StaticAllocator
{
	/** Start address. */
	protected final int base;
	
	/** The current size. */
	private int _size;
	
	/**
	 * Initializes the static allocator.
	 *
	 * @param __ba The allocation size.
	 * @since 2019/04/24
	 */
	public StaticAllocator(int __ba)
	{
		this.base = __ba;
	}
	
	/**
	 * Allocates the given number of bytes.
	 *
	 * @param __sz The number of bytes to allocate.
	 * @return The allocation pointer.
	 * @since 2019/04/24
	 */
	public final int allocate(int __sz)
	{
		// Round up
		__sz = (__sz + 3) & (~3);
		
		// Determine new size
		int oldsize = this._size;
		this._size = oldsize + __sz;
		
		// Debug
		todo.DEBUG.note("Allocate %d at @%08x", __sz, this.base + oldsize);
		
		// Return pointer
		return this.base + oldsize;
	}
	
	/**
	 * Returns the current size of the allocator.
	 *
	 * @return The allocation size.
	 * @since 2019/04/24
	 */
	public final int size()
	{
		return this._size;
	}
}

