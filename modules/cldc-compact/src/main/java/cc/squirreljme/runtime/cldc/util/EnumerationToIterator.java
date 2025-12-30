// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Maps an {@link Enumeration} to an {@link Iterator}.
 *
 * @param <E> The element type.
 * @see IteratorToEnumeration
 * @since 2025/12/30
 */
@SquirrelJMEVendorApi
public final class EnumerationToIterator<E>
	implements Iterator<E>
{
	/** The enumeration being wrapped. */
	private final Enumeration<E> _it;
	
	/**
	 * Initializes the wrapper.
	 *
	 * @param __it The iterator to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	public EnumerationToIterator(Enumeration<E> __it)
		throws NullPointerException
	{
		if (__it == null)
			throw new NullPointerException("NARG");
		
		this._it = __it;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public boolean hasNext()
	{
		return this._it.hasMoreElements();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public E next()
	{
		return this._it.nextElement();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("RORO");
	}
}
