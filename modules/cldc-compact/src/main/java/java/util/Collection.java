// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;

public interface Collection<E>
	extends Iterable<E>
{
	boolean add(E __a);
	
	boolean addAll(Collection<? extends E> __a);
	
	void clear();
	
	boolean contains(Object __a);
	
	@Api
	boolean containsAll(Collection<?> __a);
	
	@Api
	@Override
	boolean equals(Object __a);
	
	@Api
	@Override
	int hashCode();
	
	boolean isEmpty();
	
	@Override
	Iterator<E> iterator();
	
	boolean remove(Object __a);
	
	@Api
	boolean removeAll(Collection<?> __a);
	
	@Api
	boolean retainAll(Collection<?> __a);
	
	int size();
	
	Object[] toArray();
	
	<T> T[] toArray(T[] __v)
		throws NullPointerException;
}

