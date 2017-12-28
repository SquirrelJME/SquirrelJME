// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

/**
 * This wraps an object and allows comparison of the object by value only.
 *
 * @param <T> The type of value to wrap.
 * @since 2017/12/28
 */
final class __IdentityWrapper__<T>
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
	__IdentityWrapper__(T __v)
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
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof __IdentityWrapper__))
			return false;
		
		return this.value == ((__IdentityWrapper__<?>)__o).value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public int hashCode()
	{
		return this.value.hashCode();
	}
}

