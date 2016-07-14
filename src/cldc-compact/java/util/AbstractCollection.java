// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.util;

public abstract class AbstractCollection<E>
	implements Collection<E>
{
	protected AbstractCollection()
	{
		throw new Error("TODO");
	}
	
	public abstract Iterator<E> iterator();
	
	public abstract int size();
	
	public boolean add(E __a)
	{
		throw new Error("TODO");
	}
	
	public boolean addAll(Collection<? extends E> __a)
	{
		throw new Error("TODO");
	}
	
	public void clear()
	{
		throw new Error("TODO");
	}
	
	public boolean contains(Object __a)
	{
		throw new Error("TODO");
	}
	
	public boolean containsAll(Collection<?> __a)
	{
		throw new Error("TODO");
	}
	
	public boolean isEmpty()
	{
		throw new Error("TODO");
	}
	
	public boolean remove(Object __a)
	{
		throw new Error("TODO");
	}
	
	public boolean removeAll(Collection<?> __a)
	{
		throw new Error("TODO");
	}
	
	public boolean retainAll(Collection<?> __a)
	{
		throw new Error("TODO");
	}
	
	public Object[] toArray()
	{
		throw new Error("TODO");
	}
	
	public <T> T[] toArray(T[] __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
}

