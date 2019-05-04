// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.vki;

/**
 * This contains the actual memory allocator.
 *
 * @since 2019/05/04
 */
public final class Allocator
{
	/** Bit to indicate memory partition is free. */
	public static final int MEMPART_FREE_BIT =
		0x80000000;
	
	/** Offset to size of memory partition. */
	public static final int OFF_MEMPART_SIZE =
		0;
	
	/** Offset to next link in memory partition. */
	public static final int OFF_MEMPART_NEXT =
		4;
	
	/** Allocation base. */
	static int _allocbase;
	
	/**
	 * Allocates a space within memory of the given size and then returns
	 * it.
	 *
	 * @param __sz The number of bytes to allocate.
	 * @return The allocated object or {@code 0} if allocation has failed.
	 * @since 2019/04/22
	 */
	public static final int allocate(int __sz)
	{
		// Cannot allocate zero bytes
		if (__sz == 0)
			return 0;
		
		// This is the seeker which scans through the memory links to find
		// free space somewhere
		int seeker = Allocator._allocbase;
		
		// Round allocations to nearest 4 bytes since the VM expects this
		// alignment be used
		__sz = (__sz + 3) & (~3);
		
		// We will be going through every chain
		for (;;)
		{
			// If the seeker ever ends up at the null pointer then we just
			// ran off the end of the chain
			if (seeker == 0)
				return 0;
			
			// Read size and next address
			int size = Assembly.memReadInt(seeker, OFF_MEMPART_SIZE),
				next = Assembly.memReadInt(seeker, OFF_MEMPART_NEXT);
				
			// This region of memory is free for use
			if ((size & MEMPART_FREE_BIT) != 0)
			{
				// Determine the actual size available by clipping the bit
				// of and then. The block size is the size of this region
				// with the partition info
				int blocksize = (size ^ MEMPART_FREE_BIT),
					actcount = blocksize - 8;
				
				// There is enough space to use this partition
				if (__sz <= actcount)
				{
					// The return pointer is the region start address
					int rv = seeker + 8;
					
					// This is the new block size, if it does not match the
					// current block size then we are not using an entire
					// block (if it does match then we just claimed all the
					// free space here).
					int newblocksize = (__sz + 8);
					if (blocksize != newblocksize)
					{
						// This is the address of the next block
						int nextseeker = seeker + newblocksize;
						
						// The size of this block is free and it has the
						// remaining size of the current block's old size
						// minute the new block size
						Assembly.memWriteInt(nextseeker, OFF_MEMPART_SIZE,
							(blocksize - newblocksize) | MEMPART_FREE_BIT);
						
						// The next link of the next block because our
						// current link (since it is a linked list)
						Assembly.memWriteInt(nextseeker, OFF_MEMPART_NEXT,
							next);
						
						// The next link of our current block (the one we
						// are claiming)
						Assembly.memWriteInt(seeker, OFF_MEMPART_NEXT,
							nextseeker);
					}
					
					// Write the new block properties and its used
					// indication
					Assembly.memWriteInt(seeker, OFF_MEMPART_SIZE,
						newblocksize);
					
					// Clear the memory here since it is expected that
					// everything in Java has been initialized to zero,
					// this is also much safer than C's malloc().
					for (int i = 0; i < __sz; i += 4)
						Assembly.memWriteInt(rv, i, 0);
					
					// Return pointer
					return rv;
				}
			}
			
			// If this point was reached, we need to try the next link
			seeker = next;
		}
	}
}

