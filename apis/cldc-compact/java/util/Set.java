// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

public interface Set<E>
	extends Collection<E>
{
	@Override
	public abstract boolean add(E __a);
	
	@Override
	public abstract boolean addAll(Collection<? extends E> __a);
	
	@Override
	public abstract void clear();
	
	@Override
	public abstract boolean contains(Object __a);
	
	@Override
	public abstract boolean containsAll(Collection<?> __a);
	
	@Override
	public abstract boolean equals(Object __a);
	
	@Override
	public abstract int hashCode();
	
	@Override
	public abstract boolean isEmpty();
	
	@Override
	public abstract Iterator<E> iterator();
	
	@Override
	public abstract boolean remove(Object __a);
	
	@Override
	public abstract boolean removeAll(Collection<?> __a);
	
	@Override
	public abstract boolean retainAll(Collection<?> __a);
	
	@Override
	public abstract int size();
	
	@Override
	public abstract Object[] toArray();
	
	@Override
	public abstract <T> T[] toArray(T[] __a);
}


