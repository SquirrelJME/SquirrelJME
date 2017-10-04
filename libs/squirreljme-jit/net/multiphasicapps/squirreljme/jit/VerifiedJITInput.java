// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This class contains input for the JIT which has completely been verified
 * to be correct and well forming. After the verification step, the compiler
 * needs to perform far less verification actions to determine if things are
 * correct.
 *
 * This is an optimization which performs all verification in a single step
 * which means that it can fail fast. It also means code generation is faster
 * and there does not need to guessing as to what is valid and what is not.
 * Essentially, during the compilation phase things can be taken for
 * granted.
 *
 * @since 2017/10/03
 */
public final class VerifiedJITInput
{
	/**
	 * Initializes the verified input.
	 *
	 * @since 2017/10/03
	 */
	private VerifiedJITInput()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Verifies the input and returns a verified input
	 *
	 * @param __i The input for the JIT.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/03
	 */
	public static VerifiedJITInput verify(JITInput __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

