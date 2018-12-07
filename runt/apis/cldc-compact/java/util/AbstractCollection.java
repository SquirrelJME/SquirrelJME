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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public boolean add(E __a)
	{
		throw new UnsupportedOperationException("RORO");
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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/05
	 */
	@Override
	public void clear()
	{
		for (Iterator<E> it = this.iterator(); it.hasNext();)
		{
			// Remove is always after a next
			it.next();
			it.remove();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public boolean contains(Object __v)
	{
		// Slow as it checks each entry
		for (E e : this)
			if (__v == null ? e == null : __v.equals(e))
				return true;
		
		// Not found
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public boolean containsAll(Collection<?> __c)
		throws NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Check each entry
		int total = 0,
			found = 0;
		for (Object e : __c)
		{
			total++;
			if (this.contains(e))
				found++;
		}
		
		return found == total;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public boolean isEmpty()
	{
		return this.size() == 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public boolean remove(Object __v)
	{
		throw new UnsupportedOperationException("RORO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public boolean removeAll(Collection<?> __c)
		throws NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Remove things through our own iterator
		boolean did = false;
		for (Iterator<E> it = this.iterator(); it.hasNext();)
		{
			E e = it.next();
			
			// If it is in the collection, remove it
			if ((did |= __c.contains(e)))
				it.remove();
		}
		
		return did;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public boolean retainAll(Collection<?> __c)
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Remove things through our own iterator
		boolean did = false;
		for (Iterator<E> it = this.iterator(); it.hasNext();)
		{
			E e = it.next();
			
			// If it is not in the collection, remove it
			if ((did |= (!__c.contains(e))))
				it.remove();
		}
		
		return did;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public Object[] toArray()
	{
		int n = this.size();
		Object[] rv = new Object[n];
		int i = 0;
		for (Iterator<E> it = this.iterator(); it.hasNext();)
			rv[i++] = it.next();
		
		return rv;
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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/29
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("[");
		
		// Build string
		boolean comma = false;
		for (Iterator<E> it = this.iterator(); it.hasNext();)
		{
			// Add comma
			if (comma)
				sb.append(", ");
			comma = true;
			
			// Add item
			sb.append(it.next());
		}
		
		sb.append("]");
		return sb.toString();
	}
}

