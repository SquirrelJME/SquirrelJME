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
 * This is used to store the information such as which registers and the stack
 * offsets which are used to store a given argument on initial entry of a
 * method and when calling it.
 *
 * @since 2017/03/20
 */
public final class ArgumentAllocation
{
	/**
	 * Initializes the argument allocation with the specified registers.
	 *
	 * @param __t The type of data stored in the argument.
	 * @param __r The registers used to store the value.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/20
	 */
	public ArgumentAllocation(DataType __t, Register... __r)
		throws NullPointerException
	{
		// Check
		if (__t == null || __r == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Initializes the argument allocation with the specified stack offset.
	 *
	 * @param __t The type of data stored in the argument.
	 * @param __so The stack offset of the argument.
	 * @throws IllegalArgumentException If the stack offset is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/20
	 */
	public ArgumentAllocation(DataType __t, int __so)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ED0j Invalid stack offset specified.}
		if (__so == Integer.MIN_VALUE)
			throw new IllegalArgumentException("ED0j");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/20
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Obtains the register at the given index.
	 *
	 * @param <R> The class to cast to.
	 * @param __cl The class to cast to.
	 * @param __i The register index to get.
	 * @return The register at this index.
	 * @throws ClassCastException If the class type is not valid.
	 * @throws IndexOutOfBoundsException If the register is out of bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/20
	 */
	public <R extends Register> R getRegister(Class<R> __cl, int __i)
		throws ClassCastException, IndexOutOfBoundsException,
			NullPointerException
	{
		// Check
		if (__i < 0 || __i >= numRegisters())
			throw new IndexOutOfBoundsException("IOOB");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/20
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the number of registers used.
	 *
	 * @return The register use count.
	 * @since 2017/03/20
	 */
	public int numRegisters()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the stack offset of the argument.
	 *
	 * @return The stack offset or {@link Integer#MIN_VALUE} if it is not
	 * valid.
	 * @since 2017/03/20
	 */
	public int stackOffset()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/20
	 */
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
}

