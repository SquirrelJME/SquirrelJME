// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This adapts an iterator to an enumeration.
 *
 * @param <E> The element type.
 * @since 2019/05/05
 */
public final class IteratorToEnumeration<E>
	implements Enumeration<E>
{
	/** The iterator to access. */
	private final Iterator<E> _it;
	
	/**
	 * Initializes the iterator adapter.
	 *
	 * @param __it The iterator to adapt.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/05
	 */
	public IteratorToEnumeration(Iterator<E> __it)
		throws NullPointerException
	{
		if (__it == null)
			throw new NullPointerException("NARG");
		
		this._it = __it;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public final boolean hasMoreElements()
	{
		return this._it.hasNext();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public final E nextElement()
		throws NoSuchElementException
	{
		return this._it.next();
	}
}

