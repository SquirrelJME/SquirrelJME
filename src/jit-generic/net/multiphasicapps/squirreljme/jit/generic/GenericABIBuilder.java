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
 * This class is used to generate instances of {@link GenericABI} which is
 * used by the register allocator and the generic JIT compiler to determine
 * how other methods are called.
 *
 * @since 2016/09/01
 */
public final class GenericABIBuilder
{
	/**
	 * Adds an argument register.
	 *
	 * @param __r The register that is part of the input arguments for a
	 * method.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void addArgument(GenericRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Adds a result register.
	 *
	 * @param __r The register that is used for the return value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void addResult(GenericRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Adds a callee saved register.
	 *
	 * @param __r The register that is callee saved.
	 * @throws JITException If the register is temporary or is the stack
	 * register.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void addSaved(GenericRegister __r)
		throws JITException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Adds a caller saved register.
	 *
	 * @param __r The register that is caller saved.
	 * @throws JITException If the register is saved or is the stack register.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void addTemporary(GenericRegister __r)
		throws JITException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Builds the given ABI set.
	 *
	 * @return The generic ABI set.
	 * @throws JITException If a group contains no registers, the stack
	 * register was not set, or the direction register was not set.
	 * @since 2016/09/01
	 */
	public final GenericABI build()
		throws JITException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Sets the stack register.
	 *
	 * @param __r The register to use for the stack.
	 * @throws JITException If the register is temporary or saved.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void stack(GenericRegister __r)
		throws JITException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Sets the requires stack alignment needed for method calls.
	 *
	 * @param __i The number of bytes to align to.
	 * @throws JITException If the alignment is zero or negative.
	 * @since 2016/09/01
	 */
	public final void stackAlignment(int __i)
		throws JITException
	{
		// {@squirreljme.error BA0u The stack alignment is zero or negative.
		// (The alignment)}
		if (__i <= 0)
			throw new JITException(String.format("BA0u %d", __i));
		
		throw new Error("TODO");
	}
	
	/**
	 * Sets the direction of the stack.
	 *
	 * @param __d The stack direction.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void stackDirection(GenericStackDirection __d)
		throws NullPointerException
	{
		// Check
		if (__d == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

