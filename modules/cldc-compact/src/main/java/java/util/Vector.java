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

import cc.squirreljme.runtime.cldc.util.IteratorToEnumeration;
import cc.squirreljme.runtime.cldc.util.SynchronizedIterator;

/**
 * This is similar to {@link ArrayList} except that it is synchronized and
 * thread-safe by default.
 *
 * @see ArrayList
 * @since 2019/05/13
 */
public class Vector<E>
	extends AbstractList<E>
	implements RandomAccess, Cloneable
{
	/** The number of elements to add if the size is too small. */
	protected int capacityIncrement;
	
	/** The number of elements in the vector. */
	protected int elementCount;
	
	/** The elements in the vector. */
	protected Object[] elementData;
	
	/**
	 * Initializes the vector using the specified initial capacity.
	 *
	 * @param __cap The initial capacity.
	 * @param __inc The capacity increment.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2019/05/13
	 */
	public Vector(int __cap, int __inc)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ32 Initial capacity cannot be negative.
		if (__cap < 0)
			throw new IllegalArgumentException("ZZ32");
		
		this.elementData = new Object[__cap];
		this.capacityIncrement = (__inc < 0 ? 0 : __inc);
	}
	
	/**
	 * Initializes the vector using the specified initial capacity.
	 *
	 * @param __cap The initial capacity.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2019/05/13
	 */
	public Vector(int __cap)
		throws IllegalArgumentException
	{
		this(__cap, 0);
	}
	
	/**
	 * Initializes the vector.
	 *
	 * @since 2019/05/13
	 */
	public Vector()
	{
		this(10, 0);
	}
	
	public Vector(Collection<? extends E> __a)
	{
		super();
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/14
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public void add(int __i, E __v)
	{
		synchronized (this)
		{
			int size = this.elementCount;
			if (__i < 0 || __i > size)
				throw new IndexOutOfBoundsException("IOOB");
			
			Object[] elements = this.elementData;
			int cap = elements.length,
				nextsize = size + 1;
			
			// Cannot fit in this array
			Object[] source = elements;
			if (nextsize > cap)
			{
				// Grow the list by a bit
				int newcap = nextsize + Math.max(1, this.capacityIncrement);
				elements = new Object[newcap];
				
				// Copy old stuff over, but only up to the index as needed
				for (int i = 0; i < __i; i++)
					elements[i] = source[i];
			}
			
			// Move down to fit
			for (int i = size - 1, o = size; o > __i; i--, o--)
				elements[o] = source[i];
			
			// Store data here
			elements[__i] = __v;
			
			// Store new information
			this.elementCount = nextsize;
			if (elements != source)
				this.elementData = elements;
			
			// Structurally modified
			this.modCount++;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/14
	 */
	@Override
	public boolean addAll(Collection<? extends E> __c)
	{
		synchronized (this)
		{
			return super.addAll(__c);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/14
	 */
	@Override
	public boolean addAll(int __i, Collection<? extends E> __c)
	{
		synchronized (this)
		{
			return super.addAll(__i, __c);
		}
	}
	
	/**
	 * Adds an element to the end of the vector.
	 *
	 * @param __v The element to add.
	 * @since 2019/05/14
	 */
	public void addElement(E __v)
	{
		synchronized (this)
		{
			this.add(__v);
		}
	}
	
	public int capacity()
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/14
	 */
	@Override
	public void clear()
	{
		synchronized (this)
		{
			// Wipe the length
			this.elementCount = 0;
			
			// And wipe the array
			Object[] elements = this.elementData;
			for (int i = 0, n = elements.length; i < n; i++)
				elements[i] = null;
		}
	}
	
	@Override
	public Object clone()
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	@Override
	public boolean contains(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean containsAll(Collection<?> __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	/**
	 * Copies this vector to the given array.
	 *
	 * @param __a The target array.
	 * @throws ArrayStoreException If the array cannot store the vector
	 * values.
	 * @throws IndexOutOfBoundsException If the array is too small.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/24
	 */
	public void copyInto(Object[] __a)
		throws ArrayStoreException, IndexOutOfBoundsException,
			NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// {@squirreljme.error ZZ3q The destination array is too small.}
			int n = this.elementCount;
			if (n > __a.length)
				throw new IndexOutOfBoundsException("ZZ3q");
			
			// Copy into
			Object[] elements = this.elementData;
			for (int i = 0; i < n; i++)
				__a[i] = elements[i];
		}
	}
	
	/**
	 * Returns the element at the given index.
	 *
	 * @param __i The element index.
	 * @return The element.
	 * @since 2019/05/14
	 */
	public E elementAt(int __i)
	{
		synchronized (this)
		{
			return this.get(__i);
		}
	}
	
	/**
	 * Returns an enumeration over the elements.
	 *
	 * @return The element enumeration.
	 * @since 2019/05/14
	 */
	public Enumeration<E> elements()
	{
		return new IteratorToEnumeration<E>(this.iterator());
	}
	
	/**
	 * Ensures that the vector can store the given number of elements.
	 *
	 * @param __n The element capacity.
	 * @since 2019/05/14
	 */
	public void ensureCapacity(int __n)
	{
		synchronized (this)
		{
			// Pointless
			if (__n <= 0)
				return;
			
			// Meets or exceeds the desired capacity?
			Object[] elements = this.elementData;
			int nowl = elements.length;
			if (__n <= nowl)
				return;
			
			// Copy values over
			Object[] extra = new Object[__n];
			for (int i = 0; i < nowl; i++)
				extra[i] = elements[i];
			
			// Set
			this.elementData = extra;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/19
	 */
	@Override
	public boolean equals(Object __o)
	{
		synchronized (this)
		{
			return super.equals(__o);
		}
	}
	
	/**
	 * Returns the first element in the vector.
	 *
	 * @return The first element.
	 * @throws NoSuchElementException If the vector is empty.
	 * @since 2019/05/14
	 */
	@SuppressWarnings({"unchecked"})
	public E firstElement()
		throws NoSuchElementException
	{
		synchronized (this)
		{
			if (this.elementCount <= 0)
				throw new NoSuchElementException("NSEE");
			
			return (E)this.elementData[0];
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/14
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public E get(int __i)
	{
		synchronized (this)
		{
			if (__i < 0 || __i >= this.elementCount)
				throw new ArrayIndexOutOfBoundsException("IOOB");
			
			return (E)this.elementData[__i];
		}
	}
	
	@Override
	public int hashCode()
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/19
	 */
	@Override
	public int indexOf(Object __v)
	{
		synchronized (this)
		{
			return super.indexOf(__v);
		}
	}
	
	public int indexOf(Object __a, int __b)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public void insertElementAt(E __a, int __b)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/14
	 */
	@Override
	public Iterator<E> iterator()
	{
		synchronized (this)
		{
			return new SynchronizedIterator<E>(this, super.iterator());
		}
	}
	
	public E lastElement()
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	@Override
	public int lastIndexOf(Object __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public int lastIndexOf(Object __a, int __b)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/19
	 */
	@Override
	public boolean remove(Object __v)
	{
		synchronized (this)
		{
			return super.remove(__v);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/19
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public E remove(int __i)
		throws ArrayIndexOutOfBoundsException
	{
		synchronized (this)
		{
			// Out of bounds?
			int size = this.elementCount;
			if (__i < 0 || __i >= size)
				throw new ArrayIndexOutOfBoundsException("IOOB");
			
			// Get the old element first
			Object[] elements = this.elementData;
			Object rv = elements[__i];
			
			// Copy all of the elements down
			for (int o = __i, i = __i + 1; i < size; o++, i++)
				elements[o] = elements[i];
			elements[size - 1] = null;
			
			// Set new size
			this.elementCount = size - 1;
			
			// And the old element
			return (E)rv;
		}
	}
	
	@Override
	public boolean removeAll(Collection<?> __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	/**
	 * Removes all elements in the vector.
	 *
	 * @since 2019/05/14
	 */
	public void removeAllElements()
	{
		synchronized (this)
		{
			this.clear();
		}
	}
	
	/**
	 * Removes the given value from the list.
	 *
	 * @param __v The value to remove.
	 * @return If it was removed.
	 * @since 2019/05/19
	 */
	public boolean removeElement(Object __v)
	{
		synchronized (this)
		{
			return this.remove(__v);
		}
	}
	
	/**
	 * Removes the element at the given index.
	 *
	 * @param __i The index to remove.
	 * @throws ArrayIndexOutOfBoundsException If the index is out of bounds.
	 * @since 2019/05/19
	 */
	public void removeElementAt(int __i)
		throws ArrayIndexOutOfBoundsException
	{
		synchronized (this)
		{
			this.remove(__i);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/19
	 */
	@Override
	protected void removeRange(int __a, int __b)
	{
		synchronized (this)
		{
			super.removeRange(__a, __b);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/19
	 */
	@Override
	public boolean retainAll(Collection<?> __c)
	{
		synchronized (this)
		{
			return super.retainAll(__c);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/14
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public E set(int __i, E __v)
	{
		synchronized (this)
		{
			// Out of bounds?
			int size = this.elementCount;
			if (__i < 0 || __i >= size)
				throw new ArrayIndexOutOfBoundsException("IOOB");
			
			// Read old value
			Object[] elements = this.elementData;
			E rv = (E)elements[__i];
			
			// Set new value
			elements[__i] = __v;
			
			// Return old
			return rv;
		}
	}
	
	/**
	 * Sets the element at the given index, note that compared to
	 * {@link #set(int, Object)} the parameters are reversed.
	 *
	 * @param __v The value to set.
	 * @param __i The index to set.
	 * @throws ArrayIndexOutOfBoundsException If the index is out of bounds.
	 * @since 2019/05/14
	 */
	public void setElementAt(E __v, int __i)
		throws ArrayIndexOutOfBoundsException
	{
		synchronized (this)
		{
			this.set(__i, __v);
		}
	}
	
	/**
	 * Sets the size of this vector so that it has the given number of
	 * elements.
	 *
	 * @param __n The number of elements the vector should be.
	 * @throws ArrayIndexOutOfBoundsException If the size is negative.
	 * @since 2019/05/14
	 */
	public void setSize(int __n)
		throws ArrayIndexOutOfBoundsException
	{
		if (__n < 0)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		synchronized (this)
		{
			// Ensure elements can fit first
			this.ensureCapacity(__n);
			
			// Null out any extra elements
			int count = this.elementCount;
			Object[] elements = this.elementData;
			for (int i = count; i < __n; i++)
				elements[i] = null;
			
			// Set new count
			this.elementCount = __n;
			
			// Modified
			this.modCount++;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public int size()
	{
		synchronized (this)
		{
			return this.elementCount;
		}
	}
	
	@Override
	public List<E> subList(int __a, int __b)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public void trimToSize()
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
}

