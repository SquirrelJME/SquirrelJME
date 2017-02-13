// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This class manages register and stack allocations and is used to determine
 * where and how to store variables for computational and storage use.
 *
 * Instances of this class would also be created when a call to a method is
 * required to be performed.
 *
 * @since 2017/02/11
 */
public abstract class CellAllocator
{
	/**
	 * This is used to grant a single purpose or multiple purposes to the
	 * specified register. This tells the allocator how the registers should
	 * be handled by the compiler.
	 *
	 * @param __r The register to define a purpose for.
	 * @param __p The purposes to give the specified register.
	 * @throws JITException If the register could not be defined.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/13
	 */
	protected final void defineRegister(Register __r, RegisterPurpose... __p)
		throws JITException, NullPointerException
	{
		// Check
		if (__r == null || __p == null || __p.length <= 0)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

