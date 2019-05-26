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
	/** Memory chunk size offset. */
	public static final int CHUNK_SIZE_OFFSET =
		0;
	
	/** Next chunk address. */
	public static final int CHUNK_NEXT_OFFSET =
		4;
	
	/** The length of chunks. */
	public static final int CHUNK_LENGTH =
		8;
	
	/** The base RAM address. */
	private static volatile int _rambase;
	
	/**
	 * Not used.
	 *
	 * @since 2019/05/26
	 */
	private Allocator()
	{
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
			
			// Reached the terminator, whatever is left is the remaining
			// amount of free memory
			if (csz == 0 || cnx == 0)
			{
				// The size of this block is whatever remains in memory
				Assembly.memWriteInt(seeker, CHUNK_SIZE_OFFSET,
					__ramsize - seeker);
				
				// And ensure the next block is always zero!
				Assembly.memWriteInt(seeker, CHUNK_NEXT_OFFSET,
					0);
				
				// Stop
				break;
			}
			
			// Go to the next block in memory
			seeker = cnx;
		}
		
		// Set memory parameters
		Allocator._rambase = __rambase;
	}
}
