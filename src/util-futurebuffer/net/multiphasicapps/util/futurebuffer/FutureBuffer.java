// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.futurebuffer;

import net.multiphasicapps.util.growbuffer.GrowBuffer;

/**
 * This class represents a buffer that can have some of its data defined in
 * the future (potentially when more information is known).
 *
 * @since 2016/09/30
 */
public class FutureBuffer
{
	/** The backing grow buffer. */
	protected final GrowBuffer buffer =
		new GrowBuffer();
	
	/**
	 * Returns the byte at the specified index.
	 *
	 * @param __i The index to get.
	 * @return The byte at this index.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * buffer bounds.
	 * @since 2016/09/30
	 */
	public byte get(int __i)
		throws IndexOutOfBoundsException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Reads multiple bytes from the buffer.
	 *
	 * @param __i The index to start reading values at.
	 * @param __b The buffer to read bytes into.
	 * @throws IndexOutOfBoundsException If the index is not within the buffer
	 * bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	public void get(int __i, byte[] __b)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
			
		// Forward
		get(__i, __b, 0, __b.length);
	}
	
	/**
	 * Reads multiple bytes from the buffer.
	 *
	 * @param __i The index to start reading values at.
	 * @param __b The buffer to read bytes into.
	 * @param __o The offset in the destination array to start writing values
	 * at.
	 * @param __l The number of bytes to read.
	 * @throws IndexOutOfBoundsException If the index is not within the buffer
	 * bounds, the length and/or offset are negative, or they exceed the bounds
	 * of the array.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	public void get(int __i, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int bn = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > bn)
			throw new IndexOutOfBoundsException("IOOB");
			
		throw new Error("TODO");
	}
	
	/**
	 * Returns the byte at the specified index without performing future
	 * modifications of the data.
	 *
	 * @param __i The index to get.
	 * @return The byte at this index.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * buffer bounds.
	 * @since 2016/09/30
	 */
	public byte getRaw(int __i)
		throws IndexOutOfBoundsException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Reads multiple bytes from the buffer without performing future
	 * modifications of the data.
	 *
	 * @param __i The index to start reading values at.
	 * @param __b The buffer to read bytes into.
	 * @throws IndexOutOfBoundsException If the index is not within the buffer
	 * bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	public void getRaw(int __i, byte[] __b)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
			
		// Forward
		getRaw(__i, __b, 0, __b.length);
	}
	
	/**
	 * Reads multiple bytes from the buffer without performing future
	 * modifications of the data.
	 *
	 * @param __i The index to start reading values at.
	 * @param __b The buffer to read bytes into.
	 * @param __o The offset in the destination array to start writing values
	 * at.
	 * @param __l The number of bytes to read.
	 * @throws IndexOutOfBoundsException If the index is not within the buffer
	 * bounds, the length and/or offset are negative, or they exceed the bounds
	 * of the array.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	public void getRaw(int __i, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int bn = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > bn)
			throw new IndexOutOfBoundsException("IOOB");
			
		throw new Error("TODO");
	}
	
	/**
	 * Places a byte at the specified index and returns the old byte.
	 *
	 * @param __i The index to place the byte at.
	 * @param __v The value to set.
	 * @return The value that was previously set at this position.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * buffer bounds.
	 * @since 2016/09/30
	 */
	public byte put(int __i, byte __v)
		throws IndexOutOfBoundsException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Writes multiple bytes into the buffer.
	 *
	 * @param __i The index to write values at.
	 * @param __b The input array to obtain values from.
	 * @throws IndexOutOfBoundsException If the index is not within the buffer
	 * bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	public void put(int __i, byte[] __b)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Forward
		put(__i, __b, 0, __b.length);
	}
	
	/**
	 * Writes multiple bytes into the buffer.
	 *
	 * @param __i The index to write values at.
	 * @param __b The input array to obtain values from.
	 * @param __o The offset in the array to start reading values from.
	 * @param __l The number of bytes to write.
	 * @throws IndexOutOfBoundsException If the index is not within the buffer
	 * bounds, the length and/or offset are negative, or they exceed the bounds
	 * of the array.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	public void put(int __i, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int bn = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > bn)
			throw new IndexOutOfBoundsException("IOOB");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the current size of the buffer.
	 *
	 * @return The buffer size.
	 * @since 2016/09/30
	 */
	public int size()
	{
		throw new Error("TODO");
	}
}

