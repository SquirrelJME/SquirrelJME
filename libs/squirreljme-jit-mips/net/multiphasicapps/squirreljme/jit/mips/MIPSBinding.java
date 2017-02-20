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

import net.multiphasicapps.squirreljme.jit.Binding;

/**
 * This is a binding for MIPS register or stack values which determines where
 * values exist.
 *
 * @since 2017/02/19
 */
public class MIPSBinding
	implements Binding
{
	/**
	 * Initializes the MIPS binding to a register.
	 *
	 * @param __r The register to bind to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/20
	 */
	public MIPSBinding(MIPSRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Initializes the MIPS binding to multiple registers.
	 *
	 * @param __r The register to bind to.
	 * @param __a The other registers to bind to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/20
	 */
	public MIPSBinding(MIPSRegister __r, MIPSRegister... __a)
		throws NullPointerException
	{
		// Check
		if (__r == null || __a == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/19
	 */
	@Override
	public MIPSBinding copy()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/19
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/19
	 */
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}
}

