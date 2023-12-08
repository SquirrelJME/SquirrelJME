// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Native allocation pool.
 *
 * @since 2023/12/08
 */
public final class AllocPool
	implements Pointer
{
	/** The pointer to the native pool. */
	private final long _pointer;
	
	/**
	 * Initializes the allocation pool.
	 *
	 * @param __size The size of the pool.
	 * @throws IllegalArgumentException If {@code __size} is zero or negative.
	 * @throws VMException If pool initialization failed.
	 * @since 2023/12/08
	 */
	public AllocPool(int __size)
		throws IllegalArgumentException, VMException
	{
		if (__size <= 0)
			throw new IllegalArgumentException("Invalid pool size: " + __size);
		
		// Allocate the native pool
		this._pointer = AllocPool.__poolMalloc(__size, this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/08
	 */
	@Override
	public long pointerAddress()
	{
		return this._pointer;
	}
	
	/**
	 * Allocates a pool via {@code malloc()} and returns the pointer to it.
	 *
	 * @param __size The size of the pool.
	 * @param __wrapper The front end wrapper.
	 * @return The native pointer to the pool.
	 * @throws VMException If the pool could not be allocated or initialized.
	 * @since 2023/12/08
	 */
	private static native long __poolMalloc(
		@Range(from = 1, to = Integer.MAX_VALUE) int __size,
		@NotNull AllocPool __wrapper)
		throws VMException;
}
