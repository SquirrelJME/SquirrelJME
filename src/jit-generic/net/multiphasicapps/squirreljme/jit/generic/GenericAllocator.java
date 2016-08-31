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
public abstract class GenericAllocator
{
	/**
	 * Initializes the generic register allocator with the given set of
	 * registers.
	 *
	 * @param __stack The stack register
	 * @param __dir The direction the stack moves in, if {@code true} then
	 * it goes from higher addresses to lower addresses, if {@code false} it
	 * moves from lower addresses to higher addresses.
	 * @param __pool The constant pool register which is used to access
	 * constant pool linking data.
	 * @param __meth The current method of execution.
	 * @param __gp General purpose registers, used for input arguments and
	 * general method work.
	 * @throws JITException If no general purpose registers were specified or
	 * a register is specified multiple times.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/31
	 */
	public GenericAllocator(GenericRegister __stack, boolean __dir,
		GenericRegister __pool, GenericRegister __meth, GenericRegister[] __gp)
		throws JITException, NullPointerException
	{
		// Check
		if (__stack == null || __pool == null || __meth == null ||
			__gp == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

