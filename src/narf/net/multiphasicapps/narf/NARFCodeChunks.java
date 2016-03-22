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
	
	/** Internal chunks. */
	private volatile __Chunk__[] _chunks;
	
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
		
		// Setup initial chunk
		_chunks = new __Chunk__[1];
		_chunks[0] = new __Chunk__();
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
			// Get chunk for the given position
			__Chunk__ cur = chunkForPos(__i);
			
			// Add byte
			cur.add(__i, __v);
		}
		
		// Self
		return this;
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
			// Get chunk for the given position
			__Chunk__ cur = chunkForPos(__i);
			
			// Set byte
			cur.set(__i, __v);
		}
		
		// Self
		return this;
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
			// Get all the chunks;
			__Chunk__ cx[] = _chunks;
			
			// Get the last one, since that will determine the total number
			// of bytes used
			__Chunk__ last = cx[cx.length - 1];
			
			// The size is quite simple
			return last._position + last.length();
		}
	}
	
	/**
	 * Returns the chunk for the given position.
	 *
	 * @param __i The position to get the chunk for.
	 * @return The chunk at the given position.
	 * @throws IndexOutOfBoundsException If the position is outside of the
	 * chunk table.
	 * @since 2016/03/22
	 */
	private __Chunk__ chunkForPos(int __i)
		throws IndexOutOfBoundsException
	{
		// Must be within bounds
		if (__i < 0)
			throw new IndexOutOfBoundsException(String.format("NF05 %d", __i));
		
		// Lock
		synchronized (lock)
		{
			// Get all the chunks;
			__Chunk__ cx[] = _chunks;
			int n = cx.length;
			
			// If requesting the last byte, then return the last chunk because
			// it would be written at the end
			int sz = size();
			if (__i == sz)
				return cx[n - 1];
			
			// Out of bounds otherwise
			else if (__i > sz)
				throw new IndexOutOfBoundsException(String.format("NF06 %d %d",
					__i, sz));
			
			// Go through the chunks, starting in the middle to find the
			// correct one. A binary tree sort of way is taken to the searching
			for (int cl = 0, cc = n >>> 1, ch = n;;)
			{
				// Get the current chunk
				__Chunk__ maybe = cx[cc];
				
				// If within bounds, use it
				if (maybe.containsPosition(__i))
					return maybe;
				
				// Get chunk position
				int cp = maybe._position;
				
				// If the wanted address is lower, try a lower chunk
				if (__i < cp)
				{
					// End cap at current postion
					ch = cc;
					
					// Current position drops to half
					cc = cl + ((cc - cl) >>> 1);
				}
				
				// Otherwise try a higher chunk
				else
				{
					// Lower cap uses current position
					cl = cc;
					
					// Current gains half
					cc = cc + ((ch - cc) >>> 1);
				}
			}
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
		
		/** The number of bytes used in the chunk. */
		private volatile int _count;
		
		/** The position this chunk starts at */
		private volatile int _position;
		
		/** The chunk index in the master table. */
		private volatile int _index;
		
		/**
		 * Initializes the chunk.
		 *
		 * @since 2016/03/22
		 */
		private __Chunk__()
		{
		}
		
		/**
		 * Adds a byte to the given chunk, all bytes following are moved over.
		 *
		 * @param __i The real position to add to.
		 * @param __v The The value to write.
		 * @return {@code this}.
		 * @throws IndexOutOfBoundsException If the computed logical position
		 * does not belong in this chunk.
		 * @since 2016/03/22
		 */
		public __Chunk__ add(int __i, byte __v)
			throws IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				// Obtain the logical add position
				int logpos = __i - _position;
				int len = _count;
				if (logpos < 0 || logpos > len)
					throw new IndexOutOfBoundsException(
						String.format("NF07 %d %d %d", __i, logpos, len));
				
				// Get data buffer
				byte[] ddx = data;
				
				// At the end of the chunk
				if (len == chunksize)
				{
					// Writing at the end of the chunk, make a new chunk
					// following this and place the data in that instead
					if (logpos == len)
						throw new Error("TODO");
					
					// Writing in the middle, split it instead down the middle
					// because more bytes could be added between the two.
					else
						throw new Error("TODO");
				}
				
				// Move byte over to make room for this byte, if possible
				for (int i = len - 1; i > logpos; i--)
					ddx[i] = ddx[i - 1];
				
				// Write byte here
				ddx[logpos] = __v;
				
				// More bytes are available
				_count = len + 1;
			}
			
			// Self
			return this;
		}
		
		/**
		 * Is the given position within this chunk?
		 *
		 * @param __p The position to check if this chunk contains a byte for
		 * it.
		 * @return {@code true} if the given position is contained within this
		 * chunk.
		 * @since 2016/03/22
		 */
		public boolean containsPosition(int __p)
		{
			// Lock
			synchronized (lock)
			{
				int pos = _position;
				return (__p >= pos && __p < pos + length());
			}
		}
		
		/**
		 * Returns the length of this chunk.
		 *
		 * @return The chunk length.
		 * @since 2016/03/22
		 */
		public int length()
		{
			// Lock
			synchronized (lock)
			{
				return _count;
			}
		}
		
		/**
		 * Sets a byte within the buffer to a specific value.
		 *
		 * @param __i The real position to set.
		 * @param __v The The value to write.
		 * @return {@code this}.
		 * @throws IndexOutOfBoundsException If the computed logical position
		 * does not belong in this chunk.
		 * @since 2016/03/22
		 */
		public __Chunk__ set(int __i, byte __v)
			throws IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				// Obtain the logical add position
				int logpos = __i - _position;
				int len = _count;
				if (logpos < 0 || logpos > len)
					throw new IndexOutOfBoundsException(
						String.format("NF07 %d %d %d", __i, logpos, len));
				
				// Get data buffer
				byte[] ddx = data;
				
				// Write byte here
				ddx[logpos] = __v;
			}
			
			// Self
			return this;
		}
	}
}

