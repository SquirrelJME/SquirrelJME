// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import net.multiphasicapps.util.unsigned.UnsignedLong;

/**
 * This class is used for managing pools of memory which may be available for
 * SquirrelJME to use.
 *
 * @since 2016/06/05
 */
public abstract class MemoryPoolManager
{
	/** This is used for comparing the base address of memory pools. */
	public static final Comparator<MemoryPool> COMPARE_POOL_BASE_ADDRESS =
		new Comparator<MemoryPool>()
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/06/08
			 */
			@Override
			public int compare(MemoryPool __a, MemoryPool __b)
			{
				return UnsignedLong.compareUnsignedUnsigned(__a.baseAddress(),
					__b.baseAddress());
			}
		};
	
	/** The object to lock on when using the pool manager. */
	protected final Object lock =
		new Object();
	
	/** The memory pools which are available. */
	private final List<MemoryPool> _pools =
		new ArrayList<>();
	
	/**
	 * Initializes the memory pool manager.
	 *
	 * @since 2016/06/08
	 */
	public MemoryPoolManager()
	{
	}
	
	/**
	 * This is called by the pool manager to allocate more memory.
	 *
	 * @param __sz The number of bytes to allocate.
	 * @return The newly allocated memory pool.
	 * @throws MemoryIOException If the memory pool could not be allocated.
	 * @since 2016/06/08
	 */
	protected abstract MemoryPool internalAllocate(long __sz)
		throws MemoryIOException;
	
	/**
	 * Returns a memory pool which is associated with the given linear sequence
	 * starting from zero.
	 *
	 * It is permittable for different memory pools to be returned with each
	 * given index, this may happen in the event that a memory pool is freed.
	 *
	 * @param __i The index to get.
	 * @return The memory pool at the given index.
	 * @throws NoSuchMemoryPoolException If no pool is associated with the
	 * given index.
	 * @since 2016/06/09
	 */
	public final MemoryPool get(int __i)
		throws NoSuchMemoryPoolException
	{
		// {@squirreljme.error BT01 Negative indices have no associated pool.}
		if (__i < 0)
			throw new NoSuchMemoryPoolException("BT01");
		
		// Lock
		synchronized (this.lock)
		{
			// Loop to clear out any potentially removed pools.
			List<MemoryPool> pools = this._pools;
			for (;;)
			{
				// {@squirreljme.error BT02 No pools exceed the given index.}
				int n = pools.size();
				if (__i >= n)
					throw new NoSuchMemoryPoolException("BT02");
			
				// Return the pool at the index
				MemoryPool rv = pools.get(__i);
				
				// If the pool was removed then remove the pool at this index
				if (rv == null)
				{
					pools.remove(__i);
					continue;
				}
				
				// Use it
				return rv;
			}
		}
	}
	
	/**
	 * Returns the locking object to use in the memory pool manager (the object
	 * to lock on).
	 *
	 * @return The locking object.
	 * @since 2016/06/08
	 */
	public final Object obtainLock()
	{
		return this.lock;
	}
	
	/**
	 * Returns the size of all memory pools which are managed by this manager.
	 *
	 * @return The total memory pool size.
	 * @since 2016/06/08
	 */
	public final long size()
	{
		// Lock
		synchronized (lock)
		{
			// Count the total bytes in the pool
			long total = 0L;
			for (MemoryPool mp : this._pools)
			{
				// Do not accept negative sizes
				long sz = mp.size();
				if (sz >= 0)
				{
					// Never exceed 8 EiB
					long next = total + sz;
					if (next < total)
						total = Long.MAX_VALUE;
				
					// Set new total
					total = next;
				}
			}
			
			// Return
			return total;
		}
	}
}

