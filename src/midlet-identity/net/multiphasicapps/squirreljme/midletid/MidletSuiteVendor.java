// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.midletid;

/**
 * This represents the vendor of a midlet suite.
 *
 * @since 2016/10/12
 */
public final class MidletSuiteVendor
	implements Comparable<MidletSuiteVendor>
{
	/**
	 * Initializes the suite vendor.
	 *
	 * @param __v The value to parse.
	 * @throws IllegalArgumentException If the input is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/12
	 */
	public MidletSuiteVendor(String __v)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public int compareTo(MidletSuiteVendor __o)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
}

