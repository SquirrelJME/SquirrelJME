// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This is the base list for classes which are optimized for sequential and
 * not randomized access.
 *
 * @param <E> The element to store in the list.
 * @since 2018/10/29
 */
public abstract class AbstractSequentialList<E>
	extends AbstractList<E>
{
	/**
	 * Initializes the base sequential list.
	 *
	 * @since 2018/10/29
	 */
	protected AbstractSequentialList()
	{
	}
	
	@Override
	public abstract ListIterator<E> listIterator(int __i);
	
	@Override
	public void add(int __a, E __b)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean addAll(int __a, Collection<? extends E> __b)
	{
		throw new todo.TODO();
	}
	
	@Override
	public E get(int __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public Iterator<E> iterator()
	{
		return this.listIterator(0);
	}
	
	@Override
	public E remove(int __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public E set(int __a, E __b)
	{
		throw new todo.TODO();
	}
}

