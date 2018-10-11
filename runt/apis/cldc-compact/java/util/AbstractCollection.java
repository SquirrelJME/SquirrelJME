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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public boolean addAll(Collection<? extends E> __c)
		throws NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Add but we also need to keep track if the underlying collection
		// was actually changed
		boolean changed = false;
		for (E e : __c)
			changed |= this.add(e);
		
		return changed;
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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public <T> T[] toArray(T[] __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Use to check or create a new array of the given type
		int size = this.size();
		
		// Only copy elements if there are any to copy, prevents creation
		// of the iterator
		if (size > 0)
		{
			// If the array is too small, reallocate it to fit
			if (__v.length < size)
				throw new todo.TODO();
			
			// Copy elements based on the iteration order, just ignore the
			// class and hope it works
			int o = 0;
			for (E e : this)
				__v[o++] = (T)e;
		}
		
		// The element at the end of the array, if there is room is set to
		// null
		if (__v.length > size)
			__v[size] = null;
		
		return __v;
	}
	
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
}

