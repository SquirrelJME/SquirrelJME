// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.annotation.ProgrammerTip;

/**
 * This is the base class for all list types.
 *
 * @since 2018/12/07
 */
@Api
public abstract class AbstractList<E>
	extends AbstractCollection<E>
	implements List<E>
{
	/**
	 * The modification count of this list, used to detect situations
	 * where a list was modified while it was being iterated.
	 */
	@Api
	protected transient int modCount;
	
	/**
	 * Constructor requiring sub-classing.
	 *
	 * @since 2018/09/15
	 */
	@Api
	protected AbstractList()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public abstract E get(int __i)
		throws IndexOutOfBoundsException;
	
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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/29
	 */
	@Override
	@ProgrammerTip("Implement for variable sized lists.")
	public void add(int __a, E __b)
	{
		throw new UnsupportedOperationException("RORO");
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
		int n = this.size();
		if (__i < 0 || __i > n)
			throw new IndexOutOfBoundsException("NARG");
		
		// Add all elements
		for (E e : __c)
			this.add(__i++, e);
		
		// If the size changed the list was modified
		return this.size() != n;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/29
	 */
	@Override
	public void clear()
	{
		this.removeRange(0, this.size());
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
	@ImplementationNote("This method considers if the source and target " +
		"lists are RandomAccess, for more optimized comparison.")
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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public int hashCode()
	{
		int rv = 1;
		for (E e : this)
			rv = 31 * rv + (e == null ? 0 : e.hashCode());
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public int indexOf(Object __v)
	{
		for (int i = 0, n = this.size(); i < n; i++)
		{
			E e = this.get(i);
			if ((__v == null ? e == null : __v.equals(e)))
				return i;
		}
		
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public Iterator<E> iterator()
	{
		return new __AbstractListListIterator__<E>(this, 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public int lastIndexOf(Object __v)
	{
		for (int i = this.size() - 1; i >= 0; i--)
		{
			E e = this.get(i);
			if ((__v == null ? e == null : __v.equals(e)))
				return i;
		}
		
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public ListIterator<E> listIterator()
	{
		return this.listIterator(0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public ListIterator<E> listIterator(int __i)
	{
		return new __AbstractListListIterator__<E>(this, __i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/29
	 */
	@Override
	@ProgrammerTip("Implement for variable sized lists.")
	public E remove(int __i)
	{
		throw new UnsupportedOperationException("RORO");
	}
	
	/**
	 * Removes elements from the given inclusive range to the given exclusive
	 * range.
	 *
	 * The basic implementation of this method uses the list iterator until
	 * the from index is reached, once it has been reached it will remove
	 * the given number of elements. Therefor
	 *
	 * @param __from The first element to remove, inclusive.
	 * @param __to The last element to remove, exclusive.
	 * @since 2018/10/29
	 */
	@ProgrammerTip("The basic implementation of this method is not " +
		"efficient at all, it should be reimplemented if removal is a " +
		"busy operation.")
	@Api
	protected void removeRange(int __from, int __to)
	{
		int left = __to - __from;
		ListIterator<E> li = this.listIterator(__from);
		while (left > 0)
		{
			if (!li.hasNext())
				break;
			
			// Get and remove
			li.next();
			li.remove();
			left--;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/29
	 */
	@Override
	@ProgrammerTip("Implement for modifiable lists.")
	public E set(int __i, E __v)
	{
		throw new UnsupportedOperationException("RORO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/11/30
	 */
	@Override
	public List<E> subList(int __from, int __to)
		throws IllegalArgumentException, IndexOutOfBoundsException
	{
		return new __AbstractListSubList__<E>(this, __from, __to);
	}
}

