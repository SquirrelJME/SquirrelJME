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

/**
 * Returns the size formulas for allocation.
 *
 * @since 2023/12/14
 */
public enum AllocSizeOf
{
	/** Rom suite functions. */
	ROM_SUITE_FUNCTIONS(1),
	
	/** Reserved pool. */
	RESERVED_POOL(2),
	
	/* End. */
	;
	
	/** The enumeration ID. */
	protected final int id;
	
	/**
	 * Initializes the allocation size handler.
	 *
	 * @param __id The enumeration ID.
	 * @since 2023/12/14
	 */
	AllocSizeOf(int __id)
	{
		this.id = __id;
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
	 * @throws VMException If the size could not be determined.
	 * @since 2023/12/14
	 */
	public final int size(int __count)
		throws IllegalArgumentException, VMException
	{
		if (__count < 0)
			throw new IllegalArgumentException("Negative size.");
		
		return AllocSizeOf.__size(this.id, __count);
	}
	
	/**
	 * Returns the size of the given type.
	 *
	 * @param __id The type identifier.
	 * @param __count The number of entries to use.
	 * @return The resultant size.
	 * @throws VMException If it could not be calculated.
	 * @since 2023/12/14
	 */
	private static native int __size(int __id, int __count)
		throws VMException;
}
