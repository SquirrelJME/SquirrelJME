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

public abstract class AbstractList<E>
	extends AbstractCollection<E>
	implements List<E>
{
	protected transient int modCount;
	
	protected AbstractList()
	{
		super();
		throw new Error("TODO");
	}
	
	public abstract E get(int __a);
	
	@Override
	public boolean add(E __a)
	{
		throw new Error("TODO");
	}
	
	public void add(int __a, E __b)
	{
		throw new Error("TODO");
	}
	
	public boolean addAll(int __a, Collection<? extends E> __b)
	{
		throw new Error("TODO");
	}
	
	@Override
	public void clear()
	{
		throw new Error("TODO");
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	public int indexOf(Object __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public Iterator<E> iterator()
	{
		throw new Error("TODO");
	}
	
	public int lastIndexOf(Object __a)
	{
		throw new Error("TODO");
	}
	
	public ListIterator<E> listIterator()
	{
		throw new Error("TODO");
	}
	
	public ListIterator<E> listIterator(int __a)
	{
		throw new Error("TODO");
	}
	
	public E remove(int __a)
	{
		throw new Error("TODO");
	}
	
	protected void removeRange(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public E set(int __a, E __b)
	{
		throw new Error("TODO");
	}
	
	public List<E> subList(int __a, int __b)
	{
		throw new Error("TODO");
	}
}

