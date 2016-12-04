// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

/**
 * This represents the types of namespaces which are avalable for usage.
 *
 * Namespaces may treat their project layouts differently compared to other
 * namespaces.
 *
 * @since 2016/10/28
 */
public enum NamespaceType
{
	/** Static assets such images and other non-compiling files. */
	ASSETS,
	
	/** Projects which are part of the build system. */
	BUILD,
	
	/** Liblets which are only included by midlets and APIs. */
	LIBLETS,
	
	/** Midlets which are actual applications. */
	MIDLETS,
	
	/** APIs which implement configurations, profiles, and standards. */
	APIS,
	
	/** End. */
	;
	
	/** The string representation. */
	protected final String string =
		__lowerCase(name());
	
	/**
	 * {@inheritDoc}
	 * @since 201610/28
	 */
	@Override
	public final String toString()
	{
		return this.string;
	}
	
	/**
	 * Determines the namespace type which is associated with the given
	 * string.
	 *
	 * @param __s The string to convert to a namespace type.
	 * @return The namespace type associated with the given string.
	 * @throws IllegalArgumentException If a type was not found for the
	 * given string.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/28
	 */
	public static NamespaceType of(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Find matching name
		for (NamespaceType t : values())
			if (__s.equals(t.name()) || __s.equals(t.toString()))
				return t;
		
		// {@squirreljme.error AT01 Unknown namespace type. (The input
		// namespace type)}
		throw new IllegalArgumentException(String.format("AT01 %s", __s));
	}
	
	/**
	 * Lowercases all characters in the given string.
	 *
	 * @param __s The string to lowercase.
	 * @return The lowercase form of the given string.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/28
	 */
	private static String __lowerCase(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Lowercase all characters
		int n = __s.length();
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Lowercase
			if (c >= 'A' && c <= 'Z')
				sb.append((char)((c - 'A') + 'a'));
			
			// Keep
			else
				sb.append(c);
		}
		
		// Return it
		return sb.toString();
	}
}

