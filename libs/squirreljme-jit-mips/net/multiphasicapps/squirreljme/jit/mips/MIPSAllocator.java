// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.mips;

import net.multiphasicapps.squirreljme.jit.CellAllocator;

/**
 * This is the MIPS allocator which is used to manage register and stack
 * allocations on the MIPS CPU.
 *
 * @since 2017/02/13
 */
public class MIPSAllocator
	extends CellAllocator
{
	/**
	 * Initializes the MIPS allocator.
	 *
	 * @param __config The configuration to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/13
	 */
	public MIPSAllocator(MIPSConfig __config)
		throws NullPointerException
	{
		// Check
		if (__config == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

