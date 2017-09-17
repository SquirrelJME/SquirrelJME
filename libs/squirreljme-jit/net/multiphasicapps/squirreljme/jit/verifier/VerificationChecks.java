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

import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.squirreljme.jit.java.ClassName;
import net.multiphasicapps.squirreljme.jit.java.MethodInvocationType;
import net.multiphasicapps.squirreljme.jit.java.MethodReference;

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
	/** Current verification checks. */
	private final Set<VerificationCheck> _checks =
		new LinkedHashSet<>();
	
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
		check(new CanExtendCheck(__t, __o));
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
		check(new CanImplementCheck(__t, __o));
	}
	
	/**
	 * Adds a check to see if the given class can invoke the given method in
	 * another class.
	 *
	 * @param __srccl The class doing the invoking.
	 * @param __srcname The name of the method doing the call.
	 * @param __srcdesc The descriptor of the method doing the call.
	 * @param __t The type of invocation being performed.
	 * @param __m The target method being called.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/16
	 */
	public final void canInvoke(ClassName __srccl, MethodName __srcname,
		MethodDescriptor __srcdesc, MethodInvocationType __t,
		MethodReference __m)
		throws NullPointerException
	{
		check(new CanInvokeCheck(__srccl, __srcname, __srcdesc, __t, __m));
	}
	
	/**
	 * Adds the given check to be performed.
	 *
	 * @param __v The check to be performed.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/01
	 */
	public final void check(VerificationCheck __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		this._checks.add(__v);
		
		// Debug
		System.err.printf("DEBUG -- check(%s)%n", __v);
	}
}

