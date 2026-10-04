// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

/**
 * This wraps an object and allows comparison of the object by value only.
 *
 * @param <T> The type of value to wrap.
 * @since 2017/12/28
 */
public final class Identity<T>
{
	/** The wrapped object. */
	protected final T value;
	
	/**
	 * Initializes the wrapped object.
	 *
	 * @param __v The value to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	public Identity(T __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		this.value = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof Identity))
			return false;
		
		return this.value == ((Identity<?>)__o).value;
	}
	
	/**
	 * Returns the wrapped object.
	 *
	 * @return The wrapped object.
	 * @since 2017/12/28
	 */
	public final T get()
	{
		return this.value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public final int hashCode()
	{
		return System.identityHashCode(this.value);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@Override
	public final String toString()
	{
		return this.value.toString();
	}
}

