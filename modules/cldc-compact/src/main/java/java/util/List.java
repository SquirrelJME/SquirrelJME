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
	
	void add(int __a, E __b);
	
	boolean addAll(int __a, Collection<? extends E> __b);
	
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
	
	int indexOf(Object __a);
	
	int lastIndexOf(Object __a);
	
	ListIterator<E> listIterator();
	
	ListIterator<E> listIterator(int __a);
	
	E remove(int __a);
	
	E set(int __a, E __b);
	
	List<E> subList(int __a, int __b);
	
	@Override
	<T> T[] toArray(T[] __a);
}

