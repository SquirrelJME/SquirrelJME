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

public interface Collection<E>
	extends Iterable<E>
{
	boolean add(E __a);
	
	boolean addAll(Collection<? extends E> __a);
	
	void clear();
	
	boolean contains(Object __a);
	
	boolean containsAll(Collection<?> __a);
	
	@Override
	boolean equals(Object __a);
	
	@Override
	int hashCode();
	
	boolean isEmpty();
	
	boolean remove(Object __a);
	
	boolean removeAll(Collection<?> __a);
	
	boolean retainAll(Collection<?> __a);
	
	int size();
	
	Object[] toArray();
	
	<T> T[] toArray(T[] __v)
		throws NullPointerException;
}

