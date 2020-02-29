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

public interface List<E>
	extends Collection<E>
{
	boolean add(E __a);
	
	void add(int __a, E __b);
	
	boolean addAll(Collection<? extends E> __a);
	
	boolean addAll(int __a, Collection<? extends E> __b);
	
	void clear();
	
	boolean contains(Object __a);
	
	boolean containsAll(Collection<?> __a);
	
	@Override
	boolean equals(Object __a);
	
	/**
	 * Gets the value at the specified index.
	 *
	 * @param __i The index to get.
	 * @return The value at this index.
	 * @throws IndexOutOfBoundsException If the index it out of bounds for the
	 * list.
	 * @since 2018/12/07
	 */
	E get(int __i)
		throws IndexOutOfBoundsException;
	
	@Override
	int hashCode();
	
	int indexOf(Object __a);
	
	boolean isEmpty();
	
	Iterator<E> iterator();
	
	int lastIndexOf(Object __a);
	
	ListIterator<E> listIterator();
	
	ListIterator<E> listIterator(int __a);
	
	boolean remove(Object __a);
	
	E remove(int __a);
	
	boolean removeAll(Collection<?> __a);
	
	boolean retainAll(Collection<?> __a);
	
	E set(int __a, E __b);
	
	int size();
	
	List<E> subList(int __a, int __b);
	
	Object[] toArray();
	
	<T> T[] toArray(T[] __a);
}

