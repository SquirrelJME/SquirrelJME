// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.linkage;

/**
 * This represents the target of an invocation based on a given invocation
 * type.
 *
 * @since 2017/04/01
 */
public final class MethodInvokeTarget
{
	/**
	 * Initializes the invocation target.
	 *
	 * @param __to The target reference.
	 * @param __t The type of invocation to perform.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/01
	 */
	public MethodInvokeTarget(MethodReference __to, MethodInvokeType __t)
		throws NullPointerException
	{
		// Check
		if (__to == null || __t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
}

