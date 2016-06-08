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
				throw new Error("TODO");
			}
		};
	
	/** The object to lock on when using the pool manager. */
	protected final Object lock =
		new Object();
	
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
	 * Returns the locking object to use in the memory pool (the object to
	 * lock on).
	 *
	 * @return The locking object.
	 * @since 2016/06/08
	 */
	public final Object obtainLock()
	{
		return this.lock;
	}
}

