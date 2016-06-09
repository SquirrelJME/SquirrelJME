// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

import net.multiphasicapps.squirreljme.memory.AbstractMemoryPool;
import net.multiphasicapps.squirreljme.memory.MemoryIOException;
import net.multiphasicapps.squirreljme.memory.MemoryPool;
import net.multiphasicapps.squirreljme.memory.MemoryPoolManager;

/**
 * This the memory pool manager which is used by the interpreter.
 *
 * @since 2016/06/08
 */
@Deprecated
public class InterpreterMemoryPoolManager
	extends MemoryPoolManager
{
	/** The backing memory pool which is used on the next internal allocate. */
	protected final InterpreterMemoryPool internalpool;
	
	/** Was the internal pool returned yet? */
	private volatile boolean _used;
	
	/**
	 * Initializes the memory pool manager.
	 *
	 * @param __bytes The number of bytes to use.
	 * @param __ba The base address of the pool.
	 * @since 2016/06/08
	 */
	public InterpreterMemoryPoolManager(int __bytes, long __ba)
	{
		// Setup pool
		this.internalpool = new InterpreterMemoryPool(__bytes, __ba);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/08
	 */
	@Override
	protected final MemoryPool internalAllocate(long __sz)
		throws MemoryIOException
	{
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error AN08 The interpreter only has a single
			// memory pool which is available.}
			if (this._used)
				throw new MemoryIOException("AN08");
			
			// Mark used and return it
			this._used = true;
			return internalpool;
		}
	}
}

