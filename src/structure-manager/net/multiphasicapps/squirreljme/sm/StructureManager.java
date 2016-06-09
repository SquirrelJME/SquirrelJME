// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.sm;

import net.multiphasicapps.squirreljme.memory.MemoryIOException;
import net.multiphasicapps.squirreljme.memory.MemoryPool;
import net.multiphasicapps.squirreljme.memory.MemoryPoolManager;
import net.multiphasicapps.squirreljme.memory.NoSuchMemoryPoolException;

/**
 * This is the manager which manages all allocated structures.
 *
 * This is shared by all implementations of SquirrelJME so that they have a
 * uniform structure across all architectures and platforms.
 *
 * The start of the memory pool which is managed by this structure manager
 * contains the following fields:
 *
 * 1. int -- The atomic lock on the pool, 1 means an active lock (uses compare
 *           and swap).
 *           0 = Not locked.
 *           1 = Locked, a thread is checking this pool for allocation.
 * 2. int -- The number of times the garbage collector has been run. For every
 *           run of the garbage collector, this is used by other threads to
 *           detect if a GC event has occurred so that it must restart
 *           allocation.
 * 3. int -- The atomic lock on the garbage collector. Only the first pool's
 *           lock is considered for the garbage collector.
 *           0 = Not locked.
 *           1 = Locked, a thread is not garbage collecting but is locking a
 *               pool so it may be checked.
 *           2 = A garbage collector is running. If a lock from 0 to 1 fails
 *               then the GC count will be checked against the entry value to
 *               determine if a GC has ran. This check will be done in a yield
 *               loop. If a GC is needed then any thread that is allocating
 *               will attempt to CAS from 0 to 2. Any threads that detect an
 *               increase in the GC count must restart their allocations.
 * 4. int -- The number of table entries which exist in this pool.
 *
 * @since 2016/06/08
 */
public class StructureManager
{
	/** The used memory pool. */
	protected final MemoryPoolManager poolman;
	
	/** The type used for pointers. */
	protected final PointerType pointertype;
	
	/** The number of bytes in a pointer. */
	protected final long pointerbytes;
	
	/** The pointer mask. */
	protected final long pointermask;
	
	/**
	 * Intializes the object manager.
	 *
	 * @param __pm Initializes the memory pool manager.
	 * @param __pt The type of values used for pointers.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/08
	 */
	public StructureManager(MemoryPoolManager __pm, PointerType __pt)
		throws NullPointerException
	{
		// Check
		if (__pm == null || __pm == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.poolman = __pm;
		this.pointertype = __pt;
		this.pointerbytes = __pt.bytes();
		this.pointermask = __pt.mask();
	}
	
	/**
	 * Attempts to allocate the given amount of bytes and returns 
	 *
	 * @param __b The number of bytes to allocate.
	 * @return The pointer to the table pointer (or object), information such
	 * as the actually used data pointer address can be obtained from the
	 * table pointer data.
	 * @throws IllegalArgumentException If the number of bytes to allocate is
	 * zero or negative.
	 * @throws PoolOutOfMemoryException If there is no more free space within
	 * the memory pool.
	 * @since 2016/06/09
	 */
	public final long allocate(long __b)
		throws IllegalArgumentException, PoolOutOfMemoryException
	{
		// {@squirreljme.error BW01 Cannot allocate zero or a negative number
		// of bytes.}
		if (__b <= 0)
			throw new IllegalArgumentException("BW01");
		
		// Go through all pools and look for a candidate
		try
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Returns the data type to use for pointer based values.
	 *
	 * @return The pointer data type to use.
	 * @since 2016/06/08
	 */
	public final PointerType pointerType()
	{
		return this.pointertype;
	}
}

