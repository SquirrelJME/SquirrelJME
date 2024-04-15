// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This is a hiding iterator which masks functionality.
 *
 * @param <T> The type.
 * @since 2020/06/21
 */
final class __HideIterator__<T>
	implements Iterator<T>
{
	/** The iterator being hidden. */
	private final Iterator<T> _iterator;
	
	/**
	 * Initializes the hiding iterator.
	 * 
	 * @param __iterator The iterator to hide.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/21
	 */
	public __HideIterator__(Iterator<T> __iterator)
		throws NullPointerException
	{
		if (__iterator == null)
			throw new NullPointerException("NARG");
		
		this._iterator = __iterator;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/21
	 */
	@Override
	public boolean hasNext()
	{
		return this._iterator.hasNext();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/21
	 */
	@Override
	public T next()
	{
		return this._iterator.next();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/21
	 */
	@Override
	public void remove()
	{
		this._iterator.remove();
	}
}
