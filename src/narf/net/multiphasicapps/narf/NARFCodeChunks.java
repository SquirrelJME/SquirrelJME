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
		clear();
	}
	
	/**
	 * Initializes a new code chunk buffer from the given buffer.
	 *
	 * @param __from The data to copy data from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/22
	 */
	public NARFCodeChunks(NARFCodeChunks __from)
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
	public NARFCodeChunks(int __cs, NARFCodeChunks __from)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__from == null)
			throw new NullPointerException("NARG");
		if (__cs <= 0)
			throw new IllegalArgumentException(String.format("NF04 %d", __cs));
		
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
		synchronized (lock)
		{
			return (int)Math.min(Integer.MAX_VALUE,
				(long)chunksize * (long)_chunks.length);
		}
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
			__chunkForPos(__i).add(__i, __v);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Clears the code chunk and removes all bytes from it and sets the size
	 * to zero.
	 *
	 * @return {@code this}.
	 * @since 2016/03/22
	 */
	public NARFCodeChunks clear()
	{
		// Lock
		synchronized (lock)
		{
			// Just initialize new chunk buffers
			__Chunk__[] ch;
			_chunks = ch = new __Chunk__[1];
			ch[0] = new __Chunk__();
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
			return __chunkForPos(__i).get(__i);
		}
	}
	
	/**
	 * Performs a quick compaction of all chunks to potentially reduce wasted
	 * allocation space.
	 *
	 * @return {@code this}.
	 * @since 2016/03/22
	 */
	public NARFCodeChunks quickCompact()
	{
		// Lock
		synchronized (lock)
		{
			int oldlen = _chunks.length;
			for (int i = 0; i < _chunks.length;)
			{
				// Perform compaction
				_chunks[i].__compact();
				
				// If it did not happen, then increment
				int newlen = _chunks.length;
				if (oldlen == newlen)
					i++;
				else
					oldlen = newlen;
			}
		}
		
		// Self
		return this;
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
		synchronized (lock)
		{
			return __chunkForPos(__i).remove(__i);
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
		synchronized (lock)
		{
			return __chunkForPos(__i).set(__i, __v);
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
	private __Chunk__ __chunkForPos(int __i)
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
					// Insert new chunk following this one.
					__Chunk__ newbie = __insertAfter();
					
					// Writing at the end of the chunk, make a new chunk
					// following this and place the data in that instead
					if (logpos == len)
					{
						// Only a single byte here
						newbie._count = 1;
						
						// And it has that specific value too
						newbie.data[0] = __v;
					}
					
					// Writing in the middle, split it instead down the middle
					// because more bytes could be added between the two.
					else
					{
						// New length is half the chunk length
						int nxlen = chunksize >>> 1;
						
						// Direct buffer copy to theirs
						byte[] rembuf = newbie.data;
						for (int i = 0; i < nxlen; i++)
							rembuf[i] = ddx[i + nxlen];
						
						// Writing on our side?
						byte[] splice;
						int spliceat;
						if (logpos < nxlen)
						{
							splice = ddx;
							spliceat = logpos;
							
							// Corrected lengths
							newbie._count = nxlen;
							_count = nxlen + 1;
						}
						
						// Writing on the other side
						else
						{
							splice = rembuf;
							spliceat = logpos - nxlen;
							
							// Corrected lengths
							newbie._count = nxlen + 1;
							_count = nxlen;
						}
						
						// Move byte over to make room for this byte
						for (int i = nxlen - 1; i >= spliceat; i--)
							splice[i + 1] = splice[i];
			
						// Write byte here
						splice[spliceat] = __v;
					}
				}
				
				// No need to splice chunks
				else
				{
					// Move byte over to make room for this byte
					for (int i = len - 1; i >= logpos; i--)
						ddx[i + 1] = ddx[i];
				
					// Write byte here
					ddx[logpos] = __v;
					
					// More bytes are available
					_count = len + 1;
				}
				
				// Correct positions and lengths
				__correct();
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
		 * Gets the byte at the given position.
		 *
		 * @param __i The real position to set.
		 * @param __v The The value to write.
		 * @return {@code this}.
		 * @throws IndexOutOfBoundsException If the computed logical position
		 * does not belong in this chunk.
		 * @since 2016/03/22
		 */
		public byte get(int __i)
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
				
				// Read byte here
				return data[logpos];
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
		 * Remove a byte within the buffer and returns it.
		 *
		 * @param __i The real position to set.
		 * @param __v The The value to write.
		 * @return {@code this}.
		 * @throws IndexOutOfBoundsException If the computed logical position
		 * does not belong in this chunk.
		 * @since 2016/03/22
		 */
		public byte remove(int __i)
			throws IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				// Obtain the logical add position
				int logpos = __i - _position;
				int len = _count;
				if (logpos < 0 || logpos >= len)
					throw new IndexOutOfBoundsException(
						String.format("NF07 %d %d %d", __i, logpos, len));
				
				// Get data buffer
				byte[] ddx = data;
				
				// The value to return
				byte rv = ddx[logpos];
				
				// Move bytes down
				for (int i = logpos + 1; i < len; i++)
					ddx[i - 1] = ddx[i];
				
				// Set new size
				_count = len - 1;
				
				// Potentially compact chunks
				__compact();
				
				// Correct
				__correct();
				
				// Return it
				return rv;
			}
		}
		
		/**
		 * Sets a byte within the buffer to a specific value.
		 *
		 * @param __i The real position to set.
		 * @param __v The The value to write.
		 * @return The old value.
		 * @throws IndexOutOfBoundsException If the computed logical position
		 * does not belong in this chunk.
		 * @since 2016/03/22
		 */
		public byte set(int __i, byte __v)
			throws IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				// Obtain the logical add position
				int logpos = __i - _position;
				int len = _count;
				if (logpos < 0 || logpos >= len)
					throw new IndexOutOfBoundsException(
						String.format("NF07 %d %d %d", __i, logpos, len));
				
				// Get data buffer
				byte[] ddx = data;
				
				// Write byte here and return the old value
				byte rv = ddx[logpos];
				ddx[logpos] = __v;
				return rv;
			}
		}
		
		/**
		 * Removes this chunk from the chunk list.
		 *
		 * @return {@code this}.
		 * @since 2016/03/22
		 */
		private __Chunk__ __bye()
		{
			// Lock
			synchronized (lock)
			{
				// Get the chunk list
				__Chunk__[] was = _chunks;
				int wasl = was.length;
				
				// If this is the only chunk, ignore
				if (wasl <= 1)
					return this;
				
				// Allocate new array
				int nowl = wasl - 1;
				__Chunk__[] now = new __Chunk__[nowl];
				
				// Copy all the chunks before this one
				int mydx = _index;
				for (int i = 0; i < mydx; i++)
					now[i] = was[i];
				
				// Copy all chunks following this, with new positional data
				for (int i = mydx + 1; i < wasl; i++)
				{
					// Move over
					__Chunk__ shove;
					now[i - 1] = shove = was[i];
					
					// Increase its index
					shove._index = i - 1;
				}
				
				// Use new array
				_chunks = now;
			}
			
			// Self
			return this;
		}
		
		/**
		 * Compacts this chunk into nearby chunks.
		 *
		 * @return {@code this}.
		 * @since 2016/03/22
		 */
		private __Chunk__ __compact()
		{
			// Lock
			synchronized (lock)
			{
				for (;;)
				{
					// Get the chunk table
					int mydx = _index;
					__Chunk__[] was = _chunks;
					int wasl = was.length;
				
					// This is the last chunk? Ignore
					if (mydx == wasl - 1)
						return this;
					
					// Get the next chunk and its details
					__Chunk__ next = was[mydx + 1];
					int nextc = next._count;
					
					// This and the next is small enough to fit in a chunk?
					int myc = _count;
					if ((myc + nextc) > chunksize)
						return this;
					
					// Add those bits to ours
					byte[] ddx = data;
					byte[] oox = next.data;
					for (int i = 0; i < nextc; i++)
						ddx[myc + i] = oox[i];
					
					// Set new size of this chunk
					_count = myc + nextc;
					
					// Remove the next chunk from the table
					int nowl = wasl - 1;
					__Chunk__[] now = new __Chunk__[nowl];
					
					// Copy all the chunks until this one
					for (int i = 0; i <= mydx; i++)
						now[i] = was[i];
					
					// Copy all chunks following this, with new positional data
					for (int i = mydx + 2; i < wasl; i++)
					{
						// Move over
						__Chunk__ shove;
						now[i - 1] = shove = was[i];
					
						// Increase its index
						shove._index = i - 1;
					}
					
					// Set new buffer data
					_chunks = now;
				}
			}
		}
		
		/**
		 * Corrects the positions of all the chunks following this one.
		 *
		 * @return {@code this}.
		 * @since 2016/03/22
		 */
		private __Chunk__ __correct()
		{
			// Lock
			synchronized (lock)
			{
				// Get our index to start at
				int mydx = _index;
				
				// Start position of next block;
				int pos = _position + _count;
				
				// Get the chunk list
				__Chunk__[] list = _chunks;
				int len = list.length;
				
				// Does not need correction if this is the last one
				if (mydx == len - 1)
					return this;
				
				// Correct positions
				for (int i = mydx + 1; i < len; i++)
				{
					// Get
					__Chunk__ at = list[i];
					
					// Set position
					at._position = pos;
					pos += at._count;
				}
			}
			
			// Self
			return this;
		}
		
		/**
		 * Inserts a chunk right after this one.
		 *
		 * @return The newly created chunk.
		 * @since 2016/03/22
		 */
		private __Chunk__ __insertAfter()
		{
			// Lock
			synchronized (lock)
			{
				// Get old chunk array and the count
				__Chunk__[] was = _chunks;
				int wasl = was.length;
				
				// Setup new chunk array
				int nowl = wasl + 1;
				__Chunk__[] now = new __Chunk__[nowl];
				
				// Copy all the chunks up to this one
				int mydx = _index;
				for (int i = 0; i <= mydx; i++)
					now[i] = was[i];
				
				// Copy all chunks following this, with new positional data
				for (int i = mydx + 1; i < wasl; i++)
				{
					// Move over
					__Chunk__ shove;
					now[i + 1] = shove = was[i];
					
					// Increase its index
					shove._index = i + 1;
				}
				
				// Create new next chunk
				__Chunk__ next = new __Chunk__();
				now[mydx + 1] = next;
				
				// Index follows this one
				next._index = mydx + 1;
				
				// Use new array
				_chunks = now;
				
				// Return it
				return next;
			}
		}
	}
}

