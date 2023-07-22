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
public interface Collection<E>
	extends Iterable<E>
{
	@Api
	boolean add(E __a);
	
	@Api
	boolean addAll(Collection<? extends E> __a);
	
	@Api
	void clear();
	
	@Api
	boolean contains(Object __a);
	
	@Api
	boolean containsAll(Collection<?> __a);
	
	@Override
	boolean equals(Object __a);
	
	@Override
	int hashCode();
	
	@Api
	boolean isEmpty();
	
	@Override
	Iterator<E> iterator();
	
	@Api
	boolean remove(Object __a);
	
	@Api
	boolean removeAll(Collection<?> __a);
	
	@Api
	boolean retainAll(Collection<?> __a);
	
	@Api
	int size();
	
	@Api
	Object[] toArray();
	
	@Api
	<T> T[] toArray(T[] __v)
		throws NullPointerException;
}

