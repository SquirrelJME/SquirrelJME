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
import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.Constants;

/**
 * This is an allocator which uses a pre-set tag value for any allocations.
 *
 * @since 2019/09/23
 */
public final class TaskAllocator
{
	/** The size of the static field area. */
	public static final short STATIC_FIELD_SIZE =
		8192;
	
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
		int rv = Allocator.allocate(
			this.tagbits | (__tag & Allocator.CHUNK_BITS_VALUE_MASK), __sz);
		
		// Ran out of memory?
		if (rv == 0)
			throw new TaskOutOfMemoryError();
		
		return rv;
	}
	
	/**
	 * Allocates an integer sized array with the given values, no class type
	 * is set.
	 *
	 * @param __v The values to store.
	 * @return The pointer to the allocated array.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final int allocateArrayInt(int... __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Initialize base array
		int count = __v.length;
		int rv = this.allocateArrayIntEmpty(count);
		
		// Copy pointer values to the array
		int bp = rv + Constants.ARRAY_BASE_SIZE;
		for (int i = 0, wp = 0; i < count; i++, wp += 4)
			Assembly.memWriteInt(bp, wp, __v[i]);
		
		// Return the result of it
		return rv;
	}
	
	/**
	 * Allocates an integer sized array with the given values.
	 *
	 * @param __cl The class to set it as.
	 * @param __v The values to store.
	 * @return The pointer to the allocated array.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final int allocateArrayInt(TaskClass __cl, int... __v)
		throws NullPointerException
	{
		if (__cl == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Allocate using the base form
		int rv = this.allocateArrayInt(__v);
		
		// Store object type
		Assembly.memWriteInt(rv, Constants.OBJECT_CLASS_OFFSET,
			__cl.infoPointer());
		
		// Use this
		return rv;
	}
	
	/**
	 * Allocates an integer sized array with the given values, no class type
	 * is set here.
	 *
	 * @param __n The number of elements in the array.
	 * @return The pointer to the allocated array.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final int allocateArrayIntEmpty(int __n)
		throws NullPointerException
	{
		// Allocate array pointer
		int rv = this.allocateObject(Constants.ARRAY_BASE_SIZE + (__n * 4));
		
		// Write array size
		Assembly.memWriteInt(rv, Constants.ARRAY_LENGTH_OFFSET, __n);
		
		// Use this
		return rv;
	}
	
	/**
	 * Allocates an integer sized array with the given values.
	 *
	 * @param __cl The class to set it as.
	 * @param __n The number of elements in the array.
	 * @return The pointer to the allocated array.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final int allocateArrayIntEmpty(TaskClass __cl, int __n)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Allocate array pointer
		int rv = this.allocateObject(__cl,
			Constants.ARRAY_BASE_SIZE + (__n * 4));
		
		// Write array size
		Assembly.memWriteInt(rv, Constants.ARRAY_LENGTH_OFFSET, __n);
		
		// Use this
		return rv;
	}
	
	/**
	 * Allocates an object type.
	 *
	 * @param __sz The size of the object.
	 * @return The allocated bytes.
	 * @throws IllegalArgumentException If the allocation size is smaller
	 * than the object base size.
	 * @since 2019/10/26
	 */
	public final int allocateObject(int __sz)
		throws IllegalArgumentException
	{
		// {@squirreljme.error SV0x Object allocation is less than the object
		// base size.}
		if (__sz < Constants.OBJECT_BASE_SIZE)
			throw new IllegalArgumentException("SV0x");
		
		// Allocate
		int rv = this.allocate(Allocator.CHUNK_BIT_IS_OBJECT, __sz);
		
		// Set initial count to one, to match new
		Assembly.memWriteInt(rv, Constants.OBJECT_COUNT_OFFSET,
			1);
		
		return rv;
	}
	
	/**
	 * Allocates an object type.
	 *
	 * @param __cl The class type.
	 * @param __sz The size of the object.
	 * @return The allocated bytes.
	 * @since 2019/12/01
	 */
	public final int allocateObject(TaskClass __cl, int __sz)
	{
		int rv = this.allocateObject(__sz);
		
		// Store class type here
		Assembly.memWriteInt(rv, Constants.OBJECT_CLASS_OFFSET,
			__cl.infoPointer());
		
		return rv;
	}
	
	/**
	 * Allocates a region that can fit a constant pool with the given number
	 * of entries.
	 *
	 * @param __n The number of entries to use.
	 * @return The pointer to the allocation.
	 * @since 2019/11/25
	 */
	public final int allocatePool(int __n)
	{
		return this.allocate(Allocator.CHUNK_BIT_IS_POOL, 4 * __n);
	}
	
	/**
	 * Frees the given pointer, this is just a helper method.
	 *
	 * @param __p The pointer to free.
	 * @since 2019/10/19
	 */
	public final void free(int __p)
	{
		Allocator.free(__p);
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
		
		// We need to allocate this data region
		synchronized (this)
		{
			// Double-get in case we ran into this twice!
			rv = this._staticfieldptr;
			if (rv != 0)
				return rv;
			
			// Allocate and store this space
			this._staticfieldptr = (rv = this.allocate(0, STATIC_FIELD_SIZE));
			
			// Could not allocate for this task!
			if (rv == 0)
				throw new TaskOutOfMemoryError();
			
			// And use it
			return rv;
		}
	}
}

