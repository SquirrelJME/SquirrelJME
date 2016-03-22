// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf;

/**
 * This class provides a dynamically sized array of bytes for efficient
 * insertion and removal of bytes in the middle of the entire virtual buffer.
 *
 * This class must be thread safe.
 *
 * @since 2016/03/22
 */
public class NARFCodeChunks
{
	/** The default individual chunk size. */
	public static final int DEFAULT_CHUNK_SIZE =
		32;
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The chunk size to use. */
	protected final int chunksize;
	
	/**
	 * Initializes the code chunks using the default chunk size.
	 *
	 * @since 2016/03/22
	 */
	public NARFCodeChunks()
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
	public NARFCodeChunks(int __cs)
		throws IllegalArgumentException
	{
		// Check
		if (__cs <= 0)
			throw new IllegalArgumentException(String.format("NF04 %d", __cs));
		
		// Set
		chunksize = __cs;
	}
	
	/**
	 * Adds a byte to the end of the chunk.
	 *
	 * @param __v The value to add.
	 * @return {@code this}.
	 * @since 2016/03/22
	 */
	public NARFCodeChunks add(byte __v)
	{
		// Lock
		synchronized (lock)
		{
			return add(size(), __v);
		}
	}
	
	/**
	 * Inserts a byte at the given position which moves all of the bytes
	 * following it forward.
	 *
	 * @param __i The index to read the byte for.
	 * @param __v The value to add.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the address is not within the
	 * chunk bounds.
	 * @since 2016/03/22
	 */
	public NARFCodeChunks add(int __i, byte __v)
		throws IndexOutOfBoundsException
	{
		// Lock
		synchronized (lock)
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
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Sets the byte at the given position.
	 *
	 * @param __i The index to write the byte at.
	 * @param __v The byte to write.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the address is not within the
	 * chunk bounds.
	 * @since 2016/03/22
	 */
	public NARFCodeChunks set(int __i, byte __v)
		throws IndexOutOfBoundsException
	{
		// Lock
		synchronized (lock)
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
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * This represents a single chunk within the group of chunks.
	 *
	 * This class must be thread safe.
	 *
	 * @since 2016/03/22
	 */
	private final class __Chunk__
	{
		/** Chunk data. */
		protected final byte[] data =
			new byte[chunksize];
		
		/** Head position. */
		private volatile int _head;
		
		/** Tail position. */
		private volatile int _tail;
		
		/**
		 * Initializes the chunk.
		 *
		 * @since 2016/03/22
		 */
		private __Chunk__()
		{
		}
	}
}

