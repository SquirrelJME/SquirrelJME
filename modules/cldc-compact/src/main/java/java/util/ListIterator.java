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
	void add(E __v);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	boolean hasNext();
	
	boolean hasPrevious();
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	E next();
	
	int nextIndex();
	
	E previous();
	
	int previousIndex();
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	void remove();
	
	void set(E __v);
}

