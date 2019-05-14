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
		// {@squirreljme.error ZZ49 Initial capacity cannot be negative.
		if (__cap < 0)
			throw new IllegalArgumentException("ZZ49");
		
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
	
	@Override
	public void clear()
	{
		throw new todo.TODO();
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
	
	public void copyInto(Object[] __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public E elementAt(int __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public Enumeration<E> elements()
	{
		throw new todo.TODO();
	}
	
	public void ensureCapacity(int __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	@Override
	public boolean equals(Object __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
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
	
	@Override
	public E get(int __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
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
	
	@Override
	public int indexOf(Object __a)
	{
		throw new todo.TODO();
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
	
	@Override
	public boolean isEmpty()
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	@Override
	public Iterator<E> iterator()
	{
		synchronized (this)
		{
			throw new todo.TODO();
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
	
	@Override
	public boolean remove(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public E remove(int __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
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
	
	public void removeAllElements()
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public boolean removeElement(Object __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public void removeElementAt(int __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	@Override
	protected void removeRange(int __a, int __b)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	@Override
	public boolean retainAll(Collection<?> __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	@Override
	public E set(int __a, E __b)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public void setElementAt(E __a, int __b)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public void setSize(int __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
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

