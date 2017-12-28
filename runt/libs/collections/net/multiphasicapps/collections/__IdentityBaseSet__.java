// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * This is the base class for the identity set.
 *
 * @param <T> The type of element to store in the set.
 * @since 2017/12/28
 */
abstract class __IdentityBaseSet__<T>
	extends AbstractSet<T>
{
	/** The set to use as a backing storage. */
	private final Set<__IdentityWrapper__<T>> _backing;
	
	/**
	 * Initializes the base set using the given backing set.
	 *
	 * @param __back The set to back with.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	__IdentityBaseSet__(Set<__IdentityWrapper__<T>> __back)
		throws NullPointerException
	{
		if (__back == null)
			throw new NullPointerException("NARG");
		
		this._backing = __back;
	}
	
	/**
	 * Initializes the base set using the given backing set.
	 *
	 * @param __back The set to back with.
	 * @param __from The collection to source data from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	__IdentityBaseSet__(Set<__IdentityWrapper__<T>> __back,
		Collection<? extends T> __from)
		throws NullPointerException
	{
		if (__back == null || __from == null)
			throw new NullPointerException("NARG");
		
		this._backing = __back;
		this.addAll(__from);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public final boolean add(T __t)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public final void clear()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public final boolean contains(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public final boolean containsAll(Collection<?> __c)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public final boolean isEmpty()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public final Iterator<T> iterator()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public final boolean remove(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public final boolean removeAll(Collection<?> __c)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public final boolean retainAll(Collection<?> __c)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public final int size()
	{
		throw new todo.TODO();
	}
}

