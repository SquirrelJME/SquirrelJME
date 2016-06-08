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

/**
 * This interface represents a standard memory pool which is used by a manager
 * to provide access to areas of memory.
 *
 * @since 2016/06/05
 */
public interface MemoryPool
	extends AtomicOperations, ReadableMemory, WritableMemory
{
	/**
	 * This is the number of bytes which are reserved at the start of every
	 * memory pool. The reserved space is used for fallback atomic operations
	 * in the event that they are not supported. The space is also used to
	 * indicate the number of object handles have been allocated.
	 *
	 * 0 : Pool atomic flag. If a native atomic operation is not supported then
	 *     this value will be used to implement it.
	 * 4 : Not used.
	 * 8 : The number of locks on the pool (atomically modified and used also
	 *     by the garbage collector).
	 * 12: The object allocation count.
	 */
	public static final long RESERVED_SPACE =
		16L;
	
	/**
	 * Returns the base address and location of this memory pool.
	 *
	 * The value returned here must be treated as an unsigned value.
	 *
	 * @retrun The pool's base address, the handling of this value is not
	 * signed.
	 * @since 2016/06/08
	 */
	public abstract long baseAddress();
	
	/**
	 * Returns the number of bytes which are available in this pool.
	 *
	 * @return The number of available bytes.
	 * @since 2016/06/05
	 */
	public abstract long size();
}

