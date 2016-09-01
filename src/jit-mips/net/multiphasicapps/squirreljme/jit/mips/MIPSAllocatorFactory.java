// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.mips;

import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.generic.GenericAllocator;
import net.multiphasicapps.squirreljme.jit.generic.
	GenericAllocatorFactory;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;

/**
 * This factory creates register allocators for the MIPS output code.
 *
 * @since 2016/08/30
 */
public class MIPSAllocatorFactory
	implements GenericAllocatorFactory
{
	/** Stack register. */
	protected final MIPSRegister stack;
	
	/** Stack direction. */
	protected final boolean direction;
	
	/** General purpose registers. */
	private final MIPSRegister[] _gprs;
	
	/**
	 * Initializes the MIPS register allocator factory.
	 *
	 * @param __t The used triplet.
	 * @param __stack The stack register
	 * @param __dir The direction the stack moves in, if {@code true} then
	 * it goes from higher addresses to lower addresses, if {@code false} it
	 * moves from lower addresses to higher addresses.
	 * @param __gp General purpose registers, used for input arguments and
	 * general method work.
	 * @throws JITException If it could not be created (likely because the
	 * CPU is not known).
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/30
	 */
	public MIPSAllocatorFactory(JITTriplet __t, MIPSRegister __stack,
		boolean __dir, MIPSRegister[] __gp)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null || __stack == null || __gp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.stack = __stack;
		this.direction = __dir;
		this._gprs = __gp.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/30
	 */
	@Override
	public GenericAllocator create()
	{
		return new GenericAllocator(this.stack, this.direction, this._gprs);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/30
	 */
	@Override
	public String[] properties()
	{
		throw new Error("TODO");
	}
}

