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
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void addSaved(GenericRegister __r)
		throws NullPointerException
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
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void addTemporary(GenericRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

