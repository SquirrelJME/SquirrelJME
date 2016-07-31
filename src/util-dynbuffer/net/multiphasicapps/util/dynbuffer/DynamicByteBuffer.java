// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.dynbuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a dynamically sized array of bytes for efficient
 * insertion and removal of bytes in the middle of the entire virtual buffer.
 *
 * Internally the class partitions area of the buffer into chunks. Data within
 * the chunks is then partitioned so that when data is added or removed no
 * other data must be moved around.
 *
 * This class must be thread safe.
 *
 * @since 2016/03/22
 */
public class DynamicByteBuffer
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The chunks that are within this buffer. */
	private final List<__Chunk__> _chunks =
		new ArrayList<>();
	
	/**
	 * Initializes the byte buffers which does not copy from another buffer.
	 *
	 * @since 2016/03/22
	 */
	public DynamicByteBuffer()
	{
	}
	
	/**
	 * Initializes a new buffer buffer from the given buffer.
	 *
	 * @param __from The data to copy data from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/22
	 */
	public DynamicByteBuffer(DynamicByteBuffer __from)
		throws NullPointerException
	{
		// Check
		if (__from == null)
			throw new NullPointerException("NARG");
		
		// Lock the other and copy the data
		synchronized (__from.lock)
		{
			// Long copy operation
			int fl = __from.size();
			for (int i = 0; i < fl; i++)
				add(i, __from.get(i));
		}
	}
	
	/**
	 * Initializes a new dynamic byte buffer using data from the given array.
	 *
	 * @param __from The array to copy data from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/31
	 */
	public DynamicByteBuffer(byte... __from)
		throws NullPointerException
	{
		add(0, __from);
	}
	
	/**
	 * Returns the actual byte buffer size.
	 *
	 * @return The actual bytes used to hold the byte buffer data.
	 * @since 2016/03/22
	 */
	public final int actualSize()
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Adds a byte to the end of the buffer.
	 *
	 * @param __v The value to add.
	 * @since 2016/03/22
	 */
	public final void add(byte __v)
	{
		this.add(size(), __v);
	}
	
	/**
	 * Bulk adding of bytes to the end of the buffer.
	 *
	 * @param __src The bytes to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/31
	 */
	public final void add(byte[] __src)
		throws NullPointerException
	{
		this.add(size(), __src, 0, __src.length);
	}
	
	/**
	 * Bulk appending of bytes to the end of the buffer.
	 *
	 * @param __src The source byte array.
	 * @param __o The base offset in the source array.
	 * @param __l The number of bytes to write.
	 * @throws IndexOutOfBoundsException If the input array range is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/08
	 */
	public final void add(byte[] __src, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		this.add(size(), __src, __o, __l);
	}
	
	/**
	 * Inserts a byte at the given position which moves all of the bytes
	 * following it forward.
	 *
	 * @param __i The index to read the byte for.
	 * @param __v The value to add.
	 * @throws IndexOutOfBoundsException If the address is not within the
	 * buffer bounds.
	 * @since 2016/03/22
	 */
	public final void add(int __i, byte __v)
		throws IndexOutOfBoundsException
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Adds multiple bytes to the specified position in the given chunk.
	 *
	 * @param __base The address where bytes are to be written.
	 * @param __src The source buffer.
	 * @since 2016/07/31
	 */
	public final void add(int __base, byte[] __src)
	{
		this.add(__base, __src, 0, __src.length);
	}
	
	/**
	 * Bulk appending of bytes at a specific location.
	 *
	 * @param __base The base location to start writing at.
	 * @param __src The source byte array.
	 * @param __o The base offset in the source array.
	 * @param __l The number of bytes to write.
	 * @throws IndexOutOfBoundsException If the input array range is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/08
	 */
	public final void add(int __base, byte[] __src, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__src == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __src.length)
			throw new IndexOutOfBoundsException(String.format("BAOB %d %d %d",
				__src.length, __o, __l));
		
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Clears the byte buffer and removes all bytes from it and sets the size
	 * to zero.
	 *
	 * @since 2016/03/22
	 */
	public final void clear()
	{
		// Lock
		synchronized (this.lock)
		{
			this._chunks.clear();
		}
	}
	
	/**
	 * Gets the byte at the given index.
	 *
	 * @param __i The index to read the byte for.
	 * @return The value of the byte at the given position.
	 * @throws IndexOutOfBoundsException If the address is not within the
	 * buffer bounds.
	 * @since 2016/03/22
	 */
	public final byte get(int __i)
		throws IndexOutOfBoundsException
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Obtains multiple bytes from this buffered byte buffer and places them
	 * into the given destination byte array.
	 *
	 * @param __base The base index to read from.
	 * @param __dest The destination buffer.
	 * @throws IndexOutOfBoundsException If the base and the length exceeds the
	 * buffer region.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/19
	 */
	public final void get(int __base, byte[] __dest)
		throws NullPointerException
	{
		get(__base, __dest, 0, __dest.length);
	}
	
	/**
	 * Obtains multiple bytes from this buffered byte buffer and places them
	 * into the given destination byte array.
	 *
	 * @param __base The base index to start a read from.
	 * @param __dest The destination buffer.
	 * @param __o The offset into the target buffer.
	 * @param __l The length of the target buffer.
	 * @throws IndexOutOfBoundsException If the base and the length exceeds the
	 * buffer region, or the array offsets and lengths are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/08
	 */
	public final void get(int __base, byte[] __dest, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__dest == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __dest.length)
			throw new IndexOutOfBoundsException(String.format("BAOB %d %d %d",
				__dest.length, __o, __l));
		
		// Lock
		synchronized (this.lock)
		{
			// Source exceeds bounds?
			int limit = size();
			if (__base < 0 || (__base + __l) > limit)
				throw new IndexOutOfBoundsException(String.format(
					"IOOB %d %d %d", __base, __l, limit));
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * Performs a quick compaction of all buffers to potentially reduce wasted
	 * allocation space.
	 *
	 * @return {@code this}.
	 * @since 2016/03/22
	 */
	public final DynamicByteBuffer quickCompact()
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Removes a byte at the given index.
	 *
	 * @param __i The index which contains the byte to remove.
	 * @return The value which was at this position.
	 * @throws IndexOutOfBoundsException If the address is not within the
	 * buffer bounds.
	 * @since 2016/03/22
	 */
	public final byte remove(int __i)
		throws IndexOutOfBoundsException
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Removes multiple bytes from the given position
	 *
	 * @param __i The base index to get bytes from.
	 * @param __b The destination array where read bytes are placed.
	 * @return The number of removed bytes.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public final int remove(int __i, byte[] __b)
		throws IndexOutOfBoundsException, NullPointerException
	{
		return remove(__i, __b, 0, __b.length);
	}
	
	/**
	 * Removes multiple bytes at the given position.
	 *
	 * @param __i The base index to get bytes from.
	 * @param __b The destination array where read bytes are placed.
	 * @param __o The offset to start writing at.
	 * @param __l The number of bytes to read.
	 * @return The number of removed bytes.
	 * @throws IndexOutOfBoundsException If the index is not within bounds or
	 * the offset or length are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public final int remove(int __i, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		if (__i < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Sets the byte at the given position.
	 *
	 * @param __i The index to write the byte at.
	 * @param __v The byte to write.
	 * @return The old value.
	 * @throws IndexOutOfBoundsException If the address is not within the
	 * buffer bounds.
	 * @since 2016/03/22
	 */
	public final byte set(int __i, byte __v)
		throws IndexOutOfBoundsException
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Returns the size of all the bytes in the buffer.
	 *
	 * @return The total byte count.
	 * @since 2016/03/22
	 */
	public final int size()
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/22
	 */
	@Override
	public final String toString()
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Locates the chunk that contains the specified position for a get
	 * operation.
	 *
	 * @param __pos The position to get the chunk for.
	 * @return The chunk for the given position.
	 * @throws IndexOutOfBoundsException If the position is not within the
	 * buffer bounds.
	 * @since 2016/07/31
	 */
	private __Chunk__ __locateForGet(int __pos)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error AD01 Could not locate the chunk for the given
		// position. (The requested position)}
		int size = this.size();
		if (__pos < 0 || __pos >= size)
			throw new IndexOutOfBoundsException(String.format("AD01 %d",
				__pos));
		
		throw new Error("TODO");
	}
}

