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

/**
 * Represents an allocation link.
 *
 * @since 2023/12/14
 */
public final class AllocLink
	implements Pointer
{
	/** The pointer to the memory block. */
	private final long _blockPtr;
	
	/** The pointer to the link block. */
	private final long _linkPtr;
	
	/**
	 * Initializes the allocation link.
	 *
	 * @param __blockPtr The block pointer address.
	 * @param __linkPtr The link pointer address.
	 * @since 2023/12/14
	 */
	AllocLink(long __blockPtr, long __linkPtr)
	{
		this._blockPtr = __blockPtr;
		this._linkPtr = __linkPtr;
	}
	
	/**
	 * Returns the link address.
	 *
	 * @return The address of the link.
	 * @since 2023/12/14
	 */
	public long linkAddress()
	{
		return this._linkPtr;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/14
	 */
	@Override
	public long pointerAddress()
	{
		return this._blockPtr;
	}
	
	/**
	 * Returns the size of the allocation link.
	 *
	 * @return The size of the link.
	 * @throws VMException If it could not be calculated.
	 * @since 2023/12/14
	 */
	public int size()
		throws VMException
	{
		return AllocLink.__size(this._linkPtr);
	}
	
	/**
	 * Returns the size of the allocation pointer. 
	 *
	 * @param __linkPtr The link pointer.
	 * @return The resultant size.
	 * @throws VMException If it could not be determined.
	 * @since 2023/12/14
	 */
	private static native int __size(long __linkPtr)
		throws VMException;
}
