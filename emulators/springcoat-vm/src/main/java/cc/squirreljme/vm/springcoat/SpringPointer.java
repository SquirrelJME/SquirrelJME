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
 * This represents a single pointer within SpringCoat.
 *
 * @since 2020/03/11
 */
public final class SpringPointer
	implements Comparable<SpringPointer>
{
	/** The pointer value. */
	public final long pointer;
	
	/** Pointer as integer value. */
	public int pointerInt;
	
	/**
	 * Initializes the pointer.
	 *
	 * @param __a The address of the pointer.
	 * @since 2020/03/13
	 */
	public SpringPointer(long __a)
	{
		if (__a < 0 || __a > Integer.MAX_VALUE)
			throw new IllegalArgumentException(String.format(
				"Illegal pointer: %08x", __a));
		
		this.pointer = __a;
		this.pointerInt = (int)__a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/11
	 */
	@Override
	public final int compareTo(SpringPointer o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/11
	 */
	@Override
	public final int hashCode()
	{
		return this.pointerInt;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/11
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof SpringPointer))
			return false;
		
		return this.pointerInt == ((SpringPointer)__o).pointerInt;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/21
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}
