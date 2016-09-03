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
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.JITVariableType;

/**
 * This is a class which is used to allocate and manage native registers
 * which are available for a given CPU.
 *
 * @since 2016/08/30
 */
public class GenericAllocator
{
	/** The configuration used. */
	protected final JITOutputConfig.Immutable config;
	
	/** The ABI used. */
	protected final GenericABI abi;
	
	/**
	 * Initializes the register allocator using the specified configuration
	 * and the given ABI.
	 *
	 * @param __conf The configuration being used.
	 * @param __abi The ABI used on the target system.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	GenericAllocator(JITOutputConfig.Immutable __conf, GenericABI __abi)
		throws NullPointerException
	{
		// Check
		if (__conf == null || __abi == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
		this.abi = __abi;
	}
	
	/**
	 * Returns the ABI that is used for this.
	 *
	 * @return The used ABI.
	 * @since 2016/09/03
	 */
	public final GenericABI abi()
	{
		return this.abi;
	}
	
	/**
	 * Primes the method arguments and sets the initial state that is used
	 * on entry of a method.
	 *
	 * @param __t The arguments to the method.
	 * @throws JITException If they could not be primed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/03
	 */
	public void primeArguments(JITVariableType[] __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Records the current state of the allocator.
	 *
	 * @return The allocator state.
	 * @since 2016/09/03
	 */
	public final GenericAllocatorState recordState()
	{
		throw new Error("TODO");
	}
	
	/**
	 * This registers temporary and local variable slots which are assigned
	 * to registers in a method.
	 *
	 * @param __stack The number of stack entries.
	 * @param __locals The number of local variables.
	 * @throws JITException If they could not be counted.
	 * @since 2016/09/03
	 */
	public final void variableCounts(int __stack, int __locals)
		throws JITException
	{
		// {@squirreljme.error BA1h The number of stack and/or local variables
		// has a negative count. (The stack variable count; The local variable
		// count)}
		if (__stack < 0 || __locals < 0)
			throw new JITException(String.format("BA1h %d %d", __stack,
				__locals));
		
		throw new Error("TODO");
	}
}

