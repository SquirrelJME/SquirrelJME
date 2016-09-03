// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

/**
 * This contains a state for the register allocator which is used in the
 * branch processor so that the state that has been previously set can be
 * matched when jumping to that location.
 *
 * This class is immutable.
 *
 * @since 2016/08/31
 */
public final class GenericAllocatorState
{
	/**
	 * Snapshots the state of the allocator.
	 *
	 * @param __ga The allocator to snapshot from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/03
	 */
	GenericAllocatorState(GenericAllocator __ga)
		throws NullPointerException
	{
		// Check
		if (__ga == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

