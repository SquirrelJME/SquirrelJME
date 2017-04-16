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

/**
 * This class is used to store registers. Since representing some values may
 * require multiple registers to be used, this makes a single type safe means
 * of having multiple registers specified. This also simplifies changing which
 * registers are used.
 *
 * This class is immutable.
 *
 * @since 2017/04/16
 */
public final class RegisterList
{
	/**
	 * This initializes the register list using the given array for registers.
	 *
	 * @param __r The registers to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/16
	 */
	public RegisterList(Register[] __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * This initializes the register list using specified registers.
	 *
	 * @param __r The first register to use.
	 * @param __mr The remaining registers to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/16
	 */
	public RegisterList(Register __r, Register... __mr)
		throws NullPointerException
	{
		// Check
		if (__r == null || __mr == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * This initializes the register list using the given iterable for
	 * registers.
	 *
	 * @param __r The registers to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/16
	 */
	public RegisterList(Iterable<Register> __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/16
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/16
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/16
	 */
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
}

