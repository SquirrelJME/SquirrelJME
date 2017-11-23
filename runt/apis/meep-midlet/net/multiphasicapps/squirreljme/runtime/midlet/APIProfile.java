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

import net.multiphasicapps.strings.StringUtils;

/**
 * This represents a profile that may be implemented, such as MIDP. Support
 * for profiles may or may not be complete.
 *
 * @since 2016/12/14
 */
public final class APIProfile
	extends API
	implements Comparable<APIProfile>, ManifestedDependency
{
	/**
	 * Initializes the constant in name and version form.
	 *
	 * @param __n The input string.
	 * @throws InvalidMidletException If the name and version form is not
	 * valid.
	 * @since 2016/12/14
	 */
	public APIProfile(String __n)
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
	public APIProfile(String __n, MidletVersion __v)
		throws InvalidMidletException
	{
		super(__n, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/21
	 */
	@Override
	public int compareTo(APIProfile __o)
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
		if (!(__o instanceof APIProfile))
			return false;
		
		// Forward
		return super.equals(__o);
	}
	
	/**
	 * Parses the list of profiles.
	 *
	 * @param __s The string to parse.
	 * @return An array containing parsed profiles.
	 * @throws InvalidMidletException If the API is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/23
	 */
	public static final APIProfile[] parseList(String __s)
		throws InvalidMidletException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Split input strings with spaces
		String[] split = StringUtils.basicSplit("\0 \t\r\n", __s);
		
		// Parse profiles for each
		int n = split.length;
		APIProfile[] rv = new APIProfile[n];
		for (int i = 0; i < n; i++)
			rv[i] = new APIProfile(split[i]);
		
		return rv;
	}
}

