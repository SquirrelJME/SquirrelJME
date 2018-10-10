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

public abstract class AbstractSet<E>
	extends AbstractCollection<E>
	implements Set<E>
{
	/**
	 * Requires that the class be extended.
	 *
	 * @since 2018/10/10
	 */
	protected AbstractSet()
	{
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean removeAll(Collection<?> __a)
	{
		throw new todo.TODO();
	}
}

