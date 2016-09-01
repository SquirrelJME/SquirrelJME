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
import net.multiphasicapps.squirreljme.jit.generic.GenericRegister;
import net.multiphasicapps.squirreljme.jit.JITVariableType;

/**
 * This represents a MIPS CPU register.
 *
 * @since 2016/09/01
 */
public final class MIPSRegister
	implements GenericRegister
{
	/**
	 * {@inheritDoc}
	 * @since 2016/09/01
	 */
	@Override
	public JITVariableType floatType()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/01
	 */
	@Override
	public JITVariableType intType()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Obtains a MIP register.
	 *
	 * @param __t The triplet which is used to get the bit size.
	 * @param __n The register number.
	 * @param __f If {@code true} then the register is a floating point one.
	 * @throws JITException If the register number is out of bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public static MIPSRegister of(JITTriplet __t, int __n, boolean __f)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BV01 The specified register number is not valid
		// for MIPS. (The register number)}
		if (__n < 0 || __n > 32)
			throw new JITException(String.format("BV01 %d", __n));
		
		throw new Error("TODO");
	}
}

