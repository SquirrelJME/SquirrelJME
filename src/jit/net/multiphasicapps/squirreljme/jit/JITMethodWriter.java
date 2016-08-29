// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This interface is used by the class writer to write output logic to the
 * resulting native machine code generator.
 *
 * The JIT itself converts the stack based machine to a completely register
 * based machine.
 *
 * @since 2016/08/19
 */
public interface JITMethodWriter
	extends AutoCloseable
{
	/**
	 * {@inheritDoc}
	 * @since 2016/08/19
	 */
	@Override
	public abstract void close()
		throws JITException;
	
	/**
	 * Primes the argument methods, essentially the input used for the method
	 * as it varies between methods. If the method is an instance method
	 * then the first argument will be {@link JITVariableType#OBJECT}.
	 *
	 * All of the input arguments map to virtual registers starting from
	 * {@code 0}.
	 *
	 * Any {@code null} elements in the type array specify that the area is
	 * not used and would be the top of a {@code float} or {@code double}.
	 *
	 * @param __t The variable types which are used in input.
	 * @throws JITException If the arguments could not be mapped.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/29
	 */
	public abstract void primeArguments(JITVariableType[] __t)
		throws JITException, NullPointerException;
}

