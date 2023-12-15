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
 * Returns the size formulas for allocation.
 *
 * @since 2023/12/14
 */
public enum AllocTypeSizeOf
{
	/** Rom suite functions. */
	ROM_SUITE_FUNCTIONS(1),
	
	/** Reserved pool. */
	RESERVED_POOL(2),
	
	/* End. */
	;
	
	/** The enumeration ID. */
	protected final int enumId;
	
	/**
	 * Initializes the allocation size handler.
	 *
	 * @param __enumId The enumeration ID.
	 * @since 2023/12/14
	 */
	AllocTypeSizeOf(int __enumId)
	{
		this.enumId = __enumId;
	}
	
	/**
	 * Returns the allocation size with zero members.
	 *
	 * @return The resultant size.
	 * @since 2023/12/14
	 */
	public final int size()
	{
		return this.size(0);
	}
	
	/**
	 * Returns the allocation size with the given number of members.
	 *
	 * @param __count The number of members.
	 * @return The resultant size.
	 * @throws IllegalArgumentException If the count is negative.
	 * @since 2023/12/14
	 */
	public final int size(int __count)
		throws IllegalArgumentException
	{
		if (__count < 0)
			throw new IllegalArgumentException("Negative size.");
		
		throw Debugging.todo();
	}
}
