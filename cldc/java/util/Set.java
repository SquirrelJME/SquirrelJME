// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.util;

public interface Set<E>
	extends Collection<E>
{
	public abstract boolean add(E __a);
	
	public abstract boolean addAll(Collection<? extends E> __a);
	
	public abstract void clear();
	
	public abstract boolean contains(Object __a);
	
	public abstract boolean containsAll(Collection<?> __a);
	
	@Override
	public abstract boolean equals(Object __a);
	
	@Override
	public abstract int hashCode();
	
	public abstract boolean isEmpty();
	
	public abstract Iterator<E> iterator();
	
	public abstract boolean remove(Object __a);
	
	public abstract boolean removeAll(Collection<?> __a);
	
	public abstract boolean retainAll(Collection<?> __a);
	
	public abstract int size();
	
	public abstract Object[] toArray();
	
	public abstract <T> T[] toArray(T[] __a);
}


