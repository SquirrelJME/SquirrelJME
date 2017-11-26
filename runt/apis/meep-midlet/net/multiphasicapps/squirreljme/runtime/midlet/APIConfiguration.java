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
 * This represents a configuration such as CLDC which specifies which base
 * classes are available.
 *
 * @since 2016/12/14
 */
public final class APIConfiguration
	extends API
	implements Comparable<APIConfiguration>, ManifestedDependency
{
	/**
	 * Initializes the constant in name and version form.
	 *
	 * @param __n The input string.
	 * @throws InvalidMidletException If the name and version form is not
	 * valid.
	 * @since 2016/12/14
	 */
	public APIConfiguration(String __n)
		throws InvalidMidletException
	{
		super(APIConfiguration.__normalizeVariant(__n));
	}
	
	/**
	 * Initializes the constant using the given name and version.
	 *
	 * @param __n The API name.
	 * @param __v The API version.
	 * @throws InvalidMidletException If the arguments are not valid.
	 * @since 2016/12/14
	 */
	public APIConfiguration(String __n, MidletVersion __v)
		throws InvalidMidletException
	{
		super(__n, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/21
	 */
	@Override
	public int compareTo(APIConfiguration __o)
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
		if (!(__o instanceof APIConfiguration))
			return false;
		
		// Forward
		return super.equals(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/23
	 */
	@Override
	public boolean isOptional()
	{
		return false;
	}
	
	/**
	 * Parses the list of configurations.
	 *
	 * @param __s The string to parse for configurations.
	 * @return An array containing parsed configurations.
	 * @throws InvalidMidletException If the API is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/26
	 */
	public static final APIConfiguration[] parseList(String __s)
		throws InvalidMidletException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Split input strings with spaces
		String[] split = StringUtils.basicSplit("\0 \t\r\n", __s);
		
		// Parse profiles for each
		int n = split.length;
		APIConfiguration[] rv = new APIConfiguration[n];
		for (int i = 0; i < n; i++)
			rv[i] = new APIConfiguration(split[i]);
		
		return rv;
	}
	
	/**
	 * Normalizes the variant so that {@code CLDC-1.8-Compact} becomes
	 * {@code CLDC-Compact-1.8}.
	 *
	 * @param __n The string to potentially normalize.
	 * @return The normalized name and variant.
	 * @throws InvalidMidletException If the API is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/21
	 */
	private static String __normalizeVariant(String __n)
		throws InvalidMidletException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AD03 Expected a dash before the end of the
		// string which is also not the last character. (The input string)}
		int ld = __n.lastIndexOf('-'), n = __n.length();
		if (ld < 0 || (ld + 1) >= n)
			throw new InvalidMidletException(String.format("AD03 %s", __n));
		
		// If the character after the dash is a number then the version
		// will directly follow it
		char cad = __n.charAt(ld + 1);
		if (cad >= '0' && cad <= '9')
			return __n;
		
		// {@squirreljme.error AD04 Invalid configuration name, expected
		// another dash. (The input string)}
		int nld = __n.lastIndexOf('-', ld - 1);
		if (nld < 0)
			throw new InvalidMidletException(String.format("AD04 %s", __n));
		
		// Swap around
		return __n.substring(0, nld) + __n.substring(ld) +
			__n.substring(nld, ld);
	}
}

