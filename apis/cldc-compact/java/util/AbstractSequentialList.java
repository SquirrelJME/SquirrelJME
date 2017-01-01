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

public abstract class AbstractSequentialList<E>
	extends AbstractList<E>
{
	protected AbstractSequentialList()
	{
		super();
		throw new Error("TODO");
	}
	
	@Override
	public abstract ListIterator<E> listIterator(int __a);
	
	@Override
	public void add(int __a, E __b)
	{
		throw new Error("TODO");
	}
	
	@Override
	public boolean addAll(int __a, Collection<? extends E> __b)
	{
		throw new Error("TODO");
	}
	
	@Override
	public E get(int __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public Iterator<E> iterator()
	{
		throw new Error("TODO");
	}
	
	@Override
	public E remove(int __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public E set(int __a, E __b)
	{
		throw new Error("TODO");
	}
}

