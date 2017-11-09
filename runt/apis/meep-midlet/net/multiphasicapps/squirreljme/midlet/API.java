// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.midlet;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is the base class for API name and version representations.
 *
 * APIs are not case sensitive.
 *
 * @since 2016/12/14
 */
public abstract class API
{
	/** The name. */
	protected final String name;
	
	/** The version. */
	protected final MidletVersion version;
	
	/** String reference. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the constant in name and version form.
	 *
	 * @param __n The input string.
	 * @throws IllegalArgumentException If the name and version form is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/14
	 */
	API(String __n)
		throws IllegalArgumentException, NullPointerException
	{
		this(__extractName(__n), __extractVersion(__n));
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
		
		// Set
		this.version = __v;
		
		// Convert name
		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = __n.length(); i < n; i++)
		{
			// Lowercase
			char c = __n.charAt(i);
			if (c >= 'A' && c <= 'Z')
				c = (char)('a' + (c - 'A'));
			
			sb.append(c);
		}
		
		// Set
		this.name = sb.toString();
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
		return this.name.hashCode() ^ this.version.hashCode();
	}
	
	/**
	 * Returns the name of the API.
	 *
	 * @return The API name.
	 * @since 2017/01/21
	 */
	public final String name()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/21
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv =
				this.name + "-" + this.version));
		
		return rv;
	}
	
	/**
	 * Returns the version of the API.
	 *
	 * @return The API version.
	 * @since 2017/01/21
	 */
	public final MidletVersion version()
	{
		return this.version;
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
		
		// Compare name first
		int n = this.name.compareTo(__o.name);
		if (n != 0)
			return n;
		
		// Then the version
		return this.version.compareTo(__o.version);
	}
	
	/**
	 * Extracts the name from the API String.
	 *
	 * @param __n The string to extract from.
	 * @return The name.
	 * @throws IllegalArgumentException If the input form is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/21
	 */
	private static String __extractName(String __n)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AD01 Could not extract the name from the
		// API because it is not in the correct form. (The input string)}
		int ld = __n.lastIndexOf('-');
		if (ld < 0)
			throw new IllegalArgumentException(String.format("AD01 %s", __n));
		
		// Simple split
		return __n.substring(0, ld);
	}
	
	/**
	 * Extracts the version from the API String.
	 *
	 * @param __n The string to extract from.
	 * @return The version.
	 * @throws IllegalArgumentException If the input form is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/21
	 */
	private static MidletVersion __extractVersion(String __n)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
			
		// {@squirreljme.error AD02 Could not extract the version from the
		// API because it is not in the correct form. (The input string)}
		int ld = __n.lastIndexOf('-');
		if (ld < 0)
			throw new IllegalArgumentException(String.format("AD02 %s", __n));
		
		// Split and decode version
		return new MidletVersion(__n.substring(ld + 1));
	}
}

