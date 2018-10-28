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

public abstract class AbstractList<E>
	extends AbstractCollection<E>
	implements List<E>
{
	/**
	 * The modification count of this list, used to detect situations
	 * where a list was modified while it was being iterated.
	 */
	protected transient int modCount;
	
	/**
	 * Constructor requiring sub-classing.
	 *
	 * @since 2018/09/15
	 */
	protected AbstractList()
	{
	}
	
	@Override
	public abstract E get(int __a);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public boolean add(E __v)
	{
		int oldsize = this.size();
		this.add(oldsize, __v);
		return this.size() != oldsize;
	}
	
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
	public void clear()
	{
		throw new todo.TODO();
	}
	
	/**
	 * This method follows the contract of the {@link List#equals(Object)}
	 * method.
	 *
	 * @param __o The object to compare against.
	 * @return If the specified object is a list and is equal to this list.
	 * @see List#equals(Object)
	 * @since 2017/11/21
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Same object, no point in comparing against self
		if (this == __o)
			return true;
		
		// The other object must be a list
		if (!(__o instanceof List))
			return false;
		
		// If either (or both) lists are not random access then it is possible
		// that there is a penalty in determining the size of the list, so
		// single entries must be considered via the iterators.
		List<?> o = (List<?>)__o;
		if (!(this instanceof RandomAccess) || !(o instanceof RandomAccess))
		{
			// Need both iterators
			Iterator<?> ai = this.iterator(),
				bi = o.iterator();
			
			// The lists may have infinite length
			while (true)
			{
				// If one list ends before the other then they are not equal
				boolean anext;
				if ((anext = ai.hasNext()) != bi.hasNext())
					return false;
				
				// Both are empty at the same time, same length
				if (!anext)
					return true;
				
				// Need these
				Object a = ai.next(),
					b = bi.next();
				
				// Nulls compare the same, but fail on mismatches
				if (a == null)
					if (b == null)
						continue;
					else
						return false;
				
				// This call should check b for null
				else if (!a.equals(b))
					return false;
			}
		}
		
		// Otherwise, a size comparison is likely not to be costly.
		else
		{
			// Equal lists always have the same size
			int an = this.size(),
				bn = o.size();
			if (an != bn)
				return false;
			
			for (int i = 0; i < an; i++)
			{
				Object a = this.get(i),
					b = o.get(i);
				
				// Nulls compare the same, but fail on mismatches
				if (a == null)
					if (b == null)
						continue;
					else
						return false;
				
				// This call should check b for null
				else if (!a.equals(b))
					return false;
			}
			
			// If this point reached, the lists are equal
			return true;
		}
	}
	
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	@Override
	public int indexOf(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public Iterator<E> iterator()
	{
		throw new todo.TODO();
	}
	
	@Override
	public int lastIndexOf(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public ListIterator<E> listIterator()
	{
		throw new todo.TODO();
	}
	
	@Override
	public ListIterator<E> listIterator(int __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public E remove(int __a)
	{
		throw new todo.TODO();
	}
	
	protected void removeRange(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	@Override
	public E set(int __a, E __b)
	{
		throw new todo.TODO();
	}
	
	@Override
	public List<E> subList(int __a, int __b)
	{
		throw new todo.TODO();
	}
}

