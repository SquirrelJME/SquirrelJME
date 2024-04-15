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
public interface Set<E>
	extends Collection<E>
{
	@Override
	boolean add(E __a);
	
	@Override
	boolean addAll(Collection<? extends E> __a);
	
	@Override
	void clear();
	
	@Override
	boolean contains(Object __a);
	
	@Override
	boolean containsAll(Collection<?> __a);
	
	@Override
	boolean equals(Object __a);
	
	@Override
	int hashCode();
	
	@Override
	boolean isEmpty();
	
	@Override
	Iterator<E> iterator();
	
	@Override
	boolean remove(Object __a);
	
	@Override
	boolean removeAll(Collection<?> __a);
	
	@Override
	boolean retainAll(Collection<?> __a);
	
	@Override
	int size();
	
	@Override
	Object[] toArray();
	
	@Override
	<T> T[] toArray(T[] __a);
}


