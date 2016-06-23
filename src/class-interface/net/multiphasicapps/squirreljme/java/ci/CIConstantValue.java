// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci;

/**
 * This represents a constant value.
 *
 * @param <V> The type of value to store.
 * @since 2016/04/24
 */
public abstract class CIConstantValue<V>
	implements CIPoolEntry
{
	/** The stored value. */
	protected final V value;	
	
	/**
	 * Initializes the constant value.
	 *
	 * @param __v The value to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/24
	 */
	CIConstantValue(V __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Set
		value = __v;
	}
	
	/**
	 * Returns the value of this constant.
	 *
	 * @return The constant value.
	 * @since 2016/04/24
	 */
	public final V get()
	{
		return value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public final String toString()
	{
		return value.toString();
	}
}

