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

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.squirreljme.jit.JITObjectProperties;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This contains the information on a given ABI such as which registers are
 * used and the purpose of their usage.
 *
 * @since 2016/09/01
 */
public final class GenericABI
	implements JITObjectProperties
{
	/**
	 * Initializes the ABI from the given builder.
	 *
	 * @param __b The builder creating this.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	GenericABI(GenericABIBuilder __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the list of registers that are used for passing method
	 * arguments.
	 *
	 * @param __k The kind of registers used.
	 * @return The list of argument registers.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final List<GenericRegister> arguments(GenericRegisterKind __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Checks if the specified register is a callee saved register.
	 *
	 * @param __r The register to check if it is callee saved.
	 * @return {@code true} then it is calle saved.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public boolean isSaved(GenericRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Checks if the specified register is a caller saved register.
	 *
	 * @param __r The register to check if it is caller saved.
	 * @return {@code true} then it is caller saved.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public boolean isTemporary(GenericRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/01
	 */
	@Override
	public final String[] properties()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the list of registers which are used to store the return value
	 * for when a method is called.
	 *
	 * @param __k The kinds of registers to get.
	 * @return The list of return value registers.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final List<GenericRegister> result(GenericRegisterKind __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the list of saved registers which must be preserved across
	 * method calls. These registers are callee saved.
	 *
	 * @param __k The kinds of registers to get.
	 * @return The list of saved registers.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final List<GenericRegister> saved(GenericRegisterKind __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the list of registers which are considered temporary and are not
	 * saved across method calls. These registers are caller saved.
	 *
	 * @param __k The kind of registers used.
	 * @return The list of temporary registers.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final List<GenericRegister> temporary(GenericRegisterKind __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

