// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents the basic verification target for a jump target which does
 * not have any associated allocation information. This is used to quickly
 * verify that a jump from another region of code is valid.
 *
 * @since 2017/05/20
 */
public final class BasicVerificationTarget
{
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the verification target state.
	 *
	 * @param __stack The stack state.
	 * @param __top The top of the stack.
	 * @param __locals The local variables.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/20
	 */
	public BasicVerificationTarget(JavaType[] __stack, int __top,
		JavaType[] __locals)
		throws NullPointerException
	{
		// Check
		if (__stack == null || __locals == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/20
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/20
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/20
	 */
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
}

