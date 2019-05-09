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

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.annotation.ProgrammerTip;

/**
 * This is the base class for all list types.
 *
 * @since 2018/12/07
 */
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
		return new __ListIterator__(0);
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
		return new __ListIterator__(__i);
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
	
	@Override
	public List<E> subList(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * List iterator which can go forwards and backwards through this abstract
	 * list. Indexed elements are used here, not sequential lists.
	 *
	 * @since 2018/10/28
	 */
	private final class __ListIterator__
		implements ListIterator<E>
	{
		/** The next element to be returned. */
		private int _next;
		
		/** The current modification count, to detect modifications. */
		private int _atmod =
			AbstractList.this.modCount;
		
		/** The index to be removed. */
		private int _rmdx =
			-1;
		
		/**
		 * Initializes the list iterator.
		 *
		 * @param __i The index to use.
		 * @throws IndexOutOfBoundsException If the index is outside the list
		 * bounds.
		 * @since 2018/10/28
		 */
		__ListIterator__(int __i)
			throws IndexOutOfBoundsException
		{
			if (__i < 0 || __i > AbstractList.this.size())
				throw new IndexOutOfBoundsException("IOOB");
			
			this._next = __i;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/28
		 */
		@Override
		public final void add(E __a)
		{
			// Check modification
			this.__checkConcurrent();
			
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/28
		 */
		@Override
		public final boolean hasNext()
		{
			// Check modification
			this.__checkConcurrent();
			
			// There are elements as long as the next one is below the size
			return this._next < AbstractList.this.size();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/28
		 */
		@Override
		public final boolean hasPrevious()
		{
			// Check modification
			this.__checkConcurrent();
			
			// As long as this is not the first element there will be
			// previous ones
			return this._next > 0;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/28
		 */
		@Override
		public final E next()
			throws NoSuchElementException
		{
			// Check modification
			this.__checkConcurrent();
			
			// End of list?
			int next = this._next;
			if (this._next >= AbstractList.this.size())
				throw new NoSuchElementException("NSEE");
			
			// Get this element
			E rv = AbstractList.this.get(next);
			
			// Next one is after this, also the element to be removed is set
			// by this method
			this._rmdx = next;
			this._next = next + 1;
			
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/28
		 */
		@Override
		public final int nextIndex()
		{
			// Check modification
			this.__checkConcurrent();
			
			return this._next;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/28
		 */
		@Override
		public final E previous()
			throws NoSuchElementException
		{
			// Check modification
			this.__checkConcurrent();
			
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/28
		 */
		@Override
		public final int previousIndex()
		{
			// Check modification
			this.__checkConcurrent();
			
			// If next is zero then this would be -1
			return this._next - 1;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/28
		 */
		@Override
		public final void remove()
		{
			// Check modification
			this.__checkConcurrent();
			
			// {@squirreljme.error ZZ1k No previously returned element was
			// iterated, it was already removed, or an element was added.}
			int rmdx = this._rmdx;
			if (rmdx < 0)
				throw new IllegalStateException("ZZ1k");
			
			// Remove this index
			this._rmdx = -1;
			AbstractList.this.remove(rmdx);
			
			// Next element would be moved down
			int next = this._next;
			if (next > rmdx)
				this._next = next - 1;
			
			// Set new modification count
			this._atmod = AbstractList.this.modCount;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/28
		 */
		@Override
		public final void set(E __v)
		{
			// Check modification
			this.__checkConcurrent();
			
			throw new todo.TODO();
		}
		
		/**
		 * Checks if the list was concurrently modified.
		 *
		 * @throws ConcurrentModificationException If it was modified.
		 * @since 2018/10/29
		 */
		private final void __checkConcurrent()
			throws ConcurrentModificationException
		{
			// {@squirreljme.error ZZ1l List has been concurrently modified.}
			if (this._atmod != AbstractList.this.modCount)
				throw new ConcurrentModificationException("ZZ1l");
		}
	}
}

