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
 * This wraps the given array as a list for {@link Arrays#asList(Object[])}.
 *
 * @since 2016/08/31
 */
final class __ArraysList__<T>
	extends AbstractList<T>
	implements RandomAccess
{
	/** The array length. */
	protected final int length;
	
	/** The wrapped array. */
	private final T[] _wrapped;
	
	/**
	 * Initializes the wrapped array.
	 *
	 * @param __v The array to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/31
	 */
	__ArraysList__(T[] __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._wrapped = __v;
		this.length = __v.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/31
	 */
	@Override
	public T get(int __i)
	{
		return this._wrapped[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/31
	 */
	@Override
	public T set(int __i, T __v)
	{
		T[] wrapped = this._wrapped;
		T rv = wrapped[__i];
		wrapped[__i] = __v;
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/31
	 */
	@Override
	public int size()
	{
		return this.length;
	}
}

