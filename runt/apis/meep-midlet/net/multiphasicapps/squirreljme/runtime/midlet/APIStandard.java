// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.midlet;

/**
 * This represents the name of a standard API which implements some kind of
 * interface or otherwise.
 *
 * @since 2016/12/14
 */
public final class APIStandard
	extends API
	implements Comparable<APIStandard>, ManifestedDependency
{
	/**
	 * Initializes the constant in name and version form.
	 *
	 * @param __n The input string.
	 * @throws InvalidMidletException If the name and version form is not
	 * valid.
	 * @since 2016/12/14
	 */
	public APIStandard(String __n)
		throws InvalidMidletException
	{
		super(__n);
	}
	
	/**
	 * Initializes the constant using the given name and version.
	 *
	 * @param __n The API name.
	 * @param __v The API version.
	 * @throws InvalidMidletException If the arguments are not valid.
	 * @since 2016/12/14
	 */
	public APIStandard(String __n, MidletVersion __v)
		throws InvalidMidletException
	{
		super(__n, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/21
	 */
	@Override
	public int compareTo(APIStandard __o)
	{
		return super.__compareTo(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/21
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		// Must be the same class
		if (!(__o instanceof APIStandard))
			return false;
		
		// Forward
		return super.equals(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/27
	 */
	@Override
	public boolean isOptional()
	{
		return false;
	}
}

