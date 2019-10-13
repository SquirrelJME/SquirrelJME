// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.task;

import cc.squirreljme.jvm.Allocator;

/**
 * This is an allocator which uses a pre-set tag value for any allocations.
 *
 * @since 2019/09/23
 */
public final class TaskAllocator
{
	/** The extra tag bits to use. */
	protected final int tagbits;
	
	/** The static field pointer. */
	private int _staticfieldptr;
	
	/**
	 * Initializes the tagged allocator.
	 *
	 * @param __pid The PID to allocate for.
	 * @since 2019/09/23
	 */
	public TaskAllocator(int __pid)
	{
		// The tag bits are just the PID shifted up a bit
		this.tagbits = __pid << 4;
	}
	
	/**
	 * Allocates memory for this tag.
	 *
	 * @param __tag The tag used.
	 * @param __sz The number of bytes to allocate.
	 * @return The allocated bytes.
	 * @since 2019/06/23
	 */
	public final int allocate(int __tag, int __sz)
	{
		// Just perform the allocation with our PID as part of the tag and
		// whatever was passed, masked correctly
		return Allocator.allocate(
			this.tagbits | (__tag & Allocator.CHUNK_BITS_VALUE_MASK), __sz);
	}
	
	/**
	 * Returns the static field pointer.
	 *
	 * @return The static field pointer.
	 * @since 2019/10/13
	 */
	public final int getStaticFieldPointer()
	{
		// If this has already been initialized then use it!
		int rv = this._staticfieldptr;
		if (rv != 0)
			return rv;
		
		throw new todo.TODO();
	}
}

