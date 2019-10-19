// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This contains the static memory allocator.
 *
 * @since 2019/05/26
 */
public final class Allocator
{
	/** Chunk is an object. */
	public static final byte CHUNK_BIT_IS_OBJECT =
		0x01;
	
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
	
	/** Memory chunk size offset. */
	public static final byte CHUNK_SIZE_OFFSET =
		0;
	
	/** Next chunk address. */
	public static final byte CHUNK_NEXT_OFFSET =
		4;
	
	/** The length of chunks. */
	public static final byte CHUNK_LENGTH =
		8;
	
	/** Extra size to add that must be hit before a chunk is split. */
	public static final byte SPLIT_REQUIREMENT =
		16;
		
	/** Locking magic number. */
	private static final int _LOCK_MAGIC =
		0x506F4C79;
	
	/** The base RAM address. */
	private static volatile int _rambase;
	
	/** The locking pointer address. */
	private static volatile int _lockptr;
	
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
	public static final int allocate(int __tag, int __sz)
	{
		// Determine the special locking key to use, never let this be zero!
		int key = (_LOCK_MAGIC ^ (__tag + __sz)) | 1;
		
		// Try locking the pointer
		int lp = Allocator._lockptr;
		try
		{
			// Lock using our special key, which will never be zero!
			// Spin-lock so this is executed as fast as possible!
			while (0 != Assembly.atomicCompareGetAndSet(0, key, lp))
				continue;
			
			// Fall into the allocation without lock
			return Allocator.allocateWithoutLock(__tag, __sz);
		}
		
		// Clear the lock always
		finally
		{
			// Clear out lock, if not matched then something is wrong!
			int old;
			if (key != (old = Assembly.atomicCompareGetAndSet(key, 0, lp)))
			{
				// Another allocation took our lock??
				todo.DEBUG.code('a', 'l', old);
				Assembly.breakpoint();
				
				// {@squirreljme.error SV0j Another allocation took the lock
				// from us?}
				throw new VirtualMachineError("SV0j");
			}
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
	public static final int allocateWithoutLock(int __tag, int __sz)
	{
		// The number of desired bytes
		int want = CHUNK_LENGTH + (__sz <= 4 ? 4 : ((__sz + 3) & (~3)));
		
		// Negative size or too big?
		if (__sz < 0 || want > CHUNK_SIZE_LIMIT)
			return 0;
		
		// Go through the memory chunks to locate a free chunk
		int seeker = Allocator._rambase;
		while (seeker != 0)
		{
			// Read chunk properties
			int csz = Assembly.memReadInt(seeker, CHUNK_SIZE_OFFSET),
				cnx = Assembly.memReadInt(seeker, CHUNK_NEXT_OFFSET);
			
			// Is this a free block? And can we fit in it?
			if ((csz & CHUNK_TAG_MASK) == CHUNK_TAG_FREE &&
				want <= (csz & CHUNK_SIZE_MASK))
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
	public static final void free(int __p)
	{
		// Determine the special locking key to use, never let this be zero!
		int key = (_LOCK_MAGIC ^ __p) | 1;
		
		// Try locking the pointer
		int lp = Allocator._lockptr;
		try
		{
			// Lock using our special key, which will never be zero!
			// Spin-lock so this is executed as fast as possible!
			while (0 != Assembly.atomicCompareGetAndSet(0, key, lp))
				continue;
			
			// Fall into the free without lock
			Allocator.freeWithoutLock(__p);
		}
		
		// Clear the lock always
		finally
		{
			// Clear out lock, if not matched then something is wrong!
			int old;
			if (key != (old = Assembly.atomicCompareGetAndSet(key, 0, lp)))
			{
				// Another free took our lock??
				todo.DEBUG.code('f', 'l', old);
				Assembly.breakpoint();
				
				// {@squirreljme.error SV0k Another free took the lock
				// from us?}
				throw new VirtualMachineError("SV0k");
			}
		}
	}
	
	/**
	 * Frees the specified memory pointer, making it available for later use,
	 * without using a lock.
	 *
	 * @param __p The pointer to free.
	 * @since 2019/05/27
	 */
	public static final void freeWithoutLock(int __p)
	{
		// This should never happen
		if (__p == 0 || __p == Constants.BAD_MAGIC)
			Assembly.breakpoint();
		
		// Determine the seeker position for this chunk
		int seeker = __p - CHUNK_LENGTH;
		
		// Read chunk properties
		int csz = Assembly.memReadInt(seeker, CHUNK_SIZE_OFFSET),
			cnx = Assembly.memReadInt(seeker, CHUNK_NEXT_OFFSET);
		
		// Actual logically used space
		int usedspace = (csz & CHUNK_SIZE_MASK);
		
		// Clear out memory with invalid data, that is BAD_MAGIC
		int bm = Constants.BAD_MAGIC;
		Assembly.sysCallP(SystemCallIndex.MEM_SET_INT,
			__p, bm, usedspace - CHUNK_LENGTH);
		if (Assembly.sysCallPV(SystemCallIndex.ERROR_GET,
			SystemCallIndex.MEM_SET_INT) != SystemCallError.NO_ERROR)
		{
			// Fast memsetint() is not supported, so manually wipe
			// all the bytes!
			for (int i = CHUNK_LENGTH; i < usedspace; i += 4)
				Assembly.memWriteInt(seeker, i, bm);
		}
		
		// Make sure the reference count index is zero, to detect uncount
		// after free
		int rci = CHUNK_LENGTH + Constants.OBJECT_COUNT_OFFSET;
		if (rci + 4 <= csz)
			Assembly.memWriteInt(seeker, rci, 0);
		
		// See if we can merge this with the following chunk
		if (cnx != 0)
		{
			// Get properties of the next chunk
			int nsz = Assembly.memReadInt(cnx, CHUNK_SIZE_OFFSET),
				nnx = Assembly.memReadInt(cnx, CHUNK_NEXT_OFFSET);
			
			// Free space? Merge into it!
			if ((nsz & CHUNK_TAG_MASK) == CHUNK_TAG_FREE)
			{
				// Calculate the would be new size
				int newsize = usedspace + (nsz & CHUNK_SIZE_MASK);
				
				// Only merge chunks which are within the size limit, otherwise
				// a large portion of memory will not able to be reclaimed
				// because it would logically have a small size
				if (newsize <= CHUNK_SIZE_LIMIT)
				{
					// New size of our current chunk
					Assembly.memWriteInt(seeker, CHUNK_SIZE_OFFSET,
						newsize | CHUNK_TAG_FREE);
					
					// Our chunk's next becomes the right side's next
					Assembly.memWriteInt(seeker, CHUNK_NEXT_OFFSET,
						nnx);
					
					// Do not use normal free set
					return;
				}
			}
		}
		
		// Set chunk as free now, keep the original size
		Assembly.memWriteInt(seeker, CHUNK_SIZE_OFFSET,
			csz | CHUNK_TAG_FREE);
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
	private static final int __claim(int __tag, int __want, int __seeker,
		int __csz, int __cnx)
	{
		// Calculate free space in this chunk
		int freespace = (__csz & CHUNK_SIZE_MASK);
		
		// This chunk will be split into a used and free chunk
		if (__want + SPLIT_REQUIREMENT <= freespace)
		{
			// The size of the right side chunk is cut by our wanted
			// size
			int nextsize = freespace - __want;
			
			// The position of the next chunk
			int nextpos = __seeker + __want;
			
			// Setup new chunk
			Assembly.memWriteInt(__seeker, CHUNK_SIZE_OFFSET,
				(__want & CHUNK_SIZE_MASK) | (__tag << CHUNK_TAG_SHIFT));
			Assembly.memWriteInt(__seeker, CHUNK_NEXT_OFFSET,
				nextpos);
			
			// Setup the split chunk, points to the original next
			Assembly.memWriteInt(nextpos, CHUNK_SIZE_OFFSET,
				(nextsize & CHUNK_SIZE_MASK) | CHUNK_TAG_FREE);
			Assembly.memWriteInt(nextpos, CHUNK_NEXT_OFFSET,
				__cnx);
		}
		
		// Not being split, so the block gets claimed
		else
		{
			// Keep the originally passed size, but set the tag
			Assembly.memWriteInt(__seeker, CHUNK_SIZE_OFFSET,
				freespace | (__tag << CHUNK_TAG_SHIFT));
		}
		
		// The returning pointer
		int rv = __seeker + CHUNK_LENGTH;
		
		// Clear out memory since Java expects the data to be
		// initialized to zero always
		Assembly.sysCallP(SystemCallIndex.MEM_SET,
			rv, 0, __want - CHUNK_LENGTH);
		if (Assembly.sysCallPV(SystemCallIndex.ERROR_GET,
			SystemCallIndex.MEM_SET) != SystemCallError.NO_ERROR)
		{
			// Fast memset() is not supported, so manually wipe
			// all the bytes!
			for (int i = CHUNK_LENGTH; i < __want; i += 4)
				Assembly.memWriteInt(__seeker, i, 0);
		}
		
		// Return the used pointer
		return rv;
	}
	
	/**
	 * Initializes the RAM links.
	 *
	 * @param __rambase The base of RAM.
	 * @param __ramsize The amount of RAM available.
	 * @since 2019/05/26
	 */
	static final void __initRamLinks(int __rambase, int __ramsize)
	{
		// Loops through all blocks
		for (int seeker = __rambase;;)
		{
			// Read current and next offset
			int csz = Assembly.memReadInt(seeker, CHUNK_SIZE_OFFSET),
				cnx = Assembly.memReadInt(seeker, CHUNK_NEXT_OFFSET);
			
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
				int sizeleft = __ramsize - seeker;
				while (sizeleft > 0)
				{
					// Determine how big this chunk becomes
					int usesize = (sizeleft > CHUNK_SIZE_LIMIT ?
						CHUNK_SIZE_LIMIT : sizeleft);
					
					// Reduce used size
					sizeleft -= usesize;
					
					// Create free block here
					Assembly.memWriteInt(seeker, CHUNK_SIZE_OFFSET,
						usesize | CHUNK_TAG_FREE);
					
					// Set pointer and seeker to the next block
					if (sizeleft > SPLIT_REQUIREMENT)
					{
						// Where is this located?
						int nextp = seeker + usesize;
						
						// Write next block position
						Assembly.memWriteInt(seeker, CHUNK_NEXT_OFFSET,
							nextp);
						
						// Go here next
						seeker = nextp;
					}
					
					// Too small to really be considered a block, so just
					// drop the next one
					else
					{
						// Always ensure the next block is zero!
						Assembly.memWriteInt(seeker, CHUNK_NEXT_OFFSET,
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
		Allocator._rambase = __rambase;
		
		// Set pointer used to control the lock state of memory
		Allocator._lockptr = Allocator.allocateWithoutLock(0, 4);
	}
}
