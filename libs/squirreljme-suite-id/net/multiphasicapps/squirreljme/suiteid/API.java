// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.suiteid;

/**
 * This is the base class for API name and version representations.
 *
 * APIs are not case sensitive.
 *
 * @since 2016/12/14
 */
public abstract class API
{
	/**
	 * Initializes the constant in name and version form.
	 *
	 * @param __n
	 * @throws IllegalArgumentException If the name and version form is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/14
	 */
	API(String __n)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Initializes the constant using the given name and version.
	 *
	 * @param __n The API name.
	 * @param __v The API version.
	 * @throws IllegalArgumentException If the arguments are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/14
	 */
	API(String __n, MidletVersion __v)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__n == null || __v == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/21
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Must also be an API
		if (!(__o instanceof API))
			return false;
		
		// Just use the comparison method
		return __compareTo((API)__o) == 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/21
	 */
	@Override
	public final int hashCode()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/21
	 */
	@Override
	public final String toString()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Compares this API against another so that they are ordered correctly.
	 *
	 * @param __o The other API to compare against.
	 * @return The comparison order.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/21
	 */
	final int __compareTo(API __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

