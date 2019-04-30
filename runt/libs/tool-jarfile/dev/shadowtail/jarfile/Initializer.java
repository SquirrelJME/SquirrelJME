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
 * This is used to build the initialization sequence accordingly. It is used
 * determine the initial amount of memory needed along with all the various
 * actions which need to be performed at this point.
 *
 * The initializer starts with a memory sequence chunk which could later be
 * freed when it is no longer needed potentially.
 *
 * @since 2019/04/30
 */
public final class Initializer
{
	/** Current size of the initializer, includes mem link for freeing. */
	private int _size =
		8;
	
	/**
	 * Allocates memory in the initialization sequence.
	 *
	 * @param __sz The number of bytes to allocate.
	 * @return The pointer address of the allocation.
	 * @since 2019/04/30
	 */
	public final int allocate(int __sz)
	{
		// Round allocation to 4-bytes
		__sz = (__sz + 3) & (~3);
		
		throw new todo.TODO();
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteByte(int __addr, int __v)
	{
		this.memWriteByte(null, __addr, __v);
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __m The modifier to use when writing.
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteByte(Modifier __m, int __addr, int __v)
	{
		// Default modifier
		if (__m == null)
			__m = Modifier.NONE;
		
		throw new todo.TODO();
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteInt(int __addr, int __v)
	{
		this.memWriteInt(null, __addr, __v);
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __m The modifier to use when writing.
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteInt(Modifier __m, int __addr, int __v)
	{
		// Default modifier
		if (__m == null)
			__m = Modifier.NONE;
		
		throw new todo.TODO();
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteShort(int __addr, int __v)
	{
		this.memWriteShort(null, __addr, __v);
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __m The modifier to use when writing.
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteShort(Modifier __m, int __addr, int __v)
	{
		// Default modifier
		if (__m == null)
			__m = Modifier.NONE;
		
		throw new todo.TODO();
	}
	
	/**
	 * Converts and builds the initializer sequence.
	 *
	 * @return The byte array representing the sequence.
	 * @since 2019/04/30
	 */
	public final byte[] toByteArray()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Represents the action modifier that is available.
	 *
	 * @since 2019/04/30
	 */
	public static enum Modifier
	{
		/** No modification to be done. */
		NONE,
		
		/** Offset by RAM address. */
		RAM_OFFSET,
		
		/** Offset by JAR address. */
		JAR_OFFSET,
		
		/** End. */
		;
	}
}

