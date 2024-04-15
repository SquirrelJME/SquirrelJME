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
 * Iterator over the cached set.
 *
 * @param <S> The class type.
 * @since 2018/12/06
 */
final class __ServiceLoaderCachedIterator__<S>
	implements Iterator<S>
{
	/** The search class. */
	private final Class<S> _search;
	
	/** The array to use for this. */
	private final Object[] _items;
	
	/** The next index. */
	private int _next;
	
	/**
	 * Wraps the given array and provides an iterator of it.
	 *
	 * @param __s The search class.
	 * @param __it The iterator to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/06
	 */
	__ServiceLoaderCachedIterator__(Class<S> __s, Object[] __it)
		throws NullPointerException
	{
		if (__s == null || __it == null)
			throw new NullPointerException("NARG");
		
		this._search = __s;
		this._items = __it;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public final boolean hasNext()
	{
		return this._next < this._items.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public final S next()
	{
		Object[] items = this._items;
		int next = this._next;
		
		// No more?
		if (next >= items.length)
			throw new NoSuchElementException("NSEE");
		
		// Get and iterator
		Object rv = items[next];
		this._next = next + 1;
		return this._search.cast(rv);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public final void remove()
	{
		throw new UnsupportedOperationException("RORO");
	}
}
