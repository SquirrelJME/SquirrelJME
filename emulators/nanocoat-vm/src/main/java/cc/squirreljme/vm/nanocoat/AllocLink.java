// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

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
	 * @since 2023/12/14
	 */
	public int size()
	{
		throw Debugging.todo();
	}
}
