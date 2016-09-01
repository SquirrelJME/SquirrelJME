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

import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This is a class which is used to allocate and manage native registers
 * which are available for a given CPU.
 *
 * @since 2016/08/30
 */
public class GenericAllocator
{
	/** The stack register selected. */
	protected final GenericRegister stack;
	
	/** The direction the stack moves in. */
	protected final boolean direction;
	
	/**
	 * Initializes the generic register allocator with the given set of
	 * registers.
	 *
	 * @param __stack The stack register
	 * @param __dir The direction the stack moves in, if {@code true} then
	 * it goes from higher addresses to lower addresses, if {@code false} it
	 * moves from lower addresses to higher addresses.
	 * @param __gp General purpose registers, used for input arguments and
	 * general method work.
	 * @throws JITException If no general purpose registers were specified or
	 * a register is specified multiple times.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/31
	 */
	public GenericAllocator(GenericRegister __stack, boolean __dir,
		GenericRegister[] __gp)
		throws JITException, NullPointerException
	{
		// Check
		if (__stack == null || __gp == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BA1b No general purpose registers specified.}
		if (__gp.length <= 0)
			throw new JITException("BA1b");
		
		// Set
		this.stack = __stack;
		this.direction = __dir;
		
		throw new Error("TODO");
	}
}

