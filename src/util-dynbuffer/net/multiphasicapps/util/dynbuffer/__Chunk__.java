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

import java.util.List;

/**
 * This represents a chunk of data within a buffer.
 *
 * Each chunk initially starts with a byte array of the default size. If bytes
 * are removed or inserted in specific locations within the chunk that cause
 * lots of data to be moved around (above the threshold) then the chunk will
 * be split and still use the same byte array.
 *
 * @since 2016/07/31
 */
final class __Chunk__
{
	/**
	 * {@squirreljme.property
	 * net.multiphasicapps.util.dynbuffer.chunksize=(size) This is used to
	 * specify an alternative size ot be used for the default size of a chunk
	 * within a dynamic buffer.}
	 */
	private static final int _DEFAULT_SIZE =
		Math.max(Integer.getInteger(
			"net.multiphasicapps.util.dynbuffer.chunksize", 128), 1);
	
	/** The owning buffer. */
	protected final DynamicByteBuffer owner;
	
	/** The buffer data. */
	final byte[] _data;
	
	/** The data start position. */
	final int _datastart;
	
	/** The data end position. */
	volatile int _dataend;
	
	/** The chunk index, gets adjusted when chunks are added/removed. */
	volatile int _index;
	
	/** The calculated position of this chunk. */
	volatile int _calcpos;
	
	/** The data head. */
	volatile int _head;
	
	/** The data tail. */
	volatile int _tail;
	
	/**
	 * This initializes a blank chunk using the specified buffer as the
	 * owner.
	 *
	 * @param __dbb The byte buffer which owns this chunk.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/01
	 */
	__Chunk__(DynamicByteBuffer __dbb)
		throws NullPointerException
	{
		// Check
		if (__dbb == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __dbb;
		
		// Create buffer with the default size
		int ds = _DEFAULT_SIZE;
		this._data = new byte[ds];
		this._datastart = 0;
		this._dataend = ds;
	}
	
	/**
	 * Initializes a chunk which uses pre-existing data.
	 *
	 * @param __dbb The owning dynamic buffer.
	 * @param __b The byte array to base data for.
	 * @param __o The offset of the data.
	 * @param __l The length of the data area.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or they exceed the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/02
	 */
	__Chunk__(DynamicByteBuffer __dbb, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__dbb == null)
			throw new NullPointerException("NARG");
		int n = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > n)
			throw new IndexOutOfBoundsException(String.format("BAOB %d %d %d",
				n, __o, __l));
		
		// Set
		this.owner = __dbb;
		
		// Set
		this._data = __b;
		this._datastart = __o;
		this._dataend = __o + __l;
		
		// Head and tail are at the start
		this._head = __o;
		this._tail = __o;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/02
	 */
	@Override
	public String toString()
	{
		return String.format("{i=%d, d=[%d, %d), p=%d, rw=[%d, %d]}",
			this._index, this._datastart, this._dataend, this.__position(),
			this._head, this._tail);
	}
	
	/**
	 * Adds the specified bytes to the current chunk. If the data cannot fit
	 * then nearby chunks are checked. If data cannot fit there, then a new
	 * chunk is created.
	 *
	 * @param __base The physical address to write to.
	 * @param __b The bytes to write.
	 * @param __o Offset into the array.
	 * @param __l Number of bytes to write.
	 * @since 2016/08/01
	 */
	final void __add(int __base, byte[] __b, int __o, int __l)
	{
		// Do not bother if adding nothing
		if (__l <= 0)
			return;
		
		// Write until all bytes have been placed
		__Chunk__ now = this;
		int at = __o;
		int left = __l;
		DynamicByteBuffer owner = this.owner;
		while (left > 0)
		{
			// Get a few variables
			byte[] data = now._data;
			int nowpos = now.__position();
			int datastart = now._datastart;
			int dataend = now._dataend;
			int head = now._head;
			int tail = now._tail;
			int htrun = tail - head;
			int headleft = head - datastart;
			
			// Detect if writing at the head (when the chunk is not empty)
			if (head != tail && __base <= nowpos)
			{
				// Debug
				System.err.println("DEBUG -- xA");
				
				// Enough room to write before the head
				if (left < headleft)
				{
					// The real base to start write at
					int basehead = head - left;
					
					// Write loop
					int wpos = basehead;
					while (left > 0)
					{
						data[wpos++] = __b[at++];
						left--;
					}
					
					// head is at the base now
					now._head = basehead;
				}
				
				// Not enough room in the head, add chunk before this one
				else
				{
					// Create new chunk
					__Chunk__ slab = new __Chunk__(owner);
					
					// Add before this one (at this index)
					owner.__insert(slab, now._index);
					
					// Switch to this chunk
					now = slab;
					continue;
				}
			}
			
			// Writing at the tail end
			else if (__base >= (head + htrun))
			{
				// Debug
				System.err.println("DEBUG -- xB");
				
				// No more room left?
				if (dataend == tail)
				{
					// Get next chunk, if applicable
					__Chunk__ into = now.__next();
					
					// If no chunk, add one
					if (into == null)
					{
						int index = now._index;
						now = new __Chunk__(owner);
						owner.__insert(now, index + 1);
					}
					
					// Otherwise use it
					else
						now = into;
					
					// Try again
					continue;
				}
				
				// No need to loop on the outer end
				while (left > 0 && tail < dataend)
				{
					data[tail++] = __b[at++];
					left--;
				}
				
				// Set new tail
				now._tail = tail;
			}
			
			// Split chunk for reading/writing
			else
			{
				// Debug
				System.err.println("DEBUG -- xC");
				
				// Determine the data position used
				int atdp = head + (__base - nowpos);
				
				// Debug
				System.err.printf("DEBUG -- Splice? %d %d %d%n",
					head, atdp, tail);
				
				// Create chunk to follow this one
				__Chunk__ next = new __Chunk__(owner);
				
				// And another chunk that has the remainder of the data after
				// that
				__Chunk__ after = new __Chunk__(owner, data, atdp,
					dataend - atdp);
				after._head = atdp;	// Starts at partition
				after._tail = tail;	// Inherited
				
				// The current chunk ends at the split
				after._dataend = atdp;
				after._tail = atdp;
				
				// Insert
				int index = now._index;
				owner.__insert(next, index + 1);
				owner.__insert(after, index + 2);
				
				// Write into the next block
				now = next;
				continue;
			}
		}
	}
	
	/**
	 * Deletes bytes at the given address.
	 *
	 * @param __base The position to start deletion from.
	 * @param __l The number of bytes to delete.
	 * @since 2016/08/02
	 */
	final void __delete(int __base, int __l)
	{
		// Deleting nothing?
		if (__l <= 0)
			return;
		
		// The base chunk is this one
		__Chunk__ pre = this.__previous();
		__Chunk__ startc = this, trailc = this;
		int left = __l;
		while (left > 0)
		{
			// Calculate the logical position
			int logstart = trailc._head + (__base - trailc.__position());
			int logend = Math.min(trailc._tail, logstart + left);
			
			// Remove count
			int did = logend - logstart;
			left -= did;
			
			// Go to the next block?
			if (left > 0)
				trailc = trailc.__next();
		}
		
		// Perform actual chunk removal
		DynamicByteBuffer owner = this.owner;
		List<__Chunk__> chunks = owner._chunks;
		while (trailc != pre)
		{
			// Actual data starts and end relative to the current head
			int relstart = (__base - trailc.__position());
			int relend = relstart + __l;
			
			// Head and tail positions
			int head = trailc._head, tail = trailc._tail;
			
			// The head/tail positions for write
			int vhead = head + relstart, vtail = tail + relend;
			
			// Are the values at or past the head and/or tail?
			// This is used to determine if the entire chunk can just be
			// dropped.
			boolean pasthead = relstart <= head,
				pasttail = relend >= tail;
			
			// Debug
			System.err.printf("DEBUG -- Delete %d %d (in %s -> %s, " +
				"%d - %d, %d %d, %s, %s)%n", __base,
				__l, trailc, pre, relstart, relend, head, tail, pasthead,
				pasttail);
			
			// The previous chunk (before it gets deleted)
			__Chunk__ goprev = trailc.__previous();
			int tindex = trailc._index;
			
			// Deletes the entire chunk
			if (pasthead && pasttail)
			{
				// If this is the sole chunk in the buffer then just clear
				// the head/tail
				if (chunks.size() == 1)
				{
					int ds = trailc._datastart;
					trailc._head = ds;
					trailc._tail = ds;
				}
				
				// Otherwise remove it
				else
				{
					System.err.println("TODO -- Efficient merge of chunks.");
					
					// Remove chunk here and make it stale
					chunks.remove(tindex);
					owner._staledx = Math.min(owner._staledx, tindex);
					owner.__correctIndices(tindex);
				}
			}
			
			// Deletes everything at the head to the higher position
			else if (pasthead)
				trailc._head = vtail;
			
			// Deletes everything from the lower position to the tail
			else if (pasttail)
				trailc._tail = vhead;
			
			// Deletes data in the middle of the chunk, splitting it
			else
				throw new Error("TODO");
			
			// Go to the previous chunk
			trailc = goprev;
		}
		
		// Data has changed
		__maybeStale();
	}
	
	/**
	 * Returns the end position of this chunk.
	 *
	 * @return The end position.
	 * @since 2016/08/01
	 */
	final int __endPosition()
	{
		return __position() + __size();
	}
	
	/**
	 * This reads data from the current chunk while potentially moving to
	 * later chunks.
	 *
	 * @param __base The physical address to read from.
	 * @param __b The storage area for read bytes.
	 * @param __o Offset into the array.
	 * @param __l Number of bytes to read.
	 * @since 2016/08/01
	 */
	final void __get(int __base, byte[] __b, int __o, int __l)
	{
		// Do not bother if getting nothing
		if (__l <= 0)
			return;
		
		// Remainder and position
		int at = __o;
		int left = __l;
		
		// Keep reading
		__Chunk__ now = this;
		while (left > 0 && now != null)
		{
			// Calculate the logical position
			int logstart = now._head + (__base - now.__position());
			int logend = Math.min(now._tail, logstart + left);
			
			// Copy values
			byte[] data = now._data;
			for (int i = logstart; i < logend; i++)
				__b[at++] = data[i];
			
			// Next read
			now = now.__next();
			int did = logend - logstart;
			left -= did;
		}
		
		// {@squirreljme.error AD05 Did not read all bytes.}
		if (left > 0)
			throw new IllegalStateException("AD05");
	}
	
	/**
	 * This makes it so that any block following this one becomes stale if
	 * this block is before the current stale index.
	 *
	 * @since 2016/08/01
	 */
	final void __maybeStale()
	{
		// Force any blocks following this to become stale
		DynamicByteBuffer owner = this.owner;
		int index = this._index;
		if (owner._staledx > index)
			owner._staledx = index + 1;
	}
	
	/**
	 * Returns the next chunk.
	 *
	 * @return The next chunk or {@code null} if this is the last.
	 * @since 2016/08/02
	 */
	final __Chunk__ __next()
	{
		// The last chunk does not have a next
		List<__Chunk__> chunks = this.owner._chunks;
		int index = this._index + 1;
		if (index == chunks.size())
			return null;
		
		// Otherwise get it
		return chunks.get(index);
	}
	
	/**
	 * Returns the physical position associated with the start of the chunk.
	 *
	 * @return The chunk's physical starting position.
	 * @since 2016/08/01
	 */
	final int __position()
	{
		// The first chunk always returns zero
		int index = this._index;
		if (index == 0)
			return 0;
		
		// Check if the index is stale
		DynamicByteBuffer owner = this.owner;
		int staledx = owner._staledx;
		int pos = this._calcpos;
		if (index >= staledx)
		{
			// There should always be a chunk before the first
			__Chunk__ prev = this.__previous();
			if (prev == null)
				throw new RuntimeException("OOPS");
			
			// Based on previous position
			pos = prev.__endPosition();
			owner._staledx = index + 1;
			this._calcpos = pos;
		}
		
		// Return position
		return pos;
	}
	
	/**
	 * Returns the chunk that is before this one.
	 *
	 * @return The previous chunk.
	 * @since 2016/08/01
	 */
	final __Chunk__ __previous()
	{
		// The first chunk never has a previous one
		int index = this._index;
		if (index == 0)
			return null;
		
		// Otherwise get it
		return this.owner._chunks.get(index - 1);
	}
	
	/**
	 * This is similar to get except that it stores values into chunks
	 * overwriting any previously set value.
	 *
	 * @param __base The physical address to write to.
	 * @param __b The storage area for written bytes.
	 * @param __o Offset into the array.
	 * @param __l Number of bytes to write.
	 * @since 2016/08/02
	 */
	final void __set(int __base, byte[] __b, int __o, int __l)
	{
		// Do not bother if getting nothing
		if (__l <= 0)
			return;
		
		// Remainder and position
		int at = __o;
		int left = __l;
		
		// Keep reading
		__Chunk__ now = this;
		while (left > 0 && now != null)
		{
			// Calculate the logical position
			int logstart = now._head + (__base - now.__position());
			int logend = Math.min(now._tail, logstart + left);
			
			// Copy values
			byte[] data = now._data;
			for (int i = logstart; i < logend; i++)
				data[i] = __b[at++];
			
			// Next to write into
			now = now.__next();
			int did = logend - logstart;
			left -= did;
		}
		
		// {@squirreljme.error AD06 Did not set all bytes.}
		if (left > 0)
			throw new IllegalStateException("AD06");
	}
	
	/**
	 * Returns the size of this chunk.
	 *
	 * @return The chunk size.
	 * @since 2016/08/01
	 */
	final int __size()
	{
		return this._tail - this._head;
	}
}

