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
	 * Initializes a pool using a static address.
	 *
	 * @param __address The address where the storage is.
	 * @param __size The number of bytes to allocate.
	 * @throws IllegalArgumentException If the size is not valid.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException On null arguments.
	 * @since 2023/12/14
	 */
	public AllocPool(long __address, int __size)
		throws IllegalArgumentException, NullPointerException, VMException
	{
		if (__address == 0)
			throw new NullPointerException("NARG");
		
		if (__size <= 0)
			throw new IllegalArgumentException("Zero or negative size.");
		
		// Initialize a static pool
		this._pointer = AllocPool.__poolStatic(__address, __size, this);
	}
	
	/**
	 * Allocates memory.
	 *
	 * @param __size The number of bytes to allocate.
	 * @return The resultant link.
	 * @throws IllegalArgumentException If the size is zero or negative.
	 * @throws VMException If there was an error allocating the data.
	 * @since 2023/12/14
	 */
	public AllocLink alloc(int __size)
		throws IllegalArgumentException, VMException
	{
		if (__size <= 0)
			throw new IllegalArgumentException("Zero or negative size.");
		
		/** Allocate memory. */
		long block = AllocPool.__alloc(this._pointer, __size);
		return new AllocLink(block, AllocPool.__getLink(block));
	}
	
	/**
	 * Allocates the given type.
	 *
	 * @param __type The type to allocate.
	 * @return The resultant link.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the allocation could not be performed.
	 * @since 2023/12/14
	 */
	public AllocLink alloc(AllocTypeSizeOf __type)
		throws NullPointerException, VMException
	{
		return this.alloc(__type, 0);
	}
	
	/**
	 * Allocates the given type.
	 *
	 * @param __type The type to allocate.
	 * @param __count The number of elements to allocate.
	 * @return The resultant link.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the allocation could not be performed.
	 * @since 2023/12/14
	 */
	public AllocLink alloc(AllocTypeSizeOf __type, int __count)
		throws IndexOutOfBoundsException, NullPointerException, VMException
	{
		return this.alloc(__type.size(__count));
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
	 * Allocates memory within the pool.
	 *
	 * @param __poolPtr The pool to allocate in.
	 * @param __size The number of bytes to allocate.
	 * @return The resultant pool pointer.
	 * @throws VMException If the pointer could not be allocated.
	 * @since 2023/12/14
	 */
	private static native long __alloc(long __poolPtr, int __size)
		throws VMException;
	
	/**
	 * Returns the raw address link for the given block.
	 *
	 * @param __blockPtr The block pointer used.
	 * @return The address of the block.
	 * @throws VMException If the link could not be obtained.
	 * @since 2023/12/14
	 */
	private static native long __getLink(long __blockPtr)
		throws VMException;
	
	/**
	 * Allocates a pool via {@code malloc()} and returns the pointer to it.
	 *
	 * @param __size The size of the pool.
	 * @param __this The front end wrapper.
	 * @return The native pointer to the pool.
	 * @throws VMException If the pool could not be allocated or initialized.
	 * @since 2023/12/08
	 */
	private static native long __poolMalloc(
		@Range(from = 1, to = Integer.MAX_VALUE) int __size,
		@NotNull AllocPool __this)
		throws VMException;
	
	/**
	 * Initializes a static pool at the given address.
	 *
	 * @param __address The address to initialize at.
	 * @param __size The number of bytes to allocate for.
	 * @param __this The reference to this object for the front end.
	 * @return The pointer to the given pool.
	 * @throws VMException If the pool could not be initialized.
	 * @since 2023/12/14
	 */
	private static native long __poolStatic(long __address, int __size,
		AllocPool __this)
		throws VMException;
}
