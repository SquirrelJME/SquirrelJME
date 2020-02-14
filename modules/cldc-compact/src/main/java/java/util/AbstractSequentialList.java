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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public abstract ListIterator<E> listIterator(int __i);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/29
	 */
	@Override
	public void add(int __i, E __v)
	{
		// Just adds the entry at the given position
		this.listIterator(__i).add(__v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public boolean addAll(int __i, Collection<? extends E> __c)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		boolean mod = false;
		ListIterator<E> li = this.listIterator(__i);
		for (E e : __c)
		{
			li.add(e);
			li.next();
			
			// Modified
			mod = true;
		}
		
		return mod;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public E get(int __i)
	{
		return this.listIterator(__i).next();
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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public E remove(int __i)
	{
		ListIterator<E> li = this.listIterator(__i);
		E rv = li.next();
		li.remove();
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public E set(int __i, E __v)
	{
		ListIterator<E> li = this.listIterator(__i);
		E rv = li.next();
		li.set(__v);
		return rv;
	}
}

