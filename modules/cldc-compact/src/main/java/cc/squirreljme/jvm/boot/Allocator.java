// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.Constants;
import cc.squirreljme.jvm.SystemCallError;
import cc.squirreljme.jvm.SystemCallIndex;

/**
 * This contains the static memory allocator.
 *
 * @since 2019/05/26
 */
@SuppressWarnings("FeatureEnvy")
public final class Allocator
{
	/** Chunk is an object. */
	public static final byte CHUNK_BIT_IS_OBJECT =
		0x01;
	
	/** Constant pool. */
	public static final byte CHUNK_BIT_IS_POOL =
		0x02;
	
	/** Tag value bits mask. */
	public static final int CHUNK_BITS_VALUE_MASK =
		0x0F;
	
	/** Shift for the chunk tag. */
	public static final byte CHUNK_TAG_SHIFT =
		24;
	
	/** Memory free tag (all bits set). */
	public static final int CHUNK_TAG_FREE =
		0xFF000000;
	
	/** Chunk tag mask. */
	public static final int CHUNK_TAG_MASK =
		0xFF000000;
	
	/** The chunk size mask. */
	public static final int CHUNK_SIZE_MASK =
		0x00FFFFFF;
	
	/** Chunk size limit, lower than the cap to allow for chunk area. */
	public static final int CHUNK_SIZE_LIMIT =
		16777200;
	
	/** Next chunk address. */
	public static final byte CHUNK_NEXT_OFFSET =
		0;
	
	/** Memory chunk size offset. */
	public static final byte CHUNK_SIZE_OFFSET =
		8;
	
	/** The length of chunks. */
	public static final byte CHUNK_LENGTH =
		12;
	
	/** Extra size to add that must be hit before a chunk is split. */
	public static final byte SPLIT_REQUIREMENT =
		16;
	
	/** The base RAM address. */
	@SuppressWarnings("StaticVariableMayNotBeInitialized")
	private static long _ramBase;
	
	/**
	 * Not used.
	 *
	 * @since 2019/05/26
	 */
	private Allocator()
	{
	}
	
	/**
	 * Allocates the given number of bytes.
	 *
	 * @param __tag The tag to use, only the lowest 8-bits are used.
	 * @param __sz The number of bytes to allocate.
	 * @return The address of the allocated data or {@code 0} if there is
	 * not enough memory remaining.
	 * @since 2019/10/19
	 */
	public static long allocate(int __tag, int __sz)
	{
		int key = Assembly.atomicTicker();
		try
		{
			// Lock area
			for (int i = 0; !Assembly.memAllocLock(key); i++)
				Assembly.spinLockBurn(i);
			
			// Fall into the allocation without lock
			return Allocator.allocateWithoutLock(__tag, __sz);
		}
		finally
		{
			Assembly.memAllocUnlock(key);
		}
	}
	
	/**
	 * Allocates the given number of bytes, no locking is performed at all.
	 *
	 * @param __tag The tag to use, only the lowest 8-bits are used.
	 * @param __sz The number of bytes to allocate.
	 * @return The address of the allocated data or {@code 0} if there is
	 * not enough memory remaining.
	 * @since 2019/05/26
	 */
	@SuppressWarnings("StaticVariableUsedBeforeInitialization")
	public static long allocateWithoutLock(int __tag, int __sz)
	{
		// The number of desired bytes
		int want = Allocator.CHUNK_LENGTH +
			(__sz <= 4 ? 4 : ((__sz + 3) & (~3)));
		
		// Negative size or too big?
		if (__sz < 0 || want > Allocator.CHUNK_SIZE_LIMIT)
			return 0;
		
		// Go through the memory chunks to locate a free chunk
		long seeker = Allocator._ramBase;
		while (seeker != 0)
		{
			// Read chunk properties
			int csz = Assembly.memReadInt(seeker, Allocator.CHUNK_SIZE_OFFSET);
			long cnx = Assembly.memReadLong(
				seeker, Allocator.CHUNK_NEXT_OFFSET);
			
			// Is this a free block? And can we fit in it?
			if ((csz & Allocator.CHUNK_TAG_MASK) == Allocator.CHUNK_TAG_FREE &&
				want <= (csz & Allocator.CHUNK_SIZE_MASK))
				return Allocator.__claim(__tag, want, seeker, csz, cnx);
			
			// Go to the next chunk
			seeker = cnx;
		}
		
		// Did not find a free chunk
		return 0;
	}
	
	/**
	 * Frees the specified memory pointer, making it available for later use.
	 *
	 * @param __p The pointer to free.
	 * @since 2019/10/19
	 */
	public static void free(long __p)
	{
		int key = Assembly.atomicTicker();
		try
		{
			// Lock area
			for (int i = 0; !Assembly.memAllocLock(key); i++)
				Assembly.spinLockBurn(i);
			
			// Fall into the allocation without lock
			Allocator.freeWithoutLock(__p);
		}
		finally
		{
			Assembly.memAllocUnlock(key);
		}
	}
	
	/**
	 * Frees the specified memory pointer, making it available for later use,
	 * without using a lock.
	 *
	 * @param __p The pointer to free.
	 * @since 2019/05/27
	 */
	public static void freeWithoutLock(long __p)
	{
		// This should never happen
		if (__p == 0 || __p == Constants.BAD_MAGIC)
			Assembly.breakpoint();
		
		// Determine the seeker position for this chunk
		long seeker = __p - Allocator.CHUNK_LENGTH;
		
		// Read chunk properties
		int csz = Assembly.memReadInt(seeker, Allocator.CHUNK_SIZE_OFFSET);
		long cnx = Assembly.memReadLong(seeker, Allocator.CHUNK_NEXT_OFFSET);
		
		// Actual logically used space
		int usedSpace = (csz & Allocator.CHUNK_SIZE_MASK);
		
		// Clear out memory with invalid data, that is BAD_MAGIC
		for (int i = Allocator.CHUNK_LENGTH; i < usedSpace; i += 4)
			Assembly.memWriteInt(seeker, i, Constants.BAD_MAGIC);
		
		// Make sure the reference count index is zero, to detect un-count
		// after free
		int rci = Allocator.CHUNK_LENGTH + Constants.OBJECT_COUNT_OFFSET;
		if (rci + 4 <= csz)
			Assembly.memWriteInt(seeker, rci, 0);
		
		// See if we can merge this with the following chunk
		if (cnx != 0)
		{
			// Get properties of the next chunk
			int nsz = Assembly.memReadInt(cnx, Allocator.CHUNK_SIZE_OFFSET);
			long nnx = Assembly.memReadLong(cnx, Allocator.CHUNK_NEXT_OFFSET);
			
			// Free space? Merge into it!
			if ((nsz & Allocator.CHUNK_TAG_MASK) == Allocator.CHUNK_TAG_FREE)
			{
				// Calculate the would be new size
				int newsize = usedSpace + (nsz & Allocator.CHUNK_SIZE_MASK);
				
				// Only merge chunks which are within the size limit, otherwise
				// a large portion of memory will not able to be reclaimed
				// because it would logically have a small size
				if (newsize <= Allocator.CHUNK_SIZE_LIMIT)
				{
					// New size of our current chunk
					Assembly.memWriteInt(seeker, Allocator.CHUNK_SIZE_OFFSET,
						newsize | Allocator.CHUNK_TAG_FREE);
					
					// Our chunk's next becomes the right side's next
					Assembly.memWriteLong(seeker, Allocator.CHUNK_NEXT_OFFSET,
						nnx);
					
					// Do not use normal free set
					return;
				}
			}
		}
		
		// Set chunk as free now, keep the original size
		Assembly.memWriteInt(seeker, Allocator.CHUNK_SIZE_OFFSET,
			csz | Allocator.CHUNK_TAG_FREE);
	}
	
	/**
	 * Claims the given block and returns it.
	 *
	 * @param __tag The tag to use, only 8-bits are used.
	 * @param __want The wanted size.
	 * @param __seeker The current chunk position.
	 * @param __csz The chunk size.
	 * @param __cnx The chunk next pointer.
	 * @return The allocation pointer.
	 * @since 2019/06/21
	 */
	private static long __claim(int __tag, int __want, long __seeker,
		int __csz, long __cnx)
	{
		// Calculate free space in this chunk
		int freeSpace = (__csz & Allocator.CHUNK_SIZE_MASK);
		
		// This chunk will be split into a used and free chunk
		if (__want + Allocator.SPLIT_REQUIREMENT <= freeSpace)
		{
			// The size of the right side chunk is cut by our wanted
			// size
			int nextSize = freeSpace - __want;
			
			// The position of the next chunk
			long nextPos = __seeker + __want;
			
			// Setup new chunk
			Assembly.memWriteInt(__seeker, Allocator.CHUNK_SIZE_OFFSET,
				(__want & Allocator.CHUNK_SIZE_MASK) |
				(__tag << Allocator.CHUNK_TAG_SHIFT));
			Assembly.memWriteLong(__seeker, Allocator.CHUNK_NEXT_OFFSET,
				nextPos);
			
			// Setup the split chunk, points to the original next
			Assembly.memWriteInt(nextPos, Allocator.CHUNK_SIZE_OFFSET,
				(nextSize & Allocator.CHUNK_SIZE_MASK) |
				Allocator.CHUNK_TAG_FREE);
			Assembly.memWriteLong(nextPos, Allocator.CHUNK_NEXT_OFFSET,
				__cnx);
		}
		
		// Not being split, so the block gets claimed
		else
		{
			// Keep the originally passed size, but set the tag
			Assembly.memWriteInt(__seeker, Allocator.CHUNK_SIZE_OFFSET,
				freeSpace | (__tag << Allocator.CHUNK_TAG_SHIFT));
		}
		
		// The returning pointer
		long rv = __seeker + Allocator.CHUNK_LENGTH;
		
		// Clear out memory since Java expects the data to be
		// initialized to zero always
		for (int i = Allocator.CHUNK_LENGTH; i < __want; i += 4)
			Assembly.memWriteInt(__seeker, i, 0);
		
		// Return the used pointer
		return rv;
	}
	
	/**
	 * Initializes the RAM links.
	 *
	 * @param __ramBase The base of RAM.
	 * @param __ramSize The amount of RAM available.
	 * @since 2019/05/26
	 */
	static void __initRamLinks(long __ramBase, int __ramSize)
	{
		// Loops through all blocks
		for (long seeker = __ramBase;;)
		{
			// Read current and next offset
			int csz = Assembly.memReadInt(seeker, Allocator.CHUNK_SIZE_OFFSET);
			long cnx = Assembly.memReadLong(seeker,
				Allocator.CHUNK_NEXT_OFFSET);
			
			// Reached the terminator which has been initialized to zero by
			// the BootROM, so this block and whatever is left becomes free
			// allocation space
			if (csz == 0 || cnx == 0)
			{
				// Since there is an allocation limit of 16MiB, if there is
				// more than 16MiB of memory available then there will be the
				// problem of having one gigantic chunk that is too small, or
				// keeping total memory capped at 16MiB. So to avoid this,
				// this will create as many chunks as needed to fill the
				// remainder of memory!
				
				// Create chunks as big as possible across a span
				long sizeLeft = __ramSize - (seeker - __ramBase);
				while (sizeLeft > 0)
				{
					// Determine how big this chunk becomes
					int useSize = (int)(sizeLeft > Allocator.CHUNK_SIZE_LIMIT ?
						Allocator.CHUNK_SIZE_LIMIT : sizeLeft);
					
					// Reduce used size
					sizeLeft -= useSize;
					
					// Create free block here
					Assembly.memWriteInt(seeker, Allocator.CHUNK_SIZE_OFFSET,
						useSize | Allocator.CHUNK_TAG_FREE);
					
					// Set pointer and seeker to the next block
					if (sizeLeft > Allocator.SPLIT_REQUIREMENT)
					{
						// Where is this located?
						long nextp = seeker + useSize;
						
						// Write next block position
						Assembly.memWriteLong(seeker,
							Allocator.CHUNK_NEXT_OFFSET,
							nextp);
						
						// Go here next
						seeker = nextp;
					}
					
					// Too small to really be considered a block, so just
					// drop the next one
					else
					{
						// Always ensure the next block is zero!
						Assembly.memWriteLong(seeker,
							Allocator.CHUNK_NEXT_OFFSET,
							0);
						
						// Stop
						break;
					}
				}
				
				// Stop
				break;
			}
			
			// Go to the next block in memory
			seeker = cnx;
		}
		
		// Set memory parameters
		Allocator._ramBase = __ramBase;
	}
}
