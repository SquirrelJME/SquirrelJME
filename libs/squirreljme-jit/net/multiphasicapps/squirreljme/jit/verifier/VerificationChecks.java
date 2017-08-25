// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.verifier;

import net.multiphasicapps.squirreljme.jit.java.ClassName;

/**
 * This class is used to contain all of the checks that need to be performed
 * in order for the verifier to completley pass. This contains a set of
 * {@link VerificationCheck} instances which can be used to to ensure that
 * the output binary will meet the specifications of the virtual machine.
 *
 * @see VerificationCheck
 * @since 2017/08/24
 */
public final class VerificationChecks
{
	/**
	 * Adds a check if the given class can extend the other class.
	 *
	 * @param __t The class doing the extending.
	 * @param __o The class to be extended.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/25
	 */
	public final void canExtend(ClassName __t, ClassName __o)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Adds a check if the given class can implement the other class.
	 *
	 * @param __t The class doing the implementing.
	 * @param __o The class to be implemented.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/25
	 */
	public final void canImplement(ClassName __t, ClassName __o)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
}

