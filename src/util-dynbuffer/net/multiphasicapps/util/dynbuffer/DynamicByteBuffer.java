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

/**
 * This class provides a dynamically sized array of bytes for efficient
 * insertion and removal of bytes in the middle of the entire virtual buffer.
 *
 * This class must be thread safe.
 *
 * {@squirreljme.error AD04 The input chunk size is negative. (The negative
 * chunk size)}
 *
 * @since 2016/03/22
 */
public class DynamicByteBuffer
{
	/** The default individual chunk size. */
	public static final int DEFAULT_CHUNK_SIZE =
		32;
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The default chunk size to use. */
	protected final int chunksize;
	
	/**
	 * Initializes the code chunks using the default chunk size.
	 *
	 * @since 2016/03/22
	 */
	public DynamicByteBuffer()
	{
		this(DEFAULT_CHUNK_SIZE);
	}
	
	/**
	 * Initializes the code chunks with the given chunk size.
	 *
	 * @param __cs The chunk size to use for chunks.
	 * @throws IllegalArgumentException If the chunk size is zero or negative.
	 * @since 2016/03/22
	 */
	public DynamicByteBuffer(int __cs)
		throws IllegalArgumentException
	{
		// Check
		if (__cs <= 0)
			throw new IllegalArgumentException(String.format("AD04 %d", __cs));
		
		// Set
		chunksize = __cs;
		
		// Setup initial chunk
		clear();
	}
	
	/**
	 * Initializes a new code chunk buffer from the given buffer.
	 *
	 * @param __from The data to copy data from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/22
	 */
	public DynamicByteBuffer(DynamicByteBuffer __from)
		throws NullPointerException
	{
		this(DEFAULT_CHUNK_SIZE, __from);
	}
	
	/**
	 * Initializes a new code chunk buffer from the given buffer.
	 *
	 * @param __cs The chunk size to use for chunks.
	 * @param __from The data to copy data from.
	 * @throws IllegalArgumentException If the chunk size is zero or negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/22
	 */
	public DynamicByteBuffer(int __cs, DynamicByteBuffer __from)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__from == null)
			throw new NullPointerException("NARG");
		if (__cs <= 0)
			throw new IllegalArgumentException(String.format("AD04 %d", __cs));
		
		// Set
		chunksize = __cs;
		
		// Setup initial chunk
		clear();
		
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
	 * Returns the actual code chunk size.
	 *
	 * @return The actual bytes used to hold the code chunk data.
	 * @since 2016/03/22
	 */
	public int actualSize()
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Adds a byte to the end of the chunk.
	 *
	 * @param __v The value to add.
	 * @since 2016/03/22
	 */
	public void add(byte __v)
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Bulk appending of bytes to the end of the chunk.
	 *
	 * @param __src The source byte array.
	 * @param __o The base offset in the source array.
	 * @param __l The number of bytes to write.
	 * @throws IndexOutOfBoundsException If the input array range is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/08
	 */
	public void add(byte[] __src, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Inserts a byte at the given position which moves all of the bytes
	 * following it forward.
	 *
	 * @param __i The index to read the byte for.
	 * @param __v The value to add.
	 * @throws IndexOutOfBoundsException If the address is not within the
	 * chunk bounds.
	 * @since 2016/03/22
	 */
	public void add(int __i, byte __v)
		throws IndexOutOfBoundsException
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
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
	public void add(int __base, byte[] __src, int __o, int __l)
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
	 * Clears the code chunk and removes all bytes from it and sets the size
	 * to zero.
	 *
	 * @since 2016/03/22
	 */
	public void clear()
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Gets the byte at the given index.
	 *
	 * @param __i The index to read the byte for.
	 * @return The value of the byte at the given position.
	 * @throws IndexOutOfBoundsException If the address is not within the
	 * chunk bounds.
	 * @since 2016/03/22
	 */
	public byte get(int __i)
		throws IndexOutOfBoundsException
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Obtains multiple bytes from this chunked byte buffer and places them
	 * into the given destination byte array.
	 *
	 * @param __base The base index to read from.
	 * @param __dest The destination buffer.
	 * @throws IndexOutOfBoundsException If the base and the length exceeds the
	 * buffer region.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/19
	 */
	public void get(int __base, byte[] __dest)
		throws NullPointerException
	{
		get(__base, __dest, 0, __dest.length);
	}
	
	/**
	 * Obtains multiple bytes from this chunked byte buffer and places them
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
	public void get(int __base, byte[] __dest, int __o, int __l)
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
	 * Performs a quick compaction of all chunks to potentially reduce wasted
	 * allocation space.
	 *
	 * @return {@code this}.
	 * @since 2016/03/22
	 */
	public DynamicByteBuffer quickCompact()
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
	 * chunk bounds.
	 * @since 2016/03/22
	 */
	public byte remove(int __i)
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
	public int remove(int __i, byte[] __b)
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
	public int remove(int __i, byte[] __b, int __o, int __l)
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
	 * chunk bounds.
	 * @since 2016/03/22
	 */
	public byte set(int __i, byte __v)
		throws IndexOutOfBoundsException
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Returns the size of all the bytes in the chunk.
	 *
	 * @return The total byte count.
	 * @since 2016/03/22
	 */
	public int size()
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
	public String toString()
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
}

