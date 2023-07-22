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

@Api
public interface List<E>
	extends Collection<E>
{
	@Override
	boolean add(E __a);
	
	@Api
	void add(int __a, E __b);
	
	@Override
	boolean addAll(Collection<? extends E> __a);
	
	@Api
	boolean addAll(int __a, Collection<? extends E> __b);
	
	@Override
	void clear();
	
	@Override
	boolean contains(Object __a);
	
	@Override
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
	@Api
	E get(int __i)
		throws IndexOutOfBoundsException;
	
	@Override
	int hashCode();
	
	@Api
	int indexOf(Object __a);
	
	@Override
	boolean isEmpty();
	
	@Override
	Iterator<E> iterator();
	
	@Api
	int lastIndexOf(Object __a);
	
	@Api
	ListIterator<E> listIterator();
	
	@Api
	ListIterator<E> listIterator(int __a);
	
	@Override
	boolean remove(Object __a);
	
	@Api
	E remove(int __a);
	
	@Override
	boolean removeAll(Collection<?> __a);
	
	@Override
	boolean retainAll(Collection<?> __a);
	
	@Api
	E set(int __a, E __b);
	
	@Override
	int size();
	
	@Api
	List<E> subList(int __a, int __b);
	
	@Override
	Object[] toArray();
	
	@Override
	<T> T[] toArray(T[] __a);
}

