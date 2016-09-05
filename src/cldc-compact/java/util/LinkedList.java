// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.util;

/**
 * A linked list is a list of items which are held together using chains. Each
 * value for an element is placed within a chain link which is then held to
 * other links in the chain.
 *
 * This class has efficient insertion and removal via the iterator interfaces.
 *
 * Random access is not efficient and the further away the element is from the
 * initial sequence the more elements will need to be skipped to access the
 * data.
 *
 * @param <E> The type of element to store.
 * @since 2016/09/05
 */
public class LinkedList<E>
	extends AbstractSequentialList<E>
	implements List<E>, Deque<E>, Cloneable
{
	/** The number of entries in the list. */
	private volatile int _count;
	
	/**
	 * Initializes a linked list with no entries.
	 *
	 * @since 2016/09/05
	 */
	public LinkedList()
	{
	}
	
	/**
	 * Initializes a linked list which contains a copy of all of the elements
	 * of the other collection in its iterator order.
	 *
	 * @param __a The collection to copy elements from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/05
	 */
	public LinkedList(Collection<? extends E> __a)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// Just call addAll
		addAll(__a);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public void addFirst(E __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public void addLast(E __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public Object clone()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public Iterator<E> descendingIterator()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E element()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E getFirst()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E getLast()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public ListIterator<E> listIterator(int __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public boolean offer(E __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public boolean offerFirst(E __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public boolean offerLast(E __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E peek()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E peekFirst()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E peekLast()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E poll()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E pollFirst()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E pollLast()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E pop()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public void push(E __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E remove()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E removeFirst()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public boolean removeFirstOccurrence(Object __a)
	{
		// Start at the start of the list and remove the first match
		Iterator<E> rover = iterator();
		while (rover.hasNext())
		{
			E v = rover.next();
			
			// If this is the same object then remove it
			if (Objects.equals(v, __a))
			{
				rover.remove();
				return true;
			}
		}
		
		// Nothing was removed
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E removeLast()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public boolean removeLastOccurrence(Object __a)
	{
		// Start at the end of the list and remove the first match
		ListIterator<E> rover = listIterator(size());
		while (rover.hasPrevious())
		{
			E v = rover.previous();
			
			// If this is the same object then remove it
			if (Objects.equals(v, __a))
			{
				rover.remove();
				return true;
			}
		}
		
		// Nothing was removed
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public int size()
	{
		return this._count;
	}
}

