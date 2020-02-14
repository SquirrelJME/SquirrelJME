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
	public abstract boolean add(E __a);
	
	public abstract void add(int __a, E __b);
	
	public abstract boolean addAll(Collection<? extends E> __a);
	
	public abstract boolean addAll(int __a, Collection<? extends E> __b);
	
	public abstract void clear();
	
	public abstract boolean contains(Object __a);
	
	public abstract boolean containsAll(Collection<?> __a);
	
	@Override
	public abstract boolean equals(Object __a);
	
	/**
	 * Gets the value at the specified index.
	 *
	 * @param __i The index to get.
	 * @return The value at this index.
	 * @throws IndexOutOfBoundsException If the index it out of bounds for the
	 * list.
	 * @since 2018/12/07
	 */
	public abstract E get(int __i)
		throws IndexOutOfBoundsException;
	
	@Override
	public abstract int hashCode();
	
	public abstract int indexOf(Object __a);
	
	public abstract boolean isEmpty();
	
	public abstract Iterator<E> iterator();
	
	public abstract int lastIndexOf(Object __a);
	
	public abstract ListIterator<E> listIterator();
	
	public abstract ListIterator<E> listIterator(int __a);
	
	public abstract boolean remove(Object __a);
	
	public abstract E remove(int __a);
	
	public abstract boolean removeAll(Collection<?> __a);
	
	public abstract boolean retainAll(Collection<?> __a);
	
	public abstract E set(int __a, E __b);
	
	public abstract int size();
	
	public abstract List<E> subList(int __a, int __b);
	
	public abstract Object[] toArray();
	
	public abstract <T> T[] toArray(T[] __a);
}

