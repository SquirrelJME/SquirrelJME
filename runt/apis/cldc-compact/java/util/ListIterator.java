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

public interface ListIterator<E>
	extends Iterator<E>
{
	public abstract void add(E __a);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public abstract boolean hasNext();
	
	public abstract boolean hasPrevious();
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public abstract E next();
	
	public abstract int nextIndex();
	
	public abstract E previous();
	
	public abstract int previousIndex();
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public abstract void remove();
	
	public abstract void set(E __a);
}

