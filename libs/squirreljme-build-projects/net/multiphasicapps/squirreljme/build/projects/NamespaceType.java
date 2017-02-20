// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
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
	ASSET,
	
	/** Projects which are part of the build system. */
	BUILD,
	
	/** Liblets which are only included by midlets and APIs. */
	LIBLET,
	
	/** Midlets which are actual applications. */
	MIDLET,
	
	/** APIs which implement configurations, profiles, and standards. */
	API,
	
	/** End. */
	;
	
	/** The string representation. */
	protected final String string =
		__lowerCase(name());
	
	/**
	 * Checks whether this namespace type can depend on a project that has
	 * the given namespace type.
	 *
	 * @param __o The other type to check.
	 * @return {@code true} if the type can be dependend upon.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/21
	 */
	public boolean canDependOn(NamespaceType __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Depends on this type
		switch (this)
		{
				// These can never depend on anything else
			case ASSET:
			case BUILD:
				return false;
				
				// Standard stuff
			case MIDLET:
			case LIBLET:
			case API:
				switch (__o)
				{
						// Only liblets and APIs can be depended upon, which
						// means midlets cannot bring in other midlets
					case LIBLET:
					case API:
						return true;
					
						// No
					default:
						return false;
				}
			
				// Oops
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
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

