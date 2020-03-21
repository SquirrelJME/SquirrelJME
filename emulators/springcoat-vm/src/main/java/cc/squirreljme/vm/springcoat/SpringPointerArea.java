// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

/**
 * This represents a new area where pointers can go.
 *
 * @since 2019/12/21
 */
public final class SpringPointerArea
	implements Comparable<SpringPointerArea>
{
	/** Null pointer. */
	public static final SpringPointerArea NULL =
		new SpringPointerArea(0, 0);
	
	/** The base address. */
	public final int base;
	
	/** The length of the area. */
	public final int length;
	
	/**
	 * Initializes the pointer area.
	 *
	 * @param __b The basee.
	 * @param __l The length.
	 * @throws IllegalArgumentException If the length is negative.
	 * @since 2019/12/21
	 */
	public SpringPointerArea(int __b, int __l)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BK3f Cannot have a negative length pointer
		// area.}
		if (__l < 0)
			throw new IllegalArgumentException("BK3f");
		
		this.base = __b;
		this.length = __l;
	}
	
	/**
	 * Returns the base pointer.
	 *
	 * @return The base pointer.
	 * @since 2020/03/13
	 */
	public final SpringPointer base()
	{
		return new SpringPointer(this.base);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/21
	 */
	@Override
	public final int compareTo(SpringPointerArea __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/21
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/21
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/21
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

