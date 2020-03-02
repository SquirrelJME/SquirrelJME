// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This is a sub-list of an abstract list
 *
 * @param <E> The type of value being stored
 * @since 2019/11/30
 */
final class __AbstractListSubList__<E>
	extends AbstractList<E>
{
	/**
	 * Initializes the abstract sub-list.
	 *
	 * @param __list The list to wrap.
	 * @param __from The index to start from.
	 * @param __to The index to end at.
	 * @throws IllegalArgumentException If the end point is before the start.
	 * @throws IndexOutOfBoundsException If the from is negative or the to
	 * exceeds the list size.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/30
	 */
	__AbstractListSubList__(AbstractList<E> __list, int __from, int __to)
		throws IllegalArgumentException, IndexOutOfBoundsException,
			NullPointerException
	{
		if (__list == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ3r End point is before starting point.}
		if (__from > __to)
			throw new IllegalArgumentException("ZZ3r");
		
		// Check bounds
		int size = this.size();
		if (__from < 0 || __to > size)
			throw new IndexOutOfBoundsException("IOOB");
		
		throw new todo.TODO();
	}
	
	@Override
	public final void add(int __a, E __b)
	{
		throw new todo.TODO();
	}
	
	@Override
	public final E get(int __i)
		throws IndexOutOfBoundsException
	{
		throw new todo.TODO();
	}
	
	@Override
	public final E set(int __i, E __v)
		throws IndexOutOfBoundsException
	{
		throw new todo.TODO();
	}
	
	@Override
	public final E remove(int __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public final int size()
	{
		throw new todo.TODO();
	}
}

