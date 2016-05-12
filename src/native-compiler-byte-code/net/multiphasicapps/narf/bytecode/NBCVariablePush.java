// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.bytecode;

import java.util.List;

/**
 * This represents the type of variable to push.
 *
 * @since 2016/05/12
 */
public final class NBCVariablePush
{
	/**
	 * Pushes a new value which is an entirely new value.
	 *
	 * @param __t The type of value to push.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/12
	 */
	NBCVariablePush(NBCVariableType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Pushes a new value to the stack which is derived from a local value.
	 *
	 * @param __l The local variable to push.
	 * @throws IllegalArgumentException If the local varaible is not read.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/12
	 */
	NBCVariablePush(NBCLocalAccess __l)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__l == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Pushes an existing stack value to the stack unchanged.
	 *
	 * @param __pops The operations to pop.
	 * @param __i The index in the last being pushed.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/12
	 */
	NBCVariablePush(List<NBCVariableType> __pops, int __i)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__pops == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
}

