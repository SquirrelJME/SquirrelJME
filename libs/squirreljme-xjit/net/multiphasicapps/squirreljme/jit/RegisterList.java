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

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * This class is used to store registers. Since representing some values may
 * require multiple registers to be used, this makes a single type safe means
 * of having multiple registers specified. This also simplifies changing which
 * registers are used.
 *
 * A register may only be specified once.
 *
 * This class is immutable.
 *
 * @since 2017/04/16
 */
public final class RegisterList
	extends AbstractCollection<Register>
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
		this(Arrays.<Register>asList(
			Objects.<Register[]>requireNonNull(__r, "NARG")));
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
		this(__combine(__r, __mr));
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
	 * @since 2017/04/19
	 */
	@Override
	public Iterator<Register> iterator()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/19
	 */
	@Override
	public int size()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Combines into a single array.
	 *
	 * @param __r The first value.
	 * @param __mr The remaining values.
	 * @return The combined 
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/19
	 */
	private static Register[] __combine(Register __r, Register... __mr)
		throws NullPointerException
	{
		// Check
		if (__r == null || __mr == null)
			throw new NullPointerException("NARG");
		
		// Merge into a single array
		int n = __mr.length;
		Register[] rv = new Register[n + 1];
		rv[0] = __r;
		for (int i = 0, j = 1; i < n; i++, j++)
			rv[j] = __mr[i];
		
		return rv;
	}
}

