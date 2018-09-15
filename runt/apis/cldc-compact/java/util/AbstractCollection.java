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
 * This is a basic implementation of a {@link Collection}.
 *
 * Sub-classes are only required to implement {@link #iterator()} and
 * {@link #size()}.
 *
 * To support adding elements {@link add(Object)} must be implemented.
 *
 * To support removing elements {@link Iterator#remove()} must be implemented.
 *
 * @param <E> The element type.
 * @since 2018/09/15
 */
public abstract class AbstractCollection<E>
	implements Collection<E>
{
	/**
	 * Constructor which requires a sub-class to exist.
	 *
	 * @since 2018/09/15
	 */
	protected AbstractCollection()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public abstract Iterator<E> iterator();
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public abstract int size();
	
	@Override
	public boolean add(E __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean addAll(Collection<? extends E> __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public void clear()
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean contains(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean containsAll(Collection<?> __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean isEmpty()
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean remove(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean removeAll(Collection<?> __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean retainAll(Collection<?> __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public Object[] toArray()
	{
		throw new todo.TODO();
	}
	
	@Override
	public <T> T[] toArray(T[] __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
}

