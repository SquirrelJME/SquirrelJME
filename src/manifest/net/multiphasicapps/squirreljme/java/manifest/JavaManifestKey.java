// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.manifest;

/**
 * This represents a key which is used in a manifest, it is case insensitive
 * when it comes to ASCII values.
 *
 * @since 2016/05/29
 */
public final class JavaManifestKey
{
	/** The used string. */
	protected final String string;
	
	/**
	 * Initializes the manifest key using the given string.
	 *
	 * @param __s The string to use for the manifest key.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/29
	 */
	public JavaManifestKey(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Lower-case all letters
		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = __s.length(); i < n; i++)
			sb.append(__toLower(__s.charAt(i)));
		this.string = sb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Is another string?
		if (__o instanceof String)
			return __equals((String)__o);
		
		// Is another key?
		else if (__o instanceof JavaManifestKey)
			return __equals(((JavaManifestKey)__o).string);
		
		// Not equals
		else
			return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	public int hashCode()
	{
		return this.string.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	public String toString()
	{
		return this.string;
	}
	
	/**
	 * Compares two strings checking for case insensitivity in the basic
	 * ASCII range.
	 *
	 * @param __b The other string to compare against.
	 * @return {@code true} if the strings are equal.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/29
	 */
	private boolean __equals(String __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Cache
		String a = this.string;
		
		// Get their lengths
		int na = a.length(),
			nb = __b.length();
		
		// Would not be equal
		if (na != nb)
			return false;
		
		// Check characters
		for (int i = 0; i < na; i++)
			if (__toLower(a.charAt(i)) != __toLower(__b.charAt(i)))
				return false;
		
		// Matches
		return true;
	}
	
	/**
	 * Converts the specified character to lower case.
	 *
	 * @param __c The character to lower case.
	 * @return The lowercased character or {@code __c} if it cannot be
	 * lowercased.
	 * @since 2016/05/29
	 */
	private static char __toLower(char __c)
	{
		if (__c <= 'Z' && __c >= 'A')
			return (char)('a' + (__c - 'A'));
		return __c;
	}
}

