// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * Descending iterator over the linked list.
 *
 * @param <E> The class type.
 * @since 2019/01/20
 */
final class __DescendingIteratorViaListIterator__<E>
	implements Iterator<E>
{
	/** The list iterator to use. */
	protected final ListIterator<E> it;
	
	/**
	 * Initializes the descending iterator.
	 *
	 * @param __it The input iterator.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/20
	 */
	__DescendingIteratorViaListIterator__(ListIterator<E> __it)
		throws NullPointerException
	{
		if (__it == null)
			throw new NullPointerException("NARG");
		
		this.it = __it;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/01/20
	 */
	@Override
	public final boolean hasNext()
	{
		return this.it.hasPrevious();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/01/20
	 */
	@Override
	public final E next()
	{
		return this.it.previous();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/01/20
	 */
	@Override
	public final void remove()
	{
		this.it.remove();
	}
}

